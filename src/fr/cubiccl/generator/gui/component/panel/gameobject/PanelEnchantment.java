package fr.cubiccl.generator.gui.component.panel.gameobject;

import java.awt.GridBagConstraints;

import fr.cubiccl.generator.gameobject.Enchantment;
import fr.cubiccl.generator.gameobject.ObjectRegistry;
import fr.cubiccl.generator.gameobject.baseobjects.EnchantmentType;
import fr.cubiccl.generator.gui.component.combobox.ObjectCombobox;
import fr.cubiccl.generator.gui.component.label.CLabel;
import fr.cubiccl.generator.gui.component.panel.CPanel;
import fr.cubiccl.generator.gui.component.textfield.CEntry;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Lang;
import fr.cubiccl.generator.utils.WrongValueException;

public class PanelEnchantment extends CPanel
{
	private static final long serialVersionUID = 1904430278915127658L;

	private boolean checkMaximum;
	private ObjectCombobox<EnchantmentType> comboboxEnchant;
	private CEntry entryLevel;

	public PanelEnchantment(boolean checkMaximum)
	{
		super("enchant.title");
		this.checkMaximum = checkMaximum;
		EnchantmentType[] enchants = ObjectRegistry.getEnchantmentTypes();

		GridBagConstraints gbc = this.createGridBagLayout();
		this.add(new CLabel("enchant.select").setHasColumn(true), gbc);
		++gbc.gridx;
		this.add(this.comboboxEnchant = new ObjectCombobox<EnchantmentType>(enchants), gbc);
		--gbc.gridx;
		++gbc.gridy;
		++gbc.gridwidth;
		this.add((this.entryLevel = new CEntry("enchant.level", "1")).container, gbc);

		this.entryLevel.addIntFilter();

		this.updateTranslations();
	}

	public Enchantment generateEnchantment() throws CommandGenerationException
	{
		EnchantmentType enchant = this.selectedEnchantment();
		int level;
		String l = this.entryLevel.getText();

		try
		{
			level = Integer.parseInt(l);
			if (level < 1) throw new WrongValueException(this.entryLevel.label.getAbsoluteText(), Lang.translate("error.integer.greater").replace("<min>", "1"), l);
			if (this.checkMaximum && level > enchant.maxLevel) throw new WrongValueException(this.entryLevel.label.getAbsoluteText(), Lang
					.translate("error.integer.bounds").replace("<min>", "1").replace("<max>", Integer.toString(enchant.maxLevel)), l);
		} catch (NumberFormatException e)
		{
			throw new WrongValueException(this.entryLevel.label.getAbsoluteText(), Lang.translate("error.integer.greater").replace("<min>", "1"), l);
		}

		return new Enchantment(enchant, level);
	}

	private EnchantmentType selectedEnchantment()
	{
		return this.comboboxEnchant.getSelectedObject();
	}

}
