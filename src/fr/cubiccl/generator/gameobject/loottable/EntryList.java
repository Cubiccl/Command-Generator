package fr.cubiccl.generator.gameobject.loottable;

import java.awt.Component;
import java.util.ArrayList;

import fr.cubiccl.generator.gui.component.interfaces.IObjectList;
import fr.cubiccl.generator.gui.component.panel.CGPanel;

public class EntryList implements IObjectList
{
	public ArrayList<LootTableEntry> entries = new ArrayList<LootTableEntry>();

	@Override
	public boolean addObject(CGPanel panel, int editIndex)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public CGPanel createAddPanel(int editIndex)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Component getDisplayComponent(int index)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getValues()
	{
		// TODO Auto-generated method stub
		return new String[0];
	}

	@Override
	public void removeObject(int index)
	{
		// TODO Auto-generated method stub

	}

}
