package fr.cubiccl.generator.gameobject.advancements;

import java.awt.Component;

import fr.cubiccl.generator.gui.component.interfaces.IObjectList;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.advancement.PanelRequirement;
import fr.cubiccl.generator.gui.component.panel.utils.ListProperties;
import fr.cubiccl.generator.gui.component.panel.utils.PanelObjectList;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;

/** Represents a Requirement for an Advancement. Only used for {@link PanelObjectList Object Lists}. */
public class Requirement implements IObjectList<Requirement>
{
	/** The list of criteria in this Requirement. */
	public AdvancementCriterion[] criteria = new AdvancementCriterion[0];

	public Requirement()
	{}

	public Requirement(AdvancementCriterion[] criteria)
	{
		this.criteria = criteria;
	}

	@Override
	public CGPanel createPanel(ListProperties properties)
	{
		PanelRequirement p = new PanelRequirement((Advancement) properties.get("advancement"));
		p.setupFrom(this.criteria);
		return p;
	}

	@Override
	public Requirement duplicate(Requirement object)
	{
		this.criteria = new AdvancementCriterion[object.criteria.length];
		for (int i = 0; i < this.criteria.length; ++i)
			this.criteria[i] = new AdvancementCriterion().duplicate(object.criteria[i]);
		return this;
	}

	@Override
	public Component getDisplayComponent()
	{
		return null;
	}

	@Override
	public String getName(int index)
	{
		return this.criteria.length + " " + new Text("advancement.requirements");
	}

	@Override
	public Requirement update(CGPanel panel) throws CommandGenerationException
	{
		this.criteria = ((PanelRequirement) panel).generate();
		return this;
	}

}
