package fr.cubiccl.generator.gameobject.speedrun;

import java.awt.Component;
import java.awt.GridBagConstraints;

import org.jdom2.Element;

import fr.cubiccl.generator.gui.component.interfaces.IObjectList;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.utils.ListProperties;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;

public class ItemMove implements IObjectList<ItemMove>
{
	public static final String[] LOCATIONS =
	{ "inventory", "enderchest", "head", "chest", "legs", "boots" };
	public static final byte OUT = -1, INVENTORY = 0, ENDERCHEST = 1, HEAD = 2, CHEST = 3, LEGS = 4, BOOTS = 5;

	public static ItemMove createFrom(Checkpoint checkpoint, Element move)
	{
		ItemStackS item = ItemStackS.createFrom(move.getChild("item"));
		ItemMove m = new ItemMove(checkpoint, item);
		m.from = Byte.parseByte(move.getAttributeValue("from"));
		m.to = Byte.parseByte(move.getAttributeValue("to"));
		return m;
	}

	public final Checkpoint checkpoint;
	public byte from, to;
	private ItemStackS stack;

	public ItemMove()
	{
		this(null, new ItemStackS());
	}

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
		return new ItemMovePanel(this);
	}

	private Text description()
	{
		Text t = new Text("speedrun.move.desc");
		if (this.from == OUT) t = new Text("speedrun.move.desc.from");
		if (this.to == OUT) t = new Text("speedrun.move.desc.to");
		if (this.from != OUT) t.addReplacement("<from>", new Text("speedrun.move." + LOCATIONS[this.from]));
		if (this.to != OUT) t.addReplacement("<to>", new Text("speedrun.move." + LOCATIONS[this.to]));
		return t;
	}

	@Override
	public Component getDisplayComponent()
	{
		CGPanel panel = new CGPanel();
		GridBagConstraints gbc = panel.createGridBagLayout();
		panel.add(this.stack.getDisplayComponent(), gbc);
		++gbc.gridy;
		panel.add(new CGLabel(this.description()), gbc);
		return panel;
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
		ItemMovePanel p = (ItemMovePanel) panel;
		this.stack = p.generateItem();
		this.from = p.getFrom();
		this.to = p.getTo();
		return this;
	}

}
