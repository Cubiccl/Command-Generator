package fr.cubiccl.generator.gameobject.loottable;

import java.awt.Component;
import java.util.ArrayList;

import fr.cubiccl.generator.gameobject.GameObject;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.tags.TagList;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;
import fr.cubiccl.generator.gui.component.interfaces.IObjectList;
import fr.cubiccl.generator.gui.component.panel.CGPanel;

public class LootTable extends GameObject implements IObjectList
{

	public static LootTable createFrom(TagCompound tag)
	{
		ArrayList<LootTablePool> pools = new ArrayList<LootTablePool>();
		if (tag.hasTag(Tags.LOOTTABLE_POOLS))
		{
			TagList list = (TagList) tag.getTag(Tags.LOOTTABLE_POOLS);
			for (Tag t : list.value())
			{
				LootTablePool p = LootTablePool.createFrom((TagCompound) t);
				if (p != null) pools.add(p);
			}
		}

		LootTable table = new LootTable(pools.toArray(new LootTablePool[pools.size()]));
		table.findName(tag);
		return table;
	}

	public final ArrayList<LootTablePool> pools;

	public LootTable(LootTablePool... pools)
	{
		this.pools = new ArrayList<LootTablePool>();
		for (LootTablePool p : pools)
			this.pools.add(p);
	}

	@Override
	public boolean addObject(CGPanel panel, int editIndex)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public CGPanel createAddPanel(int editIndex)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Component getDisplayComponent(int index)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getValues()
	{
		String[] names = new String[this.pools.size()];
		for (int i = 0; i < names.length; ++i)
			names[i] = this.pools.get(i).toString();
		return names;
	}

	@Override
	public void removeObject(int index)
	{
		this.pools.remove(index);
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
		if (includeName) return new TagCompound(container, new TagList(Tags.LOOTTABLE_POOLS, tags.toArray(new Tag[tags.size()])), this.nameTag());
		return new TagCompound(container, new TagList(Tags.LOOTTABLE_POOLS, tags.toArray(new Tag[tags.size()])));
	}

}
