package fr.cubiccl.generator.gameobject;

import java.awt.Component;

import org.jdom2.Element;

import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.tags.TagList;
import fr.cubiccl.generator.gameobject.tags.TagNumber;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;
import fr.cubiccl.generator.gameobject.templatetags.TemplateList;
import fr.cubiccl.generator.gameobject.templatetags.TemplateNumber;
import fr.cubiccl.generator.gui.component.interfaces.IObjectList;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelCoordinates;
import fr.cubiccl.generator.gui.component.panel.utils.ListProperties;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;

/** Represents Coordinates. */
public class Coordinates extends GameObject<Coordinates> implements IObjectList<Coordinates>
{

	/** The X Coordinate. */
	public float x;
	/** <code>true</code> if the {@link Coordinates#x X Coordinate} is relative. */
	public boolean xRelative;
	/** The Y Coordinate. */
	public float y;
	/** <code>true</code> if the {@link Coordinates#y Y Coordinate} is relative. */
	public boolean yRelative;
	/** The Z Coordinate. */
	public float z;
	/** <code>true</code> if the {@link Coordinates#z Z Coordinate} is relative. */
	public boolean zRelative;

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
	public Coordinates fromNBT(TagCompound nbt)
	{
		for (Tag t : nbt.value())
		{
			if (t.id().equals(Tags.COORD_X.id())) this.x = (float) (double) ((TagNumber) t).value();
			if (t.id().equals(Tags.COORD_Y.id())) this.y = (float) (double) ((TagNumber) t).value();
			if (t.id().equals(Tags.COORD_Z.id())) this.z = (float) (double) ((TagNumber) t).value();
		}

		this.findName(nbt);
		return this;
	}

	/** Creates Coordinates from the input NBT Tag.
	 * 
	 * @param tag - The NBT Tag describing the Coordinates.
	 * @return The created Coordinates. */
	public Coordinates fromNBT(TagList tag)
	{
		try
		{
			this.x = (float) (double) ((TagNumber) tag.getTag(0)).value();
			this.y = (float) (double) ((TagNumber) tag.getTag(1)).value();
			this.z = (float) (double) ((TagNumber) tag.getTag(2)).value();
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		return this;
	}

	/** Creates Coordinates from the input values.
	 * 
	 * @param x - The X coordinate.
	 * @param y - The Y coordinate.
	 * @param z - The Z coordinate.
	 * @return The created Coordinates. */
	public Coordinates fromString(String x, String y, String z) throws CommandGenerationException
	{
		if (x.startsWith("~"))
		{
			x = x.substring(1);
			this.xRelative = true;
		}
		if (y.startsWith("~"))
		{
			y = y.substring(1);
			this.yRelative = true;
		}
		if (z.startsWith("~"))
		{
			z = z.substring(1);
			this.zRelative = true;
		}

		try
		{
			if (!x.equals("")) this.x = Float.parseFloat(x);
			if (!y.equals("")) this.y = Float.parseFloat(y);
			if (!z.equals("")) this.z = Float.parseFloat(z);
		} catch (NumberFormatException e)
		{
			throw new CommandGenerationException(new Text("error.coordinates"));
		}

		return this;
	}

	@Override
	public Coordinates fromXML(Element xml)
	{
		this.x = Float.parseFloat(xml.getChildText("x"));
		this.y = Float.parseFloat(xml.getChildText("y"));
		this.z = Float.parseFloat(xml.getChildText("z"));
		this.xRelative = Boolean.parseBoolean(xml.getChildText("xr"));
		this.yRelative = Boolean.parseBoolean(xml.getChildText("yr"));
		this.zRelative = Boolean.parseBoolean(xml.getChildText("zr"));
		this.findProperties(xml);
		return this;
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
	public TagCompound toNBT(TemplateCompound container)
	{
		return container.create(Tags.COORD_X.create(this.x), Tags.COORD_Y.create(this.y), Tags.COORD_Z.create(this.z));
	}

	@Override
	public String toString()
	{
		String text = "X=";
		if (this.xRelative) text += "~";
		if (!this.xRelative || this.x != 0) text += this.x;

		text += ", Y=";
		if (this.yRelative) text += "~";
		if (!this.yRelative || this.y != 0) text += this.y;

		text += ", Z=";
		if (this.zRelative) text += "~";
		if (!this.zRelative || this.z != 0) text += this.z;

		return text;
	}

	/** Converts this Object to a NBT Tag.
	 * 
	 * @param container - The template for the container Tag.
	 * @return The List container tag. */
	public TagList toTagList(TemplateList container)
	{
		return this.toTagList(container, Tags.DEFAULT_FLOAT);
	}

	/** Converts this Object to a NBT Tag.
	 * 
	 * @param container - The template for the container Tag.
	 * @param numberTags - The template for the number Tags inside the List.
	 * @return The List container tag. */
	public TagList toTagList(TemplateList container, TemplateNumber numberTags)
	{
		return container.create(numberTags.create(this.x), numberTags.create(this.y), numberTags.create(this.z));
	}

	@Override
	public Element toXML()
	{
		Element root = this.createRoot("coordinates");
		root.addContent(new Element("x").setText(Float.toString(this.x)));
		root.addContent(new Element("y").setText(Float.toString(this.y)));
		root.addContent(new Element("z").setText(Float.toString(this.z)));
		root.addContent(new Element("xr").setText(Boolean.toString(this.xRelative)));
		root.addContent(new Element("yr").setText(Boolean.toString(this.yRelative)));
		root.addContent(new Element("zr").setText(Boolean.toString(this.zRelative)));
		return root;
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
