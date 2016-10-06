package fr.cubiccl.generator.gui.component.panel.utils;

import fr.cubiccl.generator.gui.component.textfield.CEntry;

public class EntryPanel extends ConfirmPanel
{
	private static final long serialVersionUID = -3957744287487251022L;

	public final CEntry entry;

	public EntryPanel(String descriptionTextID)
	{
		super();
		this.entry = new CEntry(descriptionTextID);
		this.setMainComponent(this.entry.container);
	}

}
