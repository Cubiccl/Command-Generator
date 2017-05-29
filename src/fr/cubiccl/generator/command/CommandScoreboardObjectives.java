package fr.cubiccl.generator.command;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import fr.cubiccl.generator.gui.component.button.CGCheckBox;
import fr.cubiccl.generator.gui.component.combobox.OptionCombobox;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelCriteria;
import fr.cubiccl.generator.gui.component.textfield.CGEntry;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;
import fr.cubiccl.generator.utils.Utils;

public class CommandScoreboardObjectives extends Command implements ActionListener
{
	public static final String[] SLOTS =
	{ "belowName", "list", "sidebar", "sidebar.team.aqua", "sidebar.team.black", "sidebar.team.blue", "sidebar.team.dark_aqua", "sidebar.team.dark_blue",
			"sidebar.team.dark_gray", "sidebar.team.dark_green", "sidebar.team.dark_purple", "sidebar.team.dark_red", "sidebar.team.gold", "sidebar.team.gray",
			"sidebar.team.green", "sidebar.team.light_purple", "sidebar.team.red", "sidebar.team.yellow", "sidebar.team.white" };

	private CGCheckBox checkboxClearSlot;
	private OptionCombobox comboboxMode, comboboxDisplaySlot;
	private CGEntry entryObjective, entryDisplayName;
	private CGLabel labelSlot;
	private PanelCriteria panelCriteria;

	public CommandScoreboardObjectives()
	{
		super("scoreboard objectives", "scoreboard objectives add <name> <criteria> [display name]\n" + "scoreboard objectives remove <objective>\n"
				+ "scoreboard objectives setdisplay <slot> [objective]", 3, 4, 5);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		this.onParsingEnd();
		this.updateTranslations();
	}

	@Override
	public CGPanel createUI()
	{
		CGPanel panel = new CGPanel();
		GridBagConstraints gbc = panel.createGridBagLayout();

		++gbc.gridwidth;
		panel.add(this.labelDescription(), gbc);
		++gbc.gridy;
		--gbc.gridwidth;
		panel.add(new CGLabel("general.mode").setHasColumn(true), gbc);
		++gbc.gridx;
		panel.add(this.comboboxMode = new OptionCombobox("scoreboard.objectives.mode", "add", "remove", "setdisplay"), gbc);
		--gbc.gridx;
		++gbc.gridy;
		++gbc.gridwidth;
		panel.add((this.entryObjective = new CGEntry(Text.OBJECTIVE, Text.OBJECTIVE)).container, gbc);
		--gbc.gridwidth;
		++gbc.gridy;
		panel.add(this.labelSlot = new CGLabel("score.add.slot").setHasColumn(true), gbc);
		++gbc.gridx;
		panel.add(this.comboboxDisplaySlot = new OptionCombobox("score.slot", SLOTS), gbc);
		--gbc.gridx;
		++gbc.gridy;
		++gbc.gridwidth;
		panel.add((this.entryDisplayName = new CGEntry(new Text("score.add.display"), new Text("display.name"))).container, gbc);
		panel.add(this.checkboxClearSlot = new CGCheckBox("score.slot.clear"), gbc);
		++gbc.gridy;
		panel.add(this.panelCriteria = new PanelCriteria(), gbc);

		this.comboboxMode.addActionListener(this);
		this.checkboxClearSlot.addActionListener(this);
		this.comboboxDisplaySlot.setVisible(false);
		this.checkboxClearSlot.setVisible(false);
		this.labelSlot.setVisible(false);

		this.entryObjective.addKeyListener(new KeyListener()
		{
			@Override
			public void keyPressed(KeyEvent e)
			{}

			@Override
			public void keyReleased(KeyEvent e)
			{
				updateTranslations();
			}

			@Override
			public void keyTyped(KeyEvent e)
			{}
		});

		return panel;
	}

	@Override
	protected void resetUI()
	{
		this.checkboxClearSlot.setSelected(true);
		this.entryObjective.container.setVisible(false);
	}

	@Override
	protected Text description()
	{
		Text d = this.defaultDescription();

		if (!this.comboboxMode.getValue().equals("add")) d = new Text("command." + this.id + "." + this.comboboxMode.getValue());

		String obj = this.entryObjective.getText();
		try
		{
			Utils.checkStringId(null, obj);
		} catch (Exception e)
		{
			obj = "???";
		}

		return d.addReplacement("<objective>", obj);
	}

	@Override
	protected void onParsingEnd()
	{
		boolean display = this.comboboxMode.getValue().equals("setdisplay"), add = this.comboboxMode.getValue().equals("add");
		this.comboboxDisplaySlot.setVisible(display);
		this.entryDisplayName.container.setVisible(add);
		this.checkboxClearSlot.setVisible(display);
		this.labelSlot.setVisible(display);
		this.panelCriteria.setVisible(add);
		this.entryObjective.container.setVisible(!this.comboboxMode.getValue().equals("setdisplay") || !this.checkboxClearSlot.isSelected());
	}

	@Override
	public String generate() throws CommandGenerationException
	{
		String command = this.id + " " + this.comboboxMode.getValue();
		String objective = this.entryObjective.getText();
		boolean display = this.comboboxMode.getValue().equals("setdisplay");
		boolean clear = display && this.checkboxClearSlot.isSelected();
		if (!(clear)) this.entryObjective.checkValue(CGEntry.STRING);

		switch (this.comboboxMode.getValue())
		{
			case "add":
				command += " " + objective + " " + this.panelCriteria.getCriteria();
				if (this.entryDisplayName.getText().equals("")) return command;
				return command + " " + this.entryDisplayName.getText();

			case "setdisplay":
				if (clear) return command + " " + this.comboboxDisplaySlot.getValue();
				return command + " " + this.comboboxDisplaySlot.getValue() + " " + objective;

			default:
				break;
		}

		return command + " " + objective;
	}

	@Override
	protected void readArgument(int index, String argument, String[] fullCommand) throws CommandGenerationException
	{
		// scoreboard objectives add <name> <criteria> [display name]
		// scoreboard objectives remove <objective>
		// scoreboard objectives setdisplay <slot> [objective]
		String mode = fullCommand[1];
		if (index == 1) this.comboboxMode.setValue(argument);
		if (index == 2) if (mode.equals("setdisplay")) this.comboboxDisplaySlot.setValue(argument);
		else this.entryObjective.setText(argument);
		if (index == 3) if (mode.equals("setdisplay"))
		{
			this.checkboxClearSlot.setSelected(false);
			this.entryObjective.container.setVisible(true);
			this.entryObjective.setText(argument);
		} else this.panelCriteria.setupFrom(argument);
		if (index == 4) this.entryObjective.setText(argument);
	}
}
