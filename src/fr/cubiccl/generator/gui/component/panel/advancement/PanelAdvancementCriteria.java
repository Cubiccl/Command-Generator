package fr.cubiccl.generator.gui.component.panel.advancement;

import java.awt.GridBagConstraints;

import fr.cubiccl.generator.gameobject.advancements.AdvancementCriteria;
import fr.cubiccl.generator.gameobject.advancements.CriteriaTrigger;
import fr.cubiccl.generator.gui.component.combobox.SearchCombobox;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelTags;
import fr.cubiccl.generator.gui.component.textfield.CGEntry;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;

public class PanelAdvancementCriteria extends CGPanel
{
	private static final long serialVersionUID = 3551546365226712129L;

	private SearchCombobox comboboxTrigger;
	private CGEntry entryName;
	private CGLabel labelDescription;
	private PanelTags panelTags;

	public PanelAdvancementCriteria(AdvancementCriteria criteria)
	{
		GridBagConstraints gbc = this.createGridBagLayout();
		this.add((this.entryName = new CGEntry("general.name")).container, gbc);
		++gbc.gridy;
		this.add((this.comboboxTrigger = new SearchCombobox(CriteriaTrigger.names())).container, gbc);
		++gbc.gridy;
		this.add(this.labelDescription = new CGLabel((Text) null), gbc);
		++gbc.gridy;
		this.add(this.panelTags = new PanelTags("advancement.criteria.tags"));

		this.setupFrom(criteria);
		this.onTriggerSelection();
	}

	private void onTriggerSelection()
	{
		this.labelDescription.setTextID(this.selectedTrigger().description());
		// this.panelTags.setTags(this.selectedTrigger().conditions);
	}

	private CriteriaTrigger selectedTrigger()
	{
		return CriteriaTrigger.find(this.comboboxTrigger.getValue());
	}

	private void setupFrom(AdvancementCriteria criteria)
	{
		this.entryName.setText(criteria.name);
	}

	public void update(AdvancementCriteria criteria) throws CommandGenerationException
	{
		this.entryName.checkValue(CGEntry.STRING);
		criteria.name = this.entryName.getText();
		criteria.trigger = this.selectedTrigger();
	}

}
