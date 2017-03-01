package fr.cubiccl.generator.gameobject;

import java.awt.Component;

import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagBigNumber;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.tags.TagList;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;
import fr.cubiccl.generator.gameobject.templatetags.TemplateList;
import fr.cubiccl.generator.gui.component.interfaces.IObjectList;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelCoordinates;
import fr.cubiccl.generator.gui.component.panel.utils.ListProperties;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;

public class Coordinates extends GameObject implements IObjectList<Coordinates>
{

	public static Coordinates createFrom(String x, String y, String z) throws CommandGenerationException
	{
		float X = 0, Y = 0, Z = 0;
		boolean xr = false, yr = false, zr = false;

		if (x.startsWith("~"))
		{
			x = x.substring(1);
			xr = true;
		}
		if (y.startsWith("~"))
		{
			y = y.substring(1);
			yr = true;
		}
		if (z.startsWith("~"))
		{
			z = z.substring(1);
			zr = true;
		}

		try
		{
			if (!x.equals("")) X = Float.parseFloat(x);
			if (!y.equals("")) Y = Float.parseFloat(y);
			if (!z.equals("")) Z = Float.parseFloat(z);
		} catch (NumberFormatException e)
		{
			throw new CommandGenerationException(new Text("error.coordinates"));
		}

		return new Coordinates(X, Y, Z, xr, yr, zr);
	}

	public static Coordinates createFrom(TagCompound tag)
	{
		float x = 0, y = 0, z = 0;

		for (Tag t : tag.value())
		{
			if (t.id().equals(Tags.COORD_X.id())) x = (float) ((TagBigNumber) t).value;
			if (t.id().equals(Tags.COORD_Y.id())) y = (float) ((TagBigNumber) t).value;
			if (t.id().equals(Tags.COORD_Z.id())) z = (float) ((TagBigNumber) t).value;
		}

		Coordinates c = new Coordinates(x, y, z);
		c.findName(tag);
		return c;
	}

	public static Coordinates createFrom(TagList tag)
	{
		float x = 0, y = 0, z = 0;
		try
		{
			x = (float) (double) ((TagBigNumber) tag.getTag(0)).value();
			y = (float) (double) ((TagBigNumber) tag.getTag(1)).value();
			z = (float) (double) ((TagBigNumber) tag.getTag(2)).value();
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		return new Coordinates(x, y, z);
	}

	public float x, y, z;
	public boolean xRelative, yRelative, zRelative;

	public Coordinates()
	{
		this(0, 0, 0);
	}

	public Coordinates(float x, float y, float z)
	{
		this(x, y, z, false, false, false);
	}

	public Coordinates(float x, float y, float z, boolean xRelative, boolean yRelative, boolean zRelative)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.xRelative = xRelative;
		this.yRelative = yRelative;
		this.zRelative = zRelative;
	}

	@Override
	public CGPanel createPanel(ListProperties properties)
	{
		PanelCoordinates p = new PanelCoordinates(null, true, properties.hasCustomObjects());
		p.setupFrom(this);
		return p;
	}

	@Override
	public Component getDisplayComponent()
	{
		return new CGLabel(new Text(this.toString(), false));
	}

	@Override
	public String getName(int index)
	{
		return this.customName() != null && !this.customName().equals("") ? this.customName() : this.toString();
	}

	@Override
	public String toCommand()
	{
		String command = "";
		if (this.xRelative) command += "~";
		if (!(this.xRelative && this.x == 0)) command += this.x;
		command += " ";

		if (this.yRelative) command += "~";
		if (!(this.yRelative && this.y == 0)) command += this.y;
		command += " ";

		if (this.zRelative) command += "~";
		if (!(this.zRelative && this.z == 0)) command += this.z;

		return command;
	}

	@Override
	public String toString()
	{
		String text = "X=";
		if (this.xRelative) text += "~";
		text += this.x + ", Y=";

		if (this.yRelative) text += "~";
		text += this.y + " ,Z=";

		if (this.zRelative) text += "~";
		text += this.z;

		return text;
	}

	@Override
	public TagCompound toTag(TemplateCompound container, boolean includeName)
	{
		if (includeName) return container.create(Tags.COORD_X.create(this.x), Tags.COORD_Y.create(this.y), Tags.COORD_Z.create(this.z), this.nameTag());
		return container.create(Tags.COORD_X.create(this.x), Tags.COORD_Y.create(this.y), Tags.COORD_Z.create(this.z));
	}

	public TagList toTagList(TemplateList container)
	{
		return container.create(Tags.DEFAULT_FLOAT.create(this.x), Tags.DEFAULT_FLOAT.create(this.y), Tags.DEFAULT_FLOAT.create(this.z));
	}

	@Override
	public Coordinates update(CGPanel panel) throws CommandGenerationException
	{
		Coordinates c = ((PanelCoordinates) panel).generate();
		this.x = c.x;
		this.y = c.y;
		this.z = c.z;
		this.xRelative = c.xRelative;
		this.yRelative = c.yRelative;
		this.zRelative = c.zRelative;
		return this;
	}

}
