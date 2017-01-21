package fr.cubiccl.generator.command;

import java.awt.GridBagConstraints;

import fr.cubiccl.generator.gameobject.ItemStack;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelItem;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelTarget;
import fr.cubiccl.generator.utils.CommandGenerationException;

public class CommandGive extends Command
{
	private PanelItem panelItem;
	private PanelTarget panelTarget;

	public CommandGive()
	{
		super("give");
	}

	@Override
	public CGPanel createGUI()
	{
		CGPanel panel = new CGPanel();
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
		return "/give " + this.panelTarget.generateTarget().toCommand() + " " + item.item.id() + " " + item.amount + " " + item.damage + " "
				+ item.nbt.valueForCommand();
	}

}
