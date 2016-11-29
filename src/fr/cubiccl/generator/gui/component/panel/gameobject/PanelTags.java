package fr.cubiccl.generator.gui.component.panel.gameobject;

import java.awt.Color;
import java.awt.GridBagConstraints;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import fr.cubi.cubigui.CTextArea;
import fr.cubi.cubigui.RoundedCornerBorder;
import fr.cubiccl.generator.gameobject.ObjectRegistry;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.templatetags.TemplateTag;
import fr.cubiccl.generator.gui.component.CGList;
import fr.cubiccl.generator.gui.component.CScrollPane;
import fr.cubiccl.generator.gui.component.button.CGButton;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.utils.Lang;
import fr.cubiccl.generator.utils.Text;

public class PanelTags extends CGPanel implements ListSelectionListener
{
	private static final long serialVersionUID = -6946195818692262924L;

	private CTextArea areaValue;
	private CGButton buttonAdd, buttonRemove;
	private CGList listTags;
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

		this.listTags.addListSelectionListener(this);

		this.updateTranslations();
	}

	public TemplateTag getSelectedTag()
	{
		TemplateTag tag = this.tags.length == 0 ? null : this.tags[0];
		for (TemplateTag templateTag : this.tags)
			if (templateTag.name().equals(this.listTags.getValue())) tag = templateTag;
		return tag;
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
			String[] names = new String[this.tags.length];
			for (int i = 0; i < names.length; ++i)
				names[i] = this.tags[i].name();
			this.listTags.setValues(names);
			this.listTags.setSelectedIndex(selected == -1 ? 0 : selected);
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e)
	{
		if (this.listTags.getSelectedIndex() == -1) return;
		Tag value = this.values[this.listTags.getSelectedIndex()];
		if (value == null) this.areaValue.setText(this.tags[this.listTags.getSelectedIndex()].description() + "\n" + Lang.translate("tag.no_value"));
		else this.areaValue.setText(value.template.description() + "\n" + value.toCommand());
	}
}
