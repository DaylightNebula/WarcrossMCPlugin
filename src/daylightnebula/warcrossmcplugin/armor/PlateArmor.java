package daylightnebula.warcrossmcplugin.armor;

import daylightnebula.warcrossmcplugin.utils.Armor;
import daylightnebula.warcrossmcplugin.utils.ItemFactory;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class PlateArmor extends Armor {

    public PlateArmor() {
        super(3);
    }

    @Override
    public ItemStack getBaseItem(LivingEntity le) {
        ItemStack item = new ItemStack(Material.NETHERITE_CHESTPLATE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("Plate Armor Chestplate");

        List<String> lore = new ArrayList<String>();
        lore.add("Full set of netherite gear");
        lore.add("compacted into one item.");
        meta.setLore(lore);

        item.setItemMeta(meta);
        return item;
    }

    @Override
    public byte[] getBaseData(LivingEntity le) {
        return new byte[0];
    }

    @Override
    public ItemStack getHelmet() {
        ItemStack helmet = new ItemStack(Material.NETHERITE_HELMET);
        ItemMeta meta = helmet.getItemMeta();
        meta.setDisplayName("Plate Armor Helmet");
        helmet.setItemMeta(meta);
        return helmet;
    }

    @Override
    public ItemStack getLeggings() {
        ItemStack helmet = new ItemStack(Material.NETHERITE_LEGGINGS);
        ItemMeta meta = helmet.getItemMeta();
        meta.setDisplayName("Plate Armor Leggings");
        helmet.setItemMeta(meta);
        return helmet;
    }

    @Override
    public ItemStack getBoots() {
        ItemStack helmet = new ItemStack(Material.NETHERITE_BOOTS);
        ItemMeta meta = helmet.getItemMeta();
        meta.setDisplayName("Plate Armor Boots");
        helmet.setItemMeta(meta);
        return helmet;
    }

    @Override
    public boolean entityDamageEvent(EntityDamageByEntityEvent event, ItemSpot spot) {
        return false;
    }

    @Override
    public boolean useAbility(PlayerDropItemEvent event, ItemSpot spot) {
        return false;
    }

    @Override
    public boolean swapItems(PlayerSwapHandItemsEvent event, ItemSpot spot) {
        return false;
    }

    @Override
    public boolean whenPlayerLaunchArrow(EntityShootBowEvent event, ItemSpot spot) {
        return false;
    }

    @Override
    public void updateEvent(LivingEntity le, ItemStack instance, ItemSpot spot) {

    }
}
