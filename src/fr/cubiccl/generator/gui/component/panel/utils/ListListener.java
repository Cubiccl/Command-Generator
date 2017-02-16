package fr.cubiccl.generator.gui.component.panel.utils;

public interface ListListener<T>
{

	/** Called after a new Object has been added to the list.
	 * 
	 * @param index - The index of the Object.
	 * @param object - The object added. */
	public void onAddition(int index, T object);

	/** Called after an Object has been changed in the list.
	 * 
	 * @param index - The index of the Object.
	 * @param object - The object changed. */
	public void onChange(int index, T object);

	/** Called after an Object has been deleted from the list.
	 * 
	 * @param index - The index of the Object.
	 * @param object - The object deleted. */
	public void onDeletion(int index, T object);

}
