package net.cubiccl.generator.tests;

import java.util.HashMap;

import org.junit.Test;

import fr.cubiccl.generator.gameobject.Recipe;
import fr.cubiccl.generator.gameobject.baseobjects.BlockState;
import fr.cubiccl.generator.gameobject.tags.NBTParser;
import fr.cubiccl.generator.utils.CommandGenerationException;

public class Tests
{
	public void multisplit()
	{
		String[] split = NBTParser.multisplit("[{jkl,kkk},{\"ww\\\"w\"}]");

		for (String string : split)
		{
			System.out.println(string);
		}
	}

	public void recipePattern()
	{
		for (String string : Recipe.reducePattern("X XX X   "))
		{
			System.out.println("\"" + string + "\"");
		}
	}

	public void tagSplit()
	{
		String[] split = NBTParser.splitTagValues("s:jkl,OM:[kkk,j],klmkm:{\"ww\\\"w\",jkl}");

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
			e.printStackTrace();
		}
	}

}
