package fr.cubiccl.generator.gameobject;

import java.awt.image.BufferedImage;

import fr.cubiccl.generator.gameobject.baseobjects.Slot;
import fr.cubiccl.generator.utils.Text;
import fr.cubiccl.generator.utils.Textures;

public class Container implements NamedObject
{

	public final String id, layoutID;
	public final Slot[] slots;

	public Container(String id, String layoutID, Slot... slots)
	{
		this.id = id;
		this.layoutID = layoutID;
		this.slots = slots;
		ObjectRegistry.registerContainer(this);
	}

	@Override
	public Text name()
	{
		return new Text("container." + this.id);
	}

	public BufferedImage texture()
	{
		return Textures.getTexture("container." + this.layoutID);
	}

}
