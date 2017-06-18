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

/** Represents an editable Loot Table for map making. */
public class LootTable extends GameObject<LootTable> implements IObjectList<LootTable>
{

	/** The list of Pools in this Table. */
	private final ArrayList<LTPool> pools;

	public LootTable()
	{
		this(new LTPool[0]);
	}

	public LootTable(LTPool... pools)
	{
		this.pools = new ArrayList<LTPool>();
		for (LTPool p : pools)
			this.pools.add(p);
	}

	/** Adds a {@link LTPool Pool} to this Loot Table.
	 * 
	 * @param pool - The Pool to add. */
	public void add(LTPool pool)
	{
		this.pools.add(pool);
		this.onChange();
	}

	@Override
	public CGPanel createPanel(ListProperties properties)
	{
		if (properties.isTrue("new"))
		{
			String name = Dialogs.showInputDialog(new Text("objects.name").toString());
			if (name != null) this.setCustomName(name);
			else return null;
			CommandGenerator.window.panelLootTableSelection.list.add(this);
		}
		CommandGenerator.stateManager.clear();
		return new PanelLootTable(this);
	}

	@Override
	public LootTable duplicate(LootTable object)
	{
		this.pools.clear();
		for (LTPool pool : object.pools)
			this.pools.add(new LTPool().duplicate(pool));
		return this;
	}

	@Override
	public LootTable fromNBT(TagCompound nbt)
	{
		if (nbt.hasTag(Tags.LOOTTABLE_POOLS))
		{
			TagList list = nbt.getTag(Tags.LOOTTABLE_POOLS);
			for (Tag t : list.value())
			{
				LTPool p = LTPool.createFrom((TagCompound) t);
				if (p != null) this.pools.add(p);
			}
		}

		this.findName(nbt);
		return this;
	}

	@Override
	public LootTable fromXML(Element xml)
	{
		for (Element pool : xml.getChild("pools").getChildren())
			this.pools.add(new LTPool().fromXML(pool));
		this.findProperties(xml);
		return this;
	}

	/** @return A list of Items generated by this Loot Table. */
	public ArrayList<ItemStack> generateItems()
	{
		ArrayList<ItemStack> items = new ArrayList<ItemStack>();

		for (LTPool pool : this.pools)
			if (pool.verifyConditions()) items.addAll(pool.generateItems());

		return items;
	}

	/** @param i - The Pool index.
	 * @return The {@link LTPool Pool} at the input index. */
	public LTPool get(int i)
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

	/** Getter for {@link LootTable#pools}. */
	public LTPool[] getPools()
	{
		return this.pools.toArray(new LTPool[this.pools.size()]);
	}

	/** Removes a {@link LTPool Pool} from this Loot Table.
	 * 
	 * @param pool - The Pool to remove. */
	public void remove(LTPool pool)
	{
		this.pools.remove(pool);
	}

	/** Sets a {@link LTPool Pool} in this Loot Table.
	 * 
	 * @param index - The index of the Pool.
	 * @param pool - The Pool to set. */
	public void set(int index, LTPool pool)
	{
		this.pools.set(index, pool);
		this.onChange();
	}

	/** @return The number of {@link LTPool Pools} in this Loot Table. */
	public int size()
	{
		return this.pools.size();
	}

	@Override
	public String toCommand()
	{
		return this.toNBT(Tags.DEFAULT_COMPOUND).valueForCommand();
	}

	@Override
	public TagCompound toNBT(TemplateCompound container)
	{
		ArrayList<Tag> tags = new ArrayList<Tag>();
		for (LTPool pool : this.pools)
			tags.add(pool.toTag(Tags.DEFAULT_COMPOUND));

		TagCompound t = container.create(Tags.LOOTTABLE_POOLS.create(tags.toArray(new Tag[tags.size()])));
		t.setJson(true);
		return t;
	}

	@Override
	public String toString()
	{
		return this.customName();
	}

	@Override
	public Element toXML()
	{
		Element pools = new Element("pools");
		for (LTPool pool : this.pools)
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
