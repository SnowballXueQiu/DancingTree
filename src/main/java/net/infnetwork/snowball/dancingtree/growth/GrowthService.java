package net.infnetwork.snowball.dancingtree.growth;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public final class GrowthService {
    public boolean isSapling(Block block) { return block.getType().name().endsWith("_SAPLING"); }

    /** Uses the native generator as a dry-run: the predicate rejects every generated block. */
    public boolean canGrow(Block sapling) {
        TreeType type = treeType(sapling.getType());
        return type != null && sapling.getWorld().generateTree(
                sapling.getLocation(), new Random(), type, state -> false);
    }

    public boolean grow(Block sapling) {
        TreeType type = treeType(sapling.getType());
        return type != null && sapling.getWorld().generateTree(sapling.getLocation(), type);
    }

    public void showBonemeal(Location location) {
        World world = location.getWorld();
        if (world == null) return;
        Location center = location.clone().add(0.5, 0.6, 0.5);
        world.spawnParticle(org.bukkit.Particle.ITEM, center, 16, 0.25, 0.35, 0.25, 0.05,
                new ItemStack(Material.BONE_MEAL));
        world.spawnParticle(org.bukkit.Particle.HAPPY_VILLAGER, center, 4, 0.25, 0.3, 0.25, 0.02);
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
