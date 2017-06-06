package fr.cubiccl.generator.gameobject.baseobjects;

import java.awt.image.BufferedImage;

import org.jdom2.Element;

import fr.cubiccl.generator.CommandGenerator;
import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.utils.Lang;
import fr.cubiccl.generator.utils.Text;
import fr.cubiccl.generator.utils.Textures;
import fr.cubiccl.generator.utils.Utils;

/** Parent class for {@link Block Blocks} and {@link Item Items}. */
public abstract class BlockItem extends BaseObject
{
	public static final boolean ITEM = true, BLOCK = false;

	/** Custom Class name, for storage. */
	public String customObjectName = null;
	/** List of custom available damage values for this Block/Item if {@link BlockItem#damageMax} is <code>-1</code>. */
	private int[] damageCustom;
	/** The maximum damage value. */
	private int damageMax;
	/** Numerical ID of this Block/Item. */
	private final int idInt;
	/** Text ID of this Block/Item. */
	private final String idString;
	/** Defines how to handle language and texture.<br />
	 * If <code>0</code>, default.<br />
	 * If <code>-1</code>, texture is the same for any damage.<br />
	 * If positive, texture index is the damage modulo this texture type.<br />
	 * If negative, texture index is the damage divided by this texture type. */
	public int textureType;
	/** <code>true</code> if this is an {@link Item}, <code>false</code> if this is a {@link Block}. */
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

	/** @return The list of possible damage values for this Block/Item. */
	public int[] getDamageValues()
	{
		if (this.isDamageCustom()) return this.damageCustom;
		return Utils.generateArray(this.getMaxDamage());
	}

	/** @return The maximum damage value for this Block/Item. */
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

	/** @return <code>true</code> if the possible damage values for this Block/Item are not a consecutive list starting at 0. */
	public boolean isDamageCustom()
	{
		return this.damageMax == -1;
	}

	/** @param damage - The damage value to test.
	 * @return <code>true</code> if the input damage value is valid for this Block/Item. */
	public boolean isDamageValid(int damage)
	{
		if (!this.isDamageCustom()) return damage >= 0 && damage <= this.damageMax;
		for (int i : this.damageCustom)
			if (i == damage) return true;
		return false;
	}

	/** @return <code>true</code> if this is an {@link Item}. */
	public boolean isItem()
	{
		return this.type;
	}

	/** @return <code>true</code> if this Block/Item's texture is unique. */
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

	/** @return The name of the general Block/Item (no damage value) */
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
	 * @return The name of this Block/Item for the input damage value. */
	public Text name(int damage)
	{
		if (this.damageMax == 0) return this.namePrefix(this.idString);
		return this.namePrefix(this.idString + "." + damage);
	}

	/** Finds the correct prefix for this Block/Item's name.
	 * 
	 * @param nameID - Contains the ID and damage of the Block/Item.
	 * @return The complete name using the input name ID. */
	protected Text namePrefix(String nameID)
	{
		if (this.type == BLOCK) return new Text("block." + nameID);
		if (Lang.keyExists("item." + nameID)) return new Text("item." + nameID);
		if (Lang.keyExists("block." + nameID)) return new Text("block." + nameID);
		CommandGenerator.log("Couldn't find translation for : item." + nameID);
		return new Text("item." + nameID);
	}

	/** Sets this Block/Item to have custom damage.
	 * 
	 * @param damage - The damage values to apply. */
	public void setDamageCustom(int... damage)
	{
		this.damageCustom = damage;
		this.damageMax = -1;
	}

	/** Sets this Block/Item to have linear damage.
	 * 
	 * @param maxDamage - The maximum damage value. */
	public void setMaxDamage(int maxDamage)
	{
		this.damageMax = maxDamage;
		this.damageCustom = null;
	}

	/** @return <code>true</code> if the texture type should be saved in XML. */
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
	 * @return The name of this Block/Item for the input damage value. */
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

	/** @return <code>"block"</code> if this is a {@link Block}, or <code>"item"</code> if this is an {@link Item}. */
	private String typeName()
	{
		if (this.type == BLOCK) return "block";
		return "item";
	}

}
