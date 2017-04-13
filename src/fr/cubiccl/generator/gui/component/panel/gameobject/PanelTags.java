package fr.cubiccl.generator.gui.component.panel.gameobject;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import fr.cubi.cubigui.CTextArea;
import fr.cubi.cubigui.RoundedCornerBorder;
import fr.cubiccl.generator.gameobject.baseobjects.BaseObject;
import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.tags.TagString;
import fr.cubiccl.generator.gameobject.templatetags.ITagCreationListener;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;
import fr.cubiccl.generator.gameobject.templatetags.TemplateNumber;
import fr.cubiccl.generator.gameobject.templatetags.TemplateTag;
import fr.cubiccl.generator.gameobject.templatetags.custom.TemplateBlockId;
import fr.cubiccl.generator.gameobject.templatetags.custom.TemplateItemId;
import fr.cubiccl.generator.gameobject.templatetags.custom.TemplateParticle;
import fr.cubiccl.generator.gameobject.templatetags.custom.TemplatePatterns;
import fr.cubiccl.generator.gui.component.CGList;
import fr.cubiccl.generator.gui.component.CScrollPane;
import fr.cubiccl.generator.gui.component.button.CGButton;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.utils.Lang;
import fr.cubiccl.generator.utils.Settings;

public class PanelTags extends CGPanel implements ListSelectionListener, ActionListener, ITagCreationListener
{
	private static final long serialVersionUID = -6946195818692262924L;

	private CTextArea areaValue;
	private CGButton buttonAdd, buttonRemove;
	/** The Object the Tags will be applied to. */
	private BaseObject currentObject;
	private CGList listTags;
	private ArrayList<TemplateTag> shownTags;
	private TemplateTag[] tags;
	private HashMap<TemplateTag, Tag> values;

	public PanelTags(String titleID, int applicationType)
	{
		this(titleID, ObjectRegistry.listTags(applicationType));
	}

	public PanelTags(String titleID, TemplateTag... tags)
	{
		super(titleID);
		this.tags = tags;
		this.values = new HashMap<TemplateTag, Tag>();
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
		this.areaValue.setLineWrap(true);
		this.areaValue.setWrapStyleWord(true);
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
		if (e.getSource() == this.buttonAdd && this.selectedTag() != null)
		{
			TemplateTag tag = this.selectedTag();
			BaseObject objectToGive = this.currentObject;
			for (TemplateTag t : this.shownTags)
			{
				if (t.id().equals("DisplayData") && tag instanceof TemplateItemId && this.valueFor(t) != null) ((TemplateItemId) tag).damage = (int) (double) this
						.valueFor(t).value();
				else if (t.id().equals("carriedData") && tag instanceof TemplateBlockId && this.valueFor(t) != null) ((TemplateBlockId) tag).damage = (int) (double) this
						.valueFor(t).value();
				else if (t.id().equals("blockData") && tag instanceof TemplateBlockId && this.valueFor(t) != null) ((TemplateBlockId) tag).damage = (int) (double) this
						.valueFor(t).value();
				else if (t.id().equals("Data") && tag instanceof TemplateItemId && this.valueFor(t) != null) ((TemplateItemId) tag).damage = (int) (double) this
						.valueFor(t).value();
				else if (t.id().equals("Base") && tag instanceof TemplatePatterns && this.valueFor(t) != null) ((TemplatePatterns) tag).base = (int) (double) this
						.valueFor(t).value();
				else if (tag.id().equals("TileEntityData") && t.id().equals("Block")) objectToGive = this.valueFor(t) == null ? ObjectRegistry.blocks.first()
						: ObjectRegistry.blocks.find(((TagString) this.valueFor(t)).value());
				else if (t.id().equals("ParticleParam1") && tag instanceof TemplateParticle && this.valueFor(t) != null) ((TemplateParticle) tag).param1 = (int) (double) this
						.valueFor(t).value();
				else if (t.id().equals("ParticleParam2") && tag instanceof TemplateParticle && this.valueFor(t) != null) ((TemplateParticle) tag).param2 = (int) (double) this
						.valueFor(t).value();
			}

			tag.askValue(objectToGive, this.selectedValue(), this);
		} else if (e.getSource() == this.buttonRemove && this.selectedTag() != null)
		{
			this.values.remove(this.selectedTag());
			this.updateDisplay();
		}
	}

	@Override
	public void createTag(TemplateTag template, Tag value)
	{
		this.values.put(template, value);
		TemplateTag t;
		if ((t = this.getTag("Base")) != null && template instanceof TemplatePatterns && template.id().equals("Patterns"))
		{
			this.values.put(t, ((TemplateNumber) t).create(((TemplatePatterns) template).base));
			((TemplatePatterns) template).base = -1;
		} else if ((t = this.getTag("Data")) != null && template instanceof TemplateItemId && (template.id().equals("Item") || template.id().equals("Block")))
		{
			this.values.put(t, ((TemplateNumber) t).create(((TemplateItemId) template).damage));
			((TemplateItemId) template).damage = -1;
		} else if ((t = this.getTag("carriedData")) != null && template instanceof TemplateBlockId && template.id().equals("carried"))
		{
			this.values.put(t, ((TemplateNumber) t).create(((TemplateBlockId) template).damage));
			((TemplateBlockId) template).damage = -1;
		} else if ((t = this.getTag("blockData")) != null && template instanceof TemplateBlockId && template.id().equals("blockId"))
		{
			this.values.put(t, ((TemplateNumber) t).create(((TemplateBlockId) template).damage));
			((TemplateBlockId) template).damage = -1;
		} else if ((t = this.getTag("DisplayData")) != null && template instanceof TemplateItemId && template.id().equals("DisplayTile"))
		{
			this.values.put(t, ((TemplateNumber) t).create(((TemplateItemId) template).damage));
			((TemplateItemId) template).damage = -1;
		} else if ((t = this.getTag("ParticleParam1")) != null && template instanceof TemplateParticle && template.id().equals("Particle"))
		{
			this.values.put(t, ((TemplateNumber) t).create(((TemplateParticle) template).param1));
			((TemplateParticle) template).param1 = 0;
		}
		if ((t = this.getTag("ParticleParam2")) != null && template instanceof TemplateParticle && template.id().equals("Particle"))
		{
			this.values.put(t, ((TemplateNumber) t).create(((TemplateParticle) template).param2));
			((TemplateParticle) template).param2 = 0;
		}
		this.updateDisplay();
	}

	public TagCompound generateTags(TemplateCompound container)
	{
		ArrayList<Tag> tags = new ArrayList<Tag>();
		for (Tag tag : this.values.values())
			if (tag != null && this.shownTags.contains(tag.template)) tags.add(tag);
		return container.create(tags.toArray(new Tag[tags.size()]));
	}

	private TemplateTag getTag(String id)
	{
		for (TemplateTag tag : this.tags)
			if (tag.id().equals(id)) return tag;
		return null;
	}

	public TemplateTag selectedTag()
	{
		if (this.shownTags.size() == 0 || this.listTags.getSelectedIndex() == -1) return null;
		return this.shownTags.get(this.listTags.getSelectedIndex());
	}

	public Tag selectedValue()
	{
		return this.valueFor(this.selectedTag());
	}

	public void setTags(TemplateTag... tags)
	{
		this.tags = tags;
		this.setTargetObject(this.currentObject);
	}

	public void setTargetObject(BaseObject object)
	{
		this.currentObject = object;
		this.shownTags.clear();
		Set<TemplateTag> templates = new HashSet<TemplateTag>();
		templates.addAll(this.values.keySet());
		for (TemplateTag template : templates)
			if (this.getTag(template.id()) == null || !template.canApplyTo(this.currentObject)) this.values.remove(template);
		for (TemplateTag template : this.tags)
			if (template.canApplyTo(this.currentObject)) this.shownTags.add(template);

		this.updateTranslations();
	}

	public void setValues(Tag[] value)
	{
		this.values.clear();

		for (Tag t : value)
			this.values.put(t.template, t);
		this.updateDisplay();
	}

	private void updateDisplay()
	{
		if (this.listTags.getSelectedIndex() == -1 || this.shownTags.size() == -1) return;
		Tag value = this.selectedValue();
		if (value == null) this.areaValue.setText(this.selectedTag().description(this.currentObject) + "\n" + Lang.translate("tag.no_value"));
		else this.areaValue.setText(value.template.description(this.currentObject) + "\n"
				+ value.toCommand(Settings.getSetting(Settings.INDENTATION).equals("true") ? 0 : -1));
	}

	@Override
	public void updateTranslations()
	{
		super.updateTranslations();
		if (this.tags == null) return;
		if (this.shownTags.size() == 0)
		{
			this.listTags.setValues(new String[0]);
			this.areaValue.setText("");
		} else
		{
			int selected = this.listTags.getSelectedIndex();
			String[] names = new String[this.shownTags.size()];
			for (int i = 0; i < names.length; ++i)
				names[i] = this.shownTags.get(i).id();
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
		if (template == null) return null;
		return this.values.get(template);
	}
}
