package fr.cubiccl.generator.command;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
		super("tp");
	}

	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		boolean coords = this.comboboxMode.getValue().equals("coordinates");
		this.panelDestination.setVisible(!coords);
		this.panelCoordinates.setVisible(coords);
		this.panelRotation.setVisible(coords);
	}

	@Override
	public CGPanel createGUI()
	{
		CGPanel panel = new CGPanel();
		GridBagConstraints gbc = panel.createGridBagLayout();

		this.panelRotation = new CGPanel("tp.rotation");
		GridBagConstraints gbc2 = this.panelRotation.createGridBagLayout();
		this.panelRotation.add((this.entryYRot = new CGEntry("tp.rotation.y", "0")).container, gbc2);
		++gbc2.gridy;
		this.panelRotation.add((this.entryXRot = new CGEntry("tp.rotation.x", "0")).container, gbc2);
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

		return panel;
	}

	@Override
	public String generate() throws CommandGenerationException
	{
		String command = "/tp " + this.panelTarget.generateTarget().toCommand() + " ";
		if (this.comboboxMode.getValue().equals("entity")) return command + this.panelDestination.generateTarget().toCommand();

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

		return command + this.panelCoordinates.generateCoordinates().toCommand() + " "
				+ (rotation ? (" " + (this.checkboxYRot.isSelected() ? "~" : "") + y + " " + (this.checkboxXRot.isSelected() ? "~" : "") + x) : "");
	}
}
