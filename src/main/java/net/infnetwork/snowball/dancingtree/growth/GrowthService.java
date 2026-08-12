package net.infnetwork.snowball.dancingtree.growth;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;

public final class GrowthService {
    public boolean isSapling(Block block) { return block.getType().name().endsWith("_SAPLING"); }

    /** Delegates to Paper's native bonemeal implementation, including all vanilla checks. */
    public boolean applyBonemeal(Block sapling) {
        return sapling.applyBoneMeal(BlockFace.UP);
    }

    public void showBonemeal(Location location) {
        World world = location.getWorld();
        if (world == null) return;
        Location center = location.clone().add(0.5, 0.6, 0.5);
        world.spawnParticle(org.bukkit.Particle.ITEM, center, 16, 0.25, 0.35, 0.25, 0.05,
                new ItemStack(Material.BONE_MEAL));
        world.spawnParticle(org.bukkit.Particle.HAPPY_VILLAGER, center, 4, 0.25, 0.3, 0.25, 0.02);
    }
}
