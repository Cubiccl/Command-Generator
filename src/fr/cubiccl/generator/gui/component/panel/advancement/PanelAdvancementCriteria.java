package fr.cubiccl.generator.gui.component.panel.advancement;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import fr.cubiccl.generator.gameobject.advancements.AdvancementCriterion;
import fr.cubiccl.generator.gameobject.advancements.CriterionTrigger;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.templatetags.TemplateTag;
import fr.cubiccl.generator.gui.component.combobox.SearchCombobox;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelTags;
import fr.cubiccl.generator.gui.component.textfield.CGEntry;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;

public class PanelAdvancementCriteria extends CGPanel implements ActionListener
{
	private static final long serialVersionUID = 3551546365226712129L;

	private SearchCombobox comboboxTrigger;
	private CGEntry entryName;
	private CGLabel labelDescription;
	private PanelTags panelTags;

	public PanelAdvancementCriteria(AdvancementCriterion criteria)
	{
		GridBagConstraints gbc = this.createGridBagLayout();
		gbc.gridwidth = 2;
		this.add((this.entryName = new CGEntry("general.name")).container, gbc);
		++gbc.gridy;
		gbc.gridwidth = 1;
		this.add(new CGLabel("advancement.criteria.description").setHasColumn(true), gbc);
		++gbc.gridx;
		this.add((this.comboboxTrigger = new SearchCombobox(CriterionTrigger.names())).container, gbc);
		--gbc.gridx;
		++gbc.gridy;
		gbc.gridwidth = 2;
		this.add(this.labelDescription = new CGLabel((Text) null), gbc);
		++gbc.gridy;
		this.add(this.panelTags = new PanelTags("advancement.criteria.tags", CriterionTrigger.listTags()), gbc);

		this.comboboxTrigger.addActionListener(this);

		this.setupFrom(criteria);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == this.comboboxTrigger) this.onTriggerSelection();
	}

	private void onTriggerSelection()
	{
		this.labelDescription.setTextID(this.selectedTrigger().description());
		this.panelTags.setTargetObject(this.selectedTrigger());
		this.panelTags.setTags(this.selectedTrigger().conditions.toArray(new TemplateTag[this.selectedTrigger().conditions.size()]));
		this.panelTags.setVisible(this.selectedTrigger().conditions.size() > 0);
	}

	private CriterionTrigger selectedTrigger()
	{
		return CriterionTrigger.find(this.comboboxTrigger.getValue());
	}

	private void setupFrom(AdvancementCriterion criteria)
	{
		this.entryName.setText(criteria.name);
		this.comboboxTrigger.setSelectedItem(criteria.trigger.id);
		this.onTriggerSelection();
		this.panelTags.setValues(criteria.getConditions());
	}

	public void update(AdvancementCriterion criteria) throws CommandGenerationException
	{
		this.entryName.checkValue(CGEntry.STRING);
		criteria.name = this.entryName.getText();
		criteria.trigger = this.selectedTrigger();
		criteria.clearConditions();
		for (Tag t : this.panelTags.getValues())
			criteria.addCondition(t);
	}

}
