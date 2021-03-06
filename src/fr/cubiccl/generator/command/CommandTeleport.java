package fr.cubiccl.generator.command;

import java.awt.GridBagConstraints;

import fr.cubiccl.generator.gameobject.Coordinates;
import fr.cubiccl.generator.gameobject.target.Target;
import fr.cubiccl.generator.gui.component.button.CGCheckBox;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelCoordinates;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelTarget;
import fr.cubiccl.generator.gui.component.textfield.CGEntry;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Replacement;
import fr.cubiccl.generator.utils.Text;
import fr.cubiccl.generator.utils.WrongValueException;

public class CommandTeleport extends Command
{
	private CGCheckBox checkboxXRot, checkboxYRot;
	private CGEntry entryYRot, entryXRot;
	private PanelCoordinates panelCoordinates;
	private PanelTarget panelTarget;

	public CommandTeleport()
	{
		super("teleport", "teleport <entity> <x> <y> <z> [<y-rot> <x-rot>]", 5, 7);
	}

	@Override
	public CGPanel createUI()
	{
		CGPanel panel = new CGPanel();
		GridBagConstraints gbc = panel.createGridBagLayout();

		CGPanel rotation = new CGPanel("tp.rotation");
		GridBagConstraints gbc2 = rotation.createGridBagLayout();
		rotation.add((this.entryYRot = new CGEntry(new Text("tp.rotation.y"), "0", new Text("0 � 360", false))).container, gbc2);
		++gbc2.gridy;
		rotation.add((this.entryXRot = new CGEntry(new Text("tp.rotation.x"), "0", new Text("-90 � 90", false))).container, gbc2);
		++gbc2.gridx;
		--gbc2.gridy;
		rotation.add(this.checkboxYRot = new CGCheckBox("tp.rotation.relative"), gbc2);
		++gbc2.gridy;
		rotation.add(this.checkboxXRot = new CGCheckBox("tp.rotation.relative"), gbc2);

		panel.add(this.labelDescription(), gbc);
		++gbc.gridy;
		panel.add(this.panelTarget = new PanelTarget(PanelTarget.ALL_ENTITIES), gbc);
		++gbc.gridy;
		panel.add(this.panelCoordinates = new PanelCoordinates("tp.coordinates"), gbc);
		++gbc.gridy;
		panel.add(rotation, gbc);

		this.entryXRot.addNumberFilter();
		this.entryYRot.addNumberFilter();

		this.panelCoordinates.addArgumentChangeListener(this);
		this.panelTarget.addArgumentChangeListener(this);

		return panel;
	}

	@Override
	protected void resetUI()
	{
		this.entryXRot.setText("0");
		this.entryYRot.setText("0");
		this.checkboxXRot.setSelected(true);
		this.checkboxXRot.setSelected(true);
	}

	@Override
	protected Text description()
	{
		return this.defaultDescription().addReplacement("<target>", this.panelTarget.displayTarget())
				.addReplacement("<coordinates>", this.panelCoordinates.displayCoordinates());
	}

	@Override
	public String generate() throws CommandGenerationException
	{
		String command = this.id + " " + this.panelTarget.generate().toCommand() + " " + this.panelCoordinates.generate().toCommand();

		String y = this.entryYRot.getText(), x = this.entryXRot.getText();
		boolean rotation = !y.equals("") || !x.equals("");
		if (rotation)
		{
			try
			{
				float f = Float.parseFloat(y);
				if (f < -180 || f > 180) throw new WrongValueException(this.entryYRot.label.getAbsoluteText(), new Text("error.number.bounds", new Replacement(
						"<min>", "-180"), new Replacement("<max>", "180")), y);
			} catch (NumberFormatException e)
			{
				throw new WrongValueException(this.entryYRot.label.getAbsoluteText(), new Text("error.number.bounds", new Replacement("<min>", "-180"),
						new Replacement("<max>", "180")), y);
			}
			try
			{
				float f = Float.parseFloat(x);
				if (f < -90 || f > 90) throw new WrongValueException(this.entryXRot.label.getAbsoluteText(), new Text("error.number.bounds", new Replacement(
						"<min>", "-90"), new Replacement("<max>", "90")), x);
			} catch (NumberFormatException e)
			{
				throw new WrongValueException(this.entryXRot.label.getAbsoluteText(), new Text("error.number.bounds", new Replacement("<min>", "-90"),
						new Replacement("<max>", "90")), x);
			}
		}

		return command + (rotation ? (" " + (this.checkboxYRot.isSelected() ? "~" : "") + y + " " + (this.checkboxXRot.isSelected() ? "~" : "") + x) : "");
	}

	@Override
	protected void readArgument(int index, String argument, String[] fullCommand) throws CommandGenerationException
	{
		// teleport <entity> <x> <y> <z> [<y-rot> <x-rot>]
		if (index == 1) this.panelTarget.setupFrom(new Target().fromString(argument));
		if (index == 2) this.panelCoordinates.setupFrom(new Coordinates().fromString(argument, fullCommand[3], fullCommand[4]));
		if (index == 5 || index == 6)
		{
			if (argument.startsWith("~"))
			{
				argument = argument.substring(1);
				if (index == 5) this.checkboxYRot.setSelected(true);
				if (index == 6) this.checkboxXRot.setSelected(true);
			}
			try
			{
				float f = Float.parseFloat(argument);
				if (index == 5 && f >= -180 && f <= 180) this.entryYRot.setText(argument);
				if (index == 6 && f >= -90 && f <= 90) this.entryXRot.setText(argument);
			} catch (Exception e)
			{}
		}
	}
}
