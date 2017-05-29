package fr.cubiccl.generator.utils;

import java.awt.image.BufferedImage;
import java.util.HashMap;

import fr.cubiccl.generator.CommandGenerator;

/** Manages Images. */
public class Textures
{
	/** Maps texture IDs to file paths. */
	private static final HashMap<String, String> paths = new HashMap<String, String>();
	/** Maps file paths to the loaded Buffered Images. */
	private static final HashMap<String, BufferedImage> textures = new HashMap<String, BufferedImage>();

	/** Reloads the textures and remaps the IDs to the paths. */
	public static void createTextures()
	{
		paths.clear();
		textures.clear();
		String[] pathArray = FileUtils.readFileAsArray("textures.txt");
		for (String path : pathArray)
		{
			String[] data = path.split("=");
			for (int i = 0; i < data.length - 1; ++i)
				paths.put(data[i], "textures/" + data[data.length - 1]);
		}
	}

	/** @param textureID - The Identifier of the Texture.
	 * @return The corresponding BufferedImage. Returns null if <code>textureID</code> is not recognized or the image File can't be found. */
	public static BufferedImage getTexture(String textureID)
	{
		textureID = textureID.replaceAll("minecraft:", "");
		String path = paths.get(textureID);

		if (path == null)
		{
			path = locateTexture(textureID);
			if (path == null) return null;
			paths.put(textureID, path);
		}

		if (!textures.containsKey(path))
		{
			loadTexture(path);
			if (textures.get(path) == null) CommandGenerator.log("Couldn't find texture: " + textureID);
		}
		if (textures.get(path) == null) return null;
		return textures.get(path);
	}

	/** Loads an Image.
	 * 
	 * @param path - The path to the File containing the Image. */
	private static void loadTexture(String path)
	{
		textures.put(path, FileUtils.readImage(path));
	}

	/** Locates the Image with the input ID.
	 * 
	 * @param textureID - The ID of the Image.
	 * @return The path to the Image File. */
	private static String locateTexture(String textureID)
	{
		String defaultPath = "textures/" + textureID.replaceAll("\\.", "/");
		if (!textureID.startsWith("item.") || FileUtils.fileExists(defaultPath + ".png")) return defaultPath;
		return "textures/block/" + textureID.replaceAll("\\.", "/").substring("item/".length());
	}

	private Textures()
	{}
}
