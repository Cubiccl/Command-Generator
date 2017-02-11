package fr.cubiccl.generator.gui.component.panel;

import java.awt.Component;
import java.util.ArrayList;

import fr.cubi.cubigui.CTabbedPane;
import fr.cubiccl.generator.gui.component.interfaces.ITranslated;
import fr.cubiccl.generator.utils.Text;

public class CGTabbedPane extends CTabbedPane implements ITranslated
{
	private static final long serialVersionUID = 5704525701122893291L;

	private ArrayList<Text> texts = new ArrayList<Text>();

	public void addTab(String titleID, Component component)
	{
		this.addTab(new Text(titleID), component);
	}

	public void addTab(Text title, Component component)
	{
		this.texts.add(title);
		super.addTab(title.toString(), component);
	}

	public Text getTextTitleAt(int index)
	{
		return this.texts.get(index);
	}

	@Override
	public String getTitleAt(int index)
	{
		return super.getTitleAt(index).toString();
	}

	@Override
	public int indexOfTab(String title)
	{
		for (int i = 0; i < this.texts.size(); ++i)
			if (this.texts.get(i).toString().equals(title)) return i;
		return -1;
	}

	@Override
	public void removeTabAt(int index)
	{
		super.remove(index);
		this.texts.remove(index);
	}

	@Override
	public void setTitleAt(int index, String titleID)
	{
		this.setTitleAt(index, new Text(titleID));
	}

	public void setTitleAt(int index, Text title)
	{
		this.texts.set(index, title);
		super.setTitleAt(index, title.toString());
	}

	@Override
	public void updateTranslations()
	{
		for (int i = 0; i < this.getTabCount(); ++i)
			super.setTitleAt(i, this.getTitleAt(i));
	}

}
