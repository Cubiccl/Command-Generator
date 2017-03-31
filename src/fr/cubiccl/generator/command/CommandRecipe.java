package fr.cubiccl.generator.command;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.gameobject.target.Target;
import fr.cubiccl.generator.gui.component.combobox.OptionCombobox;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelItem;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelTarget;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;

public class CommandRecipe extends Command implements ActionListener
{
	private static final byte GIVE = 0, TAKE = 1;
	private static final byte ONE = 0, ALL = 1;

	private OptionCombobox comboboxMode, comboboxNumber;
	private PanelItem panelItem;
	private PanelTarget panelTarget;

	public CommandRecipe()
	{
		super("recipe", "recipe <give|take> <player> <item|*>", 4);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		this.panelItem.setVisible(!this.comboboxNumber.getValue().equals("all"));
		this.updateTranslations();
	}

	@Override
	public CGPanel createGUI()
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
		panel.add(this.comboboxNumber = new OptionCombobox("recipe", "one", "all"), gbc);
		--gbc.gridx;
		++gbc.gridy;
		++gbc.gridwidth;
		panel.add(this.panelItem = new PanelItem("recipe.item"), gbc);
		++gbc.gridy;
		panel.add(this.panelTarget = new PanelTarget("target.title.player", PanelTarget.PLAYERS_ONLY), gbc);

		this.comboboxMode.addActionListener(this);
		this.comboboxNumber.addActionListener(this);
		this.panelItem.addArgumentChangeListener(this);
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

		if (this.numberMode() == ONE) description.addReplacement("<item>", this.panelItem.selectedItem().toString());
		description.addReplacement("<target>", this.panelTarget.displayTarget());

		return description;
	}

	@Override
	public String generate() throws CommandGenerationException
	{
		String command = this.id + " " + this.comboboxMode.getValue() + " ";
		if (this.numberMode() == ALL) command += "* ";
		else command += "achievement." + this.panelItem.selectedItem().id() + " ";

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
	{// recipe <give|take> <player> <item|*>
		if (index == 1) this.comboboxMode.setValue(argument);
		else if (index == 2) this.panelTarget.setupFrom(Target.createFrom(argument));
		else if (index == 3)
		{
			if (argument.equals("*")) this.comboboxNumber.setValue("all");
			else
			{
				this.comboboxNumber.setValue("one");
				this.panelItem.setItem(ObjectRegistry.items.find(argument));
			}
		}
	}

}
