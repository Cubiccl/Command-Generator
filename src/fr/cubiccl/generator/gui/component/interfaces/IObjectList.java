package fr.cubiccl.generator.gui.component.interfaces;

import java.awt.image.BufferedImage;

import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.utils.Text;

public interface IObjectList
{

	/** @return True if the object was correctly added. If not, the Panel will not close. */
	public boolean addObject(CGPanel panel);

	public CGPanel createAddPanel();

	public Text getName(int index);

	public BufferedImage getTexture(int index);

	public String[] getValues();

	public void removeObject(int index);

}
