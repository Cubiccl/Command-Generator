package fr.cubiccl.generator.gameobject;

import java.awt.image.BufferedImage;

import fr.cubiccl.generator.gameobject.baseobjects.BaseObject;
import fr.cubiccl.generator.gameobject.baseobjects.Slot;
import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.utils.Text;
import fr.cubiccl.generator.utils.Textures;

public class Container extends BaseObject
{

	public final String id, layoutID;
	public final Slot[] slots;
	public final int startsAt;

	public Container(String id, String layoutID, int startsAt, Slot... slots)
	{
		this.id = id;
		this.layoutID = layoutID;
		this.startsAt = startsAt;
		this.slots = slots;
		ObjectRegistry.containers.register(this);
	}

	@Override
	public String id()
	{
		return this.id;
	}

	@Override
	public Text name()
	{
		return new Text("container." + this.id);
	}

	@Override
	public BufferedImage texture()
	{
		return Textures.getTexture("container." + this.layoutID);
	}

}
