package fr.cubiccl.generator.command;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import fr.cubiccl.generator.gui.component.combobox.OptionCombobox;
import fr.cubiccl.generator.gui.component.label.CLabel;
import fr.cubiccl.generator.gui.component.panel.CPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelCoordinates;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelTarget;
import fr.cubiccl.generator.gui.component.textfield.CEntry;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Lang;
import fr.cubiccl.generator.utils.MissingValueException;
import fr.cubiccl.generator.utils.WrongValueException;

public class CommandStats extends Command implements ActionListener
{
	private OptionCombobox comboboxSourceMode, comboboxMode, comboboxStat;
	private CEntry entryObjective;
	private CLabel labelStat;
	private PanelCoordinates panelCoordinates;
	private PanelTarget panelTarget, panelSource;

	public CommandStats()
	{
		super("stats");
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == this.comboboxMode)
		{
			boolean set = this.comboboxMode.getValue().equals("set");
			this.entryObjective.container.setVisible(set);
			this.panelTarget.setVisible(set);
			this.labelStat.setTextID("stats.stat." + this.comboboxMode.getValue());
		} else if (e.getSource() == this.comboboxSourceMode)
		{
			boolean block = this.comboboxSourceMode.getValue().equals("block");
			this.panelCoordinates.setVisible(block);
			this.panelSource.setVisible(!block);
		}
	}

	@Override
	public CPanel createGUI()
	{
		CPanel panel = new CPanel();
		GridBagConstraints gbc = panel.createGridBagLayout();

		++gbc.gridwidth;
		panel.add(this.labelDescription(), gbc);
		++gbc.gridy;
		panel.add(this.comboboxSourceMode = new OptionCombobox("stats.mode", "block", "entity"), gbc);
		++gbc.gridy;
		panel.add(this.panelCoordinates = new PanelCoordinates("stats.coordinates"), gbc);
		panel.add(this.panelSource = new PanelTarget("stats.source", PanelTarget.ALL_ENTITIES), gbc);
		++gbc.gridy;
		panel.add(this.comboboxMode = new OptionCombobox("stats.mode", "set", "clear"), gbc);
		++gbc.gridy;
		--gbc.gridwidth;
		panel.add(this.labelStat = new CLabel("stats.stat.set"), gbc);
		++gbc.gridx;
		panel.add(this.comboboxStat = new OptionCombobox("stats.stat", "AffectedBlocks", "AffectedEntities", "AffectedItems", "QueryResult", "SuccessCount"),
				gbc);
		--gbc.gridx;
		++gbc.gridy;
		++gbc.gridwidth;
		panel.add(this.panelTarget = new PanelTarget("stats.target", PanelTarget.ALL_ENTITIES), gbc);
		++gbc.gridy;
		panel.add((this.entryObjective = new CEntry("stats.objective")).container, gbc);

		this.panelSource.setVisible(false);
		this.comboboxSourceMode.addActionListener(this);
		this.comboboxMode.addActionListener(this);

		return panel;
	}

	@Override
	public String generate() throws CommandGenerationException
	{
		String command = "/stats " + this.comboboxSourceMode.getValue() + " ";

		if (this.comboboxSourceMode.getValue().equals("block")) command += this.panelCoordinates.generateCoordinates().toCommand();
		else command += this.panelSource.generateTarget().toCommand();

		command += " " + this.comboboxMode.getValue() + " " + this.comboboxStat.getValue();
		if (this.comboboxMode.getValue().equals("set"))
		{
			String objective = this.entryObjective.getText();
			if (objective.equals("")) throw new MissingValueException(this.entryObjective.label.getAbsoluteText());
			if (objective.contains(" ")) throw new WrongValueException(this.entryObjective.label.getAbsoluteText(), Lang.translate("error.space"), objective);
			command += " " + this.panelTarget.generateTarget().toCommand() + " " + objective;
		}

		return command;
	}

}
