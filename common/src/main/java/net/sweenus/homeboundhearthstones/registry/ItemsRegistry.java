package net.sweenus.homeboundhearthstones.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.item.Item;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;
import net.sweenus.homeboundhearthstones.HomeboundHearthstones;
import net.sweenus.homeboundhearthstones.config.HomeboundHearthstonesConfig;
import net.sweenus.homeboundhearthstones.custom.HearthstoneItem;

public class ItemsRegistry {

    static float uses = (int) HomeboundHearthstonesConfig.getGeneralSettings("hearthstone_uses");


    public static final DeferredRegister<Item> ITEM = DeferredRegister.create(HomeboundHearthstones.MOD_ID, Registry.ITEM_KEY);

    public static final RegistrySupplier<HearthstoneItem> HEARTHSTONE = ITEM.register( "hearthstone", () ->
            new HearthstoneItem(new Item.Settings().group(HomeboundHearthstones.HOMEBOUNDHEARTHSTONES).rarity(Rarity.RARE).maxDamage((int) uses)));

}
