package net.cubiccl.generator.tests;

import org.junit.Test;

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
	public void tagSplit()
	{
		String[] split = NBTReader.splitTagValues("s:jkl,OM:[kkk,j],klmkm:{\"ww\\\"w\",jkl}");
		
		for (String string : split)
		{
			System.out.println(string);
		}
	}

}
