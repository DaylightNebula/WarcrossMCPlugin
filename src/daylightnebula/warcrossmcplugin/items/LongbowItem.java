package daylightnebula.warcrossmcplugin.items;

import daylightnebula.warcrossmcplugin.utils.ArrowManager;
import daylightnebula.warcrossmcplugin.utils.Item;
import daylightnebula.warcrossmcplugin.utils.ItemFactory;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class LongbowItem extends Item {
    ItemStack baseItem;

    public LongbowItem() {
        super(2);

        baseItem = new ItemStack(Material.BOW);
        ItemMeta meta = baseItem.getItemMeta();

        meta.setDisplayName(ChatColor.GOLD + "Long Bow");
        List<String> lore = new ArrayList<String>();
        lore.add("5 Attack Damage");
        ItemFactory.setAttackDamage(meta, 5);
        meta.setLore(lore);

        baseItem.setItemMeta(meta);
    }

    @Override
    public ItemStack getBaseItem(LivingEntity le) {
        return baseItem;
    }

    // Data Structure: <cooldown - 127 (due to byte range of -128 -> 127)> <next attack deal double>
    @Override
    public byte[] getBaseData(LivingEntity le) {
        return new byte[]{};
    }

    @Override
    public boolean entityDamageEvent(EntityDamageByEntityEvent event, ItemSpot spot) {
        return true;
    }

    @Override
    public boolean useAbility(PlayerDropItemEvent event, ItemSpot spot) {
        return true;
    }

    @Override
    public boolean swapItems(PlayerSwapHandItemsEvent event, ItemSpot spot) {
        return true;
    }

    @Override
    public boolean whenPlayerLaunchArrow(EntityShootBowEvent event, ItemSpot spot) {
        ArrowManager.RegisterArrow((Arrow) event.getProjectile(), new ArrowManager.ArrowRunnable() {
            @Override
            public void run(EntityDamageByEntityEvent event) {
                double armor = ItemFactory.getArmorPoints((LivingEntity) event.getEntity());
                event.setDamage(5);
                Bukkit.broadcastMessage("Damage: " + event.getDamage() + ", Damage After Reduction: " + event.getFinalDamage());
            }
        });

        return true;
    }

    @Override
    public void updateEvent(LivingEntity le, ItemStack instance, ItemSpot spot) {
        if (le instanceof Player) {
            Player p = (Player) le;
            if (!p.getInventory().contains(Material.ARROW))
                p.getInventory().addItem(new ItemStack(Material.ARROW, 1));
        }
    }
}
