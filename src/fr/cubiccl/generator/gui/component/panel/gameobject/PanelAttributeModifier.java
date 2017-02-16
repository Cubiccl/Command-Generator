package fr.cubiccl.generator.gui.component.panel.gameobject;

import java.awt.GridBagConstraints;
import java.util.concurrent.ThreadLocalRandom;

import fr.cubiccl.generator.CommandGenerator;
import fr.cubiccl.generator.gameobject.AttributeModifier;
import fr.cubiccl.generator.gameobject.baseobjects.Attribute;
import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.gameobject.registries.ObjectSaver;
import fr.cubiccl.generator.gui.component.combobox.ObjectCombobox;
import fr.cubiccl.generator.gui.component.combobox.OptionCombobox;
import fr.cubiccl.generator.gui.component.interfaces.ICustomObject;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.utils.PanelCustomObject;
import fr.cubiccl.generator.gui.component.textfield.CGEntry;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;
import fr.cubiccl.generator.utils.Utils;

public class PanelAttributeModifier extends CGPanel implements ICustomObject<AttributeModifier>
{
	private static final long serialVersionUID = -8412726255073387389L;

	private ObjectCombobox<Attribute> comboboxAttribute;
	private OptionCombobox comboboxOperation, comboboxSlot;
	private CGEntry entryName, entryAmount, entryUUIDMost, entryUUIDLeast;
	private boolean isApplied;
	private CGLabel labelSlot, labelAttribute;

	public PanelAttributeModifier(boolean isApplied)
	{
		this(isApplied, true);
	}

	public PanelAttributeModifier(boolean isApplied, boolean customObjects)
	{
		this.isApplied = isApplied;

		GridBagConstraints gbc = this.createGridBagLayout();
		gbc.gridwidth = 2;
		this.add((this.entryName = new CGEntry(new Text("attribute.modifier.name"), new Text("attribute.modifier.name"))).container, gbc);

		++gbc.gridy;
		gbc.gridwidth = 1;
		this.add(this.labelSlot = new CGLabel("attribute.modifier.slot").setHasColumn(true), gbc);
		++gbc.gridx;
		this.add(this.comboboxSlot = new OptionCombobox("attribute.modifier.slot", AttributeModifier.SLOTS), gbc);

		--gbc.gridx;
		++gbc.gridy;
		this.add(this.labelAttribute = new CGLabel("attribute.modifier.attribute").setHasColumn(true), gbc);
		++gbc.gridx;
		this.add(this.comboboxAttribute = new ObjectCombobox<Attribute>(ObjectRegistry.attributes.list()), gbc);

		--gbc.gridx;
		++gbc.gridy;
		this.add(new CGLabel("attribute.modifier.operation").setHasColumn(true), gbc);
		++gbc.gridx;
		this.add(this.comboboxOperation = new OptionCombobox("attribute.modifier.operation", "add", "multiply", "multiply_all"), gbc);

		--gbc.gridx;
		++gbc.gridy;
		++gbc.gridwidth;
		this.add((this.entryAmount = new CGEntry(new Text("attribute.modifier.amount"), "0", Text.NUMBER)).container, gbc);
		++gbc.gridy;
		this.add(
				(this.entryUUIDMost = new CGEntry(new Text("attribute.modifier.uuidmost"), Long.toString(ThreadLocalRandom.current().nextLong()), Text.INTEGER)).container,
				gbc);
		++gbc.gridy;
		this.add((this.entryUUIDLeast = new CGEntry(new Text("attribute.modifier.uuidleast"), Long.toString(ThreadLocalRandom.current().nextLong()),
				Text.INTEGER)).container, gbc);
		++gbc.gridy;
		this.add(new CGLabel("attribute.modifier.uuid.details"), gbc);
		++gbc.gridy;
		gbc.fill = GridBagConstraints.NONE;
		if (customObjects) this.add(new PanelCustomObject<AttributeModifier, AttributeModifier>(this, ObjectSaver.attributeModifiers), gbc);

		this.entryAmount.addNumberFilter();
		this.entryUUIDMost.addNumberFilter();
		this.entryUUIDLeast.addNumberFilter();

		if (this.isApplied)
		{
			this.comboboxAttribute.setVisible(false);
			this.comboboxSlot.setVisible(false);
			this.labelAttribute.setVisible(false);
			this.labelSlot.setVisible(false);
		}
	}

	@Override
	public AttributeModifier generate()
	{
		try
		{
			return new AttributeModifier(this.comboboxAttribute.getSelectedObject(), this.entryName.getText(), this.comboboxSlot.getValue(),
					(byte) this.comboboxOperation.getSelectedIndex(), Double.parseDouble(this.entryAmount.getText()), Long.parseLong(this.entryUUIDMost
							.getText()), Long.parseLong(this.entryUUIDLeast.getText()));
		} catch (Exception e)
		{
			CommandGenerator.report(new CommandGenerationException(new Text("Error creating Attribute Modifier.")));
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void setupFrom(AttributeModifier modifier)
	{
		if (!this.isApplied) this.comboboxAttribute.setSelected(modifier.attribute);
		this.comboboxOperation.setSelectedIndex(modifier.operation);
		if (!this.isApplied) this.comboboxSlot.setValue(modifier.slot);
		this.entryName.setText(modifier.name);
		this.entryAmount.setText(Utils.doubleToString(modifier.amount));
		this.entryUUIDMost.setText(Utils.doubleToString(modifier.UUIDMost));
		this.entryUUIDLeast.setText(Utils.doubleToString(modifier.UUIDLeast));
	}

}
