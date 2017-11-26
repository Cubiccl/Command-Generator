package fr.cubiccl.generator.gameobject.tags;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jnbt.*;

/** Changes Command Generator's NBT Tags to JNBT's Tags and vice versa. */
public class TagTransformer
{

	/** @param tag - An NBT Tag.
	 * @return The input Tag in Json format. */
	private static String asJson(org.jnbt.Tag tag)
	{
		return tag.getName() + ":" + valueAsJson(tag);
	}

	public static TagNumber toCG(ByteTag tag)
	{
		return (TagNumber) toCG(tag);
	}

	public static TagCompound toCG(CompoundTag tag)
	{
		return (TagCompound) toCG(tag);
	}

	public static TagNumber toCG(DoubleTag tag)
	{
		return (TagNumber) toCG(tag);
	}

	public static TagNumber toCG(FloatTag tag)
	{
		return (TagNumber) toCG(tag);
	}

	public static TagNumber toCG(IntTag tag)
	{
		return (TagNumber) toCG(tag);
	}

	public static TagList toCG(ListTag tag)
	{
		return (TagList) toCG(tag);
	}

	public static TagNumber toCG(LongTag tag)
	{
		return (TagNumber) toCG(tag);
	}

	/** @param tag - A JNBT Tag.
	 * @return The Command Generator Tag version. */
	public static Tag toCG(org.jnbt.Tag tag)
	{
		return NBTParser.parse(asJson(tag), false, true, true);
	}

	public static TagNumber toCG(ShortTag tag)
	{
		return (TagNumber) toCG(tag);
	}

	/** @param tag - A Command Generator Tag.
	 * @return The JNBT Tag version. */
	public static org.jnbt.Tag toJNBT(Tag tag)
	{
		int type = tag.type();
		switch (type)
		{
			case Tag.STRING:
				return toJNBT((TagString) tag);

			case Tag.BYTE:
			case Tag.SHORT:
			case Tag.INT:
			case Tag.LONG:
			case Tag.FLOAT:
			case Tag.DOUBLE:
				return toJNBT((TagNumber) tag);

			case Tag.LIST:
				return toJNBT((TagList) tag);

			case Tag.COMPOUND:
				return toJNBT((TagCompound) tag);

			default:
				return null;
		}
	}

	public static CompoundTag toJNBT(TagCompound tag)
	{
		HashMap<String, org.jnbt.Tag> tags = new HashMap<String, org.jnbt.Tag>();
		for (Tag t : tag.value())
			tags.put(t.id(), toJNBT(t));
		return new CompoundTag(tag.id(), tags);
	}

	public static ListTag toJNBT(TagList tag)
	{
		ArrayList<org.jnbt.Tag> tags = new ArrayList<org.jnbt.Tag>();
		for (Tag t : tag.value())
			tags.add(toJNBT(t));
		return new ListTag(tag.id(), tags.size() == 0 ? StringTag.class : tags.get(0).getClass(), tags);
	}

	public static org.jnbt.Tag toJNBT(TagNumber tag)
	{
		int type = tag.type();
		switch (type)
		{
			case Tag.BYTE:
				return new ByteTag(tag.id(), (byte) tag.valueInt());

			case Tag.SHORT:
				return new ShortTag(tag.id(), (short) tag.valueInt());

			case Tag.INT:
				return new IntTag(tag.id(), tag.valueInt());

			case Tag.LONG:
				return new LongTag(tag.id(), (long) (double) tag.value());

			case Tag.FLOAT:
				return new FloatTag(tag.id(), (float) (double) tag.value());

			case Tag.DOUBLE:
				return new DoubleTag(tag.id(), tag.value());

			default:
				return null;
		}
	}

	public static StringTag toJNBT(TagString tag)
	{
		return new StringTag(tag.id(), tag.value());
	}

	@SuppressWarnings("unchecked")
	private static String valueAsJson(org.jnbt.Tag tag)
	{
		int type = NBTUtils.getTypeCode(tag.getClass());
		switch (type)
		{
			case NBTConstants.TYPE_BYTE:
				return tag.getValue() + TagNumber.SUFFIX[Tag.BYTE];

			case NBTConstants.TYPE_SHORT:
				return tag.getValue() + TagNumber.SUFFIX[Tag.SHORT];

			case NBTConstants.TYPE_INT:
				return tag.getValue() + TagNumber.SUFFIX[Tag.INT];

			case NBTConstants.TYPE_LONG:
				return tag.getValue() + TagNumber.SUFFIX[Tag.LONG];

			case NBTConstants.TYPE_FLOAT:
				return tag.getValue() + TagNumber.SUFFIX[Tag.FLOAT];

			case NBTConstants.TYPE_DOUBLE:
				return tag.getValue() + TagNumber.SUFFIX[Tag.DOUBLE];

			case NBTConstants.TYPE_STRING:
				return "\"" + tag.getValue() + "\"";

			case NBTConstants.TYPE_LIST:
				String v = "[";
				List<org.jnbt.Tag> list = (List<org.jnbt.Tag>) tag.getValue();
				for (int i = 0; i < list.size(); ++i)
				{
					if (i != 0) v += ",";
					v += valueAsJson(list.get(i));
				}
				return v + "]";

			case NBTConstants.TYPE_COMPOUND:
				v = "{";
				HashMap<String, org.jnbt.Tag> tags = (HashMap<String, org.jnbt.Tag>) tag.getValue();
				boolean started = false;
				for (org.jnbt.Tag t : tags.values())
				{
					if (!started) started = true;
					else v += ",";
					v += asJson(t);
				}
				return v + "}";

			default:
				return "";
		}
	}

}
