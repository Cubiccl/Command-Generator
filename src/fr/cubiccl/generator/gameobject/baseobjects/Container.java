package fr.cubiccl.generator.gameobject.baseobjects;

import java.awt.image.BufferedImage;

import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.utils.Text;
import fr.cubiccl.generator.utils.Textures;

public class Container extends BaseObject
{

	public final String[] applicable;
	public final String id;
	public final Slot[] slots;
	public final int startsAt;

	public Container(String id, int startsAt, String[] applicable, Slot... slots)
	{
		this.id = id;
		this.startsAt = startsAt;
		this.applicable = applicable;
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
		return Textures.getTexture("container." + this.id());
	}

}
