package fr.cubiccl.generator.gui.component.panel.advancement;

import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;
import fr.cubiccl.generator.gui.component.combobox.OptionCombobox;
import fr.cubiccl.generator.gui.component.panel.utils.PanelTestValues;
import fr.cubiccl.generator.utils.Utils;

public class PanelCriteriaLocation extends PanelTestValues
{
	private static final long serialVersionUID = 8159722392050714702L;

	private OptionCombobox comboboxBiome, comboboxFeature, comboboxDimension;
	private PanelPosition panelPosition;

	public PanelCriteriaLocation()
	{
		this.addComponent("criteria.location.position", this.panelPosition = new PanelPosition("criteria.location.position"));
		this.addComponent("criteria.location.biome", this.comboboxBiome = new OptionCombobox("tag.biome", Utils.BIOMES));
		this.addComponent("criteria.location.feature", this.comboboxFeature = new OptionCombobox("locate", Utils.STRUCTURES));
		this.addComponent("criteria.location.dimension", this.comboboxDimension = new OptionCombobox("tag.dimension", Utils.DIMENSIONS));
	}

	public boolean checkInput()
	{
		if (this.isSelected(this.panelPosition)) return this.panelPosition.checkInput();
		return true;
	}

	public TagCompound generateValue(TemplateCompound container)
	{
		TagCompound tag = container.create();
		if (this.isSelected(this.panelPosition)) tag.addTag(this.panelPosition.generateTag(Tags.CRITERIA_POSITION));
		if (this.isSelected(this.comboboxBiome)) tag.addTag(Tags.CRITERIA_BIOME.create(this.comboboxBiome.getValue()));
		if (this.isSelected(this.comboboxFeature)) tag.addTag(Tags.CRITERIA_FEATURE.create(this.comboboxFeature.getValue()));
		if (this.isSelected(this.comboboxDimension)) tag.addTag(Tags.CRITERIA_DIMENSION.create(this.comboboxDimension.getValue()));
		return tag;
	}

	public void setupFrom(TagCompound tag)
	{
		if (tag.hasTag(Tags.CRITERIA_POSITION))
		{
			this.select(this.panelPosition);
			this.panelPosition.setupFrom(tag.getTag(Tags.CRITERIA_POSITION));
		}
		if (tag.hasTag(Tags.CRITERIA_BIOME))
		{
			this.select(this.comboboxBiome);
			this.comboboxBiome.setValue(tag.getTag(Tags.CRITERIA_BIOME).value());
		}
		if (tag.hasTag(Tags.CRITERIA_FEATURE))
		{
			this.select(this.comboboxFeature);
			this.comboboxFeature.setValue(tag.getTag(Tags.CRITERIA_FEATURE).value());
		}
		if (tag.hasTag(Tags.CRITERIA_DIMENSION))
		{
			this.select(this.comboboxDimension);
			this.comboboxDimension.setValue(tag.getTag(Tags.CRITERIA_DIMENSION).value());
		}
	}

}
