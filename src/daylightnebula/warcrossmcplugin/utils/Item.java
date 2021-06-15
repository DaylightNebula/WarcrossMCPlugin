package daylightnebula.warcrossmcplugin.utils;

import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;

public abstract class Item {

    public int points;

    public Item(int points) {
        this.points = points;
    }

    public abstract ItemStack getBaseItem(LivingEntity le);
    public abstract byte[] getBaseData(LivingEntity le);

    public abstract boolean entityDamageEvent(EntityDamageByEntityEvent event, ItemSpot spot);
    public abstract boolean useAbility(PlayerDropItemEvent event, ItemSpot spot);
    public abstract boolean swapItems(PlayerSwapHandItemsEvent event, ItemSpot spot);
    public abstract boolean whenPlayerLaunchArrow(EntityShootBowEvent event, ItemSpot spot);
    public abstract void updateEvent(LivingEntity le, ItemStack instance, ItemSpot spot);

    public enum ItemSpot {
        MAINHAND,
        OFFHAND,
        HELMET,
        CHESTPLATE,
        LEGGINGS,
        BOOTS
    }
}
