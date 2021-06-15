package daylightnebula.warcrossmcplugin.items;

import daylightnebula.warcrossmcplugin.utils.Essentials;
import daylightnebula.warcrossmcplugin.utils.Item;
import daylightnebula.warcrossmcplugin.utils.ItemFactory;
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

import java.util.ArrayList;
import java.util.List;

public class GreatswordItem extends Item {

    ItemStack baseItem;

    public GreatswordItem() {
        super(2);

        baseItem = new ItemStack(Material.IRON_SWORD);
        ItemMeta meta = baseItem.getItemMeta();

        meta.setDisplayName(ChatColor.YELLOW + "Greatsword");
        List<String> lore = new ArrayList<String>();
        lore.add("3 Attack Damage");
        lore.add("Press Q to make your next attack");
        lore.add("deal double damage. Cooldown: 9s");
        ItemFactory.setAttackDamage(meta, 3);
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
        return new byte[]{-128, 0};
    }

    @Override
    public boolean entityDamageEvent(EntityDamageByEntityEvent event, ItemSpot spot) {
        // check and get vars
        if (spot != ItemSpot.MAINHAND) return true;
        LivingEntity le = ((LivingEntity) event.getDamager());
        ItemStack is = le.getEquipment().getItemInMainHand();
        ItemMeta meta = is.getItemMeta();

        // check if this attack should deal extra damage
        //  if it should apply the extra damage
        byte[] data = meta.getPersistentDataContainer().get(Essentials.key, PersistentDataType.BYTE_ARRAY);
        if (data[1] == 1) {
            event.setDamage(event.getDamage() * 2);
            data[1] = 0;
            le.getWorld().spawnParticle(Particle.LAVA, event.getEntity().getLocation(), 80);
        }
        meta.getPersistentDataContainer().set(Essentials.key, PersistentDataType.BYTE_ARRAY, data);

        is.setItemMeta(meta);

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
        data[1] = 1;
        data[0] = 53;
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
