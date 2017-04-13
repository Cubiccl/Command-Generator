package fr.cubiccl.generator.gui.component.panel.tag;

import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.ButtonGroup;

import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gui.component.button.CGRadioButton;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.utils.CommandGenerationException;

public class PanelAbilities extends CGPanel
{
	private static final long serialVersionUID = -1807635766661445658L;

	private CGRadioButton buttonFlyingYes, buttonFlyingNo, buttonFlyingNull;
	private CGRadioButton buttonInstabuildYes, buttonInstabuildNo, buttonInstabuildNull;
	private CGRadioButton buttonInvulnerableYes, buttonInvulnerableNo, buttonInvulnerableNull;
	private CGRadioButton buttonMayBuildYes, buttonMayBuildNo, buttonMayBuildNull;
	private CGRadioButton buttonMayFlyYes, buttonMayFlyNo, buttonMayFlyNull;

	public PanelAbilities()
	{
		GridBagConstraints gbc = this.createGridBagLayout();

		CGPanel panelMayFly = new CGPanel("abilities.may_fly");
		panelMayFly.setLayout(new GridLayout(1, 3, 5, 1));
		panelMayFly.add(this.buttonMayFlyYes = new CGRadioButton("general.yes"));
		panelMayFly.add(this.buttonMayFlyNo = new CGRadioButton("general.no"));
		panelMayFly.add(this.buttonMayFlyNull = new CGRadioButton("general.unspecified"));
		ButtonGroup group = new ButtonGroup();
		group.add(this.buttonMayFlyYes);
		group.add(this.buttonMayFlyNo);
		group.add(this.buttonMayFlyNull);

		CGPanel panelFlying = new CGPanel("abilities.flying");
		panelFlying.setLayout(new GridLayout(1, 3, 5, 1));
		panelFlying.add(this.buttonFlyingYes = new CGRadioButton("general.yes"));
		panelFlying.add(this.buttonFlyingNo = new CGRadioButton("general.no"));
		panelFlying.add(this.buttonFlyingNull = new CGRadioButton("general.unspecified"));
		group = new ButtonGroup();
		group.add(this.buttonFlyingYes);
		group.add(this.buttonFlyingNo);
		group.add(this.buttonFlyingNull);

		CGPanel panelMayBuild = new CGPanel("abilities.may_build");
		panelMayBuild.setLayout(new GridLayout(1, 3, 5, 1));
		panelMayBuild.add(this.buttonMayBuildYes = new CGRadioButton("general.yes"));
		panelMayBuild.add(this.buttonMayBuildNo = new CGRadioButton("general.no"));
		panelMayBuild.add(this.buttonMayBuildNull = new CGRadioButton("general.unspecified"));
		group = new ButtonGroup();
		group.add(this.buttonMayBuildYes);
		group.add(this.buttonMayBuildNo);
		group.add(this.buttonMayBuildNull);

		CGPanel panelInstabuild = new CGPanel("abilities.instabuild");
		panelInstabuild.setLayout(new GridLayout(1, 3, 5, 1));
		panelInstabuild.add(this.buttonInstabuildYes = new CGRadioButton("general.yes"));
		panelInstabuild.add(this.buttonInstabuildNo = new CGRadioButton("general.no"));
		panelInstabuild.add(this.buttonInstabuildNull = new CGRadioButton("general.unspecified"));
		group = new ButtonGroup();
		group.add(this.buttonInstabuildYes);
		group.add(this.buttonInstabuildNo);
		group.add(this.buttonInstabuildNull);

		CGPanel panelInvulnerable = new CGPanel("abilities.invulnerable");
		panelInvulnerable.setLayout(new GridLayout(1, 3, 5, 1));
		panelInvulnerable.add(this.buttonInvulnerableYes = new CGRadioButton("general.yes"));
		panelInvulnerable.add(this.buttonInvulnerableNo = new CGRadioButton("general.no"));
		panelInvulnerable.add(this.buttonInvulnerableNull = new CGRadioButton("general.unspecified"));
		group = new ButtonGroup();
		group.add(this.buttonInvulnerableYes);
		group.add(this.buttonInvulnerableNo);
		group.add(this.buttonInvulnerableNull);

		this.add(panelMayFly, gbc);
		++gbc.gridy;
		this.add(panelFlying, gbc);
		++gbc.gridy;
		this.add(panelMayBuild, gbc);
		++gbc.gridy;
		this.add(panelInstabuild, gbc);
		++gbc.gridy;
		this.add(panelInvulnerable, gbc);

		this.buttonMayFlyNull.setSelected(true);
		this.buttonFlyingNull.setSelected(true);
		this.buttonMayBuildNull.setSelected(true);
		this.buttonInstabuildNull.setSelected(true);
		this.buttonInvulnerableNull.setSelected(true);
	}

	public Tag[] generateAbilities() throws CommandGenerationException
	{
		ArrayList<Tag> tags = new ArrayList<Tag>();

		if (!this.buttonMayFlyNull.isSelected()) tags.add(Tags.ABILITIES_MAY_FLY.create(this.buttonMayFlyYes.isSelected() ? 1 : 0));
		if (!this.buttonFlyingNull.isSelected()) tags.add(Tags.ABILITIES_FLYING.create(this.buttonFlyingYes.isSelected() ? 1 : 0));
		if (!this.buttonMayBuildNull.isSelected()) tags.add(Tags.ABILITIES_MAY_BUILD.create(this.buttonMayBuildYes.isSelected() ? 1 : 0));
		if (!this.buttonInstabuildNull.isSelected()) tags.add(Tags.ABILITIES_INSTABUILD.create(this.buttonInstabuildYes.isSelected() ? 1 : 0));
		if (!this.buttonInvulnerableNull.isSelected()) tags.add(Tags.ABILITIES_INVULNERABLE.create(this.buttonInvulnerableYes.isSelected() ? 1 : 0));

		return tags.toArray(new Tag[tags.size()]);
	}

	public void setupFrom(TagCompound tag)
	{
		this.buttonMayFlyNull.setSelected(true);
		this.buttonFlyingNull.setSelected(true);
		this.buttonMayBuildNull.setSelected(true);
		this.buttonInstabuildNull.setSelected(true);
		this.buttonInvulnerableNull.setSelected(true);

		if (tag.hasTag(Tags.ABILITIES_MAY_FLY))
		{
			if (tag.getTag(Tags.ABILITIES_MAY_FLY).value() == 1) this.buttonMayFlyYes.setSelected(true);
			else this.buttonMayFlyNo.setSelected(true);
		}
		if (tag.hasTag(Tags.ABILITIES_FLYING))
		{
			if (tag.getTag(Tags.ABILITIES_FLYING).value() == 1) this.buttonFlyingYes.setSelected(true);
			else this.buttonFlyingNo.setSelected(true);
		}
		if (tag.hasTag(Tags.ABILITIES_MAY_BUILD))
		{
			if (tag.getTag(Tags.ABILITIES_MAY_BUILD).value() == 1) this.buttonMayBuildYes.setSelected(true);
			else this.buttonMayBuildNo.setSelected(true);
		}
		if (tag.hasTag(Tags.ABILITIES_INSTABUILD))
		{
			if (tag.getTag(Tags.ABILITIES_INSTABUILD).value() == 1) this.buttonInstabuildYes.setSelected(true);
			else this.buttonInstabuildNo.setSelected(true);
		}
		if (tag.hasTag(Tags.ABILITIES_INVULNERABLE))
		{
			if (tag.getTag(Tags.ABILITIES_INVULNERABLE).value() == 1) this.buttonInvulnerableYes.setSelected(true);
			else this.buttonInvulnerableNo.setSelected(true);
		}
	}
}
