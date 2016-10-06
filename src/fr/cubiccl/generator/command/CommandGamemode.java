package fr.cubiccl.generator.command;

import java.awt.GridBagConstraints;

import fr.cubiccl.generator.gui.component.combobox.OptionCombobox;
import fr.cubiccl.generator.gui.component.label.CLabel;
import fr.cubiccl.generator.gui.component.panel.CPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelTarget;
import fr.cubiccl.generator.utils.CommandGenerationException;

public class CommandGamemode extends Command
{
	private OptionCombobox comboboxGamemode;
	private PanelTarget panelTarget;

	public CommandGamemode()
	{
		super("gamemode");
	}

	@Override
	public CPanel createGUI()
	{
		CPanel panel = new CPanel();
		GridBagConstraints gbc = panel.createGridBagLayout();

		++gbc.gridwidth;
		panel.add(this.labelDescription(), gbc);
		++gbc.gridy;
		--gbc.gridwidth;
		panel.add(new CLabel("gamemode.select").setHasColumn(true), gbc);
		++gbc.gridx;
		panel.add(this.comboboxGamemode = new OptionCombobox("gamemode", "survival", "creative", "adventure", "spectator"), gbc);
		--gbc.gridx;
		++gbc.gridy;
		++gbc.gridwidth;
		panel.add(this.panelTarget = new PanelTarget("target.title.player", PanelTarget.PLAYERS_ONLY), gbc);

		return panel;
	}

	@Override
	public String generate() throws CommandGenerationException
	{
		return "/gamemode " + this.comboboxGamemode.getValue() + " " + this.panelTarget.generateTarget().toCommand();
	}

}
