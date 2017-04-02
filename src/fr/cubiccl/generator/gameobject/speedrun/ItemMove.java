package fr.cubiccl.generator.gameobject.speedrun;

import java.awt.Component;

import org.jdom2.Element;

import fr.cubiccl.generator.gui.component.interfaces.IObjectList;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.utils.ListProperties;
import fr.cubiccl.generator.utils.CommandGenerationException;

public class ItemMove implements IObjectList<ItemMove>
{
	public static final byte OUT = -1, INVENTORY = 0, ENDERCHEST = 1, HEAD = 2, CHEST = 3, LEGS = 4, BOOTS = 5;

	public static ItemMove createFrom(Checkpoint checkpoint, Element move)
	{
		ItemStackS item = ItemStackS.createFrom(move);
		ItemMove m = new ItemMove(checkpoint, item);
		m.from = Byte.parseByte(move.getAttributeValue("from"));
		m.to = Byte.parseByte(move.getAttributeValue("to"));
		return m;
	}

	public final Checkpoint checkpoint;
	public byte from, to;
	private ItemStackS stack;

	public ItemMove(Checkpoint checkpoint, ItemStackS item)
	{
		this.checkpoint = checkpoint;
		this.stack = item;
		this.from = OUT;
		this.to = INVENTORY;
	}

	@Override
	public CGPanel createPanel(ListProperties properties)
	{
		// TODO ItemMove.createPanel(properties)
		return null;
	}

	@Override
	public Component getDisplayComponent()
	{
		// TODO ItemMove.getDisplayComponent()
		return this.stack.getDisplayComponent();
	}

	public ItemStackS getItem()
	{
		return this.stack;
	}

	@Override
	public String getName(int index)
	{
		return this.stack.getName(index);
	}

	public boolean isAdding()
	{
		return this.from == OUT;
	}

	public boolean isDeleting()
	{
		return this.to == OUT;
	}

	public boolean isTransferring()
	{
		return !this.isAdding() && !this.isDeleting();
	}

	public void setItem(ItemStackS item)
	{
		this.stack = item;
		this.checkpoint.onChange();
	}

	public Element toXML()
	{
		Element root = new Element("move");
		root.setAttribute("from", Byte.toString(this.from));
		root.setAttribute("to", Byte.toString(this.to));
		root.addContent(this.stack.toXML());
		return root;
	}

	@Override
	public ItemMove update(CGPanel panel) throws CommandGenerationException
	{
		// ItemMove.update(panel)
		return null;
	}

}
