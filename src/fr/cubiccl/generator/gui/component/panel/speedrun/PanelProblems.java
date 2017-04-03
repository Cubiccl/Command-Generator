package fr.cubiccl.generator.gui.component.panel.speedrun;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import fr.cubi.cubigui.CTextArea;
import fr.cubi.cubigui.RoundedCornerBorder;
import fr.cubiccl.generator.gameobject.speedrun.MissingItemsError;
import fr.cubiccl.generator.gameobject.speedrun.Speedrun;
import fr.cubiccl.generator.gui.component.CGList;
import fr.cubiccl.generator.gui.component.button.CGButton;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;

public class PanelProblems extends CGPanel implements ListSelectionListener, ActionListener
{
	private static final long serialVersionUID = 8496389805076984987L;

	private CTextArea areaDescription;
	private CGButton buttonSee;
	private CGLabel labelFine;
	private CGList listProblems;
	public final PanelSpeedrun parent;
	private MissingItemsError[] problems;
	public final Speedrun speedrun;

	public PanelProblems(Speedrun speedrun, PanelSpeedrun parent)
	{
		super("speedrun.problems");
		this.speedrun = speedrun;
		this.parent = parent;

		GridBagConstraints gbc = this.createGridBagLayout();
		gbc.fill = GridBagConstraints.VERTICAL;
		this.add(this.listProblems = new CGList(), gbc);
		this.add(this.labelFine = new CGLabel("speedrun.problems.nothing"), gbc);
		++gbc.gridx;
		this.add(this.areaDescription = new CTextArea(""), gbc);
		++gbc.gridx;
		gbc.fill = GridBagConstraints.NONE;
		this.add(this.buttonSee = new CGButton("speedrun.problems.see"), gbc);

		this.listProblems.addListSelectionListener(this);
		this.buttonSee.addActionListener(this);
		this.areaDescription.setBackground(Color.WHITE);
		this.areaDescription.setBorder(new RoundedCornerBorder(true));
		this.areaDescription.setPreferredSize(new Dimension(400, 100));

		this.onSpeedrunUpdate();
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		this.parent.selectCheckpoint(this.selectedProblem().checkpoint);
	}

	private void onSelection()
	{
		if (this.selectedProblem() == null) this.areaDescription.setText("");
		else this.areaDescription.setText(this.selectedProblem().description());
		this.buttonSee.setEnabled(this.selectedProblem() != null && this.selectedProblem().checkpoint != null);
	}

	public void onSpeedrunUpdate()
	{
		int previous = this.listProblems.getSelectedIndex();

		this.problems = this.speedrun.listProblems();
		String[] names = new String[this.problems.length];
		for (int i = 0; i < names.length; ++i)
			names[i] = this.problems[i].name().toString();
		this.listProblems.setValues(names);

		boolean valid = this.speedrun.isValid();
		this.listProblems.setVisible(!valid);
		this.areaDescription.setVisible(!valid);
		this.buttonSee.setVisible(!valid);
		this.labelFine.setVisible(valid);

		this.listProblems.setSelectedIndex(previous >= this.problems.length ? this.problems.length - 1 : previous);
		this.onSelection();
	}

	private MissingItemsError selectedProblem()
	{
		return this.listProblems.getSelectedIndex() == -1 ? null : this.problems[this.listProblems.getSelectedIndex()];
	}

	@Override
	public void valueChanged(ListSelectionEvent e)
	{
		this.onSelection();
	}

}
