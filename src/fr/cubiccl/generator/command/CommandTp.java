package fr.cubiccl.generator.command;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import fr.cubiccl.generator.gameobject.Coordinates;
import fr.cubiccl.generator.gameobject.target.Target;
import fr.cubiccl.generator.gui.component.button.CGCheckBox;
import fr.cubiccl.generator.gui.component.combobox.OptionCombobox;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelCoordinates;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelTarget;
import fr.cubiccl.generator.gui.component.textfield.CGEntry;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Replacement;
import fr.cubiccl.generator.utils.Text;
import fr.cubiccl.generator.utils.WrongValueException;

public class CommandTp extends Command implements ActionListener
{
	private CGCheckBox checkboxXRot, checkboxYRot;
	private OptionCombobox comboboxMode;
	private CGEntry entryYRot, entryXRot;
	private PanelCoordinates panelCoordinates;
	private CGPanel panelRotation;
	private PanelTarget panelTarget, panelDestination;

	public CommandTp()
	{
		super("tp", "tp <target player> <destination player>\n" + "tp <player> <x> <y> <z> [<y-rot> <x-rot>]", 3, 5, 7);
	}

	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		boolean coords = this.toCoordinates();
		this.panelDestination.setVisible(!coords);
		this.panelCoordinates.setVisible(coords);
		this.panelRotation.setVisible(coords);
		this.updateTranslations();
	}

	@Override
	public CGPanel createUI()
	{
		CGPanel panel = new CGPanel();
		GridBagConstraints gbc = panel.createGridBagLayout();

		this.panelRotation = new CGPanel("tp.rotation");
		GridBagConstraints gbc2 = this.panelRotation.createGridBagLayout();
		this.panelRotation.add((this.entryYRot = new CGEntry(new Text("tp.rotation.y"), "0", new Text("0 — 360", false))).container, gbc2);
		++gbc2.gridy;
		this.panelRotation.add((this.entryXRot = new CGEntry(new Text("tp.rotation.x"), "0", new Text("-90 — 90", false))).container, gbc2);
		++gbc2.gridx;
		--gbc2.gridy;
		this.panelRotation.add(this.checkboxYRot = new CGCheckBox("tp.rotation.relative"), gbc2);
		++gbc2.gridy;
		this.panelRotation.add(this.checkboxXRot = new CGCheckBox("tp.rotation.relative"), gbc2);

		panel.add(this.labelDescription(), gbc);
		++gbc.gridy;
		panel.add(this.panelTarget = new PanelTarget(PanelTarget.ALL_ENTITIES), gbc);
		++gbc.gridy;
		panel.add(this.comboboxMode = new OptionCombobox("tp.mode", "coordinates", "entity"), gbc);
		++gbc.gridy;
		panel.add(this.panelCoordinates = new PanelCoordinates("tp.coordinates"), gbc);
		panel.add(this.panelDestination = new PanelTarget("tp.destination", PanelTarget.ALL_ENTITIES), gbc);
		++gbc.gridy;
		panel.add(this.panelRotation, gbc);

		this.panelCoordinates.setRelativeText(new Text("tp.relative.entity"));
		this.panelDestination.setVisible(false);
		this.comboboxMode.addActionListener(this);

		this.entryXRot.addNumberFilter();
		this.entryYRot.addNumberFilter();

		this.panelTarget.addArgumentChangeListener(this);
		this.panelCoordinates.addArgumentChangeListener(this);
		this.panelDestination.addArgumentChangeListener(this);

		return panel;
	}

	@Override
	protected void resetUI()
	{
		this.entryXRot.setText("0");
		this.entryYRot.setText("0");
		this.checkboxXRot.setSelected(true);
		this.checkboxYRot.setSelected(true);
	}

	@Override
	protected Text description()
	{
		if (this.toCoordinates()) return this.defaultDescription().addReplacement("<target>", this.panelTarget.displayTarget())
				.addReplacement("<destination>", this.panelCoordinates.displayCoordinates());
		return this.defaultDescription().addReplacement("<target>", this.panelTarget.displayTarget())
				.addReplacement("<destination>", this.panelDestination.displayTarget());
	}

	@Override
	public String generate() throws CommandGenerationException
	{
		String command = this.id + " " + this.panelTarget.generate().toCommand() + " ";
		if (!this.toCoordinates()) return command + this.panelDestination.generate().toCommand();

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

		return command + this.panelCoordinates.generate().toCommand() + " "
				+ (rotation ? (" " + (this.checkboxYRot.isSelected() ? "~" : "") + y + " " + (this.checkboxXRot.isSelected() ? "~" : "") + x) : "");
	}

	@Override
	protected void readArgument(int index, String argument, String[] fullCommand) throws CommandGenerationException
	{
		// tp <target player> <destination player>
		// tp <player> <x> <y> <z> [<y-rot> <x-rot>]

		if (index == 1) this.panelTarget.setupFrom(new Target().fromString(argument));
		if (index == 2)
		{
			boolean entity = fullCommand.length == 3;
			this.panelDestination.setVisible(entity);
			this.panelCoordinates.setVisible(!entity);
			this.panelRotation.setVisible(!entity);
			if (entity) this.panelDestination.setupFrom(new Target().fromString(argument));
			else this.panelCoordinates.setupFrom(new Coordinates().fromString(argument, fullCommand[3], fullCommand[4]));
		}
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

	private boolean toCoordinates()
	{
		return this.comboboxMode.getValue().equals("coordinates");
	}
}
