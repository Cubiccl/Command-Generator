package fr.cubiccl.generator.gui.component;

import java.util.ArrayList;

import javax.swing.JList;
import javax.swing.JScrollPane;

import fr.cubiccl.generator.gui.component.combobox.CListCellRenderer;

public class CList extends JList<String>
{
	private static final long serialVersionUID = -407548264318123185L;

	private ArrayList<String> values;
	public final JScrollPane scrollPane;

	public CList()
	{
		this(new String[0]);
	}

	public CList(String... values)
	{
		super(values);
		this.values = new ArrayList<String>();
		for (String value : values)
			this.values.add(value);
		this.scrollPane = new JScrollPane();
		this.scrollPane.setViewportView(this);
		this.setCellRenderer(new CListCellRenderer());
	}

	public void addValue(String value)
	{
		if (this.values.contains(value)) return;
		this.values.add(value);
		this.reload();
	}

	public String getValue()
	{
		if (this.values.size() == 0 || this.getSelectedIndex() == -1) return null;
		return this.values.get(this.getSelectedIndex());
	}

	private void reload()
	{
		this.setModel(new CList(this.values.toArray(new String[this.values.size()])).getModel());
	}

	public void removeValue(String value)
	{
		if (!this.values.contains(value)) return;
		this.values.remove(value);
		this.reload();
	}

	public void setValues(String... values)
	{
		this.values.clear();
		for (String value : values)
			this.values.add(value);
		this.reload();
	}

	public void clear()
	{
		this.values.clear();
		this.reload();
	}

}
