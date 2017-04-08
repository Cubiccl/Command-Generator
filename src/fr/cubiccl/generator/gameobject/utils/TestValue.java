package fr.cubiccl.generator.gameobject.utils;

import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;
import fr.cubiccl.generator.gameobject.templatetags.TemplateNumber;

public class TestValue
{

	public final TemplateNumber exactTag;
	public boolean isRanged = false;
	public TemplateNumber minTag, maxTag;
	public final TemplateCompound rangeTag;
	public double valueMin, valueMax;

	public TestValue(TemplateNumber exactTag, TemplateCompound rangeTag)
	{
		super();
		this.exactTag = exactTag;
		this.rangeTag = rangeTag;
		this.minTag = Tags.VALUE_MIN;
		this.maxTag = Tags.VALUE_MAX;
		this.valueMin = 0;
		this.valueMax = 0;
	}

	public void findValue(TagCompound t)
	{
		if (t.hasTag(this.exactTag))
		{
			this.isRanged = false;
			this.valueMin = (double) t.getTag(this.exactTag).value();
		} else if (t.hasTag(this.rangeTag))
		{
			this.isRanged = true;
			TagCompound range = (TagCompound) t.getTag(this.rangeTag);
			this.valueMin = (double) range.getTag(this.minTag).value();
			this.valueMax = (double) range.getTag(this.maxTag).value();
		}
	}

	public Tag toTag()
	{
		if (!this.isRanged) return this.exactTag.create(this.valueMin);
		return this.rangeTag.create(this.minTag.create(this.valueMin), this.maxTag.create(this.valueMax));
	}
}
