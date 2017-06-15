package fr.cubiccl.generator.gameobject.templatetags.custom;

import java.awt.Component;

import fr.cubiccl.generator.gameobject.JsonMessage;
import fr.cubiccl.generator.gameobject.baseobjects.BaseObject;
import fr.cubiccl.generator.gameobject.tags.*;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateList;
import fr.cubiccl.generator.gui.component.interfaces.IObjectList;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelListJsonMessage;
import fr.cubiccl.generator.gui.component.panel.utils.ListProperties;
import fr.cubiccl.generator.gui.component.panel.utils.PanelObjectList;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;

public class TemplatePages extends TemplateList
{

	public static class Page implements IObjectList<Page>
	{
		private JsonMessage[] messages;

		public Page()
		{
			this(new JsonMessage[0]);
		}

		public Page(JsonMessage... messages)
		{
			this.messages = messages;
		}

		@Override
		public CGPanel createPanel(ListProperties properties)
		{
			PanelListJsonMessage p = new PanelListJsonMessage();
			for (JsonMessage message : this.messages)
				p.addMessage(message);
			return p;
		}

		@Override
		public Component getDisplayComponent()
		{
			return null;
		}

		@Override
		public String getName(int index)
		{
			return new Text("json.page.index").addReplacement("<index>", Integer.toString(index + 1)).toString();
		}

		public TagString toTag()
		{
			TagCompound[] tags = new TagCompound[this.messages.length];
			for (int i = 0; i < tags.length; ++i)
				tags[i] = this.messages[i].toNBT(Tags.DEFAULT_COMPOUND);
			TagList list = Tags.DEFAULT_LIST.create(tags);
			return Tags.DEFAULT_STRING.create(list.valueForCommand());
		}

		@Override
		public Page update(CGPanel panel) throws CommandGenerationException
		{
			this.messages = ((PanelListJsonMessage) panel).getMessages();
			return this;
		}

	}

	private static Page createPage(String value)
	{
		Tag t = NBTParser.parse(value, true, true);
		if (t instanceof TagCompound) return new Page(new JsonMessage().fromNBT((TagCompound) t));
		JsonMessage[] messages = new JsonMessage[((TagList) t).size()];
		for (int i = 0; i < messages.length; ++i)
			messages[i] = new JsonMessage().fromNBT((TagCompound) ((TagList) t).getTag(i));
		return new Page(messages);
	}

	@Override
	protected CGPanel createPanel(BaseObject<?> object, Tag previousValue)
	{
		PanelObjectList<Page> panel = new PanelObjectList<TemplatePages.Page>(null, (Text) null, Page.class);
		if (previousValue != null) for (Tag t : ((TagList) previousValue).value())
		{
			panel.add(createPage(((TagString) t).value()));
		}
		panel.setName(this.title());
		return panel;
	}

	@SuppressWarnings("unchecked")
	@Override
	public TagList generateTag(BaseObject<?> object, CGPanel panel)
	{
		TagList list = this.create();
		for (Page page : ((PanelObjectList<Page>) panel).values())
			list.addTag(page.toTag());
		return list;
	}

}
