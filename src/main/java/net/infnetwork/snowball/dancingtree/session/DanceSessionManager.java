package net.infnetwork.snowball.dancingtree.session;

import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class DanceSessionManager {
    private final Map<SessionKey, DanceSession> sessions = new HashMap<>();

    public DanceSessionManager(JavaPlugin plugin) {
        plugin.getServer().getScheduler().runTaskTimer(plugin, this::removeExpired, 20L, 20L);
    }

    public int addSneak(UUID playerId, Location sapling, long now, long timeoutMillis) {
        SessionKey key = new SessionKey(playerId, sapling);
        DanceSession session = sessions.computeIfAbsent(key, ignored -> new DanceSession());
        if (now - session.lastSneakAt > timeoutMillis) session.count = 0;
        session.count++;
        session.lastSneakAt = now;
        return session.count;
    }

    public void reset(UUID playerId, Location sapling) { sessions.remove(new SessionKey(playerId, sapling)); }
    public void removePlayer(UUID playerId) { sessions.keySet().removeIf(key -> key.playerId.equals(playerId)); }
    public void clear() { sessions.clear(); }

    private void removeExpired() {
        // Sessions are cheap and this also prevents stale player/tree entries lingering forever.
        long now = System.currentTimeMillis();
        sessions.entrySet().removeIf(entry -> now - entry.getValue().lastSneakAt > 30_000L);
    }

    private record SessionKey(UUID playerId, UUID worldId, int x, int y, int z) {
        private SessionKey(UUID playerId, Location location) {
            this(playerId, location.getWorld().getUID(), location.getBlockX(), location.getBlockY(), location.getBlockZ());
        }
    }
    private static final class DanceSession { private int count; private long lastSneakAt; }
}
