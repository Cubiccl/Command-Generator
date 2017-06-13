package fr.cubiccl.generator.gui.component.interfaces;

import fr.cubiccl.generator.gameobject.GameObject;
import fr.cubiccl.generator.utils.CommandGenerationException;

public interface ICustomObject<T extends GameObject<T>>
{

	/** @return The created Custom Object. */
	public T generate() throws CommandGenerationException;

	/** Sets up this Panel according to the input Object. */
	public void setupFrom(T object);

}
