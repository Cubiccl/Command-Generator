package fr.cubiccl.generator.gameobject.templatetags;

import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagNumber;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.utils.ComboboxPanel;
import fr.cubiccl.generator.gui.component.panel.utils.EntryPanel;

public class TemplateNumber extends TemplateTag
{
	private double minValue, maxValue;
	private String[] names;
	public final int numberType;
	private String prefix;
	private int[] values;

	/** @param numberType see {@link TagNumber#INTEGER} */
	public TemplateNumber(String id, int tagType, int numberType, String... applicable)
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
	public TemplateNumber(String id, int tagType, String... applicable)
	{
		this(id, tagType, TagNumber.INTEGER, applicable);
	}

	@Override
	protected CGPanel createPanel()
	{
		if (this.names == null)
		{
			EntryPanel p = new EntryPanel(this.description());
			p.entry.addIntFilter();
			p.entry.setText("0");
			return p;
		}
		return new ComboboxPanel(this.description(), this.prefix, this.names);
	}

	@Override
	public Tag generateTag(CGPanel panel)
	{
		if (this.names == null) return new TagNumber(this, ((EntryPanel) panel).entry.getText());

		int index = ((ComboboxPanel) panel).combobox.getSelectedIndex();
		if (this.values == null) return new TagNumber(this, Integer.toString(index));

		return new TagNumber(this, Integer.toString(this.values[index]));
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

	@Override
	protected boolean isInputValid(CGPanel panel)
	{
		try
		{
			if (this.names == null) Integer.parseInt(((EntryPanel) panel).entry.getText());
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
