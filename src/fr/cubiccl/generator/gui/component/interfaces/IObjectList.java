package fr.cubiccl.generator.gui.component.interfaces;

import java.awt.Component;

import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.utils.ListProperties;
import fr.cubiccl.generator.utils.CommandGenerationException;

/** Any class implementing this interface should have an empty constructor */
public interface IObjectList<T>
{// TODO verify empty constructors

	/** @param properties - The properties of the List.
	 * @return A panel to display to the user, so that he or she may create or edit an Object. */
	public CGPanel createPanel(ListProperties properties);

	/** @return A Panel that should display the selected Object. */
	public Component getDisplayComponent();

	/** @param index - The current index in the List.
	 * @return The name of this Object. */
	public abstract String getName(int index);

	/** @param panel - The panel the user interacted with to create the Object.
	 * @return The created object. */
	public T setupFrom(CGPanel panel) throws CommandGenerationException;
}
