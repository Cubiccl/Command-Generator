package fr.cubiccl.generator.command;

import java.awt.GridBagConstraints;

import fr.cubiccl.generator.gui.component.combobox.OptionCombobox;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;

public class CommandDifficulty extends Command
{
	public static final String[][] acceptable =
	{
	{ "peaceful", "p", "0" },
	{ "easy", "e", "1" },
	{ "normal", "n", "2" },
	{ "hard", "h", "3" } };
	private OptionCombobox comboboxDifficulty;

	public CommandDifficulty()
	{
		super("difficulty", "difficulty <difficulty>", 2);
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
	protected Text description()
	{
		return this.defaultDescription().addReplacement("<difficulty>", new Text("difficulty." + this.comboboxDifficulty.getValue()));
	}

	@Override
	public String generate() throws CommandGenerationException
	{
		return this.id + " " + this.comboboxDifficulty.getValue();
	}

	@Override
	protected void readArgument(int index, String argument, String[] fullCommand) throws CommandGenerationException
	{
		if (index == 1)
		{
			for (int i = 0; i < acceptable.length; ++i)
				for (int j = 0; j < acceptable[i].length; ++j)
					if (acceptable[i][j].equals(argument))
					{
						this.comboboxDifficulty.setSelectedIndex(i);
						return;
					}
		}
	}

}
