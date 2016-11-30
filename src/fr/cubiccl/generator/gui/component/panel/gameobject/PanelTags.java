package fr.cubiccl.generator.gui.component.panel.gameobject;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import fr.cubi.cubigui.CTextArea;
import fr.cubi.cubigui.RoundedCornerBorder;
import fr.cubiccl.generator.gameobject.ObjectRegistry;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.templatetags.ITagCreationListener;
import fr.cubiccl.generator.gameobject.templatetags.TemplateTag;
import fr.cubiccl.generator.gui.component.CGList;
import fr.cubiccl.generator.gui.component.CScrollPane;
import fr.cubiccl.generator.gui.component.button.CGButton;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.utils.Lang;
import fr.cubiccl.generator.utils.Text;

public class PanelTags extends CGPanel implements ListSelectionListener, ActionListener, ITagCreationListener
{
	private static final long serialVersionUID = -6946195818692262924L;

	private CTextArea areaValue;
	private CGButton buttonAdd, buttonRemove;
	/** The Object to check if tags are allowed for. */
	private String currentObject;
	private CGList listTags;
	private ArrayList<TemplateTag> shownTags;
	private TemplateTag[] tags;
	private Tag[] values;

	public PanelTags(String titleID, int tagType)
	{
		this(titleID, ObjectRegistry.getTags(tagType));
	}

	public PanelTags(String titleID, TemplateTag... tags)
	{
		super(titleID);
		this.tags = tags;
		this.values = new Tag[this.tags.length];
		this.shownTags = new ArrayList<TemplateTag>();

		GridBagConstraints gbc = this.createGridBagLayout();
		this.add((this.listTags = new CGList()).scrollPane, gbc);
		++gbc.gridx;
		gbc.fill = GridBagConstraints.BOTH;
		CScrollPane scrollpane;
		this.add(scrollpane = new CScrollPane(this.areaValue = new CTextArea("")), gbc);
		--gbc.gridx;
		++gbc.gridy;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		this.add(this.buttonAdd = new CGButton("general.add"), gbc);
		++gbc.gridx;
		this.add(this.buttonRemove = new CGButton("general.remove"), gbc);

		this.areaValue.setBackground(Color.WHITE);
		this.areaValue.setBorder(new RoundedCornerBorder(true));
		scrollpane.setPreferredSize(this.listTags.scrollPane.getPreferredSize());
		this.listTags.scrollPane.setPreferredSize(scrollpane.getPreferredSize());

		this.listTags.addListSelectionListener(this);
		this.buttonAdd.addActionListener(this);
		this.buttonRemove.addActionListener(this);

		this.updateTranslations();
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == this.buttonAdd) this.getSelectedTag().askValue(this);
		else if (e.getSource() == this.buttonRemove)
		{
			this.values[this.indexOf(this.getSelectedTag())] = null;
			this.updateDisplay();
		}
	}

	@Override
	public void createTag(TemplateTag template, Tag value)
	{
		this.values[this.indexOf(template)] = value;
		this.updateDisplay();
	}

	public TemplateTag getSelectedTag()
	{
		TemplateTag tag = this.tags.length == 0 ? null : this.tags[0];
		for (TemplateTag templateTag : this.tags)
			if (templateTag.name().equals(this.listTags.getValue())) tag = templateTag;
		return tag;
	}

	private int indexOf(TemplateTag template)
	{
		TemplateTag tag = this.getSelectedTag();
		for (int i = 0; i < this.tags.length; ++i)
			if (tag == this.tags[i]) return i;
		return -1;
	}

	public Tag selectedValue()
	{
		return this.valueFor(this.getSelectedTag());
	}

	public void setObjectForTags(String objectID)
	{
		this.currentObject = objectID;
		this.shownTags.clear();
		for (TemplateTag tag : this.tags)
			if (tag.canApplyTo(this.currentObject)) this.shownTags.add(tag);
		this.updateTranslations();
	}

	private void updateDisplay()
	{
		if (this.listTags.getSelectedIndex() == -1) return;
		Tag value = this.selectedValue();
		if (value == null) this.areaValue.setText(this.getSelectedTag().description() + "\n" + Lang.translate("tag.no_value"));
		else this.areaValue.setText(value.template.description() + "\n" + value.toCommand());
	}

	@Override
	public void updateTranslations()
	{
		super.updateTranslations();
		if (this.tags == null) return;
		if (this.tags.length == 0) this.areaValue.setText(new Text("tags.none").toString());
		else
		{
			int selected = this.listTags.getSelectedIndex();
			String[] names = new String[this.shownTags.size()];
			for (int i = 0; i < names.length; ++i)
				names[i] = this.shownTags.get(i).name();
			this.listTags.setValues(names);
			this.listTags.setSelectedIndex(selected == -1 ? 0 : selected);
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e)
	{
		this.updateDisplay();
	}

	private Tag valueFor(TemplateTag template)
	{
		for (Tag tag : this.values)
			if (tag != null && tag.template == template) return tag;
		return null;
	}
}
