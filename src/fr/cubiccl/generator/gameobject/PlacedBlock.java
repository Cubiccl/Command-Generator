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

public class PlacedBlock extends GameObject implements IObjectList<PlacedBlock>
{

	public static PlacedBlock createFrom(Element block)
	{
		PlacedBlock b = new PlacedBlock();
		b.block = ObjectRegistry.blocks.find(block.getChildText("id"));
		b.data = Integer.parseInt(block.getChildText("data"));
		b.nbt = (TagCompound) NBTReader.read(block.getChildText("nbt"), true, false, true);
		b.findProperties(block);
		return b;
	}

	public static PlacedBlock createFrom(TagCompound tag)
	{
		Block b = ObjectRegistry.blocks.first();
		int d = 0;
		TagCompound nbt = Tags.BLOCK_NBT.create();

		for (Tag t : tag.value())
		{
			if (t.id().equals(Tags.BLOCK_ID.id())) b = ObjectRegistry.blocks.find(((TagString) t).value);
			if (t.id().equals(Tags.BLOCK_DATA.id())) d = ((TagNumber) t).valueInt();
			if (t.id().equals(Tags.BLOCK_NBT.id())) nbt = (TagCompound) t;
		}

		PlacedBlock bl = new PlacedBlock(b, d, nbt);
		bl.findName(tag);
		return bl;
	}

	private Block block;
	private int data;
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

	public Block getBlock()
	{
		return block;
	}

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

	public TagCompound getNbt()
	{
		return nbt;
	}

	public void setBlock(Block block)
	{
		this.block = block;
		this.onChange();
	}

	public void setData(int data)
	{
		this.data = data;
		this.onChange();
	}

	public void setNbt(TagCompound nbt)
	{
		this.nbt = nbt;
		this.onChange();
	}

	public BufferedImage texture()
	{
		return this.block.texture(this.data);
	}

	public String toCommand()
	{
		return this.block.id() + " " + this.data + " " + this.nbt.value();
	}

	@Override
	public String toString()
	{
		return this.block.name(this.data).toString();
	}

	@Override
	public TagCompound toTag(TemplateCompound container)
	{
		ArrayList<Tag> tags = new ArrayList<Tag>();

		tags.add(Tags.BLOCK_ID.create(this.block.id()));
		tags.add(Tags.BLOCK_DATA.create(this.data));
		tags.add(this.nbt);

		return container.create(tags.toArray(new Tag[tags.size()]));
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
