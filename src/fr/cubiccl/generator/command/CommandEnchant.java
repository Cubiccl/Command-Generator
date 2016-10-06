package fr.cubiccl.generator.command;

import java.awt.GridBagConstraints;

import fr.cubiccl.generator.gui.component.panel.CPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelEnchantment;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelTarget;
import fr.cubiccl.generator.utils.CommandGenerationException;

public class CommandEnchant extends Command
{
	private PanelEnchantment panelEnchant;
	private PanelTarget panelTarget;

	public CommandEnchant()
	{
		super("enchant");
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
		panel.add(this.panelEnchant = new PanelEnchantment(true), gbc);

		return panel;
	}

	@Override
	public String generate() throws CommandGenerationException
	{
		return "/enchant " + this.panelTarget.generateTarget().toCommand() + " " + this.panelEnchant.generateEnchantment().toCommand();
	}

}
