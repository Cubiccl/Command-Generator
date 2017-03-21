package fr.cubiccl.generator.command;

import java.awt.GridBagConstraints;

import fr.cubiccl.generator.gui.component.combobox.OptionCombobox;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;

public class CommandLocate extends Command
{
	private OptionCombobox comboboxStructure;

	public CommandLocate()
	{
		super("locate", "locate <structure>", 2);
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
		panel.add(new CGLabel("locate.select").setHasColumn(true), gbc);
		++gbc.gridx;
		panel.add(this.comboboxStructure = new OptionCombobox("locate", "EndCity", "Fortress", "Mansion", "Mineshaft", "Monument", "Stronghold", "Temple",
				"Village"), gbc);

		return panel;
	}

	@Override
	protected Text description()
	{
		return this.defaultDescription().addReplacement("<structure>", new Text("locate." + this.comboboxStructure.getValue()));
	}

	@Override
	public String generate() throws CommandGenerationException
	{
		return this.id + " " + this.comboboxStructure.getValue();
	}

	@Override
	protected void readArgument(int index, String argument, String[] fullCommand) throws CommandGenerationException
	{
		// locate <structure>
		if (index == 1) this.comboboxStructure.setValue(argument);
	}

}
