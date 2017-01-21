package fr.cubiccl.generator.gameobject.baseobjects;

import java.awt.image.BufferedImage;
import java.util.Comparator;

import fr.cubiccl.generator.utils.Text;

public abstract class BaseObject
{

	/** Sorts Objects alphabetically according to their IDs. */
	public static class ObjectComparatorID implements Comparator<BaseObject>
	{
		@Override
		public int compare(BaseObject o1, BaseObject o2)
		{
			return o1.id().toLowerCase().compareTo(o2.id().toLowerCase());
		}
	}

	/** Sorts Objects in increasing order according to their numerical IDs. */
	public static class ObjectComparatorIDNum implements Comparator<BaseObject>
	{
		@Override
		public int compare(BaseObject o1, BaseObject o2)
		{
			return o1.idNum() - o2.idNum();
		}
	}

	/** @return This Object's identifier. */
	public abstract String id();

	/** @return This Object's numerical ID. */
	public int idNum()
	{
		return -1;
	}

	/** @return This Object's name. Returns this object's ID by default. */
	public Text name()
	{
		return new Text(this.id(), false);
	}

	/** @return This Object's texture. Can be null. */
	public BufferedImage texture()
	{
		return null;
	}

}
