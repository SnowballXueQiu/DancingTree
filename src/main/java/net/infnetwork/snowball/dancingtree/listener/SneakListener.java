package net.infnetwork.snowball.dancingtree.listener;

import net.infnetwork.snowball.dancingtree.growth.GrowthService;
import net.infnetwork.snowball.dancingtree.session.DanceSessionManager;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class SneakListener implements Listener {
    private final JavaPlugin plugin;
    private final DanceSessionManager sessions;
    private final GrowthService growth;

    public SneakListener(JavaPlugin plugin, DanceSessionManager sessions, GrowthService growth) {
        this.plugin = plugin; this.sessions = sessions; this.growth = growth;
    }

    @EventHandler public void onToggleSneak(PlayerToggleSneakEvent event) {
        if (!event.isSneaking()) return;
        Player player = event.getPlayer();
        Block sapling = findNearbySapling(player);
        if (sapling == null) return;

        int count = sessions.addSneak(player.getUniqueId(), sapling.getLocation(), System.currentTimeMillis(),
                plugin.getConfig().getLong("session-timeout-seconds", 5L) * 1000L);
        growth.showBonemeal(sapling.getLocation());
        int required = Math.max(1, plugin.getConfig().getInt("sneaks-required", 8));
        if (count < required) return;
        sessions.reset(player.getUniqueId(), sapling.getLocation());
        // Native Paper bonemeal performs the exact vanilla ground/space/growth checks.
        growth.applyBonemeal(sapling);
    }

    @EventHandler public void onQuit(PlayerQuitEvent event) { sessions.removePlayer(event.getPlayer().getUniqueId()); }

    private Block findNearbySapling(Player player) {
        int radius = (int) Math.ceil(plugin.getConfig().getDouble("interaction-range", 3.0));
        Block origin = player.getLocation().getBlock();
        double maxDistance = plugin.getConfig().getDouble("interaction-range", 3.0);
        Block nearest = null; double nearestDistance = Double.MAX_VALUE;
        for (int x = -radius; x <= radius; x++) for (int y = -radius; y <= radius; y++) for (int z = -radius; z <= radius; z++) {
            Block candidate = origin.getRelative(x, y, z);
            if (!growth.isSapling(candidate)) continue;
            double distance = candidate.getLocation().add(0.5, 0.5, 0.5).distance(player.getLocation());
            if (distance <= maxDistance && distance < nearestDistance) { nearest = candidate; nearestDistance = distance; }
        }
        return nearest;
    }
}
