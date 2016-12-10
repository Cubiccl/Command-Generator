package fr.cubiccl.generator.gameobject.templatetags;

import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gui.component.panel.utils.ConfirmPanel;

public abstract class TemplateList extends TemplateTag
{
	public static class DefaultList extends TemplateList
	{

		public DefaultList(String id, int tagType, String... applicable)
		{
			super(id, tagType, applicable);
		}

		@Override
		protected ConfirmPanel createPanel(String objectId)
		{
			return null;
		}

		@Override
		public Tag generateTag(ConfirmPanel panel)
		{
			return null;
		}

	}

	public TemplateList(String id, int tagType, String... applicable)
	{
		super(id, tagType, applicable);
	}

}
