package fr.cubiccl.generator.gameobject.templatetags.custom;

import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagString;
import fr.cubiccl.generator.gameobject.templatetags.TemplateString;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelParticle;

public class TemplateParticle extends TemplateString
{
	public int param1 = 0, param2 = 0;

	public TemplateParticle(String id, byte tagType, String[] applicable)
	{
		super(id, tagType, applicable);
	}

	@Override
	protected CGPanel createPanel(String objectId, Tag previousValue)
	{
		PanelParticle p = new PanelParticle(null);

		if (previousValue != null)
		{
			p.setSelected(ObjectRegistry.particles.find(((TagString) previousValue).value()));
			p.setParam1(this.param1);
			p.setParam2(this.param2);
		}

		p.setName(this.title());
		return p;
	}

	@Override
	public TagString generateTag(CGPanel panel)
	{
		PanelParticle p = (PanelParticle) panel;
		this.param1 = p.generateParam1();
		this.param2 = p.generateParam2();
		return new TagString(this, p.selectedParticle().id);
	}

	@Override
	protected boolean isInputValid(CGPanel panel)
	{
		return true;
	}

}
