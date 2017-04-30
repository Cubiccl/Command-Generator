package fr.cubiccl.generator.gui.component.panel.advancement;

import java.awt.event.ActionEvent;
import java.util.ArrayList;

import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;
import fr.cubiccl.generator.gui.component.button.CGCheckBox;
import fr.cubiccl.generator.gui.component.panel.utils.PanelTestValues;

public class PanelDamageFlag extends PanelTestValues
{
	private static final long serialVersionUID = -6598302948505578568L;

	private CGCheckBox checkboxBypassArmor, checkboxBypassInvul, checkboxBypassMagic;
	private CGCheckBox checkboxIsExplosion, checkboxIsFire, checkboxIsMagic, checkboxIsProjectile;
	private PanelCriteriaEntity panelSource, panelDirect;

	public PanelDamageFlag(String titleID)
	{
		super(titleID);
		this.addComponent("criteria.damage.bypass_armor", this.checkboxBypassArmor = new CGCheckBox("criteria.damage.bypass_armor.no"));
		this.addComponent("criteria.damage.bypass_invul", this.checkboxBypassInvul = new CGCheckBox("criteria.damage.bypass_invul.no"));
		this.addComponent("criteria.damage.bypass_magic", this.checkboxBypassMagic = new CGCheckBox("criteria.damage.bypass_magic.no"));
		this.addComponent("criteria.damage.is_explosion", this.checkboxIsExplosion = new CGCheckBox("criteria.damage.is_explosion.no"));
		this.addComponent("criteria.damage.is_fire", this.checkboxIsFire = new CGCheckBox("criteria.damage.is_fire.no"));
		this.addComponent("criteria.damage.is_magic", this.checkboxIsMagic = new CGCheckBox("criteria.damage.is_magic.no"));
		this.addComponent("criteria.damage.is_projectile", this.checkboxIsProjectile = new CGCheckBox("criteria.damage.is_projectile.no"));
		this.addComponent("criteria.damage.source", this.panelSource = new PanelCriteriaEntity("criteria.damage.source"));
		this.addComponent("criteria.damage.direct", this.panelDirect = new PanelCriteriaEntity("criteria.damage.direct"));

		this.checkboxBypassArmor.addActionListener(this);
		this.checkboxBypassInvul.addActionListener(this);
		this.checkboxBypassMagic.addActionListener(this);
		this.checkboxIsExplosion.addActionListener(this);
		this.checkboxIsFire.addActionListener(this);
		this.checkboxIsMagic.addActionListener(this);
		this.checkboxIsProjectile.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		super.actionPerformed(e);
		this.checkbox();

	}

	private void checkbox()
	{
		if (this.checkboxBypassArmor.isSelected()) this.checkboxBypassArmor.setTextID("criteria.damage.bypass_armor.yes");
		else this.checkboxBypassArmor.setTextID("criteria.damage.bypass_armor.no");

		if (this.checkboxBypassInvul.isSelected()) this.checkboxBypassInvul.setTextID("criteria.damage.bypass_invul.yes");
		else this.checkboxBypassInvul.setTextID("criteria.damage.bypass_invul.no");

		if (this.checkboxBypassMagic.isSelected()) this.checkboxBypassMagic.setTextID("criteria.damage.bypass_magic.yes");
		else this.checkboxBypassMagic.setTextID("criteria.damage.bypass_magic.no");

		if (this.checkboxIsExplosion.isSelected()) this.checkboxIsExplosion.setTextID("criteria.damage.is_explosion.yes");
		else this.checkboxIsExplosion.setTextID("criteria.damage.is_explosion.no");

		if (this.checkboxIsFire.isSelected()) this.checkboxIsFire.setTextID("criteria.damage.is_fire.yes");
		else this.checkboxIsFire.setTextID("criteria.damage.is_fire.no");

		if (this.checkboxIsMagic.isSelected()) this.checkboxIsMagic.setTextID("criteria.damage.is_magic.yes");
		else this.checkboxIsMagic.setTextID("criteria.damage.is_magic.no");

		if (this.checkboxIsProjectile.isSelected()) this.checkboxIsProjectile.setTextID("criteria.damage.is_projectile.yes");
		else this.checkboxIsProjectile.setTextID("criteria.damage.is_projectile.no");
	}

	public boolean checkInput()
	{
		if (this.isSelected(this.panelSource) && !this.panelSource.checkInput()) return false;
		if (this.isSelected(this.panelDirect) && !this.panelDirect.checkInput()) return false;
		return true;
	}

	public TagCompound generateTag(TemplateCompound container)
	{
		ArrayList<Tag> tags = new ArrayList<Tag>();
		if (this.isSelected(this.checkboxBypassArmor)) tags.add(Tags.CRITERIA_BYPASSARMOR.create(this.checkboxBypassArmor.isSelected()));
		if (this.isSelected(this.checkboxBypassInvul)) tags.add(Tags.CRITERIA_BYPASSINVUL.create(this.checkboxBypassInvul.isSelected()));
		if (this.isSelected(this.checkboxBypassMagic)) tags.add(Tags.CRITERIA_BYPASSMAGIC.create(this.checkboxBypassMagic.isSelected()));
		if (this.isSelected(this.checkboxIsExplosion)) tags.add(Tags.CRITERIA_ISEXPLOSION.create(this.checkboxIsExplosion.isSelected()));
		if (this.isSelected(this.checkboxIsFire)) tags.add(Tags.CRITERIA_ISFIRE.create(this.checkboxIsFire.isSelected()));
		if (this.isSelected(this.checkboxIsMagic)) tags.add(Tags.CRITERIA_ISMAGIC.create(this.checkboxIsMagic.isSelected()));
		if (this.isSelected(this.checkboxIsProjectile)) tags.add(Tags.CRITERIA_ISPROJECTILE.create(this.checkboxIsProjectile.isSelected()));
		if (this.isSelected(this.panelSource)) tags.add(Tags.CRITERIA_SOURCE_ENTITY.create(this.panelSource.generateTags()));
		if (this.isSelected(this.panelDirect)) tags.add(Tags.CRITERIA_DIRECT_ENTITY.create(this.panelDirect.generateTags()));
		return container.create(tags.toArray(new Tag[tags.size()]));
	}

	public void setupFrom(TagCompound tag)
	{
		if (tag.hasTag(Tags.CRITERIA_BYPASSARMOR))
		{
			this.select(this.checkboxBypassArmor);
			this.checkboxBypassArmor.setSelected(tag.getTag(Tags.CRITERIA_BYPASSARMOR).value());
		}
		if (tag.hasTag(Tags.CRITERIA_BYPASSINVUL))
		{
			this.select(this.checkboxBypassInvul);
			this.checkboxBypassMagic.setSelected(tag.getTag(Tags.CRITERIA_BYPASSINVUL).value());
		}
		if (tag.hasTag(Tags.CRITERIA_BYPASSMAGIC))
		{
			this.select(this.checkboxBypassMagic);
			this.checkboxBypassMagic.setSelected(tag.getTag(Tags.CRITERIA_BYPASSMAGIC).value());
		}
		if (tag.hasTag(Tags.CRITERIA_ISEXPLOSION))
		{
			this.select(this.checkboxIsExplosion);
			this.checkboxIsExplosion.setSelected(tag.getTag(Tags.CRITERIA_ISEXPLOSION).value());
		}
		if (tag.hasTag(Tags.CRITERIA_ISFIRE))
		{
			this.select(this.checkboxIsFire);
			this.checkboxIsFire.setSelected(tag.getTag(Tags.CRITERIA_ISFIRE).value());
		}
		if (tag.hasTag(Tags.CRITERIA_ISMAGIC))
		{
			this.select(this.checkboxIsMagic);
			this.checkboxIsMagic.setSelected(tag.getTag(Tags.CRITERIA_ISMAGIC).value());
		}
		if (tag.hasTag(Tags.CRITERIA_ISPROJECTILE))
		{
			this.select(this.checkboxIsProjectile);
			this.checkboxIsProjectile.setSelected(tag.getTag(Tags.CRITERIA_ISPROJECTILE).value());
		}
		if (tag.hasTag(Tags.CRITERIA_SOURCE_ENTITY))
		{
			this.select(this.panelSource);
			this.panelSource.setupFrom(tag.getTag(Tags.CRITERIA_SOURCE_ENTITY));
		}
		if (tag.hasTag(Tags.CRITERIA_DIRECT_ENTITY))
		{
			this.select(this.panelDirect);
			this.panelDirect.setupFrom(tag.getTag(Tags.CRITERIA_DIRECT_ENTITY));
		}
		this.checkbox();
	}

}
