package net.cubiccl.generator.tests;

import org.junit.Test;

import fr.cubiccl.generator.gameobject.Recipe;
import fr.cubiccl.generator.gameobject.tags.NBTReader;

public class Tests
{
	public void multisplit()
	{
		String[] split = NBTReader.multisplit("[{jkl,kkk},{\"ww\\\"w\"}]");

		for (String string : split)
		{
			System.out.println(string);
		}
	}

	@Test
	public void recipePattern()
	{
		for (String string : Recipe.createPattern("X XX X   "))
		{
			System.out.println("\"" + string + "\"");
		}
	}

	public void tagSplit()
	{
		String[] split = NBTReader.splitTagValues("s:jkl,OM:[kkk,j],klmkm:{\"ww\\\"w\",jkl}");

		for (String string : split)
		{
			System.out.println(string);
		}
	}

}
