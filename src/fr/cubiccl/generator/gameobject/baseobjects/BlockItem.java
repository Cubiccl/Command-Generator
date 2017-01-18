package fr.cubiccl.generator.gameobject.baseobjects;

import java.awt.image.BufferedImage;

import fr.cubiccl.generator.CommandGenerator;
import fr.cubiccl.generator.gameobject.IconedObject;
import fr.cubiccl.generator.gameobject.NamedObject;
import fr.cubiccl.generator.gameobject.registries.NumIdObject;
import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.utils.Lang;
import fr.cubiccl.generator.utils.Text;
import fr.cubiccl.generator.utils.Textures;
import fr.cubiccl.generator.utils.Utils;

/** Represents a Block or an Item. */
public class BlockItem implements NamedObject, BaseObject, NumIdObject, IconedObject
{
	public static final boolean ITEM = true, BLOCK = false;

	/** List of available damage values for this Block/Item. */
	public final int[] damage;
	/** Numerical ID of this Block/Item. */
	public final int idInt;
	/** Text ID of this Block/Item. */
	public final String idString;
	/** Defines how to handle language and texture. */
	public int textureType, langType;
	/** True if this is an Item, false if this is a Block. */
	public final boolean type;

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
		this.idString = "minecraft:" + idString;
		this.idInt = idInt;
		this.damage = damage;
		this.textureType = 0;
		this.langType = 0;

		if (this.type == ITEM) ObjectRegistry.items.register((Item) this);
		else ObjectRegistry.blocks.register((Block) this);
	}

	protected Text getName(String nameID)
	{
		if (this.type == BLOCK) return new Text("block." + nameID);
		if (Lang.keyExists("item." + nameID)) return new Text("item." + nameID);
		if (Lang.keyExists("block." + nameID)) return new Text("block." + nameID);
		CommandGenerator.log("Couldn't find translation for : item." + nameID);
		return new Text("item." + nameID);
	}

	@Override
	public String id()
	{
		return this.idString;
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
		return new Text(this.idString, false);
	}

	/** @param damage - A damage value.
	 * @return The name of this Block/Item for the given damage value. */
	public Text name(int damage)
	{
		if (this.damage.length == 1 || this.langType == -1) return this.getName(this.idString);
		return this.getName(this.idString + "." + damage);
	}

	@Override
	public Integer numId()
	{
		return this.idInt;
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

	private String typeName()
	{
		if (this.type == BLOCK) return "block";
		return "item";
	}

}
