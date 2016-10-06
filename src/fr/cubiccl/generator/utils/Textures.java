package fr.cubiccl.generator.utils;

import java.awt.image.BufferedImage;
import java.util.HashMap;

import fr.cubiccl.generator.CommandGenerator;

public class Textures
{
	/** Maps texture IDs to filePaths. */
	private static final HashMap<String, String> paths = new HashMap<String, String>();
	/** Maps filePaths to the loaded Buffered Images. */
	private static final HashMap<String, BufferedImage> textures = new HashMap<String, BufferedImage>();

	public static void createTextures()
	{
		String[] pathArray = FileUtils.readFileAsArray("textures.txt");
		for (String path : pathArray)
		{
			String[] data = path.split("=");
			for (int i = 0; i < data.length - 1; ++i)
				paths.put(data[i], data[data.length - 1]);
		}
	}

	/** @param textureID - The Identifier of the Texture.
	 * @return The corresponding BufferedImage. Returns null if <code>textureID</code> is not recognized or the File can't be found. */
	public static BufferedImage getTexture(String textureID)
	{
		String path = paths.get(textureID);
		if (!textures.containsKey(path)) loadTexture(path);
		if (textures.get(path) == null) CommandGenerator.log("Couldn't find texture: " + textureID);
		return textures.get(path);
	}

	/** @param textureID - Loads the Buffered Image at <code>path</code> and registers it. */
	private static void loadTexture(String path)
	{
		textures.put(path, FileUtils.readImage(path));
	}

	private Textures()
	{}
}
