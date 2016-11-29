package fr.cubiccl.generator.gameobject.templatetags;

import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagNumber;
import fr.cubiccl.generator.gui.component.panel.CGPanel;

public class TemplateNumber extends TemplateTag
{
	public final int numberType;

	/** @param numberType see {@link TagNumber#INTEGER} */
	public TemplateNumber(String id, int tagType, int numberType, String... applicable)
	{
		super(id, tagType, applicable);
		this.numberType = numberType;
	}

	/** defauls to integer */
	public TemplateNumber(String id, int tagType, String... applicable)
	{
		this(id, tagType, TagNumber.INTEGER, applicable);
	}

	@Override
	protected CGPanel createPanel()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Tag generateTag(CGPanel panel)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean shouldStateClose(CGPanel panel)
	{
		// TODO Auto-generated method stub
		return false;
	}

}
