package fr.cubiccl.generator.gui.component.panel.loottable;

import java.util.ArrayList;

import fr.cubiccl.generator.gameobject.loottable.LootTableCondition;
import fr.cubiccl.generator.gameobject.loottable.LootTableCondition.Condition;
import fr.cubiccl.generator.gui.component.panel.utils.PanelObjectList;

public class PanelConditionList extends PanelObjectList<LootTableCondition>
{
	private static final long serialVersionUID = -4983985911988135738L;

	public PanelConditionList(String titleID, String popupTitleID, Object... properties)
	{
		super(titleID, popupTitleID, LootTableCondition.class, properties);
	}

	@Override
	public void add(LootTableCondition object)
	{
		super.add(object);
		this.onListChange();
	}

	@Override
	public void delete(int index)
	{
		super.delete(index);
		this.onListChange();
	}

	private void onListChange()
	{
		ArrayList<Condition> conditions = new ArrayList<Condition>();
		for (LootTableCondition c : this.values())
			conditions.add(c.condition);

		this.properties.set("conditions", conditions);
		this.buttonAdd.setEnabled(conditions.size() != Condition.values().length);
	}

	@Override
	public void set(int index, LootTableCondition object)
	{
		super.set(index, object);
		this.onListChange();
	}

}
