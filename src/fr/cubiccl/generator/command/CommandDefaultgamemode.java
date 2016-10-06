package fr.cubiccl.generator.command;

import java.awt.GridBagConstraints;

import fr.cubiccl.generator.gui.component.combobox.OptionCombobox;
import fr.cubiccl.generator.gui.component.label.CLabel;
import fr.cubiccl.generator.gui.component.panel.CPanel;
import fr.cubiccl.generator.utils.CommandGenerationException;

public class CommandDefaultgamemode extends Command
{
	private OptionCombobox comboboxGamemode;

	public CommandDefaultgamemode()
	{
		super("defaultgamemode");
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

		return panel;
	}

	@Override
	public String generate() throws CommandGenerationException
	{
		return "/defaultgamemode " + this.comboboxGamemode.getValue();
	}

}
