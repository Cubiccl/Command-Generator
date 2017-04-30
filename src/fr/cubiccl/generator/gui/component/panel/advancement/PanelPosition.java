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

public class PanelPosition extends PanelTestValues
{
	private static final long serialVersionUID = 2712274801702975265L;

	private PanelRangedValue panelX, panelY, panelZ;
	private TestValue x, y, z;

	public PanelPosition()
	{
		this.x = new TestValue(Tags.CRITERIA_X, Tags.CRITERIA_X_);
		this.y = new TestValue(Tags.CRITERIA_Y, Tags.CRITERIA_Y_);
		this.z = new TestValue(Tags.CRITERIA_Z, Tags.CRITERIA_Z_);

		this.addComponent("criteria.position.x", this.panelX = new PanelRangedValue("criteria.position.x", null, Text.NUMBER));
		this.addComponent("criteria.position.y", this.panelY = new PanelRangedValue("criteria.position.y", null, Text.NUMBER));
		this.addComponent("criteria.position.z", this.panelZ = new PanelRangedValue("criteria.position.z", null, Text.NUMBER));
	}

	public boolean checkInput()
	{
		try
		{
			if (this.isSelected(this.panelX)) this.panelX.checkInput(CGEntry.NUMBER);
			if (this.isSelected(this.panelY)) this.panelY.checkInput(CGEntry.NUMBER);
			if (this.isSelected(this.panelZ)) this.panelZ.checkInput(CGEntry.NUMBER);
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
		if (this.isSelected(this.panelX)) t.addTag(this.panelX.generateValue(this.x).toTag());
		if (this.isSelected(this.panelY)) t.addTag(this.panelY.generateValue(this.y).toTag());
		if (this.isSelected(this.panelZ)) t.addTag(this.panelZ.generateValue(this.z).toTag());
		return t;
	}

	public void setupFrom(TagCompound tag)
	{
		if (this.x.findValue(tag))
		{
			this.panelX.setupFrom(this.x);
			this.select(this.panelX);
		}
		if (this.y.findValue(tag))
		{
			this.panelY.setupFrom(this.y);
			this.select(this.panelY);
		}
		if (this.z.findValue(tag))
		{
			this.panelZ.setupFrom(this.z);
			this.select(this.panelZ);
		}
	}

}
