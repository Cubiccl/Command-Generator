package fr.cubiccl.generator.gui.component.interfaces;

import java.awt.image.BufferedImage;

import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.utils.Text;

public interface IObjectList
{

	public boolean addObject(CGPanel panel);

	public CGPanel createAddPanel();

	public Text getName(int index);

	public BufferedImage getTexture(int index);

	public String[] getValues();

	public void remove(int index);

}
