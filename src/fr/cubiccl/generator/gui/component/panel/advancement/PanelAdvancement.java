package fr.cubiccl.generator.gui.component.panel.advancement;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.SwingConstants;

import fr.cubi.cubigui.CTextArea;
import fr.cubiccl.generator.CommandGenerator;
import fr.cubiccl.generator.gameobject.advancements.Advancement;
import fr.cubiccl.generator.gameobject.advancements.AdvancementCriteria;
import fr.cubiccl.generator.gameobject.advancements.Requirement;
import fr.cubiccl.generator.gui.component.button.CGButton;
import fr.cubiccl.generator.gui.component.button.CGCheckBox;
import fr.cubiccl.generator.gui.component.button.CGRadioButton;
import fr.cubiccl.generator.gui.component.combobox.OptionCombobox;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.label.HelpLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.CGTabbedPane;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelItem;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelJsonMessage;
import fr.cubiccl.generator.gui.component.panel.utils.PanelObjectList;
import fr.cubiccl.generator.gui.component.textfield.CGEntry;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;

public class PanelAdvancement extends CGPanel implements ActionListener
{

	private static final long serialVersionUID = -773099244243224959L;

	public final Advancement advancement;
	private CTextArea areaReqDesc;
	private CGButton buttonSave, buttonCancel;
	private CGRadioButton buttonTString, buttonTJson;
	private CGCheckBox checkboxAnnounce, checkboxToast, checkboxAppears;
	private OptionCombobox comboboxFrame;
	private PanelObjectList<AdvancementCriteria> criteria;
	private CGEntry entryTitle, entryDescription, entryBackground, entryParent, entryExperience, entryFunction;
	private PanelItem panelItem;
	private PanelObjectList<Requirement> panelRequirements;
	private PanelJsonMessage panelTitleJson;
	private PanelObjectList<Text> recipes, loot;
	private CGTabbedPane tabbedPane;

	public PanelAdvancement(Advancement advancement)
	{
		this.advancement = advancement;
		CGLabel l = new CGLabel(new Text(this.advancement.customName(), false));
		l.setFont(l.getFont().deriveFont(Font.BOLD, 30));
		l.setHorizontalAlignment(SwingConstants.CENTER);

		CGPanel p = new CGPanel();
		GridBagConstraints gbc = p.createGridBagLayout();
		p.add(this.buttonSave = new CGButton("objects.save"), gbc);
		++gbc.gridx;
		p.add(this.buttonCancel = new CGButton("general.cancel_edit"), gbc);

		CGPanel title = new CGPanel("advancement.title");
		gbc = title.createGridBagLayout();
		title.add(this.buttonTString = new CGRadioButton("advancement.title.string"), gbc);
		++gbc.gridx;
		title.add(this.buttonTJson = new CGRadioButton("advancement.title.json"), gbc);
		--gbc.gridx;
		++gbc.gridy;
		++gbc.gridwidth;
		title.add((this.entryTitle = new CGEntry("general.name")).container, gbc);
		title.add(this.panelTitleJson = new PanelJsonMessage(false), gbc);

		CGPanel display = new CGPanel();

		gbc = display.createGridBagLayout();
		gbc.gridwidth = 3;
		display.add(title, gbc);
		++gbc.gridy;
		display.add(this.panelItem = new PanelItem("advancement.display_item", true, false, false), gbc);
		++gbc.gridy;
		display.add(p, gbc);
		++gbc.gridy;
		display.add((this.entryDescription = new CGEntry("general.description")).container, gbc);
		++gbc.gridy;
		gbc.gridwidth = 2;
		display.add((this.entryParent = new CGEntry("advancement.parent")).container, gbc);
		gbc.gridx = 2;
		gbc.gridwidth = 1;
		display.add(new HelpLabel("advancement.parent.help"), gbc);
		gbc.gridx = 0;
		++gbc.gridy;
		gbc.gridwidth = 2;
		display.add((this.entryBackground = new CGEntry("advancement.background")).container, gbc);
		gbc.gridx = 2;
		gbc.gridwidth = 1;
		display.add(new HelpLabel("advancement.background.help"), gbc);
		gbc.gridx = 0;

		++gbc.gridy;
		display.add(new CGLabel("advancement.frame").setHasColumn(true), gbc);
		++gbc.gridx;
		gbc.gridwidth = 2;
		display.add(this.comboboxFrame = new OptionCombobox("advancement.frame", "task", "goal", "challenge"), gbc);

		--gbc.gridx;
		++gbc.gridy;
		gbc.gridwidth = 3;
		display.add(this.checkboxAnnounce = new CGCheckBox("advancement.announce"), gbc);
		++gbc.gridy;
		display.add(this.checkboxToast = new CGCheckBox("advancement.toast"), gbc);
		++gbc.gridy;
		display.add(this.checkboxAppears = new CGCheckBox("advancement.appears"), gbc);

		CGPanel rewards = new CGPanel();
		gbc = rewards.createGridBagLayout();
		rewards.add((this.entryExperience = new CGEntry("advancement.experience")).container, gbc);
		++gbc.gridy;
		rewards.add(this.recipes = new PanelObjectList<Text>("advancement.recipes", (Text) null, Text.class, "description", "advancement.recipe"), gbc);
		++gbc.gridy;
		rewards.add(this.loot = new PanelObjectList<Text>("advancement.loots", (Text) null, Text.class, "description", "advancement.loot"), gbc);
		++gbc.gridy;
		rewards.add((this.entryFunction = new CGEntry("advancement.function")).container, gbc);
		this.entryFunction.addHelpLabel(new HelpLabel("advancement.function.help"));

		CGPanel criterias = new CGPanel();
		gbc = criterias.createGridBagLayout();
		criterias.add(this.criteria = new PanelObjectList<AdvancementCriteria>("advancement.criteria", "advancement.criteria", AdvancementCriteria.class));
		++gbc.gridy;
		criterias.add(this.panelRequirements = new PanelObjectList<Requirement>("advancement.requirements", "", Requirement.class, "advancement",
				this.advancement), gbc);
		++gbc.gridy;
		criterias.add(this.areaReqDesc = new CTextArea(new Text("advancement.requirements.description").toString()), gbc);
		this.areaReqDesc.setWrapStyleWord(true);
		this.areaReqDesc.setLineWrap(true);

		this.tabbedPane = new CGTabbedPane();
		this.tabbedPane.addTab(new Text("advancement.display"), display);
		this.tabbedPane.addTab(new Text("advancement.criteria"), criterias);
		this.tabbedPane.addTab(new Text("advancement.rewards"), rewards);

		gbc = this.createGridBagLayout();
		this.add(l, gbc);
		++gbc.gridy;
		this.add(this.tabbedPane, gbc);
		++gbc.gridy;
		this.add(p, gbc);

		this.panelItem.setHasDurability(false);
		this.panelItem.setHasAmount(false);

		this.buttonSave.addActionListener(this);
		this.buttonCancel.addActionListener(this);
		this.buttonTString.addActionListener(this);
		this.buttonTJson.addActionListener(this);

		ButtonGroup group = new ButtonGroup();
		group.add(this.buttonTString);
		group.add(this.buttonTJson);

		this.cancel();
		this.updateTranslations();
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == this.buttonSave) this.save();
		else if (e.getSource() == this.buttonCancel) this.cancel();
		else if (e.getSource() == this.buttonTJson || e.getSource() == this.buttonTString) this.updateTitleType();
	}

	private void cancel()
	{
		this.panelItem.setItem(this.advancement.getItem());
		this.panelItem.setDamage(this.advancement.getData());
		this.comboboxFrame.setValue(this.advancement.frame);
		this.entryTitle.setText(this.advancement.title == null ? "" : this.advancement.title);
		this.entryDescription.setText(this.advancement.description == null ? "" : this.advancement.description);
		this.entryBackground.setText(this.advancement.background == null ? "" : this.advancement.background);
		this.entryParent.setText(this.advancement.parent == null ? "" : this.advancement.parent);
		if (this.advancement.jsonTitle != null) this.panelTitleJson.setupFrom(this.advancement.jsonTitle);
		this.checkboxAnnounce.setSelected(this.advancement.announce);
		this.checkboxToast.setSelected(this.advancement.toast);
		this.checkboxAppears.setSelected(!this.advancement.hidden);

		this.criteria.clear();
		for (AdvancementCriteria criteria : this.advancement.getCriteria())
			this.criteria.add(criteria);
		this.panelRequirements.clear();
		for (AdvancementCriteria[] req : this.advancement.requirements)
			this.panelRequirements.add(new Requirement(req));

		this.entryExperience.setText(Integer.toString(this.advancement.rewardExperience));
		this.recipes.clear();
		for (String s : this.advancement.rewardRecipes)
			this.recipes.add(new Text(s, false));
		this.loot.clear();
		for (String s : this.advancement.rewardLoot)
			this.loot.add(new Text(s, false));
		if (this.advancement.rewardFunction == null) this.entryFunction.setText("");
		else this.entryFunction.setText(this.advancement.rewardFunction);

		this.buttonTString.setSelected(this.advancement.title != null);
		this.buttonTJson.setSelected(this.advancement.jsonTitle != null);
		this.updateTitleType();
	}

	private void save()
	{
		if (this.buttonTJson.isSelected()) try
		{
			this.entryExperience.checkValue(CGEntry.INTEGER);
			this.advancement.jsonTitle = this.panelTitleJson.generate();
		} catch (CommandGenerationException e)
		{
			e.printStackTrace();
			CommandGenerator.report(e);
			return;
		}
		else this.advancement.jsonTitle = null;
		if (this.buttonTString.isSelected()) this.advancement.title = this.entryTitle.getText();
		else this.advancement.title = null;
		this.advancement.announce = this.checkboxAnnounce.isSelected();
		this.advancement.toast = this.checkboxToast.isSelected();
		this.advancement.hidden = !this.checkboxAppears.isSelected();

		this.advancement.setItem(this.panelItem.selectedItem());
		this.advancement.setData(this.panelItem.selectedDamage());
		this.advancement.frame = this.comboboxFrame.getValue();
		this.advancement.description = this.entryDescription.getText();
		this.advancement.background = this.entryBackground.getText();
		if (this.advancement.background.equals("")) this.advancement.background = null;
		this.advancement.parent = this.entryParent.getText();
		if (this.advancement.parent.equals("")) this.advancement.parent = null;

		this.advancement.clearCriteria();
		for (AdvancementCriteria criteria : this.criteria.values())
			this.advancement.addCriterion(criteria);

		this.advancement.rewardExperience = Integer.parseInt(this.entryExperience.getText());
		this.advancement.rewardRecipes.clear();
		for (Text t : this.recipes.values())
			this.advancement.rewardRecipes.add(t.id);
		this.advancement.rewardLoot.clear();
		for (Text t : this.loot.values())
			this.advancement.rewardLoot.add(t.id);
		if (this.entryFunction.getText().equals("")) this.advancement.rewardFunction = null;
		else this.advancement.rewardFunction = this.entryFunction.getText();

		this.advancement.requirements.clear();
		for (Requirement r : this.panelRequirements.values())
			this.advancement.requirements.add(r.criterias);

		CommandGenerator.window.panelAdvancementSelection.list.updateList();
	}

	private void updateTitleType()
	{
		this.entryTitle.container.setVisible(this.buttonTString.isSelected());
		this.panelTitleJson.setVisible(this.buttonTJson.isSelected());
	}

	@Override
	public void updateTranslations()
	{
		super.updateTranslations();
		if (this.areaReqDesc != null) this.areaReqDesc.setText(new Text("advancement.requirements.description").toString());
	}

}
