package net.infnetwork.snowball.dancingtree.growth;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.ItemStack;

public final class GrowthService {
    public boolean isSapling(Block block) { return block.getType().name().endsWith("_SAPLING"); }

    /** Delegates to Paper's native bonemeal implementation, including all vanilla checks. */
    public boolean applyBonemeal(Block sapling) {
        return sapling.applyBoneMeal(BlockFace.UP);
    }

    public boolean generateTree(Block sapling) {
        TreeType type = treeType(sapling.getType());
        if (type == null || !hasVanillaSoil(sapling)) return false;
        BlockData original = sapling.getBlockData().clone();
        sapling.setType(Material.AIR, false);
        boolean generated = sapling.getWorld().generateTree(sapling.getLocation(), type);
        if (!generated) sapling.setBlockData(original, false);
        return generated;
    }

    public void showBonemeal(Location location) {
        World world = location.getWorld();
        if (world == null) return;
        Location center = location.clone().add(0.5, 0.6, 0.5);
        world.spawnParticle(org.bukkit.Particle.ITEM, center, 16, 0.25, 0.35, 0.25, 0.05,
                new ItemStack(Material.BONE_MEAL));
        world.spawnParticle(org.bukkit.Particle.HAPPY_VILLAGER, center, 4, 0.25, 0.3, 0.25, 0.02);
    }

    private boolean hasVanillaSoil(Block sapling) {
        return switch (sapling.getRelative(BlockFace.DOWN).getType()) {
            case DIRT, GRASS_BLOCK, PODZOL, MYCELIUM, MOSS_BLOCK, ROOTED_DIRT, MUD, FARMLAND -> true;
            default -> false;
        };
    }

    private TreeType treeType(Material material) {
        return switch (material.name()) {
            case "OAK_SAPLING" -> TreeType.TREE;
            case "BIRCH_SAPLING" -> TreeType.BIRCH;
            case "SPRUCE_SAPLING" -> TreeType.REDWOOD;
            case "JUNGLE_SAPLING" -> TreeType.JUNGLE;
            case "ACACIA_SAPLING" -> TreeType.ACACIA;
            case "DARK_OAK_SAPLING" -> TreeType.DARK_OAK;
            case "CHERRY_SAPLING" -> TreeType.CHERRY;
            case "PALE_OAK_SAPLING" -> paleOakType();
            default -> null;
        };
    }

    private TreeType paleOakType() {
        try { return TreeType.valueOf("PALE_OAK"); }
        catch (IllegalArgumentException ignored) { return null; }
    }
}
