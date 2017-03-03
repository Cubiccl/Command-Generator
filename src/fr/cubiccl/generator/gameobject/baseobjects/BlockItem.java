package fr.cubiccl.generator.gameobject.baseobjects;

import java.awt.image.BufferedImage;

import org.jdom2.Element;

import fr.cubiccl.generator.CommandGenerator;
import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.utils.Lang;
import fr.cubiccl.generator.utils.Text;
import fr.cubiccl.generator.utils.Textures;
import fr.cubiccl.generator.utils.Utils;

/** Represents a Block or an Item. */
public abstract class BlockItem extends BaseObject
{
	public static final boolean ITEM = true, BLOCK = false;

	/** List of available damage values for this Block/Item. */
	public final int[] damage;
	/** Numerical ID of this Block/Item. */
	private final int idInt;
	/** Text ID of this Block/Item. */
	private final String idString;
	/** Defines how to handle language and texture. */
	public int textureType, langType;
	/** True if this is an Item, false if this is a Block. */
	private final boolean type;

	public BlockItem(boolean type, int idInt, String idString)
	{
		this(type, idInt, idString, 0);
	}

	public BlockItem(boolean type, int idInt, String idString, int maxDamage)
	{
		this(type, idInt, idString, Utils.generateArray(maxDamage));
	}

	public BlockItem(boolean type, int idInt, String idString, int... damage)
	{
		this.type = type;
		this.idString = idString == null ? null : "minecraft:" + idString;
		this.idInt = idInt;
		this.damage = damage;
		this.textureType = 0;
		this.langType = 0;

		if (idString != null) if (this.type == ITEM) ObjectRegistry.items.register((Item) this);
		else ObjectRegistry.blocks.register((Block) this);
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

	private boolean isDamageCustom()
	{
		for (int i = 0; i < damage.length; ++i)
			if (this.damage[i] != i) return true;
		return false;
	}

	public boolean isDataValid(int data)
	{
		for (int i : this.damage)
			if (i == data) return true;
		return false;
	}

	/** @return True if this is an Item, false if this is a Block. */
	public boolean isItem()
	{
		return this.type;
	}

	/** @return The name of the general Block/Item (no damage) */
	public Text mainName()
	{
		String nameID = this.idString;
		if (this.type == BLOCK && Lang.keyExists("block." + nameID)) return new Text("block." + nameID);
		if (Lang.keyExists("item." + nameID)) return new Text("item." + nameID);
		if (this.type == ITEM && Lang.keyExists("block." + nameID)) return new Text("block." + nameID);
		CommandGenerator.log("Couldn't find translation for : block." + nameID);
		return new Text("block." + nameID);
	}

	@Override
	public Text name()
	{
		return this.mainName();
	}

	/** @param damage - A damage value.
	 * @return The name of this Block/Item for the given damage value. */
	public Text name(int damage)
	{
		if (this.damage.length == 1 || this.langType == -1) return this.name(this.idString);
		return this.name(this.idString + "." + damage);
	}

	protected Text name(String nameID)
	{
		if (this.type == BLOCK) return new Text("block." + nameID);
		if (Lang.keyExists("item." + nameID)) return new Text("item." + nameID);
		if (Lang.keyExists("block." + nameID)) return new Text("block." + nameID);
		CommandGenerator.log("Couldn't find translation for : item." + nameID);
		return new Text("item." + nameID);
	}

	@Override
	public BufferedImage texture()
	{
		return this.texture(this.damage[0]);
	}

	/** @param damage - A damage value.
	 * @return The name of this Block/Item for the given damage value. */
	public BufferedImage texture(int damage)
	{
		if (this.damage.length == 1 || this.textureType == -1) return Textures.getTexture(this.typeName() + "." + this.idString);
		if (this.textureType == 0) return Textures.getTexture(this.typeName() + "." + this.idString + "_" + damage);
		if (this.textureType < -1) return Textures.getTexture(this.typeName() + "." + this.idString + "_" + damage / -this.textureType);
		return Textures.getTexture(this.typeName() + "." + this.idString + "_" + damage % this.textureType);
	}

	@Override
	public Element toXML()
	{
		Element root = new Element(this.isItem() ? "item" : "block");
		root.setAttribute("idint", Integer.toString(this.idNum()));
		root.setAttribute("idstr", this.id().substring("minecraft:".length()));
		if (this.textureType != 0) root.addContent(new Element("texture").setText(Integer.toString(this.textureType)));

		if (this.isDamageCustom())
		{
			String d = "" + this.damage[0];
			for (int i = 1; i < this.damage.length; ++i)
				d += ":" + this.damage[i];
			root.addContent(new Element("customdamage").setText(d));
		} else if (this.damage.length != 1) root.addContent(new Element("maxdamage").setText(Integer.toString(this.damage.length - 1)));

		return root;
	}

	private String typeName()
	{
		if (this.type == BLOCK) return "block";
		return "item";
	}

}
