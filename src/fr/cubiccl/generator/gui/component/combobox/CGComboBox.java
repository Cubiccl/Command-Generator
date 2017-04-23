package fr.cubiccl.generator.gui.component.combobox;

import java.awt.Dimension;

import fr.cubi.cubigui.CComboBox;
import fr.cubi.cubigui.DisplayUtils;

public class CGComboBox extends CComboBox
{
	private static final long serialVersionUID = -5586551752242617407L;

	private String[] values;

	public CGComboBox(String... values)
	{
		super(values);
		this.values = values;
	}

	public String getValue()
	{
		if (this.getSelectedIndex() == -1) return null;
		return this.values[this.getSelectedIndex()];
	}

	public void setValues(String[] values)
	{
		this.values = values;
		int index = this.getSelectedIndex();
		this.setModel(new CGComboBox(this.values).getModel());
		if (index > 0 && index < this.values.length) this.setSelectedIndex(index);

		int textWidth = 0;
		for (String value : this.values)
			textWidth = Math.max(DisplayUtils.textWidth(value), textWidth);
		this.setPreferredSize(new Dimension(Math.max(140, textWidth), 27));
	}

}
