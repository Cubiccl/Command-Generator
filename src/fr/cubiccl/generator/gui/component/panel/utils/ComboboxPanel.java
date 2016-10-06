package fr.cubiccl.generator.gui.component.panel.utils;

import fr.cubiccl.generator.gui.component.combobox.OptionCombobox;

public class ComboboxPanel extends ConfirmPanel
{
	private static final long serialVersionUID = -3957744287487251022L;

	public final OptionCombobox combobox;

	public ComboboxPanel(String descriptionTextID, String prefix, String... options)
	{
		super();
		this.combobox = new OptionCombobox(prefix, options);
		this.setMainComponent(this.combobox);
	}

}
