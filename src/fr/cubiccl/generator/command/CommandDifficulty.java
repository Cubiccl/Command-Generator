package fr.cubiccl.generator.command;

import java.awt.GridBagConstraints;

import fr.cubiccl.generator.gui.component.combobox.OptionCombobox;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.utils.CommandGenerationException;

public class CommandDifficulty extends Command
{
	private OptionCombobox comboboxDifficulty;

	public CommandDifficulty()
	{
		super("difficulty");
	}

	@Override
	public CGPanel createGUI()
	{
		CGPanel panel = new CGPanel();
		GridBagConstraints gbc = panel.createGridBagLayout();

		++gbc.gridwidth;
		panel.add(this.labelDescription(), gbc);
		++gbc.gridy;
		--gbc.gridwidth;
		panel.add(new CGLabel("difficulty.select").setHasColumn(true), gbc);
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
