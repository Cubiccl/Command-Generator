package fr.cubiccl.generator.command;

import java.awt.GridBagConstraints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.textfield.CGEntry;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;

public class CommandFunction extends Command
{
	private CGEntry entryMessage;

	public CommandFunction()
	{
		super("function", "function <path>", 2);
	}

	@Override
	public CGPanel createGUI()
	{
		CGPanel panel = new CGPanel();
		GridBagConstraints gbc = panel.createGridBagLayout();

		panel.add(this.labelDescription(), gbc);
		++gbc.gridy;
		panel.add((this.entryMessage = new CGEntry(new Text("function.path"), new Text("function.path"))).container, gbc);

		this.entryMessage.addKeyListener(new KeyListener()
		{
			@Override
			public void keyPressed(KeyEvent e)
			{}

			@Override
			public void keyReleased(KeyEvent e)
			{
				updateTranslations();
			}

			@Override
			public void keyTyped(KeyEvent e)
			{}
		});

		return panel;
	}

	@Override
	protected Text description()
	{
		return this.defaultDescription();
	}

	@Override
	public String generate() throws CommandGenerationException
	{
		return this.id + " " + this.entryMessage.getText();
	}

	@Override
	protected void readArgument(int index, String argument, String[] fullCommand) throws CommandGenerationException
	{
		if (index == 1) this.entryMessage.setText(argument);
	}

}
