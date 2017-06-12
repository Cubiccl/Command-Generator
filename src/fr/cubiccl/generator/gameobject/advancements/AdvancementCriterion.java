package fr.cubiccl.generator.gameobject.advancements;

import java.awt.Component;
import java.util.ArrayList;

import org.jdom2.Element;

import fr.cubiccl.generator.gameobject.tags.NBTParser;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound.DefaultCompound;
import fr.cubiccl.generator.gui.component.interfaces.IObjectList;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.advancement.PanelAdvancementCriteria;
import fr.cubiccl.generator.gui.component.panel.utils.ListProperties;
import fr.cubiccl.generator.utils.CommandGenerationException;

/** Represents a Criterion for an Advancement. */
public class AdvancementCriterion implements IObjectList<AdvancementCriterion>
{

	/** Creates a Criterion from the input XML element.
	 * 
	 * @param criterion - The XML element describing the Criterion.
	 * @return The created Criterion. */
	public static AdvancementCriterion createFrom(Element criterion)
	{
		AdvancementCriterion c = new AdvancementCriterion();
		c.name = criterion.getAttributeValue("name");
		c.trigger = CriterionTrigger.find(criterion.getAttributeValue("trigger"));

		ArrayList<Tag> conditions = new ArrayList<Tag>();
		for (Element condition : criterion.getChildren("condition"))
			conditions.add(NBTParser.parse(condition.getText(), false, true, true));

		for (Tag t : conditions)
		{
			if (c.trigger.conditions.contains(t.template)) c.conditions.add(t);
			else for (Tag tag : c.trigger.findContainedTags(t))
				c.conditions.add(tag);
		}

		return c;
	}

	/** Creates a Criterion from the input NBT Tag.
	 * 
	 * @param criterion - The NBT Tag describing the Criterion.
	 * @return The created Criterion. */
	public static AdvancementCriterion createFrom(TagCompound tag)
	{
		AdvancementCriterion c = new AdvancementCriterion();
		c.name = tag.id();
		if (tag.hasTag(Tags.ADVANCEMENT_TRIGGER)) c.trigger = CriterionTrigger.find(tag.getTag(Tags.ADVANCEMENT_TRIGGER).value());
		if (tag.hasTag(Tags.ADVANCEMENT_CONDITIONS)) for (Tag t : tag.getTag(Tags.ADVANCEMENT_CONDITIONS).value())
			c.conditions.add(t);
		return c;
	}

	/** The conditions for this Criterion. */
	private ArrayList<Tag> conditions;
	/** This Criterion's name. */
	public String name;
	/** This Criterion's trigger. */
	public CriterionTrigger trigger;

	public AdvancementCriterion()
	{
		this.trigger = CriterionTrigger.bred_animals;
		this.conditions = new ArrayList<Tag>();
	}

	/** Adds a condition to this Criterion.
	 * 
	 * @param condition - The condition to add. */
	public void addCondition(Tag condition)
	{
		this.conditions.add(condition);
	}

	/** Removes all conditions from this Criterion. */
	public void clearConditions()
	{
		this.conditions.clear();
	}

	@Override
	public CGPanel createPanel(ListProperties properties)
	{
		return new PanelAdvancementCriteria(this);
	}

	/** Getter for {@link AdvancementCriterion#conditions}. */
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

	/** Removes a condition from this Criterion.
	 * 
	 * @param condition - The condition to remove. */
	public void removeCondition(Tag condition)
	{
		this.conditions.remove(condition);
	}

	/** @return This Criterion as an NBT Tag to be generated. */
	public TagCompound toTag()
	{
		return new DefaultCompound(this.name, Tag.UNKNOWN).create(Tags.ADVANCEMENT_TRIGGER.create(this.trigger.id),
				Tags.ADVANCEMENT_CONDITIONS.create(this.getConditions()));
	}

	/** @return This Criterion as an XML element to be stored. */
	public Element toXML()
	{
		Element root = new Element("criteria");
		root.setAttribute("name", this.name);
		root.setAttribute("trigger", this.trigger.id);
		for (Tag tag : this.getConditions())
			root.addContent(new Element("condition").setText(tag.toCommand(-1)));
		return root;
	}

	@Override
	public AdvancementCriterion update(CGPanel panel) throws CommandGenerationException
	{
		((PanelAdvancementCriteria) panel).update(this);
		return this;
	}
}
