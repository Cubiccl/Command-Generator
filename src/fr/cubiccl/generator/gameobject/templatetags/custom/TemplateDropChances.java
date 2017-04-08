package fr.cubiccl.generator.gameobject.templatetags.custom;

import java.awt.GridBagConstraints;

import org.jdom2.Element;

import fr.cubiccl.generator.CommandGenerator;
import fr.cubiccl.generator.gameobject.baseobjects.BaseObject;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagList;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateList;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.textfield.CGEntry;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;

public class TemplateDropChances extends TemplateList
{
	private class PanelDropChances extends CGPanel
	{
		private static final long serialVersionUID = -2401725294462669637L;

		private CGEntry[] entries;

		public PanelDropChances(String id, int slotCount)
		{
			this.entries = new CGEntry[slotCount];
			GridBagConstraints gbc = this.createGridBagLayout();
			for (int i = 0; i < slotCount; ++i)
			{
				this.add((this.entries[i] = new CGEntry(new Text("dropchance." + id + "." + i), "1", new Text("0 - 1", false))).container, gbc);
				++gbc.gridy;
			}
		}
	}

	private int slotCount = 1;

	public TemplateDropChances(String id, byte applicationType, String[] applicable)
	{
		super(id, applicationType, applicable);
	}

	@Override
	protected CGPanel createPanel(BaseObject object, Tag previousValue)
	{
		PanelDropChances p = new PanelDropChances(this.id(), this.slotCount);

		if (previousValue != null)
		{
			TagList t = (TagList) previousValue;
			for (int i = 0; i < this.slotCount; ++i)
				p.entries[i].setText(Float.toString((float) (double) t.getTag(i).value()));
		}

		p.setName(this.title());
		return p;
	}

	@Override
	public TagList generateTag(BaseObject object, CGPanel panel)
	{
		Tag[] tags = new Tag[this.slotCount];
		for (int i = 0; i < tags.length; i++)
			tags[i] = Tags.DEFAULT_FLOAT.create(Double.parseDouble(((PanelDropChances) panel).entries[i].getText()));
		return this.create(tags);
	}

	@Override
	protected boolean isInputValid(BaseObject object, CGPanel panel)
	{
		for (int i = 0; i < this.slotCount; i++)
			try
			{
				((PanelDropChances) panel).entries[i].checkValueInBounds(CGEntry.FLOAT, 0, 1);
			} catch (CommandGenerationException e)
			{
				CommandGenerator.report(e);
				return false;
			}
		return true;
	}

	public void setSlotCount(int slotCount)
	{
		this.slotCount = slotCount;
	}

	@Override
	public Element toXML()
	{
		Element root = super.toXML();
		root.getChild("customtype").setText(root.getChildText("customtype") + this.slotCount);
		return root;
	}

}
