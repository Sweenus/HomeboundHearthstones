package net.sweenus.homeboundhearthstones.forge;

import dev.architectury.platform.forge.EventBuses;
import net.sweenus.homeboundhearthstones.HomeboundHearthstones;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(HomeboundHearthstones.MOD_ID)
public class HomeboundHearthstonesForge {
    public HomeboundHearthstonesForge() {
        // Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(HomeboundHearthstones.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        HomeboundHearthstones.init();
    }
}