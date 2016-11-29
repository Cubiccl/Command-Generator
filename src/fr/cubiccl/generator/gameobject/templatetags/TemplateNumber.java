package fr.cubiccl.generator.gameobject.templatetags;

import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagNumber;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.utils.EntryPanel;
import fr.cubiccl.generator.utils.Text;

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
		EntryPanel p = new EntryPanel(new Text(this.id + ".desciption"));
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
	public boolean shouldStateClose(CGPanel panel)
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
