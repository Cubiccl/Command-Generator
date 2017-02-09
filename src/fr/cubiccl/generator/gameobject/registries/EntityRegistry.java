package fr.cubiccl.generator.gameobject.registries;

import java.lang.reflect.Array;
import java.util.ArrayList;

import fr.cubiccl.generator.gameobject.baseobjects.BaseObject.ObjectComparatorID;
import fr.cubiccl.generator.gameobject.baseobjects.BaseObject.ObjectComparatorIDNum;
import fr.cubiccl.generator.gameobject.baseobjects.BaseObject.ObjectComparatorName;
import fr.cubiccl.generator.gameobject.baseobjects.Entity;
import fr.cubiccl.generator.utils.Settings;

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

	public Entity[] list(boolean includePlayer)
	{
		return this.list(Integer.parseInt(Settings.getSetting(Settings.SORT_TYPE)), includePlayer);
	}

	@Deprecated
	public Entity[] list(int sortType)
	{
		return this.list(sortType, true);
	}

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
