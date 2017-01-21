package fr.cubiccl.generator.gui.component.panel.tag;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.tags.TagList;
import fr.cubiccl.generator.gameobject.tags.TagNumber;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;
import fr.cubiccl.generator.gui.component.button.CGCheckBox;
import fr.cubiccl.generator.gui.component.combobox.OptionCombobox;
import fr.cubiccl.generator.gui.component.interfaces.IObjectList;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.utils.PanelColor;
import fr.cubiccl.generator.gui.component.panel.utils.PanelObjectList;
import fr.cubiccl.generator.utils.Text;

public class ExplosionPanel extends CGPanel
{
	private class ColorList implements IObjectList
	{
		private ArrayList<TagNumber> colors;
		private String name;

		public ColorList(String nameID)
		{
			this.name = nameID;
			this.colors = new ArrayList<TagNumber>();
		}

		@Override
		public boolean addObject(CGPanel panel)
		{
			this.colors.add(new TagNumber(Tags.DEFAULT_INTEGER, ((PanelColor) panel).getValue()));
			return true;
		}

		@Override
		public CGPanel createAddPanel()
		{
			return new PanelColor(this.name);
		}

		@Override
		public Text getName(int index)
		{
			return new Text(Integer.toString(this.colors.get(index).value()), false);
		}

		@Override
		public BufferedImage getTexture(int index)
		{
			BufferedImage img = new BufferedImage(40, 40, BufferedImage.TYPE_INT_RGB);
			Graphics g = img.getGraphics();
			g.setColor(new Color(this.colors.get(index).value()));
			g.fillRect(0, 0, 40, 40);
			return img;
		}

		@Override
		public String[] getValues()
		{
			String[] names = new String[this.colors.size()];
			for (int i = 0; i < names.length; i++)
				names[i] = Integer.toString(this.colors.get(i).value());
			return names;
		}

		@Override
		public void removeObject(int index)
		{
			this.colors.remove(index);
		}

		public void setValues(Tag[] value)
		{
			for (Tag t : value)
				if (t instanceof TagNumber) this.colors.add((TagNumber) t);
		}

	}

	private static final long serialVersionUID = 2699563337471683919L;

	private CGCheckBox boxFlicker, boxTrail;
	private OptionCombobox comboboxType;
	private PanelObjectList panelPrimaryColors, panelFadeColors;
	private ColorList primaryColors, fadeColors;

	public ExplosionPanel()
	{
		GridBagConstraints gbc = this.createGridBagLayout();
		this.add(new CGLabel("firework.type").setHasColumn(true), gbc);
		++gbc.gridx;
		this.add(this.comboboxType = new OptionCombobox("firework.type", "small", "large", "star", "creeper", "burst"), gbc);

		--gbc.gridx;
		++gbc.gridy;
		this.add(this.boxFlicker = new CGCheckBox("firework.flicker"), gbc);
		++gbc.gridx;
		this.add(this.boxTrail = new CGCheckBox("firework.trail"), gbc);

		--gbc.gridx;
		++gbc.gridy;
		++gbc.gridwidth;
		this.add(this.panelPrimaryColors = new PanelObjectList(this.primaryColors = new ColorList("firework.colors.primary")), gbc);
		++gbc.gridy;
		this.add(this.panelFadeColors = new PanelObjectList(this.fadeColors = new ColorList("firework.colors.fade")), gbc);
		
		this.setName("tag.title.Explosion");
	}

	public TagCompound generateExplosion(TemplateCompound container)
	{
		return new TagCompound(container, new TagNumber(Tags.FIREWORK_TYPE, this.comboboxType.getSelectedIndex()), new TagNumber(Tags.FIREWORK_FLICKER,
				this.boxFlicker.isSelected() ? 1 : 0), new TagNumber(Tags.FIREWORK_TRAIL, this.boxTrail.isSelected() ? 1 : 0), new TagList(
				Tags.FIREWORK_COLORS, this.primaryColors.colors.toArray(new TagNumber[this.primaryColors.colors.size()])), new TagList(
				Tags.FIREWORK_FADE_COLORS, this.fadeColors.colors.toArray(new TagNumber[this.fadeColors.colors.size()])));
	}

	public void setupFrom(TagCompound previousValue)
	{
		for (Tag t : previousValue.value())
		{
			if (t.id().equals(Tags.FIREWORK_TYPE.id())) this.comboboxType.setSelectedIndex(((TagNumber) t).value());
			else if (t.id().equals(Tags.FIREWORK_FLICKER.id())) this.boxFlicker.setSelected(((TagNumber) t).value() == 1);
			else if (t.id().equals(Tags.FIREWORK_TRAIL.id())) this.boxTrail.setSelected(((TagNumber) t).value() == 1);
			else if (t.id().equals(Tags.FIREWORK_COLORS.id())) this.primaryColors.setValues(((TagList) t).value());
			else if (t.id().equals(Tags.FIREWORK_FADE_COLORS.id())) this.fadeColors.setValues(((TagList) t).value());
		}
		this.panelFadeColors.updateList();
		this.panelPrimaryColors.updateList();
	}

}
