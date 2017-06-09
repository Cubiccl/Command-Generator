package fr.cubiccl.generator.gameobject.utils;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.tags.TagNumber;
import fr.cubiccl.generator.gameobject.tags.TagString;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;
import fr.cubiccl.generator.gui.component.panel.tag.PatternsPanel;
import fr.cubiccl.generator.utils.FileUtils;
import fr.cubiccl.generator.utils.Text;
import fr.cubiccl.generator.utils.Utils;

/** Represents Patterns for Banners. */
public class Pattern
{
	/** Colors for the patterns. */
	public static final Color[] COLORS =
	{ Color.WHITE, new Color(216, 127, 51), new Color(178, 76, 216), new Color(102, 153, 216), new Color(229, 229, 51), new Color(127, 204, 25),
			new Color(242, 127, 165), new Color(76, 76, 76), new Color(153, 153, 153), new Color(76, 127, 153), new Color(127, 63, 178),
			new Color(51, 76, 178), new Color(102, 76, 51), new Color(102, 127, 51), new Color(153, 51, 51), new Color(25, 25, 25) };

	/** Creates a Pattern from the input NBT Tag.
	 * 
	 * @param tag - The NBT Tag containing the Tags describing the Pattern.
	 * @return The create Pattern. */
	public static Pattern createFrom(TagCompound tag)
	{
		return new Pattern(((TagNumber) tag.getTagFromId(Tags.PATTERN_COLOR.id())).valueInt(), ((TagString) tag.getTagFromId(Tags.PATTERN_SHAPE.id())).value());
	}

	/** This Pattern's color. */
	public final int color;
	/** This Pattern's shape.
	 * 
	 * @see PatternsPanel#PATTERNS */
	public final String shape;

	public Pattern(int color, String shape)
	{
		super();
		this.color = color;
		this.shape = shape;
	}

	/** Draws this Pattern. */
	public void draw(Graphics g)
	{
		BufferedImage image = FileUtils.readImage("textures/banner/" + this.shape);
		WritableRaster raster = image.getRaster();
		Color c = COLORS[this.color];

		for (int x = 0; x < image.getWidth(); ++x)
			for (int y = 0; y < image.getHeight(); ++y)
			{
				int[] pixels = raster.getPixel(x, y, (int[]) null);
				pixels[0] = c.getRed();
				pixels[1] = c.getGreen();
				pixels[2] = c.getBlue();
				raster.setPixel(x, y, pixels);
			}

		g.drawImage(image, 0, 0, 105, 200, null);
	}

	@Override
	public String toString()
	{
		return new Text("color." + Utils.WOOL_COLORS[this.color]) + " " + new Text("pattern." + this.shape).toString().toLowerCase();
	}

	/** @param container - The container to put the NBT Tags describing this Pattern into.
	 * @return This Pattern as an NBT Tag. */
	public TagCompound toTag(TemplateCompound container)
	{
		return container.create(Tags.PATTERN_SHAPE.create(this.shape), Tags.PATTERN_COLOR.create(this.color));
	}

}