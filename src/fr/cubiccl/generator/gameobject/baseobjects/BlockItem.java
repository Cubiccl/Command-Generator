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

	public String customObjectName = null;
	/** List of custom available damage values for this Block/Item. */
	private int[] damageCustom;
	/** The maximum damage value. */
	private int damageMax;
	/** Numerical ID of this Block/Item. */
	private final int idInt;
	/** Text ID of this Block/Item. */
	private final String idString;
	/** Defines how to handle language and texture. */
	public int textureType;
	/** True if this is an Item, false if this is a Block. */
	private final boolean type;

	public BlockItem(boolean type, int idInt, String idString)
	{
		this.type = type;
		this.idString = idString == null ? null : "minecraft:" + idString;
		this.idInt = idInt;
		this.textureType = 0;
		this.damageMax = 0;
		this.damageCustom = null;

		if (idString != null) if (this.type == ITEM) ObjectRegistry.items.register((Item) this);
		else ObjectRegistry.blocks.register((Block) this);
	}

	public BlockItem(boolean type, int idInt, String idString, int maxDamage)
	{
		this(type, idInt, idString);
		this.damageMax = maxDamage;
	}

	public BlockItem(boolean type, int idInt, String idString, int... damage)
	{
		this(type, idInt, idString);
		this.damageMax = -1;
		this.damageCustom = damage;
	}

	public int[] getDamageValues()
	{
		if (this.isDamageCustom()) return this.damageCustom;
		return Utils.generateArray(this.getMaxDamage());
	}

	public int getMaxDamage()
	{
		return this.damageMax;
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

	public boolean isDamageCustom()
	{
		return this.damageMax == -1;
	}

	public boolean isDamageValid(int data)
	{
		if (!this.isDamageCustom()) return data >= 0 && data <= this.damageMax;
		for (int i : this.damageCustom)
			if (i == data) return true;
		return false;
	}

	/** @return True if this is an Item, false if this is a Block. */
	public boolean isItem()
	{
		return this.type;
	}

	private boolean isTextureUnique()
	{
		if (this.damageMax == 0 || this.textureType == -1) return true;
		if (this.textureType < -1)
		{
			for (int i : this.getDamageValues())
				if (i >= -this.textureType) return false;
			return true;
		}
		return false;
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
		if (this.damageMax == 0) return this.name(this.idString);
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

	public void setDamageCustom(int... damage)
	{
		this.damageCustom = damage;
		this.damageMax = -1;
	}

	public void setMaxDamage(int maxDamage)
	{
		this.damageMax = maxDamage;
		this.damageCustom = null;
	}

	protected boolean shouldSaveTextureType()
	{
		return this.textureType != 0 && this.customObjectName == null;
	}

	@Override
	public BufferedImage texture()
	{
		return this.texture(this.isDamageCustom() ? this.damageCustom[0] : 0);
	}

	/** @param damage - A damage value.
	 * @return The name of this Block/Item for the given damage value. */
	public BufferedImage texture(int damage)
	{
		if (this.isTextureUnique()) return Textures.getTexture(this.typeName() + "." + this.idString);
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

		if (this.shouldSaveTextureType()) root.addContent(new Element("texture").setText(Integer.toString(this.textureType)));
		if (this.customObjectName == null)
		{

			if (this.isDamageCustom())
			{
				String d = "" + this.damageCustom[0];
				for (int i = 1; i < this.damageCustom.length; ++i)
					d += ":" + this.damageCustom[i];
				root.addContent(new Element("customdamage").setText(d));
			} else if (this.damageMax != 0) root.addContent(new Element("maxdamage").setText(Integer.toString(this.damageMax)));
		} else root.addContent(new Element(this.isItem() ? "customitem" : "customblock").setText(this.customObjectName));

		return root;
	}

	private String typeName()
	{
		if (this.type == BLOCK) return "block";
		return "item";
	}

}
