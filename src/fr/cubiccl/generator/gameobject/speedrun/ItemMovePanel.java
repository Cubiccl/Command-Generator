package fr.cubiccl.generator.gameobject.speedrun;

import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.utils.CommandGenerationException;

public class ItemMovePanel extends CGPanel
{
	private static final long serialVersionUID = 4330039223060054788L;

	public ItemMovePanel()
	{
		// TODO Auto-generated constructor stub
	}

	public ItemMovePanel(ItemMove itemMove)
	{
		this();
		this.setupFrom(itemMove);
	}

	public ItemStackS generateItem() throws CommandGenerationException
	{
		// TODO Auto-generated method stub
		return null;
	}

	private void setupFrom(ItemMove itemMove)
	{
		// TODO Auto-generated method stub

	}

	public byte getFrom()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	public byte getTo()
	{
		// TODO Auto-generated method stub
		return 0;
	}

}
