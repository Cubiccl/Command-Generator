package fr.cubiccl.generator.gameobject.templatetags.custom;

import java.awt.GridBagConstraints;

import fr.cubiccl.generator.CommandGenerator;
import fr.cubiccl.generator.gameobject.Coordinates;
import fr.cubiccl.generator.gameobject.baseobjects.BaseObject;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.tags.TagList;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelCoordinates;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;

public class TemplatePose extends TemplateCompound
{

	private class PanelPose extends CGPanel
	{
		private static final long serialVersionUID = 3394333445873812914L;

		private PanelCoordinates body, head, leftArm, rightArm, leftLeg, rightLeg;

		public PanelPose()
		{
			GridBagConstraints gbc = this.createGridBagLayout();
			this.add(this.body = new PanelCoordinates("pose.body", false), gbc);
			++gbc.gridy;
			this.add(this.leftArm = new PanelCoordinates("pose.arm.left", false), gbc);
			++gbc.gridy;
			this.add(this.leftLeg = new PanelCoordinates("pose.leg.left", false), gbc);

			++gbc.gridx;
			gbc.gridy = 0;
			this.add(this.head = new PanelCoordinates("pose.head", false), gbc);
			++gbc.gridy;
			this.add(this.rightArm = new PanelCoordinates("pose.arm.right", false), gbc);
			++gbc.gridy;
			this.add(this.rightLeg = new PanelCoordinates("pose.leg.right", false), gbc);

			this.body.setEntryText(new Text("rotation.x"), new Text("rotation.y"), new Text("rotation.z"));
			this.head.setEntryText(new Text("rotation.x"), new Text("rotation.y"), new Text("rotation.z"));
			this.leftArm.setEntryText(new Text("rotation.x"), new Text("rotation.y"), new Text("rotation.z"));
			this.rightArm.setEntryText(new Text("rotation.x"), new Text("rotation.y"), new Text("rotation.z"));
			this.leftLeg.setEntryText(new Text("rotation.x"), new Text("rotation.y"), new Text("rotation.z"));
			this.rightLeg.setEntryText(new Text("rotation.x"), new Text("rotation.y"), new Text("rotation.z"));
		}

	}

	public TemplatePose(String id, byte applicationType, String[] applicable)
	{
		super(id, applicationType, applicable);
	}

	@Override
	protected CGPanel createPanel(BaseObject object, Tag previousValue)
	{
		PanelPose p = new PanelPose();

		if (previousValue != null)
		{
			TagCompound t = (TagCompound) previousValue;
			p.body.setupFrom(Coordinates.createFrom((TagList) t.getTagFromId(Tags.POSE_BODY.id())));
			p.head.setupFrom(Coordinates.createFrom((TagList) t.getTagFromId(Tags.POSE_HEAD.id())));
			p.leftArm.setupFrom(Coordinates.createFrom((TagList) t.getTagFromId(Tags.POSE_ARM_LEFT.id())));
			p.rightArm.setupFrom(Coordinates.createFrom((TagList) t.getTagFromId(Tags.POSE_ARM_RIGHT.id())));
			p.leftLeg.setupFrom(Coordinates.createFrom((TagList) t.getTagFromId(Tags.POSE_LEG_LEFT.id())));
			p.rightLeg.setupFrom(Coordinates.createFrom((TagList) t.getTagFromId(Tags.POSE_LEG_RIGHT.id())));
		}

		p.setName(this.title());
		return p;
	}

	@Override
	public TagCompound generateTag(BaseObject object, CGPanel panel)
	{
		PanelPose p = (PanelPose) panel;
		TagList[] tags = new TagList[6];
		try
		{
			tags[0] = p.body.generate().toTagList(Tags.POSE_BODY);
			tags[1] = p.head.generate().toTagList(Tags.POSE_HEAD);
			tags[2] = p.leftArm.generate().toTagList(Tags.POSE_ARM_LEFT);
			tags[3] = p.rightArm.generate().toTagList(Tags.POSE_ARM_RIGHT);
			tags[4] = p.leftLeg.generate().toTagList(Tags.POSE_LEG_LEFT);
			tags[5] = p.rightLeg.generate().toTagList(Tags.POSE_LEG_RIGHT);
		} catch (CommandGenerationException e)
		{
			e.printStackTrace();
		}
		return this.create(tags);
	}

	@Override
	protected boolean isInputValid(BaseObject object, CGPanel panel)
	{
		try
		{
			PanelPose p = (PanelPose) panel;
			p.body.generate().toTagList(Tags.POSE_BODY);
			p.head.generate().toTagList(Tags.POSE_HEAD);
			p.leftArm.generate().toTagList(Tags.POSE_ARM_LEFT);
			p.rightArm.generate().toTagList(Tags.POSE_ARM_RIGHT);
			p.leftLeg.generate().toTagList(Tags.POSE_LEG_LEFT);
			p.rightLeg.generate().toTagList(Tags.POSE_LEG_RIGHT);
			return true;
		} catch (CommandGenerationException e)
		{
			CommandGenerator.report(e);
			return false;
		}
	}

}
