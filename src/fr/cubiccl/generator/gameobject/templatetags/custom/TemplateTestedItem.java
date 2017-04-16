package fr.cubiccl.generator.gameobject.templatetags.custom;

import fr.cubiccl.generator.CommandGenerator;
import fr.cubiccl.generator.gameobject.ItemStack;
import fr.cubiccl.generator.gameobject.baseobjects.BaseObject;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.advancement.PanelTestedItem;
import fr.cubiccl.generator.utils.CommandGenerationException;

public class TemplateTestedItem extends TemplateCompound
{

	public TemplateTestedItem(String id, byte applicationType, String... applicable)
	{
		super(id, applicationType, applicable);
	}

	@Override
	protected CGPanel createPanel(BaseObject object, Tag previousValue)
	{
		PanelTestedItem p = new PanelTestedItem();
		if (previousValue != null) p.setupFrom(ItemStack.createFrom((TagCompound) previousValue, true));
		return p;
	}

	@Override
	protected TagCompound generateTag(BaseObject object, CGPanel panel)
	{
		try
		{
			return ((PanelTestedItem) panel).generate().toTagForTest(this);
		} catch (CommandGenerationException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	@Override
	protected boolean isInputValid(BaseObject object, CGPanel panel)
	{
		try
		{
			((PanelTestedItem) panel).generate();
			return true;
		} catch (CommandGenerationException e)
		{
			e.printStackTrace();
			CommandGenerator.report(e);
			return false;
		}
	}

}
