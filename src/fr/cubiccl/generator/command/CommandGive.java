package fr.cubiccl.generator.command;

import java.awt.GridBagConstraints;

import fr.cubiccl.generator.gameobject.ItemStack;
import fr.cubiccl.generator.gui.component.panel.CPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelItem;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelTarget;
import fr.cubiccl.generator.utils.CommandGenerationException;

public class CommandGive extends Command
{
	private PanelTarget panelTarget;
	private PanelItem panelItem;

	public CommandGive()
	{
		super("give");
	}

	@Override
	public CPanel createGUI()
	{
		CPanel panel = new CPanel();
		GridBagConstraints gbc = panel.createGridBagLayout();

		panel.add(this.labelDescription(), gbc);
		++gbc.gridy;
		panel.add(this.panelTarget = new PanelTarget(PanelTarget.PLAYERS_ONLY), gbc);
		++gbc.gridy;
		panel.add(this.panelItem = new PanelItem("give.item"), gbc);

		return panel;
	}

	@Override
	public String generate() throws CommandGenerationException
	{
		ItemStack item = this.panelItem.generateItem();
		return "/give " + this.panelTarget.generateTarget().toCommand() + " " + item.item.idString + " " + item.amount + " " + item.data;
	}

}
