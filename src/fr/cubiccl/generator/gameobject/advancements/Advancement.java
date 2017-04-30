package fr.cubiccl.generator.gameobject.advancements;

import java.awt.Component;
import java.util.ArrayList;

import org.jdom2.Element;

import fr.cubiccl.generator.CommandGenerator;
import fr.cubiccl.generator.gameobject.GameObject;
import fr.cubiccl.generator.gameobject.JsonMessage;
import fr.cubiccl.generator.gameobject.baseobjects.Item;
import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.tags.TagList;
import fr.cubiccl.generator.gameobject.tags.TagString;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;
import fr.cubiccl.generator.gui.Dialogs;
import fr.cubiccl.generator.gui.component.interfaces.IObjectList;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.advancement.PanelAdvancement;
import fr.cubiccl.generator.gui.component.panel.gameobject.display.PanelAdvancementDisplay;
import fr.cubiccl.generator.gui.component.panel.utils.ListProperties;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;

public class Advancement extends GameObject implements IObjectList<Advancement>
{

	public static Advancement createFrom(Element advancement)
	{
		Advancement a = new Advancement();
		a.item = ObjectRegistry.items.find(advancement.getAttributeValue("icon"));
		if (advancement.getAttribute("icondata") != null) a.data = Integer.parseInt(advancement.getAttributeValue("icondata"));
		a.frame = advancement.getAttributeValue("frame");
		if (advancement.getChild("title") != null) a.title = advancement.getChildText("title");
		else if (advancement.getChild("json_title") != null) a.jsonTitle = JsonMessage.createFrom(advancement.getChild("json_title"));
		if (advancement.getChild("background") != null) a.background = advancement.getChildText("background");
		if (advancement.getChild("description") != null) a.description = advancement.getChildText("description");
		if (advancement.getChild("parent") != null) a.parent = advancement.getChildText("parent");

		for (Element criteria : advancement.getChild("criterias").getChildren())
			a.criteria.add(AdvancementCriteria.createFrom(criteria));

		for (Element req : advancement.getChild("requirements").getChildren("r"))
		{
			ArrayList<AdvancementCriteria> r = new ArrayList<AdvancementCriteria>();
			for (Element name : req.getChildren("c"))
				for (AdvancementCriteria c : a.criteria)
					if (name.getText().equals(c.name))
					{
						r.add(c);
						break;
					}
			a.requirements.add(r.toArray(new AdvancementCriteria[r.size()]));
		}

		if (advancement.getChild("experience") != null) a.rewardExperience = Integer.parseInt(advancement.getChildText("experience"));

		for (Element recipe : advancement.getChild("recipes").getChildren())
			a.rewardRecipes.add(recipe.getText());

		for (Element loot : advancement.getChild("loot").getChildren())
			a.rewardLoot.add(loot.getText());

		if (advancement.getChild("commands") != null) for (Element command : advancement.getChild("commands").getChildren())
			a.rewardCommands.add(command.getText());

		a.findProperties(advancement);
		return a;
	}

	public static Advancement createFrom(TagCompound tag)
	{
		Advancement a = new Advancement();

		if (tag.hasTag(Tags.ADVANCEMENT_DISPLAY))
		{
			TagCompound display = tag.getTag(Tags.ADVANCEMENT_DISPLAY);
			if (display.hasTag(Tags.ADVANCEMENT_ICON))
			{
				TagCompound icon = display.getTag(Tags.ADVANCEMENT_ICON);
				a.item = ObjectRegistry.items.find(icon.getTag(Tags.RECIPE_ITEM_ID).value());
				if (icon.hasTag(Tags.RECIPE_ITEM_DATA)) a.data = icon.getTag(Tags.RECIPE_ITEM_DATA).valueInt();
			}
			if (display.hasTag(Tags.ADVANCEMENT_TITLE)) a.title = display.getTag(Tags.ADVANCEMENT_TITLE).value();
			else if (display.hasTag(Tags.ADVANCEMENT_TITLE_JSON)) a.jsonTitle = JsonMessage.createFrom(display.getTag(Tags.ADVANCEMENT_TITLE_JSON));
			if (display.hasTag(Tags.ADVANCEMENT_FRAME)) a.frame = display.getTag(Tags.ADVANCEMENT_FRAME).value();
			if (display.hasTag(Tags.ADVANCEMENT_BACKGROUND)) a.background = display.getTag(Tags.ADVANCEMENT_BACKGROUND).value();
			if (display.hasTag(Tags.ADVANCEMENT_DESCRIPTION)) a.description = display.getTag(Tags.ADVANCEMENT_DESCRIPTION).value();
		}

		if (tag.hasTag(Tags.ADVANCEMENT_PARENT)) a.parent = tag.getTag(Tags.ADVANCEMENT_PARENT).value();

		if (tag.hasTag(Tags.ADVANCEMENT_CRITERIA)) for (Tag c : tag.getTag(Tags.ADVANCEMENT_CRITERIA).value())
			a.criteria.add(AdvancementCriteria.createFrom((TagCompound) c));

		if (tag.hasTag(Tags.ADVANCEMENT_REQUIREMENTS)) for (Tag req : tag.getTag(Tags.ADVANCEMENT_REQUIREMENTS).value())
		{
			ArrayList<AdvancementCriteria> r = new ArrayList<AdvancementCriteria>();
			for (Tag t : ((TagList) req).value())
			{
				String name = ((TagString) t).value();
				for (AdvancementCriteria c : a.criteria)
				{
					if (c.name.equals(name))
					{
						r.add(c);
						break;
					}
				}
			}
			a.requirements.add(r.toArray(new AdvancementCriteria[r.size()]));
		}

		if (tag.hasTag(Tags.ADVANCEMENT_REWARDS))
		{
			TagCompound rewards = tag.getTag(Tags.ADVANCEMENT_REWARDS);

			if (rewards.hasTag(Tags.ADVANCEMENT_RECIPES)) for (Tag recipe : rewards.getTag(Tags.ADVANCEMENT_RECIPES).value())
				a.rewardRecipes.add(((TagString) recipe).value());

			if (rewards.hasTag(Tags.ADVANCEMENT_LOOT)) for (Tag l : rewards.getTag(Tags.ADVANCEMENT_LOOT).value())
				a.rewardLoot.add(((TagString) l).value());

			if (rewards.hasTag(Tags.ADVANCEMENT_COMMANDS)) for (Tag l : rewards.getTag(Tags.ADVANCEMENT_COMMANDS).value())
				a.rewardCommands.add(((TagString) l).value());

			if (rewards.hasTag(Tags.ADVANCEMENT_EXPERIENCE)) a.rewardExperience = rewards.getTag(Tags.ADVANCEMENT_EXPERIENCE).valueInt();
		}

		return a;
	}

	public String background, description, frame, parent, title;
	private ArrayList<AdvancementCriteria> criteria;
	private int data;
	private Item item;
	public JsonMessage jsonTitle;
	public ArrayList<AdvancementCriteria[]> requirements;
	public int rewardExperience;
	public ArrayList<String> rewardLoot, rewardRecipes, rewardCommands;

	public Advancement()
	{
		this.rewardLoot = new ArrayList<String>();
		this.rewardRecipes = new ArrayList<String>();
		this.rewardCommands = new ArrayList<String>();
		this.criteria = new ArrayList<AdvancementCriteria>();
		this.requirements = new ArrayList<AdvancementCriteria[]>();
		this.rewardExperience = 0;
		this.item = ObjectRegistry.items.first();
		this.frame = "task";
		this.data = 0;
	}

	public void addCriterion(AdvancementCriteria criteria)
	{
		this.criteria.add(criteria);
		this.onChange();
	}

	public void clearCriteria()
	{
		this.criteria.clear();
	}

	@Override
	public CGPanel createPanel(ListProperties properties)
	{
		if ((boolean) properties.get("new"))
		{
			String name = Dialogs.showInputDialog(new Text("objects.name").toString());
			if (name != null) this.setCustomName(name);
			else return null;
			CommandGenerator.window.panelAdvancementSelection.list.add(this);
		}
		CommandGenerator.stateManager.clear();
		return new PanelAdvancement(this);
	}

	public AdvancementCriteria[] getCriteria()
	{
		return this.criteria.toArray(new AdvancementCriteria[this.criteria.size()]);
	}

	public int getData()
	{
		return this.data;
	}

	@Override
	public Component getDisplayComponent()
	{
		return new PanelAdvancementDisplay(this);
	}

	public Item getItem()
	{
		return this.item;
	}

	@Override
	public String getName(int index)
	{
		return this.customName();
	}

	public boolean isValid()
	{
		return (this.title != null || this.jsonTitle != null) && this.criteria.size() > 0;
	}

	public void removeCriterion(AdvancementCriteria criteria)
	{
		ArrayList<AdvancementCriteria[]> newReq = new ArrayList<AdvancementCriteria[]>();
		for (AdvancementCriteria[] req : this.requirements)
		{
			ArrayList<AdvancementCriteria> r = new ArrayList<AdvancementCriteria>();
			for (AdvancementCriteria c : req)
				if (c != criteria) r.add(c);
			newReq.add(r.toArray(new AdvancementCriteria[r.size()]));
		}

		this.criteria.remove(criteria);
		this.requirements.clear();
		this.requirements.addAll(newReq);
	}

	public void setData(int data)
	{
		this.data = data;
		this.onChange();
	}

	public void setItem(Item item)
	{
		this.item = item;
		this.onChange();
	}

	@Override
	public String toCommand()
	{
		return this.toTag(Tags.DEFAULT_COMPOUND).valueForCommand(0);
	}

	@Override
	public String toString()
	{
		return this.customName();
	}

	@Override
	public TagCompound toTag(TemplateCompound container)
	{
		ArrayList<Tag> tags = new ArrayList<Tag>();
		if (this.data != 0) tags.add(Tags.ADVANCEMENT_ICON.create(Tags.RECIPE_ITEM_ID.create(this.item.id()), Tags.RECIPE_ITEM_DATA.create(this.data)));
		tags.add(Tags.ADVANCEMENT_ICON.create(Tags.ITEM_IDITEM.create(this.item.id())));
		if (this.title != null) tags.add(Tags.ADVANCEMENT_TITLE.create(this.title));
		else if (this.jsonTitle != null) tags.add(this.jsonTitle.toTag(Tags.ADVANCEMENT_TITLE_JSON));
		tags.add(Tags.ADVANCEMENT_FRAME.create(this.frame));
		if (this.background != null) tags.add(Tags.ADVANCEMENT_BACKGROUND.create(this.background));
		if (this.description != null) tags.add(Tags.ADVANCEMENT_DESCRIPTION.create(this.description));

		TagCompound display = Tags.ADVANCEMENT_DISPLAY.create(tags.toArray(new Tag[tags.size()]));
		tags.clear();
		tags.add(display);
		if (this.parent != null) tags.add(Tags.ADVANCEMENT_PARENT.create(this.parent));

		ArrayList<Tag> criterias = new ArrayList<Tag>();
		for (AdvancementCriteria c : this.criteria)
			criterias.add(c.toTag());
		tags.add(Tags.ADVANCEMENT_CRITERIA.create(criterias.toArray(new Tag[criterias.size()])));

		if (this.requirements.size() != 0)
		{
			ArrayList<TagList> req = new ArrayList<TagList>();
			for (AdvancementCriteria[] r : this.requirements)
			{
				TagString[] names = new TagString[r.length];
				for (int i = 0; i < names.length; ++i)
					names[i] = Tags.DEFAULT_STRING.create(r[i].name);
				req.add(Tags.DEFAULT_LIST.create(names));
			}
			tags.add(Tags.ADVANCEMENT_REQUIREMENTS.create(req.toArray(new TagList[req.size()])));
		}

		ArrayList<Tag> rewards = new ArrayList<Tag>();
		if (this.rewardRecipes.size() != 0)
		{
			ArrayList<TagString> recipes = new ArrayList<TagString>();
			for (String recipe : this.rewardRecipes)
				recipes.add(Tags.DEFAULT_STRING.create(recipe));
			rewards.add(Tags.ADVANCEMENT_RECIPES.create(recipes.toArray(new Tag[recipes.size()])));
		}
		if (this.rewardLoot.size() != 0)
		{
			ArrayList<TagString> loot = new ArrayList<TagString>();
			for (String l : this.rewardLoot)
				loot.add(Tags.DEFAULT_STRING.create(l));
			rewards.add(Tags.ADVANCEMENT_LOOT.create(loot.toArray(new Tag[loot.size()])));
		}
		if (this.rewardCommands.size() != 0)
		{
			ArrayList<TagString> commands = new ArrayList<TagString>();
			for (String c : this.rewardCommands)
				commands.add(Tags.DEFAULT_STRING.create(c));
			rewards.add(Tags.ADVANCEMENT_COMMANDS.create(commands.toArray(new Tag[commands.size()])));
		}
		if (this.rewardExperience != 0) rewards.add(Tags.ADVANCEMENT_EXPERIENCE.create(this.rewardExperience));
		if (rewards.size() != 0) tags.add(Tags.ADVANCEMENT_REWARDS.create(rewards.toArray(new Tag[rewards.size()])));

		return container.create(tags.toArray(new Tag[tags.size()])).setJson(true);
	}

	@Override
	public Element toXML()
	{
		Element root = this.createRoot("advancement");
		root.setAttribute("icon", this.item.id());
		if (this.data != 0) root.setAttribute("icondata", Integer.toString(this.data));
		root.setAttribute("frame", this.frame);
		if (this.title != null) root.addContent(new Element("title").setText(this.title));
		if (this.description != null) root.addContent(new Element("description").setText(this.description));
		else if (this.jsonTitle != null) root.addContent(this.jsonTitle.toXML());
		if (this.background != null) root.addContent(new Element("background").setText(this.background));
		if (this.parent != null) root.addContent(new Element("parent").setText(this.parent));

		Element criteria = new Element("criterias");
		for (AdvancementCriteria c : this.criteria)
			criteria.addContent(c.toXML());
		root.addContent(criteria);

		Element req = new Element("requirements");
		for (AdvancementCriteria[] r : this.requirements)
		{
			Element r1 = new Element("r");
			for (AdvancementCriteria c : r)
				r1.addContent(new Element("c").setText(c.name));
			req.addContent(r1);
		}
		root.addContent(req);

		if (this.rewardExperience != 0) root.addContent(new Element("experience").setText(Integer.toString(this.rewardExperience)));

		Element recipes = new Element("recipes");
		for (String recipe : this.rewardRecipes)
			recipes.addContent(new Element("r").setText(recipe));
		root.addContent(recipes);

		Element loot = new Element("loot");
		for (String l : this.rewardLoot)
			loot.addContent(new Element("l").setText(l));
		root.addContent(loot);

		Element commands = new Element("commands");
		for (String c : this.rewardCommands)
			commands.addContent(new Element("c").setText(c));
		root.addContent(commands);

		return root;
	}

	@Override
	@Deprecated
	public Advancement update(CGPanel panel) throws CommandGenerationException
	{
		return this;
	}
}
