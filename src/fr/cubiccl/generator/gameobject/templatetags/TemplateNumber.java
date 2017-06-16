package fr.cubiccl.generator.gameobject.templatetags;

import java.text.DecimalFormat;
import java.util.ArrayList;

import org.jdom2.Element;

import fr.cubiccl.generator.gameobject.baseobjects.BaseObject;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagNumber;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.utils.ComboboxPanel;
import fr.cubiccl.generator.gui.component.panel.utils.EntryPanel;
import fr.cubiccl.generator.gui.component.panel.utils.PanelRadio;
import fr.cubiccl.generator.utils.Text;

/** An NBT Tag with numeral value. */
public class TemplateNumber extends TemplateTag
{
	/** <code>true</code> if this is a Byte NBT Tag, but should work as a Boolean NBT Tag. */
	boolean isByteBoolean = false;
	/** The maximum valid value for this NBT Tag. */
	private double maxValue;
	/** The minimum valid value for this NBT Tag. */
	private double minValue;
	/** If not <code>null</code>, this NBT Tag will show names for its possible values. This represents the ID of the translations to use for these names. */
	private String[] names;
	/** The prefix to use with {@link TemplateNumber#names}. */
	private String prefix;
	/** If not <code>null</code>, the custom values to use with {@link TemplateNumber#names}. If <code>null</code>, 0 to <code>names.length-1</code> will be used. */
	private int[] values;

	public TemplateNumber()
	{
		this(null, Tag.UNKNOWN);
	}

	/** @param numberType see {@link TagNumber#INTEGER} */
	public TemplateNumber(String id, byte applicationType, byte numberType, String... applicable)
	{
		super(id, numberType, applicationType, applicable);
		this.minValue = -Double.MAX_VALUE;
		this.maxValue = Double.MAX_VALUE;
		this.names = null;
		this.prefix = null;
		this.values = null;
	}

	/** defaults to integer */
	public TemplateNumber(String id, byte applicationType, String... applicable)
	{
		this(id, applicationType, Tag.INT, applicable);
	}

	/** Creates this NBT Tag with the input value.
	 * 
	 * @param value - The value to set.
	 * @return The created NBT Tag. */
	@SuppressWarnings("deprecation")
	public TagNumber create(double value)
	{
		return new TagNumber(this, value);
	}

	/** Creates this NBT Tag with the input value.
	 * 
	 * @param value - The value to set.
	 * @return The created NBT Tag. */
	@SuppressWarnings("deprecation")
	public TagNumber create(int value)
	{
		return new TagNumber(this, value);
	}

	@Override
	protected CGPanel createPanel(BaseObject<?> object, Tag previousValue)
	{
		if (this.isByteBoolean)
		{
			PanelRadio p = new PanelRadio(this.description(object), "value", "true", "false");
			if (previousValue != null && (double) previousValue.value() == 0) p.setSelected(1);
			p.setName(new Text(this.id(), false));
			return p;
		}

		if (this.names == null)
		{
			EntryPanel p = new EntryPanel(this.description(object));
			if (this.isBigNumber()) p.entry.addNumberFilter();
			else p.entry.addIntFilter();
			if (previousValue != null)
			{
				if (this.isBigNumber()) p.entry.setText(new DecimalFormat("#").format(((TagNumber) previousValue).value()));
				else p.entry.setText(Integer.toString(((TagNumber) previousValue).valueInt()));
			} else p.entry.setText("0");
			p.setName(new Text(this.id(), false));
			return p;
		}
		ComboboxPanel p = new ComboboxPanel(this.description(object), this.prefix, this.names);
		if (previousValue != null)
		{
			int v = ((TagNumber) previousValue).valueInt();
			if (this.values != null) for (int i = 0; i < this.values.length; ++i)
				if (this.values[i] == v)
				{
					v = i;
					break;
				}
			if (v < this.names.length && v >= 0) p.combobox.setSelectedIndex(v);
		}
		p.setName(new Text(this.id(), false));
		return p;
	}

	@Override
	public TemplateTag fromXML(Element xml)
	{
		super.fromXML(xml);

		if (xml.getChild("intnamed") != null)
		{
			if (xml.getChild("intnamed") != null)
			{
				ArrayList<String> values = new ArrayList<String>();
				for (Element v : xml.getChild("intnamed").getChildren())
					values.add(v.getText());
				this.setNames(xml.getChild("intnamed").getAttributeValue("prefix"), values.toArray(new String[values.size()]));
			}
		}

		if (xml.getChild("byteboolean") != null) this.isByteBoolean = Boolean.parseBoolean(xml.getChildText("byteboolean"));

		return super.fromXML(xml);
	}

	@Override
	protected Tag generateTag(BaseObject<?> object, CGPanel panel)
	{
		if (this.isByteBoolean) return this.create(1 - ((PanelRadio) panel).getSelected());

		if (this.names == null)
		{
			if (this.isBigNumber()) return this.create(Double.parseDouble(((EntryPanel) panel).entry.getText()));
			return this.create(Integer.parseInt(((EntryPanel) panel).entry.getText()));
		}

		int index = ((ComboboxPanel) panel).combobox.getSelectedIndex();
		if (this.values == null) return this.create(index);

		return this.create(this.values[index]);
	}

	/** Getter for {@link TemplateNumber#maxValue}. */
	public double getMaxValue()
	{
		return this.maxValue;
	}

	/** Getter for {@link TemplateNumber#minValue}. */
	public double getMinValue()
	{
		return this.minValue;
	}

	/** Getter for {@link TemplateNumber#values}. */
	public int[] getValues()
	{
		return this.values;
	}

	/** @return <code>true</code> if this NBT Tag has Long, Float or Double values. */
	public boolean isBigNumber()
	{
		return this.tagType == Tag.LONG || this.tagType == Tag.FLOAT || this.tagType == Tag.DOUBLE;
	}

	@Override
	protected boolean isInputValid(BaseObject<?> object, CGPanel panel)
	{
		if (this.isByteBoolean) return true;
		try
		{
			if (this.names == null)
			{
				if (this.isBigNumber()) Double.parseDouble(((EntryPanel) panel).entry.getText());
				else Integer.parseInt(((EntryPanel) panel).entry.getText());
			}
		} catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public Tag parseTag(String value, boolean isJson, boolean readUnknown)
	{
		if (this.tagType != Tag.RANGE && this.tagType != Tag.INT) value = value.substring(0, value.length() - 1);
		else if (this.tagType == Tag.RANGE && ((TemplateRange) this).numberType != Tag.INT) value = value.substring(0, value.length() - 1);
		if (this.isBigNumber()) return this.create(Double.parseDouble(value));
		return this.create(Integer.parseInt(value));
	}

	/** Setter for {@link TemplateNumber#minValue} and {@link TemplateNumber#maxValue}. */
	public void setBounds(double min, double max)
	{
		this.minValue = min;
		this.maxValue = max;
	}

	/** Setter for {@link TemplateNumber#prefix} and {@link TemplateNumber#names}. */
	public void setNames(String prefix, String... names)
	{
		this.prefix = prefix;
		this.names = names;
	}

	/** Setter for {@link TemplateNumber#values}. */
	public void setValues(int... values)
	{
		this.values = values;
	}

	/** @return The suffix to add to the value. */
	public String suffix()
	{
		return TagNumber.SUFFIX[this.tagType];
	}

	@Override
	public Element toXML()
	{
		Element root = super.toXML();
		if (this.names != null)
		{
			Element values = new Element("intnamed");
			values.setAttribute("prefix", this.prefix);
			for (String v : this.names)
				values.addContent(new Element("v").setText(v));
			root.addContent(values);
		}
		if (this.isByteBoolean) root.addContent(new Element("byteboolean").setText("true"));
		return root;
	}

}
