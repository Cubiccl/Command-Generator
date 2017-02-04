package fr.cubiccl.generator.gameobject;

import java.util.ArrayList;

import fr.cubiccl.generator.gameobject.baseobjects.Block;
import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.tags.TagNumber;
import fr.cubiccl.generator.gameobject.tags.TagString;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;

public class PlacedBlock extends GameObject
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

	public PlacedBlock(Block block, int data, TagCompound nbt)
	{
		this.block = block;
		this.data = data;
		this.nbt = nbt;
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
