package fr.cubiccl.generator.gameobject.templatetags;

import fr.cubiccl.generator.gameobject.baseobjects.BaseObject;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagBoolean;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.utils.PanelRadio;
import fr.cubiccl.generator.utils.Text;

public class TemplateBoolean extends TemplateTag
{

	public TemplateBoolean(String id, byte applicationType, String... applicable)
	{
		super(id, Tag.BOOLEAN, applicationType, applicable);
	}

	@Override
	protected CGPanel createPanel(BaseObject object, Tag previousValue)
	{
		PanelRadio p = new PanelRadio(this.description(object), "value", "true", "false");
		if (previousValue != null && (int) previousValue.value() == 0) p.setSelected(1);
		p.setName(new Text(this.id(), false));
		return p;
	}

	@Override
	protected TagBoolean generateTag(BaseObject object, CGPanel panel)
	{
		return new TagBoolean(this, ((PanelRadio) panel).getSelected() == 0);
	}

	@Override
	public TagBoolean readTag(String value, boolean isJson)
	{
		return new TagBoolean(this, value.equals("true"));
	}

}
