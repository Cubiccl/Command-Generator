package fr.cubiccl.generator.gameobject.loottable;

import java.awt.Component;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import fr.cubiccl.generator.CommandGenerator;
import fr.cubiccl.generator.gameobject.GameObject;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.tags.TagList;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;
import fr.cubiccl.generator.gui.component.interfaces.IObjectList;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.loottable.PanelLootTable;
import fr.cubiccl.generator.gui.component.panel.utils.ListProperties;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;

public class LootTable extends GameObject implements IObjectList<LootTable>
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

	public LootTable()
	{
		this(new LootTablePool[0]);
	}

	public LootTable(LootTablePool... pools)
	{
		this.pools = new ArrayList<LootTablePool>();
		for (LootTablePool p : pools)
			this.pools.add(p);
	}

	@Override
	public CGPanel createPanel(ListProperties properties)
	{
		if ((boolean) properties.get("new"))
		{
			this.setCustomName(JOptionPane.showInputDialog(new Text("objects.name")));
			if (this.customName() == null) return null;
			CommandGenerator.window.panelLootTableSelection.list.add(this);
		}
		CommandGenerator.stateManager.clear();
		return new PanelLootTable(this);
	}

	public String display()
	{
		return this.toTag(Tags.DEFAULT_COMPOUND).valueForCommand(0);
	}

	@Override
	public Component getDisplayComponent()
	{
		return null;
	}

	@Override
	public String getName(int index)
	{
		return this.customName();
	}

	@Override
	public LootTable setupFrom(CGPanel panel) throws CommandGenerationException
	{
		return null;
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

		TagCompound t;
		if (includeName) t = new TagCompound(container, new TagList(Tags.LOOTTABLE_POOLS, tags.toArray(new Tag[tags.size()])), this.nameTag());
		else t = new TagCompound(container, new TagList(Tags.LOOTTABLE_POOLS, tags.toArray(new Tag[tags.size()])));
		t.setJson(true);
		return t;
	}

}
