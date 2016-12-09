package fr.cubiccl.generator.gameobject.templatetags;

import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gui.component.panel.CGPanel;

public abstract class TemplateCompound extends TemplateTag
{
	public static class DefaultCompound extends TemplateCompound
	{

		public DefaultCompound(String id, int tagType, String... applicable)
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

	public TemplateCompound(String id, int tagType, String... applicable)
	{
		super(id, tagType, applicable);
	}

}
