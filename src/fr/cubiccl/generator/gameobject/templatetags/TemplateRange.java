package fr.cubiccl.generator.gameobject.templatetags;

import java.util.ArrayList;

import fr.cubiccl.generator.CommandGenerator;
import fr.cubiccl.generator.gameobject.baseobjects.BaseObject;
import fr.cubiccl.generator.gameobject.tags.NBTParser;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.tags.TagNumber;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.tag.PanelRangedValue;
import fr.cubiccl.generator.gui.component.textfield.CGEntry;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;

/** This NBT Tag can be either a {@link TemplateNumber Number NBT Tag} or a {@link TemplateCompound Compound NBT Tag} containing a minimum Tag and a maximum Tag. */
public class TemplateRange extends TemplateNumber
{
	/** NBT Tags to use for maximum values if ranged, depending on the {@link TemplateRange#numberType number type}. */
	public static final TemplateNumber[] max =
	{ null, null, null, TagsMain.VALUE_MAX, null, TagsMain.VALUE_MAX_FLOAT, TagsMain.VALUE_MAX_DOUBLE };
	/** NBT Tags to use for minimum values if ranged, depending on the {@link TemplateRange#numberType number type}. */
	public static final TemplateNumber[] min =
	{ null, null, null, TagsMain.VALUE_MIN, null, TagsMain.VALUE_MIN_FLOAT, TagsMain.VALUE_MIN_DOUBLE };

	/** The number type. */
	public final byte numberType;
	/** The NBT Tag to use as maximum if ranged. */
	private TemplateNumber tagMax;
	/** The NBT Tag to use as minimum if ranged. */
	private TemplateNumber tagMin;

	public TemplateRange(String id, byte applicationType, byte numberType, String... applicable)
	{
		super(id, applicationType, Tag.RANGE, applicable);
		this.numberType = numberType;
		this.setRangeTags();
	}

	/** Creates this NBT Tag as a fixed value with the input value.
	 * 
	 * @param value - The value to set.
	 * @return The created NBT Tag. */
	@Override
	@SuppressWarnings("deprecation")
	public TagNumber create(double value)
	{
		return new TagNumber(this, value);
	}

	/** Creates this NBT Tag as ranged values with the input values.
	 * 
	 * @param min - The minimum value.
	 * @param max - The maximum value.
	 * @return The created NBT Tag. */
	public TagCompound create(double min, double max)
	{
		return this.create(this.tagMin.create(min), this.tagMax.create(max));
	}

	/** Creates this NBT Tag as a fixed value with the input value.
	 * 
	 * @param value - The value to set.
	 * @return The created NBT Tag. */
	@Override
	@SuppressWarnings("deprecation")
	public TagNumber create(int value)
	{
		return new TagNumber(this, value);
	}

	/** Creates this NBT Tag as ranged values with the input values.
	 * 
	 * @param min - The minimum value.
	 * @param max - The maximum value.
	 * @return The created NBT Tag. */
	public TagCompound create(int min, int max)
	{
		return this.create(this.tagMin.create(min), this.tagMax.create(max));
	}

	/** Creates this NBT Tag with the input values.
	 * 
	 * @param value - The list of NBT Tags contained in this Compound.
	 * @return The created NBT Tag. */
	@SuppressWarnings("deprecation")
	private TagCompound create(Tag... value)
	{
		return new TagCompound(this, value);
	}

	@Override
	protected CGPanel createPanel(BaseObject<?> object, Tag previousValue)
	{
		PanelRangedValue p = new PanelRangedValue(this.description(object), this.numberType == Tag.INT ? Text.INTEGER : Text.NUMBER);
		if (previousValue != null)
		{
			if (previousValue instanceof TagNumber)
			{
				if (this.isInt()) p.setFixed(((TagNumber) previousValue).valueInt());
				else p.setFixed(((TagNumber) previousValue).value());
			} else
			{
				TagCompound t = (TagCompound) previousValue;
				if (this.isInt()) p.setRanged(t.getTag(this.tagMin).valueInt(), t.getTag(this.tagMax).valueInt());
				else p.setRanged(t.getTag(this.tagMin).value(), t.getTag(this.tagMax).value());
			}
		}
		return p;
	}

	@Override
	protected Tag generateTag(BaseObject<?> object, CGPanel panel)
	{
		PanelRangedValue p = (PanelRangedValue) panel;
		if (p.isRanged()) return this.create(p.min(), p.max());
		return this.create(p.value());
	}

	@Override
	protected boolean isInputValid(BaseObject<?> object, CGPanel panel)
	{
		PanelRangedValue p = (PanelRangedValue) panel;
		try
		{
			p.checkInput(this.isInt() ? CGEntry.INTEGER : CGEntry.NUMBER);
			return true;
		} catch (CommandGenerationException e)
		{
			CommandGenerator.report(e);
			return false;
		}
	}

	/** @return <code>true</code> if this NBT Tag has integer values. */
	private boolean isInt()
	{
		return this.numberType == Tag.INT;
	}

	@Override
	public Tag parseTag(String value, boolean isJson, boolean readUnknown)
	{
		if (!value.startsWith("{") && !value.endsWith("}")) return super.parseTag(value, isJson, readUnknown);
		if (value.startsWith("{") && value.endsWith("}")) value = value.substring(1, value.length() - 1);
		String[] values = NBTParser.splitTagValues(value);
		ArrayList<Tag> tags = new ArrayList<Tag>();
		for (String v : values)
		{
			if (v.equals("")) continue;
			Tag t = NBTParser.parse(v, false, isJson, readUnknown);
			if (t != null) tags.add(t);
		}
		return this.create(tags.toArray(new Tag[tags.size()]));
	}

	/** Selects the correct {@link TemplateRange#tagMin minimum NBT Tag} and {@link TemplateRange#tagMax maximum NBT Tag}. */
	private void setRangeTags()
	{
		this.tagMin = min[this.numberType];
		this.tagMax = max[this.numberType];
	}

	@Override
	public String suffix()
	{
		return TagNumber.SUFFIX[this.numberType];
	}

}
