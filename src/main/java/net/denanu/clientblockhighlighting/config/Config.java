package net.denanu.clientblockhighlighting.config;

import java.io.File;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;


import fi.dy.masa.malilib.config.ConfigUtils;
import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.config.IConfigHandler;
import fi.dy.masa.malilib.config.options.ConfigBoolean;
import fi.dy.masa.malilib.config.options.ConfigColor;
import fi.dy.masa.malilib.util.FileUtils;
import fi.dy.masa.malilib.util.JsonUtils;
import net.denanu.clientblockhighlighting.Mod;

public class Config implements IConfigHandler {
	private static final String CONFIG_FILE_NAME = Mod.MOD_ID + ".json";
	
	public static class Generic {
		public static final ConfigBoolean SHOULD_RENDER = new ConfigBoolean("shouldrender", true, "Should Render Highlights", "Should Render Highlights");
		
		public static final ImmutableList<IConfigBase> OPTIONS = ImmutableList.of(
				SHOULD_RENDER
				);
	}
	
	public static class Color {
		public static final ConfigColor OUTLINE_COLOR = new ConfigColor	("highlightOutline",  	"0xFFFFFFFF", "The Outline of the Highlighter");
		public static final ConfigColor FILL_COLOR = new ConfigColor	("fillOutline",  		"0x30FFFFFF", "The Fill of the Highlighter");
		
		public static final ImmutableList<IConfigBase> OPTIONS = ImmutableList.of(
				OUTLINE_COLOR,
				FILL_COLOR
				);
	}

	public static void loadFromFile()
    {
        File configFile = new File(FileUtils.getConfigDirectory(), CONFIG_FILE_NAME);

        if (configFile.exists() && configFile.isFile() && configFile.canRead())
        {
            JsonElement element = JsonUtils.parseJsonFile(configFile);

            if (element != null && element.isJsonObject())
            {
                JsonObject root = element.getAsJsonObject();

                ConfigUtils.readConfigBase(root, "Generic", Generic.OPTIONS);
            }
        }
    }

    public static void saveToFile()
    {
        File dir = FileUtils.getConfigDirectory();

        if ((dir.exists() && dir.isDirectory()) || dir.mkdirs())
        {
            JsonObject root = new JsonObject();

            ConfigUtils.writeConfigBase(root, "Generic", Generic.OPTIONS);

            JsonUtils.writeJsonToFile(root, new File(dir, CONFIG_FILE_NAME));
        }
    }

    @Override
    public void load()
    {
        loadFromFile();
    }

    @Override
    public void save()
    {
        saveToFile();
    }
}
