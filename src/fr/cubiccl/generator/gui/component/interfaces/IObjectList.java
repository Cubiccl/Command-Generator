package fr.cubiccl.generator.gui.component.interfaces;

import java.awt.Component;

import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.utils.ListProperties;
import fr.cubiccl.generator.utils.CommandGenerationException;

/** Any class implementing this interface should have an empty constructor */
public interface IObjectList<T>
{

	/** @param properties - The properties of the List.
	 * @return A panel to display to the user, so that he or she may create or edit an Object. */
	public CGPanel createPanel(ListProperties properties);

	/** Clones the input Object.
	 * 
	 * @param object - The Object to copy.
	 * @return This object. Returns <code>null</code> if the duplication fails. */
	public T duplicate(T object);

	/** @return A Panel that should display the selected Object. */
	public Component getDisplayComponent();

	/** @param index - The current index in the List.
	 * @return The name of this Object. */
	public abstract String getName(int index);

	/** Should always return this object, except in extreme cases where it is a core object.
	 * 
	 * @param panel - The panel the user interacted with to create the Object.
	 * @return This object, modified according to the user's choices. */
	public T update(CGPanel panel) throws CommandGenerationException;

}
