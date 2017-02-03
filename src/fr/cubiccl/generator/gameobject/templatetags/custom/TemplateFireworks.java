package fr.cubiccl.generator.gameobject.templatetags.custom;

import java.awt.GridBagConstraints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import fr.cubiccl.generator.CommandGenerator;
import fr.cubiccl.generator.gameobject.baseobjects.BaseObject;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.tags.TagList;
import fr.cubiccl.generator.gameobject.tags.TagNumber;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;
import fr.cubiccl.generator.gui.component.interfaces.IObjectList;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.tag.ExplosionPanel;
import fr.cubiccl.generator.gui.component.panel.utils.PanelObjectList;
import fr.cubiccl.generator.gui.component.textfield.CGEntry;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;

public class TemplateFireworks extends TemplateCompound
{
	private class ExplosionList implements IObjectList
	{
		private ArrayList<TagCompound> explosions = new ArrayList<TagCompound>();

		@Override
		public boolean addObject(CGPanel panel, int editIndex)
		{
			if (editIndex == -1) this.explosions.add(((ExplosionPanel) panel).generateExplosion(Tags.DEFAULT_COMPOUND));
			else this.explosions.set(editIndex, ((ExplosionPanel) panel).generateExplosion(Tags.DEFAULT_COMPOUND));
			return true;
		}

		@Override
		public CGPanel createAddPanel(int editIndex)
		{
			ExplosionPanel p = new ExplosionPanel();
			if (editIndex != -1) p.setupFrom(this.explosions.get(editIndex));
			return p;
		}

		@Override
		public Text getName(int index)
		{
			return new Text("Explosion " + index, false);
		}

		@Override
		public BufferedImage getTexture(int index)
		{
			return null;
		}

		@Override
		public String[] getValues()
		{
			String[] values = new String[this.explosions.size()];
			for (int i = 0; i < values.length; i++)
				values[i] = this.getName(i).toString();
			return values;
		}

		@Override
		public void removeObject(int index)
		{
			this.explosions.remove(index);
		}

		public void setValues(Tag[] value)
		{
			for (Tag t : value)
				if (t instanceof TagCompound) this.explosions.add((TagCompound) t);
		}

	}

	private class FireworksPanel extends CGPanel
	{
		private static final long serialVersionUID = -2739754221060564022L;

		private CGEntry entryFlight;
		private ExplosionList explosions;
		private PanelObjectList panelExplosions;

		public FireworksPanel()
		{
			this.explosions = new ExplosionList();
			GridBagConstraints gbc = this.createGridBagLayout();
			this.add((this.entryFlight = new CGEntry(new Text("firework.flight"), "1", Text.INTEGER)).container, gbc);
			++gbc.gridy;
			this.add(this.panelExplosions = new PanelObjectList(this.explosions), gbc);

			this.entryFlight.addIntFilter();
		}

		public TagCompound generateFireworks(TemplateCompound container) throws CommandGenerationException
		{
			return new TagCompound(container, new TagNumber(Tags.FIREWORK_FLIGHT, Integer.parseInt(this.entryFlight.getText())), new TagList(
					Tags.FIREWORK_EXPLOSIONS, this.explosions.explosions.toArray(new TagCompound[this.explosions.explosions.size()])));
		}

		public void setupFrom(TagCompound previousValue)
		{
			for (Tag t : previousValue.value())
			{
				if (t.id().equals(Tags.FIREWORK_FLIGHT.id())) this.entryFlight.setText(Integer.toString(((TagNumber) t).value()));
				if (t.id().equals(Tags.FIREWORK_EXPLOSIONS.id())) this.explosions.setValues(((TagList) t).value());
			}
			this.panelExplosions.updateList();
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
