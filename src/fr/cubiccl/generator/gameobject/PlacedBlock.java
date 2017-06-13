package fr.cubiccl.generator.gameobject;

import java.awt.Component;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import org.jdom2.Element;

import fr.cubiccl.generator.gameobject.baseobjects.Block;
import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.gameobject.tags.*;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;
import fr.cubiccl.generator.gui.component.interfaces.IObjectList;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelBlock;
import fr.cubiccl.generator.gui.component.panel.gameobject.display.PanelBlockDisplay;
import fr.cubiccl.generator.gui.component.panel.utils.ListProperties;
import fr.cubiccl.generator.utils.CommandGenerationException;

/** Represents a Block placed in the world. */
public class PlacedBlock extends GameObject<PlacedBlock> implements IObjectList<PlacedBlock>
{

	/** The {@link Block} type. */
	private Block block;
	/** The data of the Block. */
	private int data;
	/** The NBT Tags if this is a Tile Entity. */
	private TagCompound nbt;

	public PlacedBlock()
	{
		this(ObjectRegistry.blocks.find("stone"), 0, Tags.BLOCK_NBT.create());
	}

	public PlacedBlock(Block block, int data, TagCompound nbt)
	{
		this.block = block;
		this.data = data;
		this.nbt = nbt;
	}

	/** @return The number of Items in this Container. If not a Container, returns <code>-1</code>. */
	public int containerSize()
	{
		if (this.nbt.hasTag("Items")) return ((TagList) this.nbt.getTagFromId("Items")).size();
		return -1;
	}

	@Override
	public CGPanel createPanel(ListProperties properties)
	{
		PanelBlock p = new PanelBlock(null, true, true, properties.hasCustomObjects());
		p.setupFrom(this);
		return p;
	}

	@Override
	public PlacedBlock fromNBT(TagCompound nbt)
	{
		for (Tag t : nbt.value())
		{
			if (t.id().equals(Tags.BLOCK_ID.id())) this.block = ObjectRegistry.blocks.find(((TagString) t).value);
			if (t.id().equals(Tags.BLOCK_DATA.id())) this.data = ((TagNumber) t).valueInt();
			if (t.id().equals(Tags.BLOCK_NBT.id())) this.nbt = (TagCompound) t;
		}

		this.findName(nbt);
		return this;
	}

	@Override
	public PlacedBlock fromXML(Element xml)
	{
		this.block = ObjectRegistry.blocks.find(xml.getChildText("id"));
		this.data = Integer.parseInt(xml.getChildText("data"));
		this.nbt = (TagCompound) NBTParser.parse(xml.getChildText("nbt"), true, false, true);
		this.findProperties(xml);
		return this;
	}

	/** Getter for {@link PlacedBlock#block}. */
	public Block getBlock()
	{
		return block;
	}

	/** Getter for {@link PlacedBlock#data}. */
	public int getData()
	{
		return data;
	}

	@Override
	public Component getDisplayComponent()
	{
		return new PanelBlockDisplay(this);
	}

	@Override
	public String getName(int index)
	{
		return this.customName() != null && !this.customName().equals("") ? this.customName() : this.block.name(this.data).toString();
	}

	/** Getter for {@link PlacedBlock#nbt}. */
	public TagCompound getNbt()
	{
		return nbt;
	}

	/** Setter for {@link PlacedBlock#block}. */
	public void setBlock(Block block)
	{
		this.block = block;
		this.onChange();
	}

	/** Setter for {@link PlacedBlock#data}. */
	public void setData(int data)
	{
		this.data = data;
		this.onChange();
	}

	/** Setter for {@link PlacedBlock#nbt}. */
	public void setNbt(TagCompound nbt)
	{
		this.nbt = nbt;
		this.onChange();
	}

	/** @return This Placed Block's texture. */
	public BufferedImage texture()
	{
		return this.block.texture(this.data);
	}

	@Override
	public String toCommand()
	{
		return this.block.id() + " " + this.data + " " + this.nbt.value();
	}

	@Override
	public TagCompound toNBT(TemplateCompound container)
	{
		ArrayList<Tag> tags = new ArrayList<Tag>();

		tags.add(Tags.BLOCK_ID.create(this.block.id()));
		tags.add(Tags.BLOCK_DATA.create(this.data));
		tags.add(this.nbt);

		return container.create(tags.toArray(new Tag[tags.size()]));
	}

	@Override
	public String toString()
	{
		return this.block.name(this.data).toString();
	}

	@Override
	public Element toXML()
	{
		Element root = this.createRoot("item");
		root.addContent(new Element("id").setText(this.block.id()));
		root.addContent(new Element("data").setText(Integer.toString(this.data)));
		root.addContent(new Element("nbt").setText(this.nbt.valueForCommand()));
		return root;
	}

	@Override
	public PlacedBlock update(CGPanel panel) throws CommandGenerationException
	{
		PlacedBlock b = ((PanelBlock) panel).generate();
		this.block = b.block;
		this.data = b.data;
		this.nbt = b.nbt;
		return this;
	}

}
