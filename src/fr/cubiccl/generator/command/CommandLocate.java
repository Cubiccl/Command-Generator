package fr.cubiccl.generator.command;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import fr.cubiccl.generator.gui.component.combobox.OptionCombobox;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;
import fr.cubiccl.generator.utils.Utils;

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
		panel.add(this.comboboxStructure = new OptionCombobox("locate", Utils.STRUCTURES), gbc);

		this.comboboxStructure.addActionListener(new ActionListener()
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
