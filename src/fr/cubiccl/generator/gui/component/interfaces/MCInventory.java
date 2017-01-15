package fr.cubiccl.generator.gui.component.interfaces;

import fr.cubiccl.generator.gui.component.panel.gameobject.MCInventoryDrawer;

public interface MCInventory
{

	public void onClick();

	public void onExit();

	/** Keyboard arrow pressed. {@link MCInventoryDrawer#MOVE_RIGHT} */
	public void onMove(int direction);

	/** Mouse movement. */
	public void onMove(int x, int y);

}
