package fr.cubiccl.generator.utils;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import fr.cubiccl.generator.CommandGenerator;

public class FileUtils
{
	public static final String folder = "res/";

	/** @param path - The path to the File.
	 * @return Each line of the File in a String Array. */
	public static String[] readFileAsArray(String path)
	{
		ArrayList<String> lines = new ArrayList<String>();
		File file = new File(folder + path);
		if (file.exists())
		{
			try
			{
				BufferedReader br = new BufferedReader(new FileReader(file));
				String line;
				while ((line = br.readLine()) != null)
					if (!line.equals("") && !line.startsWith("//")) lines.add(line);
				br.close();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		} else CommandGenerator.log("File not found : " + path);
		return lines.toArray(new String[lines.size()]);
	}

	/** @param path - The path to the File.
	 * @return The Image located at <code>path</code>. */
	public static BufferedImage readImage(String path)
	{
		File file = new File(folder + path + ".png");
		if (!file.exists())
		{
			CommandGenerator.log("Couldn't find Image: " + path);
			return null;
		}
		try
		{
			return ImageIO.read(file);
		} catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	/** @param path - Path to the File
	 * @param data - Each line to write. */
	public static void writeToFile(String path, String[] data)
	{
		File file = new File(folder + path);
		if (file.exists()) file.delete();
		try
		{
			PrintWriter bw = new PrintWriter(new FileWriter(file));
			for (String line : data)
				bw.println(line);
			bw.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private FileUtils()
	{}

}
