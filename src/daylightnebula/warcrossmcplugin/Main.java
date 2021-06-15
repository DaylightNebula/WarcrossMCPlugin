package daylightnebula.warcrossmcplugin;

import daylightnebula.warcrossmcplugin.utils.Essentials;
import daylightnebula.warcrossmcplugin.utils.ItemManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;

import java.util.List;

public class Main extends JavaPlugin implements CommandExecutor, Listener {

    final boolean debugHealthbars = true;
    final boolean displayEffects = true;

    @Override
    public void onEnable() {
        Essentials.plugin = this;
        this.getCommand("getitems").setExecutor(this);
        Bukkit.getPluginManager().registerEvents(this, this);
        //getServer().getPluginManager().registerEvents(new ArmorListener(getConfig().getStringList("blocked")), this);

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                List<LivingEntity> livingEntities = Bukkit.getWorlds().get(0).getLivingEntities();

                for (LivingEntity le : livingEntities) {
                    ItemManager.updateEvent(le);

                    if (debugHealthbars) le.setCustomName(le.getHealth() + "");
                    if (displayEffects) {
                        for (PotionEffect pe : le.getActivePotionEffects())
                            Bukkit.broadcastMessage(pe.getType() + " effect found!");
                    }
                }
            }
        }, 0L, 1L);
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        ItemManager.entityDamageEvent(event);
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        ItemManager.useAbility(event);
    }

    @EventHandler
    public void onEntityShootBow(EntityShootBowEvent event) {
        ItemManager.whenPlayerLaunchArrow(event);
    }

    @EventHandler
    public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent event) {
        ItemManager.swapItems(event);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        //ItemManager.invClick(event);
    }

    @Override
    public void onDisable() {

    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (s.equals("getitems") && commandSender instanceof Player) {
            ((Player) commandSender).openInventory(ItemManager.getAdminInventory((Player)commandSender));
        }

        return true;
    }
}
