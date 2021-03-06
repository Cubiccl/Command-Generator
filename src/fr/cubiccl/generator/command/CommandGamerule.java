package fr.cubiccl.generator.command;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Comparator;

import fr.cubiccl.generator.CommandGenerator;
import fr.cubiccl.generator.gui.component.button.CGButton;
import fr.cubiccl.generator.gui.component.button.CGCheckBox;
import fr.cubiccl.generator.gui.component.label.HelpLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.utils.ComboboxPanel;
import fr.cubiccl.generator.gui.component.textfield.CGEntry;
import fr.cubiccl.generator.utils.*;
import fr.cubiccl.generator.utils.Settings.Version;

public class CommandGamerule extends Command implements ActionListener, IStateListener<ComboboxPanel>
{
	public static final String[] GAMERULES_1d11 =
	{ "commandBlockOutput", "disableElytraMovementCheck", "doDaylightCycle", "doEntityDrops", "doFireTick", "doMobLoot", "doMobSpawning", "doTileDrops",
			"doWeatherCycle", "keepInventory", "logAdminCommands", "maxEntityCramming", "mobGriefing", "naturalRegeneration", "randomTickSpeed",
			"reducedDebugInfo", "sendCommandFeedback", "showDeathMessages", "spawnRadius", "spectatorsGenerateChunks" }, GAMERULES_1d12 =
	{ "announceAdvancements", "doLimitedCrafting", "gameLoopFunction", "maxCommandChainLength" };

	private static String[] gamerules()
	{
		ArrayList<String> gamerules = new ArrayList<String>();

		for (String string : GAMERULES_1d11)
			gamerules.add(string);
		if (Settings.version().isAfter(Version.v1d12)) for (String string : GAMERULES_1d12)
			gamerules.add(string);

		gamerules.sort(Comparator.naturalOrder());
		return gamerules.toArray(new String[gamerules.size()]);
	}

	private CGButton buttonPredefined;
	private CGCheckBox checkboxQuery;
	private CGEntry entryGamerule, entryValue;

	private String[] gamerules;

	public CommandGamerule()
	{
		super("gamerule", "gamerule <rule> [value]", 2, 3);
		this.gamerules = gamerules();
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == this.buttonPredefined) CommandGenerator.stateManager.setState(new ComboboxPanel(new Text("gamerule.predefined.select"),
				"gamerule", this.gamerules), this);
		else this.onCheckbox();
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
		panel.add((this.entryGamerule = new CGEntry(new Text("gamerule.entry"), new Text("gamerule.main"))).container, gbc);
		++gbc.gridx;
		panel.add(this.buttonPredefined = new CGButton("gamerule.predefined"), gbc);
		--gbc.gridx;
		++gbc.gridy;
		panel.add(this.checkboxQuery = new CGCheckBox("gamerule.query"), gbc);
		++gbc.gridx;
		panel.add(new HelpLabel("gamerule.query.help"), gbc);
		--gbc.gridx;
		++gbc.gridy;
		++gbc.gridwidth;
		panel.add((this.entryValue = new CGEntry(Text.VALUE, "0", null)).container, gbc);

		this.buttonPredefined.addActionListener(this);
		this.checkboxQuery.addActionListener(this);
		this.entryValue.addKeyListener(new KeyListener()
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
		this.checkboxQuery.setSelected(false);
		this.onCheckbox();
	}

	@Override
	protected Text description()
	{
		Text d = this.defaultDescription();
		if (this.checkboxQuery.isSelected()) d = new Text("command." + this.id + ".query");
		else d.addReplacement("<value>", this.entryValue.getText());
		d.addReplacement("<gamerule>", this.entryGamerule.getText());
		return null;
	}

	@Override
	protected void onParsingEnd()
	{
		this.onCheckbox();
	}

	@Override
	public String generate() throws CommandGenerationException
	{
		String command = this.id + " " + this.entryGamerule.getText();
		String gamerule = this.entryGamerule.getText(), value = this.entryValue.getText();
		this.entryGamerule.checkValue(CGEntry.STRING);

		boolean predefined = false;
		for (String rule : this.gamerules)
			if (rule.equals(gamerule))
			{
				predefined = true;
				break;
			}
		if (predefined && !gamerule.equals("gameLoopFunction"))
		{
			if (gamerule.equals("randomTickSpeed") || gamerule.equals("spawnRadius") || gamerule.equals("maxEntityCramming")
					|| gamerule.equals("maxCommandChainLength")) try
			{
				int i = Integer.parseInt(value);
				if (i < 0) throw new WrongValueException(this.entryValue.label.getAbsoluteText(), new Text("error.integer.positive"), value);
			} catch (NumberFormatException e)
			{
				throw new WrongValueException(this.entryValue.label.getAbsoluteText(), new Text("error.integer.positive"), value);
			}
			else if (!value.equals("true") && !value.equals("false")) throw new WrongValueException(this.entryValue.label.getAbsoluteText(), new Text(
					"error.value.truefalse"), value);
		}

		return command + (this.checkboxQuery.isSelected() ? "" : " " + this.entryValue.getText());
	}

	private void onCheckbox()
	{
		this.entryValue.container.setVisible(!this.checkboxQuery.isSelected());
	}

	@Override
	protected void readArgument(int index, String argument, String[] fullCommand) throws CommandGenerationException
	{
		// gamerule <rule> [value]
		if (index == 1)
		{
			this.entryGamerule.setText(argument);
			this.checkboxQuery.setSelected(true);
		}
		if (index == 2)
		{
			this.entryValue.setText(argument);
			this.checkboxQuery.setSelected(false);
		}
	}

	@Override
	public boolean shouldStateClose(ComboboxPanel panel)
	{
		this.entryGamerule.setText(panel.combobox.getValue());
		return true;
	}

}
