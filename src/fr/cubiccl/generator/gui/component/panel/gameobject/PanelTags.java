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
import fr.cubiccl.generator.gameobject.baseobjects.BaseObject;
import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.tags.TagNumber;
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
	private Tag[] values;

	public PanelTags(String titleID, int tagType)
	{
		this(titleID, ObjectRegistry.listTags(tagType));
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
		if (e.getSource() == this.buttonAdd && this.getSelectedTag() != null)
		{
			TemplateTag tag = this.getSelectedTag();
			BaseObject objectToGive = this.currentObject;
			for (TemplateTag t : this.shownTags)
			{
				if (t.id().equals("DisplayData") && tag instanceof TemplateItemId && this.valueFor(t) != null) ((TemplateItemId) tag).damage = (int) this
						.valueFor(t).value();
				else if (t.id().equals("carriedData") && tag instanceof TemplateBlockId && this.valueFor(t) != null) ((TemplateBlockId) tag).damage = (int) this
						.valueFor(t).value();
				else if (t.id().equals("blockData") && tag instanceof TemplateBlockId && this.valueFor(t) != null) ((TemplateBlockId) tag).damage = (int) this
						.valueFor(t).value();
				else if (t.id().equals("Data") && tag instanceof TemplateItemId && this.valueFor(t) != null) ((TemplateItemId) tag).damage = (int) this
						.valueFor(t).value();
				else if (t.id().equals("Base") && tag instanceof TemplatePatterns && this.valueFor(t) != null) ((TemplatePatterns) tag).base = (int) this
						.valueFor(t).value();
				else if (tag.id().equals("TileEntityData") && t.id().equals("Block")) objectToGive = this.valueFor(t) == null ? ObjectRegistry.blocks.first()
						: ObjectRegistry.blocks.find(((TagString) this.valueFor(t)).value());
				else if (t.id().equals("ParticleParam1") && tag instanceof TemplateParticle && this.valueFor(t) != null) ((TemplateParticle) tag).param1 = (int) this
						.valueFor(t).value();
				else if (t.id().equals("ParticleParam2") && tag instanceof TemplateParticle && this.valueFor(t) != null) ((TemplateParticle) tag).param2 = (int) this
						.valueFor(t).value();
			}

			tag.askValue(objectToGive, this.selectedValue(), this);
		} else if (e.getSource() == this.buttonRemove && this.getSelectedTag() != null)
		{
			this.values[this.indexOf(this.getSelectedTag())] = null;
			this.updateDisplay();
		}
	}

	@Override
	public void createTag(TemplateTag template, Tag value)
	{
		this.values[this.indexOf(template)] = value;
		boolean particles = false;
		for (int i = 0; i < this.tags.length; ++i)
		{
			if (this.tags[i].id().equals("Base") && template instanceof TemplatePatterns && template.id().equals("Patterns"))
			{
				this.values[i] = new TagNumber((TemplateNumber) this.tags[i], ((TemplatePatterns) template).base);
				((TemplatePatterns) template).base = -1;
				break;
			} else if (this.tags[i].id().equals("Data") && template instanceof TemplateItemId
					&& (template.id().equals("Item") || template.id().equals("Block")))
			{
				this.values[i] = new TagNumber((TemplateNumber) this.tags[i], ((TemplateItemId) template).damage);
				((TemplateItemId) template).damage = -1;
				break;
			} else if (this.tags[i].id().equals("carriedData") && template instanceof TemplateBlockId && template.id().equals("carried"))
			{
				this.values[i] = new TagNumber((TemplateNumber) this.tags[i], ((TemplateBlockId) template).damage);
				((TemplateBlockId) template).damage = -1;
				break;
			}  else if (this.tags[i].id().equals("blockData") && template instanceof TemplateBlockId && template.id().equals("blockId"))
			{
				this.values[i] = new TagNumber((TemplateNumber) this.tags[i], ((TemplateBlockId) template).damage);
				((TemplateBlockId) template).damage = -1;
				break;
			} else if (this.tags[i].id().equals("DisplayData") && template instanceof TemplateItemId && template.id().equals("DisplayTile"))
			{
				this.values[i] = new TagNumber((TemplateNumber) this.tags[i], ((TemplateItemId) template).damage);
				((TemplateItemId) template).damage = -1;
				break;
			} else if (this.tags[i].id().equals("ParticleParam1") && template instanceof TemplateParticle && template.id().equals("Particle"))
			{
				this.values[i] = new TagNumber((TemplateNumber) this.tags[i], ((TemplateParticle) template).param1);
				((TemplateParticle) template).param1 = 0;
				if (particles) break;
				particles = true;
			} else if (this.tags[i].id().equals("ParticleParam2") && template instanceof TemplateParticle && template.id().equals("Particle"))
			{
				this.values[i] = new TagNumber((TemplateNumber) this.tags[i], ((TemplateParticle) template).param2);
				((TemplateParticle) template).param2 = 0;
				if (particles) break;
				particles = true;
			}
		}
		this.updateDisplay();
	}

	public TagCompound generateTags(TemplateCompound container)
	{
		ArrayList<Tag> tags = new ArrayList<Tag>();
		for (Tag tag : this.values)
			if (tag != null) tags.add(tag);
		return new TagCompound(container, tags.toArray(new Tag[tags.size()]));
	}

	public TemplateTag getSelectedTag()
	{
		if (this.shownTags.size() == 0 || this.listTags.getSelectedIndex() == -1) return null;
		return this.shownTags.get(this.listTags.getSelectedIndex());
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

	public void setTags(Tag[] value)
	{
		for (int i = 0; i < this.values.length; i++)
			this.values[i] = null;
		for (Tag t : value)
		{
			for (int i = 0; i < this.tags.length; ++i)
			{
				if (this.tags[i].id().equals(t.id()))
				{
					this.values[i] = t;
					break;
				}
			}
		}
	}

	public void setTargetObject(BaseObject object)
	{
		this.currentObject = object;
		this.shownTags.clear();
		for (int i = 0; i < this.tags.length; ++i)
			if (this.tags[i].canApplyTo(this.currentObject)) this.shownTags.add(this.tags[i]);
			else this.values[i] = null;

		this.updateTranslations();
	}

	private void updateDisplay()
	{
		if (this.listTags.getSelectedIndex() == -1 || this.shownTags.size() == -1) return;
		Tag value = this.selectedValue();
		if (value == null) this.areaValue.setText(this.getSelectedTag().description(this.currentObject) + "\n" + Lang.translate("tag.no_value"));
		else this.areaValue.setText(value.template.description(this.currentObject) + "\n" + value.toCommand());
	}

	@Override
	public void updateTranslations()
	{
		super.updateTranslations();
		if (this.tags == null) return;
		if (this.shownTags.size() == 0) this.areaValue.setText("");
		else
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
		for (Tag tag : this.values)
			if (tag != null && tag.template == template) return tag;
		return null;
	}
}
