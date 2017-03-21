package fr.cubiccl.generator.command;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import fr.cubiccl.generator.gui.component.combobox.OptionCombobox;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;

public class CommandDefaultgamemode extends Command
{
	public static final String[][] acceptable =
	{
	{ "survival", "s", "0" },
	{ "creative", "c", "1" },
	{ "adventure", "a", "2" },
	{ "spectator", "sp", "3" } };
	private OptionCombobox comboboxGamemode;

	public CommandDefaultgamemode()
	{
		super("defaultgamemode", "defaultgamemode <gamemode>", 2);
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
		panel.add(new CGLabel("gamemode.select").setHasColumn(true), gbc);
		++gbc.gridx;
		panel.add(this.comboboxGamemode = new OptionCombobox("gamemode", "survival", "creative", "adventure", "spectator"), gbc);

		this.comboboxGamemode.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				updateTranslations();
			}
		});

		return panel;
	}

	@Override
	protected Text description()
	{
		return this.defaultDescription().addReplacement("<mode>", new Text("gamemode." + this.comboboxGamemode.getValue()));
	}

	@Override
	public String generate() throws CommandGenerationException
	{
		return this.id + " " + this.comboboxGamemode.getValue();
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
						this.comboboxGamemode.setSelectedIndex(i);
						return;
					}
		}
	}

}
