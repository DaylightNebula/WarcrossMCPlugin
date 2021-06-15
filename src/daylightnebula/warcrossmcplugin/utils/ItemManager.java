package daylightnebula.warcrossmcplugin.utils;

import daylightnebula.warcrossmcplugin.armor.PlateArmor;
import daylightnebula.warcrossmcplugin.items.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class ItemManager {

    public static Item[] items = new Item[]{
            new GreatswordItem(),
            new RepeaterCrossbowItem(),
            new ClawsItem(),
            new GreatbowItem(),
            new LongbowItem(),
            new MaceItem(),
            new ShadowBladeItem(),
            new ShurikansItem(),
            new WarhammerItem(),
            new PlateArmor()
    };

    public static void checkData(LivingEntity le, ItemStack itemstack) {
        if (Essentials.key == null) Essentials.key = new NamespacedKey(Essentials.plugin, "item-data");
        if (itemstack == null) return;

        for (Item item : items)
            if (ItemFactory.checkEquals(item.getBaseItem(le), itemstack)) {
                if (!itemstack.getItemMeta().getPersistentDataContainer().isEmpty()) return;
                if (item.getBaseData(le) == null || item.getBaseData(le).length <= 0) return;
                ItemMeta meta = itemstack.getItemMeta();
                meta.getPersistentDataContainer().set(Essentials.key, PersistentDataType.BYTE_ARRAY, item.getBaseData(le));
                itemstack.setItemMeta(meta);
            }
    }

    public static Inventory getAdminInventory(LivingEntity le) {
        Inventory inv = Bukkit.createInventory(null, 27, "Admin Item Menu");

        for (Item item : items)
            inv.addItem(ItemFactory.getCloneWithData(item.getBaseItem(le), item.getBaseData(le)));

        return inv;
    }

    public static boolean entityDamageEvent(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof LivingEntity) {
            LivingEntity le = (LivingEntity) event.getDamager();
            ItemStack mainhand = le.getEquipment().getItemInMainHand();
            ItemStack offhand = le.getEquipment().getItemInOffHand();
            ItemStack helmet = le.getEquipment().getHelmet();
            ItemStack chestplate = le.getEquipment().getChestplate();
            ItemStack leggings = le.getEquipment().getLeggings();
            ItemStack boots = le.getEquipment().getBoots();

            checkData(le, mainhand);
            checkData(le, offhand);
            checkData(le, helmet);
            checkData(le, chestplate);
            checkData(le, leggings);
            checkData(le, boots);

            for (Item item : items) {
                if (ItemFactory.checkEquals(item.getBaseItem(le), mainhand))
                    return item.entityDamageEvent(event, Item.ItemSpot.MAINHAND);
                else if (ItemFactory.checkEquals(item.getBaseItem(le), offhand))
                    return item.entityDamageEvent(event, Item.ItemSpot.OFFHAND);
                else if (ItemFactory.checkEquals(item.getBaseItem(le), helmet))
                    return item.entityDamageEvent(event, Item.ItemSpot.HELMET);
                else if (ItemFactory.checkEquals(item.getBaseItem(le), chestplate))
                    return item.entityDamageEvent(event, Item.ItemSpot.CHESTPLATE);
                else if (ItemFactory.checkEquals(item.getBaseItem(le), leggings))
                    return item.entityDamageEvent(event, Item.ItemSpot.LEGGINGS);
                else if (ItemFactory.checkEquals(item.getBaseItem(le), boots))
                    return item.entityDamageEvent(event, Item.ItemSpot.BOOTS);
            }
        }

        if (ArrowManager.arrowEvents.size() > 0 && ArrowManager.arrowEvents.keySet().contains(event.getDamager())) {
            ArrowManager.arrowEvents.get(event.getDamager()).run(event);
        }

        return false;
    }

    public static boolean useAbility(PlayerDropItemEvent event) {
        Player p = event.getPlayer();
        ItemStack mainhand = event.getItemDrop().getItemStack();
        ItemStack offhand = p.getEquipment().getItemInOffHand();
        ItemStack helmet = p.getEquipment().getHelmet();
        ItemStack chestplate = p.getEquipment().getChestplate();
        ItemStack leggings = p.getEquipment().getLeggings();
        ItemStack boots = p.getEquipment().getBoots();

        checkData(p, mainhand);
        checkData(p, offhand);
        checkData(p, helmet);
        checkData(p, chestplate);
        checkData(p, leggings);
        checkData(p, boots);

        for (Item item : items) {
            if (ItemFactory.checkEquals(item.getBaseItem(p), mainhand))
                return item.useAbility(event, Item.ItemSpot.MAINHAND);
            else if (ItemFactory.checkEquals(item.getBaseItem(p), offhand))
                return item.useAbility(event, Item.ItemSpot.OFFHAND);
            else if (ItemFactory.checkEquals(item.getBaseItem(p), helmet))
                return item.useAbility(event, Item.ItemSpot.HELMET);
            else if (ItemFactory.checkEquals(item.getBaseItem(p), chestplate))
                return item.useAbility(event, Item.ItemSpot.CHESTPLATE);
            else if (ItemFactory.checkEquals(item.getBaseItem(p), leggings))
                return item.useAbility(event, Item.ItemSpot.LEGGINGS);
            else if (ItemFactory.checkEquals(item.getBaseItem(p), boots))
                return item.useAbility(event, Item.ItemSpot.BOOTS);
        }
        return false;
    }

    public static boolean swapItems(PlayerSwapHandItemsEvent event) {
        Player p = event.getPlayer();
        ItemStack mainhand = event.getMainHandItem();
        ItemStack offhand = event.getOffHandItem();
        ItemStack helmet = p.getEquipment().getHelmet();
        ItemStack chestplate = p.getEquipment().getChestplate();
        ItemStack leggings = p.getEquipment().getLeggings();
        ItemStack boots = p.getEquipment().getBoots();

        checkData(p, mainhand);
        checkData(p, offhand);
        checkData(p, helmet);
        checkData(p, chestplate);
        checkData(p, leggings);
        checkData(p, boots);

        for (Item item : items) {
            if (ItemFactory.checkEquals(item.getBaseItem(p), mainhand))
                return item.swapItems(event, Item.ItemSpot.MAINHAND);
            else if (offhand != null && ItemFactory.checkEquals(item.getBaseItem(p), offhand))
                return item.swapItems(event, Item.ItemSpot.OFFHAND);
            else if (ItemFactory.checkEquals(item.getBaseItem(p), helmet))
                return item.swapItems(event, Item.ItemSpot.HELMET);
            else if (ItemFactory.checkEquals(item.getBaseItem(p), chestplate))
                return item.swapItems(event, Item.ItemSpot.CHESTPLATE);
            else if (ItemFactory.checkEquals(item.getBaseItem(p), leggings))
                return item.swapItems(event, Item.ItemSpot.LEGGINGS);
            else if (ItemFactory.checkEquals(item.getBaseItem(p), boots))
                return item.swapItems(event, Item.ItemSpot.BOOTS);
        }
        return false;
    }

    public static boolean whenPlayerLaunchArrow(EntityShootBowEvent event) {
        LivingEntity le = event.getEntity();
        ItemStack mainhand = le.getEquipment().getItemInMainHand();
        ItemStack offhand = le.getEquipment().getItemInOffHand();
        ItemStack helmet = le.getEquipment().getHelmet();
        ItemStack chestplate = le.getEquipment().getChestplate();
        ItemStack leggings = le.getEquipment().getLeggings();
        ItemStack boots = le.getEquipment().getBoots();

        checkData(le, mainhand);
        checkData(le, offhand);
        checkData(le, helmet);
        checkData(le, chestplate);
        checkData(le, leggings);
        checkData(le, boots);

        for (Item item : items) {
            if (ItemFactory.checkEquals(item.getBaseItem(le), mainhand))
                return item.whenPlayerLaunchArrow(event, Item.ItemSpot.MAINHAND);
            else if (ItemFactory.checkEquals(item.getBaseItem(le), offhand))
                return item.whenPlayerLaunchArrow(event, Item.ItemSpot.OFFHAND);
            else if (ItemFactory.checkEquals(item.getBaseItem(le), helmet))
                return item.whenPlayerLaunchArrow(event, Item.ItemSpot.HELMET);
            else if (ItemFactory.checkEquals(item.getBaseItem(le), chestplate))
                return item.whenPlayerLaunchArrow(event, Item.ItemSpot.CHESTPLATE);
            else if (ItemFactory.checkEquals(item.getBaseItem(le), leggings))
                return item.whenPlayerLaunchArrow(event, Item.ItemSpot.LEGGINGS);
            else if (ItemFactory.checkEquals(item.getBaseItem(le), boots))
                return item.whenPlayerLaunchArrow(event, Item.ItemSpot.BOOTS);
        }
        return false;
    }

    public static void updateEvent(LivingEntity le) {
        // vars
        boolean chestplatefound = false;

        // item stacks for each possible custom item slot
        ItemStack mainhand = le.getEquipment().getItemInMainHand();
        ItemStack offhand = le.getEquipment().getItemInOffHand();
        ItemStack chestplate = ((Player) le).getInventory().getItem(5);

        //Bukkit.broadcastMessage(chestplate.getType() + "");

        // check to make sure each custom item has custom data on it to avoid errors
        checkData(le, mainhand);
        checkData(le, offhand);
        checkData(le, chestplate);

        // for each possible custom item
        for (Item item : items) {
            Bukkit.broadcastMessage(item.toString());
            // check each possible custom item slot, if equals call an update event
            if (ItemFactory.checkEquals(item.getBaseItem(le), mainhand))
                item.updateEvent(le, mainhand, Item.ItemSpot.MAINHAND);
            else if (ItemFactory.checkEquals(item.getBaseItem(le), offhand))
                item.updateEvent(le, offhand, Item.ItemSpot.OFFHAND);
            else if (ItemFactory.checkEquals(item.getBaseItem(le), chestplate)) {
                // special armor code
                // check if armor
                Bukkit.broadcastMessage("found armor?");
                if (!(item instanceof Armor)) return;
                Armor armor = (Armor) item;

                // chestplate found
                chestplatefound = true;

                // update
                item.updateEvent(le, chestplate, Item.ItemSpot.CHESTPLATE);

                le.getEquipment().setHelmet(armor.getHelmet());
                le.getEquipment().setChestplate(chestplate);
                le.getEquipment().setLeggings(armor.getLeggings());
                le.getEquipment().setBoots(armor.getBoots());
            }
        }

        // if no chestplate was found
        if (!chestplatefound) {
            // check and set each non-chestplate armor slot to null
            if (le.getEquipment().getHelmet() != null) le.getEquipment().setHelmet(null);
            if (le.getEquipment().getChestplate() != null) le.getEquipment().setChestplate(null);
            if (le.getEquipment().getLeggings() != null) le.getEquipment().setLeggings(null);
            if (le.getEquipment().getBoots() != null) le.getEquipment().setBoots(null);
        }
    }
}
