package fr.cubiccl.generator.gameobject.templatetags.custom.advancements;

import java.util.ArrayList;

import fr.cubiccl.generator.CommandGenerator;
import fr.cubiccl.generator.gameobject.advancements.TestedEffect;
import fr.cubiccl.generator.gameobject.baseobjects.BaseObject;
import fr.cubiccl.generator.gameobject.baseobjects.EffectType;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.utils.PanelObjectList;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;

public class TemplateCriteriaEffects extends TemplateCompound
{

	public TemplateCriteriaEffects(String id, String... applicable)
	{
		super(id, Tag.UNAVAILABLE, applicable);
	}

	public void checkInput(TestedEffect[] values) throws CommandGenerationException
	{
		ArrayList<EffectType> types = new ArrayList<EffectType>();
		for (TestedEffect effect : values)
			if (types.contains(effect.effect)) throw new CommandGenerationException(new Text("error.effects.duplicate").addReplacement("<effect>",
					effect.effect.name()));
			else types.add(effect.effect);
	}

	public TagCompound create(TestedEffect[] values)
	{
		TagCompound t = this.create();
		for (TestedEffect effect : values)
			t.addTag(effect.toTag());
		return t;
	}

	@Override
	protected CGPanel createPanel(BaseObject object, Tag previousValue)
	{
		PanelObjectList<TestedEffect> p = new PanelObjectList<TestedEffect>(null, (Text) null, TestedEffect.class);
		if (previousValue != null) for (Tag t : ((TagCompound) previousValue).value())
			p.add(TestedEffect.createFrom((TagCompound) t));
		return p;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected TagCompound generateTag(BaseObject object, CGPanel panel)
	{
		return this.create(((PanelObjectList<TestedEffect>) panel).values());
	}

	@SuppressWarnings("unchecked")
	@Override
	protected boolean isInputValid(BaseObject object, CGPanel panel)
	{
		try
		{
			this.checkInput(((PanelObjectList<TestedEffect>) panel).values());
		} catch (CommandGenerationException e)
		{
			CommandGenerator.report(e);
			return false;
		}
		return true;
	}

}
