package fr.cubiccl.generator.gui.component;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
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
		this.setValues(values);
		this.scrollPane = new CScrollPane(this);
		this.scrollPane.addComponentListener(new ComponentListener()
		{

			@Override
			public void componentHidden(ComponentEvent e)
			{}

			@Override
			public void componentMoved(ComponentEvent e)
			{}

			@Override
			public void componentResized(ComponentEvent e)
			{
				reload();
			}

			@Override
			public void componentShown(ComponentEvent e)
			{}
		});
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
		if (this.scrollPane == null) return;
		String[] shownValues = new String[this.values.size()];
		for (int i = 0; i < shownValues.length; i++)
			shownValues[i] = this.values.get(i).length() < this.scrollPane.getWidth() / this.getFont().getSize() * 2 ? this.values.get(i) : this.values.get(i)
					.substring(0, Math.max(0, this.scrollPane.getWidth() / this.getFont().getSize() * 2 - 3)) + "...";
		super.setValues(shownValues);
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
