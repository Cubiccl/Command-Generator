package fr.cubiccl.generator.command;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import fr.cubiccl.generator.gui.component.button.CCheckBox;
import fr.cubiccl.generator.gui.component.combobox.OptionCombobox;
import fr.cubiccl.generator.gui.component.panel.CPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelCoordinates;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelTarget;
import fr.cubiccl.generator.gui.component.textfield.CEntry;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Lang;
import fr.cubiccl.generator.utils.WrongValueException;

public class CommandTp extends Command implements ActionListener
{
	private CCheckBox checkboxXRot, checkboxYRot;
	private OptionCombobox comboboxMode;
	private CEntry entryYRot, entryXRot;
	private PanelCoordinates panelCoordinates;
	private CPanel panelRotation;
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
	public CPanel createGUI()
	{
		CPanel panel = new CPanel();
		GridBagConstraints gbc = panel.createGridBagLayout();

		this.panelRotation = new CPanel("tp.rotation");
		GridBagConstraints gbc2 = this.panelRotation.createGridBagLayout();
		this.panelRotation.add((this.entryYRot = new CEntry("tp.rotation.y")).container, gbc2);
		++gbc2.gridy;
		this.panelRotation.add((this.entryXRot = new CEntry("tp.rotation.x")).container, gbc2);
		++gbc2.gridx;
		--gbc2.gridy;
		this.panelRotation.add(this.checkboxYRot = new CCheckBox("tp.rotation.relative"), gbc2);
		++gbc2.gridy;
		this.panelRotation.add(this.checkboxXRot = new CCheckBox("tp.rotation.relative"), gbc2);

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

		this.panelCoordinates.setRelativeText("tp.relative.entity");
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
				if (f < -180 || f > 180) throw new WrongValueException(this.entryYRot.label.getAbsoluteText(), Lang.translate("error.number.bounds")
						.replace("<min>", "-180").replace("<max>", "180"), y);
			} catch (NumberFormatException e)
			{
				throw new WrongValueException(this.entryYRot.label.getAbsoluteText(), Lang.translate("error.number.bounds").replace("<min>", "-180")
						.replace("<max>", "180"), y);
			}
			try
			{
				float f = Float.parseFloat(x);
				if (f < -90 || f > 90) throw new WrongValueException(this.entryXRot.label.getAbsoluteText(), Lang.translate("error.number.bounds")
						.replace("<min>", "-90").replace("<max>", "90"), x);
			} catch (NumberFormatException e)
			{
				throw new WrongValueException(this.entryXRot.label.getAbsoluteText(), Lang.translate("error.number.bounds").replace("<min>", "-90")
						.replace("<max>", "90"), x);
			}
		}

		return command + this.panelCoordinates.generateCoordinates().toCommand() + " "
				+ (rotation ? (" " + (this.checkboxYRot.isSelected() ? "~" : "") + y + " " + (this.checkboxXRot.isSelected() ? "~" : "") + x) : "");
	}
}
