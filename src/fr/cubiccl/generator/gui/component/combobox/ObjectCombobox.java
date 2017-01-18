package fr.cubiccl.generator.gui.component.combobox;

import fr.cubiccl.generator.gameobject.NamedObject;
import fr.cubiccl.generator.gui.component.interfaces.ITranslated;

public class ObjectCombobox<T extends NamedObject> extends SearchCombobox implements ITranslated
{
	private static final long serialVersionUID = 3964813972467084524L;

	private T[] objects;

	public ObjectCombobox(@SuppressWarnings("unchecked") T... objects)
	{
		super();
		this.objects = objects;
		this.updateTranslations();
	}

	public T getSelectedObject()
	{
		String name = super.getValue();
		for (T object : this.objects)
			if (object.name().toString().equals(name)) return object;
		return this.objects[0];
	}

	public void setSelected(T object)
	{
		this.setSelectedItem(object.name().toString());
	}

	public void setValues(@SuppressWarnings("unchecked") T... objects)
	{
		this.objects = objects;
		this.updateTranslations();
	}

	@Override
	public void updateTranslations()
	{
		String[] names = new String[this.objects.length];
		for (int i = 0; i < names.length; ++i)
			names[i] = this.objects[i].name().toString();
		super.setValues(names);
	}

}
