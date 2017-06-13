package fr.cubiccl.generator.gui.component.panel.loottable;

import java.awt.Font;
import java.awt.GridBagConstraints;

import javax.swing.SwingConstants;

import fr.cubiccl.generator.gameobject.loottable.LootTable;
import fr.cubiccl.generator.gameobject.loottable.LTPool;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.utils.ListListener;
import fr.cubiccl.generator.gui.component.panel.utils.PanelObjectList;
import fr.cubiccl.generator.utils.Text;

public class PanelLootTable extends CGPanel implements ListListener<LTPool>
{
	private static final long serialVersionUID = -8542900975171049623L;

	private PanelLTDisplay display;
	private PanelObjectList<LTPool> listPools;
	public final LootTable lootTable;

	public PanelLootTable(LootTable lootTable)
	{
		this.lootTable = lootTable;

		CGLabel l = new CGLabel(new Text(lootTable.customName(), false));
		l.setFont(l.getFont().deriveFont(Font.BOLD, 30));
		l.setHorizontalAlignment(SwingConstants.CENTER);

		GridBagConstraints gbc = this.createGridBagLayout();
		gbc.anchor = GridBagConstraints.CENTER;
		this.add(l, gbc);
		++gbc.gridy;
		this.add(new CGLabel("loottable.pools.description"), gbc);
		++gbc.gridy;
		this.add(this.listPools = new PanelObjectList<LTPool>("loottable.pools", "loottable.pool", LTPool.class), gbc);
		++gbc.gridy;
		gbc.fill = GridBagConstraints.NONE;
		this.add(this.display = new PanelLTDisplay(this.lootTable), gbc);

		this.listPools.setValues(this.lootTable.getPools());
		this.listPools.addListListener(this);
	}

	@Override
	public void onAddition(int index, LTPool object)
	{
		this.lootTable.add(object);
		this.display.update();
	}

	@Override
	public void onChange(int index, LTPool object)
	{
		this.lootTable.set(index, object);
		this.display.update();
	}

	@Override
	public void onDeletion(int index, LTPool object)
	{
		this.lootTable.remove(object);
		this.display.update();
	}

}
