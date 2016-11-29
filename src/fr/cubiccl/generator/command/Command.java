package fr.cubiccl.generator.command;

import javax.swing.BorderFactory;

import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.utils.CommandGenerationException;

public abstract class Command
{
	public final String id;

	public Command(String id)
	{
		this.id = id;
		Commands.registerCommand(this);
	}

	public abstract CGPanel createGUI();

	public abstract String generate() throws CommandGenerationException;

	protected CGLabel labelDescription()
	{
		CGLabel l = new CGLabel("command." + this.id);
		l.setBorder(BorderFactory.createRaisedBevelBorder());
		return l;
	}

}
