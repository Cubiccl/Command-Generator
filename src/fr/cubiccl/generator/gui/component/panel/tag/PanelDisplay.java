package fr.cubiccl.generator.gui.component.panel.tag;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.ButtonGroup;

import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.tags.TagList;
import fr.cubiccl.generator.gameobject.tags.TagNumber;
import fr.cubiccl.generator.gameobject.tags.TagString;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.custom.TemplateDisplay;
import fr.cubiccl.generator.gui.component.button.CGCheckBox;
import fr.cubiccl.generator.gui.component.button.CGRadioButton;
import fr.cubiccl.generator.gui.component.interfaces.IObjectList;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.utils.EntryPanel;
import fr.cubiccl.generator.gui.component.panel.utils.PanelColor;
import fr.cubiccl.generator.gui.component.panel.utils.PanelObjectList;
import fr.cubiccl.generator.gui.component.textfield.CGEntry;
import fr.cubiccl.generator.utils.Replacement;
import fr.cubiccl.generator.utils.Text;

public class PanelDisplay extends CGPanel implements IObjectList, ActionListener
{
	private static final long serialVersionUID = 979622679531646803L;

	private CGRadioButton buttonTranslate, buttonName, buttonNone;
	private CGCheckBox checkboxDye;
	private CGEntry entryName;
	private ArrayList<String> lore;
	private PanelColor panelColor;

	public PanelDisplay()
	{
		this.lore = new ArrayList<String>();
		GridBagConstraints gbc = this.createGridBagLayout();

		this.add(this.buttonNone = new CGRadioButton("display.none"), gbc);
		++gbc.gridx;
		this.add(this.buttonName = new CGRadioButton("display.name.use"), gbc);
		++gbc.gridx;
		this.add(this.buttonTranslate = new CGRadioButton("display.translate"), gbc);

		gbc.gridx = 0;
		++gbc.gridy;
		gbc.gridwidth = 3;
		this.add((this.entryName = new CGEntry("display.name", "")).container, gbc);
		++gbc.gridy;
		this.add(new PanelObjectList(this), gbc);
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

	@Override
	public boolean addObject(CGPanel panel)
	{
		return this.lore.add(((EntryPanel) panel).entry.getText());
	}

	@Override
	public CGPanel createAddPanel()
	{
		return new EntryPanel("display.lore");
	}

	public TagCompound generateDisplay(TemplateDisplay container)
	{
		ArrayList<Tag> tags = new ArrayList<Tag>();

		if (this.buttonTranslate.isSelected()) tags.add(new TagString(Tags.DISPLAY_LOCAL, this.entryName.getText()));
		else if (this.buttonName.isSelected()) tags.add(new TagString(Tags.DISPLAY_NAME, this.entryName.getText()));

		TagString[] lores = new TagString[this.lore.size()];
		for (int i = 0; i < lores.length; ++i)
			lores[i] = new TagString(Tags.DEFAULT_STRING, this.lore.get(i));
		tags.add(new TagList(Tags.DISPLAY_LORE, lores));

		if (this.checkboxDye.isSelected()) tags.add(new TagNumber(Tags.DISPLAY_COLOR, this.panelColor.getValue()));

		return new TagCompound(container, tags.toArray(new Tag[tags.size()]));
	}

	@Override
	public Text getName(int index)
	{
		String value = this.lore.get(index);
		if (value.length() <= 40) return new Text(value, false);
		return new Text(value.substring(0, 37) + "...", false);
	}

	@Override
	public BufferedImage getTexture(int index)
	{
		return null;
	}

	@Override
	public String[] getValues()
	{
		String[] values = new String[this.lore.size()];
		for (int i = 0; i < values.length; i++)
			values[i] = new Text("display.lore.x", new Replacement("<index>", Integer.toString(i))).toString();
		return values;
	}

	@Override
	public void removeObject(int index)
	{
		this.lore.remove(index);
	}

	public void setupFrom(TagCompound previousValue)
	{
		if (previousValue.hasTag(Tags.DISPLAY_LOCAL.id))
		{
			this.buttonTranslate.setSelected(true);
			this.entryName.setVisible(true);
			this.entryName.setText((String) previousValue.getTagFromId(Tags.DISPLAY_LOCAL.id).value());
		}
		if (previousValue.hasTag(Tags.DISPLAY_NAME.id))
		{
			this.buttonTranslate.setSelected(true);
			this.entryName.setVisible(true);
			this.entryName.setText((String) previousValue.getTagFromId(Tags.DISPLAY_NAME.id).value());
		}
		if (previousValue.hasTag(Tags.DISPLAY_LORE.id))
		{
			this.lore.clear();
			TagList lores = (TagList) previousValue.getTagFromId(Tags.DISPLAY_LORE.id);
			for (Tag t : lores.value())
				this.lore.add((String) t.value());
		}
		if (previousValue.hasTag(Tags.DISPLAY_COLOR.id))
		{
			this.checkboxDye.setSelected(true);
			this.panelColor.setVisible(true);
			this.panelColor.setupFrom((int) previousValue.getTagFromId(Tags.DISPLAY_COLOR.id).value());
		}
	}

}
