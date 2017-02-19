package fr.cubiccl.generator.gui.component.panel.tag;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ButtonGroup;

import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.tags.TagList;
import fr.cubiccl.generator.gameobject.tags.TagString;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.custom.TemplateDisplay;
import fr.cubiccl.generator.gui.component.button.CGCheckBox;
import fr.cubiccl.generator.gui.component.button.CGRadioButton;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.utils.PanelColor;
import fr.cubiccl.generator.gui.component.panel.utils.PanelObjectList;
import fr.cubiccl.generator.gui.component.textfield.CGEntry;
import fr.cubiccl.generator.utils.Text;

public class PanelDisplay extends CGPanel implements ActionListener
{
	private static final long serialVersionUID = 979622679531646803L;

	private CGRadioButton buttonTranslate, buttonName, buttonNone;
	private CGCheckBox checkboxDye;
	private CGEntry entryName;
	private PanelObjectList<Text> list;
	private PanelColor panelColor;

	public PanelDisplay()
	{
		GridBagConstraints gbc = this.createGridBagLayout();

		this.add(this.buttonNone = new CGRadioButton("display.none"), gbc);
		++gbc.gridx;
		this.add(this.buttonName = new CGRadioButton("display.name.use"), gbc);
		++gbc.gridx;
		this.add(this.buttonTranslate = new CGRadioButton("display.translate"), gbc);

		gbc.gridx = 0;
		++gbc.gridy;
		gbc.gridwidth = 3;
		this.add((this.entryName = new CGEntry(new Text("display.name"), new Text("display.name"))).container, gbc);
		++gbc.gridy;
		this.add(this.list = new PanelObjectList<Text>("display.lore.title", "display.lore.x", Text.class), gbc);
		++gbc.gridy;
		this.add(this.checkboxDye = new CGCheckBox("display.dye.check"), gbc);
		++gbc.gridy;
		this.add(this.panelColor = new PanelColor("display.dye"), gbc);

		this.checkboxDye.addActionListener(this);
		this.buttonNone.addActionListener(this);
		this.buttonName.addActionListener(this);
		this.buttonTranslate.addActionListener(this);

		this.entryName.container.setVisible(false);
		this.panelColor.setVisible(false);

		ButtonGroup group = new ButtonGroup();
		group.add(this.buttonNone);
		group.add(this.buttonName);
		group.add(this.buttonTranslate);
		this.buttonNone.setSelected(true);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == this.checkboxDye) this.panelColor.setVisible(this.checkboxDye.isSelected());
		else this.entryName.container.setVisible(!this.buttonNone.isSelected());
	}

	public TagCompound generateDisplay(TemplateDisplay container)
	{
		ArrayList<Tag> tags = new ArrayList<Tag>();

		if (this.buttonTranslate.isSelected()) tags.add(Tags.DISPLAY_LOCAL.create(this.entryName.getText()));
		else if (this.buttonName.isSelected()) tags.add(Tags.DISPLAY_NAME.create(this.entryName.getText()));

		TagString[] lores = new TagString[this.list.length()];
		for (int i = 0; i < lores.length; ++i)
			lores[i] = Tags.DEFAULT_STRING.create(this.list.get(i).toString());
		tags.add(Tags.DISPLAY_LORE.create(lores));

		if (this.checkboxDye.isSelected()) tags.add(Tags.DISPLAY_COLOR.create(this.panelColor.getValue()));

		return container.create(tags.toArray(new Tag[tags.size()]));
	}

	public void setupFrom(TagCompound previousValue)
	{
		if (previousValue.hasTag(Tags.DISPLAY_LOCAL.id()))
		{
			this.buttonTranslate.setSelected(true);
			this.entryName.setVisible(true);
			this.entryName.setText((String) previousValue.getTagFromId(Tags.DISPLAY_LOCAL.id()).value());
		}
		if (previousValue.hasTag(Tags.DISPLAY_NAME.id()))
		{
			this.buttonName.setSelected(true);
			this.entryName.setVisible(true);
			this.entryName.setText((String) previousValue.getTagFromId(Tags.DISPLAY_NAME.id()).value());
		}
		if (previousValue.hasTag(Tags.DISPLAY_LORE.id()))
		{
			this.list.clear();
			TagList lores = (TagList) previousValue.getTagFromId(Tags.DISPLAY_LORE.id());
			for (Tag t : lores.value())
				this.list.add(new Text((String) t.value(), false));
		}
		if (previousValue.hasTag(Tags.DISPLAY_COLOR.id()))
		{
			this.checkboxDye.setSelected(true);
			this.panelColor.setVisible(true);
			this.panelColor.setupFrom((int) previousValue.getTagFromId(Tags.DISPLAY_COLOR.id()).value());
		}
	}
}
