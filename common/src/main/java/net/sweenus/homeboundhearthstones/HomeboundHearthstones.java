package net.sweenus.homeboundhearthstones;

import com.google.gson.JsonObject;
import dev.architectury.registry.CreativeTabRegistry;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.sweenus.homeboundhearthstones.config.Config;
import net.sweenus.homeboundhearthstones.config.HomeboundHearthstonesConfig;
import net.sweenus.homeboundhearthstones.registry.ItemsRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

public class HomeboundHearthstones {
    public static final String MOD_ID = "homeboundhearthstones";

    public static final ItemGroup HOMEBOUNDHEARTHSTONES = CreativeTabRegistry.create(new Identifier(MOD_ID, "homeboundhearthstones"), () ->
            new ItemStack(ItemsRegistry.HEARTHSTONE.get()));

    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static void init() {

        //CONFIG

        HomeboundHearthstonesConfig.init();

        String defaultConfig = """
                {
                  "regen_homeboundhearthstones_config_file": false
                }""";

        File configFile = Config.createFile("config/homeboundhearthstones/backupconfig.json", defaultConfig, false);
        JsonObject json = Config.getJsonObject(Config.readFile(configFile));

        HomeboundHearthstonesConfig.generateConfigs(json == null || !json.has("regen_homeboundhearthstones_config_file") || json.get("regen_homeboundhearthstones_config_file").getAsBoolean());
        HomeboundHearthstonesConfig.loadConfig();

        ItemsRegistry.ITEM.register();

    }
}