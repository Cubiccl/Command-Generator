package fr.cubiccl.generator.utils;

import fr.cubiccl.generator.gui.component.panel.CGPanel;


public interface IStateListener<T extends CGPanel>
{

	/** Called when the state is asked to close.
	 * 
	 * @param panel
	 * @return true if the state should close. */
	public boolean shouldStateClose(T panel);

}
