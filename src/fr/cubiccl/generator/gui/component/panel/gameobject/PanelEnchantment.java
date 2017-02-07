package fr.cubiccl.generator.gui.component.panel.gameobject;

import java.awt.GridBagConstraints;

import fr.cubiccl.generator.gameobject.Enchantment;
import fr.cubiccl.generator.gameobject.baseobjects.EnchantmentType;
import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.gameobject.registries.ObjectSaver;
import fr.cubiccl.generator.gui.component.combobox.ObjectCombobox;
import fr.cubiccl.generator.gui.component.interfaces.ICustomObject;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.utils.PanelCustomObject;
import fr.cubiccl.generator.gui.component.textfield.CGEntry;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;

public class PanelEnchantment extends CGPanel implements ICustomObject<Enchantment>
{
	private static final long serialVersionUID = 1904430278915127658L;

	private boolean checkMaximum;
	private ObjectCombobox<EnchantmentType> comboboxEnchant;
	private CGEntry entryLevel;

	public PanelEnchantment(boolean checkMaximum)
	{
		this(checkMaximum, true);
	}

	public PanelEnchantment(boolean checkMaximum, boolean customObjects)
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

		++gbc.gridy;
		gbc.fill = GridBagConstraints.NONE;
		if (customObjects) this.add(new PanelCustomObject<Enchantment>(this, ObjectSaver.enchantments), gbc);

		this.entryLevel.addIntFilter();

		this.updateTranslations();
	}

	public Enchantment generate() throws CommandGenerationException
	{
		if (this.checkMaximum) this.entryLevel.checkValueInBounds(CGEntry.INTEGER, 1, this.selectedEnchantment().maxLevel);
		this.entryLevel.checkValueSuperior(CGEntry.INTEGER, 1);
		return new Enchantment(this.selectedEnchantment(), Integer.parseInt(this.entryLevel.getText()));
	}

	private EnchantmentType selectedEnchantment()
	{
		return this.comboboxEnchant.getSelectedObject();
	}

	public void setEnchantment(EnchantmentType enchantment)
	{
		if (enchantment != null) this.comboboxEnchant.setSelected(enchantment);
	}

	public void setLevel(int level)
	{
		if (!this.checkMaximum || this.selectedEnchantment().maxLevel >= level) this.entryLevel.setText(Integer.toString(level));
	}

	public void setupFrom(Enchantment enchantment)
	{
		this.setEnchantment(enchantment.type);
		this.setLevel(enchantment.level);
	}

}
