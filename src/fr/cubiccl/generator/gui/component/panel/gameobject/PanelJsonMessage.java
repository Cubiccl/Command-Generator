package fr.cubiccl.generator.gui.component.panel.gameobject;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import fr.cubiccl.generator.gameobject.ItemStack;
import fr.cubiccl.generator.gameobject.JsonMessage;
import fr.cubiccl.generator.gameobject.LivingEntity;
import fr.cubiccl.generator.gameobject.registries.ObjectSaver;
import fr.cubiccl.generator.gameobject.tags.NBTParser;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.target.Target;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gui.component.button.CGCheckBox;
import fr.cubiccl.generator.gui.component.combobox.OptionCombobox;
import fr.cubiccl.generator.gui.component.interfaces.ICustomObject;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.label.HelpLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.utils.PanelCustomObject;
import fr.cubiccl.generator.gui.component.textfield.CGEntry;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;
import fr.cubiccl.generator.utils.Utils;

public class PanelJsonMessage extends CGPanel implements ActionListener, ICustomObject<JsonMessage>
{
	class PanelClickEvent extends CGPanel implements ActionListener
	{
		private static final long serialVersionUID = -3177207292056026877L;

		private OptionCombobox comboboxMode;
		private CGEntry entryValue;

		public PanelClickEvent()
		{
			super("json.click");

			GridBagConstraints gbc = this.createGridBagLayout();
			this.add(this.comboboxMode = new OptionCombobox("json.click.mode", "open_url", "run_command", "change_page"), gbc);
			++gbc.gridy;
			this.add((this.entryValue = new CGEntry(new Text("json.url"), Text.VALUE)).container, gbc);

			this.comboboxMode.addActionListener(this);
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{
			switch (this.comboboxMode.getValue())
			{
				case "run_command":
					this.entryValue.label.setTextID(new Text("json.command"));
					break;

				case "change_page":
					this.entryValue.label.setTextID(new Text("json.page"));
					break;

				default:
					this.entryValue.label.setTextID(new Text("json.url"));
					break;
			}
		}

	}

	class PanelHoverEvent extends CGPanel implements ActionListener
	{
		private static final long serialVersionUID = 367164758149085273L;

		private OptionCombobox comboboxMode;
		private PanelCriteria panelAchievement;
		private PanelEntity panelEntity;
		private PanelItem panelItem;
		private PanelJsonMessage panelJson;

		public PanelHoverEvent()
		{
			super("json.hover");
			GridBagConstraints gbc = this.createGridBagLayout();
			this.add(this.comboboxMode = new OptionCombobox("json.hover.mode", "show_text", "show_item", "show_achievement", "show_entity"), gbc);
			++gbc.gridy;
			this.add(this.panelJson = new PanelJsonMessage(false), gbc);

			this.comboboxMode.addActionListener(this);
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{
			String mode = this.comboboxMode.getValue();
			if (mode.equals("show_achievement") && this.panelAchievement == null) this.add(this.panelAchievement = new PanelCriteria("json.achievement"),
					this.gbc);
			if (mode.equals("show_entity") && this.panelEntity == null) this.add(this.panelEntity = new PanelEntity("json.entity"), this.gbc);
			if (mode.equals("show_item") && this.panelItem == null)
			{
				this.add(this.panelItem = new PanelItem("json.item"), this.gbc);
				this.panelItem.setEnabledContent(true, false);
			}

			if (this.panelAchievement != null) this.panelAchievement.setVisible(mode.equals("show_achievement"));
			if (this.panelEntity != null) this.panelEntity.setVisible(mode.equals("show_entity"));
			if (this.panelItem != null) this.panelItem.setVisible(mode.equals("show_item"));
			this.panelJson.setVisible(mode.equals("show_text"));
		}

		public Object getValue() throws CommandGenerationException
		{
			switch (this.comboboxMode.getValue())
			{
				case "show_achievement":
					return this.panelAchievement.getCriteria();

				case "show_entity":
					return this.panelEntity.generate().toNBT(Tags.ENTITY).valueForCommand();

				case "show_item":
					return this.panelItem.generate().toNBT(Tags.ITEM).valueForCommand();

				default:
					return this.panelJson.generate();
			}
		}
	}

	private static final long serialVersionUID = -6195765844988257240L;

	private CGCheckBox checkboxBold, checkboxUnderlined, checkboxItalic, checkboxStrikethrough, checkboxObfuscated, checkboxClickEvent, checkboxHoverEvent;
	private OptionCombobox comboboxMode, comboboxColor;
	private CGEntry entryMain, entryInsertion;
	private boolean hasEvents;
	private PanelClickEvent panelClickEvent;
	private PanelHoverEvent panelHoverEvent;
	private PanelTarget panelTarget;

	public PanelJsonMessage()
	{
		this(true);
	}

	public PanelJsonMessage(boolean hasEvents)
	{
		this(hasEvents, true);
	}

	public PanelJsonMessage(boolean hasEvents, boolean customObjects)
	{
		super();
		this.hasEvents = hasEvents;
		GridBagConstraints gbc = this.createGridBagLayout();

		gbc.gridwidth = 4;
		this.add(this.comboboxMode = new OptionCombobox("json.mode", "text", "translate", "score", "selector"), gbc);
		++gbc.gridy;
		this.add((this.entryMain = new CGEntry("json.text")).container, gbc);
		++gbc.gridy;
		this.add(this.panelTarget = new PanelTarget(PanelTarget.ALL_ENTITIES), gbc);
		++gbc.gridy;
		this.add((this.entryInsertion = new CGEntry(new Text("json.insertion"), new Text("json.insertion"))).container, gbc);
		++gbc.gridy;
		gbc.gridwidth = 1;
		this.add(new CGLabel("json.color").setHasColumn(true), gbc);
		++gbc.gridx;
		this.add(this.comboboxColor = new OptionCombobox("color", Utils.COLORS), gbc);
		++gbc.gridx;
		++gbc.gridwidth;
		this.add(this.checkboxBold = new CGCheckBox("json.bold"), gbc);
		gbc.gridx = 0;
		++gbc.gridy;
		this.add(this.checkboxUnderlined = new CGCheckBox("json.underlined"), gbc);
		gbc.gridx += 2;
		this.add(this.checkboxItalic = new CGCheckBox("json.italic"), gbc);
		gbc.gridx = 0;
		++gbc.gridy;
		this.add(this.checkboxStrikethrough = new CGCheckBox("json.strikethrough"), gbc);
		gbc.gridx += 2;
		this.add(this.checkboxObfuscated = new CGCheckBox("json.obfuscated"), gbc);
		gbc.gridx = 0;
		++gbc.gridy;
		this.add(this.checkboxClickEvent = new CGCheckBox("json.click"), gbc);
		gbc.gridx += 2;
		this.add(this.checkboxHoverEvent = new CGCheckBox("json.hover"), gbc);
		gbc.gridx = 0;
		++gbc.gridy;
		gbc.gridwidth = 4;
		if (this.hasEvents)
		{
			this.add(this.panelClickEvent = new PanelClickEvent(), gbc);
			++gbc.gridy;
			this.add(this.panelHoverEvent = new PanelHoverEvent(), gbc);
			++gbc.gridy;
		}
		gbc.fill = GridBagConstraints.NONE;
		if (customObjects) this.add(new PanelCustomObject<JsonMessage, JsonMessage>(this, ObjectSaver.jsonMessages), gbc);

		this.comboboxMode.addActionListener(this);
		this.checkboxClickEvent.addActionListener(this);
		this.checkboxHoverEvent.addActionListener(this);
		if (this.hasEvents)
		{
			this.panelClickEvent.setVisible(false);
			this.panelHoverEvent.setVisible(false);
		} else
		{
			this.checkboxClickEvent.setVisible(false);
			this.checkboxHoverEvent.setVisible(false);
		}
		this.panelTarget.setVisible(false);
		this.entryInsertion.addHelpLabel(new HelpLabel("json.insertion.help"));

	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == this.comboboxMode)
		{
			String mode = this.comboboxMode.getValue();
			this.entryMain.container.setVisible(!mode.equals("selector"));
			this.panelTarget.setVisible(mode.equals("score") || mode.equals("selector"));

			switch (mode)
			{
				case "text":
					this.entryMain.label.setTextID(new Text("json.text"));
					break;

				case "translate":
					this.entryMain.label.setTextID(new Text("json.translation"));
					break;

				case "score":
					this.entryMain.label.setTextID(Text.OBJECTIVE);
					break;

				default:
					break;
			}
		}
		if (e.getSource() == this.checkboxClickEvent) this.panelClickEvent.setVisible(this.checkboxClickEvent.isSelected());
		if (e.getSource() == this.checkboxHoverEvent) this.panelHoverEvent.setVisible(this.checkboxHoverEvent.isSelected());
	}

	@Override
	public JsonMessage generate() throws CommandGenerationException
	{
		String text = this.entryMain.getText();
		if (this.comboboxMode.getValue().equals("selector")) text = this.panelTarget.generate().toCommand();
		else if (!this.comboboxMode.getValue().equals("text")) this.entryMain.checkValue(CGEntry.STRING);

		JsonMessage message = new JsonMessage((byte) this.comboboxMode.getSelectedIndex(), text, this.comboboxColor.getSelectedIndex());
		if (this.comboboxMode.getValue().equals("score")) message.target = this.panelTarget.generate().toCommand();

		message.bold = this.checkboxBold.isSelected();
		message.underlined = this.checkboxUnderlined.isSelected();
		message.italic = this.checkboxItalic.isSelected();
		message.strikethrough = this.checkboxStrikethrough.isSelected();
		message.obfuscated = this.checkboxObfuscated.isSelected();
		if (!this.entryInsertion.getText().equals("")) message.insertion = this.entryInsertion.getText();

		if (this.hasEvents && this.checkboxClickEvent.isSelected()) message.setClick(this.panelClickEvent.comboboxMode.getValue(),
				this.panelClickEvent.entryValue.getText());
		if (this.hasEvents && this.checkboxHoverEvent.isSelected()) message.setHover(this.panelHoverEvent.comboboxMode.getValue(),
				this.panelHoverEvent.getValue());

		return message;
	}

	@Override
	public void setupFrom(JsonMessage message)
	{
		this.comboboxMode.setSelectedIndex(message.mode);
		if (message.mode != JsonMessage.SELECTOR) this.entryMain.setText(message.text);
		if (message.target != null) this.panelTarget.setupFrom(new Target().fromString(message.target));
		if (message.mode == JsonMessage.SELECTOR) this.panelTarget.setupFrom(new Target().fromString(message.text));

		this.comboboxColor.setSelectedIndex(message.color);
		this.checkboxBold.setSelected(message.bold);
		this.checkboxUnderlined.setSelected(message.underlined);
		this.checkboxItalic.setSelected(message.italic);
		this.checkboxStrikethrough.setSelected(message.strikethrough);
		this.checkboxObfuscated.setSelected(message.obfuscated);
		this.entryInsertion.setText(message.insertion);

		if (message.clickAction != null && this.hasEvents)
		{
			this.panelClickEvent.comboboxMode.setValue(message.clickAction);
			this.panelClickEvent.entryValue.setText(message.clickValue);
			this.checkboxClickEvent.setSelected(true);
			this.panelClickEvent.setVisible(true);
		}
		if (message.hoverAction != null && this.hasEvents)
		{
			this.panelHoverEvent.comboboxMode.setValue(message.hoverAction);
			switch (this.panelHoverEvent.comboboxMode.getValue())
			{
				case "show_achievement":
					this.panelHoverEvent.panelAchievement.setupFrom((String) message.hoverValue);
					break;

				case "show_entity":
					this.panelHoverEvent.panelEntity.setupFrom(new LivingEntity().fromNBT((TagCompound) NBTParser.parse((String) message.hoverValue, true,
							false)));
					break;

				case "show_item":
					this.panelHoverEvent.panelItem.setupFrom(new ItemStack().fromNBT((TagCompound) NBTParser.parse((String) message.hoverValue, true, false)));
					break;

				case "show_text":
					this.panelHoverEvent.panelJson.setupFrom((JsonMessage) message.hoverValue);
					break;

				default:
					break;
			}
			this.checkboxHoverEvent.setSelected(true);
			this.panelHoverEvent.setVisible(true);
		}
	}

}
