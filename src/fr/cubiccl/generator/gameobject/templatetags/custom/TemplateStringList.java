package fr.cubiccl.generator.gameobject.templatetags.custom;

import fr.cubiccl.generator.gameobject.baseobjects.BaseObject;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagList;
import fr.cubiccl.generator.gameobject.tags.TagString;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateList;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.utils.PanelObjectList;
import fr.cubiccl.generator.utils.Text;

public class TemplateStringList extends TemplateList
{

	@Override
	protected CGPanel createPanel(BaseObject<?> object, Tag previousValue)
	{
		Text[] values = new Text[0];
		if (previousValue != null)
		{
			TagList t = (TagList) previousValue;
			values = new Text[t.size()];
			for (int i = 0; i < values.length; i++)
				values[i] = new Text((String) t.getTag(i).value(), false);
		}
		PanelObjectList<Text> p = new PanelObjectList<Text>(null, "tag." + this.id(), Text.class, "description", this.description(object));
		p.setValues(values);
		p.setName(this.title());
		return p;
	}

	@Override
	public TagList generateTag(BaseObject<?> object, CGPanel panel)
	{
		@SuppressWarnings("unchecked")
		Text[] s = ((PanelObjectList<Text>) panel).values();
		TagString[] tags = new TagString[s.length];
		for (int i = 0; i < tags.length; i++)
			tags[i] = Tags.DEFAULT_STRING.create(s[i].toString());
		return this.create(tags);
	}
}
