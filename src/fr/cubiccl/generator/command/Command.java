package fr.cubiccl.generator.command;

import javax.swing.BorderFactory;

import fr.cubiccl.generator.gui.component.label.CLabel;
import fr.cubiccl.generator.gui.component.panel.CPanel;
import fr.cubiccl.generator.utils.CommandGenerationException;

public abstract class Command
{
	public final String id;

	public Command(String id)
	{
		this.id = id;
		Commands.registerCommand(this);
	}

	public abstract CPanel createGUI();

	public abstract String generate() throws CommandGenerationException;

	protected CLabel labelDescription()
	{
		CLabel l = new CLabel("command." + this.id);
		l.setBorder(BorderFactory.createRaisedBevelBorder());
		return l;
	}

}
