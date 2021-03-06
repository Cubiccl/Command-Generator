package fr.cubiccl.generator.gameobject.registries;

import java.lang.reflect.Array;
import java.util.ArrayList;

import fr.cubiccl.generator.gameobject.baseobjects.BaseObject.ObjectComparatorID;
import fr.cubiccl.generator.gameobject.baseobjects.BaseObject.ObjectComparatorIDNum;
import fr.cubiccl.generator.gameobject.baseobjects.BaseObject.ObjectComparatorName;
import fr.cubiccl.generator.gameobject.baseobjects.Entity;
import fr.cubiccl.generator.utils.Settings;

/** Specific Registry for Entities. */
public class EntityRegistry extends ObjectRegistry<Entity>
{

	EntityRegistry()
	{
		super(false, true, Entity.class);
	}

	@Deprecated
	@Override
	public Entity[] list()
	{
		return super.list();
	}

	/** @param includePlayer - <code>true</code> if the {@link Entity#PLAYER Player} Entity should be included.
	 * @return The list of all Objects in this Registry, sorted as the user requested in the settings. */
	public Entity[] list(boolean includePlayer)
	{
		return this.list(Integer.parseInt(Settings.getSetting(Settings.SORT_TYPE)), includePlayer);
	}

	@Deprecated
	public Entity[] list(int sortType)
	{
		return this.list(sortType, true);
	}

	/** @param sortType - How to sort the Objects.
	 * @param includePlayer - <code>true</code> if the {@link Entity#PLAYER Player} Entity should be included.
	 * @return The list of all Objects in this Registry.
	 * @see ObjectRegistry#SORT_ALPHABETICALLY */
	public Entity[] list(int sortType, boolean includePlayer)
	{
		ArrayList<Entity> a = new ArrayList<Entity>();
		a.addAll(this.registry.values());
		if (!includePlayer) a.remove(Entity.PLAYER);

		if (sortType == SORT_NAME) a.sort(new ObjectComparatorName());
		else if (sortType == SORT_ALPHABETICALLY) a.sort(new ObjectComparatorID());
		else if (sortType == SORT_NUMERICALLY && this.hasNumericalIds) a.sort(new ObjectComparatorIDNum());
		else a.sort(new ObjectComparatorID());

		return a.toArray((Entity[]) Array.newInstance(this.c, a.size()));
	}

}
