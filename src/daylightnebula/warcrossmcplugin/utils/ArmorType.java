package daylightnebula.warcrossmcplugin.utils;

import org.bukkit.inventory.ItemStack;

import static org.bukkit.Material.*;

public enum ArmorType {
    HELMET(5), CHESTPLATE(6), LEGGINGS(7), BOOTS(8), SHIELD(45), OFF_HAND(45);

    private final int slot;

    ArmorType(int slot){
        this.slot = slot;
    }

    /**
     * Attempts to match the ArmorType for the specified ItemStack.
     *
     * @param itemStack The ItemStack to parse the type of.
     * @return The parsed ArmorType. (null if none were found.)
     */
    public final static ArmorType matchType(final ItemStack itemStack){
        if(itemStack == null)
            return null;

        switch (itemStack.getType()){
            case DIAMOND_HELMET:
            case GOLDEN_HELMET:
            case IRON_HELMET:
            case CHAINMAIL_HELMET:
            case LEATHER_HELMET:
                return HELMET;
            case DIAMOND_CHESTPLATE:
            case GOLDEN_CHESTPLATE:
            case IRON_CHESTPLATE:
            case CHAINMAIL_CHESTPLATE:
            case LEATHER_CHESTPLATE:
                return CHESTPLATE;
            case DIAMOND_LEGGINGS:
            case GOLDEN_LEGGINGS:
            case IRON_LEGGINGS:
            case CHAINMAIL_LEGGINGS:
            case LEATHER_LEGGINGS:
                return LEGGINGS;
            case DIAMOND_BOOTS:
            case GOLDEN_BOOTS:
            case IRON_BOOTS:
            case CHAINMAIL_BOOTS:
            case LEATHER_BOOTS:
                return BOOTS;
            case SHIELD:
                return SHIELD;
            case AIR:
                return null;
            default:
                return OFF_HAND;
        }
    }

    public int getSlot(){
        return slot;
    }
}
