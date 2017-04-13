package fr.cubiccl.generator.gui.component.panel.advancement;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.SwingConstants;

import fr.cubiccl.generator.CommandGenerator;
import fr.cubiccl.generator.gameobject.advancements.Advancement;
import fr.cubiccl.generator.gui.component.button.CGButton;
import fr.cubiccl.generator.gui.component.button.CGRadioButton;
import fr.cubiccl.generator.gui.component.combobox.OptionCombobox;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.label.HelpLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.CGTabbedPane;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelItem;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelJsonMessage;
import fr.cubiccl.generator.gui.component.textfield.CGEntry;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;

public class PanelAdvancement extends CGPanel implements ActionListener
{
	private static final long serialVersionUID = -773099244243224959L;

	public final Advancement advancement;
	private CGButton buttonSave, buttonCancel;
	private CGRadioButton buttonTString, buttonTJson;
	private OptionCombobox comboboxFrame;
	private CGEntry entryTitle, entryDescription, entryBackground, entryParent;
	private PanelItem panelItem;
	private PanelJsonMessage panelTitleJson;
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
		display.add(this.panelItem = new PanelItem("advancement.display_item", false, false, false), gbc);
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

		gbc.gridx = 0;
		++gbc.gridy;
		gbc.gridwidth = 3;
		display.add(p, gbc);

		this.tabbedPane = new CGTabbedPane();
		this.tabbedPane.addTab(new Text("advancement.display"), display);
		this.tabbedPane.addTab(new Text("advancement.criteria"), new CGPanel());
		this.tabbedPane.addTab(new Text("advancement.rewards"), new CGPanel());

		gbc = this.createGridBagLayout();
		this.add(l, gbc);
		++gbc.gridy;
		this.add(this.tabbedPane, gbc);

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
		// TODO Auto-generated method stub
		this.panelItem.setItem(this.advancement.getItem());
		this.comboboxFrame.setValue(this.advancement.frame);
		this.entryTitle.setText(this.advancement.title == null ? "" : this.advancement.title);
		this.entryDescription.setText(this.advancement.description == null ? "" : this.advancement.description);
		this.entryBackground.setText(this.advancement.background == null ? "" : this.advancement.background);
		this.entryParent.setText(this.advancement.parent == null ? "" : this.advancement.parent);
		if (this.advancement.jsonTitle != null) this.panelTitleJson.setupFrom(this.advancement.jsonTitle);

		this.buttonTString.setSelected(this.advancement.title != null);
		this.buttonTJson.setSelected(this.advancement.jsonTitle != null);
		this.updateTitleType();
	}

	private void save()
	{
		if (this.buttonTJson.isSelected()) try
		{
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

		this.advancement.setItem(this.panelItem.selectedItem());
		this.advancement.frame = this.comboboxFrame.getValue();
		this.advancement.description = this.entryDescription.getText();
		this.advancement.background = this.entryBackground.getText();
		if (this.advancement.background.equals("")) this.advancement.background = null;
		this.advancement.parent = this.entryParent.getText();
		if (this.advancement.parent.equals("")) this.advancement.parent = null;
	}

	private void updateTitleType()
	{
		this.entryTitle.container.setVisible(this.buttonTString.isSelected());
		this.panelTitleJson.setVisible(this.buttonTJson.isSelected());
	}

}
