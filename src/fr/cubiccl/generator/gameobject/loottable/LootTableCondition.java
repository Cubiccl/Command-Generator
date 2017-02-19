package fr.cubiccl.generator.gameobject.loottable;

import java.awt.Component;
import java.util.ArrayList;

import fr.cubi.cubigui.CTextArea;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.tags.TagString;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;
import fr.cubiccl.generator.gui.component.interfaces.IObjectList;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.loottable.PanelCondition;
import fr.cubiccl.generator.gui.component.panel.utils.ListProperties;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Replacement;
import fr.cubiccl.generator.utils.Text;

public class LootTableCondition implements IObjectList<LootTableCondition>
{

	public static enum Condition
	{
		ENTITY_PROPERTIES("entity_properties"),
		ENTITY_SCORES("entity_scores"),
		KILLED_BY_PLAYER("killed_by_player"),
		RANDOM_CHANCE("random_chance"),
		RANDOM_CHANCE_WITH_LOOTING("random_chance_with_looting");

		public static Condition get(String name)
		{
			for (Condition c : values())
				if (c.name.equals(name)) return c;
			return null;
		}

		public final String name;

		private Condition(String name)
		{
			this.name = name;
		}

		public Text translate()
		{
			return new Text("lt_condition." + this.name);
		}
	}

	public static LootTableCondition createFrom(TagCompound tag)
	{
		ArrayList<Tag> tags = new ArrayList<Tag>();
		Condition c = null;

		for (Tag t : tag.value())
		{
			if (t.id().equals(Tags.LOOTTABLE_CONDITION.id())) c = Condition.get(((TagString) t).value());
			else tags.add(t);
		}

		if (c == null) return null;
		return new LootTableCondition(c, tags.toArray(new Tag[tags.size()]));
	}

	public Condition condition;
	public Tag[] tags;

	public LootTableCondition()
	{
		this(Condition.values()[0]);
	}

	public LootTableCondition(Condition condition, Tag... tags)
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
	public LootTableCondition setupFrom(CGPanel panel) throws CommandGenerationException
	{
		LootTableCondition c = ((PanelCondition) panel).generate();
		this.condition = c.condition;
		this.tags = c.tags;
		return this;
	}

	@Override
	public String toString()
	{
		String display = this.condition.translate().toString();
		for (Tag tag : this.tags)
			display += ",\n" + tag.toCommand(-1);
		return display;
	}

	public TagCompound toTag(TemplateCompound container)
	{
		Tag[] output = new Tag[this.tags.length + 1];
		output[0] = Tags.LOOTTABLE_CONDITION.create(this.condition.name);

		for (int i = 0; i < this.tags.length; ++i)
			output[i + 1] = this.tags[i];

		return container.create(output);
	}

}
