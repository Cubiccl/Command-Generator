package fr.cubiccl.generator.gui.component.panel.loottable;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ButtonGroup;

import fr.cubiccl.generator.gameobject.loottable.LootTableCondition;
import fr.cubiccl.generator.gameobject.loottable.LootTableCondition.Condition;
import fr.cubiccl.generator.gameobject.tags.*;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gui.component.button.CGRadioButton;
import fr.cubiccl.generator.gui.component.combobox.CGComboBox;
import fr.cubiccl.generator.gui.component.combobox.OptionCombobox;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.textfield.CGEntry;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;

public class PanelCondition extends CGPanel implements ActionListener
{
	private static final long serialVersionUID = 1808992472445548208L;

	private CGRadioButton buttonScoreExact, buttonScoreRange;
	private CGComboBox comboboxCondition;
	private OptionCombobox comboboxEntity;
	private CGEntry entry1, entry2;

	public PanelCondition()
	{
		String[] values = new String[Condition.values().length];
		for (int i = 0; i < values.length; ++i)
			values[i] = Condition.values()[i].translate().toString();

		GridBagConstraints gbc = this.createGridBagLayout();
		gbc.gridwidth = 2;
		this.add(this.comboboxCondition = new CGComboBox(values), gbc);
		++gbc.gridy;
		this.add(this.comboboxEntity = new OptionCombobox("lt_condition.entity", "this", "killer", "killer_player"), gbc);
		++gbc.gridy;
		--gbc.gridwidth;
		this.add(this.buttonScoreExact = new CGRadioButton((Text) null), gbc);
		++gbc.gridx;
		this.add(this.buttonScoreRange = new CGRadioButton((Text) null), gbc);

		--gbc.gridx;
		++gbc.gridy;
		++gbc.gridwidth;
		this.add((this.entry1 = new CGEntry(null)).container, gbc);
		++gbc.gridy;
		this.add((this.entry2 = new CGEntry(null)).container, gbc);

		ButtonGroup group = new ButtonGroup();
		group.add(this.buttonScoreExact);
		group.add(this.buttonScoreRange);
		this.buttonScoreExact.setSelected(true);

		this.entry1.addNumberFilter();
		this.entry2.addNumberFilter();

		this.comboboxCondition.addActionListener(this);
		this.buttonScoreExact.addActionListener(this);
		this.buttonScoreRange.addActionListener(this);
		this.updateDisplay();
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		this.updateDisplay();
	}

	public LootTableCondition generate() throws CommandGenerationException
	{
		Condition condition = Condition.values()[this.comboboxCondition.getSelectedIndex()];
		ArrayList<Tag> tags = new ArrayList<Tag>();

		if (condition == Condition.ENTITY_PROPERTIES || condition == Condition.ENTITY_SCORES) tags.add(new TagString(Tags.LT_CONDITION_ENTITY,
				this.comboboxEntity.getValue()));
		if (condition == Condition.ENTITY_PROPERTIES) tags.add(new TagCompound(Tags.LT_CONDITION_PROPERTIES, new TagBoolean(Tags.LT_CONDITION_ENTITY_ONFIRE,
				this.buttonScoreExact.isSelected())));
		else if (condition == Condition.ENTITY_SCORES)
		{} else if (condition == Condition.KILLED_BY_PLAYER) tags.add(new TagBoolean(Tags.LT_CONDITION_KILLED, this.buttonScoreExact.isSelected()));
		else if (condition == Condition.RANDOM_CHANCE || condition == Condition.RANDOM_CHANCE_WITH_LOOTING)
		{
			this.entry1.checkValue(CGEntry.FLOAT);
			tags.add(new TagBigNumber(Tags.LT_CONDITION_CHANCE, Float.parseFloat(this.entry1.getText())));
			if (condition == Condition.RANDOM_CHANCE_WITH_LOOTING)
			{
				this.entry2.checkValue(CGEntry.FLOAT);
				tags.add(new TagBigNumber(Tags.LT_CONDITION_LOOTING, Float.parseFloat(this.entry2.getText())));
			}
		}

		return new LootTableCondition(condition, tags.toArray(new Tag[tags.size()]));
	}

	public void setupFrom(LootTableCondition condition)
	{
		this.comboboxCondition.setSelectedItem(condition.condition.translate().toString());
		this.updateDisplay();

		TagCompound t = new TagCompound(Tags.DEFAULT_COMPOUND, condition.tags);
		Condition c = condition.condition;

		if ((c == Condition.ENTITY_PROPERTIES || c == Condition.ENTITY_SCORES) && t.hasTag(Tags.LT_CONDITION_ENTITY)) this.comboboxEntity
				.setValue(((TagString) t.getTag(Tags.LT_CONDITION_ENTITY)).value());
		if (c == Condition.ENTITY_PROPERTIES && t.hasTag(Tags.LT_CONDITION_PROPERTIES))
		{
			TagCompound tag = (TagCompound) t.getTag(Tags.LT_CONDITION_PROPERTIES);
			if (tag.hasTag(Tags.LT_CONDITION_ENTITY_ONFIRE))
			{
				this.buttonScoreExact.setSelected(((TagBoolean) tag.getTag(Tags.LT_CONDITION_ENTITY_ONFIRE)).value());
				this.buttonScoreRange.setSelected(!((TagBoolean) tag.getTag(Tags.LT_CONDITION_ENTITY_ONFIRE)).value());
			}
		} else if (c == Condition.ENTITY_SCORES && t.hasTag(Tags.LT_CONDITION_SCORES))
		{} else if (c == Condition.KILLED_BY_PLAYER && t.hasTag(Tags.LT_CONDITION_KILLED))
		{
			this.buttonScoreExact.setSelected(((TagBoolean) t.getTag(Tags.LT_CONDITION_KILLED)).value());
			this.buttonScoreRange.setSelected(!((TagBoolean) t.getTag(Tags.LT_CONDITION_KILLED)).value());
		} else if (c == Condition.RANDOM_CHANCE || c == Condition.RANDOM_CHANCE_WITH_LOOTING)
		{
			if (t.hasTag(Tags.LT_CONDITION_CHANCE)) this.entry1.setText(Double.toString(((TagBigNumber) t.getTag(Tags.LT_CONDITION_CHANCE)).value()));
			if (c == Condition.RANDOM_CHANCE_WITH_LOOTING && t.hasTag(Tags.LT_CONDITION_LOOTING)) this.entry2.setText(Double.toString(((TagBigNumber) t
					.getTag(Tags.LT_CONDITION_LOOTING)).value()));
		}
	}

	private void updateDisplay()
	{
		Condition condition = Condition.values()[this.comboboxCondition.getSelectedIndex()];

		this.comboboxEntity.setVisible(condition == Condition.ENTITY_SCORES || condition == Condition.ENTITY_PROPERTIES);
		this.buttonScoreExact.setVisible(this.comboboxEntity.isVisible() || condition == Condition.KILLED_BY_PLAYER);
		this.buttonScoreRange.setVisible(this.buttonScoreExact.isVisible());

		this.entry1.container.setVisible(condition == Condition.RANDOM_CHANCE || condition == Condition.RANDOM_CHANCE_WITH_LOOTING);
		this.entry2.container.setVisible(condition == Condition.RANDOM_CHANCE_WITH_LOOTING);

		this.buttonScoreExact.setText(condition == Condition.ENTITY_PROPERTIES ? new Text("lt_condition.entity_properties.on_fire") : new Text(
				"lt_condition.killed_by_player.player"));
		this.buttonScoreRange.setText(condition == Condition.ENTITY_PROPERTIES ? new Text("lt_condition.entity_properties.not_on_fire") : new Text(
				"lt_condition.killed_by_player.not_player"));

		this.entry1.label.setTextID(new Text(condition == Condition.RANDOM_CHANCE ? "lt_condition.random_chance.chance" : "lt_condition.random_chance.chance"));
		this.entry2.label.setTextID(new Text("lt_condition.random_chance_with_looting.multiplier"));
	}
}
