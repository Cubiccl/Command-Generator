package fr.cubiccl.generator.command;

import java.awt.GridBagConstraints;

import fr.cubiccl.generator.gameobject.baseobjects.Sound;
import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.gui.component.combobox.ObjectCombobox;
import fr.cubiccl.generator.gui.component.combobox.OptionCombobox;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelCoordinates;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelTarget;
import fr.cubiccl.generator.gui.component.textfield.CGEntry;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;

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
		panel.add((this.comboboxSound = new ObjectCombobox<Sound>(ObjectRegistry.sounds.list())).container, gbc);
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
		panel.add((this.entryVolume = new CGEntry(new Text("playsound.volume"), "1", Text.NUMBER)).container, gbc);
		++gbc.gridy;
		panel.add((this.entryPitch = new CGEntry(new Text("playsound.pitch"), "1", Text.NUMBER)).container, gbc);
		++gbc.gridy;
		panel.add((this.entryMinVolume = new CGEntry(new Text("playsound.minimum_volume"), "1", Text.NUMBER)).container, gbc);

		this.panelCoordinates.setRelativeText(new Text("playsound.relative"));
		this.entryVolume.addNumberFilter();
		this.entryPitch.addNumberFilter();
		this.entryMinVolume.addNumberFilter();

		return panel;
	}

	@Override
	public String generate() throws CommandGenerationException
	{
		String v = this.entryVolume.getText(), p = this.entryPitch.getText(), mv = this.entryMinVolume.getText();

		this.entryVolume.checkValueSuperior(CGEntry.FLOAT, 0);
		this.entryPitch.checkValueInBounds(CGEntry.FLOAT, 0, 2);
		this.entryMinVolume.checkValueSuperior(CGEntry.FLOAT, 0);

		return this.id + " " + this.comboboxSound.getSelectedObject().id + " " + this.comboboxSource.getValue() + " "
				+ this.panelTarget.generateTarget().toCommand() + this.panelCoordinates.generateCoordinates().toCommand() + " " + v + " " + p + " " + mv;
	}
}
