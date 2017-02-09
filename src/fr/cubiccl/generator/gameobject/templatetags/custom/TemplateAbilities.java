package fr.cubiccl.generator.gameobject.templatetags.custom;

import fr.cubiccl.generator.CommandGenerator;
import fr.cubiccl.generator.gameobject.baseobjects.BaseObject;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.tag.PanelAbilities;
import fr.cubiccl.generator.utils.CommandGenerationException;

public class TemplateAbilities extends TemplateCompound
{

	public TemplateAbilities(String id, byte applicationType, String[] applicable)
	{
		super(id, applicationType, applicable);
	}

	@Override
	protected PanelAbilities createPanel(BaseObject object, Tag previousValue)
	{
		PanelAbilities p = new PanelAbilities();
		if (previousValue != null) p.setupFrom((TagCompound) previousValue);
		p.setName(this.title());
		return p;
	}

	@Override
	protected TagCompound generateTag(BaseObject object, CGPanel panel)
	{
		try
		{
			return new TagCompound(this, ((PanelAbilities) panel).generateAbilities());
		} catch (CommandGenerationException e)
		{
			return null;
		}
	}

	@Override
	protected boolean isInputValid(BaseObject object, CGPanel panel)
	{
		try
		{
			((PanelAbilities) panel).generateAbilities();
			return true;
		} catch (CommandGenerationException e)
		{
			CommandGenerator.report(e);
			return false;
		}
	}

}
