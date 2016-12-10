package fr.cubiccl.generator.gameobject.templatetags;

import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gui.component.panel.utils.ConfirmPanel;

public abstract class TemplateCompound extends TemplateTag
{
	public static class DefaultCompound extends TemplateCompound
	{

		public DefaultCompound(String id, int tagType, String... applicable)
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

	public TemplateCompound(String id, int tagType, String... applicable)
	{
		super(id, tagType, applicable);
	}

}
