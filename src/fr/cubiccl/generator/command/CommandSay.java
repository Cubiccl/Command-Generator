package fr.cubiccl.generator.command;

import java.awt.GridBagConstraints;

import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.textfield.CGEntry;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;

public class CommandSay extends Command
{
	private CGEntry entryMessage;

	public CommandSay()
	{
		super("say");
	}

	@Override
	public CGPanel createGUI()
	{
		CGPanel panel = new CGPanel();
		GridBagConstraints gbc = panel.createGridBagLayout();

		panel.add(this.labelDescription(), gbc);
		++gbc.gridy;
		panel.add((this.entryMessage = new CGEntry(new Text("say.message"), new Text("say.message"))).container, gbc);

		return panel;
	}

	@Override
	public String generate() throws CommandGenerationException
	{
		return this.id + " " + this.entryMessage.getText();
	}

}
