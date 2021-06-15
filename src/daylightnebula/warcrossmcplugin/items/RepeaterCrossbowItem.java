package daylightnebula.warcrossmcplugin.items;

import daylightnebula.warcrossmcplugin.utils.Essentials;
import daylightnebula.warcrossmcplugin.utils.Item;
import daylightnebula.warcrossmcplugin.utils.ItemFactory;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CrossbowMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class RepeaterCrossbowItem extends Item {
    ItemStack baseItem;

    public RepeaterCrossbowItem() {
        super(3);

        baseItem = new ItemStack(Material.CROSSBOW);
        CrossbowMeta meta = (CrossbowMeta) baseItem.getItemMeta();

        meta.setDisplayName(ChatColor.RED + "Repeater Crossbow");
        List<String> lore = new ArrayList<String>();
        lore.add("Can fire up to 15 arrows without");
        lore.add("reloading.  Press Q to reload in");
        lore.add("2 seconds.");
        ItemFactory.setAttackDamage(meta, 2);
        ItemFactory.setAttackSpeed(meta, 5);
        meta.setLore(lore);
        meta.addChargedProjectile(new ItemStack(Material.ARROW, 1));

        baseItem.setItemMeta(meta);
    }

    @Override
    public ItemStack getBaseItem(LivingEntity le) {
        return baseItem;
    }

    // Data Structure: <reload cooldown>
    @Override
    public byte[] getBaseData(LivingEntity le) {
        return new byte[]{0, 15};
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
        data[0] = 21;
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
        Arrow arrow = (Arrow) event.getProjectile();
        arrow.setPickupStatus(AbstractArrow.PickupStatus.DISALLOWED);

        return false;
    }

    @Override
    public void updateEvent(LivingEntity le, ItemStack instance, ItemSpot spot) {
        // remove one from cooldown (shifted back 128 due to byte range limitations)
        CrossbowMeta meta = (CrossbowMeta) instance.getItemMeta();
        byte[] data = meta.getPersistentDataContainer().get(Essentials.key, PersistentDataType.BYTE_ARRAY);
        if (data[0] > 1) data[0] -= 1;
        if (data[0] == 1 && le instanceof Player) {
            ((Player) le).getInventory().setItem(9, new ItemStack(Material.ARROW, 15));
            data[0] = 0;
        }
        meta.getPersistentDataContainer().set(Essentials.key, PersistentDataType.BYTE_ARRAY, data);

        // check if crossbow is not loaded, if it isnt load and remove 1 arrow if arrows are available
        if (le instanceof Player && meta.getChargedProjectiles().size() == 0) {
            Player p = (Player) le;
            if (p.getInventory().contains(Material.ARROW)) {
                p.getInventory().getItem(9).setAmount(p.getInventory().getItem(9).getAmount() - 1);
                meta.addChargedProjectile(new ItemStack(Material.ARROW, 1));
            }
        }

        instance.setItemMeta(meta);

        // cooldown on xp bar
        // equation for level: level^2 + 6 × level
        if (spot != ItemSpot.MAINHAND) return; // only show cooldown if item is in mainhand
        if (!(le instanceof Player)) return; // only run if le is a player
        int secondsleft = ((int) data[0] + 128) / 20;
        ((Player) le).setLevel(secondsleft);
        if (((Player) le).getInventory().getItem(9) != null)
            ((Player) le).setExp(((Player) le).getInventory().getItem(9).getAmount() / 15f);
        else
            ((Player) le).setExp(0);
    }
}
