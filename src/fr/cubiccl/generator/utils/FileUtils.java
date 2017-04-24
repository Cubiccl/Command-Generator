package fr.cubiccl.generator.utils;

import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import fr.cubiccl.generator.CommandGenerator;
import fr.cubiccl.generator.gui.Dialogs;

public class FileUtils
{
	public static final String resourcesFolder = "resources/";
	private static final String VERSION_URL = "https://www.dropbox.com/s/1crneiyy6fx3lqk/version.txt?dl=1";

	public static void checkForUpdates()
	{
		CommandGenerator.log("Checking for updates...");
		if (!download(VERSION_URL, resourcesFolder + "version.txt"))
		{
			CommandGenerator.log("Couldn't check for updates, version checking failed.");
			return;
		}
		if (Settings.GENERATOR_VERSION.equals(readFileAsArray("version.txt")[0])) return;

		if (!Dialogs.showConfirmMessage("A new update for the Command Generator has been found ! Would you like to update?", "Yes", "No")) return;
		File f = new File("updater2.exe");
		if (!f.exists()) CommandGenerator.log("Couldn't update because there is no updater... WTF");
		else try
		{
			Desktop.getDesktop().open(f);
			System.exit(0);
		} catch (IOException e)
		{
			e.printStackTrace();
			CommandGenerator.log("Couldn't update : " + e.getMessage());
		}
	}

	public static void delete(String path)
	{
		File f = new File(resourcesFolder + path);
		if (f.exists()) f.delete();
	}

	/** Downloads a single file.
	 * 
	 * @return True if the download was successful. */
	public static boolean download(String url, String destination)
	{
		try
		{
			URL download = new URL(url);
			ReadableByteChannel rbc = Channels.newChannel(download.openStream());
			FileOutputStream fileOut = new FileOutputStream(destination);
			fileOut.getChannel().transferFrom(rbc, 0, 1 << 24);
			fileOut.flush();
			fileOut.close();
			rbc.close();
			return true;
		} catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

	public static boolean fileExists(String path)
	{
		File file = new File(resourcesFolder + path);
		return file != null && file.exists();
	}

	public static String readChangelog()
	{
		String changelog = "Changelog: Version " + Settings.GENERATOR_VERSION;
		String[] data = readFileAsArray("changelog.txt");
		int start = 0, end = 0;
		for (int i = 0; i < data.length; ++i)
			if (data[i].contains("ADDITIONS")) start = i + 1;
			else if (data[i].contains("CHANGED TRANSLATIONS"))
			{
				end = i;
				break;
			}

		for (int i = start; i < end; ++i)
			if (data[i].contains("BUGS FIXED")) changelog += "<br />";
			else changelog += "<br />" + data[i];

		return changelog;
	}

	/** @param path - The path to the File.
	 * @return Each line of the File in a String Array. */
	public static String[] readFileAsArray(String path)
	{
		File file = new File(resourcesFolder + path);

		ArrayList<String> lines = new ArrayList<String>();
		if (file != null && file.exists())
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
		File file = new File(resourcesFolder + path + ".png");
		if (file == null || !file.exists())
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

	/** @param path - Path to the File, .xml excluded. */
	public static Element readXMLFile(String path)
	{
		File file = new File(resourcesFolder + path + ".xml");
		SAXBuilder builder = new SAXBuilder();
		try
		{
			return builder.build(file).getRootElement();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/** Avoids errors when unzipping the update. */
	public static void renameUpdater()
	{
		File delete = new File("updater-windows.exe");
		File rename = new File("updater.exe");
		if (delete.exists() && rename.exists()) delete.delete();
		if (rename.exists()) rename.renameTo(delete);
		delete = new File("Bunifu_UI_v1.52.dll");
		rename = new File("dll1.dll");
		if (delete.exists() && rename.exists()) delete.delete();
		if (rename.exists()) rename.renameTo(delete);

		delete = new File("Ionic.Zip.dll");
		rename = new File("dll2.dll");
		if (delete.exists() && rename.exists()) delete.delete();
		if (rename.exists()) rename.renameTo(delete);

		delete = new File("updater.jar");
		rename = new File("updater-common.jar");
		if (delete.exists() && rename.exists()) delete.delete();
		if (rename.exists()) rename.renameTo(delete);
	}

	/** @param root - The Root XML element to save.
	 * @param path - Path to the File, .xml excluded. */
	public static void saveXMLFile(Element root, String path)
	{
		try
		{
			new XMLOutputter(Format.getPrettyFormat()).output(new Document(root), new FileOutputStream(resourcesFolder + path + ".xml"));
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/** @param path - Path to the File
	 * @param data - Each line to write. */
	public static void writeToFile(String path, String[] data)
	{
		File file = new File(resourcesFolder + path);

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
