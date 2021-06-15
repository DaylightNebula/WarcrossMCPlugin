package daylightnebula.warcrossmcplugin.utils;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Base64;
import java.util.List;
import java.util.UUID;

public class ItemFactory {

    public static ItemStack getCloneWithData(ItemStack i, byte[] data) {
        if (Essentials.key == null) Essentials.key = new NamespacedKey(Essentials.plugin, "item-data");

        ItemStack item = i.clone();
        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(Essentials.key, PersistentDataType.BYTE_ARRAY, data);
        item.setItemMeta(meta);
        return item;
    }

    public static boolean checkEquals(ItemStack src, ItemStack dest) {
        if (src == null || src.getItemMeta() == null || src.getItemMeta().getLore() == null) return false;
        if (dest == null || dest.getItemMeta() == null || dest.getItemMeta().getLore() == null) return false;

        return ((src.getItemMeta().getDisplayName().equals(dest.getItemMeta().getDisplayName()))
        && (src.getItemMeta().getLore().equals(dest.getItemMeta().getLore())));

    }

    public static void setAttackDamage(ItemMeta meta, int damage) {
        Multimap<Attribute, AttributeModifier> attribs = ArrayListMultimap.create();
        attribs.put(Attribute.GENERIC_ATTACK_DAMAGE,
                new org.bukkit.attribute.AttributeModifier(
                        UUID.randomUUID(),
                        "generic.attack_damage",
                        damage,
                        AttributeModifier.Operation.ADD_NUMBER
                )
        );
        meta.setAttributeModifiers(attribs);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
    }

    public static void setAttackSpeed(ItemMeta meta, double speed) {
        Multimap<Attribute, AttributeModifier> attribs = ArrayListMultimap.create();
        attribs.put(Attribute.GENERIC_ATTACK_DAMAGE,
                new org.bukkit.attribute.AttributeModifier(
                        UUID.randomUUID(),
                        "generic.attack_speed",
                        speed,
                        AttributeModifier.Operation.ADD_NUMBER
                )
        );
        meta.setAttributeModifiers(attribs);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
    }

    public static double getArmorPoints(LivingEntity le) {
        ItemStack helmet = le.getEquipment().getHelmet();
        ItemStack chest = le.getEquipment().getChestplate();
        ItemStack boots = le.getEquipment().getBoots();
        ItemStack pants = le.getEquipment().getLeggings();
        double red = 0.0;
        if (helmet != null) {
            if (helmet.getType() == Material.LEATHER_HELMET) red = red + 0.04;
            else if (helmet.getType() == Material.GOLDEN_HELMET) red = red + 0.08;
            else if (helmet.getType() == Material.CHAINMAIL_HELMET) red = red + 0.08;
            else if (helmet.getType() == Material.IRON_HELMET) red = red + 0.08;
            else if (helmet.getType() == Material.DIAMOND_HELMET) red = red + 0.12;
            else if (helmet.getType() == Material.NETHERITE_HELMET) red = red + 0.12;
        }
        if (chest != null) {
            if (chest.getType() == Material.LEATHER_CHESTPLATE) red = red + 0.12;
            else if (chest.getType() == Material.GOLDEN_CHESTPLATE) red = red + 0.20;
            else if (chest.getType() == Material.CHAINMAIL_CHESTPLATE) red = red + 0.20;
            else if (chest.getType() == Material.IRON_CHESTPLATE) red = red + 0.24;
            else if (chest.getType() == Material.DIAMOND_CHESTPLATE) red = red + 0.32;
            else if (chest.getType() == Material.NETHERITE_CHESTPLATE) red = red + 0.32;
        }
        if (pants != null) {
            if (pants.getType() == Material.LEATHER_LEGGINGS) red = red + 0.08;
            else if (pants.getType() == Material.GOLDEN_LEGGINGS) red = red + 0.12;
            else if (pants.getType() == Material.CHAINMAIL_LEGGINGS) red = red + 0.16;
            else if (pants.getType() == Material.IRON_LEGGINGS) red = red + 0.20;
            else if (pants.getType() == Material.DIAMOND_LEGGINGS) red = red + 0.24;
            else if (pants.getType() == Material.NETHERITE_LEGGINGS) red = red + 0.24;
        }
        if (boots != null) {
            if (boots.getType() == Material.LEATHER_BOOTS) red = red + 0.04;
            else if (boots.getType() == Material.GOLDEN_BOOTS) red = red + 0.04;
            else if (boots.getType() == Material.CHAINMAIL_BOOTS) red = red + 0.04;
            else if (boots.getType() == Material.IRON_BOOTS) red = red + 0.08;
            else if (boots.getType() == Material.DIAMOND_BOOTS) red = red + 0.12;
            else if (boots.getType() == Material.NETHERITE_BOOTS) red = red + 0.12;
        }
        return red * 25;
    }
}
