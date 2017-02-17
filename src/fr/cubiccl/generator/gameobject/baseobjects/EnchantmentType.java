package fr.cubiccl.generator.gameobject.baseobjects;

import java.awt.Component;
import java.awt.image.BufferedImage;

import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.gui.component.interfaces.IObjectList;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelEnchantment;
import fr.cubiccl.generator.gui.component.panel.utils.ListProperties;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;

public class EnchantmentType extends BaseObject implements IObjectList<EnchantmentType>
{

	/** This Enchantment's numerical ID. */
	public final int idInt;
	/** This Enchantment's text ID. */
	public final String idString;
	/** This Enchantment's maximum Level in survival. */
	public final int maxLevel;

	public EnchantmentType()
	{
		this(-1, null, 0);
	}

	public EnchantmentType(int idInt, String idString, int maxLevel)
	{
		this.idInt = idInt;
		this.idString = "minecraft:" + idString;
		this.maxLevel = maxLevel;
		if (idInt > 0) ObjectRegistry.enchantments.register(this);
	}

	@Override
	public CGPanel createPanel(ListProperties properties)
	{
		PanelEnchantment p = new PanelEnchantment(false, false);
		p.setEnchantment(this);
		p.setHasLevel(false);
		return p;
	}

	@Override
	public Component getDisplayComponent()
	{
		return new CGLabel(this.name());
	}

	@Override
	public String getName(int index)
	{
		return this.name().toString();
	}

	@Override
	public String id()
	{
		return this.idString;
	}

	@Override
	public int idNum()
	{
		return this.idInt;
	}

	@Override
	public Text name()
	{
		return new Text("enchantment." + this.idString);
	}

	@Override
	public EnchantmentType setupFrom(CGPanel panel) throws CommandGenerationException
	{
		return ((PanelEnchantment) panel).selectedEnchantment();
	}

	public BufferedImage texture()
	{
		return null;
	}

}
