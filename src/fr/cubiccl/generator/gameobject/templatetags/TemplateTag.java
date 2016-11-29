package fr.cubiccl.generator.gameobject.templatetags;

import fr.cubiccl.generator.CommandGenerator;
import fr.cubiccl.generator.gameobject.NamedObject;
import fr.cubiccl.generator.gameobject.ObjectRegistry;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.utils.IStateListener;
import fr.cubiccl.generator.utils.Lang;

public abstract class TemplateTag implements IStateListener<CGPanel>, NamedObject
{
	public static final String[] TYPE_NAMES =
	{ "block", "item", "entity" };

	private String[] applicable;
	public final String id;
	public final int type;

	public TemplateTag(String id, int tagType, String... applicable)
	{
		super();
		this.id = id;
		this.type = tagType;
		this.applicable = applicable;
		ObjectRegistry.registerTag(this, tagType);
	}

	public void askValue()
	{
		CommandGenerator.stateManager.setState(this.createPanel(), this);
	}

	public boolean canApplyTo(String id)
	{
		for (String app : this.applicable)
			if (app.equals(id)) return true;
		return false;
	}

	protected abstract CGPanel createPanel();

	public String description()
	{
		return Lang.translate("tag." + TYPE_NAMES[this.type] + "." + this.id + ".description");
	}

	public abstract Tag generateTag(CGPanel panel);

	@Override
	public String name()
	{
		return Lang.translate("tag." + TYPE_NAMES[this.type] + "." + this.id);
	}
}
