package fr.cubiccl.generator.gameobject.templatetags;

import java.text.DecimalFormat;

import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagBigNumber;
import fr.cubiccl.generator.gameobject.tags.TagNumber;
import fr.cubiccl.generator.gui.component.panel.utils.ComboboxPanel;
import fr.cubiccl.generator.gui.component.panel.utils.ConfirmPanel;
import fr.cubiccl.generator.gui.component.panel.utils.EntryPanel;

public class TemplateNumber extends TemplateTag
{
	private double minValue, maxValue;
	private String[] names;
	public final byte numberType;
	private String prefix;
	private int[] values;

	/** @param numberType see {@link TagNumber#INTEGER} */
	public TemplateNumber(String id, byte tagType, byte numberType, String... applicable)
	{
		super(id, tagType, applicable);
		this.numberType = numberType;
		this.minValue = -Double.MAX_VALUE;
		this.maxValue = Double.MAX_VALUE;
		this.names = null;
		this.prefix = null;
		this.values = null;
	}

	/** defaults to integer */
	public TemplateNumber(String id, byte tagType, String... applicable)
	{
		this(id, tagType, TagNumber.INTEGER, applicable);
	}

	@Override
	protected ConfirmPanel createPanel(String objectId, Tag previousValue)
	{
		if (this.numberType == TagNumber.BYTE_BOOLEAN)
		{
			ComboboxPanel p = new ComboboxPanel(this.description(), "value", "true", "false");
			if (previousValue != null && (int) previousValue.value() == 0) p.combobox.setSelectedIndex(1);
			return p;
		}

		if (this.names == null)
		{
			EntryPanel p = new EntryPanel(this.description());
			if (this.isBigNumber()) p.entry.addNumberFilter();
			else p.entry.addIntFilter();
			if (previousValue != null)
			{
				if (this.isBigNumber()) p.entry.setText(new DecimalFormat("#").format(((TagBigNumber) previousValue).value()));
				else p.entry.setText(Integer.toString(((TagNumber) previousValue).value()));
			} else p.entry.setText("0");
			return p;
		}
		ComboboxPanel p = new ComboboxPanel(this.description(), this.prefix, this.names);
		if (previousValue != null)
		{
			int v = ((TagNumber) previousValue).value();
			if (this.values != null) for (int i = 0; i < this.values.length; ++i)
				if (this.values[i] == v)
				{
					v = i;
					break;
				}
			p.combobox.setSelectedIndex(v);
		}
		return p;
	}

	@Override
	public Tag generateTag(ConfirmPanel panel)
	{
		if (this.numberType == TagNumber.BYTE_BOOLEAN) return new TagNumber(this, 1 - ((ComboboxPanel) panel).combobox.getSelectedIndex());

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
		return this.numberType == TagNumber.LONG || this.numberType == TagNumber.FLOAT || this.numberType == TagNumber.DOUBLE;
	}

	@Override
	protected boolean isInputValid(ConfirmPanel panel)
	{
		if (this.numberType == TagNumber.BYTE_BOOLEAN) return true;
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

	public void setBounds(double min, double max)
	{
		this.minValue = min;
		this.maxValue = max;
	}

	public void setNames(String prefix, String[] names)
	{
		this.prefix = prefix;
		this.names = names;
	}

	public void setValues(int... values)
	{
		this.values = values;
	}

}
