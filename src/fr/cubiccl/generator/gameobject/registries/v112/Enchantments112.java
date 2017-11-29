package fr.cubiccl.generator.gameobject.registries.v112;

import fr.cubiccl.generator.gameobject.baseobjects.EnchantmentType;

public final class Enchantments112
{

	// Armor Enchantments
	public static final EnchantmentType Protection = new EnchantmentType(0, "protection", 5);
	public static final EnchantmentType ProtectionFire = new EnchantmentType(1, "fire_protection", 4);
	public static final EnchantmentType FeatherFalling = new EnchantmentType(2, "feather_falling", 4);
	public static final EnchantmentType ProtectionBlast = new EnchantmentType(3, "blast_protection", 4);
	public static final EnchantmentType ProtectionProjectile = new EnchantmentType(4, "projectile_protection", 4);
	public static final EnchantmentType Respiration = new EnchantmentType(5, "respiration", 3);
	public static final EnchantmentType AquaAffinity = new EnchantmentType(6, "aqua_affinity", 1);
	public static final EnchantmentType Thorns = new EnchantmentType(7, "thorns", 3);
	public static final EnchantmentType DepthStrider = new EnchantmentType(8, "depth_strider", 3);
	public static final EnchantmentType FrostWalker = new EnchantmentType(9, "frost_walker", 2);
	public static final EnchantmentType CurseBinding = new EnchantmentType(10, "binding_curse", 1);

	// Weapon Enchantments
	public static final EnchantmentType Sharpness = new EnchantmentType(16, "sharpness", 5);
	public static final EnchantmentType Smite = new EnchantmentType(17, "smite", 5);
	public static final EnchantmentType BaneOfArthropods = new EnchantmentType(18, "bane_of_arthropods", 5);
	public static final EnchantmentType Knockback = new EnchantmentType(19, "knockback", 2);
	public static final EnchantmentType FireAspect = new EnchantmentType(20, "fire_aspect", 2);
	public static final EnchantmentType Looting = new EnchantmentType(21, "looting", 3);
	public static final EnchantmentType SweepingEdge = new EnchantmentType(22, "sweeping_edge", 3);

	// Tools Enchantments
	public static final EnchantmentType Efficiency = new EnchantmentType(32, "efficiency", 5);
	public static final EnchantmentType SilkTouch = new EnchantmentType(33, "silk_touch", 1);
	public static final EnchantmentType Unbreaking = new EnchantmentType(34, "unbreaking", 3);
	public static final EnchantmentType Fortune = new EnchantmentType(35, "fortune", 3);

	// Bow Enchantments
	public static final EnchantmentType Power = new EnchantmentType(48, "power", 5);
	public static final EnchantmentType Punch = new EnchantmentType(49, "punch", 2);
	public static final EnchantmentType Flame = new EnchantmentType(50, "flame", 1);
	public static final EnchantmentType Infinity = new EnchantmentType(51, "infinity", 1);

	// Fishing Rod Enchantments
	public static final EnchantmentType LuckOfTheSea = new EnchantmentType(61, "luck_of_the_sea", 3);
	public static final EnchantmentType Lure = new EnchantmentType(62, "lure", 3);

	// Global Enchantments
	public static final EnchantmentType Mending = new EnchantmentType(70, "mending", 1);
	public static final EnchantmentType CurseVanishing = new EnchantmentType(71, "vanishing_curse", 1);

	public static void load()
	{}

	private Enchantments112()
	{}

}
