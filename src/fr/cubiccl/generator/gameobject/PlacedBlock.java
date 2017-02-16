package fr.cubiccl.generator.gameobject;

import java.awt.Component;
import java.util.ArrayList;

import fr.cubiccl.generator.gameobject.baseobjects.Block;
import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.tags.TagNumber;
import fr.cubiccl.generator.gameobject.tags.TagString;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;
import fr.cubiccl.generator.gui.component.interfaces.IObjectList;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.label.ImageLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelBlock;
import fr.cubiccl.generator.gui.component.panel.utils.ListProperties;
import fr.cubiccl.generator.utils.CommandGenerationException;

public class PlacedBlock extends GameObject implements IObjectList<PlacedBlock>
{

	public static PlacedBlock createFrom(TagCompound tag)
	{
		Block b = ObjectRegistry.blocks.first();
		int d = 0;
		TagCompound nbt = new TagCompound(Tags.BLOCK_NBT);

		for (Tag t : tag.value())
		{
			if (t.id().equals(Tags.BLOCK_ID.id())) ObjectRegistry.blocks.find(((TagString) t).value);
			if (t.id().equals(Tags.BLOCK_DATA.id())) d = ((TagNumber) t).value;
			if (t.id().equals(Tags.BLOCK_NBT.id())) nbt = (TagCompound) t;
		}

		PlacedBlock bl = new PlacedBlock(b, d, nbt);
		bl.findName(tag);
		return bl;
	}

	public final Block block;
	public final int data;
	public final TagCompound nbt;

	public PlacedBlock()
	{
		this(ObjectRegistry.blocks.find("stone"), 0, new TagCompound(Tags.DEFAULT_COMPOUND));
	}

	public PlacedBlock(Block block, int data, TagCompound nbt)
	{
		this.block = block;
		this.data = data;
		this.nbt = nbt;
	}

	@Override
	public CGPanel createPanel(ListProperties properties)
	{
		PanelBlock p = new PanelBlock(null, true, true, properties.hasCustomObjects());
		p.setupFrom(this);
		return p;
	}

	@Override
	public Component getDisplayComponent()
	{
		CGPanel p = new CGPanel();
		p.add(new CGLabel(this.block.name(this.data)));
		p.add(new ImageLabel(this.block.texture(this.data)));
		return p;
	}

	@Override
	public String getName(int index)
	{
		return this.customName() != null && !this.customName().equals("") ? this.customName() : this.block.name(this.data).toString();
	}

	@Override
	public PlacedBlock setupFrom(CGPanel panel) throws CommandGenerationException
	{
		return ((PanelBlock) panel).generate();
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
	public TagCompound toTag(TemplateCompound container, boolean includeName)
	{
		ArrayList<Tag> tags = new ArrayList<Tag>();

		tags.add(new TagString(Tags.BLOCK_ID, this.block.id()));
		tags.add(new TagNumber(Tags.BLOCK_DATA, this.data));
		tags.add(this.nbt);
		if (includeName) tags.add(this.nameTag());

		return new TagCompound(container, tags.toArray(new Tag[tags.size()]));
	}

}
