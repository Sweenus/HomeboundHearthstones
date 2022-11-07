package net.sweenus.homeboundhearthstones.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class HomeboundHearthstonesConfig {
    private static final HashMap<String, Boolean> BOOLEAN_OPTIONS = new LinkedHashMap<>();
    private static final HashMap<String, Float> GENERAL_OPTIONS = new LinkedHashMap<>();
    private static final HashMap<String, Float> FLOAT_OPTIONS = new LinkedHashMap<>();
    private static final HashMap<String, Float> WEAPON_OPTIONS = new LinkedHashMap<>();

    public static boolean getBooleanValue(String key) {
        if (!BOOLEAN_OPTIONS.containsKey(key)) {
            System.out.println(key);
        }
        return BOOLEAN_OPTIONS.getOrDefault(key, null);
    }

    public static float getGeneralSettings(String key) {
        if (!GENERAL_OPTIONS.containsKey(key)) {
            System.out.println(key);
        }
        return GENERAL_OPTIONS.getOrDefault(key, null);
    }

    public static float getFloatValue(String key) {
        if (!FLOAT_OPTIONS.containsKey(key)) {
            System.out.println(key);
        }
        return FLOAT_OPTIONS.getOrDefault(key, null);
    }

    public static float getWeaponAttributes(String key) {
        if (!WEAPON_OPTIONS.containsKey(key)) {
            System.out.println(key);
        }
        return WEAPON_OPTIONS.getOrDefault(key, null);
    }

    public static void init() {

        //FLOAT_OPTIONS.put("speed_chance", 15f);
        //FLOAT_OPTIONS.put("speed_duration", 300f);


        GENERAL_OPTIONS.put("hearthstone_cooldown_duration", 5f); //1800f
        GENERAL_OPTIONS.put("hearthstone_uses", 5f);


        BOOLEAN_OPTIONS.put("can_transport_ores", false);

    }

    public static void loadConfig() {
        JsonObject json;
        json = Config.getJsonObject(Config.readFile(new File("config/homeboundhearthstones/booleans.json5")));
        for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
            BOOLEAN_OPTIONS.put(entry.getKey(), entry.getValue().getAsBoolean());
        }

        json = Config.getJsonObject(Config.readFile(new File("config/homeboundhearthstones/general_config.json5")));
        for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
            GENERAL_OPTIONS.put(entry.getKey(), entry.getValue().getAsFloat());
        }

        json = Config.getJsonObject(Config.readFile(new File("config/homeboundhearthstones/effects_config.json5")));
        for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
            FLOAT_OPTIONS.put(entry.getKey(), entry.getValue().getAsFloat());
        }

        json = Config.getJsonObject(Config.readFile(new File("config/homeboundhearthstones/weapon_attributes.json5")));
        for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
            WEAPON_OPTIONS.put(entry.getKey(), entry.getValue().getAsFloat());
        }

    }


    public static void generateConfigs(boolean overwrite) {
        StringBuilder config = new StringBuilder("{\n");
        int i = 0;
        for (String key : BOOLEAN_OPTIONS.keySet()) {
            if (i == 0) {
                config.append("// -- BOOLEAN CONFIGURATION -- \n");
                config.append("\n");
                config.append("// Can ingots/ores/raw_ores be transported with the player via Hearthstone \n");
            }
            config.append("  \"").append(key).append("\": ").append(BOOLEAN_OPTIONS.get(key));
            ++i;
            if (i < BOOLEAN_OPTIONS.size()) {
                config.append(",");
            }
            config.append("\n");
        }
        config.append("}");
        Config.createFile("config/homeboundhearthstones/booleans.json5", config.toString(), overwrite);

        config = new StringBuilder("{\n");
        i = 0;
        for (String item : GENERAL_OPTIONS.keySet()) {
            if (i == 0) {
                config.append("// -- GENERAL CONFIGURATION -- \n");
                config.append("\n");
                config.append("// Hearthstone cooldown in seconds \n");
            }
            if (i == 1) {
                config.append("\n");
                config.append("// Hearthstone uses before the item breaks \n");
            }
            config.append("  \"").append(item).append("\": ").append(GENERAL_OPTIONS.get(item));
            ++i;
            if (i < GENERAL_OPTIONS.size()) {
                config.append(",");
            }
            config.append("\n");
        }
        config.append("}");
        Config.createFile("config/homeboundhearthstones/general_config.json5", config.toString(), overwrite);

        config = new StringBuilder("{\n");
        i = 0;
        for (String item : FLOAT_OPTIONS.keySet()) {
            if (i == 0) {
                config.append("// -- EFFECTS CONFIGURATION -- \n");
                config.append("\n");
                config.append("//Chance range 0-100, where 100 = 100% chance to occur\n");
                config.append("//Radius is measured in blocks\n");
                config.append("//Duration in ticks, where 20 is equivalent to one second \n");
                config.append("\n");
                config.append("// -- Runic Power: Swiftness -- \n");
                config.append("// ---------------------------- \n");
            }
            config.append("  \"").append(item).append("\": ").append(FLOAT_OPTIONS.get(item));
            ++i;
            if (i < FLOAT_OPTIONS.size()) {
                config.append(",");
            }
            config.append("\n");
        }
        config.append("}");
        Config.createFile("config/homeboundhearthstones/effects_config.json5", config.toString(), overwrite);

        config = new StringBuilder("{\n");
        i = 0;
        for (String item : WEAPON_OPTIONS.keySet()) {
            if (i == 0) {
                config.append("// -- WEAPON ATTRIBUTES CONFIGURATION -- \n");
                config.append("// These values should be THE SAME ON BOTH CLIENT AND SERVER, otherwise damage tooltips will display incorrect on the client \n");
                config.append("// The damage values of weapons can be modified by adjusting their weights \n");
                config.append("// This is not the outputted damage value you see in game, but it affects it directly \n");
                config.append("// Calculation: vanilla tool material damage + base_modifier + positive_modifier - negative_modifier = actual modifier \n");
                config.append("\n");
                config.append("// -- Positive Damage Modifiers -- \n");
                config.append("// Example use-case: Adding 3 to a value below will INCREASE the in-game damage of that weapon type by 3 \n");
                config.append("// ------------------------------- \n");
            }
            if (i == 1) {
                config.append("\n");
                config.append("// -- Negative Damage Modifiers -- \n");
                config.append("// Example use-case: Adding 3 to a value below will DECREASE the in-game damage of that weapon type by 3 \n");
                config.append("// ------------------------------- \n");
            }
            if (i == 2) {
                config.append("\n");
                config.append("// -- Base Damage Modifiers -- \n");
                config.append("// Positive & Negative damage modifiers scale off these base values \n");
                config.append("// --------------------------- \n");
            }
            if (i == 8) {
                config.append("\n");
                config.append("// -- Attack Speed Modifiers -- \n");
                config.append("// Recommended range: -1.0 to -3.7, with -1.0 being fast and -3.7 being slow \n");
                config.append("// ---------------------------- \n");
            }
            config.append("  \"").append(item).append("\": ").append(WEAPON_OPTIONS.get(item));
            ++i;
            if (i < WEAPON_OPTIONS.size()) {
                config.append(",");
            }
            config.append("\n");
        }
        config.append("}");
        Config.createFile("config/homeboundhearthstones/weapon_attributes.json5", config.toString(), overwrite);

    }
}