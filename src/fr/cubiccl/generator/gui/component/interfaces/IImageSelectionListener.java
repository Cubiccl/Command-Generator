package fr.cubiccl.generator.gui.component.interfaces;

public interface IImageSelectionListener
{

	/** Increments or decrements current selection. */
	public void changeSelection(int objectCount);

	public int currentSelection();

	public void selectObject(int objectIndex);

}
