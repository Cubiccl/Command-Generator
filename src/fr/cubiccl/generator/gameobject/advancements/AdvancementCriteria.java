package fr.cubiccl.generator.gameobject.advancements;

import java.awt.Component;
import java.util.ArrayList;

import org.jdom2.Element;

import fr.cubiccl.generator.gameobject.tags.NBTReader;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound.DefaultCompound;
import fr.cubiccl.generator.gui.component.interfaces.IObjectList;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.advancement.PanelAdvancementCriteria;
import fr.cubiccl.generator.gui.component.panel.utils.ListProperties;
import fr.cubiccl.generator.utils.CommandGenerationException;

public class AdvancementCriteria implements IObjectList<AdvancementCriteria>
{

	public static AdvancementCriteria createFrom(Element criteria)
	{
		AdvancementCriteria c = new AdvancementCriteria();
		c.name = criteria.getAttributeValue("name");
		c.trigger = CriteriaTrigger.find(criteria.getAttributeValue("trigger"));

		ArrayList<Tag> conditions = new ArrayList<Tag>();
		for (Element condition : criteria.getChildren("condition"))
			conditions.add(NBTReader.read(condition.getText(), false, true, true));

		for (Tag t : conditions)
		{
			if (c.trigger.conditions.contains(t.template)) c.conditions.add(t);
			else for (Tag tag : c.trigger.findContainedTags(t))
				c.conditions.add(tag);
		}

		return c;
	}

	public static AdvancementCriteria createFrom(TagCompound tag)
	{
		AdvancementCriteria c = new AdvancementCriteria();
		c.name = tag.id();
		if (tag.hasTag(Tags.ADVANCEMENT_TRIGGER)) c.trigger = CriteriaTrigger.find(tag.getTag(Tags.ADVANCEMENT_TRIGGER).value());
		if (tag.hasTag(Tags.ADVANCEMENT_CONDITIONS)) for (Tag t : tag.getTag(Tags.ADVANCEMENT_CONDITIONS).value())
			c.conditions.add(t);
		return c;
	}

	private ArrayList<Tag> conditions;
	public String name;
	public CriteriaTrigger trigger;

	public AdvancementCriteria()
	{
		this.trigger = CriteriaTrigger.bred_animals;
		this.conditions = new ArrayList<Tag>();
	}

	public void addCondition(Tag condition)
	{
		this.conditions.add(condition);
	}

	public void clearConditions()
	{
		this.conditions.clear();
	}

	public ArrayList<Tag> conditionsInContainers()
	{

		ArrayList<Tag> tags = new ArrayList<Tag>();

		for (Tag t : this.conditions)
		{
			TemplateCompound container = t.template.container;
			if (container == null) tags.add(t);
			else
			{
				TagCompound c = null;
				for (Tag tag : tags)
					if (tag.template.equals(container))
					{
						c = (TagCompound) tag;
						break;
					}
				if (c == null)
				{
					c = container.create();
					c.setJson(true);
					tags.add(c);
				}

				c.addTag(t);
			}
		}

		return tags;
	}

	@Override
	public CGPanel createPanel(ListProperties properties)
	{
		return new PanelAdvancementCriteria(this);
	}

	public Tag[] getConditions()
	{
		return this.conditions.toArray(new Tag[this.conditions.size()]);
	}

	@Override
	public Component getDisplayComponent()
	{
		return null;
	}

	@Override
	public String getName(int index)
	{
		return this.name;
	}

	public void removeCondition(Tag condition)
	{
		this.conditions.remove(condition);
	}

	public TagCompound toTag()
	{
		ArrayList<Tag> tags = this.conditionsInContainers();
		return new DefaultCompound(this.name, Tag.UNKNOWN).create(Tags.ADVANCEMENT_TRIGGER.create(this.trigger.id),
				Tags.ADVANCEMENT_CONDITIONS.create(tags.toArray(new Tag[tags.size()])));
	}

	public Element toXML()
	{
		Element root = new Element("criteria");
		root.setAttribute("name", this.name);
		root.setAttribute("trigger", this.trigger.id);
		for (Tag tag : this.conditionsInContainers())
			root.addContent(new Element("condition").setText(tag.toCommand(-1)));
		return root;
	}

	@Override
	public AdvancementCriteria update(CGPanel panel) throws CommandGenerationException
	{
		((PanelAdvancementCriteria) panel).update(this);
		return this;
	}
}
