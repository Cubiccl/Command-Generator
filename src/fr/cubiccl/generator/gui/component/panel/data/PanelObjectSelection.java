package fr.cubiccl.generator.gui.component.panel.data;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import fr.cubiccl.generator.CommandGenerator;
import fr.cubiccl.generator.gameobject.baseobjects.*;
import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.templatetags.TemplateBoolean;
import fr.cubiccl.generator.gameobject.templatetags.TemplateNumber;
import fr.cubiccl.generator.gameobject.templatetags.TemplateString;
import fr.cubiccl.generator.gameobject.templatetags.TemplateTag;
import fr.cubiccl.generator.gui.Dialogs;
import fr.cubiccl.generator.gui.component.button.CGButton;
import fr.cubiccl.generator.gui.component.combobox.CGComboBox;
import fr.cubiccl.generator.gui.component.combobox.ObjectCombobox;
import fr.cubiccl.generator.gui.component.combobox.OptionCombobox;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.utils.Lang;
import fr.cubiccl.generator.utils.Replacement;
import fr.cubiccl.generator.utils.Text;

public class PanelObjectSelection extends CGPanel implements ActionListener
{
	private static final int BLOCK = 0, ITEM = 1, ENTITY = 2, EFFECT = 3, ENCHANTMENT = 4, ACHIEVEMENT = 5, ATTRIBUTE = 6, PARTICLE = 7, SOUND = 8,
			CONTAINER = 9, LISTS = 10, BLOCKTAGS = 11, ITEMTAGS = 12, ENTITYTAGS = 13;
	public static final int HEIGHT = 100;
	private static final String[] OBJECTS =
	{ "block", "item", "entity", "effect", "enchantment", "achievement", "attribute", "particle", "sound", "container", "list", "blocktag", "itemtag",
			"entitytag" };
	private static final long serialVersionUID = -4153348926393029726L;

	private CGButton buttonAdd, buttonEdit, buttonRemove;
	private ObjectCombobox<Achievement> comboboxAchievement;
	private ObjectCombobox<Attribute> comboboxAttribute;
	private ObjectCombobox<Block> comboboxBlock;
	private ObjectCombobox<TemplateTag> comboboxBlocktag;
	private ObjectCombobox<Container> comboboxContainer;
	private ObjectCombobox<EffectType> comboboxEffect;
	private ObjectCombobox<EnchantmentType> comboboxEnchantment;
	private ObjectCombobox<Entity> comboboxEntity;
	private ObjectCombobox<TemplateTag> comboboxEntitytag;
	private ObjectCombobox<Item> comboboxItem;
	private ObjectCombobox<TemplateTag> comboboxItemtag;
	private CGComboBox comboboxList;
	private ObjectCombobox<Particle> comboboxParticle;
	private ObjectCombobox<Sound> comboboxSound;
	private OptionCombobox comboboxType;
	private int mode = BLOCK;

	public PanelObjectSelection()
	{
		super();

		CGPanel panelCombobox = new CGPanel();
		GridBagConstraints gbc = panelCombobox.createGridBagLayout();
		panelCombobox.add(this.comboboxType = new OptionCombobox("objects", OBJECTS), gbc);
		++gbc.gridx;
		panelCombobox.add((this.comboboxBlock = new ObjectCombobox<Block>()).container, gbc);
		panelCombobox.add((this.comboboxItem = new ObjectCombobox<Item>()).container, gbc);
		panelCombobox.add((this.comboboxEntity = new ObjectCombobox<Entity>()).container, gbc);
		panelCombobox.add((this.comboboxEffect = new ObjectCombobox<EffectType>()).container, gbc);
		panelCombobox.add((this.comboboxEnchantment = new ObjectCombobox<EnchantmentType>()).container, gbc);
		panelCombobox.add((this.comboboxAchievement = new ObjectCombobox<Achievement>()).container, gbc);
		panelCombobox.add((this.comboboxAttribute = new ObjectCombobox<Attribute>()).container, gbc);
		panelCombobox.add((this.comboboxParticle = new ObjectCombobox<Particle>()).container, gbc);
		panelCombobox.add((this.comboboxSound = new ObjectCombobox<Sound>()).container, gbc);
		panelCombobox.add((this.comboboxContainer = new ObjectCombobox<Container>()).container, gbc);
		panelCombobox.add((this.comboboxBlocktag = new ObjectCombobox<TemplateTag>()).container, gbc);
		panelCombobox.add((this.comboboxItemtag = new ObjectCombobox<TemplateTag>()).container, gbc);
		panelCombobox.add((this.comboboxEntitytag = new ObjectCombobox<TemplateTag>()).container, gbc);
		panelCombobox.add(this.comboboxList = new CGComboBox(), gbc);

		CGPanel panelButtons = new CGPanel();
		gbc = panelCombobox.createGridBagLayout();
		panelButtons.add(this.buttonAdd = new CGButton("general.add"), gbc);
		++gbc.gridx;
		panelButtons.add(this.buttonEdit = new CGButton("general.edit"), gbc);
		++gbc.gridx;
		panelButtons.add(this.buttonRemove = new CGButton("general.remove"), gbc);

		gbc = this.createGridBagLayout();
		this.add(panelCombobox, gbc);
		++gbc.gridy;
		this.add(panelButtons, gbc);

		this.buttonAdd.addActionListener(this);
		this.buttonEdit.addActionListener(this);
		this.buttonRemove.addActionListener(this);
		this.comboboxType.addActionListener(this);
		this.comboboxBlock.addActionListener(this);
		this.comboboxItem.addActionListener(this);
		this.comboboxEntity.addActionListener(this);
		this.comboboxEffect.addActionListener(this);
		this.comboboxEnchantment.addActionListener(this);
		this.comboboxAchievement.addActionListener(this);
		this.comboboxAttribute.addActionListener(this);
		this.comboboxParticle.addActionListener(this);
		this.comboboxSound.addActionListener(this);
		this.comboboxContainer.addActionListener(this);
		this.comboboxBlocktag.addActionListener(this);
		this.comboboxItemtag.addActionListener(this);
		this.comboboxEntitytag.addActionListener(this);
		this.comboboxList.addActionListener(this);

		this.addComponentListener(new ComponentListener()
		{

			@Override
			public void componentHidden(ComponentEvent e)
			{}

			@Override
			public void componentMoved(ComponentEvent e)
			{}

			@Override
			public void componentResized(ComponentEvent e)
			{
				revalidate();
			}

			@Override
			public void componentShown(ComponentEvent e)
			{
				revalidate();
			}
		});

		this.updateDisplay();
		this.updateData();
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == this.comboboxType)
		{
			this.mode = this.comboboxType.getSelectedIndex();
			this.updateDisplay();
		} else if (e.getSource() == this.buttonAdd) this.add();
		else if (e.getSource() == this.buttonEdit) this.edit();
		else if (e.getSource() == this.buttonRemove)
		{
			if (this.mode == LISTS)
			{
				if (!Dialogs.showConfirmMessage(new Text("general.delete.confirm", new Replacement("<object>", this.comboboxList.getValue())).toString(),
						Lang.translate("general.yes"), Lang.translate("general.no"))) return;
				this.delete();
			} else
			{
				if (!Dialogs.showConfirmMessage(new Text("general.delete.confirm", new Replacement("<object>", this.selectedObject().name())).toString(),
						Lang.translate("general.yes"), Lang.translate("general.no"))) return;
				this.delete();
			}
		}
	}

	private void add()
	{
		String id = Dialogs.showInputDialog("Object id ?");
		if (id == null) return;

		int idNum = -1;
		if (this.mode == BLOCK || this.mode == ITEM || this.mode == EFFECT || this.mode == ENCHANTMENT) try
		{
			idNum = Integer.parseInt(Dialogs.showInputDialog("Object num id ?"));
		} catch (NumberFormatException e)
		{
			return;
		}

		CGPanel panelToShow = null;
		if (this.mode == BLOCK) panelToShow = new PanelBlockEdition(new Block(idNum, id));
		if (this.mode == ITEM) panelToShow = new PanelItemEdition(new Item(idNum, id));
		if (this.mode == ENTITY) new Entity(id).register();
		if (this.mode == EFFECT) new EffectType(idNum, id).register();
		;
		if (this.mode == ENCHANTMENT) panelToShow = new PanelEnchantmentEdition(new EnchantmentType(idNum, id).register());
		if (this.mode == ACHIEVEMENT)
		{
			String item = Dialogs.showInputDialog("Item id ?");
			if (item == null) return;
			Item i = ObjectRegistry.items.find(item);
			if (i == null) Dialogs.showMessage("Item " + item + " could not be found.");
			panelToShow = new PanelAchievementEdition(new Achievement(id, i).register());
		}
		if (this.mode == ATTRIBUTE) new Attribute(id).register();
		if (this.mode == PARTICLE) new Particle(id).register();
		if (this.mode == SOUND) new Sound(id).register();
		if (this.mode == LISTS)
		{
			ObjectRegistry.createList(id);
			panelToShow = new PanelListEdition(id);
		}
		if (this.mode >= BLOCKTAGS)
		{
			byte applicationType = this.mode == BLOCKTAGS ? Tag.BLOCK : this.mode == ITEMTAGS ? Tag.ITEM : Tag.ENTITY;
			String type = Dialogs
					.showInputDialog("Tag type ? STRING = 0, BYTE = 1, SHORT = 2, INT = 3, LONG = 4, FLOAT = 5, DOUBLE = 6, LIST = 7, COMPOUND = 8, BOOLEAN = 9");
			if (type == null) return;
			try
			{
				Byte t = Byte.parseByte(type);
				if (t == Tag.STRING) new TemplateString(id, applicationType);
				else if (t == Tag.BOOLEAN) new TemplateBoolean(id, applicationType);
				else if (t <= Tag.DOUBLE) new TemplateNumber(id, applicationType, t);
			} catch (Exception e)
			{
				//ObjectCreator.createCustomTag(id, applicationType, new String[0], type, null);
			}
		}

		if (panelToShow != null)
		{
			CommandGenerator.stateManager.clear();
			CommandGenerator.stateManager.setState(panelToShow, null);
		}
		this.updateData();
	}

	@SuppressWarnings("unchecked")
	private void delete()
	{
		if (this.mode == LISTS) ObjectRegistry.removeList(this.comboboxList.getValue());
		else this.selectedRegistry().remove(this.selectedObject());
		this.updateData();
	}

	private void edit()
	{
		CGPanel panel = null;
		if (this.mode == BLOCK) panel = new PanelBlockEdition((Block) this.selectedObject());
		if (this.mode == ITEM) panel = new PanelItemEdition((Item) this.selectedObject());
		if (this.mode == ENCHANTMENT) panel = new PanelEnchantmentEdition((EnchantmentType) this.selectedObject());
		if (this.mode == ACHIEVEMENT) panel = new PanelAchievementEdition((Achievement) this.selectedObject());
		if (this.mode == LISTS) panel = new PanelListEdition((String) this.comboboxList.getSelectedItem());
		CommandGenerator.stateManager.clear();
		CommandGenerator.stateManager.setState(panel, null);
	}

	private boolean hasEditing()
	{
		return this.mode != ENTITY && this.mode != EFFECT && this.mode != ATTRIBUTE && this.mode != PARTICLE && this.mode != SOUND && this.mode != BLOCKTAGS
				&& this.mode != ITEMTAGS && this.mode != ENTITYTAGS;
	}

	@SuppressWarnings("rawtypes")
	private ObjectCombobox selectedCombobox()
	{
		if (this.mode == ITEM) return this.comboboxItem;
		if (this.mode == ENTITY) return this.comboboxEntity;
		if (this.mode == EFFECT) return this.comboboxEffect;
		if (this.mode == ENCHANTMENT) return this.comboboxEnchantment;
		if (this.mode == ACHIEVEMENT) return this.comboboxAchievement;
		if (this.mode == ATTRIBUTE) return this.comboboxAttribute;
		if (this.mode == PARTICLE) return this.comboboxParticle;
		if (this.mode == SOUND) return this.comboboxSound;
		if (this.mode == BLOCKTAGS) return this.comboboxBlocktag;
		if (this.mode == ITEMTAGS) return this.comboboxItemtag;
		if (this.mode == ENTITYTAGS) return this.comboboxEntitytag;
		return this.comboboxBlock;
	}

	@SuppressWarnings("rawtypes")
	private BaseObject selectedObject()
	{
		return this.selectedCombobox().getSelectedObject();
	}

	@SuppressWarnings("rawtypes")
	private ObjectRegistry selectedRegistry()
	{
		if (this.mode == ITEM) return ObjectRegistry.items;
		if (this.mode == ENTITY) return ObjectRegistry.entities;
		if (this.mode == EFFECT) return ObjectRegistry.effects;
		if (this.mode == ENCHANTMENT) return ObjectRegistry.enchantments;
		if (this.mode == ACHIEVEMENT) return ObjectRegistry.achievements;
		if (this.mode == ATTRIBUTE) return ObjectRegistry.attributes;
		if (this.mode == PARTICLE) return ObjectRegistry.particles;
		if (this.mode == SOUND) return ObjectRegistry.sounds;
		if (this.mode == BLOCKTAGS) return ObjectRegistry.blockTags;
		if (this.mode == ITEMTAGS) return ObjectRegistry.itemTags;
		if (this.mode == ENTITYTAGS) return ObjectRegistry.entityTags;
		return ObjectRegistry.blocks;
	}

	public void updateData()
	{
		this.comboboxBlock.setValues(ObjectRegistry.blocks.list());
		this.comboboxItem.setValues(ObjectRegistry.items.list());
		this.comboboxEntity.setValues(ObjectRegistry.entities.list(false));
		this.comboboxEffect.setValues(ObjectRegistry.effects.list());
		this.comboboxEnchantment.setValues(ObjectRegistry.enchantments.list());
		this.comboboxAchievement.setValues(ObjectRegistry.achievements.list());
		this.comboboxAttribute.setValues(ObjectRegistry.attributes.list());
		this.comboboxParticle.setValues(ObjectRegistry.particles.list());
		this.comboboxSound.setValues(ObjectRegistry.sounds.list());
		this.comboboxContainer.setValues(ObjectRegistry.containers.list());
		this.comboboxBlocktag.setValues(ObjectRegistry.blockTags.list());
		this.comboboxItemtag.setValues(ObjectRegistry.itemTags.list());
		this.comboboxEntitytag.setValues(ObjectRegistry.entityTags.list());
		this.comboboxList.setValues(ObjectRegistry.getLists());
	}

	private void updateDisplay()
	{
		this.comboboxBlock.container.setVisible(this.mode == BLOCK);
		this.comboboxItem.container.setVisible(this.mode == ITEM);
		this.comboboxEntity.container.setVisible(this.mode == ENTITY);
		this.comboboxEffect.container.setVisible(this.mode == EFFECT);
		this.comboboxEnchantment.container.setVisible(this.mode == ENCHANTMENT);
		this.comboboxAchievement.container.setVisible(this.mode == ACHIEVEMENT);
		this.comboboxAttribute.container.setVisible(this.mode == ATTRIBUTE);
		this.comboboxParticle.container.setVisible(this.mode == PARTICLE);
		this.comboboxSound.container.setVisible(this.mode == SOUND);
		this.comboboxContainer.container.setVisible(this.mode == CONTAINER);
		this.comboboxBlocktag.container.setVisible(this.mode == BLOCKTAGS);
		this.comboboxItemtag.container.setVisible(this.mode == ITEMTAGS);
		this.comboboxEntitytag.container.setVisible(this.mode == ENTITYTAGS);
		this.comboboxList.setVisible(this.mode == LISTS);

		this.buttonEdit.setEnabled(this.hasEditing());
	}
}
