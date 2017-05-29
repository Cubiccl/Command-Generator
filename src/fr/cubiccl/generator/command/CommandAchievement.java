package fr.cubiccl.generator.command;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.gameobject.target.Target;
import fr.cubiccl.generator.gui.component.combobox.OptionCombobox;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelAchievement;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelTarget;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;

public class CommandAchievement extends Command implements ActionListener
{
	private static final byte GIVE = 0, TAKE = 1;
	private static final byte ONE = 0, ALL = 1;

	private OptionCombobox comboboxMode, comboboxNumber;
	private PanelAchievement panelAchievement;
	private PanelTarget panelTarget;

	public CommandAchievement()
	{
		super("achievement", "achievement <give|take> <achievement|*> <player>", 4);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		this.panelAchievement.setVisible(!this.comboboxNumber.getValue().equals("all"));
		this.updateTranslations();
	}

	@Override
	public CGPanel createUI()
	{
		CGPanel panel = new CGPanel();
		GridBagConstraints gbc = panel.createGridBagLayout();

		++gbc.gridwidth;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		panel.add(this.labelDescription(), gbc);
		++gbc.gridy;
		--gbc.gridwidth;
		gbc.anchor = GridBagConstraints.EAST;
		panel.add(this.comboboxMode = new OptionCombobox("achievement", "give", "take"), gbc);
		++gbc.gridx;
		gbc.anchor = GridBagConstraints.NORTH;
		panel.add(this.comboboxNumber = new OptionCombobox("achievement", "one", "all"), gbc);
		--gbc.gridx;
		++gbc.gridy;
		++gbc.gridwidth;
		panel.add(this.panelAchievement = new PanelAchievement(), gbc);
		++gbc.gridy;
		panel.add(this.panelTarget = new PanelTarget("target.title.player", PanelTarget.PLAYERS_ONLY), gbc);

		this.comboboxMode.addActionListener(this);
		this.comboboxNumber.addActionListener(this);
		this.panelAchievement.addArgumentChangeListener(this);
		this.panelTarget.addArgumentChangeListener(this);

		return panel;
	}

	@Override
	protected Text description()
	{
		Text description = this.defaultDescription();
		if (this.mode() == TAKE)
		{
			if (this.numberMode() == ALL) description = new Text("command." + this.id + ".take.all");
			else description = new Text("command." + this.id + ".take");
		} else if (this.numberMode() == ALL) description = new Text("command." + this.id + ".all");

		if (this.numberMode() == ONE) description.addReplacement("<achievement>", this.panelAchievement.getAchievement().toString());
		description.addReplacement("<target>", this.panelTarget.displayTarget());

		return description;
	}

	@Override
	public String generate() throws CommandGenerationException
	{
		String command = this.id + " " + this.comboboxMode.getValue() + " ";
		if (this.numberMode() == ALL) command += "* ";
		else command += "achievement." + this.panelAchievement.getAchievement().id.substring("minecraft:".length()) + " ";

		return command + this.panelTarget.generate().toCommand();
	}

	private byte mode()
	{
		if (this.comboboxMode == null) return GIVE;
		return (byte) this.comboboxMode.getSelectedIndex();
	}

	private byte numberMode()
	{
		if (this.comboboxNumber == null) return ONE;
		return (byte) this.comboboxNumber.getSelectedIndex();
	}

	@Override
	protected void readArgument(int index, String argument, String[] fullCommand) throws CommandGenerationException
	{
		if (index == 1) this.comboboxMode.setValue(argument);
		else if (index == 3) this.panelTarget.setupFrom(Target.createFrom(argument));
		else if (index == 2)
		{
			if (argument.equals("*")) this.comboboxNumber.setValue("all");
			else
			{
				this.comboboxNumber.setValue("one");
				this.panelAchievement.setSelection(ObjectRegistry.achievements.find(argument));
			}
		}
	}

}
