package fr.cubiccl.generator.gameobject.templatetags;

import java.util.Stack;

import fr.cubiccl.generator.CommandGenerator;
import fr.cubiccl.generator.gameobject.ObjectRegistry;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.utils.IStateListener;
import fr.cubiccl.generator.utils.Text;

public abstract class TemplateTag implements IStateListener<CGPanel>
{
	public static final String[] TYPE_NAMES =
	{ "block", "item", "entity" };

	private String[] applicable;
	/** Need several in case of chest-like recursion */
	private Stack<ITagCreationListener> creationListeners;
	public final String id;
	public final byte type;

	public TemplateTag(String id, byte tagType, String... applicable)
	{
		super();
		this.id = id;
		this.type = tagType;
		this.applicable = applicable;
		this.creationListeners = new Stack<ITagCreationListener>();
		ObjectRegistry.registerTag(this, tagType);
	}

	public void askValue(String objectId, Tag previousValue, ITagCreationListener panelTags)
	{
		this.creationListeners.push(panelTags);
		CommandGenerator.stateManager.setState(this.createPanel(objectId, previousValue), this);
	}

	public boolean canApplyTo(String id)
	{
		for (String app : this.applicable)
		{
			if (app.equals("ANY")) return true;
			if (app.equals(id)) return true;
		}
		return false;
	}

	protected abstract CGPanel createPanel(String objectId, Tag previousValue);

	public Text description()
	{
		return new Text("tag." + TYPE_NAMES[this.type] + "." + this.id);
	}

	public abstract Tag generateTag(CGPanel panel);

	protected boolean isInputValid(CGPanel panel)
	{
		return true;
	}

	@Override
	public boolean shouldStateClose(CGPanel panel)
	{
		if (this.isInputValid(panel))
		{
			this.creationListeners.pop().createTag(this, this.generateTag(panel));
			return true;
		}
		return false;
	}

	public Text title()
	{
		return new Text("tag.title." + this.id);
	}
}
