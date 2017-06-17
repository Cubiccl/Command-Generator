package fr.cubiccl.generator.gameobject.advancements;

import java.awt.Component;
import java.util.ArrayList;

import org.jdom2.Element;

import fr.cubiccl.generator.gameobject.tags.NBTParser;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound.DefaultCompound;
import fr.cubiccl.generator.gameobject.utils.NBTSaveable;
import fr.cubiccl.generator.gameobject.utils.XMLSaveable;
import fr.cubiccl.generator.gui.component.interfaces.IObjectList;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.advancement.PanelAdvancementCriteria;
import fr.cubiccl.generator.gui.component.panel.utils.ListProperties;
import fr.cubiccl.generator.utils.CommandGenerationException;

/** Represents a Criterion for an Advancement. */
public class AdvancementCriterion implements IObjectList<AdvancementCriterion>, NBTSaveable<AdvancementCriterion>, XMLSaveable<AdvancementCriterion>
{

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

	@Override
	public AdvancementCriterion fromNBT(TagCompound nbt)
	{
		this.name = nbt.id();
		if (nbt.hasTag(Tags.ADVANCEMENT_TRIGGER)) this.trigger = CriterionTrigger.find(nbt.getTag(Tags.ADVANCEMENT_TRIGGER).value());
		if (nbt.hasTag(Tags.ADVANCEMENT_CONDITIONS)) for (Tag t : nbt.getTag(Tags.ADVANCEMENT_CONDITIONS).value())
			this.conditions.add(t);
		return this;
	}

	@Override
	public AdvancementCriterion fromXML(Element xml)
	{
		this.name = xml.getAttributeValue("name");
		this.trigger = CriterionTrigger.find(xml.getAttributeValue("trigger"));

		ArrayList<Tag> conditions = new ArrayList<Tag>();
		for (Element condition : xml.getChildren("condition"))
			conditions.add(NBTParser.parse(condition.getText(), false, true, true));

		for (Tag t : conditions)
			if (this.trigger.conditions.contains(t.template)) this.conditions.add(t);
		/* else for (Tag tag : this.trigger.findContainedTags(t)) this.conditions.add(tag); */

		return this;
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

	@Override
	public TagCompound toNBT(TemplateCompound container)
	{
		return new DefaultCompound(this.name, Tag.UNKNOWN).create(Tags.ADVANCEMENT_TRIGGER.create(this.trigger.id),
				Tags.ADVANCEMENT_CONDITIONS.create(this.getConditions()));
	}

	@Override
	public Element toXML()
	{
		Element root = new Element("criteria");
		root.setAttribute("name", this.name);
		root.setAttribute("trigger", this.trigger.id);
		for (Tag tag : this.getConditions())
			root.addContent(new Element("condition").setText(tag.setJson(true).toCommand(-1)));
		return root;
	}

	@Override
	public AdvancementCriterion update(CGPanel panel) throws CommandGenerationException
	{
		((PanelAdvancementCriteria) panel).update(this);
		return this;
	}
}
