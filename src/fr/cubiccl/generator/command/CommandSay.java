package fr.cubiccl.generator.command;

import java.awt.GridBagConstraints;

import fr.cubiccl.generator.gui.component.panel.CPanel;
import fr.cubiccl.generator.gui.component.textfield.CEntry;
import fr.cubiccl.generator.utils.CommandGenerationException;

public class CommandSay extends Command
{
	private CEntry entryMessage;

	public CommandSay()
	{
		super("say");
	}

	@Override
	public CPanel createGUI()
	{
		CPanel panel = new CPanel();
		GridBagConstraints gbc = panel.createGridBagLayout();
		
		panel.add(this.labelDescription(), gbc);
		++gbc.gridy;
		panel.add((this.entryMessage = new CEntry("say.message")).container, gbc);

		return panel;
	}

	@Override
	public String generate() throws CommandGenerationException
	{
		return "/say " + this.entryMessage.getText();
	}

}
