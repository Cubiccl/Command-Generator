package fr.cubiccl.generator.utils;

import fr.cubiccl.generator.gui.component.panel.CGPanel;

/** To be called when the state is asked to close.
 * 
 * @param <T> - The UI type. */
public interface IStateListener<T extends CGPanel>
{

	/** Called when the current state is asked to close.
	 * 
	 * @param panel - The state's UI.
	 * @return true if the state should close. */
	public boolean shouldStateClose(T panel);

}
