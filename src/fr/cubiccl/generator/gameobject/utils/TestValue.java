package fr.cubiccl.generator.gameobject.utils;

import java.util.Random;

import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.templatetags.*;

/** Can either be a TagNumber, or a TagCompound containing two TagNumbers (min and max). */
public class TestValue
{

	/** @return The maximum Template Number Tag for the input Number tag (finds the Template with same {@link TemplateTag#tagType tag type}.). */
	private static TemplateNumber maxFor(TemplateNumber tag)
	{
		return TemplateRange.max[tag.tagType()];
	}

	/** @return The minimum Template Number Tag for the input Number tag (finds the Template with same {@link TemplateTag#tagType tag type}.). */
	private static TemplateNumber minFor(TemplateNumber tag)
	{
		return TemplateRange.min[tag.tagType()];
	}

	/** The Template Number Tag to use when the value is fixed. */
	public final TemplateNumber exactTag;
	/** <code>true</code> if the value is ranged between {@link TestValue#valueMin} and {@link TestValue#valueMax}.<br />
	 * <code>false</code> if the value is equal to {@link TestValue#valueMin}. */
	public boolean isRanged = false;
	/** The Template Tag for the maximum value if ranged. */
	public TemplateNumber maxTag;
	/** The Template Tag for the minimum value if ranged. */
	public TemplateNumber minTag;
	/** The Compound Tag if ranged. */
	public final TemplateCompound rangeTag;
	/** If {@link TestValue#isRanged} is <code>true</code>, maximum possible value. Else, unused. */
	public double valueMax;
	/** If {@link TestValue#isRanged} is <code>true</code>, minimum possible value. Else, exact value. */
	public double valueMin;

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

	/** Looks for the value of this test in the input list of Tags.
	 * 
	 * @param tag - The tags to look for the value into.
	 * @return <code>true</code> if the value was found. */
	public boolean findValue(Tag[] tags)
	{
		return this.findValue(Tags.DEFAULT_COMPOUND.create(tags));
	}

	/** Looks for the value of this test in the input Compound.
	 * 
	 * @param tag - The tag to look for the value into.
	 * @return <code>true</code> if the value was found. */
	public boolean findValue(TagCompound tag)
	{
		if (tag.hasTag(this.exactTag))
		{
			this.isRanged = false;
			this.valueMin = tag.getTag(this.exactTag).value();
			return true;
		} else if (tag.hasTag(this.rangeTag))
		{
			this.isRanged = true;
			TagCompound range = tag.getTag(this.rangeTag);
			this.valueMin = range.getTag(this.minTag).value();
			this.valueMax = range.getTag(this.maxTag).value();
			return true;
		}
		return false;
	}

	/** @return A possible value from this test. */
	public double generateValue()
	{
		if (this.isRanged) return new Random().nextDouble() * (this.valueMax - this.valueMin + 1) + this.valueMax;
		return this.valueMin;
	}

	/** @return <code>true</code> if this value is in integer format. */
	public boolean isInt()
	{
		return this.exactTag.tagType() != Tag.DOUBLE && this.exactTag.tagType() != Tag.FLOAT;
	}

	/** @return This value as NBT Tags. */
	public Tag toTag()
	{
		if (!this.isRanged) return this.exactTag.create(this.valueMin);
		return this.rangeTag.create(this.minTag.create(this.valueMin), this.maxTag.create(this.valueMax));
	}
}
