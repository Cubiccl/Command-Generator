package fr.cubiccl.generator.command;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import fr.cubiccl.generator.gameobject.baseobjects.Sound;
import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.gameobject.target.Target;
import fr.cubiccl.generator.gui.component.button.CGCheckBox;
import fr.cubiccl.generator.gui.component.combobox.ObjectCombobox;
import fr.cubiccl.generator.gui.component.combobox.OptionCombobox;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelTarget;
import fr.cubiccl.generator.utils.CommandGenerationException;

public class CommandStopsound extends Command implements ActionListener
{

	private CGCheckBox checkboxAllSound;
	private OptionCombobox comboboxChannel;
	private ObjectCombobox<Sound> comboboxSound;
	private CGLabel labelSound;
	private PanelTarget panelTarget;

	public CommandStopsound()
	{
		super("stopsound", "stopsound <player> <source> [sound]", 2, 3, 4);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		this.finishReading();
	}

	@Override
	protected CGPanel createGUI()
	{
		CGPanel p = new CGPanel();
		GridBagConstraints gbc = p.createGridBagLayout();
		gbc.gridwidth = 2;

		p.add(this.labelDescription());
		++gbc.gridy;
		p.add(this.panelTarget = new PanelTarget(PanelTarget.PLAYERS_ONLY), gbc);
		++gbc.gridy;
		p.add(this.checkboxAllSound = new CGCheckBox("stopsound.all"), gbc);
		++gbc.gridy;
		--gbc.gridwidth;
		p.add(this.labelSound = new CGLabel("stopsound.sound"), gbc);
		++gbc.gridx;
		p.add((this.comboboxSound = new ObjectCombobox<Sound>(ObjectRegistry.sounds.list())).container, gbc);
		--gbc.gridx;
		++gbc.gridy;
		p.add(new CGLabel("stopsound.source").setHasColumn(true), gbc);
		++gbc.gridx;
		p.add(this.comboboxChannel = new OptionCombobox("playsound.source", CommandPlaysound.CHANNELS), gbc);

		this.checkboxAllSound.addActionListener(this);

		return p;
	}

	@Override
	protected void defaultGui()
	{
		this.checkboxAllSound.setSelected(true);
		this.comboboxSound.container.setVisible(false);
		this.labelSound.setVisible(false);
		this.comboboxChannel.setValue("master");
	}

	@Override
	protected void finishReading()
	{
		this.comboboxSound.container.setVisible(!this.checkboxAllSound.isSelected());
		this.labelSound.setVisible(!this.checkboxAllSound.isSelected());
	}

	@Override
	public String generate() throws CommandGenerationException
	{
		String command = this.id + " " + this.panelTarget.generate().toCommand() + " " + this.comboboxChannel.getValue();
		if (!this.checkboxAllSound.isSelected()) command += " " + this.comboboxSound.getSelectedObject().id();
		return command;
	}

	@Override
	protected void readArgument(int index, String argument, String[] fullCommand) throws CommandGenerationException
	{
		// stopsound <player> <source> [sound]
		if (index == 1) this.panelTarget.setupFrom(Target.createFrom(argument));
		if (index == 2) this.comboboxChannel.setValue(argument);
		if (index == 3)
		{
			this.checkboxAllSound.setSelected(false);
			this.comboboxSound.setSelected(ObjectRegistry.sounds.find(argument));
		}
	}

}
