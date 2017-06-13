package fr.cubiccl.generator.command;

import java.awt.GridBagConstraints;

import fr.cubiccl.generator.gameobject.Coordinates;
import fr.cubiccl.generator.gui.component.combobox.OptionCombobox;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelCoordinates;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;

public class CommandTestforblocks extends Command
{
	private OptionCombobox comboboxMode;
	private PanelCoordinates panelCoordinatesSourceStart, panelCoordinatesSourceEnd, panelCoordinatesDestination;

	public CommandTestforblocks()
	{
		super("testforblocks", "testforblocks <pattern-coordinates-start> <pattern-coordinates-end> <test-coordinates> [mode]", 10, 11);
	}

	@Override
	public CGPanel createUI()
	{
		CGPanel panel = new CGPanel();
		GridBagConstraints gbc = panel.createGridBagLayout();

		++gbc.gridwidth;
		panel.add(this.labelDescription(), gbc);
		++gbc.gridy;
		--gbc.gridwidth;
		panel.add(this.panelCoordinatesSourceStart = new PanelCoordinates("testforblocks.source.start"), gbc);
		++gbc.gridx;
		panel.add(this.panelCoordinatesSourceEnd = new PanelCoordinates("testforblocks.source.end"), gbc);
		--gbc.gridx;
		++gbc.gridy;
		panel.add(this.panelCoordinatesDestination = new PanelCoordinates("testforblocks.destination"), gbc);
		++gbc.gridx;
		panel.add(this.comboboxMode = new OptionCombobox("testforblocks.mode", "all", "masked"), gbc);

		this.panelCoordinatesSourceStart.addArgumentChangeListener(this);
		this.panelCoordinatesSourceEnd.addArgumentChangeListener(this);
		this.panelCoordinatesDestination.addArgumentChangeListener(this);

		return panel;
	}

	@Override
	protected void resetUI()
	{
		this.comboboxMode.setValue("all");
	}

	@Override
	protected Text description()
	{
		Text d = this.defaultDescription();
		d.addReplacement("<source_start>", this.panelCoordinatesSourceStart.displayCoordinates());
		d.addReplacement("<source_end>", this.panelCoordinatesSourceEnd.displayCoordinates());
		d.addReplacement("<destination>", this.panelCoordinatesDestination.displayCoordinates());
		return d;
	}

	@Override
	public String generate() throws CommandGenerationException
	{
		return this.id + " " + this.panelCoordinatesSourceStart.generate().toCommand() + " " + this.panelCoordinatesSourceEnd.generate().toCommand() + " "
				+ this.panelCoordinatesDestination.generate().toCommand() + " " + this.comboboxMode.getValue();
	}

	@Override
	protected void readArgument(int index, String argument, String[] fullCommand) throws CommandGenerationException
	{
		// testforblocks <pattern-coordinates-start> <pattern-coordinates-end> <test-coordinates> [mode]
		if (index == 1) this.panelCoordinatesSourceStart.setupFrom(new Coordinates().fromString(argument, fullCommand[2], fullCommand[3]));
		if (index == 4) this.panelCoordinatesSourceEnd.setupFrom(new Coordinates().fromString(argument, fullCommand[5], fullCommand[6]));
		if (index == 7) this.panelCoordinatesDestination.setupFrom(new Coordinates().fromString(argument, fullCommand[8], fullCommand[9]));
		if (index == 10) this.comboboxMode.setValue(argument);
	}

}
