package fr.cubiccl.generator.gameobject.advancements;

import java.awt.Component;
import java.util.ArrayList;

import org.jdom2.Element;

import fr.cubiccl.generator.gameobject.GameObject;
import fr.cubiccl.generator.gameobject.JsonMessage;
import fr.cubiccl.generator.gameobject.baseobjects.Item;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;
import fr.cubiccl.generator.gui.component.interfaces.IObjectList;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.utils.ListProperties;
import fr.cubiccl.generator.utils.CommandGenerationException;

public class Advancement extends GameObject implements IObjectList<Advancement>
{

	public String background, frame, parent, title;
	private ArrayList<AdvancementCriteria> criteria;
	private Item item;
	public JsonMessage jsonTitle;
	public ArrayList<int[]> requirements;
	public int rewardExperience;
	public ArrayList<String> rewardLoot, rewardRecipes;

	public Advancement()
	{
		this.rewardLoot = new ArrayList<String>();
		this.rewardRecipes = new ArrayList<String>();
		this.criteria = new ArrayList<AdvancementCriteria>();
	}

	@Override
	public CGPanel createPanel(ListProperties properties)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Component getDisplayComponent()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName(int index)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toCommand()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toString()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TagCompound toTag(TemplateCompound container)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Element toXML()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Advancement update(CGPanel panel) throws CommandGenerationException
	{
		// TODO Auto-generated method stub
		return null;
	}

}
