package fr.cubiccl.generator.command;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.gameobject.target.Target;
import fr.cubiccl.generator.gui.component.combobox.OptionCombobox;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelEffect;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelTarget;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;

public class CommandEffect extends Command implements ActionListener
{
	private OptionCombobox comboboxMode;
	private PanelEffect panelEffect;
	private PanelTarget panelTarget;

	public CommandEffect()
	{
		super("effect", "effect <player> <effect> [seconds] [amplifier] [hideParticles]\n" + "effect <player> clear", 3, 4, 5, 6);
	}

	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		this.panelEffect.setVisible(this.comboboxMode.getValue().equals("apply"));
		this.updateTranslations();
	}

	@Override
	public CGPanel createUI()
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
		this.panelEffect.setDurationAsSeconds(true);

		this.comboboxMode.addActionListener(this);
		this.panelEffect.addActionListener(this);
		this.panelTarget.addArgumentChangeListener(this);

		return panel;
	}

	@Override
	protected void resetUI()
	{
		this.panelEffect.setDefault();
		this.comboboxMode.setValue("apply");
		this.panelEffect.setVisible(true);

	}

	@Override
	protected Text description()
	{
		String target = this.panelTarget.displayTarget();
		if (this.isClearing()) return new Text("command." + this.id + ".clear").addReplacement("<target>", target);
		return this.defaultDescription().addReplacement("<target>", target).addReplacement("<effect>", this.panelEffect.selectedEffect().name());
	}

	@Override
	public String generate() throws CommandGenerationException
	{
		String command = this.id + " " + this.panelTarget.generate().toCommand() + " ";

		if (this.isClearing()) return command + "clear";

		return command + this.panelEffect.generate().toCommand();
	}

	private boolean isClearing()
	{
		return this.comboboxMode.getSelectedIndex() == 1;
	}

	@Override
	protected void readArgument(int index, String argument, String[] fullCommand) throws CommandGenerationException
	{
		// effect <player> <effect> [seconds] [amplifier] [hideParticles]
		// effect <player> clear

		if (index == 1) this.panelTarget.setupFrom(new Target().fromString(argument));
		if (index == 2) if (argument.equals("clear"))
		{
			this.comboboxMode.setValue("clear");
			this.panelEffect.setVisible(false);
		} else this.panelEffect.setEffect(ObjectRegistry.effects.find(argument));
		if (index == 3) try
		{
			this.panelEffect.setDuration(Integer.parseInt(argument));
		} catch (Exception e)
		{}
		if (index == 4) try
		{
			this.panelEffect.setAmplifier(Integer.parseInt(argument));
		} catch (Exception e)
		{}
		if (index == 5) this.panelEffect.setHideParticles(argument.equals("true"));
	}
}
