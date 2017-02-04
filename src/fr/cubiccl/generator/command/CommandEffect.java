package fr.cubiccl.generator.command;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import fr.cubiccl.generator.gui.component.combobox.OptionCombobox;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelEffect;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelTarget;
import fr.cubiccl.generator.utils.CommandGenerationException;

public class CommandEffect extends Command implements ActionListener
{
	private OptionCombobox comboboxMode;
	private PanelEffect panelEffect;
	private PanelTarget panelTarget;

	public CommandEffect()
	{
		super("effect");
	}

	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		this.panelEffect.setVisible(this.comboboxMode.getValue().equals("apply"));
	}

	@Override
	public CGPanel createGUI()
	{
		CGPanel panel = new CGPanel();
		GridBagConstraints gbc = panel.createGridBagLayout();

		panel.add(this.labelDescription(), gbc);
		++gbc.gridy;
		panel.add(this.panelTarget = new PanelTarget(PanelTarget.ALL_ENTITIES), gbc);
		++gbc.gridy;
		panel.add(this.comboboxMode = new OptionCombobox("effect.mode", "apply", "clear"), gbc);
		++gbc.gridy;
		panel.add(this.panelEffect = new PanelEffect(), gbc);

		this.comboboxMode.addActionListener(this);

		return panel;
	}

	@Override
	public String generate() throws CommandGenerationException
	{
		String command = this.id + " " + this.panelTarget.generate().toCommand() + " ";

		if (this.comboboxMode.getValue().equals("clear")) return command + "clear";

		return command + this.panelEffect.generate().toCommand();
	}

}
