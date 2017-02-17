package fr.cubiccl.generator.gui.component.panel.loottable;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ButtonGroup;

import fr.cubiccl.generator.gameobject.loottable.LootTableCondition;
import fr.cubiccl.generator.gameobject.loottable.LootTableCondition.Condition;
import fr.cubiccl.generator.gameobject.tags.*;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound.DefaultCompound;
import fr.cubiccl.generator.gameobject.templatetags.TemplateNumber;
import fr.cubiccl.generator.gui.component.button.CGRadioButton;
import fr.cubiccl.generator.gui.component.combobox.CGComboBox;
import fr.cubiccl.generator.gui.component.combobox.OptionCombobox;
import fr.cubiccl.generator.gui.component.interfaces.IObjectList;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.utils.ListProperties;
import fr.cubiccl.generator.gui.component.panel.utils.PanelObjectList;
import fr.cubiccl.generator.gui.component.textfield.CGEntry;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;

public class PanelCondition extends CGPanel implements ActionListener
{
	public static class ScoreCondition implements IObjectList<ScoreCondition>
	{
		public boolean isRange;
		public int min, max;
		public String objective;

		public ScoreCondition()
		{
			this("", 0, 0, false);
		}

		public ScoreCondition(String objective, int min, int max, boolean isRange)
		{
			this.objective = objective;
			this.min = min;
			this.max = max;
			this.isRange = isRange;
		}

		@Override
		public CGPanel createPanel(ListProperties properties)
		{
			PanelConditionScore p = new PanelConditionScore();
			p.setupFrom(this);
			return p;
		}

		@Override
		public Component getDisplayComponent()
		{
			String text = this.objective + " = " + this.min;
			if (this.isRange) text = this.objective + " [" + this.min + ", " + this.max + "]";
			return new CGLabel(new Text(text, false));
		}

		@Override
		public String getName(int index)
		{
			return this.objective;
		}

		@Override
		public ScoreCondition setupFrom(CGPanel panel) throws CommandGenerationException
		{
			return ((PanelConditionScore) panel).generate();
		}

	}

	private static final long serialVersionUID = 1808992472445548208L;

	private CGRadioButton buttonTrue, buttonFalse;
	private CGComboBox comboboxCondition;
	private OptionCombobox comboboxEntity;
	private CGEntry entry1, entry2;
	private PanelObjectList<ScoreCondition> scores;

	public PanelCondition()
	{
		this(new ArrayList<Condition>());
	}

	public PanelCondition(ArrayList<Condition> usedConditions)
	{
		ArrayList<String> values = new ArrayList<String>();
		for (int i = 0; i < Condition.values().length; ++i)
			if (!usedConditions.contains(Condition.values()[i])) values.add(Condition.values()[i].translate().toString());

		GridBagConstraints gbc = this.createGridBagLayout();
		gbc.gridwidth = 2;
		this.add(this.comboboxCondition = new CGComboBox(values.toArray(new String[values.size()])), gbc);
		++gbc.gridy;
		this.add(this.comboboxEntity = new OptionCombobox("lt_condition.entity", "this", "killer", "killer_player"), gbc);
		++gbc.gridy;
		--gbc.gridwidth;
		this.add(this.buttonTrue = new CGRadioButton((Text) null), gbc);
		++gbc.gridx;
		this.add(this.buttonFalse = new CGRadioButton((Text) null), gbc);

		--gbc.gridx;
		++gbc.gridy;
		++gbc.gridwidth;
		this.add(this.scores = new PanelObjectList<ScoreCondition>("lt_condition.scores", (String) null, ScoreCondition.class), gbc);
		this.add((this.entry1 = new CGEntry(null)).container, gbc);
		++gbc.gridy;
		this.add((this.entry2 = new CGEntry(null)).container, gbc);

		ButtonGroup group = new ButtonGroup();
		group.add(this.buttonTrue);
		group.add(this.buttonFalse);
		this.buttonTrue.setSelected(true);

		this.entry1.addNumberFilter();
		this.entry2.addNumberFilter();

		this.comboboxCondition.addActionListener(this);
		this.buttonTrue.addActionListener(this);
		this.buttonFalse.addActionListener(this);
		this.updateDisplay();
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		this.updateDisplay();
	}

	public LootTableCondition generate() throws CommandGenerationException
	{
		Condition condition = this.selectedCondition();
		ArrayList<Tag> tags = new ArrayList<Tag>();

		if (condition == Condition.ENTITY_PROPERTIES || condition == Condition.ENTITY_SCORES) tags.add(new TagString(Tags.LT_CONDITION_ENTITY,
				this.comboboxEntity.getValue()));
		if (condition == Condition.ENTITY_PROPERTIES) tags.add(new TagCompound(Tags.LT_CONDITION_PROPERTIES, new TagBoolean(Tags.LT_CONDITION_ENTITY_ONFIRE,
				this.buttonTrue.isSelected())));
		else if (condition == Condition.ENTITY_SCORES)
		{
			ArrayList<Tag> scoreTags = new ArrayList<Tag>();
			for (ScoreCondition score : this.scores.values())
			{
				if (score.isRange) scoreTags.add(new TagCompound(new DefaultCompound(score.objective, Tag.UNKNOWN), new TagNumber(Tags.LT_CONDITION_SCORES_MIN,
						score.min), new TagNumber(Tags.LT_CONDITION_SCORES_MAX, score.max)));
				else scoreTags.add(new TagNumber(new TemplateNumber(score.objective, Tag.UNKNOWN), score.min));
			}
			tags.add(new TagCompound(Tags.LT_CONDITION_SCORES, scoreTags.toArray(new Tag[scoreTags.size()])));
		} else if (condition == Condition.KILLED_BY_PLAYER) tags.add(new TagBoolean(Tags.LT_CONDITION_KILLED, this.buttonTrue.isSelected()));
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

	private Condition selectedCondition()
	{
		Condition condition = Condition.values()[0];
		for (Condition c : Condition.values())
			if (c.translate().toString().equals(this.comboboxCondition.getSelectedItem())) return c;
		return condition;
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
				this.buttonTrue.setSelected(((TagBoolean) tag.getTag(Tags.LT_CONDITION_ENTITY_ONFIRE)).value());
				this.buttonFalse.setSelected(!((TagBoolean) tag.getTag(Tags.LT_CONDITION_ENTITY_ONFIRE)).value());
			}
		} else if (c == Condition.ENTITY_SCORES && t.hasTag(Tags.LT_CONDITION_SCORES))
		{
			this.scores.clear();
			TagCompound sc = (TagCompound) t.getTag(Tags.LT_CONDITION_SCORES);
			for (Tag s : sc.value())
			{
				if (s instanceof TagNumber) this.scores.add(new ScoreCondition(s.id(), ((TagNumber) s).value(), 0, false));
				else if (s instanceof TagCompound)
				{
					TagCompound v = (TagCompound) s;
					int min = 0, max = 0;
					if (v.hasTag(Tags.LT_CONDITION_SCORES_MIN)) min = ((TagNumber) v.getTag(Tags.LT_CONDITION_SCORES_MIN)).value();
					if (v.hasTag(Tags.LT_CONDITION_SCORES_MAX)) max = ((TagNumber) v.getTag(Tags.LT_CONDITION_SCORES_MAX)).value();
					this.scores.add(new ScoreCondition(v.id(), min, max, true));
				}
			}
		} else if (c == Condition.KILLED_BY_PLAYER && t.hasTag(Tags.LT_CONDITION_KILLED))
		{
			this.buttonTrue.setSelected(((TagBoolean) t.getTag(Tags.LT_CONDITION_KILLED)).value());
			this.buttonFalse.setSelected(!((TagBoolean) t.getTag(Tags.LT_CONDITION_KILLED)).value());
		} else if (c == Condition.RANDOM_CHANCE || c == Condition.RANDOM_CHANCE_WITH_LOOTING)
		{
			if (t.hasTag(Tags.LT_CONDITION_CHANCE)) this.entry1.setText(Double.toString(((TagBigNumber) t.getTag(Tags.LT_CONDITION_CHANCE)).value()));
			if (c == Condition.RANDOM_CHANCE_WITH_LOOTING && t.hasTag(Tags.LT_CONDITION_LOOTING)) this.entry2.setText(Double.toString(((TagBigNumber) t
					.getTag(Tags.LT_CONDITION_LOOTING)).value()));
		}
	}

	private void updateDisplay()
	{
		Condition condition = selectedCondition();

		this.comboboxEntity.setVisible(condition == Condition.ENTITY_SCORES || condition == Condition.ENTITY_PROPERTIES);
		this.scores.setVisible(condition == Condition.ENTITY_SCORES);
		this.buttonTrue.setVisible(condition == Condition.ENTITY_PROPERTIES || condition == Condition.KILLED_BY_PLAYER);
		this.buttonFalse.setVisible(this.buttonTrue.isVisible());

		this.entry1.container.setVisible(condition == Condition.RANDOM_CHANCE || condition == Condition.RANDOM_CHANCE_WITH_LOOTING);
		this.entry2.container.setVisible(condition == Condition.RANDOM_CHANCE_WITH_LOOTING);

		this.buttonTrue.setText(condition == Condition.ENTITY_PROPERTIES ? new Text("lt_condition.entity_properties.on_fire") : new Text(
				"lt_condition.killed_by_player.player"));
		this.buttonFalse.setText(condition == Condition.ENTITY_PROPERTIES ? new Text("lt_condition.entity_properties.not_on_fire") : new Text(
				"lt_condition.killed_by_player.not_player"));

		this.entry1.label.setTextID(new Text(condition == Condition.RANDOM_CHANCE ? "lt_condition.random_chance.chance" : "lt_condition.random_chance.chance"));
		this.entry2.label.setTextID(new Text("lt_condition.random_chance_with_looting.multiplier"));
	}
}
