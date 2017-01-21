package fr.cubiccl.generator.gameobject.registries;

import fr.cubiccl.generator.gameobject.baseobjects.Container;

public class ContainerRegistry extends ObjectRegistry<Container>
{

	ContainerRegistry()
	{
		super(false, true, Container.class);
	}

	@Override
	public void register(Container object)
	{
		super.register(object);
		for (String applicable : object.applicable)
			this.registry.put(applicable, object);
	}

}
