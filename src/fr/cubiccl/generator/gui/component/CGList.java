package fr.cubiccl.generator.gui.component;

import java.util.ArrayList;

import fr.cubi.cubigui.CList;

public class CGList extends CList
{
	private static final long serialVersionUID = -407548264318123185L;

	public final CScrollPane scrollPane;
	private ArrayList<String> values;

	public CGList()
	{
		this(new String[0]);
	}

	public CGList(String... values)
	{
		super(values);
		this.values = new ArrayList<String>();
		for (String value : values)
			this.values.add(value);
		this.scrollPane = new CScrollPane(this);
	}

	public void addValue(String value)
	{
		if (this.values.contains(value)) return;
		this.values.add(value);
		this.reload();
	}

	public void clear()
	{
		this.values.clear();
		this.reload();
	}

	public String getValue()
	{
		if (this.values.size() == 0 || this.getSelectedIndex() == -1) return null;
		return this.values.get(this.getSelectedIndex());
	}

	private void reload()
	{
		super.setValues(this.values.toArray(new String[this.values.size()]));
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

}
