package fr.cubiccl.generator.gui.component.panel.gameobject;

import java.awt.GridBagConstraints;

import fr.cubiccl.generator.gameobject.Enchantment;
import fr.cubiccl.generator.gameobject.baseobjects.EnchantmentType;
import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.gui.component.combobox.ObjectCombobox;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.textfield.CGEntry;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;

public class PanelEnchantment extends CGPanel
{
	private static final long serialVersionUID = 1904430278915127658L;

	private boolean checkMaximum;
	private ObjectCombobox<EnchantmentType> comboboxEnchant;
	private CGEntry entryLevel;

	public PanelEnchantment(boolean checkMaximum)
	{
		super("enchant.title");
		this.checkMaximum = checkMaximum;
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

		this.entryLevel.addIntFilter();

		this.updateTranslations();
	}

	public Enchantment generateEnchantment() throws CommandGenerationException
	{
		if (this.checkMaximum) this.entryLevel.checkValueInBounds(CGEntry.INTEGER, 1, this.selectedEnchantment().maxLevel);
		this.entryLevel.checkValueSuperior(CGEntry.INTEGER, 1);
		return new Enchantment(this.selectedEnchantment(), Integer.parseInt(this.entryLevel.getText()));
	}

	private EnchantmentType selectedEnchantment()
	{
		return this.comboboxEnchant.getSelectedObject();
	}

}
