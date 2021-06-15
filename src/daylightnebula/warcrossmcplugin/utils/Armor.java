package daylightnebula.warcrossmcplugin.utils;

import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public abstract class Armor extends Item {

    public int maxarmorcount; // number of slots that should not be null if armor applied
    ItemStack[] slots; // ordered helmet, leggings, boots (chestplate would be in getBaseItem)

    public Armor(int maxarmorcount) {
        super(0);
        this.maxarmorcount = maxarmorcount;
        slots = new ItemStack[3];
        slots[0] = getHelmet();
        slots[1] = getLeggings();
        slots[2] = getBoots();
    }

    public abstract ItemStack getHelmet();
    public abstract ItemStack getLeggings();
    public abstract ItemStack getBoots();

    // chestplate will be the compare item and the data item
}
