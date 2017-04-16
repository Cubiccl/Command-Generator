package fr.cubiccl.generator.gameobject.templatetags.custom;

import fr.cubiccl.generator.gameobject.baseobjects.BaseObject;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.templatetags.TemplateString;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.utils.ComboboxPanel;

public class TemplatePotion extends TemplateString
{
	public static final String[] POTIONS =
	{ "empty", "water", "mundane", "thick", "awkward", "night_vision", "long_night_vision", "invisibility", "long_invisibility", "leaping", "strong_leaping",
			"long_leaping", "fire_resistance", "long_fire_resistance", "swiftness", "strong_swiftness", "long_swiftness", "slowness", "long_slowness",
			"water_breathing", "long_water_breathing", "healing", "strong_healing", "harming", "strong_harming", "poison", "strong_poison", "long_poison",
			"regeneration", "strong_regeneration", "long_regeneration", "strength", "strong_strength", "long_strength", "weakness", "long_weakness", "luck" };

	public TemplatePotion(String id, byte applicationType, String... applicable)
	{
		super(id, applicationType, applicable);
		this.setValues(POTIONS);
	}

	@Override
	protected CGPanel createPanel(BaseObject object, Tag previousValue)
	{
		ComboboxPanel p = new ComboboxPanel(this.description(object), "tag.Potion", this.authorizedValues);
		if (previousValue != null) for (int i = 0; i < this.authorizedValues.length; ++i)
			if ((this.minecraftPrefix && this.authorizedValues[i].equals(((String) previousValue.value()).substring("minecraft:".length())))
					|| (!this.minecraftPrefix && this.authorizedValues[i].equals(previousValue.value()))) p.combobox.setSelectedIndex(i);
		return p;
	}

	@Override
	protected boolean isInputValid(BaseObject object, CGPanel panel)
	{
		return true;
	}

}
