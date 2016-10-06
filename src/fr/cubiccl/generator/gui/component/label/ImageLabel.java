package fr.cubiccl.generator.gui.component.label;

import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class ImageLabel extends JLabel
{
	private static final long serialVersionUID = -4156272617708437598L;

	public ImageLabel()
	{
		this(null);
	}

	public ImageLabel(BufferedImage texture)
	{
		super("");
		this.setImage(texture);
	}

	public void setImage(BufferedImage texture)
	{
		if (texture == null) this.setIcon(null);
		else this.setIcon(new ImageIcon(texture));
	}

}
