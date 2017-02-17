package fr.cubiccl.generator.gameobject.templatetags;

import java.text.DecimalFormat;

import fr.cubiccl.generator.gameobject.baseobjects.BaseObject;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagBigNumber;
import fr.cubiccl.generator.gameobject.tags.TagNumber;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.utils.ComboboxPanel;
import fr.cubiccl.generator.gui.component.panel.utils.EntryPanel;
import fr.cubiccl.generator.gui.component.panel.utils.PanelRadio;
import fr.cubiccl.generator.utils.Text;

public class TemplateNumber extends TemplateTag
{
	public static byte numberTypeFor(byte type)
	{
		for (int i = 0; i < TagNumber.TYPE_TRANSITION.length; ++i)
			if (TagNumber.TYPE_TRANSITION[i] == type) return (byte) i;
		return type;
	}

	private boolean isByteBoolean;
	private double minValue, maxValue;
	private String[] names;
	private String prefix;
	private int[] values;

	/** @param numberType see {@link TagNumber#INTEGER} */
	public TemplateNumber(String id, byte applicationType, byte numberType, String... applicable)
	{
		super(id, TagNumber.TYPE_TRANSITION[numberType], applicationType, applicable);
		this.isByteBoolean = numberType == TagNumber.BYTE_BOOLEAN;
		this.minValue = -Double.MAX_VALUE;
		this.maxValue = Double.MAX_VALUE;
		this.names = null;
		this.prefix = null;
		this.values = null;
	}

	/** defaults to integer */
	public TemplateNumber(String id, byte applicationType, String... applicable)
	{
		this(id, applicationType, TagNumber.INTEGER, applicable);
	}

	@Override
	protected CGPanel createPanel(BaseObject object, Tag previousValue)
	{
		if (this.isByteBoolean)
		{
			PanelRadio p = new PanelRadio(this.description(object), "value", "true", "false");
			if (previousValue != null && (int) previousValue.value() == 0) p.setSelected(1);
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
				if (this.isBigNumber()) p.entry.setText(new DecimalFormat("#").format(((TagBigNumber) previousValue).value()));
				else p.entry.setText(Integer.toString(((TagNumber) previousValue).value()));
			} else p.entry.setText("0");
			p.setName(new Text(this.id(), false));
			return p;
		}
		ComboboxPanel p = new ComboboxPanel(this.description(object), this.prefix, this.names);
		if (previousValue != null)
		{
			int v = ((TagNumber) previousValue).value();
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
	public Tag generateTag(BaseObject object, CGPanel panel)
	{
		if (this.isByteBoolean) return new TagNumber(this, 1 - ((PanelRadio) panel).getSelected());

		if (this.names == null)
		{
			if (this.isBigNumber()) return new TagBigNumber(this, Double.parseDouble(((EntryPanel) panel).entry.getText()));
			return new TagNumber(this, Integer.parseInt(((EntryPanel) panel).entry.getText()));
		}

		int index = ((ComboboxPanel) panel).combobox.getSelectedIndex();
		if (this.values == null) return new TagNumber(this, index);

		return new TagNumber(this, this.values[index]);
	}

	public double getMaxValue()
	{
		return this.maxValue;
	}

	public double getMinValue()
	{
		return this.minValue;
	}

	public int[] getValues()
	{
		return this.values;
	}

	private boolean isBigNumber()
	{
		return this.tagType == Tag.LONG || this.tagType == Tag.FLOAT || this.tagType == Tag.DOUBLE;
	}

	@Override
	protected boolean isInputValid(BaseObject object, CGPanel panel)
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
	public Tag readTag(String value, boolean isJson, boolean readUnknown)
	{
		if (this.tagType != Tag.INT) value = value.substring(0, value.length() - 1);
		if (this.isBigNumber()) return new TagBigNumber(this, Double.parseDouble(value));
		return new TagNumber(this, Integer.parseInt(value));
	}

	public void setBounds(double min, double max)
	{
		this.minValue = min;
		this.maxValue = max;
	}

	public void setNames(String prefix, String... names)
	{
		this.prefix = prefix;
		this.names = names;
	}

	public void setValues(int... values)
	{
		this.values = values;
	}

}
