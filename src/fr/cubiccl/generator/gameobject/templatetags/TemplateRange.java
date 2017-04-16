package fr.cubiccl.generator.gameobject.templatetags;

import java.util.ArrayList;

import fr.cubiccl.generator.CommandGenerator;
import fr.cubiccl.generator.gameobject.baseobjects.BaseObject;
import fr.cubiccl.generator.gameobject.tags.NBTReader;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.tags.TagNumber;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.tag.PanelRangedTag;
import fr.cubiccl.generator.gui.component.textfield.CGEntry;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;

public class TemplateRange extends TemplateNumber
{

	public final byte numberType;
	private TemplateNumber tagMin, tagMax;

	public TemplateRange(String id, byte applicationType, byte numberType, String... applicable)
	{
		super(id, applicationType, Tag.RANGE, applicable);
		this.numberType = numberType;
	}

	@SuppressWarnings("deprecation")
	public TagNumber create(double value)
	{
		return new TagNumber(this, value);
	}

	public TagCompound create(double min, double max)
	{
		return this.create(this.tagMin.create(min), this.tagMax.create(max));
	}

	@SuppressWarnings("deprecation")
	public TagNumber create(int value)
	{
		return new TagNumber(this, value);
	}

	public TagCompound create(int min, int max)
	{
		return this.create(this.tagMin.create(min), this.tagMax.create(max));
	}

	@SuppressWarnings("deprecation")
	public TagCompound create(Tag... value)
	{
		return new TagCompound(this, value);
	}

	@Override
	protected CGPanel createPanel(BaseObject object, Tag previousValue)
	{
		PanelRangedTag p = new PanelRangedTag(this.description(object), this.numberType == Tag.INT ? Text.INTEGER : Text.NUMBER);
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
	protected Tag generateTag(BaseObject object, CGPanel panel)
	{
		PanelRangedTag p = (PanelRangedTag) panel;
		if (p.isRanged()) return this.create(p.min(), p.max());
		return this.create(p.value());
	}

	@Override
	protected boolean isInputValid(BaseObject object, CGPanel panel)
	{
		PanelRangedTag p = (PanelRangedTag) panel;
		try
		{
			p.checkInput(this.isInt() ? CGEntry.INTEGER : CGEntry.NUMBER);
			return true;
		} catch (CommandGenerationException e)
		{
			e.printStackTrace();
			CommandGenerator.report(e);
			return false;
		}
	}

	private boolean isInt()
	{
		return this.numberType == Tag.INT;
	}

	@Override
	public Tag readTag(String value, boolean isJson, boolean readUnknown)
	{
		if (!value.startsWith("{") && !value.endsWith("}")) return super.readTag(value, isJson, readUnknown);
		if (value.startsWith("{") && value.endsWith("}")) value = value.substring(1, value.length() - 1);
		String[] values = NBTReader.splitTagValues(value);
		ArrayList<Tag> tags = new ArrayList<Tag>();
		for (String v : values)
		{
			if (v.equals("")) continue;
			Tag t = NBTReader.read(v, false, isJson, readUnknown);
			if (t != null) tags.add(t);
		}
		return this.create(tags.toArray(new Tag[tags.size()]));
	}

	public void setRangeTags()
	{
		TemplateNumber[] min =
		{ null, null, null, Tags.VALUE_MIN, null, Tags.VALUE_MIN_FLOAT, Tags.VALUE_MIN_DOUBLE };
		TemplateNumber[] max =
		{ null, null, null, Tags.VALUE_MAX, null, Tags.VALUE_MAX_FLOAT, Tags.VALUE_MAX_DOUBLE };
		this.tagMin = min[this.numberType];
		this.tagMax = max[this.numberType];
	}

	public String suffix()
	{
		return TagNumber.SUFFIX[this.numberType];
	}

}
