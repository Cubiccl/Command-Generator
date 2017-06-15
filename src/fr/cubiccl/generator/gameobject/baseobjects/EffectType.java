package fr.cubiccl.generator.gameobject.baseobjects;

import java.awt.image.BufferedImage;

import org.jdom2.Element;

import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.utils.Text;
import fr.cubiccl.generator.utils.Textures;

public class EffectType extends BaseObject<EffectType>
{

	/** This Effect's numerical ID. */
	private int idInt;
	/** This Effect's string ID. */
	private String idString;

	public EffectType()
	{}

	public EffectType(int idNum, String idString)
	{
		this.idString = "minecraft:" + idString;
		this.idInt = idNum;
	}

	@Override
	public EffectType fromXML(Element xml)
	{
		this.idString = "minecraft:" + xml.getAttributeValue("idstr");
		this.idInt = Integer.parseInt(xml.getAttributeValue("idint"));
		return this;
	}

	@Override
	public String id()
	{
		return this.idString;
	}

	@Override
	public int idNum()
	{
		return this.idInt;
	}

	@Override
	public Text name()
	{
		return new Text("effect." + this.idString);
	}

	@Override
	public EffectType register()
	{
		ObjectRegistry.effects.register(this);
		return this;
	}

	@Override
	public BufferedImage texture()
	{
		return Textures.getTexture("effect." + this.idString);
	}

	@Override
	public Element toXML()
	{
		return new Element("effect").setAttribute("idint", Integer.toString(this.idInt)).setAttribute("idstr", this.id().substring("minecraft:".length()));
	}

}
