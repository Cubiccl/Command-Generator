package fr.cubiccl.generator.gameobject.templatetags;

import fr.cubiccl.generator.gameobject.baseobjects.BaseObject;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagString;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.utils.ComboboxPanel;
import fr.cubiccl.generator.gui.component.panel.utils.EntryPanel;

public class TemplateString extends TemplateTag
{
	private String[] authorizedValues;

	public TemplateString(String id, byte tagType, String... applicable)
	{
		super(id, tagType, applicable);
		this.authorizedValues = null;
	}

	@Override
	protected CGPanel createPanel(BaseObject object, Tag previousValue)
	{
		if (this.authorizedValues != null)
		{
			ComboboxPanel p = new ComboboxPanel(this.description(), "tag." + this.id(), this.authorizedValues);
			if (previousValue != null) for (int i = 0; i < this.authorizedValues.length; ++i)
				if (this.authorizedValues[i].equals(previousValue.value())) p.combobox.setSelectedIndex(i);
			return p;
		}

		EntryPanel p = new EntryPanel(this.description());
		if (previousValue != null) p.entry.setText(((TagString) previousValue).value());
		return p;
	}

	@Override
	public TagString generateTag(BaseObject object, CGPanel panel)
	{
		if (this.authorizedValues != null) return new TagString(this, ((ComboboxPanel) panel).combobox.getValue());
		return new TagString(this, ((EntryPanel) panel).entry.getText());
	}

	public void setValues(String... authorizedValues)
	{
		this.authorizedValues = authorizedValues;
	}

}
