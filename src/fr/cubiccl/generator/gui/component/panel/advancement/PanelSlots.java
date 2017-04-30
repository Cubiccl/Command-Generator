package fr.cubiccl.generator.gui.component.panel.advancement;

import fr.cubiccl.generator.CommandGenerator;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;
import fr.cubiccl.generator.gameobject.utils.TestValue;
import fr.cubiccl.generator.gui.component.panel.tag.PanelRangedValue;
import fr.cubiccl.generator.gui.component.panel.utils.PanelTestValues;
import fr.cubiccl.generator.gui.component.textfield.CGEntry;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;

public class PanelSlots extends PanelTestValues
{
	private static final long serialVersionUID = -7020618884598682016L;

	private TestValue occupied, full, empty;
	private PanelRangedValue panelOccupied, panelFull, panelEmpty;

	public PanelSlots()
	{
		this.empty = new TestValue(Tags.CRITERIA_EMPTY, Tags.CRITERIA_EMPTY_);
		this.occupied = new TestValue(Tags.CRITERIA_OCCUPIED, Tags.CRITERIA_OCCUPIED_);
		this.full = new TestValue(Tags.CRITERIA_FULL, Tags.CRITERIA_FULL_);

		this.addComponent("criteria.slots.empty", this.panelEmpty = new PanelRangedValue("criteria.slots.empty", null, Text.INTEGER));
		this.addComponent("criteria.slots.occupied", this.panelOccupied = new PanelRangedValue("criteria.slots.occupied", null, Text.INTEGER));
		this.addComponent("criteria.slots.full", this.panelFull = new PanelRangedValue("criteria.slots.full", null, Text.INTEGER));
	}

	public boolean checkInput()
	{
		try
		{
			if (this.isSelected(this.panelEmpty)) this.panelEmpty.checkInput(CGEntry.INTEGER);
			if (this.isSelected(this.panelOccupied)) this.panelOccupied.checkInput(CGEntry.INTEGER);
			if (this.isSelected(this.panelFull)) this.panelFull.checkInput(CGEntry.INTEGER);
			return true;
		} catch (CommandGenerationException e)
		{
			CommandGenerator.report(e);
			return false;
		}
	}

	public TagCompound generateTag(TemplateCompound container)
	{
		TagCompound t = container.create();
		if (this.isSelected(this.panelEmpty)) t.addTag(this.panelEmpty.generateValue(this.empty).toTag());
		if (this.isSelected(this.panelOccupied)) t.addTag(this.panelOccupied.generateValue(this.occupied).toTag());
		if (this.isSelected(this.panelFull)) t.addTag(this.panelFull.generateValue(this.full).toTag());
		return t;
	}

	public void setupFrom(TagCompound tag)
	{
		if (this.full.findValue(tag))
		{
			this.select(this.panelFull);
			this.panelFull.setupFrom(this.full);
		}
		if (this.occupied.findValue(tag))
		{
			this.select(this.panelOccupied);
			this.panelOccupied.setupFrom(this.occupied);
		}
		if (this.empty.findValue(tag))
		{
			this.select(this.panelEmpty);
			this.panelEmpty.setupFrom(this.empty);
		}
	}

}
