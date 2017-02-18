package fr.cubiccl.generator.gui.component.panel.utils;

import java.util.HashMap;

public class ListProperties
{

	private static String[] getProperties(Object[] data)
	{
		String[] properties = new String[data.length / 2];
		for (int i = 0; i < properties.length; ++i)
			properties[i] = data[i * 2].toString();
		return properties;
	}

	private static Object[] getValues(Object[] data)
	{
		Object[] values = new Object[data.length / 2];
		for (int i = 0; i < values.length; ++i)
			values[i] = data[i * 2 + 1];
		return values;
	}

	private HashMap<String, Object> map;

	public ListProperties(Object... data)
	{
		this(getProperties(data), getValues(data));
	}

	public ListProperties(String[] properties, Object[] values)
	{
		this.map = new HashMap<String, Object>();
		for (int i = 0; i < values.length; ++i)
			this.set(properties[i], values[i]);
	}

	public void clear()
	{
		this.map.clear();
	}

	public boolean contains(String property)
	{
		return this.map.containsKey(property);
	}

	public Object get(String property)
	{
		return this.map.get(property);
	}

	public boolean hasCustomObjects()
	{
		return !this.contains("customObjects") || (this.contains("customObjects") && (boolean) this.get("customObjects"));
	}

	public boolean isTrue(String property)
	{
		if (!this.contains(property)) return false;
		Object p = this.get(property);
		if (!(p instanceof Boolean)) return false;
		return (boolean) p;
	}

	public void remove(String property)
	{
		this.map.remove(property);
	}

	public void set(String property, Object value)
	{
		this.map.put(property, value);
	}

}
