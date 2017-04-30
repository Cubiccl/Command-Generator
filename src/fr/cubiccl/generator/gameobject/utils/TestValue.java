package fr.cubiccl.generator.gameobject.utils;

import java.util.Random;

import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;
import fr.cubiccl.generator.gameobject.templatetags.TemplateNumber;
import fr.cubiccl.generator.gameobject.templatetags.TemplateRange;

/** Can either be a TagNumber or a TagCompound containing two TagNumbers (min and max). */
public class TestValue
{

	private static TemplateNumber maxFor(TemplateNumber tag)
	{
		return TemplateRange.max[tag.tagType];
	}

	private static TemplateNumber minFor(TemplateNumber tag)
	{
		return TemplateRange.min[tag.tagType];
	}

	public final TemplateNumber exactTag;
	public boolean isRanged = false;
	public TemplateNumber minTag, maxTag;
	public final TemplateCompound rangeTag;
	public double valueMin, valueMax;

	public TestValue(TemplateNumber exactTag, TemplateCompound rangeTag)
	{
		this(exactTag, rangeTag, minFor(exactTag), maxFor(exactTag));
	}

	public TestValue(TemplateNumber exactTag, TemplateCompound rangeTag, TemplateNumber minTag, TemplateNumber maxTag)
	{
		this(exactTag, rangeTag, minTag, maxTag, 0);
	}

	public TestValue(TemplateNumber exactTag, TemplateCompound rangeTag, TemplateNumber minTag, TemplateNumber maxTag, double min)
	{
		this.exactTag = exactTag;
		this.rangeTag = rangeTag;
		this.minTag = minTag;
		this.maxTag = maxTag;
		this.valueMin = min;
		this.valueMax = 0;
	}

	public boolean findValue(Tag[] tags)
	{
		return this.findValue(Tags.DEFAULT_COMPOUND.create(tags));
	}

	public boolean findValue(TagCompound t)
	{
		if (t.hasTag(this.exactTag))
		{
			this.isRanged = false;
			this.valueMin = t.getTag(this.exactTag).value();
			return true;
		} else if (t.hasTag(this.rangeTag))
		{
			this.isRanged = true;
			TagCompound range = t.getTag(this.rangeTag);
			this.valueMin = range.getTag(this.minTag).value();
			this.valueMax = range.getTag(this.maxTag).value();
			return true;
		}
		return false;
	}

	public double generateValue()
	{
		if (this.isRanged) return new Random().nextDouble() * (this.valueMax - this.valueMin + 1) + this.valueMax;
		return this.valueMin;
	}

	public boolean isInt()
	{
		return this.exactTag.tagType != Tag.DOUBLE && this.exactTag.tagType != Tag.FLOAT;
	}

	public Tag toTag()
	{
		if (!this.isRanged) return this.exactTag.create(this.valueMin);
		return this.rangeTag.create(this.minTag.create(this.valueMin), this.maxTag.create(this.valueMax));
	}
}
