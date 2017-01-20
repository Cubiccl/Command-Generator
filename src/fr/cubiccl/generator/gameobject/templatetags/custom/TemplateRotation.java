package fr.cubiccl.generator.gameobject.templatetags.custom;

import java.awt.GridBagConstraints;

import fr.cubiccl.generator.CommandGenerator;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagBigNumber;
import fr.cubiccl.generator.gameobject.tags.TagList;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateList;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.textfield.CGEntry;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;

public class TemplateRotation extends TemplateList
{

	private class RotationPanel extends CGPanel
	{
		private static final long serialVersionUID = -5783252043148064976L;

		private CGEntry entryY, entryX;

		public RotationPanel()
		{
			super();
			GridBagConstraints gbc = this.createGridBagLayout();
			this.add((this.entryY = new CGEntry(new Text("tp.rotation.y"), "0", new Text("0 � 360", false))).container, gbc);
			++gbc.gridy;
			this.add((this.entryX = new CGEntry(new Text("tp.rotation.x"), "0", new Text("-90 � 90", false))).container, gbc);
		}

	}

	public TemplateRotation(String id, byte tagType, String[] applicable)
	{
		super(id, tagType, applicable);
	}

	@Override
	protected CGPanel createPanel(String objectId, Tag previousValue)
	{
		RotationPanel p = new RotationPanel();
		if (previousValue != null)
		{
			p.entryY.setText(Double.toString((double) ((TagList) previousValue).value()[0].value()));
			p.entryX.setText(Double.toString((double) ((TagList) previousValue).value()[1].value()));
		}
		p.setName(this.title());
		return p;
	}

	@Override
	public TagList generateTag(CGPanel panel)
	{
		RotationPanel p = (RotationPanel) panel;
		TagBigNumber y = new TagBigNumber(Tags.DEFAULT_FLOAT, Double.parseDouble(p.entryY.getText()));
		TagBigNumber x = new TagBigNumber(Tags.DEFAULT_FLOAT, Double.parseDouble(p.entryX.getText()));
		return new TagList(this, y, x);
	}

	@Override
	protected boolean isInputValid(CGPanel panel)
	{
		RotationPanel p = (RotationPanel) panel;
		try
		{
			p.entryY.checkValueInBounds(CGEntry.FLOAT, 0, 360);
			p.entryX.checkValueInBounds(CGEntry.FLOAT, -90, 90);
			return true;
		} catch (CommandGenerationException e)
		{
			CommandGenerator.report(e);
			return false;
		}
	}

}