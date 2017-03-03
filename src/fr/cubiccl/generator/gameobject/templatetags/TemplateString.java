package fr.cubiccl.generator.gameobject.templatetags;

import java.util.regex.Pattern;

import org.jdom2.Element;

import fr.cubiccl.generator.gameobject.baseobjects.BaseObject;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagString;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.utils.ComboboxPanel;
import fr.cubiccl.generator.gui.component.panel.utils.EntryPanel;
import fr.cubiccl.generator.utils.Text;

public class TemplateString extends TemplateTag
{
	private String[] authorizedValues;

	public TemplateString(String id, byte applicationType, String... applicable)
	{
		super(id, Tag.STRING, applicationType, applicable);
		this.authorizedValues = null;
	}

	@SuppressWarnings("deprecation")
	public TagString create(String value)
	{
		return new TagString(this, value);
	}

	@Override
	protected CGPanel createPanel(BaseObject object, Tag previousValue)
	{
		if (this.authorizedValues != null)
		{
			ComboboxPanel p = new ComboboxPanel(this.description(object), "tag." + this.id(), this.authorizedValues);
			if (previousValue != null) for (int i = 0; i < this.authorizedValues.length; ++i)
				if (this.authorizedValues[i].equals(previousValue.value())) p.combobox.setSelectedIndex(i);
			return p;
		}

		EntryPanel p = new EntryPanel(this.description(object));
		if (previousValue != null) p.entry.setText(((TagString) previousValue).value());
		p.setName(new Text(this.id(), false));
		return p;
	}

	@Override
	public TagString generateTag(BaseObject object, CGPanel panel)
	{
		if (this.authorizedValues != null) return this.create(((ComboboxPanel) panel).combobox.getValue());
		return this.create(((EntryPanel) panel).entry.getText());
	}

	@Override
	public TagString readTag(String value, boolean isJson, boolean readUnknown)
	{
		if (value.startsWith("\"") && value.endsWith("\"")) value = value.substring(1, value.length() - 1);
		return this.create(value.replaceAll(Pattern.quote("\\\""), "\"").replaceAll(Pattern.quote("\\\\"), "\\"));
	}

	public void setValues(String... authorizedValues)
	{
		this.authorizedValues = authorizedValues;
	}

	@Override
	public Element toXML()
	{
		Element root = super.toXML();
		if (this.authorizedValues != null)
		{
			Element values = new Element("strvalues");
			for (String v : this.authorizedValues)
				values.addContent(new Element("v").setText(v));
			root.addContent(values);
		}
		return root;
	}

}
