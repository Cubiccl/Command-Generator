package fr.cubiccl.generator.gui.component.panel.gameobject;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import fr.cubiccl.generator.gameobject.baseobjects.Achievement;
import fr.cubiccl.generator.gameobject.baseobjects.Block;
import fr.cubiccl.generator.gameobject.baseobjects.Entity;
import fr.cubiccl.generator.gameobject.baseobjects.Item;
import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
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

		this.panelAchievement = new PanelAchievement();
		this.comboboxDetail = new OptionCombobox("stat", STAT);
		this.panelBlock = new PanelBlock(null, false, false, true);
		this.panelItem = new PanelItem(null, false, false, true, ObjectRegistry.items.list("break"));
		this.panelEntity = new PanelEntity(null, false);

		this.comboboxBase.addActionListener(this);
		this.comboboxBase.setSelectedIndex(0);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == this.comboboxBase) this.onBaseChange();
	}

	public String getCriteria()
	{
		String base = this.comboboxBase.getValue();
		if (base.equals("achievement")) return base + "." + this.panelAchievement.getAchievement().id;
		if (base.contains("Item") || base.equals("stat.drop") || base.equals("stat.pickup")) return base + "." + this.panelItem.selectedItem().id();
		if (base.toLowerCase().contains("entity")) return base + "." + this.panelEntity.selectedEntity().id;
		if (base.equals("stat.mineBlock")) return base + "." + this.panelBlock.selectedBlock().id();
		if (base.equals("stat") || base.equals("teamKill") || base.equals("killedByTeam") || base.equals("stat")) return base + "."
				+ this.comboboxDetail.getValue();
		return this.comboboxBase.getValue();
	}

	private void onBaseChange()
	{
		String base = this.comboboxBase.getValue();
		if (base.equals("achievement"))
		{
			Achievement previous = this.panelAchievement.getAchievement();
			this.setCurrentDetail(this.panelAchievement = new PanelAchievement());
			this.panelAchievement.setSelection(previous);
		} else if (base.equals("stat")) this.setCurrentDetail(this.comboboxDetail = new OptionCombobox("stat", STAT));
		else if (base.equals("stat.breakItem"))
		{
			Item previous = this.panelItem.selectedItem();
			this.setCurrentDetail(this.panelItem = new PanelItem(null, false, false, true, ObjectRegistry.items.list("break")));
			this.panelItem.setEnabledContent(false, false);
			this.panelItem.setItem(previous);
		} else if (base.equals("stat.useItem"))
		{
			Item previous = this.panelItem.selectedItem();
			this.setCurrentDetail(this.panelItem = new PanelItem(null, false, false, true, ObjectRegistry.items.list("use")));
			this.panelItem.setEnabledContent(false, false);
			this.panelItem.setItem(previous);
		} else if (base.equals("stat.craftItem"))
		{
			Item previous = this.panelItem.selectedItem();
			this.setCurrentDetail(this.panelItem = new PanelItem(null, false, false, true, ObjectRegistry.items.list("craft")));
			this.panelItem.setEnabledContent(false, false);
			this.panelItem.setItem(previous);
		} else if (base.equals("stat.drop") || base.equals("stat.pickup"))
		{
			Item previous = this.panelItem.selectedItem();
			this.setCurrentDetail(this.panelItem = new PanelItem(null, false, false, true, ObjectRegistry.items.list(ObjectRegistry.SORT_NUMERICALLY)));
			this.panelItem.setEnabledContent(false, false);
			this.panelItem.setItem(previous);
		} else if (base.equals("stat.mineBlock"))
		{
			Block previous = this.panelBlock.selectedBlock();
			this.setCurrentDetail(this.panelBlock = new PanelBlock(null, false, false, true));
			this.panelBlock.setHasData(false);
			this.panelBlock.setBlock(previous);
		} else if (base.equals("killedByTeam") || base.equals("teamKill"))
		{
			String previous = this.comboboxDetail.getValue();
			this.setCurrentDetail(this.comboboxDetail = new OptionCombobox("color", COLORS));
			this.comboboxDetail.setValue(previous);
		} else if (base.toLowerCase().contains("entity"))
		{
			Entity previous = this.panelEntity.selectedEntity();
			this.setCurrentDetail(this.panelEntity = new PanelEntity(null, false));
			this.panelEntity.setEntity(previous);
		} else this.setCurrentDetail(null);
	}

	private void setCurrentDetail(Component component)
	{
		this.gbc.gridy = 1;
		if (this.currentDetail != null) this.remove(this.currentDetail);
		this.currentDetail = component;
		if (this.currentDetail != null) this.add(this.currentDetail, this.gbc);
		this.updateUI();
	}

	public void setupFrom(String criteria)
	{
		if (!criteria.contains("."))
		{
			this.comboboxBase.setValue(criteria);
			this.onBaseChange();
			return;
		}
		this.comboboxBase.setValue(criteria.substring(0, criteria.lastIndexOf('.')));
		this.onBaseChange();
		String id = criteria.substring(criteria.lastIndexOf('.') + 1, criteria.length());
		if (criteria.startsWith("achievement.")) this.panelAchievement.setSelection(ObjectRegistry.achievements.find(id));
		else if (criteria.contains("Item") || criteria.startsWith("stat.drop.") || criteria.startsWith("stat.pickup.")) this.panelItem
				.setItem(ObjectRegistry.items.find(id));
		else if (criteria.toLowerCase().contains("entity")) this.panelEntity.setEntity(ObjectRegistry.entities.find(id));
		else if (criteria.startsWith("stat.mineBlock")) this.panelBlock.setBlock(ObjectRegistry.blocks.find(id));
		else if (criteria.startsWith("stat") || criteria.startsWith("teamKill") || criteria.startsWith("killedByTeam")) this.comboboxDetail.setValue(id);
	}
}
