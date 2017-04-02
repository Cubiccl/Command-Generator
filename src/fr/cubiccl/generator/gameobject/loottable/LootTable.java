package fr.cubiccl.generator.gameobject.loottable;

import java.awt.Component;
import java.util.ArrayList;

import org.jdom2.Element;

import fr.cubiccl.generator.CommandGenerator;
import fr.cubiccl.generator.gameobject.GameObject;
import fr.cubiccl.generator.gameobject.ItemStack;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.tags.TagList;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;
import fr.cubiccl.generator.gui.Dialogs;
import fr.cubiccl.generator.gui.component.interfaces.IObjectList;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.loottable.PanelLootTable;
import fr.cubiccl.generator.gui.component.panel.utils.ListProperties;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;

public class LootTable extends GameObject implements IObjectList<LootTable>
{

	public static LootTable createFrom(Element table)
	{
		LootTable t = new LootTable();
		for (Element pool : table.getChild("pools").getChildren())
			t.pools.add(LootTablePool.createFrom(pool));
		t.findProperties(table);
		return t;
	}

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

	private final ArrayList<LootTablePool> pools;

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

	public void add(LootTablePool pool)
	{
		this.pools.add(pool);
		this.onChange();
	}

	@Override
	public CGPanel createPanel(ListProperties properties)
	{
		if ((boolean) properties.get("new"))
		{
			String name = Dialogs.showInputDialog(new Text("objects.name").toString());
			if (name != null) this.setCustomName(name);
			else return null;
			CommandGenerator.window.panelLootTableSelection.list.add(this);
		}
		CommandGenerator.stateManager.clear();
		return new PanelLootTable(this);
	}

	public String display()
	{
		return this.toTag(Tags.DEFAULT_COMPOUND).valueForCommand(0);
	}

	public ArrayList<ItemStack> generateItems()
	{
		ArrayList<ItemStack> items = new ArrayList<ItemStack>();

		for (LootTablePool pool : this.pools)
			if (pool.verifyConditions()) items.addAll(pool.generateItems());

		return items;
	}

	public LootTablePool get(int i)
	{
		return this.pools.get(i);
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

	public LootTablePool[] getPools()
	{
		return this.pools.toArray(new LootTablePool[this.pools.size()]);
	}

	public void remove(LootTablePool pool)
	{
		this.pools.remove(pool);
	}

	public void set(int index, LootTablePool pool)
	{
		this.pools.set(index, pool);
		this.onChange();
	}

	public int size()
	{
		return this.pools.size();
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
	public TagCompound toTag(TemplateCompound container)
	{
		ArrayList<Tag> tags = new ArrayList<Tag>();
		for (LootTablePool pool : this.pools)
			tags.add(pool.toTag(Tags.DEFAULT_COMPOUND));

		TagCompound t = container.create(Tags.LOOTTABLE_POOLS.create(tags.toArray(new Tag[tags.size()])));
		t.setJson(true);
		return t;
	}

	@Override
	public Element toXML()
	{
		Element pools = new Element("pools");
		for (LootTablePool pool : this.pools)
			pools.addContent(pool.toXML());
		return this.createRoot("table").addContent(pools);
	}

	@Deprecated
	@Override
	public LootTable update(CGPanel panel) throws CommandGenerationException
	{
		return this;
	}

}
