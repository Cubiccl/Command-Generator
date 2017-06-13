package fr.cubiccl.generator.gameobject.utils;

import org.jdom2.Element;

/** Utility interface. Objects implementing this can be saved or loaded to/from XML. */
public interface XMLSaveable<T>
{

	/** Loads this Object from the input XML element.
	 * 
	 * @param xml - The XLM element describing this Objects.
	 * @return This loaded Object. */
	public T fromXML(Element xml);

	/** @return This Object as an XML element to be saved. */
	public Element toXML();

}
