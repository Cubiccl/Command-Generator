package fr.cubiccl.generator.gui.component.panel.loottable;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ButtonGroup;

import fr.cubiccl.generator.gameobject.loottable.LTCondition;
import fr.cubiccl.generator.gameobject.loottable.LTCondition.Condition;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.tags.TagNumber;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TagsMain;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound.DefaultCompound;
import fr.cubiccl.generator.gameobject.templatetags.TemplateNumber;
import fr.cubiccl.generator.gameobject.utils.TestValue;
import fr.cubiccl.generator.gui.component.button.CGRadioButton;
import fr.cubiccl.generator.gui.component.combobox.CGComboBox;
import fr.cubiccl.generator.gui.component.combobox.OptionCombobox;
import fr.cubiccl.generator.gui.component.interfaces.IObjectList;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.label.HelpLabel;
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
		public ScoreCondition duplicate(ScoreCondition object)
		{
			this.isRange = object.isRange;
			this.min = object.min;
			this.max = object.max;
			this.objective = object.objective;
			return this;
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

		public Tag toTag()
		{
			TestValue v = new TestValue(new TemplateNumber(this.objective, Tag.UNKNOWN), new DefaultCompound(this.objective, Tag.UNKNOWN));
			v.isRanged = this.isRange;
			v.valueMin = this.min;
			v.valueMax = this.max;
			return v.toTag();
		}

		@Override
		public ScoreCondition update(CGPanel panel) throws CommandGenerationException
		{
			ScoreCondition c = ((PanelConditionScore) panel).generate();
			this.isRange = c.isRange;
			this.max = c.max;
			this.min = c.min;
			this.objective = c.objective;
			return this;
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
		this.entry2.addHelpLabel(new HelpLabel("lt_condition.random_chance_with_looting.multiplier.help"));

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

	public LTCondition generate() throws CommandGenerationException
	{
		Condition condition = this.selectedCondition();
		ArrayList<Tag> tags = new ArrayList<Tag>();

		if (condition == Condition.ENTITY_PROPERTIES || condition == Condition.ENTITY_SCORES) tags.add(Tags.LT_CONDITION_ENTITY.create(this.comboboxEntity
				.getValue()));
		if (condition == Condition.ENTITY_PROPERTIES) tags.add(Tags.LT_CONDITION_PROPERTIES.create(Tags.LT_CONDITION_ENTITY_ONFIRE.create(this.buttonTrue
				.isSelected())));
		else if (condition == Condition.ENTITY_SCORES)
		{
			ArrayList<Tag> scoreTags = new ArrayList<Tag>();
			for (ScoreCondition score : this.scores.values())
				scoreTags.add(score.toTag());

			tags.add(Tags.LT_CONDITION_SCORES.create(scoreTags.toArray(new Tag[scoreTags.size()])));
		} else if (condition == Condition.KILLED_BY_PLAYER) tags.add(Tags.LT_CONDITION_KILLED.create(!this.buttonTrue.isSelected())); // reversed because tag is named inverse and stuff
		else if (condition == Condition.RANDOM_CHANCE || condition == Condition.RANDOM_CHANCE_WITH_LOOTING)
		{
			this.entry1.checkValueInBounds(CGEntry.FLOAT, 0, 1);
			tags.add(Tags.LT_CONDITION_CHANCE.create(Float.parseFloat(this.entry1.getText())));
			if (condition == Condition.RANDOM_CHANCE_WITH_LOOTING)
			{
				this.entry2.checkValue(CGEntry.FLOAT);
				tags.add(Tags.LT_CONDITION_LOOTING.create(Float.parseFloat(this.entry2.getText())));
			}
		}

		return new LTCondition(condition, tags.toArray(new Tag[tags.size()]));
	}

	private Condition selectedCondition()
	{
		Condition condition = Condition.values()[0];
		for (Condition c : Condition.values())
			if (c.translate().toString().equals(this.comboboxCondition.getSelectedItem())) return c;
		return condition;
	}

	public void setupFrom(LTCondition condition)
	{
		this.comboboxCondition.setSelectedItem(condition.condition.translate().toString());
		this.updateDisplay();

		TagCompound t = Tags.DEFAULT_COMPOUND.create(condition.tags);
		Condition c = condition.condition;

		if ((c == Condition.ENTITY_PROPERTIES || c == Condition.ENTITY_SCORES) && t.hasTag(Tags.LT_CONDITION_ENTITY)) this.comboboxEntity.setValue(t.getTag(
				Tags.LT_CONDITION_ENTITY).value());
		if (c == Condition.ENTITY_PROPERTIES && t.hasTag(Tags.LT_CONDITION_PROPERTIES))
		{
			TagCompound tag = t.getTag(Tags.LT_CONDITION_PROPERTIES);
			if (tag.hasTag(Tags.LT_CONDITION_ENTITY_ONFIRE))
			{
				this.buttonTrue.setSelected(tag.getTag(Tags.LT_CONDITION_ENTITY_ONFIRE).value());
				this.buttonFalse.setSelected(!tag.getTag(Tags.LT_CONDITION_ENTITY_ONFIRE).value());
			}
		} else if (c == Condition.ENTITY_SCORES && t.hasTag(Tags.LT_CONDITION_SCORES))
		{
			this.scores.clear();
			TagCompound sc = t.getTag(Tags.LT_CONDITION_SCORES);
			for (Tag s : sc.value())
			{
				if (s instanceof TagNumber) this.scores.add(new ScoreCondition(s.id(), ((TagNumber) s).valueInt(), 0, false));
				else if (s instanceof TagCompound)
				{
					TagCompound v = (TagCompound) s;
					int min = 0, max = 0;
					if (v.hasTag(TagsMain.VALUE_MIN)) min = v.getTag(TagsMain.VALUE_MIN).valueInt();
					if (v.hasTag(TagsMain.VALUE_MAX)) max = v.getTag(TagsMain.VALUE_MAX).valueInt();
					this.scores.add(new ScoreCondition(v.id(), min, max, true));
				}
			}
		} else if (c == Condition.KILLED_BY_PLAYER && t.hasTag(Tags.LT_CONDITION_KILLED))
		{
			// reversed because tag is named inverse and stuff
			this.buttonTrue.setSelected(!t.getTag(Tags.LT_CONDITION_KILLED).value());
			this.buttonFalse.setSelected(t.getTag(Tags.LT_CONDITION_KILLED).value());
		} else if (c == Condition.RANDOM_CHANCE || c == Condition.RANDOM_CHANCE_WITH_LOOTING)
		{
			if (t.hasTag(Tags.LT_CONDITION_CHANCE)) this.entry1.setText(Double.toString((t.getTag(Tags.LT_CONDITION_CHANCE)).value()));
			if (c == Condition.RANDOM_CHANCE_WITH_LOOTING && t.hasTag(Tags.LT_CONDITION_LOOTING)) this.entry2.setText(Double.toString(t.getTag(
					Tags.LT_CONDITION_LOOTING).value()));
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
