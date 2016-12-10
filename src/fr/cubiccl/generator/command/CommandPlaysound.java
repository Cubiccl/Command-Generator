package fr.cubiccl.generator.command;

import java.awt.GridBagConstraints;

import fr.cubiccl.generator.gameobject.ObjectRegistry;
import fr.cubiccl.generator.gameobject.Sound;
import fr.cubiccl.generator.gui.component.combobox.ObjectCombobox;
import fr.cubiccl.generator.gui.component.combobox.OptionCombobox;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelCoordinates;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelTarget;
import fr.cubiccl.generator.gui.component.textfield.CGEntry;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Replacement;
import fr.cubiccl.generator.utils.Text;
import fr.cubiccl.generator.utils.WrongValueException;

public class CommandPlaysound extends Command
{
	private ObjectCombobox<Sound> comboboxSound;
	private OptionCombobox comboboxSource;
	private CGEntry entryVolume, entryPitch, entryMinVolume;
	private PanelCoordinates panelCoordinates;
	private PanelTarget panelTarget;

	public CommandPlaysound()
	{
		super("playsound");
	}

	@Override
	public CGPanel createGUI()
	{
		CGPanel panel = new CGPanel();
		GridBagConstraints gbc = panel.createGridBagLayout();

		++gbc.gridwidth;
		panel.add(this.labelDescription(), gbc);
		--gbc.gridwidth;
		++gbc.gridy;
		panel.add(new CGLabel("playsound.sound").setHasColumn(true), gbc);
		++gbc.gridx;
		panel.add((this.comboboxSound = new ObjectCombobox<Sound>(ObjectRegistry.getSounds())).container, gbc);
		--gbc.gridx;
		++gbc.gridy;
		panel.add(new CGLabel("playsound.source").setHasColumn(true), gbc);
		++gbc.gridx;
		panel.add(this.comboboxSource = new OptionCombobox("playsound.source", "master", "music", "record", "weather", "block", "hostile", "neutral", "player",
				"ambient", "voice"), gbc);
		--gbc.gridx;
		++gbc.gridy;
		++gbc.gridwidth;
		panel.add(this.panelTarget = new PanelTarget(PanelTarget.PLAYERS_ONLY), gbc);
		++gbc.gridy;
		panel.add(this.panelCoordinates = new PanelCoordinates("playsound.coordinates"), gbc);
		++gbc.gridy;
		panel.add((this.entryVolume = new CGEntry("playsound.volume", "1")).container, gbc);
		++gbc.gridy;
		panel.add((this.entryPitch = new CGEntry("playsound.pitch", "1")).container, gbc);
		++gbc.gridy;
		panel.add((this.entryMinVolume = new CGEntry("playsound.minimum_volume", "1")).container, gbc);

		this.panelCoordinates.setRelativeText(new Text("playsound.relative"));
		this.entryVolume.addNumberFilter();
		this.entryPitch.addNumberFilter();
		this.entryMinVolume.addNumberFilter();

		return panel;
	}

	@Override
	public String generate() throws CommandGenerationException
	{
		float volume, pitch, minVolume;
		String v = this.entryVolume.getText(), p = this.entryPitch.getText(), mv = this.entryMinVolume.getText();

		try
		{
			volume = Float.parseFloat(v);
			if (volume < 0) throw new WrongValueException(this.entryVolume.label.getAbsoluteText(), new Text("error.number.greater", new Replacement("<min>",
					"0")), v);
		} catch (NumberFormatException e)
		{
			throw new WrongValueException(this.entryVolume.label.getAbsoluteText(), new Text("error.number.greater", new Replacement("<min>", "0")), v);
		}

		try
		{
			pitch = Float.parseFloat(p);
			if (pitch < 0 || pitch > 2) throw new WrongValueException(this.entryPitch.label.getAbsoluteText(), new Text("error.number.bounds", new Replacement(
					"<min>", "0"), new Replacement("<max>", "2")), p);
		} catch (NumberFormatException e)
		{
			throw new WrongValueException(this.entryPitch.label.getAbsoluteText(), new Text("error.number.bounds", new Replacement("<min>", "0"),
					new Replacement("<max>", "2")), p);
		}

		try
		{
			minVolume = Float.parseFloat(mv);
			if (minVolume < 0) throw new WrongValueException(this.entryMinVolume.label.getAbsoluteText(), new Text("error.number.greater", new Replacement(
					"<min>", "0")), mv);
		} catch (NumberFormatException e)
		{
			throw new WrongValueException(this.entryMinVolume.label.getAbsoluteText(), new Text("error.number.greater", new Replacement("<min>", "0")), mv);
		}

		return "/playsound " + this.comboboxSound.getSelectedObject().id + " " + this.comboboxSource.getValue() + " "
				+ this.panelTarget.generateTarget().toCommand() + this.panelCoordinates.generateCoordinates().toCommand() + " " + volume + " " + pitch + " "
				+ minVolume;
	}
}
