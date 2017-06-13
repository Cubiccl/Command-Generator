package fr.cubiccl.generator.command;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import fr.cubiccl.generator.gameobject.Coordinates;
import fr.cubiccl.generator.gameobject.baseobjects.Sound;
import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.gameobject.target.Target;
import fr.cubiccl.generator.gui.component.combobox.ObjectCombobox;
import fr.cubiccl.generator.gui.component.combobox.OptionCombobox;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.label.HelpLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelCoordinates;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelTarget;
import fr.cubiccl.generator.gui.component.textfield.CGEntry;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;

public class CommandPlaysound extends Command
{
	public static final String[] CHANNELS =
	{ "master", "music", "record", "weather", "block", "hostile", "neutral", "player", "ambient", "voice" };
	private ObjectCombobox<Sound> comboboxSound;
	private OptionCombobox comboboxSource;
	private CGEntry entryVolume, entryPitch, entryMinVolume;
	private PanelCoordinates panelCoordinates;
	private PanelTarget panelTarget;

	public CommandPlaysound()
	{
		super("playsound", "playsound <sound> <source> <player> [<x> <y> <z>] [volume] [pitch] [minimumVolume]", 4, 7, 8, 9, 10);
	}

	@Override
	public CGPanel createUI()
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
		panel.add(this.comboboxSource = new OptionCombobox("playsound.source", CHANNELS), gbc);
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

		this.entryVolume.addHelpLabel(new HelpLabel("playsound.volume.help"));
		this.entryPitch.addHelpLabel(new HelpLabel("playsound.pitch.help"));
		this.entryMinVolume.addHelpLabel(new HelpLabel("playsound.minimum_volume.help"));

		this.comboboxSound.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				updateTranslations();
			}
		});
		this.panelTarget.addArgumentChangeListener(this);

		return panel;
	}

	@Override
	protected void resetUI()
	{
		this.panelCoordinates.setupFrom(new Coordinates(0, 0, 0, true, true, true));
		this.entryVolume.setText("0");
		this.entryPitch.setText("1");
		this.entryMinVolume.setText("0");
	}

	@Override
	protected Text description()
	{
		return this.defaultDescription().addReplacement("<sound>", this.comboboxSound.getSelectedObject().id())
				.addReplacement("<target>", this.panelTarget.displayTarget());
	}

	@Override
	public String generate() throws CommandGenerationException
	{
		String v = this.entryVolume.getText(), p = this.entryPitch.getText(), mv = this.entryMinVolume.getText();

		this.entryVolume.checkValueSuperior(CGEntry.FLOAT, 0);
		this.entryPitch.checkValueInBounds(CGEntry.FLOAT, 0, 2);
		this.entryMinVolume.checkValueSuperior(CGEntry.FLOAT, 0);

		return this.id + " " + this.comboboxSound.getSelectedObject().id() + " " + this.comboboxSource.getValue() + " "
				+ this.panelTarget.generate().toCommand() + " " + this.panelCoordinates.generate().toCommand() + " " + v + " " + p + " " + mv;
	}

	@Override
	protected void readArgument(int index, String argument, String[] fullCommand) throws CommandGenerationException
	{
		// playsound <sound> <source> <player> [<x> <y> <z>] [volume] [pitch] [minimumVolume]
		if (index == 1) this.comboboxSound.setSelected(ObjectRegistry.sounds.find(argument));
		if (index == 2) this.comboboxSource.setValue(argument);
		if (index == 3) this.panelTarget.setupFrom(new Target().fromString(argument));
		if (index == 4) this.panelCoordinates.setupFrom(new Coordinates().fromString(argument, fullCommand[5], fullCommand[6]));
		if (index >= 7) try
		{
			if (Float.parseFloat(argument) >= 0)
			{
				if (index == 7) this.entryVolume.setText(argument);
				if (index == 8 && Float.parseFloat(argument) <= 2) this.entryPitch.setText(argument);
				if (index == 9) this.entryMinVolume.setText(argument);
			}
		} catch (Exception e)
		{}
	}
}
