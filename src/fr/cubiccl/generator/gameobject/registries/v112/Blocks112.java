package fr.cubiccl.generator.gameobject.registries.v112;

import fr.cubiccl.generator.gameobject.baseobjects.Block;
import fr.cubiccl.generator.gameobject.baseobjects.BlockState;
import fr.cubiccl.generator.gameobject.baseobjects.block.*;

public final class Blocks112
{

	public static final Block Air = new Block(0, "air");
	public static final Block Stone = new Block(1, "stone");
	public static final Block Grass = new Block(2, "grass");
	public static final Block Dirt = new Block(3, "dirt");
	public static final Block Cobblestone = new Block(4, "cobblestone");
	public static final Block Planks = new BlockWood(5, "planks");
	public static final Block Sapling = new BlockWood(6, "sapling");
	public static final Block Bedrock = new Block(7, "bedrock");
	public static final Block WaterFlowing = new BlockLiquid(8, "flowing_water");
	public static final Block Water = new BlockLiquid(9, "water");
	public static final Block LavaFlowing = new BlockLiquid(10, "flowing_lava");
	public static final Block Lava = new BlockLiquid(11, "lava");
	public static final Block Sand = new Block(12, "sand");
	public static final Block Gravel = new Block(13, "gravel");
	public static final Block OreGold = new Block(14, "gold_ore");
	public static final Block OreIron = new Block(15, "iron_ore");
	public static final Block OreCoal = new Block(16, "coal_ore");
	public static final Block Wood = new Block(17, "log");
	public static final Block Leaves = new Block(18, "leaves");
	public static final Block Sponge = new Block(19, "sponge");
	public static final Block Glass = new Block(20, "glass");
	public static final Block OreLapis = new Block(21, "lapis_ore");
	public static final Block BlockLapis = new Block(22, "lapis_block");
	public static final Block Dispenser = new BlockFacing(23, "dispenser").setTextureType(-1);
	public static final Block Sandstone = new Block(24, "sandstone");
	public static final Block NoteBlock = new Block(25, "noteblock");
	public static final Block Bed = new Block(26, "bed").setTextureType(-8);
	public static final Block RailGolden = new BlockRail(27, "golden_rail");
	public static final Block RailDetector = new BlockRail(28, "detector_rail");
	public static final Block PistonSticky = new BlockFacing(29, "sticky_piston").setTextureType(-1);
	public static final Block Cobweb = new Block(30, "web");
	public static final Block GrassTall = new Block(31, "tallgrass");
	public static final Block Deadbush = new Block(32, "deadbush");
	public static final Block Piston = new BlockFacing(33, "piston").setTextureType(-1);
	public static final Block PistonHead = new BlockFacing(34, "piston_head");
	public static final Block Wool = new BlockStained(35, "wool");
	public static final Block PistonExtension = new BlockFacing(36, "piston_extension").setTextureType(-1);
	public static final Block FlowerYellow = new Block(37, "yellow_flower");
	public static final Block FlowerRed = new Block(38, "red_flower");
	public static final Block MushroomBrown = new Block(39, "brown_mushroom");
	public static final Block MushroomRed = new Block(40, "red_mushroom");
	public static final Block BlockGold = new Block(41, "gold_block");
	public static final Block BlockIron = new Block(42, "iron_block");
	public static final Block SlabDoubleStone = new Block(43, "double_stone_slab");
	public static final Block SlabStone = new Block(44, "stone_slab").setTextureType(8);
	public static final Block Brick = new Block(45, "brick_block");
	public static final Block TNT = new Block(46, "tnt").setTextureType(-1);
	public static final Block Bookshelf = new Block(47, "bookshelf");
	public static final Block CobblestoneMossy = new Block(48, "mossy_cobblestone");
	public static final Block Obsidian = new Block(49, "obsidian");
	public static final Block Torch = new Block(50, "torch");
	public static final Block Fire = new BlockNumbered(51, "fire");
	public static final Block MobSpawner = new Block(52, "mob_spawner");
	public static final Block StairsOak = new BlockStairs(53, "oak_stairs");
	public static final Block Chest = new BlockFurniture(54, "chest");
	public static final Block RedstoneWire = new BlockNumbered(55, "redstone_wire");
	public static final Block OreDiamond = new Block(56, "diamond_ore");
	public static final Block BlockDiamond = new Block(57, "diamond_block");
	public static final Block CraftingTable = new Block(58, "crafting_table");
	public static final Block Wheat = new Block(59, "wheat");
	public static final Block Farmland = new Block(60, "farmland").setTextureType(-7);
	public static final Block Furnace = new BlockFurniture(61, "furnace");
	public static final Block FurnaceLit = new BlockFurniture(62, "lit_furnace");
	public static final Block SignStanding = new Block(63, "standing_sign").setTextureType(-1);
	public static final Block DoorOak = new BlockDoor(64, "wooden_door");
	public static final Block Ladder = new BlockFurniture(65, "ladder");
	public static final Block Rail = new Block(66, "rail");
	public static final Block StairsCobblestone = new BlockStairs(67, "stone_stairs");
	public static final Block SignWall = new BlockFurniture(68, "wall_sign");
	public static final Block Lever = new Block(69, "lever").setTextureType(-1);
	public static final Block PressurePlateStone = new Block(70, "stone_pressure_plate").setTextureType(-1);
	public static final Block DoorIron = new BlockDoor(71, "iron_door");
	public static final Block PressurePlateOak = new Block(72, "wooden_pressure_plate").setTextureType(-1);
	public static final Block OreRedstone = new Block(73, "redstone_ore");
	public static final Block OreRedstoneLit = new Block(74, "lit_redstone_ore");
	public static final Block RedstoneTorchOff = new Block(75, "unlit_redstone_torch").setTextureType(-1);
	public static final Block RedstoneTorch = new Block(76, "redstone_torch").setTextureType(-1);
	public static final Block ButtonStone = new Block(77, "stone_button");
	public static final Block SnowLayer = new Block(78, "snow_layer");
	public static final Block Ice = new Block(79, "ice");
	public static final Block Snow = new Block(80, "snow");
	public static final Block Cactus = new BlockNumbered(81, "cactus");
	public static final Block Clay = new Block(82, "clay");
	public static final Block SugarCane = new BlockNumbered(83, "reeds");
	public static final Block Jukebox = new Block(84, "jukebox").setTextureType(-1);
	public static final Block FenceOak = new Block(85, "fence");
	public static final Block Pumpkin = new Block(86, "pumpkin").setTextureType(-1);
	public static final Block Netherrack = new Block(87, "netherrack");
	public static final Block SoulSand = new Block(88, "soul_sand");
	public static final Block Glowstone = new Block(89, "glowstone");
	public static final Block PortalNether = new Block(90, "portal").setTextureType(-1);
	public static final Block PumpkinLit = new Block(91, "lit_pumpkin").setTextureType(-1);
	public static final Block Cake = new Block(92, "cake");
	public static final Block RepeaterOff = new Block(93, "unpowered_repeater");
	public static final Block RepeaterOn = new Block(94, "powered_repeater");
	public static final Block GlassColoured = new BlockStained(95, "stained_glass");
	public static final Block TrapdoorOak = new Block(96, "trapdoor");
	public static final Block StoneSilverfish = new Block(97, "monster_egg");
	public static final Block StoneBrick = new Block(98, "stonebrick");
	public static final Block MushroomBlockBrown = new Block(99, "brown_mushroom_block").setTextureType(-1);
	public static final Block MushroomBlockRed = new Block(100, "red_mushroom_block");
	public static final Block IronBars = new Block(101, "iron_bars");
	public static final Block GlassPane = new Block(102, "glass_pane");
	public static final Block MelonBlock = new Block(103, "melon_block");
	public static final Block PumpkinStem = new BlockNumbered(104, "pumpkin_stem");
	public static final Block MelonStem = new Block(105, "melon_stem");
	public static final Block Vines = new Block(106, "vine").setTextureType(-1);
	public static final Block FenceGateOak = new BlockFenceGate(107, "fence_gate");
	public static final Block StairsBrick = new BlockStairs(108, "brick_stairs");
	public static final Block StairsStoneBrick = new BlockStairs(109, "stone_brick_stairs");
	public static final Block Mycelium = new Block(110, "mycelium");
	public static final Block Lilypad = new Block(111, "waterlily");
	public static final Block NetherBrick = new Block(112, "nether_brick");
	public static final Block FenceNetherBrick = new Block(113, "nether_brick_fence");
	public static final Block StairsNetherBrick = new BlockStairs(114, "nether_brick_stairs");
	public static final Block NetherWart = new BlockNumbered(115, "nether_wart").setTextureType(0);
	public static final Block EnchantingTable = new Block(116, "enchanting_table");
	public static final Block BrewingStand = new Block(117, "brewing_stand");
	public static final Block Cauldron = new Block(118, "cauldron").setTextureType(-1);
	public static final Block PortalEnd = new Block(119, "end_portal");
	public static final Block PortalEndFrame = new Block(120, "end_portal_frame").setTextureType(-1);
	public static final Block EndStone = new Block(121, "end_stone");
	public static final Block DragonEgg = new Block(122, "dragon_egg");
	public static final Block RedstoneLamp = new Block(123, "redstone_lamp");
	public static final Block RedstoneLampLit = new Block(124, "lit_redstone_lamp");
	public static final Block SlabDoubleWood = new BlockWood(125, "double_wooden_slab");
	public static final Block SlabWood = new BlockWood(126, "wooden_slab");
	public static final Block CocoaBeans = new Block(127, "cocoa").setTextureType(-4);
	public static final Block StairsSandstone = new BlockStairs(128, "sandstone_stairs");
	public static final Block OreEmerald = new Block(129, "emerald_ore");
	public static final Block ChestEnder = new BlockFurniture(130, "ender_chest");
	public static final Block TripwireHook = new Block(131, "tripwire_hook").setTextureType(-1);
	public static final Block Tripwire = new Block(132, "tripwire");
	public static final Block BlockEmerald = new Block(133, "emerald_block");
	public static final Block StairsSpruce = new BlockStairs(134, "spruce_stairs");
	public static final Block StairsBirch = new BlockStairs(135, "birch_stairs");
	public static final Block StairsJungle = new BlockStairs(136, "jungle_stairs");
	public static final Block Command = new BlockFacing(137, "command_block");
	public static final Block Beacon = new Block(138, "beacon");
	public static final Block CobblestoneWall = new Block(139, "cobblestone_wall");
	public static final Block FlowerPot = new Block(140, "flower_pot");
	public static final Block Carrots = new BlockNumbered(141, "carrots").setTextureType(0);
	public static final Block Potatoes = new BlockNumbered(142, "potatoes").setTextureType(0);
	public static final Block ButtonOak = new Block(143, "wooden_button");
	public static final Block Skull = new Block(144, "skull").setTextureType(-1);
	public static final Block Anvil = new Block(145, "anvil").setTextureType(-4);
	public static final Block ChestTrapped = new BlockFurniture(146, "trapped_chest");
	public static final Block PressurePlateGold = new BlockNumbered(147, "light_weighted_pressure_plate");
	public static final Block PressurePlateIron = new BlockNumbered(148, "heavy_weighter_pressure_plate");
	public static final Block ComparatorOff = new Block(149, "unpowered_comparator");
	public static final Block ComparatorOn = new Block(150, "powered_comparator");
	public static final Block DaylightDetector = new Block(151, "daylight_detector").setTextureType(-1);
	public static final Block BlockRedstone = new Block(152, "redstone_block");
	public static final Block OreQuartz = new Block(153, "quartz_ore");
	public static final Block Hopper = new Block(154, "hopper").setTextureType(-1);
	public static final Block BlockQuartz = new Block(155, "quartz_block");
	public static final Block StairsQuartz = new BlockStairs(156, "quartz_stairs");
	public static final Block RailActivator = new BlockRail(157, "activator_rail");
	public static final Block Dropper = new BlockFacing(158, "dropper").setTextureType(-1);
	public static final Block TerracottaColoured = new BlockStained(159, "stained_hardened_clay");
	public static final Block GlassPaneColoured = new BlockStained(160, "stained_glass_pane");
	public static final Block Leaves2 = new Block(161, "leaves2");
	public static final Block Wood2 = new Block(162, "log2");
	public static final Block StairsAcacia = new BlockStairs(163, "acacia_stairs");
	public static final Block StairsDarkOak = new BlockStairs(164, "dark_oak_stairs");
	public static final Block Slime = new Block(165, "slime");
	public static final Block Barrier = new Block(166, "barrier");
	public static final Block TrapdoorIron = new Block(167, "iron_trapdoor");
	public static final Block Prismarine = new Block(168, "prismarine");
	public static final Block SeaLantern = new Block(169, "sea_lantern");
	public static final Block Hay = new Block(170, "hay_block");
	public static final Block Carpet = new BlockStained(171, "carpet");
	public static final Block Terracotta = new Block(172, "hardened_clay");
	public static final Block BlockCoal = new Block(173, "coal_block");
	public static final Block IcePacked = new Block(174, "packed_ice");
	public static final Block FlowerDouble = new Block(175, "double_plant").setTextureType(8);
	public static final Block BannerStanding = new Block(176, "standing_banner").setTextureType(-1);
	public static final Block BannerWall = new BlockFurniture(177, "wall_banner");
	public static final Block DaylightDetectorInverted = new Block(178, "daylight_detector_inverted").setTextureType(-1);
	public static final Block SandstoneRed = new Block(179, "red_sandstone");
	public static final Block StairsSandstoneRed = new BlockStairs(180, "red_sandstone_stairs");
	public static final Block SlabDoubleStone2 = new Block(181, "double_stone_slab2");
	public static final Block SlabStone2 = new Block(182, "stone_slab2").setTextureType(-1);
	public static final Block FenceGateSpruce = new BlockFenceGate(183, "spruce_fence_gate");
	public static final Block FenceGateBirch = new BlockFenceGate(184, "birch_fence_gate");
	public static final Block FenceGateJungle = new BlockFenceGate(185, "jungle_fence_gate");
	public static final Block FenceGateDarkOak = new BlockFenceGate(186, "dark_oak_fence_gate");
	public static final Block FenceGateAcacia = new BlockFenceGate(187, "acacia_fence_gate");
	public static final Block FenceSpruce = new Block(188, "spruce_fence");
	public static final Block FenceBirch = new Block(189, "birch_fence");
	public static final Block FenceJungle = new Block(190, "jungle_fence");
	public static final Block FenceDarkOak = new Block(191, "dark_oak_fence");
	public static final Block FenceAcacia = new Block(192, "acacia_fence");
	public static final Block DoorSpruce = new BlockDoor(193, "spruce_door");
	public static final Block DoorBirch = new BlockDoor(194, "birch_door");
	public static final Block DoorJungle = new BlockDoor(195, "jungle_door");
	public static final Block DoorAcacia = new BlockDoor(196, "acacia_door");
	public static final Block DoorDarkOak = new BlockDoor(197, "dark_oak_door");
	public static final Block EndRod = new BlockFacing(198, "end_rod");
	public static final Block ChorusPlant = new Block(199, "chorus_plant");
	public static final Block ChorusFlower = new Block(200, "chorus_flower").setTextureType(-1);
	public static final Block PurpurBlock = new Block(201, "purpur_block");
	public static final Block PurpurPillar = new Block(202, "purpur_pillar");
	public static final Block StairsPurpur = new BlockStairs(203, "purpur_stairs");
	public static final Block SlabDoublePurpur = new Block(204, "purpur_double_slab");
	public static final Block SlabPurpur = new Block(205, "purpur_slab").setTextureType(-1);
	public static final Block EndBricks = new Block(206, "end_bricks");
	public static final Block Beetroots = new BlockNumbered(207, "beetroots").setTextureType(0);
	public static final Block GrassPath = new Block(208, "grass_path");
	public static final Block PortalEndGateway = new Block(209, "end_gateway");
	public static final Block CommandRepeating = new BlockFacing(210, "repeating_command_block");
	public static final Block CommandChain = new BlockFacing(211, "chain_command_block");
	public static final Block IceFrosted = new Block(212, "frosted_ice");
	public static final Block Magma = new Block(213, "magma");
	public static final Block BlockNetherWart = new Block(214, "nether_wart_block");
	public static final Block NetherBrickRed = new Block(215, "red_nether_brick");
	public static final Block BlockBone = new Block(216, "bone_block");
	public static final Block StructureVoid = new Block(217, "structure_void");
	public static final Block Observer = new BlockFacing(218, "observer").setTextureType(-1);
	public static final Block ShulkerBoxWhite = new BlockFacing(219, "white_shulker_box");
	public static final Block ShulkerBoxOrange = new BlockFacing(220, "orange_shulker_box");
	public static final Block ShulkerBoxMagenta = new BlockFacing(221, "magenta_shulker_box");
	public static final Block ShulkerBoxSky = new BlockFacing(222, "light_blue_shulker_box");
	public static final Block ShulkerBoxYellow = new BlockFacing(223, "yellow_shulker_box");
	public static final Block ShulkerBoxLime = new BlockFacing(224, "lime_shulker_box");
	public static final Block ShulkerBoxPink = new BlockFacing(225, "pink_shulker_box");
	public static final Block ShulkerBoxGray = new BlockFacing(226, "gray_shulker_box");
	public static final Block ShulkerBoxSilver = new BlockFacing(227, "silver_shulker_box");
	public static final Block ShulkerBoxCyan = new BlockFacing(228, "cyan_shulker_box");
	public static final Block ShulkerBoxPurple = new BlockFacing(229, "purple_shulker_box");
	public static final Block ShulkerBoxBlue = new BlockFacing(230, "blue_shulker_box");
	public static final Block ShulkerBoxBrown = new BlockFacing(231, "brown_shulker_box");
	public static final Block ShulkerBoxGreen = new BlockFacing(232, "green_shulker_box");
	public static final Block ShulkerBoxRed = new BlockFacing(233, "red_shulker_box");
	public static final Block ShulkerBoxBlack = new BlockFacing(234, "black_shulker_box");
	public static final Block TerracottaGlazedWhite = new BlockTerracotta(235, "white_glazed_terracotta");
	public static final Block TerracottaGlazedOrange = new BlockTerracotta(236, "orange_glazed_terracotta");
	public static final Block TerracottaGlazedMagenta = new BlockTerracotta(237, "magenta_glazed_terracotta");
	public static final Block TerracottaGlazedSky = new BlockTerracotta(238, "light_blue_glazed_terracotta");
	public static final Block TerracottaGlazedYellow = new BlockTerracotta(239, "yellow_glazed_terracotta");
	public static final Block TerracottaGlazedLime = new BlockTerracotta(240, "lime_glazed_terracotta");
	public static final Block TerracottaGlazedPink = new BlockTerracotta(241, "pink_glazed_terracotta");
	public static final Block TerracottaGlazedGray = new BlockTerracotta(242, "gray_glazed_terracotta");
	public static final Block TerracottaGlazedSilver = new BlockTerracotta(243, "silver_glazed_terracotta");
	public static final Block TerracottaGlazedCyan = new BlockTerracotta(244, "cyan_glazed_terracotta");
	public static final Block TerracottaGlazedPurple = new BlockTerracotta(245, "purple_glazed_terracotta");
	public static final Block TerracottaGlazedBlue = new BlockTerracotta(246, "blue_glazed_terracotta");
	public static final Block TerracottaGlazedBrown = new BlockTerracotta(247, "brown_glazed_terracotta");
	public static final Block TerracottaGlazedGreen = new BlockTerracotta(248, "green_glazed_terracotta");
	public static final Block TerracottaGlazedRed = new BlockTerracotta(249, "red_glazed_terracotta");
	public static final Block TerracottaGlazedBlack = new BlockTerracotta(250, "black_glazed_terracotta");
	public static final Block Concrete = new BlockStained(251, "concrete");
	public static final Block ConcretePowder = new BlockStained(252, "concrete_powder");
	public static final Block Structure = new Block(255, "structure_block");

	static
	{
		variant(Stone, "stone", "granite", "smooth_granite", "diorite", "smooth_diorite", "andesite", "smooth_andesite");
		bool(Grass, "snowy");
		variant(Dirt, "dirt", "coarse_dirt", "podzol");
		bool(Dirt, "snowy");
		numbered(Sapling, "age", 8, 1);
		variant(Sand, "sand", "red_sand");
		log(Wood);
		leaves(Leaves);
		bool(Sponge, "wet");
		bool(Dispenser, "triggered", 8);
		variant(Sandstone, "type", 1, "sandstone", "chiseled_sandstone", "smooth_sandstone");
		variant(Bed, "facing", 1, "south", "west", "north", "east");
		bool(Bed, "occupied", 4);
		variant(Bed, "part", 8, "head", "foot");
		bool(PistonSticky, "extended", 8);
		variant(GrassTall, "type", 1, "dead_bush", "tall_grass", "fern");
		bool(Piston, "extended", 8);
		bool(PistonHead, "short", -1);
		variant(PistonHead, "type", 8, "normal", "sticky");
		bool(PistonExtension, "extended", 8);
		variant(FlowerYellow, "type", -1, "dandelion");
		variant(FlowerRed, "type", 1, "poppy", "blue_orchid", "allium", "houstonia", "red_tulip", "white_tulip", "pink_tulip", "oxeye_daisy");
		variant(SlabDoubleStone, "stone", "sandstone", "wood_old", "cobblestone", "brick", "stone_brick", "nether_brick", "quartz");
		bool(SlabDoubleStone, "seamless", 8);
		SlabDoubleStone.addUnusedDamage(10, 11, 12, 13, 14);
		variant(SlabStone, "stone", "sandstone", "wood_old", "cobblestone", "brick", "stone_brick", "nether_brick", "quartz");
		variant(SlabStone, "half", 8, "bottom", "top");
		bool(TNT, "explode");
		torch(Torch);
		connecting(Fire);
		connecting(RedstoneWire);
		numbered(RedstoneWire, "power", 1, 15);
		numbered(Wheat, "age", 1, 7);
		numbered(Farmland, "moisture", 1, 7);
		numbered(SignStanding, "rotation", 1, 15);
		variant(Rail, "shape", 1, "north_south", "east_west", "ascending_east", "ascending_west", "ascending_north", "ascending_south", "south_east",
				"south_west", "north_west", "north_east");
		bool(Lever, "powered", 8);
		variant(Lever, "facing", 1, "up_x", "east", "west", "south", "north", "bottom_z", "bottom_x", "up_z");
		bool(PressurePlateStone, "powered");
		bool(PressurePlateOak, "powered");
		torch(RedstoneTorchOff);
		torch(RedstoneTorch);
		button(ButtonStone);
		SnowLayer.addState(new BlockState("layers", BlockState.INTEGER, 1, "1", "2", "3", "4", "5", "6", "7", "8"));
		numbered(Cactus, "age", 1, 15);
		numbered(SugarCane, "age", 1, 15);
		bool(Jukebox, "has_record");
		fence(FenceOak);
		variant(Pumpkin, "facing", 1, "south", "west", "north", "east");
		variant(PortalNether, "axis", 1, "x", "z");
		variant(PumpkinLit, "facing", 1, "south", "west", "north", "east");
		numbered(Cake, "bites", 1, 6);
		repeater(RepeaterOff);
		repeater(RepeaterOn);
		trapdoor(TrapdoorOak);
		variant(StoneSilverfish, "stone", "cobblestone", "stone_brick", "mossy_brick", "cracked_brick", "chiseled_brick");
		variant(StoneBrick, "stonebrick", "mossy_stonebrick", "cracked_stonebrick", "chiseled_stonebrick");
		mushroom(MushroomBlockBrown);
		mushroom(MushroomBlockRed);
		fence(IronBars);
		fence(GlassPane);
		stem(PumpkinStem);
		stem(MelonStem);
		connecting(Vines);
		bool(Mycelium, "snowy");
		fence(FenceNetherBrick);
		numbered(NetherWart, "age", 1, 3);
		bool(BrewingStand, "has_bottle_1", -1);
		bool(BrewingStand, "has_bottle_2", -1);
		bool(BrewingStand, "has_bottle_3", -1);
		numbered(Cauldron, "level", 1, 3);
		variant(PortalEndFrame, "facing", 1, "south", "west", "north", "east");
		bool(PortalEndFrame, "eye", 4);
		variant(SlabWood, "half", 8, "bottom", "top");
		variant(CocoaBeans, "facing", 1, "south", "west", "north", "east");
		numbered(CocoaBeans, "age", 3, 2);
		variant(TripwireHook, "facing", 1, "south", "west", "north", "east");
		bool(TripwireHook, "attached", 4);
		bool(TripwireHook, "powered", 8);
		bool(Tripwire, "powered", 1);
		bool(Tripwire, "attached", 4);
		bool(Tripwire, "disarmed", 8);
		fence(Tripwire);
		bool(Command, "conditional", -1);
		variant(CobblestoneWall, "cobblestone", "mossy_cobblestone");
		fence(CobblestoneWall);
		variant(FlowerPot, "contents", -1, "empty", "dandelion", "rose", "blue_orchid", "allium", "houstonia", "red_tulip", "orange_tulip", "white_tulip",
				"pink_tulip", "oxeye_daisy", "oak_sapling", "spruce_sapling", "birch_sapling", "jungle_sapling", "acacia_sapling", "dark_oak_sapling",
				"mushroom_red", "mushroom_brown", "dead_bush", "fern", "cactus");
		numbered(Carrots, "age", 1, 7);
		numbered(Potatoes, "age", 1, 7);
		button(ButtonOak);
		bool(Skull, "nodrop", -1);
		torch(Skull);
		variant(Anvil, "facing", 1, "north", "east", "south", "west");
		numbered(Anvil, "damage", 4, 2);
		numbered(PressurePlateGold, "power", 1, 15);
		numbered(PressurePlateIron, "power", 1, 15);
		comparator(ComparatorOff);
		comparator(ComparatorOn);
		numbered(DaylightDetector, "power", 1, 15);
		Hopper.addState(new BlockState("facing", BlockState.STRING, new int[]
		{ 0, 2, 3, 4, 5 }, "down", "north", "south", "west", "east"));
		Hopper.addState(new BlockState("enabled", BlockState.BOOLEAN, 8, "true", "false"));
		variant(BlockQuartz, "default", "chiseled", "lines_y", "lines_z", "lines_x");
		bool(Dropper, "triggered", 8);
		fence(GlassPaneColoured);
		leaves(Leaves2);
		log(Wood2);
		trapdoor(TrapdoorIron);
		variant(Prismarine, "prismarine", "prismarine_bricks", "dark_prismarine");
		pillar(Hay);
		variant(FlowerDouble, "sunflower", "syringa", "double_grass", "double_fern", "double_rose", "paeonia");
		variant(FlowerDouble, "half", 8, "lower", "upper");
		numbered(BannerStanding, "rotation", 1, 15);
		numbered(DaylightDetectorInverted, "power", 1, 15);
		variant(SandstoneRed, "type", 1, "red_sandstone", "chiseled_red_sandstone", "smooth_red_sandstone");
		variant(SlabDoubleStone2, "red_sandstone");
		bool(SlabDoubleStone2, "seamless", 8);
		variant(SlabStone2, "red_sandstone");
		variant(SlabStone2, "half", 8, "bottom", "top");
		fence(FenceSpruce);
		fence(FenceBirch);
		fence(FenceJungle);
		fence(FenceDarkOak);
		fence(FenceAcacia);
		fence(ChorusFlower);
		bool(ChorusFlower, "up", -1);
		bool(ChorusFlower, "down", -1);
		pillar(PurpurPillar);
		variant(SlabDoublePurpur, "default");
		variant(SlabPurpur, "default");
		variant(SlabPurpur, "half", 8, "bottom", "top");
		numbered(Beetroots, "age", 1, 3);
		bool(CommandRepeating, "conditional", -1);
		bool(CommandChain, "conditional", -1);
		numbered(IceFrosted, "age", 1, 3);
		pillar(BlockBone);
		bool(Observer, "power", 8);
		variant(Structure, "mode", 1, "save", "load", "corner", "data");
	}

	private static void pillar(Block block)
	{
		block.setTextureType(-1);
		variant(block, "axis", 4, "y", "x", "z");
	}

	public static void bool(Block block, String state)
	{
		bool(block, state, 1);
	}

	public static void bool(Block block, String state, int damage)
	{
		block.addState(new BlockState(state, BlockState.BOOLEAN, damage, "true", "false"));
	}

	private static void button(Block block)
	{
		block.setTextureType(-1);
		bool(block, "powered", 8);
		variant(block, "facing", 1, "down", "east", "west", "south", "north", "up");
	}

	private static void comparator(Block block)
	{
		block.setTextureType(-1);
		variant(block, "facing", 1, "north", "east", "south", "west");
		variant(block, "mode", 4, "compare", "substract");
		bool(block, "powered", -1);
	}

	private static void connecting(Block block)
	{
		bool(block, "north", -1);
		bool(block, "west", -1);
		bool(block, "east", -1);
		bool(block, "south", -1);
		bool(block, "up", -1);
	}

	private static void fence(Block block)
	{
		bool(block, "north", -1);
		bool(block, "west", -1);
		bool(block, "east", -1);
		bool(block, "south", -1);
	}

	public static void leaves(Block block)
	{
		block.setTextureType(4);
		variant(block, "oak", "spruce", "birch", "jungle");
		bool(block, "decayable", 4);
		bool(block, "check_decay", 8);
	}

	public static void log(Block block)
	{
		variant(block, "oak", "spruce", "birch", "jungle");
		block.addState(new BlockState("axis", BlockState.STRING, 4, "y", "x", "z", "none"));
	}

	private static void mushroom(Block block)
	{
		block.setTextureType(-1);
		block.addState(new BlockState("variant", BlockState.STRING, new int[]
		{ 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 14, 15 }, "all_inside", "north_west", "north", "north_east", "west", "center", "east", "south_west", "south",
				"south_east", "stem", "all_outside", "all_stem"));
	}

	public static void numbered(Block block, String id, int damage, int maxValue)
	{
		String[] values = new String[maxValue + 1];
		for (int i = 0; i < values.length; ++i)
			values[i] = String.valueOf(i);
		block.addState(new BlockState(id, BlockState.INTEGER, 1, values));
	}

	private static void repeater(Block block)
	{
		block.setTextureType(-1);
		bool(block, "locked", -1);
		variant(block, "facing", 1, "north", "east", "south", "west");
		numbered(block, "delay", 4, 2);
	}

	private static void stem(Block block)
	{
		variant(block, "facing", -1, "south", "west", "north", "east", "up");
		numbered(block, "age", 1, 7);
	}

	private static void torch(Block block)
	{
		block.addState(new BlockState("facing", BlockState.STRING, 1, "east", "west", "south", "north", "up").setStartsAt(1));
	}

	private static void trapdoor(Block block)
	{
		block.setTextureType(-1);
		variant(block, "facing", 1, "north", "south", "west", "east");
		variant(block, "half", 4, "bottom", "top");
		bool(block, "open", 8);
	}

	public static void variant(Block block, String... variants)
	{
		variant(block, "variant", 1, variants);
	}

	public static void variant(Block block, String id, int damage, String... variants)
	{
		block.addState(new BlockState(id, BlockState.STRING, variants));
	}

	private Blocks112()
	{}

}
