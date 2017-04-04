package fr.cubiccl.generator.gameobject.templatetags.custom;

import fr.cubiccl.generator.gameobject.PlayerRecipe;
import fr.cubiccl.generator.gameobject.baseobjects.BaseObject;
import fr.cubiccl.generator.gameobject.tags.NBTReader;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.tags.TagString;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateString;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.utils.PanelObjectList;

public class TemplateRecipeBook extends TemplateString
{

	public TemplateRecipeBook(String id, byte applicationType, String[] applicable)
	{
		super(id, applicationType, applicable);
	}

	@Override
	protected CGPanel createPanel(BaseObject object, Tag previousValue)
	{
		PanelObjectList<PlayerRecipe> panel = new PanelObjectList<PlayerRecipe>(null, (String) null, PlayerRecipe.class);
		if (previousValue != null)
		{
			Tag[] tags = ((TagCompound) NBTReader.read(((TagString) previousValue).value(), true, true, true)).value();
			for (Tag tag : tags)
				if (tag instanceof TagCompound) panel.add(PlayerRecipe.fromNBT((TagCompound) tag));
		}
		panel.setName(this.title());
		return panel;
	}

	@Override
	public TagString generateTag(BaseObject object, CGPanel panel)
	{
		@SuppressWarnings("unchecked")
		PlayerRecipe[] recipes = ((PanelObjectList<PlayerRecipe>) panel).values();
		Tag[] tags = new Tag[recipes.length];
		for (int i = 0; i < tags.length; ++i)
			tags[i] = recipes[i].toNBT();
		return this.create(Tags.DEFAULT_COMPOUND.create(tags).setJson(true).valueForCommand());
	}

}
