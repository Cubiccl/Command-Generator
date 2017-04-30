package fr.cubiccl.generator.gameobject.advancements;

import java.awt.Component;

import fr.cubiccl.generator.gui.component.interfaces.IObjectList;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.advancement.PanelRequirement;
import fr.cubiccl.generator.gui.component.panel.utils.ListProperties;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;

public class Requirement implements IObjectList<Requirement>
{
	public AdvancementCriteria[] criterias = new AdvancementCriteria[0];

	public Requirement(AdvancementCriteria[] criteria)
	{
		this.criterias = criteria;
	}

	@Override
	public CGPanel createPanel(ListProperties properties)
	{
		PanelRequirement p = new PanelRequirement((Advancement) properties.get("advancement"));
		p.setupFrom(this.criterias);
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
		return this.criterias.length + " " + new Text("advancement.requirements");
	}

	@Override
	public Requirement update(CGPanel panel) throws CommandGenerationException
	{
		this.criterias = ((PanelRequirement) panel).generate();
		return this;
	}

}
