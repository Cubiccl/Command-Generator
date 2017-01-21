package fr.cubiccl.generator.gui.component.interfaces;

import java.awt.image.BufferedImage;

import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.utils.Text;

public interface IObjectList
{

	/** @param editIndex - The index of the Object to edit. -1 if creating a new Object.
	 * @return True if the object was correctly added. If not, the Panel will not close. */
	public boolean addObject(CGPanel panel, int editIndex);

	/** @param editIndex - The index of the Object to edit. -1 if creating a new Object.
	 * @return A panel to display to the user, so that he or she may create or edit an Object. */
	public CGPanel createAddPanel(int editIndex);

	public Text getName(int index);

	public BufferedImage getTexture(int index);

	public String[] getValues();

	public void removeObject(int index);

}
