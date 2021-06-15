package daylightnebula.warcrossmcplugin.items;

import daylightnebula.warcrossmcplugin.utils.Essentials;
import daylightnebula.warcrossmcplugin.utils.Item;
import daylightnebula.warcrossmcplugin.utils.ItemFactory;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class MaceItem extends Item {

    public final float dist = 3f;
    ItemStack baseItem;

    public MaceItem() {
        super(2);

        baseItem = new ItemStack(Material.GOLDEN_HOE);
        ItemMeta meta = baseItem.getItemMeta();

        meta.setDisplayName(ChatColor.DARK_GRAY + "Mace");
        List<String> lore = new ArrayList<String>();
        lore.add("3 Attack Damage");
        lore.add("Press Q to slow all enemies");
        lore.add("within 3 blocks. Cooldown: 9s");
        ItemFactory.setAttackDamage(meta, 3);
        meta.setCustomModelData(2);
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
        return new byte[]{-128};
    }

    @Override
    public boolean entityDamageEvent(EntityDamageByEntityEvent event, ItemSpot spot) {
        return true;
    }

    @Override
    public boolean useAbility(PlayerDropItemEvent event, ItemSpot spot) {
        // check and cancel
        if (spot != ItemSpot.MAINHAND) return true;
        ItemStack is = event.getItemDrop().getItemStack();
        event.setCancelled(true);
        ItemMeta meta = is.getItemMeta();

        // if cooldown is 0, then set cooldown and next attack to variable
        byte[] data = meta.getPersistentDataContainer().get(Essentials.key, PersistentDataType.BYTE_ARRAY);
        if (data[0] > -128) return true; // if cooldown greater than 0 quit

        // ability
        data[0] = 53;
        for (LivingEntity le : event.getPlayer().getWorld().getLivingEntities())
            if (le.getLocation().distance(event.getPlayer().getLocation()) <= 3
                    && le != event.getPlayer())
                le.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60, 1));

        // animations
        Location loc = event.getPlayer().getLocation().clone();
        loc.add(Math.cos(Math.toRadians(0)) * dist, 0, Math.sin(Math.toRadians(0)) * dist);
        loc.getWorld().spawnParticle(Particle.SMOKE_NORMAL, loc, 40);

        for (int i = 45; i < 360; i += 45) {
            int finalI = i;
            Bukkit.getScheduler().runTaskLater(Essentials.plugin, new Runnable() {
                @Override
                public void run() {
                    Location loc = event.getPlayer().getLocation().clone();
                    loc.add(Math.cos(Math.toRadians(finalI)) * dist, 0, Math.sin(Math.toRadians(finalI)) * dist);
                    loc.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, loc, 40);
                }
            }, i / 45);
        }

        // wrap up
        meta.getPersistentDataContainer().set(Essentials.key, PersistentDataType.BYTE_ARRAY, data);
        is.setItemMeta(meta);
        return true;
    }

    @Override
    public boolean swapItems(PlayerSwapHandItemsEvent event, ItemSpot spot) {
        return true;
    }

    @Override
    public boolean whenPlayerLaunchArrow(EntityShootBowEvent event, ItemSpot spot) {
        return false;
    }

    @Override
    public void updateEvent(LivingEntity le, ItemStack instance, ItemSpot spot) {
        // remove one from cooldown (shifted back 128 due to byte range limitations)
        ItemMeta meta = instance.getItemMeta();
        byte[] data = meta.getPersistentDataContainer().get(Essentials.key, PersistentDataType.BYTE_ARRAY);
        if (data[0] > -128) data[0] -= 1;
        meta.getPersistentDataContainer().set(Essentials.key, PersistentDataType.BYTE_ARRAY, data);
        instance.setItemMeta(meta);

        // cooldown on xp bar
        // equation for level: level^2 + 6 × level
        if (spot != ItemSpot.MAINHAND) return; // only show cooldown if item is in mainhand
        if (!(le instanceof Player)) return; // only run if le is a player
        int secondsleft = ((int) data[0] + 128) / 20;
        ((Player) le).setLevel(secondsleft);
    }
}
