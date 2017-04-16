package fr.cubiccl.generator.gui.component.panel.advancement;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

import fr.cubiccl.generator.gameobject.advancements.Advancement;
import fr.cubiccl.generator.gameobject.advancements.AdvancementCriteria;
import fr.cubiccl.generator.gui.component.CGList;
import fr.cubiccl.generator.gui.component.button.CGButton;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.utils.Text;

public class PanelRequirement extends CGPanel implements ActionListener
{
	private static final long serialVersionUID = 1524613792193016918L;

	private HashMap<AdvancementCriteria, Boolean> added;
	private Advancement advancement;
	private CGButton buttonAdd, buttonRemove;
	private CGList listAdded, listRemaining;

	public PanelRequirement(Advancement advancement)
	{
		this.advancement = advancement;
		this.added = new HashMap<AdvancementCriteria, Boolean>();
		for (AdvancementCriteria criteria : this.advancement.getCriteria())
			this.added.put(criteria, false);

		GridBagConstraints gbc = this.createGridBagLayout();
		this.add(new CGLabel("advancement.requirement.remaining"), gbc);
		gbc.gridx = 2;
		this.add(new CGLabel("advancement.requirement.added"), gbc);

		gbc.gridx = 0;
		++gbc.gridy;
		++gbc.gridheight;
		this.add((this.listRemaining = new CGList()).scrollPane, gbc);
		gbc.gridx = 2;
		this.add((this.listAdded = new CGList()).scrollPane, gbc);

		--gbc.gridx;
		--gbc.gridheight;
		this.add(this.buttonAdd = new CGButton(new Text(">>>", false)), gbc);
		++gbc.gridy;
		this.add(this.buttonRemove = new CGButton(new Text("<<<", false)), gbc);

		this.buttonAdd.addActionListener(this);
		this.buttonRemove.addActionListener(this);
		this.updateDisplay();
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == this.buttonAdd) for (AdvancementCriteria criteria : this.advancement.getCriteria())
		{
			if (criteria.name.equals(this.listRemaining.getValue()))
			{
				this.added.put(criteria, true);
				break;
			}
		}
		else if (e.getSource() == this.buttonRemove) for (AdvancementCriteria criteria : this.advancement.getCriteria())
			if (criteria.name.equals(this.listAdded.getValue()))
			{
				this.added.put(criteria, false);
				break;
			}

		this.updateDisplay();
	}

	public Integer[] generate()
	{
		ArrayList<Integer> list = new ArrayList<Integer>();
		AdvancementCriteria[] criteria = this.advancement.getCriteria();
		for (int i = 0; i < criteria.length; ++i)
			if (this.added.get(criteria[i])) list.add(i);
		return list.toArray(new Integer[list.size()]);
	}

	public void setupFrom(Integer[] criterias)
	{
		for (AdvancementCriteria criteria : this.advancement.getCriteria())
			this.added.put(criteria, false);
		for (Integer i : criterias)
			this.added.put(this.advancement.getCriteria()[i], true);
	}

	private void updateDisplay()
	{
		ArrayList<String> a = new ArrayList<String>(), r = new ArrayList<String>();
		for (AdvancementCriteria criteria : this.advancement.getCriteria())
			if (this.added.get(criteria)) a.add(criteria.name);
			else r.add(criteria.name);

		this.listAdded.setValues(a.toArray(new String[a.size()]));
		this.listRemaining.setValues(r.toArray(new String[r.size()]));
	}

}
