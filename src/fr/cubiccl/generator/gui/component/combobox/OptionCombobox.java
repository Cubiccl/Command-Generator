package fr.cubiccl.generator.gui.component.combobox;

import fr.cubiccl.generator.gui.component.interfaces.ITranslated;
import fr.cubiccl.generator.utils.Lang;

public class OptionCombobox extends CGComboBox implements ITranslated
{
	private static final long serialVersionUID = -1238384939092751435L;

	private String[] options;
	private String prefix;

	public OptionCombobox(String prefix, String... options)
	{
		super(options);
		this.setOptions(prefix, options);
	}

	@Override
	public String getValue()
	{
		return this.options[this.getSelectedIndex()];
	}

	public void setOptions(String prefix, String... options)
	{
		this.prefix = prefix;
		this.options = options;

		this.updateTranslations();
	}

	@Override
	public void updateTranslations()
	{
		String[] names = new String[options.length];
		for (int i = 0; i < names.length; ++i)
			names[i] = Lang.translate(prefix + "." + options[i]);

		this.setValues(names);
	}

}
