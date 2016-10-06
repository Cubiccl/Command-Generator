package fr.cubiccl.generator.command;

import java.awt.GridBagConstraints;

import fr.cubiccl.generator.gui.component.button.CCheckBox;
import fr.cubiccl.generator.gui.component.panel.CPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelCoordinates;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelTarget;
import fr.cubiccl.generator.gui.component.textfield.CEntry;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Lang;
import fr.cubiccl.generator.utils.WrongValueException;

public class CommandTeleport extends Command
{
	private CCheckBox checkboxXRot, checkboxYRot;
	private CEntry entryYRot, entryXRot;
	private PanelCoordinates panelCoordinates;
	private PanelTarget panelTarget;

	public CommandTeleport()
	{
		super("teleport");
	}

	@Override
	public CPanel createGUI()
	{
		CPanel panel = new CPanel();
		GridBagConstraints gbc = panel.createGridBagLayout();

		CPanel rotation = new CPanel("tp.rotation");
		GridBagConstraints gbc2 = rotation.createGridBagLayout();
		rotation.add((this.entryYRot = new CEntry("tp.rotation.y")).container, gbc2);
		++gbc2.gridy;
		rotation.add((this.entryXRot = new CEntry("tp.rotation.x")).container, gbc2);
		++gbc2.gridx;
		--gbc2.gridy;
		rotation.add(this.checkboxYRot = new CCheckBox("tp.rotation.relative"), gbc2);
		++gbc2.gridy;
		rotation.add(this.checkboxXRot = new CCheckBox("tp.rotation.relative"), gbc2);

		panel.add(this.labelDescription(), gbc);
		++gbc.gridy;
		panel.add(this.panelTarget = new PanelTarget(PanelTarget.ALL_ENTITIES), gbc);
		++gbc.gridy;
		panel.add(this.panelCoordinates = new PanelCoordinates("tp.coordinates"), gbc);
		++gbc.gridy;
		panel.add(rotation, gbc);

		return panel;
	}

	@Override
	public String generate() throws CommandGenerationException
	{
		String command = "/teleport " + this.panelTarget.generateTarget().toCommand() + " " + this.panelCoordinates.generateCoordinates().toCommand();

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

		return command + (rotation ? (" " + (this.checkboxYRot.isSelected() ? "~" : "") + y + " " + (this.checkboxXRot.isSelected() ? "~" : "") + x) : "");
	}
}
