package fr.cubiccl.generator.gameobject.baseobjects;


public class Block extends BlockItem
{

	public Block(int idInt, String idString)
	{
		super(BLOCK, idInt, idString);
	}

	public Block(int idInt, String idString, int maxDamage)
	{
		super(BLOCK, idInt, idString, maxDamage);
	}

	public Block(int idInt, String idString, int... damage)
	{
		super(BLOCK, idInt, idString, damage);
	}

}
