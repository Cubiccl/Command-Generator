package fr.cubiccl.generator.gameobject.templatetags;

import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagNumber;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.utils.EntryPanel;

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
		EntryPanel p = new EntryPanel(this.description());
		p.entry.addIntFilter();
		p.entry.setText("0");
		return p;
	}

	@Override
	public Tag generateTag(CGPanel panel)
	{
		return new TagNumber(this, ((EntryPanel) panel).entry.getText());
	}

	@Override
	protected boolean isInputValid(CGPanel panel)
	{
		try
		{
			Integer.parseInt(((EntryPanel) panel).entry.getText());
		} catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
