package fr.cubiccl.generator.gui.component.panel.gameobject;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;

import fr.cubiccl.generator.CommandGenerator;
import fr.cubiccl.generator.gameobject.*;
import fr.cubiccl.generator.gameobject.registries.ObjectSaver;
import fr.cubiccl.generator.gameobject.target.Target;
import fr.cubiccl.generator.gui.component.combobox.OptionCombobox;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.utils.ConfirmPanel;
import fr.cubiccl.generator.gui.component.panel.utils.PanelObjectList;
import fr.cubiccl.generator.utils.Replacement;
import fr.cubiccl.generator.utils.Text;

public class PanelCustomObjects extends ConfirmPanel
{
	private static final long serialVersionUID = 5654464660817660318L;

	private PanelObjectList<AppliedAttribute> attributes;
	private PanelObjectList<PlacedBlock> blocks;
	private OptionCombobox comboboxType;
	private PanelObjectList<Coordinates> coordinates;
	private PanelObjectList<Effect> effects;
	private PanelObjectList<Enchantment> enchantments;
	private PanelObjectList<LivingEntity> entities;
	private PanelObjectList<ItemStack> items;
	private PanelObjectList<JsonMessage> messages;
	private PanelObjectList<AttributeModifier> modifiers;
	private PanelObjectList<Target> targets;
	private PanelObjectList<TradeOffer> trades;

	public PanelCustomObjects()
	{
		super(new Text("menu.objects"), null, false);
		this.buttonCancel.setText(new Text("general.back"));
		this.setName(new Text("menu.objects"));

		String[] types = new String[ObjectSaver.savers.length];
		for (int i = 0; i < types.length; ++i)
			types[i] = ObjectSaver.savers[i].name.id.substring("objects.".length());

		CGPanel panel = new CGPanel();

		GridBagConstraints gbc = panel.createGridBagLayout();
		gbc.gridwidth = 3;
		gbc.fill = GridBagConstraints.BOTH;
		panel.add(this.comboboxType = new OptionCombobox("objects", types), gbc);
		++gbc.gridy;
		panel.add(this.modifiers = new PanelObjectList<AttributeModifier>(null, new Text("objects.title", new Replacement("<object>", new Text(
				"objects.modifier"))), AttributeModifier.class, "customObjects", false), gbc);
		panel.add(this.attributes = new PanelObjectList<AppliedAttribute>(null, new Text("objects.title", new Replacement("<object>", new Text(
				"objects.attribute"))), AppliedAttribute.class, "customObjects", false), gbc);
		panel.add(this.blocks = new PanelObjectList<PlacedBlock>(null, new Text("objects.title", new Replacement("<object>", new Text("objects.block"))),
				PlacedBlock.class, "customObjects", false), gbc);
		panel.add(this.coordinates = new PanelObjectList<Coordinates>(null, new Text("objects.title", new Replacement("<object>", new Text(
				"objects.coordinates"))), Coordinates.class, "customObjects", false), gbc);
		panel.add(this.effects = new PanelObjectList<Effect>(null, new Text("objects.title", new Replacement("<object>", new Text("objects.effect"))),
				Effect.class, "customObjects", false), gbc);
		panel.add(this.enchantments = new PanelObjectList<Enchantment>(null, new Text("objects.title", new Replacement("<object>", new Text(
				"objects.enchantment"))), Enchantment.class, "customObjects", false), gbc);
		panel.add(this.entities = new PanelObjectList<LivingEntity>(null, new Text("objects.title", new Replacement("<object>", new Text("objects.entity"))),
				LivingEntity.class), gbc);
		panel.add(this.items = new PanelObjectList<ItemStack>(null, new Text("objects.title", new Replacement("<object>", new Text("objects.item"))),
				ItemStack.class, "customObjects", false), gbc);
		panel.add(this.messages = new PanelObjectList<JsonMessage>(null, new Text("objects.title", new Replacement("<object>", new Text("objects.json"))),
				JsonMessage.class, "customObjects", false), gbc);
		panel.add(this.targets = new PanelObjectList<Target>(null, new Text("objects.title", new Replacement("<object>", new Text("objects.target"))),
				Target.class, "customObjects", false), gbc);
		panel.add(this.trades = new PanelObjectList<TradeOffer>(null, new Text("objects.title", new Replacement("<object>", new Text("objects.trade"))),
				TradeOffer.class, "customObjects", false), gbc);

		this.comboboxType.addActionListener(this);

		this.modifiers.setValues(ObjectSaver.attributeModifiers.list());
		this.attributes.setValues(ObjectSaver.attributes.list());
		this.blocks.setValues(ObjectSaver.blocks.list());
		this.coordinates.setValues(ObjectSaver.coordinates.list());
		this.effects.setValues(ObjectSaver.effects.list());
		this.enchantments.setValues(ObjectSaver.enchantments.list());
		this.entities.setValues(ObjectSaver.entities.list());
		this.items.setValues(ObjectSaver.items.list());
		this.messages.setValues(ObjectSaver.jsonMessages.list());
		this.targets.setValues(ObjectSaver.targets.list());
		this.trades.setValues(ObjectSaver.trades.list());

		this.modifiers.addListListener(ObjectSaver.attributeModifiers);
		this.attributes.addListListener(ObjectSaver.attributes);
		this.blocks.addListListener(ObjectSaver.blocks);
		this.coordinates.addListListener(ObjectSaver.coordinates);
		this.effects.addListListener(ObjectSaver.effects);
		this.enchantments.addListListener(ObjectSaver.enchantments);
		this.entities.addListListener(ObjectSaver.entities);
		this.items.addListListener(ObjectSaver.items);
		this.messages.addListListener(ObjectSaver.jsonMessages);
		this.targets.addListListener(ObjectSaver.targets);
		this.trades.addListListener(ObjectSaver.trades);

		this.onTypeSelection();
		this.setMainComponent(panel);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		super.actionPerformed(e);
		if (e.getSource() == this.buttonCancel) CommandGenerator.window.menubar.toggleObjects(true);
		if (e.getSource() == this.comboboxType) this.onTypeSelection();
	}

	private void onTypeSelection()
	{
		this.modifiers.setVisible(this.comboboxType.getValue().equals("modifier"));
		this.attributes.setVisible(this.comboboxType.getValue().equals("attribute"));
		this.blocks.setVisible(this.comboboxType.getValue().equals("block"));
		this.coordinates.setVisible(this.comboboxType.getValue().equals("coordinates"));
		this.effects.setVisible(this.comboboxType.getValue().equals("effect"));
		this.enchantments.setVisible(this.comboboxType.getValue().equals("enchantment"));
		this.entities.setVisible(this.comboboxType.getValue().equals("entity"));
		this.items.setVisible(this.comboboxType.getValue().equals("item"));
		this.messages.setVisible(this.comboboxType.getValue().equals("json"));
		this.targets.setVisible(this.comboboxType.getValue().equals("target"));
		this.trades.setVisible(this.comboboxType.getValue().equals("trade"));
	}

}
