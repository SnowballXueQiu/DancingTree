package net.infnetwork.snowball.dancingtree;

import net.infnetwork.snowball.dancingtree.growth.GrowthService;
import net.infnetwork.snowball.dancingtree.listener.SneakListener;
import net.infnetwork.snowball.dancingtree.session.DanceSessionManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class DancingTreePlugin extends JavaPlugin {
    private DanceSessionManager sessions;

    @Override public void onEnable() {
        saveDefaultConfig();
        sessions = new DanceSessionManager(this);
        getServer().getPluginManager().registerEvents(
                new SneakListener(this, sessions, new GrowthService()), this);
        getLogger().info("跳舞的树已启用。蹲在树苗旁边，开始跳舞吧！");
    }

    @Override public void onDisable() {
        if (sessions != null) sessions.clear();
    }
}
