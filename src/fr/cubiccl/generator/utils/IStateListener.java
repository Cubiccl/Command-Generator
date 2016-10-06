package fr.cubiccl.generator.utils;

import fr.cubiccl.generator.gui.component.panel.CPanel;


public interface IStateListener<T extends CPanel>
{

	/** Called when the state is asked to close.
	 * 
	 * @param panel
	 * @return true if the state should close. */
	public boolean shouldStateClose(T panel);

}
