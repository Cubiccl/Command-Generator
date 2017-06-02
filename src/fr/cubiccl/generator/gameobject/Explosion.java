package fr.cubiccl.generator.gameobject;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.tags.TagList;
import fr.cubiccl.generator.gameobject.tags.TagNumber;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;
import fr.cubiccl.generator.gui.component.interfaces.IObjectList;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.label.ImageLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.tag.ExplosionPanel;
import fr.cubiccl.generator.gui.component.panel.utils.ListProperties;
import fr.cubiccl.generator.gui.component.panel.utils.PanelColor;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;

/** Represents a Fireworks explosion. */
public class Explosion implements IObjectList<Explosion>
{
	/** Color NBT Tag. */
	public static class Color extends TagNumber implements IObjectList<Color>
	{

		public Color()
		{
			this(0);
		}

		@SuppressWarnings("deprecation")
		public Color(int value)
		{
			super(Tags.DEFAULT_INTEGER, value);
		}

		@Override
		public CGPanel createPanel(ListProperties properties)
		{
			PanelColor p = new PanelColor(null);
			p.setupFrom(this.valueInt());
			return p;
		}

		@Override
		public Component getDisplayComponent()
		{
			CGPanel p = new CGPanel();
			p.add(new CGLabel(new Text(Integer.toString(this.valueInt()), false)));
			p.add(new ImageLabel(this.getTexture()));
			return p;
		}

		@Override
		public String getName(int index)
		{
			return Integer.toString(this.valueInt());
		}

		/** @return A Square filled with this Color. */
		public BufferedImage getTexture()
		{
			BufferedImage img = new BufferedImage(40, 40, BufferedImage.TYPE_INT_RGB);
			Graphics g = img.getGraphics();
			g.setColor(new java.awt.Color(this.valueInt()));
			g.fillRect(0, 0, 40, 40);
			return img;
		}

		@Override
		public Color update(CGPanel panel) throws CommandGenerationException
		{
			this.value = ((PanelColor) panel).getValue();
			return this;
		}

	}

	/** Creates an Explosion from the input NBT Tag.
	 * 
	 * @param tag - The NBT Tag describing the Explosion.
	 * @return The created Explosion. */
	public static Explosion createFrom(TagCompound tag)
	{
		Explosion e = new Explosion();
		for (Tag t : tag.value())
		{
			if (t.id().equals(Tags.FIREWORK_TYPE.id())) e.type = (byte) ((TagNumber) t).valueInt();
			else if (t.id().equals(Tags.FIREWORK_FLICKER.id())) e.flicker = (((TagNumber) t).value() == 1);
			else if (t.id().equals(Tags.FIREWORK_TRAIL.id())) e.trail = (((TagNumber) t).value() == 1);
			else if (t.id().equals(Tags.FIREWORK_COLORS.id()))
			{
				Tag[] list = ((TagList) t).value();
				Color[] values = new Color[list.length];
				for (int i = 0; i < values.length; ++i)
					values[i] = new Color(((TagNumber) list[i]).valueInt());
				e.primary = values;
			} else if (t.id().equals(Tags.FIREWORK_FADE_COLORS.id()))
			{
				Tag[] list = ((TagList) t).value();
				Color[] values = new Color[list.length];
				for (int i = 0; i < values.length; ++i)
					values[i] = new Color(((TagNumber) list[i]).valueInt());
				e.fade = values;
			}
		}
		return e;
	}

	/** The fading Colors of this Explosion. */
	public Color[] fade;
	/** <code>true</code> if this Explosion flickers. */
	public boolean flicker;
	/** The primary Colors of this Explosion. */
	public Color[] primary;
	/** <code>true</code> if this Explosion has a trail. */
	public boolean trail;
	/** The Explosion type. */
	public byte type;

	public Explosion()
	{
		this((byte) 0, false, false, new Color[0], new Color[0]);
	}

	public Explosion(byte type, boolean flicker, boolean trail, Color[] primary, Color[] fade)
	{
		super();
		this.flicker = flicker;
		this.trail = trail;
		this.primary = primary;
		this.fade = fade;
		this.type = type;
	}

	@Override
	public CGPanel createPanel(ListProperties properties)
	{
		ExplosionPanel p = new ExplosionPanel();
		p.setupFrom(this);
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
		return "Explosion " + index;
	}

	/** Converts this Explosion to a NBT Tag.
	 * 
	 * @param container - The template for the container Tag.
	 * @return The Compound container tag. */
	public TagCompound toTag(TemplateCompound container)
	{
		return container.create(Tags.FIREWORK_TYPE.create(this.type), Tags.FIREWORK_FLICKER.create(this.flicker ? 1 : 0),
				Tags.FIREWORK_TRAIL.create(this.trail ? 1 : 0), Tags.FIREWORK_COLORS.create(this.primary), Tags.FIREWORK_FADE_COLORS.create(this.fade));
	}

	@Override
	public Explosion update(CGPanel panel) throws CommandGenerationException
	{
		Explosion e = ((ExplosionPanel) panel).generateExplosion();
		this.flicker = e.flicker;
		this.trail = e.trail;
		this.primary = e.primary;
		this.fade = e.fade;
		this.type = e.type;
		return this;
	}
}
