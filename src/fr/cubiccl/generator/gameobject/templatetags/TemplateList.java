package fr.cubiccl.generator.gameobject.templatetags;

import fr.cubiccl.generator.gameobject.baseobjects.BaseObject;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gui.component.panel.CGPanel;

public abstract class TemplateList extends TemplateTag
{
	public static class DefaultList extends TemplateList
	{

		public DefaultList(String id)
		{
			super(id, Tag.UNAVAILABLE);
		}

		@Override
		protected CGPanel createPanel(BaseObject object, Tag previousValue)
		{
			return null;
		}

		@Override
		public Tag generateTag(BaseObject object, CGPanel panel)
		{
			return null;
		}

	}

	public TemplateList(String id, byte tagType, String... applicable)
	{
		super(id, tagType, applicable);
	}

}
