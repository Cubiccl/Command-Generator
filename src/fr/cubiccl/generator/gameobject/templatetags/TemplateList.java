package fr.cubiccl.generator.gameobject.templatetags;

import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gui.component.panel.CGPanel;

public abstract class TemplateList extends TemplateTag
{
	public static class DefaultList extends TemplateList
	{

		public DefaultList(String id, int tagType, String... applicable)
		{
			super(id, tagType, applicable);
		}

		@Override
		protected CGPanel createPanel(String objectId)
		{
			return null;
		}

		@Override
		public Tag generateTag(CGPanel panel)
		{
			return null;
		}

	}

	public TemplateList(String id, int tagType, String... applicable)
	{
		super(id, tagType, applicable);
	}

}
