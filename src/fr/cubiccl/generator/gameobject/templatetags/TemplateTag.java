package fr.cubiccl.generator.gameobject.templatetags;

import java.util.Stack;

import org.jdom2.Element;

import fr.cubiccl.generator.CommandGenerator;
import fr.cubiccl.generator.gameobject.baseobjects.BaseObject;
import fr.cubiccl.generator.gameobject.baseobjects.Entity;
import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.utils.IStateListener;
import fr.cubiccl.generator.utils.Lang;
import fr.cubiccl.generator.utils.Replacement;
import fr.cubiccl.generator.utils.Text;

public abstract class TemplateTag extends BaseObject implements IStateListener<CGPanel>
{
	private static class TagCreation
	{
		public final ITagCreationListener listener;
		public final BaseObject object;

		public TagCreation(ITagCreationListener listener, BaseObject object)
		{
			this.listener = listener;
			this.object = object;
		}
	}

	public static final String[] TYPE_NAMES =
	{ "block", "item", "entity", "other", "other" };

	protected String[] applicable;
	public final byte applicationType, tagType;
	public TemplateCompound container = null;
	/** Need several in case of chest-like recursion */
	private Stack<TagCreation> creationListeners;
	public String customTagName = null;
	private final String id;

	public TemplateTag(String id, byte tagType, byte applicationType, String... applicable)
	{
		this.id = id;
		this.tagType = tagType;
		this.applicationType = applicationType;
		this.applicable = applicable;
		this.creationListeners = new Stack<TagCreation>();

		if (this.applicationType == Tag.BLOCK) ObjectRegistry.blockTags.register(this);
		else if (this.applicationType == Tag.ITEM) ObjectRegistry.itemTags.register(this);
		else if (this.applicationType == Tag.ENTITY) ObjectRegistry.entityTags.register(this);
		else if (this.applicationType == Tag.UNAVAILABLE) ObjectRegistry.unavailableTags.register(this);
	}

	/** @param object - The Object this Tag will be applied to.
	 * @param previousValue - The value this Tag previously had.
	 * @param listener - Warned when the creation is complete. */
	public void askValue(BaseObject object, Tag previousValue, ITagCreationListener listener)
	{
		this.creationListeners.push(new TagCreation(listener, object));
		CommandGenerator.stateManager.setState(this.createPanel(object, previousValue), this);
	}

	/** @return True if this tag can be applied to the Object with the input ID. */
	public boolean canApplyTo(BaseObject object)
	{
		if (object == null) return true;
		for (String app : this.applicable)
		{
			if (app.equals("ANY") && object != Entity.PLAYER) return true;
			if (app.startsWith("list=")) return ObjectRegistry.listContains(app.substring("list=".length()), object);
			if (app.replaceAll("minecraft:", "").equals(object.id().replaceAll("minecraft:", ""))) return true;
		}
		return false;
	}

	protected abstract CGPanel createPanel(BaseObject object, Tag previousValue);

	/** @param object - The Object this Tag is applied to.
	 * @return A description of this NBT Tag. */
	public Text description(BaseObject object)
	{
		String d = "tag." + TYPE_NAMES[this.applicationType] + "." + this.id;
		if (this.container != null) d += "." + this.container.id();
		Text t = new Text(d);
		if (object != null)
		{
			String objectSpecific = d + "." + object.id();
			if (Lang.keyExists(objectSpecific)) return new Text(objectSpecific, new Replacement("<o>", object.name()));
			t.addReplacement("<o>", object.name());
		}
		return t;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (!(obj instanceof TemplateTag)) return false;
		TemplateTag o = (TemplateTag) obj;

		return this.tagType == o.tagType && this.id().equals(o.id());
	}

	/** @param object - The Object this Tag is applied to.
	 * @return The generated Tag. */
	protected abstract Tag generateTag(BaseObject object, CGPanel panel);

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
	 * @return True if the user Input is valid. */
	protected boolean isInputValid(BaseObject object, CGPanel panel)
	{
		return true;
	}

	public abstract Tag readTag(String value, boolean isJson, boolean readUnknown);

	@Override
	public boolean shouldStateClose(CGPanel panel)
	{
		TagCreation creation = this.creationListeners.peek();
		if (this.isInputValid(creation.object, panel))
		{
			creation.listener.createTag(this, this.generateTag(creation.object, panel));
			this.creationListeners.pop();
			return true;
		}
		return false;
	}

	public Text title()
	{
		return new Text("tag.title." + this.id);
	}

	@Override
	public Element toXML()
	{
		Element root = new Element("tag");
		root.setAttribute("id", this.id);
		if (this.customTagName != null) root.addContent(new Element("customtype").setText(this.customTagName));
		else root.addContent(new Element("type").setText(Integer.toString(this.tagType)));

		Element applicable = new Element("applicable");
		for (String app : this.applicable)
			applicable.addContent(new Element("app").setText(app));
		root.addContent(applicable);

		return root;
	}
}
