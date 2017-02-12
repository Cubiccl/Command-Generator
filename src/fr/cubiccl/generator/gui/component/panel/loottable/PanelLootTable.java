package fr.cubiccl.generator.gui.component.panel.loottable;

import fr.cubiccl.generator.gameobject.loottable.LootTable;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.utils.Text;

public class PanelLootTable extends CGPanel
{
	private static final long serialVersionUID = -8542900975171049623L;

	public final LootTable lootTable;

	public PanelLootTable(LootTable lootTable)
	{
		// TODO Auto-generated constructor stub
		this.lootTable = lootTable;
		this.add(new CGLabel(new Text(lootTable.customName(), false)));
	}

}
