package fr.cubiccl.generator.gui.component.panel.loottable;

import java.awt.Font;
import java.awt.GridBagConstraints;

import javax.swing.SwingConstants;

import fr.cubiccl.generator.gameobject.loottable.LootTable;
import fr.cubiccl.generator.gameobject.loottable.LootTablePool;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.utils.ListListener;
import fr.cubiccl.generator.gui.component.panel.utils.PanelObjectList;
import fr.cubiccl.generator.utils.Text;

public class PanelLootTable extends CGPanel implements ListListener<LootTablePool>
{
	private static final long serialVersionUID = -8542900975171049623L;

	private PanelObjectList<LootTablePool> listPools;
	public final LootTable lootTable;

	public PanelLootTable(LootTable lootTable)
	{
		this.lootTable = lootTable;

		CGLabel l = new CGLabel(new Text(lootTable.customName(), false));
		l.setFont(l.getFont().deriveFont(Font.BOLD, 30));
		l.setHorizontalAlignment(SwingConstants.CENTER);

		GridBagConstraints gbc = this.createGridBagLayout();
		this.add(l, gbc);
		++gbc.gridy;
		this.add(new CGLabel("loottable.pools.description"), gbc);
		++gbc.gridy;
		this.add(this.listPools = new PanelObjectList<LootTablePool>("loottable.pools", "loottable.pool", LootTablePool.class), gbc);

		this.listPools.setValues(this.lootTable.pools.toArray(new LootTablePool[this.lootTable.pools.size()]));
		this.listPools.addListListener(this);
	}

	@Override
	public void onAddition(int index, LootTablePool object)
	{
		this.lootTable.pools.add(object);
	}

	@Override
	public void onChange(int index, LootTablePool object)
	{
		this.lootTable.pools.set(index, object);
	}

	@Override
	public void onDeletion(int index, LootTablePool object)
	{
		this.lootTable.pools.remove(object);
	}

}
