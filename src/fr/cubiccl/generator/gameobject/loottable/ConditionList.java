package fr.cubiccl.generator.gameobject.loottable;

import java.awt.Component;
import java.util.ArrayList;

import fr.cubi.cubigui.CTextArea;
import fr.cubiccl.generator.CommandGenerator;
import fr.cubiccl.generator.gui.component.interfaces.IObjectList;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.loottable.PanelCondition;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Replacement;
import fr.cubiccl.generator.utils.Text;

public class ConditionList implements IObjectList
{
	public ArrayList<LootTableCondition> conditions = new ArrayList<LootTableCondition>();

	@Override
	public boolean addObject(CGPanel panel, int editIndex)
	{
		try
		{
			LootTableCondition c = ((PanelCondition) panel).generate();
			if (editIndex == -1) this.conditions.add(c);
			else this.conditions.set(editIndex, c);
			return true;
		} catch (CommandGenerationException e)
		{
			CommandGenerator.report(e);
			return false;
		}
	}

	@Override
	public CGPanel createAddPanel(int editIndex)
	{
		PanelCondition p = new PanelCondition();
		if (editIndex != -1)
		{
			p.setupFrom(this.conditions.get(editIndex));
			p.setName(new Text("loottable.condition", new Replacement("<index>", Integer.toString(editIndex))));
		} else p.setName(new Text("loottable.condition", new Replacement("<index>", Integer.toString(this.conditions.size() + 1))));
		return p;
	}

	@Override
	public Component getDisplayComponent(int index)
	{
		return new CTextArea(this.conditions.get(index).toString());
	}

	@Override
	public String[] getValues()
	{
		String[] names = new String[this.conditions.size()];
		for (int i = 0; i < names.length; ++i)
			names[i] = new Text("loottable.condition", new Replacement("<index>", Integer.toString(i + 1))).toString();
		return names;
	}

	@Override
	public void removeObject(int index)
	{
		this.conditions.remove(index);
	}

}
