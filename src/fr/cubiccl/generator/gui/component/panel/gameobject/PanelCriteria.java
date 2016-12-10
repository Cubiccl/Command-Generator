package fr.cubiccl.generator.gui.component.panel.gameobject;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import fr.cubiccl.generator.gameobject.ObjectRegistry;
import fr.cubiccl.generator.gui.component.combobox.OptionCombobox;
import fr.cubiccl.generator.gui.component.panel.CGPanel;

public class PanelCriteria extends CGPanel implements ActionListener
{
	public static final String[] BASE =
	{ "achievement", "air", "armor", "deathCount", "dummy", "food", "health", "killedByTeam", "level", "playerKillCount", "stat", "stat.breakItem",
			"stat.craftItem", "stat.drop", "stat.entityKilledBy", "stat.mineBlock", "stat.killEntity", "stat.pickup", "stat.useItem", "teamkill",
			"totalKillCount", "trigger", "xp" }, COLORS =
	{ "aqua", "black", "blue", "dark_aqua", "dark_blue", "dark_gray", "dark_green", "dark_purple", "dark_red", "gold", "gray", "green", "light_purple", "red",
			"yellow", "white" }, STAT =
	{ "animalsBred", "armorCleaned", "aviateOneCm", "bannerCleaned", "beaconInteraction", "boatOneCm", "brewingstandInteraction", "cakeSlicesEaten",
			"cauldronFilled", "cauldronUsed", "chestOpened", "climbOneCm", "craftingTableInteraction", "crouchOneCm", "damageDealt", "damageTaken", "deaths",
			"dispenserInspected", "diveOneCm", "drop", "dropperInspected", "enderchestOpened", "fallOneCm", "fishCaught", "flowerPotted", "flyOneCm",
			"furnaceInteraction", "hopperInspected", "horseOneCm", "itemEnchanted", "jump", "junkFished", "leaveGame", "minecartOneCm", "mobKills",
			"noteblockPlayed", "noteblockTuned", "pigOneCm", "playerKills", "playOneMinute", "recordPlayed", "sneakTime", "sprintOneCm", "swimOneCm",
			"talkedToVillager", "timeSinceDeath", "sleepInBed", "tradedWithVillager", "trappedChestTriggered", "treasureFished", "walkOneCm" };
	private static final long serialVersionUID = 32487120076554529L;

	private OptionCombobox comboboxBase, comboboxDetail;
	private Component currentDetail;
	private PanelAchievement panelAchievement;
	private PanelBlock panelBlock;
	private PanelEntity panelEntity;
	private PanelItem panelItem;

	public PanelCriteria()
	{
		this("score.criteria");
	}

	public PanelCriteria(String titleId)
	{
		super(titleId);
		GridBagConstraints gbc = this.createGridBagLayout();
		this.add(this.comboboxBase = new OptionCombobox("criteria", BASE), gbc);
		gbc.gridy = 2;

		this.comboboxBase.addActionListener(this);
		this.comboboxBase.setSelectedIndex(0);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == this.comboboxBase)
		{
			String base = this.comboboxBase.getValue();
			if (base.equals("achievement")) this.setCurrentDetail(this.panelAchievement = new PanelAchievement());
			else if (base.equals("stat")) this.setCurrentDetail(this.comboboxDetail = new OptionCombobox("stat", STAT));
			else if (base.equals("stat.breakItem"))
			{
				this.setCurrentDetail(this.panelItem = new PanelItem(null, ObjectRegistry.getItemList("break")));
				this.panelItem.setEnabledContent(false, false);
			} else if (base.equals("stat.useItem"))
			{
				this.setCurrentDetail(this.panelItem = new PanelItem(null, ObjectRegistry.getItemList("use")));
				this.panelItem.setEnabledContent(false, false);
			} else if (base.equals("stat.craftItem"))
			{
				this.setCurrentDetail(this.panelItem = new PanelItem(null, ObjectRegistry.getItemList("craft")));
				this.panelItem.setEnabledContent(false, false);
			} else if (base.equals("stat.drop") || base.equals("stat.pickup"))
			{
				this.setCurrentDetail(this.panelItem = new PanelItem(null));
				this.panelItem.setEnabledContent(false, false);
			} else if (base.equals("stat.mineBlock"))
			{
				this.setCurrentDetail(this.panelBlock = new PanelBlock(null));
				this.panelBlock.setHasData(false);
			} else if (base.equals("killedByTeam") || base.equals("teamKill")) this.setCurrentDetail(this.comboboxDetail = new OptionCombobox("color", COLORS));
			else if (base.toLowerCase().contains("entity")) this.setCurrentDetail(this.panelEntity = new PanelEntity(null));
			else this.setCurrentDetail(null);
		}
	}

	public String getCriteria()
	{
		String base = this.comboboxBase.getValue();
		if (base.equals("achievement")) return base + "." + this.panelAchievement.getAchievement().id;
		if (base.contains("Item") || base.equals("stat.drop") || base.equals("stat.pickup")) return base + "." + this.panelItem.selectedItem().idString;
		if (base.toLowerCase().contains("entity")) return base + "." + this.panelEntity.selectedEntity().id;
		if (base.equals("stat.mineBlock")) return base + "." + this.panelBlock.selectedBlock().idString;
		if (base.equals("stat") || base.equals("teamKill") || base.equals("killedByTeam") || base.equals("stat")) return base + "."
				+ this.comboboxDetail.getValue();
		return this.comboboxBase.getValue();
	}

	private void setCurrentDetail(Component component)
	{
		this.gbc.gridy = 1;
		if (this.currentDetail != null) this.remove(this.currentDetail);
		this.currentDetail = component;
		if (this.currentDetail != null) this.add(this.currentDetail, this.gbc);
		this.updateUI();
	}

}
