package fr.cubiccl.generator.gameobject.baseobjects;

import java.awt.Component;

import org.jdom2.Element;

import fr.cubiccl.generator.gui.component.interfaces.IObjectList;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.label.ImageLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelItemSelection;
import fr.cubiccl.generator.gui.component.panel.utils.ListProperties;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;

public class Item extends BlockItem implements IObjectList<Item>
{
	public String cooksTo = null;
	public boolean hasDurability = false;
	public int maxStackSize = 64;

	public Item()
	{
		super(true, 0, null);
	}

	public Item(int idInt, String idString)
	{
		super(ITEM, idInt, idString);
	}

	public Item(int idInt, String idString, int maxDamage)
	{
		super(ITEM, idInt, idString, maxDamage);
	}

	public Item(int idInt, String idString, int... damage)
	{
		super(ITEM, idInt, idString, damage);
	}

	@Override
	public CGPanel createPanel(ListProperties properties)
	{
		PanelItemSelection p = new PanelItemSelection(false);
		if (this.id() != null) p.setSelected(this);
		return p;
	}

	@Override
	public int[] getDamageValues()
	{
		if (this.hasDurability) return new int[]
		{ 0 };
		return super.getDamageValues();
	}

	@Override
	public Component getDisplayComponent()
	{
		CGPanel p = new CGPanel();
		p.add(new CGLabel(this.mainName()));
		p.add(new ImageLabel(this.texture(0)));
		return p;
	}

	public int getDurability()
	{
		return this.getMaxDamage();
	}

	@Override
	public String getName(int index)
	{
		return this.mainName().toString();
	}

	public Text name(int damage)
	{
		if (this.hasDurability) return this.name(this.id());
		return super.name(damage);
	}

	public void setDurability(int durability)
	{
		this.hasDurability = durability != -1;
		this.setMaxDamage(durability);
	}

	@Override
	public Element toXML()
	{
		Element root = super.toXML();
		if (this.maxStackSize != 64 && !this.hasDurability) root.addContent(new Element("stacksize").setText(Integer.toString(this.maxStackSize)));
		if (this.hasDurability) root.getChild("maxdamage").setName("durability");
		if (this.cooksTo != null) root.addContent(new Element("cooksto").setText(this.cooksTo));
		return root;
	}

	@Override
	public Item update(CGPanel panel) throws CommandGenerationException
	{
		return ((PanelItemSelection) panel).selectedItem();
	}

}
