package fr.cubiccl.generator.command;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import fr.cubiccl.generator.CommandGenerator;
import fr.cubiccl.generator.gui.component.button.CButton;
import fr.cubiccl.generator.gui.component.panel.CPanel;
import fr.cubiccl.generator.gui.component.panel.utils.ComboboxPanel;
import fr.cubiccl.generator.gui.component.textfield.CEntry;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.IStateListener;
import fr.cubiccl.generator.utils.Lang;
import fr.cubiccl.generator.utils.WrongValueException;

public class CommandGamerule extends Command implements ActionListener, IStateListener<ComboboxPanel>
{
	public static final String[] GAMERULES =
	{ "commandBlockOutput", "disableElytraMovementCheck", "doDaylightCycle", "doEntityDrops", "doFireTick", "doMobLoot", "doMobSpawning", "doTileDrops",
			"keepInventory", "logAdminCommands", "mobGriefing", "naturalRegeneration", "randomTickSpeed", "reducedDebugInfo", "sendCommandFeedback",
			"showDeathMessages", "spawnRadius", "spectatorsGenerateChunks" };
	private CButton buttonPredefined;
	private CEntry entryGamerule, entryValue;

	public CommandGamerule()
	{
		super("gamerule");
	}

	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		CommandGenerator.stateManager.setState(new ComboboxPanel("gamerule.predefined.select", "gamerule", GAMERULES), this);
	}

	@Override
	public CPanel createGUI()
	{
		CPanel panel = new CPanel();
		GridBagConstraints gbc = panel.createGridBagLayout();

		++gbc.gridwidth;
		panel.add(this.labelDescription(), gbc);
		++gbc.gridy;
		--gbc.gridwidth;
		panel.add((this.entryGamerule = new CEntry("gamerule.entry")).container, gbc);
		++gbc.gridx;
		panel.add(this.buttonPredefined = new CButton("gamerule.predefined"), gbc);
		--gbc.gridx;
		++gbc.gridy;
		++gbc.gridwidth;
		panel.add((this.entryValue = new CEntry("score.value", "0")).container, gbc);

		this.entryValue.addIntFilter();

		this.buttonPredefined.addActionListener(this);

		return panel;
	}

	@Override
	public String generate() throws CommandGenerationException
	{
		String command = "/gamerule " + this.entryGamerule.getText();
		String gamerule = this.entryGamerule.getText(), value = this.entryValue.getText();

		boolean predefined = false;
		for (String rule : GAMERULES)
			if (rule.equals(gamerule))
			{
				predefined = true;
				break;
			}
		if (predefined)
		{
			if (gamerule.equals("randomTickSpeed") || gamerule.equals("spawnRadius")) try
			{
				int i = Integer.parseInt(value);
				if (i < 0) throw new WrongValueException(this.entryValue.label.getAbsoluteText(), Lang.translate("error.integer.positive"), value);
			} catch (NumberFormatException e)
			{
				throw new WrongValueException(this.entryValue.label.getAbsoluteText(), Lang.translate("error.integer.positive"), value);
			}
			else if (!value.equals("true") && !value.equals("false")) throw new WrongValueException(this.entryValue.label.getAbsoluteText(),
					Lang.translate("error.value.truefalse"), value);
		}

		return command + " " + this.entryValue.getText();
	}

	@Override
	public boolean shouldStateClose(ComboboxPanel panel)
	{
		this.entryGamerule.setText(panel.combobox.getValue());
		return true;
	}

}
