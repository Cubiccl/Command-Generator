package fr.cubiccl.generator.gui.component.panel.advancement;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import fr.cubiccl.generator.CommandGenerator;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.utils.TestValue;
import fr.cubiccl.generator.gui.component.button.CGCheckBox;
import fr.cubiccl.generator.gui.component.panel.tag.PanelRangedValue;
import fr.cubiccl.generator.gui.component.panel.utils.PanelTestValues;
import fr.cubiccl.generator.gui.component.textfield.CGEntry;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;

public class PanelDamage extends PanelTestValues
{
	private static final long serialVersionUID = -3021949794358180157L;

	private CGCheckBox checkboxBlocked;
	public final TestValue dealt, taken;
	private PanelRangedValue panelDealt, panelTaken;
	private PanelDamageFlag panelFlags;
	private PanelCriteriaEntity panelSource;

	public PanelDamage()
	{
		this.dealt = new TestValue(Tags.CRITERIA_DEALT, Tags.CRITERIA_DEALT_);
		this.taken = new TestValue(Tags.CRITERIA_TAKEN, Tags.CRITERIA_TAKEN_);

		this.addComponent("criteria.damage.dealt", this.panelDealt = new PanelRangedValue("criteria.damage.dealt", null, Text.NUMBER));
		this.addComponent("criteria.damage.taken", this.panelTaken = new PanelRangedValue("criteria.damage.taken", null, Text.NUMBER));
		this.addComponent("criteria.damage.blocked", this.checkboxBlocked = new CGCheckBox("criteria.damage.blocked.no"));
		this.addComponent("criteria.damage.flags", this.panelFlags = new PanelDamageFlag("criteria.damage.flags"));
		this.addComponent("criteria.damage.source", this.panelSource = new PanelCriteriaEntity("criteria.damage.source"));

		this.checkboxBlocked.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				block();
			}
		});
	}

	private void block()
	{
		this.checkboxBlocked.setTextID(this.checkboxBlocked.isSelected() ? new Text("criteria.damage.blocked.yes") : new Text("criteria.damage.blocked.no"));
	}

	public boolean checkInput()
	{
		try
		{
			if (this.isSelected(this.panelDealt)) this.panelDealt.checkInput(CGEntry.NUMBER);
			if (this.isSelected(this.panelTaken)) this.panelTaken.checkInput(CGEntry.NUMBER);
		} catch (CommandGenerationException e)
		{
			CommandGenerator.report(e);
			return false;
		}
		if (this.isSelected(this.panelFlags) && !this.panelFlags.checkInput()) return false;
		if (this.isSelected(this.panelSource) && !this.panelSource.checkInput()) return false;
		return true;
	}

	public Tag[] generateTags()
	{
		ArrayList<Tag> tags = new ArrayList<Tag>();
		if (this.isSelected(this.panelDealt)) tags.add(this.panelDealt.generateValue(this.dealt).toTag());
		if (this.isSelected(this.panelTaken)) tags.add(this.panelTaken.generateValue(this.taken).toTag());
		if (this.isSelected(this.checkboxBlocked)) tags.add(Tags.CRITERIA_BLOCKED.create(this.checkboxBlocked.isSelected()));
		if (this.isSelected(this.panelFlags)) tags.add(this.panelFlags.generateTag(Tags.CRITERIA_DAMAGE_FLAGS));
		if (this.isSelected(this.panelSource)) tags.add(Tags.CRITERIA_SOURCE_ENTITY.create(this.panelSource.generateTags()));
		return tags.toArray(new Tag[tags.size()]);
	}

	public void setupFrom(TagCompound value)
	{
		if (this.dealt.findValue(value))
		{
			this.select(this.panelDealt);
			this.panelDealt.setupFrom(this.dealt);
		}
		if (this.taken.findValue(value))
		{
			this.select(this.panelTaken);
			this.panelTaken.setupFrom(this.taken);
		}
		if (value.hasTag(Tags.CRITERIA_BLOCKED))
		{
			this.select(this.checkboxBlocked);
			this.checkboxBlocked.setSelected(value.getTag(Tags.CRITERIA_BLOCKED).value());
		}
		if (value.hasTag(Tags.CRITERIA_DAMAGE_FLAGS))
		{
			this.select(this.panelFlags);
			this.panelFlags.setupFrom(value.getTag(Tags.CRITERIA_DAMAGE_FLAGS));
		}
		if (value.hasTag(Tags.CRITERIA_SOURCE_ENTITY))
		{
			this.select(this.panelSource);
			this.panelSource.setupFrom(value.getTag(Tags.CRITERIA_SOURCE_ENTITY));
		}
		this.block();
	}

}
