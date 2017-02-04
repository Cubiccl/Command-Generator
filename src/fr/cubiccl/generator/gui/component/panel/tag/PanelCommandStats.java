package fr.cubiccl.generator.gui.component.panel.tag;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import fr.cubi.cubigui.CTextArea;
import fr.cubiccl.generator.CommandGenerator;
import fr.cubiccl.generator.gameobject.target.Target;
import fr.cubiccl.generator.gui.component.combobox.OptionCombobox;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelTarget;
import fr.cubiccl.generator.gui.component.textfield.CGEntry;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;

public class PanelCommandStats extends CGPanel implements ActionListener
{
	private static final long serialVersionUID = -8742873921480384531L;

	public static final byte SUCCESS_COUNT = 0, AFFECTED_BLOCKS = 1, AFFECTED_ENTITIES = 2, AFFECTED_ITEMS = 3, QUERY_RESULT = 4;

	private OptionCombobox comboboxMode;
	private int currentSelection;
	private CGEntry entryObjective;
	private String[] objectives;
	private PanelTarget panelTarget;
	private Target[] targets;

	public PanelCommandStats()
	{
		this.targets = new Target[5];
		this.objectives = new String[]
		{ "", "", "", "", "" };
		this.currentSelection = 0;

		GridBagConstraints gbc = this.createGridBagLayout();
		this.add(this.comboboxMode = new OptionCombobox("stats.stat", "SuccessCount", "AffectedBlocks", "AffectedEntities", "AffectedItems", "QueryResult"),
				gbc);
		++gbc.gridy;
		this.add(new CTextArea(new Text("stats.description").toString()), gbc);
		++gbc.gridy;
		this.add((this.entryObjective = new CGEntry(new Text("stats.objective"), Text.OBJECTIVE)).container, gbc);
		++gbc.gridy;
		this.add(this.panelTarget = new PanelTarget("stats.target", PanelTarget.ALL_ENTITIES), gbc);

		this.comboboxMode.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (this.currentSelection == this.comboboxMode.getSelectedIndex()) return;

		try
		{
			this.saveCurrent();
		} catch (CommandGenerationException e1)
		{
			CommandGenerator.report(e1);
			this.comboboxMode.setSelectedIndex(this.currentSelection);
			return;
		}

		this.currentSelection = this.comboboxMode.getSelectedIndex();
		this.panelTarget.setupFrom(this.targets[this.currentSelection]);
		this.entryObjective.setText(this.objectives[this.currentSelection]);
	}

	public String getObjective(byte statID)
	{
		return this.objectives[statID];
	}

	public Target getTarget(byte statID)
	{
		return this.targets[statID];
	}

	public void saveCurrent() throws CommandGenerationException
	{
		this.targets[this.currentSelection] = this.panelTarget.generate();
		this.objectives[this.currentSelection] = this.entryObjective.getText();
	}

	public void setObjective(byte statID, String objectiveName)
	{
		this.objectives[statID] = objectiveName;
		if (statID == this.currentSelection) this.entryObjective.setText(objectiveName);
	}

	public void setTarget(byte statID, Target target)
	{
		this.targets[statID] = target;
		if (statID == this.currentSelection) this.panelTarget.setupFrom(target);
	}

}
