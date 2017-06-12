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

/** Represents an editable Advancement for map making. */
public class Advancement extends GameObject implements IObjectList<Advancement>
{

	/** Creates an Advancement from the input XML element.
	 * 
	 * @param advancement - The XML element describing the Advancement.
	 * @return The created Advancement. */
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
		if (advancement.getChild("not_announced") != null) a.announce = false;
		if (advancement.getChild("no_toast") != null) a.toast = false;
		if (advancement.getChild("hidden") != null) a.hidden = true;

		for (Element criteria : advancement.getChild("criterias").getChildren())
			a.criteria.add(AdvancementCriterion.createFrom(criteria));

		for (Element req : advancement.getChild("requirements").getChildren("r"))
		{
			ArrayList<AdvancementCriterion> r = new ArrayList<AdvancementCriterion>();
			for (Element name : req.getChildren("c"))
				for (AdvancementCriterion c : a.criteria)
					if (name.getText().equals(c.name))
					{
						r.add(c);
						break;
					}
			a.requirements.add(r.toArray(new AdvancementCriterion[r.size()]));
		}

		if (advancement.getChild("experience") != null) a.rewardExperience = Integer.parseInt(advancement.getChildText("experience"));

		for (Element recipe : advancement.getChild("recipes").getChildren())
			a.rewardRecipes.add(recipe.getText());

		for (Element loot : advancement.getChild("loot").getChildren())
			a.rewardLoot.add(loot.getText());

		if (advancement.getChild("function") != null) a.rewardFunction = advancement.getChild("function").getText();

		a.findProperties(advancement);
		return a;
	}

	/** Creates an Advancement from the input NBT Tag.
	 * 
	 * @param tag - The NBT Tag describing the Advancement.
	 * @return The created Advancement. */
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
			if (display.hasTag(Tags.ADVANCEMENT_ANNOUNCE)) a.announce = display.getTag(Tags.ADVANCEMENT_ANNOUNCE).value();
			if (display.hasTag(Tags.ADVANCEMENT_TOAST)) a.toast = display.getTag(Tags.ADVANCEMENT_TOAST).value();
			if (display.hasTag(Tags.ADVANCEMENT_HIDDEN)) a.hidden = display.getTag(Tags.ADVANCEMENT_HIDDEN).value();
		}

		if (tag.hasTag(Tags.ADVANCEMENT_PARENT)) a.parent = tag.getTag(Tags.ADVANCEMENT_PARENT).value();

		if (tag.hasTag(Tags.ADVANCEMENT_CRITERIA)) for (Tag c : tag.getTag(Tags.ADVANCEMENT_CRITERIA).value())
			a.criteria.add(AdvancementCriterion.createFrom((TagCompound) c));

		if (tag.hasTag(Tags.ADVANCEMENT_REQUIREMENTS)) for (Tag req : tag.getTag(Tags.ADVANCEMENT_REQUIREMENTS).value())
		{
			ArrayList<AdvancementCriterion> r = new ArrayList<AdvancementCriterion>();
			for (Tag t : ((TagList) req).value())
			{
				String name = ((TagString) t).value();
				for (AdvancementCriterion c : a.criteria)
				{
					if (c.name.equals(name))
					{
						r.add(c);
						break;
					}
				}
			}
			a.requirements.add(r.toArray(new AdvancementCriterion[r.size()]));
		}

		if (tag.hasTag(Tags.ADVANCEMENT_REWARDS))
		{
			TagCompound rewards = tag.getTag(Tags.ADVANCEMENT_REWARDS);

			if (rewards.hasTag(Tags.ADVANCEMENT_RECIPES)) for (Tag recipe : rewards.getTag(Tags.ADVANCEMENT_RECIPES).value())
				a.rewardRecipes.add(((TagString) recipe).value());

			if (rewards.hasTag(Tags.ADVANCEMENT_LOOT)) for (Tag l : rewards.getTag(Tags.ADVANCEMENT_LOOT).value())
				a.rewardLoot.add(((TagString) l).value());

			if (rewards.hasTag(Tags.ADVANCEMENT_FUNCTION)) a.rewardFunction = rewards.getTag(Tags.ADVANCEMENT_FUNCTION).value();
			if (rewards.hasTag(Tags.ADVANCEMENT_EXPERIENCE)) a.rewardExperience = rewards.getTag(Tags.ADVANCEMENT_EXPERIENCE).valueInt();
		}

		return a;
	}

	/** <code>true</code> if this Advancement should be announced in the chat when granted. */
	public boolean announce;
	/** Path to the texture to use for the background of this Advancement's category if root. */
	public String background;
	/** The list of {@link AdvancementCriterion Criteria} for this Advancement. */
	private ArrayList<AdvancementCriterion> criteria;
	/** The damage value of this Advancement's display Item. */
	private int data;
	/** This Advancement's description. */
	public String description;
	/** This Advancement's frame. */
	public String frame;
	/** <code>true</code> if this Advancement should not be displayed in the Advancement menu before unlocking. */
	public boolean hidden;
	/** This Advancement's display Item. */
	private Item item;
	/** The Title of this Advancement (if Json). */
	public JsonMessage jsonTitle;
	/** This Advancement's parent. If <code>null</code>, this Advancement is a root Advancement. */
	public String parent;
	/** This Advancement's requirements. Each requirement is a list of possible Criteria to satisfy. Each requirement must be acquired, but only one Criterion per requirement is needed. */
	public ArrayList<AdvancementCriterion[]> requirements;
	/** The experience points this Advancement gives when completed. */
	public int rewardExperience;
	/** The path to the Function this Advancement triggers when completed. */
	public String rewardFunction;
	/** The Loot Tables this Advancement triggers when completed. */
	public ArrayList<String> rewardLoot;
	/** The Recipes this Advencement unlocks when completed. */
	public ArrayList<String> rewardRecipes;
	/** The Title of this Advancement (if String). */
	public String title;
	/** <code>true</code> if this Advancement should be shown in a toast message when completed. */
	public boolean toast;

	public Advancement()
	{
		this.rewardLoot = new ArrayList<String>();
		this.rewardRecipes = new ArrayList<String>();
		this.criteria = new ArrayList<AdvancementCriterion>();
		this.requirements = new ArrayList<AdvancementCriterion[]>();
		this.rewardExperience = 0;
		this.item = ObjectRegistry.items.first();
		this.frame = "task";
		this.data = 0;
		this.announce = this.toast = true;
		this.hidden = false;
	}

	/** Adds a Criterion to this Advancement.
	 * 
	 * @param criterion - The criterion to add. */
	public void addCriterion(AdvancementCriterion criterion)
	{
		this.criteria.add(criterion);
		this.onChange();
	}

	/** Deletes all criteria from this Advancement. */
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

	/** Getter for {@link Advancement#criteria}. */
	public AdvancementCriterion[] getCriteria()
	{
		return this.criteria.toArray(new AdvancementCriterion[this.criteria.size()]);
	}

	/** Getter for {@link Advancement#data}. */
	public int getData()
	{
		return this.data;
	}

	@Override
	public Component getDisplayComponent()
	{
		return new PanelAdvancementDisplay(this);
	}

	/** Getter for {@link Advancement#item}. */
	public Item getItem()
	{
		return this.item;
	}

	@Override
	public String getName(int index)
	{
		return this.customName();
	}

	/** @return <code>true</code> if this Advancement is valid, i.e. if either {@link Advancement#title} or {@link Advancement#jsonTitle} isn't <code>null</code>, and {@link Advancement#criteria} isn't empty. */
	public boolean isValid()
	{
		return (this.title != null || this.jsonTitle != null) && this.criteria.size() > 0;
	}

	/** Removes a Criterion from this Advancement.
	 * 
	 * @param criterion - The criterion to remove. */
	public void removeCriterion(AdvancementCriterion criterion)
	{
		ArrayList<AdvancementCriterion[]> newReq = new ArrayList<AdvancementCriterion[]>();
		for (AdvancementCriterion[] req : this.requirements)
		{
			ArrayList<AdvancementCriterion> r = new ArrayList<AdvancementCriterion>();
			for (AdvancementCriterion c : req)
				if (c != criterion) r.add(c);
			newReq.add(r.toArray(new AdvancementCriterion[r.size()]));
		}

		this.criteria.remove(criterion);
		this.requirements.clear();
		this.requirements.addAll(newReq);
	}

	/** Setter for {@link Advancement#data}. */
	public void setData(int data)
	{
		this.data = data;
		this.onChange();
	}

	/** Setter for {@link Advancement#item}. */
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
		if (!this.announce) tags.add(Tags.ADVANCEMENT_ANNOUNCE.create(this.announce));
		if (!this.toast) tags.add(Tags.ADVANCEMENT_TOAST.create(this.toast));
		if (this.hidden) tags.add(Tags.ADVANCEMENT_HIDDEN.create(this.hidden));

		TagCompound display = Tags.ADVANCEMENT_DISPLAY.create(tags.toArray(new Tag[tags.size()]));
		tags.clear();
		tags.add(display);
		if (this.parent != null) tags.add(Tags.ADVANCEMENT_PARENT.create(this.parent));

		ArrayList<Tag> criterias = new ArrayList<Tag>();
		for (AdvancementCriterion c : this.criteria)
			criterias.add(c.toTag());
		tags.add(Tags.ADVANCEMENT_CRITERIA.create(criterias.toArray(new Tag[criterias.size()])));

		if (this.requirements.size() != 0)
		{
			ArrayList<TagList> req = new ArrayList<TagList>();
			for (AdvancementCriterion[] r : this.requirements)
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
		if (this.rewardFunction != null) rewards.add(Tags.ADVANCEMENT_FUNCTION.create(this.rewardFunction));
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
		if (!this.announce) root.addContent(new Element("not_announced"));
		if (!this.toast) root.addContent(new Element("no_toast"));
		if (this.hidden) root.addContent(new Element("hidden"));

		Element criteria = new Element("criterias");
		for (AdvancementCriterion c : this.criteria)
			criteria.addContent(c.toXML());
		root.addContent(criteria);

		Element req = new Element("requirements");
		for (AdvancementCriterion[] r : this.requirements)
		{
			Element r1 = new Element("r");
			for (AdvancementCriterion c : r)
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
		if (this.rewardFunction != null) root.addContent(new Element("function").setText(this.rewardFunction));

		return root;
	}

	@Override
	@Deprecated
	public Advancement update(CGPanel panel) throws CommandGenerationException
	{
		return this;
	}
}
