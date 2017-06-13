package fr.cubiccl.generator.gui.component.panel.loottable;

import java.util.ArrayList;

import fr.cubiccl.generator.gameobject.loottable.LTCondition;
import fr.cubiccl.generator.gameobject.loottable.LTCondition.Condition;
import fr.cubiccl.generator.gui.component.panel.utils.PanelObjectList;

public class PanelConditionList extends PanelObjectList<LTCondition>
{
	private static final long serialVersionUID = -4983985911988135738L;

	public PanelConditionList(String titleID, String popupTitleID, Object... properties)
	{
		super(titleID, popupTitleID, LTCondition.class, properties);
	}

	@Override
	public void add(LTCondition object)
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
		for (LTCondition c : this.values())
			conditions.add(c.condition);

		this.properties.set("conditions", conditions);
		this.buttonAdd.setEnabled(conditions.size() != Condition.values().length);
	}

	@Override
	public void set(int index, LTCondition object)
	{
		super.set(index, object);
		this.onListChange();
	}

}
