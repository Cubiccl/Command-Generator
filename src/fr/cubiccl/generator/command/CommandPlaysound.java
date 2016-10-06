package fr.cubiccl.generator.command;

import java.awt.GridBagConstraints;

import fr.cubiccl.generator.gameobject.ObjectRegistry;
import fr.cubiccl.generator.gameobject.Sound;
import fr.cubiccl.generator.gui.component.combobox.ObjectCombobox;
import fr.cubiccl.generator.gui.component.combobox.OptionCombobox;
import fr.cubiccl.generator.gui.component.label.CLabel;
import fr.cubiccl.generator.gui.component.panel.CPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelCoordinates;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelTarget;
import fr.cubiccl.generator.gui.component.textfield.CEntry;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Lang;
import fr.cubiccl.generator.utils.WrongValueException;

public class CommandPlaysound extends Command
{
	private ObjectCombobox<Sound> comboboxSound;
	private OptionCombobox comboboxSource;
	private CEntry entryVolume, entryPitch, entryMinVolume;
	private PanelCoordinates panelCoordinates;
	private PanelTarget panelTarget;

	public CommandPlaysound()
	{
		super("playsound");
	}

	@Override
	public CPanel createGUI()
	{
		CPanel panel = new CPanel();
		GridBagConstraints gbc = panel.createGridBagLayout();

		panel.add(this.labelDescription(), gbc);
		++gbc.gridy;
		panel.add(new CLabel("playsound.sound").setHasColumn(true), gbc);
		++gbc.gridx;
		panel.add((this.comboboxSound = new ObjectCombobox<Sound>(ObjectRegistry.getSounds())).container, gbc);
		--gbc.gridx;
		++gbc.gridy;
		panel.add(new CLabel("playsound.source").setHasColumn(true), gbc);
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
		panel.add((this.entryVolume = new CEntry("playsound.volume", "1")).container, gbc);
		++gbc.gridy;
		panel.add((this.entryPitch = new CEntry("playsound.pitch", "1")).container, gbc);
		++gbc.gridy;
		panel.add((this.entryMinVolume = new CEntry("playsound.minimum_volume", "1")).container, gbc);

		this.panelCoordinates.setRelativeText("playsound.relative");
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
			if (volume < 0) throw new WrongValueException(this.entryVolume.label.getAbsoluteText(), Lang.translate("error.number.greater").replace("<min>", "0"), v);
		} catch (NumberFormatException e)
		{
			throw new WrongValueException(this.entryVolume.label.getAbsoluteText(), Lang.translate("error.number.greater").replace("<min>", "0"), v);
		}

		try
		{
			pitch = Float.parseFloat(p);
			if (pitch < 0 || pitch > 2) throw new WrongValueException(this.entryPitch.label.getAbsoluteText(), Lang.translate("error.number.bounds")
					.replace("<min>", "0").replace("<max>", "2"), p);
		} catch (NumberFormatException e)
		{
			throw new WrongValueException(this.entryPitch.label.getAbsoluteText(), Lang.translate("error.number.bounds").replace("<min>", "0").replace("<max>", "2"), p);
		}

		try
		{
			minVolume = Float.parseFloat(mv);
			if (minVolume < 0) throw new WrongValueException(this.entryMinVolume.label.getAbsoluteText(), Lang.translate("error.number.greater").replace("<min>", "0"),
					mv);
		} catch (NumberFormatException e)
		{
			throw new WrongValueException(this.entryMinVolume.label.getAbsoluteText(), Lang.translate("error.number.greater").replace("<min>", "0"), mv);
		}

		return "/playsound " + this.comboboxSound.getSelectedObject().id + " " + this.comboboxSource.getValue() + " "
				+ this.panelTarget.generateTarget().toCommand() + this.panelCoordinates.generateCoordinates().toCommand() + " " + volume + " " + pitch + " "
				+ minVolume;
	}

}
