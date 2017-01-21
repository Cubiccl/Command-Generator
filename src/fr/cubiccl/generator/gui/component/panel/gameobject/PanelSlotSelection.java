package fr.cubiccl.generator.gui.component.panel.gameobject;

import fr.cubiccl.generator.gameobject.baseobjects.Container;
import fr.cubiccl.generator.gui.component.panel.utils.ConfirmPanel;
import fr.cubiccl.generator.utils.Text;

public class PanelSlotSelection extends ConfirmPanel
{
	private static final long serialVersionUID = -116840334378580324L;

	public PanelSlotSelection(Text title, Container[] containers)
	{
		super(title, null);
		this.buttonOK.setVisible(false);
	}

	public String selectedSlot()
	{
		// TODO Auto-generated method stub
		return null;
	}

	public void showContainerFor(String slot)
	{
		// TODO Auto-generated method stub

	}

}
