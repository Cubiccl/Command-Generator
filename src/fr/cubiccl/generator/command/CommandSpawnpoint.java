package fr.cubiccl.generator.command;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import fr.cubiccl.generator.gameobject.Coordinates;
import fr.cubiccl.generator.gameobject.target.Target;
import fr.cubiccl.generator.gui.component.button.CGCheckBox;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelCoordinates;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelTarget;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;

public class CommandSpawnpoint extends Command implements ActionListener
{
	private CGCheckBox checkbox;
	private PanelCoordinates panelCoordinates;
	private PanelTarget panelTarget;

	public CommandSpawnpoint()
	{
		super("spawnpoint", "spawnpoint <player> <x> <y> <z>", 2, 5);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		this.onParsingEnd();
	}

	@Override
	public CGPanel createUI()
	{
		CGPanel panel = new CGPanel();
		GridBagConstraints gbc = panel.createGridBagLayout();

		panel.add(this.labelDescription(), gbc);
		++gbc.gridy;
		panel.add(this.panelTarget = new PanelTarget(PanelTarget.PLAYERS_ONLY), gbc);
		++gbc.gridy;
		panel.add(this.checkbox = new CGCheckBox("spawnpoint.current"), gbc);
		++gbc.gridy;
		panel.add(this.panelCoordinates = new PanelCoordinates("spawnpoint.coordinates"), gbc);

		this.checkbox.addActionListener(this);
		this.panelTarget.addArgumentChangeListener(this);
		this.panelCoordinates.addArgumentChangeListener(this);

		return panel;
	}

	@Override
	protected void resetUI()
	{
		this.checkbox.setSelected(true);
		this.panelCoordinates.setVisible(false);
	}

	@Override
	protected Text description()
	{
		return this.defaultDescription().addReplacement("<target>", this.panelTarget.displayTarget())
				.addReplacement("<coordinates>", this.panelCoordinates.displayCoordinates());
	}

	@Override
	protected void onParsingEnd()
	{
		this.panelCoordinates.setVisible(!this.checkbox.isSelected());
	}

	@Override
	public String generate() throws CommandGenerationException
	{
		return this.id + " " + this.panelTarget.generate().toCommand() + (this.checkbox.isSelected() ? "" : " " + this.panelCoordinates.generate().toCommand());
	}

	@Override
	protected void readArgument(int index, String argument, String[] fullCommand) throws CommandGenerationException
	{
		if (index == 1) this.panelTarget.setupFrom(new Target().fromString(argument));
		if (index == 2)
		{
			this.checkbox.setSelected(false);
			this.panelCoordinates.setupFrom(new Coordinates().fromString(argument, fullCommand[3], fullCommand[4]));
		}
	}

}
