package fr.cubiccl.generator.gameobject.templatetags.custom;

import java.util.ArrayList;

import fr.cubiccl.generator.CommandGenerator;
import fr.cubiccl.generator.gameobject.baseobjects.BaseObject;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.tags.TagString;
import fr.cubiccl.generator.gameobject.target.Target;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.tag.PanelCommandStats;
import fr.cubiccl.generator.utils.CommandGenerationException;

public class TemplateCommandStats extends TemplateCompound
{

	public TemplateCommandStats(String id, byte applicationType, String[] applicable)
	{
		super(id, applicationType, applicable);
	}

	@Override
	protected CGPanel createPanel(BaseObject object, Tag previousValue)
	{
		TagCompound previous = (TagCompound) previousValue;
		PanelCommandStats p = new PanelCommandStats();

		if (previous != null)
		{
			p.setObjective(PanelCommandStats.SUCCESS_COUNT, (String) previous.getTagFromId(Tags.STATS_SUCCESS_OBJECTIVE.id()).value());
			p.setObjective(PanelCommandStats.AFFECTED_BLOCKS, (String) previous.getTagFromId(Tags.STATS_BLOCKS_OBJECTIVE.id()).value());
			p.setObjective(PanelCommandStats.AFFECTED_ENTITIES, (String) previous.getTagFromId(Tags.STATS_ENTITIES_OBJECTIVE.id()).value());
			p.setObjective(PanelCommandStats.AFFECTED_ITEMS, (String) previous.getTagFromId(Tags.STATS_ITEMS_OBJECTIVE.id()).value());
			p.setObjective(PanelCommandStats.QUERY_RESULT, (String) previous.getTagFromId(Tags.STATS_QUERY_OBJECTIVE.id()).value());

			if (previous.hasTag(Tags.STATS_SUCCESS_NAME.id())) p.setTarget(PanelCommandStats.SUCCESS_COUNT,
					Target.createFrom((String) previous.getTagFromId(Tags.STATS_SUCCESS_NAME.id()).value()));
			if (previous.hasTag(Tags.STATS_BLOCKS_NAME.id())) p.setTarget(PanelCommandStats.AFFECTED_BLOCKS,
					Target.createFrom((String) previous.getTagFromId(Tags.STATS_BLOCKS_NAME.id()).value()));
			if (previous.hasTag(Tags.STATS_ENTITIES_NAME.id())) p.setTarget(PanelCommandStats.AFFECTED_ENTITIES,
					Target.createFrom((String) previous.getTagFromId(Tags.STATS_ENTITIES_NAME.id()).value()));
			if (previous.hasTag(Tags.STATS_ITEMS_NAME.id())) p.setTarget(PanelCommandStats.AFFECTED_ITEMS,
					Target.createFrom((String) previous.getTagFromId(Tags.STATS_ITEMS_NAME.id()).value()));
			if (previous.hasTag(Tags.STATS_QUERY_NAME.id())) p.setTarget(PanelCommandStats.QUERY_RESULT,
					Target.createFrom((String) previous.getTagFromId(Tags.STATS_QUERY_NAME.id()).value()));
		}

		p.setName(this.title());
		return p;
	}

	@Override
	public Tag generateTag(BaseObject object, CGPanel panel)
	{
		PanelCommandStats p = (PanelCommandStats) panel;
		ArrayList<TagString> tags = new ArrayList<TagString>();

		tags.add(new TagString(Tags.STATS_SUCCESS_OBJECTIVE, p.getObjective(PanelCommandStats.SUCCESS_COUNT)));
		tags.add(new TagString(Tags.STATS_BLOCKS_OBJECTIVE, p.getObjective(PanelCommandStats.AFFECTED_BLOCKS)));
		tags.add(new TagString(Tags.STATS_ENTITIES_OBJECTIVE, p.getObjective(PanelCommandStats.AFFECTED_ENTITIES)));
		tags.add(new TagString(Tags.STATS_ITEMS_OBJECTIVE, p.getObjective(PanelCommandStats.AFFECTED_ITEMS)));
		tags.add(new TagString(Tags.STATS_QUERY_OBJECTIVE, p.getObjective(PanelCommandStats.QUERY_RESULT)));

		Target t = p.getTarget(PanelCommandStats.SUCCESS_COUNT);
		if (t != null) tags.add(new TagString(Tags.STATS_SUCCESS_NAME, t.toCommand()));
		t = p.getTarget(PanelCommandStats.AFFECTED_BLOCKS);
		if (t != null) tags.add(new TagString(Tags.STATS_BLOCKS_NAME, t.toCommand()));
		t = p.getTarget(PanelCommandStats.AFFECTED_ENTITIES);
		if (t != null) tags.add(new TagString(Tags.STATS_ENTITIES_NAME, t.toCommand()));
		t = p.getTarget(PanelCommandStats.AFFECTED_ITEMS);
		if (t != null) tags.add(new TagString(Tags.STATS_ITEMS_NAME, t.toCommand()));
		t = p.getTarget(PanelCommandStats.QUERY_RESULT);
		if (t != null) tags.add(new TagString(Tags.STATS_QUERY_NAME, t.toCommand()));

		return new TagCompound(this, tags.toArray(new TagString[tags.size()]));
	}

	@Override
	protected boolean isInputValid(BaseObject object, CGPanel panel)
	{
		try
		{
			((PanelCommandStats) panel).saveCurrent();
		} catch (CommandGenerationException e)
		{
			CommandGenerator.report(e);
			return false;
		}
		return true;
	}
}
