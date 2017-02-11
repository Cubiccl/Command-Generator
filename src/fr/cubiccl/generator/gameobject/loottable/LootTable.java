package fr.cubiccl.generator.gameobject.loottable;

import java.util.ArrayList;

import fr.cubiccl.generator.gameobject.GameObject;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.tags.TagList;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;

public class LootTable extends GameObject
{

	protected final LootTablePool[] pools;

	public LootTable(LootTablePool[] pools)
	{
		this.pools = pools;
	}

	@Override
	public String toCommand()
	{
		return this.toTag(Tags.DEFAULT_COMPOUND).valueForCommand();
	}

	@Override
	public String toString()
	{
		return this.customName();
	}

	@Override
	public TagCompound toTag(TemplateCompound container, boolean includeName)
	{
		ArrayList<Tag> tags = new ArrayList<Tag>();
		for (LootTablePool pool : this.pools)
			tags.add(pool.toTag(Tags.DEFAULT_COMPOUND));
		if (includeName) tags.add(this.nameTag());
		return new TagCompound(container, new TagList(Tags.LOOTTABLE_POOLS, tags.toArray(new Tag[tags.size()])));
	}

}
