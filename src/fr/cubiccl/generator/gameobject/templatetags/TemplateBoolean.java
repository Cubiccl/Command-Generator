package fr.cubiccl.generator.gameobject.templatetags;

import org.jdom2.Element;

import fr.cubiccl.generator.gameobject.baseobjects.BaseObject;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagBoolean;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.utils.PanelRadio;
import fr.cubiccl.generator.utils.Text;

/** An NBT Tag with boolean values. */
public class TemplateBoolean extends TemplateTag
{

	public TemplateBoolean()
	{
		this(null, Tag.UNKNOWN);
	}

	public TemplateBoolean(String id, byte applicationType, String... applicable)
	{
		super(id, Tag.BOOLEAN, applicationType, applicable);
	}

	/** Creates this NBT Tag with the input value.
	 * 
	 * @param value - The value to set.
	 * @return The created NBT Tag. */
	@SuppressWarnings("deprecation")
	public TagBoolean create(boolean value)
	{
		return new TagBoolean(this, value);
	}

	@Override
	protected CGPanel createPanel(BaseObject<?> object, Tag previousValue)
	{
		PanelRadio p = new PanelRadio(this.description(object), "value", "true", "false");
		if (previousValue != null && (int) previousValue.value() == 0) p.setSelected(1);
		p.setName(new Text(this.id(), false));
		return p;
	}

	@Override
	public TemplateTag fromXML(Element xml)
	{
		this.tagType = Tag.BOOLEAN;
		return super.fromXML(xml);
	}

	@Override
	protected TagBoolean generateTag(BaseObject<?> object, CGPanel panel)
	{
		return this.create(((PanelRadio) panel).getSelected() == 0);
	}

	@Override
	public TagBoolean parseTag(String value, boolean isJson, boolean readUnknown)
	{
		return this.create(value.equals("true"));
	}

}
