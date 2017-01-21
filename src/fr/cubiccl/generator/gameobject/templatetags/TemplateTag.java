package fr.cubiccl.generator.gameobject.templatetags;

import java.util.Stack;

import fr.cubiccl.generator.CommandGenerator;
import fr.cubiccl.generator.gameobject.baseobjects.BaseObject;
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
	{ "block", "item", "entity" };

	private String[] applicable;
	/** Need several in case of chest-like recursion */
	private Stack<TagCreation> creationListeners;
	private final String id;
	public final byte type;

	public TemplateTag(String id, byte tagType, String... applicable)
	{
		this.id = id;
		this.type = tagType;
		this.applicable = applicable;
		this.creationListeners = new Stack<TagCreation>();

		if (this.type == Tag.BLOCK) ObjectRegistry.blockTags.register(this);
		else if (this.type == Tag.ITEM) ObjectRegistry.itemTags.register(this);
		else if (this.type == Tag.ENTITY) ObjectRegistry.entityTags.register(this);
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
		for (String app : this.applicable)
		{
			if (app.equals("ANY")) return true;
			if (app.replaceAll("minecraft:", "").equals(object.id().replaceAll("minecraft:", ""))) return true;
		}
		return false;
	}

	protected abstract CGPanel createPanel(BaseObject object, Tag previousValue);

	/** @param object - The Object this Tag is applied to.
	 * @return A description of this NBT Tag. */
	public Text description(BaseObject object)
	{
		String objectSpecific = "tag." + TYPE_NAMES[this.type] + "." + this.id + "." + object.id();
		if (Lang.keyExists(objectSpecific)) return new Text(objectSpecific, new Replacement("<o>", object.name()));
		return new Text("tag." + TYPE_NAMES[this.type] + "." + this.id, new Replacement("<o>", object.name()));
	}

	/** @param object - The Object this Tag is applied to.
	 * @return The generated Tag. */
	protected abstract Tag generateTag(BaseObject object, CGPanel panel);

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
}
