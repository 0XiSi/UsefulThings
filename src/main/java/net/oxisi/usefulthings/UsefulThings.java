package net.oxisi.usefulthings;

import net.oxisi.usefulthings.Commands.*;
import net.oxisi.usefulthings.Events.MessageAnnouncer;
import net.oxisi.usefulthings.Events.PlayerJoinCountdown;
import net.oxisi.usefulthings.Listeners.BlockBreakListener;
import net.oxisi.usefulthings.Listeners.EntityDeathListener;
import net.oxisi.usefulthings.Listeners.HungerMonitor;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public final class UsefulThings extends JavaPlugin {
    public static boolean isDirectionalBreakEnabled = false;

    public HashMap<UUID, Long> rankStartTime = new HashMap<>();
    public HashMap<UUID, Integer> rankDuration = new HashMap<>();


    @Override
    public void onEnable() {
        Objects.requireNonNull(this.getCommand("sortinv")).setExecutor(new AutoSortCommand());
        getCommand("sc").setExecutor(new StaffChatCommand());

        getCommand("displayentity").setExecutor(new DisplayEntityCommand());

        Objects.requireNonNull(this.getCommand("sandcastle")).setExecutor(new SandCastle(this));

        Objects.requireNonNull(this.getCommand("clonebody")).setExecutor(new ArmorStandCommand(this));

        Objects.requireNonNull(this.getCommand("actionbar")).setExecutor(new ActionBarCommand());

        Objects.requireNonNull(this.getCommand("ranktime")).setExecutor(new RankTimeLeftCommand());

        Objects.requireNonNull(this.getCommand("wheel")).setExecutor(new WheelCommand(this));

        Objects.requireNonNull(this.getCommand("openinv")).setExecutor(new OpenInventoryCommand());
        getServer().getPluginManager().registerEvents(new OpenInventoryCommand(), this);

        getServer().getPluginManager().registerEvents(new EntityDeathListener(this), this);
        getServer().getPluginManager().registerEvents(new BlockBreakListener(), this);

        getServer().getPluginManager().registerEvents(new PlayerJoinCountdown(this), this);

        getServer().getPluginManager().registerEvents(new WheelCommand(this), this);

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new MessageAnnouncer(), 0L, 1200L);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new HungerMonitor(this), 0L, 20L);


//        getServer().getPluginManager().registerEvents(new ShiftJumpListener(), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
