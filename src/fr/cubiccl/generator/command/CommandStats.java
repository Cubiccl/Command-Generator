package fr.cubiccl.generator.command;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import fr.cubiccl.generator.gameobject.Coordinates;
import fr.cubiccl.generator.gameobject.target.Target;
import fr.cubiccl.generator.gui.component.combobox.OptionCombobox;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelCoordinates;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelTarget;
import fr.cubiccl.generator.gui.component.textfield.CGEntry;
import fr.cubiccl.generator.utils.*;

public class CommandStats extends Command implements ActionListener
{
	private OptionCombobox comboboxSourceMode, comboboxMode, comboboxStat;
	private CGEntry entryObjective;
	private CGLabel labelStat;
	private PanelCoordinates panelCoordinates;
	private PanelTarget panelTarget, panelSource;

	public CommandStats()
	{
		super("stats", "stats block <x> <y> <z> clear <stat>\n" + "stats block <x> <y> <z> set <stat> <selector> <objective>\n"
				+ "stats entity <selector2> clear <stat>\n" + "stats entity <selector2> set <stat> <selector> <objective>", 5, 7, 9);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == this.comboboxMode)
		{
			boolean set = this.comboboxMode.getValue().equals("set");
			this.entryObjective.container.setVisible(set);
			this.panelTarget.setVisible(set);
			this.labelStat.setTextID(new Text("stats.stat." + this.comboboxMode.getValue()));
		} else if (e.getSource() == this.comboboxSourceMode)
		{
			boolean block = this.comboboxSourceMode.getValue().equals("block");
			this.panelCoordinates.setVisible(block);
			this.panelSource.setVisible(!block);
		}
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
		panel.add(this.comboboxSourceMode = new OptionCombobox("stats.mode", "block", "entity"), gbc);
		++gbc.gridy;
		panel.add(this.panelCoordinates = new PanelCoordinates("stats.coordinates"), gbc);
		panel.add(this.panelSource = new PanelTarget("stats.source", PanelTarget.ALL_ENTITIES), gbc);
		++gbc.gridy;
		panel.add(this.comboboxMode = new OptionCombobox("stats.mode", "set", "clear"), gbc);
		++gbc.gridy;
		--gbc.gridwidth;
		panel.add(this.labelStat = new CGLabel("stats.stat.set"), gbc);
		++gbc.gridx;
		panel.add(this.comboboxStat = new OptionCombobox("stats.stat", "AffectedBlocks", "AffectedEntities", "AffectedItems", "QueryResult", "SuccessCount"),
				gbc);
		--gbc.gridx;
		++gbc.gridy;
		++gbc.gridwidth;
		panel.add(this.panelTarget = new PanelTarget("stats.target", PanelTarget.ALL_ENTITIES), gbc);
		++gbc.gridy;
		panel.add((this.entryObjective = new CGEntry(new Text("stats.objective"), Text.OBJECTIVE)).container, gbc);

		this.labelStat.setHasColumn(true);
		this.panelSource.setVisible(false);
		this.comboboxSourceMode.addActionListener(this);
		this.comboboxMode.addActionListener(this);

		this.panelTarget.addArgumentChangeListener(this);
		this.panelSource.addArgumentChangeListener(this);
		this.panelCoordinates.addArgumentChangeListener(this);
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
		this.comboboxStat.addActionListener(this);

		return panel;
	}

	@Override
	protected Text description()
	{
		Text d = this.defaultDescription();
		if (this.comboboxSourceMode.getValue().equals("block"))
		{
			if (this.comboboxMode.getValue().equals("clear")) d = new Text("command." + this.id + ".block.clear");
			else d = new Text("command." + this.id + ".block");
		} else if (this.comboboxMode.getValue().equals("clear")) d = new Text("command." + this.id + ".clear");

		String obj = this.entryObjective.getText();
		try
		{
			Utils.checkStringId(null, obj);
		} catch (Exception e)
		{
			obj = "???";
		}

		d.addReplacement("<source>", this.panelSource.displayTarget());
		d.addReplacement("<target>", this.panelTarget.displayTarget());
		d.addReplacement("<coordinates>", this.panelCoordinates.displayCoordinates());
		d.addReplacement("<stat>", new Text("stats.stat.tostring" + this.comboboxStat.getValue()));
		d.addReplacement("<objective>", obj);
		return d;
	}

	@Override
	public String generate() throws CommandGenerationException
	{
		String command = this.id + " " + this.comboboxSourceMode.getValue() + " ";

		if (this.comboboxSourceMode.getValue().equals("block")) command += this.panelCoordinates.generate().toCommand();
		else command += this.panelSource.generate().toCommand();

		command += " " + this.comboboxMode.getValue() + " " + this.comboboxStat.getValue();
		if (this.comboboxMode.getValue().equals("set"))
		{
			String objective = this.entryObjective.getText();
			if (objective.equals("")) throw new MissingValueException(this.entryObjective.label.getAbsoluteText());
			if (objective.contains(" ")) throw new WrongValueException(this.entryObjective.label.getAbsoluteText(), new Text("error.space"), objective);
			command += " " + this.panelTarget.generate().toCommand() + " " + objective;
		}

		return command;
	}

	@Override
	protected void readArgument(int index, String argument, String[] fullCommand) throws CommandGenerationException
	{
		// stats block <x> <y> <z> clear <stat>
		// stats block <x> <y> <z> set <stat> <selector> <objective>
		// stats entity <selector2> clear <stat>
		// stats entity <selector2> set <stat> <selector> <objective>
		if (index == 1) this.comboboxSourceMode.setValue(argument);
		if (this.comboboxSourceMode.getValue().equals("block"))
		{
			if (index == 2) this.panelCoordinates.setupFrom(new Coordinates().fromString(argument, fullCommand[3], fullCommand[4]));
			if (index == 5) this.comboboxMode.setValue(argument);
			if (index == 6) this.comboboxStat.setValue(argument);
			if (index == 7) this.panelTarget.setupFrom(new Target().fromString(argument));
			if (index == 8) this.entryObjective.setText(argument);
		} else
		{
			if (index == 2) this.panelSource.setupFrom(new Target().fromString(argument));
			if (index == 3) this.comboboxMode.setValue(argument);
			if (index == 4) this.comboboxStat.setValue(argument);
			if (index == 5) this.panelTarget.setupFrom(new Target().fromString(argument));
			if (index == 6) this.entryObjective.setText(argument);
		}
	}

}
