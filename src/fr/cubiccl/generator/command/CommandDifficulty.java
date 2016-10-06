package fr.cubiccl.generator.command;

import java.awt.GridBagConstraints;

import fr.cubiccl.generator.gui.component.combobox.OptionCombobox;
import fr.cubiccl.generator.gui.component.label.CLabel;
import fr.cubiccl.generator.gui.component.panel.CPanel;
import fr.cubiccl.generator.utils.CommandGenerationException;

public class CommandDifficulty extends Command
{
	private OptionCombobox comboboxDifficulty;

	public CommandDifficulty()
	{
		super("difficulty");
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
		panel.add(new CLabel("difficulty.select").setHasColumn(true), gbc);
		++gbc.gridx;
		panel.add(this.comboboxDifficulty = new OptionCombobox("difficulty", "peaceful", "easy", "normal", "hard"), gbc);

		return panel;
	}

	@Override
	public String generate() throws CommandGenerationException
	{
		return "/difficulty " + this.comboboxDifficulty.getValue();
	}

}
