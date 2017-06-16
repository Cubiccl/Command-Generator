package fr.cubiccl.generator.gameobject.templatetags;

import java.util.Stack;

import org.jdom2.Element;

import fr.cubiccl.generator.CommandGenerator;
import fr.cubiccl.generator.gameobject.baseobjects.BaseObject;
import fr.cubiccl.generator.gameobject.baseobjects.Entity;
import fr.cubiccl.generator.gameobject.registries.ObjectCreator;
import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.utils.IStateListener;
import fr.cubiccl.generator.utils.Lang;
import fr.cubiccl.generator.utils.Replacement;
import fr.cubiccl.generator.utils.Text;

/** Describes an NBT Tag. */
public abstract class TemplateTag extends BaseObject<TemplateTag> implements IStateListener<CGPanel>
{
	/** Utility object to store tags being created. */
	private static class TagCreation
	{
		/** What to call when creation is over. */
		public final ITagCreationListener listener;
		/** What Object the Tag is being created for. */
		public final BaseObject<?> object;

		public TagCreation(ITagCreationListener listener, BaseObject<?> object)
		{
			this.listener = listener;
			this.object = object;
		}
	}

	/** Translation IDs for application types. */
	public static final String[] TYPE_NAMES =
	{ "block", "item", "entity", "other", "other" };

	/** The Object IDs this Tag can be applied to. */
	protected String[] applicable;
	/** The type of Object this Tag can be applied to.
	 * 
	 * @see Tag#BLOCK */
	private byte applicationType;
	/** Remembers what to call when this Tag is being created. This has to be a stack, in case there is recursion. */
	private Stack<TagCreation> creationListeners;
	/** The custom type of this Tag. */
	protected String customTagType = null;
	/** This NBT Tag's ID. */
	private String id;
	/** This NBT Tag's type.
	 * 
	 * @see Tag#STRING */
	protected byte tagType;

	public TemplateTag(byte applicationType)
	{
		this(null, Tag.STRING, applicationType);
	}

	public TemplateTag(String id, byte tagType, byte applicationType, String... applicable)
	{
		this.id = id;
		this.tagType = tagType;
		this.applicationType = applicationType;
		this.applicable = applicable;
		this.creationListeners = new Stack<TagCreation>();
	}

	/** Getter for {@link TemplateTag#applicationType}. */
	public byte applicationType()
	{
		return this.applicationType;
	}

	/** Called when creating this Tag. Creates the appropriate UI and shows it.
	 * 
	 * @param object - The Object this Tag will be applied to.
	 * @param previousValue - The value this Tag previously had. <code>null</code> if this Tag wasn't added before.
	 * @param listener - Warned when the creation is complete. */
	public void askValue(BaseObject<?> object, Tag previousValue, ITagCreationListener listener)
	{
		CGPanel p = this.createPanel(object, previousValue);
		if (p == null) return;
		this.creationListeners.push(new TagCreation(listener, object));
		CommandGenerator.stateManager.setState(p, this);
	}

	/** @return True if this tag can be applied to the Object with the input ID. */
	public boolean canApplyTo(BaseObject<?> object)
	{
		if (object == null) return true;
		for (String app : this.applicable)
		{
			if (app.equals("ANY") && object != Entity.PLAYER) return true;
			if (app.startsWith("list=") && ObjectRegistry.listContains(app.substring("list=".length()), object)) return true;
			if (app.replaceAll("minecraft:", "").equals(object.id().replaceAll("minecraft:", ""))) return true;
		}
		return false;
	}

	/** Creates the UI for this NBT Tag's creation.
	 * 
	 * @param object - The Object this Tag will be applied to.
	 * @param previousValue - The value this Tag previously had. <code>null</code> if this Tag wasn't added before.
	 * @return The UI to show to the user. */
	protected abstract CGPanel createPanel(BaseObject<?> object, Tag previousValue);

	/** @param object - The Object this Tag is applied to.
	 * @return A description of this NBT Tag. */
	public Text description(BaseObject<?> object)
	{
		String d = "tag." + TYPE_NAMES[this.applicationType] + "." + this.id;

		Text t = new Text(d);
		if (object != null)
		{
			String objectSpecific = d + "." + object.id();
			if (Lang.keyExists(objectSpecific)) return new Text(objectSpecific, new Replacement("<o>", object.name()));
		}
		t.addReplacement("<o>", Lang.translateObject(object, -1, false));
		return t;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (!(obj instanceof TemplateTag)) return false;
		TemplateTag o = (TemplateTag) obj;

		return this.tagType == o.tagType && this.id().equals(o.id());
	}

	@Override
	public TemplateTag fromXML(Element xml)
	{
		this.id = xml.getAttributeValue("id");
		if (xml.getChild("type") != null) this.tagType = Byte.parseByte(xml.getChildText("type"));
		this.applicable = ObjectCreator.createApplicable(xml.getChild("applicable"));
		if (xml.getChild("customtype") != null) this.customTagType = xml.getChildText("customtype");
		return this;
	}

	/** Generates the NBT Tag after the user validates their choices.
	 * 
	 * @param object - The Object this Tag is applied to.
	 * @param panel - The UI that was interacted with.
	 * @return The generated NBT Tag. */
	protected abstract Tag generateTag(BaseObject<?> object, CGPanel panel);

	/** Getter for {@link TemplateTag#applicable}. */
	public String[] getApplicable()
	{
		return this.applicable;
	}

	@Override
	public String id()
	{
		return this.id;
	}

	/** @param object - The Object this Tag is applied to.
	 * @param panel - The UI that was interacted with.
	 * @return <code>true</code> if the user input is valid. */
	protected boolean isInputValid(BaseObject<?> object, CGPanel panel)
	{
		return true;
	}

	/** Parses the value for this NBT Tag.
	 * 
	 * @param value - The value to parse.
	 * @param isJson - <code>true</code> if the format is strict Json in case more parsing is to be done with the value.
	 * @param readUnknown - <code>true</code> if unknown NBT Tags should be read in case more parsing is to be done with the value.
	 * @return The created NBT Tag. */
	public abstract Tag parseTag(String value, boolean isJson, boolean readUnknown);

	@Override
	public TemplateTag register()
	{
		if (this.applicationType == Tag.BLOCK) ObjectRegistry.blockTags.register(this);
		else if (this.applicationType == Tag.ITEM) ObjectRegistry.itemTags.register(this);
		else if (this.applicationType == Tag.ENTITY) ObjectRegistry.entityTags.register(this);
		return this;
	}

	@Override
	public boolean shouldStateClose(CGPanel panel)
	{
		TagCreation creation = this.creationListeners.peek();
		if (this.isInputValid(creation.object, panel))
		{
			creation.listener.tagCreated(this, this.generateTag(creation.object, panel));
			this.creationListeners.pop();
			return true;
		}
		return false;
	}

	/** Getter for {@link TemplateTag#tagType}. */
	public byte tagType()
	{
		return this.tagType;
	}

	/** @return The Title to be displayed when creating this NBT Tag. */
	public Text title()
	{
		return new Text("tag.title." + this.id);
	}

	@Override
	public Element toXML()
	{
		Element root = new Element("tag");
		root.setAttribute("id", this.id);
		if (this.customTagType != null) root.addContent(new Element("customtype").setText(this.customTagType));
		else root.addContent(new Element("type").setText(Integer.toString(this.tagType)));

		Element applicable = new Element("applicable");
		for (String app : this.applicable)
			applicable.addContent(new Element("app").setText(app));
		root.addContent(applicable);

		return root;
	}
}
