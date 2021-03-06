package fr.cubiccl.generator.gameobject.templatetags.custom;

import fr.cubiccl.generator.gameobject.Explosion;
import fr.cubiccl.generator.gameobject.baseobjects.BaseObject;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.tag.ExplosionPanel;

public class TemplateExplosion extends TemplateCompound
{

	@Override
	protected CGPanel createPanel(BaseObject<?> object, Tag previousValue)
	{
		ExplosionPanel p = new ExplosionPanel();
		if (previousValue != null) p.setupFrom(Explosion.createFrom((TagCompound) previousValue));
		p.setName(this.title());
		return p;
	}

	@Override
	public Tag generateTag(BaseObject<?> object, CGPanel panel)
	{
		return ((ExplosionPanel) panel).generateExplosion().toTag(this);
	}

}
