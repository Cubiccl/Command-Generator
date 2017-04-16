package fr.cubiccl.generator.gui.component.panel.gameobject;

import java.awt.GridBagConstraints;
import java.awt.event.ActionListener;

import fr.cubiccl.generator.gameobject.Enchantment;
import fr.cubiccl.generator.gameobject.baseobjects.EnchantmentType;
import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.gameobject.registries.ObjectSaver;
import fr.cubiccl.generator.gui.component.combobox.ObjectCombobox;
import fr.cubiccl.generator.gui.component.interfaces.ICustomObject;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.tag.PanelRangedTag;
import fr.cubiccl.generator.gui.component.panel.utils.PanelCustomObject;
import fr.cubiccl.generator.gui.component.textfield.CGEntry;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;

public class PanelEnchantment extends CGPanel implements ICustomObject<Enchantment>
{
	private static final long serialVersionUID = 1904430278915127658L;

	private boolean checkMaximum, ranged;
	private ObjectCombobox<EnchantmentType> comboboxEnchant;
	private CGEntry entryLevel;
	private PanelRangedTag panelRanged;

	public PanelEnchantment(boolean checkMaximum)
	{
		this(checkMaximum, true);
	}

	public PanelEnchantment(boolean checkMaximum, boolean customObjects)
	{
		this(checkMaximum, customObjects, false);
	}

	public PanelEnchantment(boolean checkMaximum, boolean customObjects, boolean ranged)
	{
		super("enchant.title");
		this.checkMaximum = checkMaximum;
		this.ranged = ranged;
		EnchantmentType[] enchants = ObjectRegistry.enchantments.list();

		GridBagConstraints gbc = this.createGridBagLayout();
		this.add(new CGLabel("enchant.select").setHasColumn(true), gbc);
		++gbc.gridx;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		this.add(this.comboboxEnchant = new ObjectCombobox<EnchantmentType>(enchants), gbc);
		--gbc.gridx;
		++gbc.gridy;
		++gbc.gridwidth;
		this.add((this.entryLevel = new CGEntry(new Text("enchant.level"), "1", Text.INTEGER)).container, gbc);
		this.add(this.panelRanged = new PanelRangedTag(new Text("enchant.level"), Text.INTEGER), gbc);

		++gbc.gridy;
		gbc.fill = GridBagConstraints.NONE;
		if (customObjects) this.add(new PanelCustomObject<Enchantment, Enchantment>(this, ObjectSaver.enchantments), gbc);

		this.entryLevel.addIntFilter();
		this.entryLevel.container.setVisible(!this.ranged);
		this.panelRanged.setVisible(this.ranged);

		this.updateTranslations();
	}

	public void addActionListener(ActionListener actionListener)
	{
		this.comboboxEnchant.addActionListener(actionListener);
	}

	public Enchantment generate() throws CommandGenerationException
	{
		if (this.ranged)
		{
			this.panelRanged.checkInput(CGEntry.INTEGER);
			Enchantment e = new Enchantment(this.selectedEnchantment(), 0);
			this.panelRanged.generateValue(e.value());
			return e;
		}
		if (this.checkMaximum) this.entryLevel.checkValueInBounds(CGEntry.INTEGER, 1, this.selectedEnchantment().maxLevel);
		this.entryLevel.checkValueSuperior(CGEntry.INTEGER, 1);
		return new Enchantment(this.selectedEnchantment(), Integer.parseInt(this.entryLevel.getText()));
	}

	public EnchantmentType selectedEnchantment()
	{
		return this.comboboxEnchant.getSelectedObject();
	}

	public void setEnchantment(EnchantmentType enchantment)
	{
		if (enchantment != null) this.comboboxEnchant.setSelected(enchantment);
	}

	public void setHasLevel(boolean hasLevel)
	{
		this.entryLevel.container.setVisible(hasLevel);
	}

	public void setLevel(int level)
	{
		if (!this.checkMaximum || this.selectedEnchantment().maxLevel >= level) this.entryLevel.setText(Integer.toString(level));
	}

	public void setupFrom(Enchantment enchantment)
	{
		this.setEnchantment(enchantment.getType());
		if (this.ranged)
		{
			if (enchantment.value().isRanged) this.panelRanged.setRanged(enchantment.getLevel(), (int) enchantment.value().valueMax);
			else this.panelRanged.setFixed((int) enchantment.getLevel());
		} else this.setLevel(enchantment.getLevel());
	}

}
