package daylightnebula.warcrossmcplugin.items;

import daylightnebula.warcrossmcplugin.utils.Essentials;
import daylightnebula.warcrossmcplugin.utils.Item;
import daylightnebula.warcrossmcplugin.utils.ItemFactory;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class ShadowBladeItem extends Item {

    final float dashRange = 3;
    ItemStack baseItem;

    public ShadowBladeItem() {
        super(3);

        baseItem = new ItemStack(Material.NETHERITE_SWORD);
        ItemMeta meta = baseItem.getItemMeta();

        meta.setDisplayName(ChatColor.DARK_GRAY + "Shadow Blade");
        List<String> lore = new ArrayList<String>();
        lore.add("4 Attack Damage, 2 Attack Speed");
        lore.add("Press Q to dash forward and");
        lore.add("increase damage by 50% for 3");
        lore.add("seconds.  Cooldown: 9s");
        ItemFactory.setAttackDamage(meta, 4);
        ItemFactory.setAttackSpeed(meta, 2);
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
    public boolean entityDamageEvent(EntityDamageByEntityEvent event, Item.ItemSpot spot) {
        byte[] data = ((LivingEntity) event.getDamager()).getEquipment().getItemInMainHand().getItemMeta().getPersistentDataContainer().get(Essentials.key, PersistentDataType.BYTE_ARRAY);

        if (data[0] <= 53 && data[0] >= -7) event.setDamage(event.getDamage() * 1.5f);

        return true;
    }

    @Override
    public boolean useAbility(PlayerDropItemEvent event, Item.ItemSpot spot) {
        // check and cancel
        if (spot != Item.ItemSpot.MAINHAND) return true;
        ItemStack is = event.getItemDrop().getItemStack();
        event.setCancelled(true);
        ItemMeta meta = is.getItemMeta();

        // if cooldown is 0, then set cooldown and next attack to variable
        byte[] data = meta.getPersistentDataContainer().get(Essentials.key, PersistentDataType.BYTE_ARRAY);
        if (data[0] > -128) return true; // if cooldown greater than 0 quit

        // ability
        data[0] = 53;
        Player player = event.getPlayer();
        Vector unitVector = new Vector(player.getLocation().getDirection().getX(), 0, player.getLocation().getDirection().getZ());
        unitVector = unitVector.normalize();
        player.setVelocity(unitVector.multiply(dashRange));

        // wrap up
        meta.getPersistentDataContainer().set(Essentials.key, PersistentDataType.BYTE_ARRAY, data);
        is.setItemMeta(meta);

        return true;
    }

    @Override
    public boolean swapItems(PlayerSwapHandItemsEvent event, Item.ItemSpot spot) {
        return true;
    }

    @Override
    public boolean whenPlayerLaunchArrow(EntityShootBowEvent event, Item.ItemSpot spot) {
        return false;
    }

    @Override
    public void updateEvent(LivingEntity le, ItemStack instance, Item.ItemSpot spot) {
        // remove one from cooldown (shifted back 128 due to byte range limitations)
        ItemMeta meta = instance.getItemMeta();
        byte[] data = meta.getPersistentDataContainer().get(Essentials.key, PersistentDataType.BYTE_ARRAY);
        if (data[0] > -128) data[0] -= 1;
        meta.getPersistentDataContainer().set(Essentials.key, PersistentDataType.BYTE_ARRAY, data);
        instance.setItemMeta(meta);

        // cooldown on xp bar
        // equation for level: level^2 + 6 × level
        if (spot != Item.ItemSpot.MAINHAND) return; // only show cooldown if item is in mainhand
        if (!(le instanceof Player)) return; // only run if le is a player
        int secondsleft = ((int) data[0] + 128) / 20;
        ((Player) le).setLevel(secondsleft);
    }
}
