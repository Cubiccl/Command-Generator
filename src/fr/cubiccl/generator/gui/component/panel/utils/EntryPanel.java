package fr.cubiccl.generator.gui.component.panel.utils;

import fr.cubiccl.generator.gui.component.textfield.CGEntry;
import fr.cubiccl.generator.utils.Text;

public class EntryPanel extends ConfirmPanel
{
	private static final long serialVersionUID = -3957744287487251022L;

	public final CGEntry entry;

	public EntryPanel(Text descriptionTextID)
	{
		super();
		this.entry = new CGEntry(descriptionTextID);
		this.setMainComponent(this.entry.container);
	}

}
