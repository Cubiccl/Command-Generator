package fr.cubiccl.generator.gameobject.registries.v112;

import fr.cubiccl.generator.gameobject.baseobjects.DefaultAdvancement;

public final class Advancements112
{

	public static final DefaultAdvancement AdventuringTime = new DefaultAdvancement("adventure", "adventuring_time", "diamond_boots");
	public static final DefaultAdvancement MonsterHunter = new DefaultAdvancement("adventure", "kill_a_mob", "iron_sword");
	public static final DefaultAdvancement MonstersHunted = new DefaultAdvancement("adventure", "kill_all_mobs", "diamond_sword");
	public static final DefaultAdvancement Adventure = new DefaultAdvancement("adventure", "root", "map");
	public static final DefaultAdvancement TakeAim = new DefaultAdvancement("adventure", "shoot_arrow", "bow");
	public static final DefaultAdvancement SweetDreams = new DefaultAdvancement("adventure", "sleep_in_bed", "bed");
	public static final DefaultAdvancement SniperDuel = new DefaultAdvancement("adventure", "sniper_duel", "arrow");
	public static final DefaultAdvancement HiredHelp = new DefaultAdvancement("adventure", "summon_iron_golem", "pumpkin");
	public static final DefaultAdvancement Postmortal = new DefaultAdvancement("adventure", "totem_of_undying", "totem_of_undying");
	public static final DefaultAdvancement WhatADeal = new DefaultAdvancement("adventure", "trade", "emerald");
	public static final DefaultAdvancement YouNeedAMint = new DefaultAdvancement("end", "dragon_breath", "dragon_breath");
	public static final DefaultAdvancement TheNextGeneration = new DefaultAdvancement("end", "dragon_egg", "dragon_egg");
	public static final DefaultAdvancement SkysTheLimit = new DefaultAdvancement("end", "elytra", "elytra");
	public static final DefaultAdvancement RemoteGateway = new DefaultAdvancement("end", "enter_end_gateway", "ender_pearl");
	public static final DefaultAdvancement TheCityAtTheEndOfTheGame = new DefaultAdvancement("end", "find_end_city", "purpur_block");
	public static final DefaultAdvancement FreeTheEnd = new DefaultAdvancement("end", "kill_dragon", "skull", 5);
	public static final DefaultAdvancement GreatViewFromUpHere = new DefaultAdvancement("end", "levitate", "shulker_shell");
	public static final DefaultAdvancement TheEndAgain = new DefaultAdvancement("end", "respawn_dragon", "end_crystal");
	public static final DefaultAdvancement TheEnd = new DefaultAdvancement("end", "root", "end_stone");
	public static final DefaultAdvancement ABalancedDiet = new DefaultAdvancement("husbandry", "balanced_diet", "apple");
	public static final DefaultAdvancement SeriousDedication = new DefaultAdvancement("husbandry", "break_diamond_hoe", "diamond_hoe");
	public static final DefaultAdvancement TwoByTwo = new DefaultAdvancement("husbandry", "breed_all_animals", "golden_carrot");
	public static final DefaultAdvancement TheParrotsAndTheBats = new DefaultAdvancement("husbandry", "breed_an_animal", "wheat");
	public static final DefaultAdvancement ASeedyPlace = new DefaultAdvancement("husbandry", "plant_seed", "wheat");
	public static final DefaultAdvancement Husbandry = new DefaultAdvancement("husbandry", "root", "hay_block");
	public static final DefaultAdvancement BestFriendsForever = new DefaultAdvancement("husbandry", "tame_an_animal", "lead");
	public static final DefaultAdvancement HowDidWeGetHere = new DefaultAdvancement("nether", "all_effects", "bucket");
	public static final DefaultAdvancement AFuriousCocktail = new DefaultAdvancement("nether", "all_potions", "milk_bucket");
	public static final DefaultAdvancement LocalBrewery = new DefaultAdvancement("nether", "brew_potion", "potion");
	public static final DefaultAdvancement BringHomeTheBeacon = new DefaultAdvancement("nether", "create_beacon", "beacon");
	public static final DefaultAdvancement Beaconator = new DefaultAdvancement("nether", "create_full_beacon", "beacon");
	public static final DefaultAdvancement SubspaceBubble = new DefaultAdvancement("nether", "fast_travel", "map");
	public static final DefaultAdvancement ATerribleFortress = new DefaultAdvancement("nether", "find_fortress", "nether_brick");
	public static final DefaultAdvancement SpookyScarySkeleton = new DefaultAdvancement("nether", "get_wither_skull", "skull", 1);
	public static final DefaultAdvancement IntoFire = new DefaultAdvancement("nether", "obtain_blaze_rod", "blaze_rod");
	public static final DefaultAdvancement ReturnToSender = new DefaultAdvancement("nether", "return_to_sender", "ghast_tear");
	public static final DefaultAdvancement Nether = new DefaultAdvancement("nether", "root", "nether_wart");
	public static final DefaultAdvancement WitheringHeights = new DefaultAdvancement("nether", "summon_wither", "nether_star");
	public static final DefaultAdvancement UneasyAlliance = new DefaultAdvancement("nether", "uneasy_alliance", "ghast_tear");
	public static final DefaultAdvancement ZombieDoctor = new DefaultAdvancement("story", "cure_zombie_villager", "golden_apple");
	public static final DefaultAdvancement NotTodayThankYou = new DefaultAdvancement("story", "deflect_arrow", "shield");
	public static final DefaultAdvancement Enchanter = new DefaultAdvancement("story", "enchant_item", "enchanted_book");
	public static final DefaultAdvancement TheEndQuestionMark = new DefaultAdvancement("story", "enter_the_end", "end_stone");
	public static final DefaultAdvancement WeNeedToGoDeeper = new DefaultAdvancement("story", "enter_the_nether", "flint_and_steel");
	public static final DefaultAdvancement EyeSpy = new DefaultAdvancement("story", "follow_ender_eye", "eye_of_ender");
	public static final DefaultAdvancement IceBucketChallenge = new DefaultAdvancement("story", "form_obsidian", "obsidian");
	public static final DefaultAdvancement IsntItIronPick = new DefaultAdvancement("story", "iron_tools", "iron_pickaxe");
	public static final DefaultAdvancement HotStuff = new DefaultAdvancement("story", "lava_bucket", "lava_bucket");
	public static final DefaultAdvancement Diamonds = new DefaultAdvancement("story", "mine_diamond", "diamond");
	public static final DefaultAdvancement StoneAge = new DefaultAdvancement("story", "mine_stone", "wooden_pickaxe");
	public static final DefaultAdvancement SuitUp = new DefaultAdvancement("story", "obtain_armor", "iron_chestplate");
	public static final DefaultAdvancement Minecraft = new DefaultAdvancement("story", "root", "grass");
	public static final DefaultAdvancement CoverMeWithDiamonds = new DefaultAdvancement("story", "shiny_gear", "diamond_chestplate");
	public static final DefaultAdvancement AcquireHardware = new DefaultAdvancement("story", "smelt_iron", "iron_ingot");
	public static final DefaultAdvancement GettingAnUpgrade = new DefaultAdvancement("story", "upgrade_tools", "stone_pickaxe");

	static
	{
		AdventuringTime.setCriteria("birch_forest_hills", "birch_forest_hills", "river", "swampland", "desert", "forest_hills", "redwood_tairga_hills",
				"taiga_cold", "mesa", "forest", "stone_beach", "ice_flats", "taiga_hills", "ice_mountains", "mesa_rock", "savanna", "plains", "frozen_river",
				"redwood_taiga", "cold_beach", "deep_ocean", "jungle_hills", "jungle_edge", "ocean", "mushroom_island_shote", "extreme_hills", "desert_hills",
				"jungle", "beaches", "savanna_rock", "taiga_cold_hills", "mesa_clear_rock", "roofed_forest", "taiga", "birch_forest", "mushroom_island",
				"extreme_hills_with_trees");
		MonsterHunter.setCriteria("cave_spider", "spider", "zombie_pigman", "enderman", "polar_bear", "blaze", "creeper", "evoker", "ghast", "guardian",
				"husk", "magma_cube", "shulker", "silverfish", "skeleton", "slime", "stray", "vindicator", "witch", "wither_skeleton", "zombie",
				"zombie_villager");
		MonstersHunted.setCriteria(MonsterHunter.criteria());
		Adventure.setCriteria("killed_something", "killed_by_something");
		TakeAim.setCriteria("shot_arrow");
		SweetDreams.setCriteria("slept_in_bed");
		SniperDuel.setCriteria("killed_skeleton");
		HiredHelp.setCriteria("summoned_golem");
		Postmortal.setCriteria("shot_arrow");
		WhatADeal.setCriteria("traded");
		YouNeedAMint.setCriteria("dragon_breath");
		TheNextGeneration.setCriteria("dragon_egg");
		SkysTheLimit.setCriteria("elytra");
		RemoteGateway.setCriteria("entered_end_gateway");
		TheCityAtTheEndOfTheGame.setCriteria("in_city");
		FreeTheEnd.setCriteria("killed_dragon");
		GreatViewFromUpHere.setCriteria("levitated");
		TheEndAgain.setCriteria("summoned_dragon");
		TheEnd.setCriteria("entered_end");
		ABalancedDiet.setCriteria("apple", "mushroom_stew", "bread", "porkchop", "cooked_porkchop", "golden_apple", "fish", "cooked_fish", "cookie", "melon",
				"beef", "cooked_beef", "chicken", "cooked_chicken", "rotten_flesh", "spider_eye", "carrot", "potato", "baked_potato", "poisonous_potato",
				"golden_carrot", "pumpkin_pie", "rabbit", "cooked_rabbit", "rabbit_stew", "mutton", "cooked_mutton", "chorus_fruit", "beetroot",
				"beetroot_soup");
		SeriousDedication.setCriteria("levitated");
		TwoByTwo.setCriteria("bred_horse", "bred_sheep", "bred_cow", "bred_mooshroom", "bred_pig", "bred_chicken", "bred_wolf", "bred_ocelot", "bred_rabbit",
				"bred_llama");
		TheParrotsAndTheBats.setCriteria(TwoByTwo.criteria());
		ASeedyPlace.setCriteria("wheat", "pumpkin_stem", "melon_stem", "beetroots", "nether_wart");
		Husbandry.setCriteria("consumed_item");
		BestFriendsForever.setCriteria("tamed_animal");
		HowDidWeGetHere.setCriteria("all_effects");
		AFuriousCocktail.setCriteria("all_effects");
		LocalBrewery.setCriteria("potion");
		BringHomeTheBeacon.setCriteria("beacon");
		Beaconator.setCriteria("beacon");
		SubspaceBubble.setCriteria("travelled");
		ATerribleFortress.setCriteria("fortress");
		SpookyScarySkeleton.setCriteria("wither_skull");
		IntoFire.setCriteria("blaze_rod");
		ReturnToSender.setCriteria("killed_ghast");
		Nether.setCriteria("entered_nether");
		WitheringHeights.setCriteria("summoned");
		UneasyAlliance.setCriteria("killed_ghast");
		ZombieDoctor.setCriteria("cured_zombie");
		NotTodayThankYou.setCriteria("deflected_projectile");
		Enchanter.setCriteria("enchanted_item");
		TheEndQuestionMark.setCriteria("entered_end");
		WeNeedToGoDeeper.setCriteria("entered_nether");
		EyeSpy.setCriteria("in_stronghold");
		IceBucketChallenge.setCriteria("obsidian");
		IsntItIronPick.setCriteria("iron_pickaxe");
		HotStuff.setCriteria("lava_bucket");
		Diamonds.setCriteria("diamond");
		StoneAge.setCriteria("get_stone");
		SuitUp.setCriteria("iron_helmet", "iron_chestplate", "iron_leggings", "iron_boots");
		Minecraft.setCriteria("crafting_table");
		CoverMeWithDiamonds.setCriteria("diamond_helmet", "diamond_chestplate", "diamond_leggings", "diamond_boots");
		AcquireHardware.setCriteria("iron");
		GettingAnUpgrade.setCriteria("stone_pickaxe");
	}

	private Advancements112()
	{}

}
