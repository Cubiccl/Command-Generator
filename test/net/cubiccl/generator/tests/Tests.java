package net.cubiccl.generator.tests;

import java.util.HashMap;

import org.junit.Test;

import fr.cubiccl.generator.gameobject.Recipe;
import fr.cubiccl.generator.gameobject.baseobjects.BlockState;
import fr.cubiccl.generator.gameobject.tags.NBTReader;
import fr.cubiccl.generator.utils.CommandGenerationException;

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

	@Test
	public void testStateParsing()
	{
		try
		{
			HashMap<String, String> parsed = BlockState.parseState("state1=value1,state2=value2");
			for (String id : parsed.keySet())
				System.out.println("id = " + id + ", value = " + parsed.get(id));
		} catch (CommandGenerationException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
