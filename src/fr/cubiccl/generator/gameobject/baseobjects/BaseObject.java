package fr.cubiccl.generator.gameobject.baseobjects;

import java.awt.image.BufferedImage;
import java.util.Comparator;

import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.gameobject.utils.XMLSaveable;
import fr.cubiccl.generator.utils.Text;

/** Parent class for Basic Objects of the Game. */
public abstract class BaseObject<T> implements XMLSaveable<T>
{

	/** Sorts Objects alphabetically according to their IDs. */
	public static class ObjectComparatorID implements Comparator<BaseObject<?>>
	{
		@Override
		public int compare(BaseObject<?> o1, BaseObject<?> o2)
		{
			return o1.id().toLowerCase().compareTo(o2.id().toLowerCase());
		}
	}

	/** Sorts Objects in increasing order according to their numerical IDs. */
	public static class ObjectComparatorIDNum implements Comparator<BaseObject<?>>
	{
		@Override
		public int compare(BaseObject<?> o1, BaseObject<?> o2)
		{
			return o1.idNum() - o2.idNum();
		}
	}

	/** Sorts Objects alphabetically according to their names. */
	public static class ObjectComparatorName implements Comparator<BaseObject<?>>
	{
		@Override
		public int compare(BaseObject<?> o1, BaseObject<?> o2)
		{
			return o1.name().toString().toLowerCase().compareTo(o2.name().toString().toLowerCase());
		}
	}

	/** @return This Object's identifier. */
	public abstract String id();

	/** @return This Object's numerical ID. */
	public int idNum()
	{
		return -1;
	}

	/** @return This Object's name. Returns this object's ID if no name. */
	public Text name()
	{
		return new Text(this.id(), false);
	}

	/** Registers this Object into its own {@link ObjectRegistry}. */
	public abstract T register();

	/** @return This Object's texture. Can be null if no texture. */
	public BufferedImage texture()
	{
		return null;
	}

	@Override
	public String toString()
	{
		return this.name().toString();
	}

}
