package fr.cubiccl.generator.gui.component.panel.gameobject;

import java.awt.GridBagConstraints;
import java.util.ArrayList;

import fr.cubiccl.generator.gameobject.AppliedAttribute;
import fr.cubiccl.generator.gameobject.AppliedAttribute.AttributeModifierList;
import fr.cubiccl.generator.gameobject.AttributeModifier;
import fr.cubiccl.generator.gameobject.baseobjects.Attribute;
import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.gameobject.registries.ObjectSaver;
import fr.cubiccl.generator.gui.component.combobox.ObjectCombobox;
import fr.cubiccl.generator.gui.component.interfaces.ICustomObject;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.utils.PanelCustomObject;
import fr.cubiccl.generator.gui.component.panel.utils.PanelObjectList;
import fr.cubiccl.generator.gui.component.textfield.CGEntry;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;
import fr.cubiccl.generator.utils.Utils;

public class PanelAttribute extends CGPanel implements ICustomObject<AppliedAttribute>
{
	private static final long serialVersionUID = -856483099169175263L;

	private ObjectCombobox<Attribute> comboboxAttribute;
	private CGEntry entryBase;
	private PanelObjectList panelModifiers;

	public PanelAttribute()
	{
		this(true);
	}

	public PanelAttribute(boolean customObjects)
	{
		super();
		GridBagConstraints gbc = this.createGridBagLayout();
		this.add((this.comboboxAttribute = new ObjectCombobox<Attribute>(ObjectRegistry.attributes.list())).container, gbc);
		++gbc.gridy;
		this.add((this.entryBase = new CGEntry(new Text("attribute.base"), "1", Text.NUMBER)).container, gbc);
		++gbc.gridy;
		this.add(this.panelModifiers = new PanelObjectList("attribute.modifiers", new AttributeModifierList(true)), gbc);
		++gbc.gridy;
		gbc.fill = GridBagConstraints.NONE;
		if (customObjects) this.add(new PanelCustomObject<AppliedAttribute>(this, ObjectSaver.attributes), gbc);

		this.entryBase.addNumberFilter();
	}

	@Override
	public AppliedAttribute generate() throws CommandGenerationException
	{
		ArrayList<AttributeModifier> modifiers = ((AttributeModifierList) this.panelModifiers.getObjectList()).modifiers;
		this.entryBase.checkValue(CGEntry.NUMBER);
		return new AppliedAttribute(this.comboboxAttribute.getSelectedObject(), Double.parseDouble(this.entryBase.getText()),
				modifiers.toArray(new AttributeModifier[modifiers.size()]));
	}

	@Override
	public void setupFrom(AppliedAttribute attribute)
	{
		this.comboboxAttribute.setSelected(attribute.attribute);
		this.entryBase.setText(Utils.doubleToString(attribute.base));
		((AttributeModifierList) this.panelModifiers.getObjectList()).modifiers.clear();
		for (AttributeModifier m : attribute.modifiers)
			((AttributeModifierList) this.panelModifiers.getObjectList()).modifiers.add(m);
		this.panelModifiers.updateList();
	}

}
