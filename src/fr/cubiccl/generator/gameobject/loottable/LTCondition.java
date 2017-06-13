package fr.cubiccl.generator.gameobject.loottable;

import java.awt.Component;
import java.util.ArrayList;

import org.jdom2.Element;

import fr.cubi.cubigui.CTextArea;
import fr.cubiccl.generator.gameobject.tags.*;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;
import fr.cubiccl.generator.gameobject.utils.XMLSaveable;
import fr.cubiccl.generator.gui.component.interfaces.IObjectList;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.loottable.PanelCondition;
import fr.cubiccl.generator.gui.component.panel.utils.ListProperties;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Replacement;
import fr.cubiccl.generator.utils.Text;

/** Represents Conditions for Loot Tables. */
public class LTCondition implements IObjectList<LTCondition>, XMLSaveable<LTCondition>
{

	/** Condition types. */
	public static enum Condition
	{
		ENTITY_PROPERTIES("entity_properties"),
		ENTITY_SCORES("entity_scores"),
		KILLED_BY_PLAYER("killed_by_player"),
		RANDOM_CHANCE("random_chance"),
		RANDOM_CHANCE_WITH_LOOTING("random_chance_with_looting");

		/** @param name - Name of the Condition.
		 * @return The Condition with the input name. */
		public static Condition find(String name)
		{
			for (Condition c : values())
				if (c.name.equals(name)) return c;
			return null;
		}

		/** This Condition's name. */
		public final String name;

		private Condition(String name)
		{
			this.name = name;
		}

		/** @return This Condition's translated name. */
		public Text translate()
		{
			return new Text("lt_condition." + this.name);
		}
	}

	/** Creates a Loot Table Condition from the input NBT Tag.
	 * 
	 * @param condition - The NBT Tag describing the Loot Table Condition.
	 * @return The created Loot Table Condition. */
	public static LTCondition createFrom(TagCompound tag)
	{
		ArrayList<Tag> tags = new ArrayList<Tag>();
		Condition c = null;

		for (Tag t : tag.value())
		{
			if (t.id().equals(Tags.LOOTTABLE_CONDITION.id())) c = Condition.find(((TagString) t).value());
			else tags.add(t);
		}

		if (c == null) return null;
		return new LTCondition(c, tags.toArray(new Tag[tags.size()]));
	}

	/** This Condition's type. */
	public Condition condition;

	/** The NBT Tags describing this Condition. */
	public Tag[] tags;

	public LTCondition()
	{
		this(Condition.values()[0]);
	}

	public LTCondition(Condition condition, Tag... tags)
	{
		this.condition = condition;
		this.tags = tags;
	}

	@SuppressWarnings("unchecked")
	@Override
	public CGPanel createPanel(ListProperties properties)
	{
		if (!properties.contains("conditions")) properties.set("conditions", new ArrayList<Condition>());
		PanelCondition p;
		if ((boolean) properties.get("new")) p = new PanelCondition((ArrayList<Condition>) properties.get("conditions"));
		else
		{
			ArrayList<Condition> conditions = (ArrayList<Condition>) properties.get("conditions");
			conditions.remove(this.condition);
			p = new PanelCondition((ArrayList<Condition>) properties.get("conditions"));
		}
		p.setupFrom(this);
		p.setName(new Text("loottable.condition", new Replacement("<index>", Integer.toString((int) properties.get("index")))));
		return p;
	}

	@Override
	public LTCondition fromXML(Element xml)
	{
		this.condition = Condition.find(xml.getChildText("id"));
		this.tags = ((TagCompound) NBTParser.parse(xml.getChildText("nbt"), true, false, true)).value();
		return this;
	}

	@Override
	public Component getDisplayComponent()
	{
		return new CTextArea(this.toString());
	}

	@Override
	public String getName(int index)
	{
		return new Text("loottable.condition", new Replacement("<index>", Integer.toString(index + 1))).toString();
	}

	@Override
	public String toString()
	{
		String display = this.condition.translate().toString();
		for (Tag tag : this.tags)
			display += ",\n" + tag.toCommand(-1);
		return display;
	}

	/** @param container - The Compound to store this Condition in.
	 * @return This Condition as an NBT Tag to be generated. */
	public TagCompound toTag(TemplateCompound container)
	{
		Tag[] output = new Tag[this.tags.length + 1];
		output[0] = Tags.LOOTTABLE_CONDITION.create(this.condition.name);

		for (int i = 0; i < this.tags.length; ++i)
			output[i + 1] = this.tags[i];

		return container.create(output);
	}

	@Override
	public Element toXML()
	{
		return new Element("condition").addContent(new Element("id").setText(this.condition.name)).addContent(
				new Element("nbt").setText(Tags.DEFAULT_COMPOUND.create(this.tags).valueForCommand()));
	}

	@Override
	public LTCondition update(CGPanel panel) throws CommandGenerationException
	{
		LTCondition c = ((PanelCondition) panel).generate();
		this.condition = c.condition;
		this.tags = c.tags;
		return this;
	}

	/** @return <code>true</code> if this Condition is verified. Only RNG is checked. */
	public boolean verify()
	{
		if (this.condition == Condition.RANDOM_CHANCE || this.condition == Condition.RANDOM_CHANCE_WITH_LOOTING)
		{
			for (Tag t : this.tags)
				if (t.template == Tags.LT_CONDITION_CHANCE && Math.random() <= ((TagNumber) t).value()) return false;
		}
		return true;
	}
}
