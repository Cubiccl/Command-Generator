package fr.cubiccl.generator.gui.component.panel.advancement;

import fr.cubiccl.generator.CommandGenerator;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TagsMain;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;
import fr.cubiccl.generator.gameobject.utils.TestValue;
import fr.cubiccl.generator.gui.component.panel.tag.PanelRangedValue;
import fr.cubiccl.generator.gui.component.panel.utils.PanelTestValues;
import fr.cubiccl.generator.gui.component.textfield.CGEntry;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;

public class PanelCriteriaDistance extends PanelTestValues
{
	private static final long serialVersionUID = 3760535637683579500L;

	private PanelRangedValue[] panels;
	private PanelRangedValue panelX, panelY, panelZ, panelAbsolute, panelHorizontal;
	private TestValue[] values;
	private TestValue x, y, z, absolute, horizontal;

	public PanelCriteriaDistance()
	{
		this(null);
	}

	public PanelCriteriaDistance(String titleID)
	{
		super(titleID);

		this.x = new TestValue(Tags.CRITERIA_X, Tags.CRITERIA_X_, TagsMain.VALUE_MIN_DOUBLE, TagsMain.VALUE_MAX_DOUBLE);
		this.y = new TestValue(Tags.CRITERIA_Y, Tags.CRITERIA_Y_, TagsMain.VALUE_MIN_DOUBLE, TagsMain.VALUE_MAX_DOUBLE);
		this.z = new TestValue(Tags.CRITERIA_Z, Tags.CRITERIA_Z_, TagsMain.VALUE_MIN_DOUBLE, TagsMain.VALUE_MAX_DOUBLE);
		this.absolute = new TestValue(Tags.CRITERIA_ABSOLUTE, Tags.CRITERIA_ABSOLUTE_, TagsMain.VALUE_MIN_DOUBLE, TagsMain.VALUE_MAX_DOUBLE);
		this.horizontal = new TestValue(Tags.CRITERIA_HORIZONTAL, Tags.CRITERIA_HORIZONTAL_, TagsMain.VALUE_MIN_DOUBLE, TagsMain.VALUE_MAX_DOUBLE);

		this.addComponent("criteria.distance.x", this.panelX = new PanelRangedValue("criteria.distance.x", null, Text.NUMBER));
		this.addComponent("criteria.distance.y", this.panelY = new PanelRangedValue("criteria.distance.y", null, Text.NUMBER));
		this.addComponent("criteria.distance.z", this.panelZ = new PanelRangedValue("criteria.distance.z", null, Text.NUMBER));
		this.addComponent("criteria.distance.absolute", this.panelAbsolute = new PanelRangedValue("criteria.distance.absolute", null, Text.NUMBER));
		this.addComponent("criteria.distance.horizontal", this.panelHorizontal = new PanelRangedValue("criteria.distance.horizontal", null, Text.NUMBER));

		this.values = new TestValue[]
		{ this.x, this.y, this.z, this.absolute, this.horizontal };
		this.panels = new PanelRangedValue[]
		{ this.panelX, this.panelY, this.panelZ, this.panelAbsolute, this.panelHorizontal };
	}

	public boolean checkInput()
	{
		for (int i = 0; i < this.values.length; ++i)
			if (this.isSelected(this.panels[i])) try
			{
				this.panels[i].checkInput(CGEntry.NUMBER);
			} catch (CommandGenerationException e)
			{
				CommandGenerator.report(e);
				return false;
			}
		return true;
	}

	public Tag generateValue(TemplateCompound container)
	{
		TagCompound tag = container.create();
		for (int i = 0; i < this.values.length; ++i)
			if (this.isSelected(this.panels[i])) tag.addTag(this.panels[i].generateValue(this.values[i]).toTag());
		return tag;
	}

	public void setupFrom(TagCompound tag)
	{
		for (int i = 0; i < this.values.length; ++i)
			if (this.values[i].findValue(tag))
			{
				this.select(this.panels[i]);
				this.panels[i].setupFrom(this.values[i]);
			}
	}

}
