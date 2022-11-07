package net.sweenus.homeboundhearthstones.fabric;

import net.sweenus.homeboundhearthstones.HomeboundHearthstones;
import net.fabricmc.api.ModInitializer;

public class HomeboundHearthstonesFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        HomeboundHearthstones.init();
    }
}