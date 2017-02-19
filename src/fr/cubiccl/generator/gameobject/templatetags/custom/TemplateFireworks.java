package fr.cubiccl.generator.gameobject.templatetags.custom;

import java.awt.GridBagConstraints;

import fr.cubiccl.generator.CommandGenerator;
import fr.cubiccl.generator.gameobject.Explosion;
import fr.cubiccl.generator.gameobject.baseobjects.BaseObject;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.tags.TagList;
import fr.cubiccl.generator.gameobject.tags.TagNumber;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.utils.PanelObjectList;
import fr.cubiccl.generator.gui.component.textfield.CGEntry;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;

public class TemplateFireworks extends TemplateCompound
{
	private class FireworksPanel extends CGPanel
	{
		private static final long serialVersionUID = -2739754221060564022L;

		private CGEntry entryFlight;
		private PanelObjectList<Explosion> panelExplosions;

		public FireworksPanel()
		{
			GridBagConstraints gbc = this.createGridBagLayout();
			this.add((this.entryFlight = new CGEntry(new Text("firework.flight"), "1", Text.INTEGER)).container, gbc);
			++gbc.gridy;
			this.add(this.panelExplosions = new PanelObjectList<Explosion>(null, (String) null, Explosion.class), gbc);

			this.entryFlight.addIntFilter();
		}

		public TagCompound generateFireworks(TemplateCompound container) throws CommandGenerationException
		{
			Explosion[] explosions = this.panelExplosions.values();
			TagCompound[] tags = new TagCompound[explosions.length];
			for (int i = 0; i < tags.length; ++i)
				tags[i] = explosions[i].toTag(Tags.DEFAULT_COMPOUND);
			return container.create(Tags.FIREWORK_FLIGHT.create(Integer.parseInt(this.entryFlight.getText())), Tags.FIREWORK_EXPLOSIONS.create(tags));
		}

		public void setupFrom(TagCompound previousValue)
		{
			for (Tag t : previousValue.value())
			{
				if (t.id().equals(Tags.FIREWORK_FLIGHT.id())) this.entryFlight.setText(Integer.toString(((TagNumber) t).value()));
				if (t.id().equals(Tags.FIREWORK_EXPLOSIONS.id()))
				{
					Tag[] tags = ((TagList) t).value();
					Explosion[] explosions = new Explosion[tags.length];
					for (int i = 0; i < tags.length; ++i)
						explosions[i] = Explosion.createFrom((TagCompound) tags[i]);
					this.panelExplosions.setValues(explosions);
				}
			}
		}

	}

	public TemplateFireworks(String id, byte applicationType, String... applicable)
	{
		super(id, applicationType, applicable);
	}

	@Override
	protected CGPanel createPanel(BaseObject object, Tag previousValue)
	{
		FireworksPanel p = new FireworksPanel();
		if (previousValue != null) p.setupFrom((TagCompound) previousValue);
		p.setName(this.title());
		return p;
	}

	@Override
	public Tag generateTag(BaseObject object, CGPanel panel)
	{
		try
		{
			return ((FireworksPanel) panel).generateFireworks(this);
		} catch (CommandGenerationException e)
		{
			return null;
		}
	}

	@Override
	protected boolean isInputValid(BaseObject object, CGPanel panel)
	{
		FireworksPanel p = ((FireworksPanel) panel);
		try
		{
			p.entryFlight.checkValueInBounds(CGEntry.INTEGER, -128, 127);
			return true;
		} catch (CommandGenerationException e)
		{
			CommandGenerator.report(e);
			return false;
		}
	}

}
