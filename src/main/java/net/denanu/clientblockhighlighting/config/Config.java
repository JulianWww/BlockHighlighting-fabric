package net.denanu.clientblockhighlighting.config;

import java.io.File;
import java.util.ArrayList;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import fi.dy.masa.malilib.config.ConfigUtils;
import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.config.IConfigHandler;
import fi.dy.masa.malilib.config.options.ConfigBoolean;
import fi.dy.masa.malilib.config.options.ConfigInteger;
import fi.dy.masa.malilib.util.FileUtils;
import fi.dy.masa.malilib.util.JsonUtils;
import net.denanu.clientblockhighlighting.Mod;

public class Config implements IConfigHandler {
	private static final String CONFIG_FILE_NAME = Mod.MOD_ID + ".json";

	public static class Generic {
		public static final ArrayList<IConfigBase> OPTIONS = new ArrayList<>();

		public static final ConfigBoolean SHOULD_RENDER 	= Generic.register(new ConfigBoolean("shouldrender", true, "Should Render Highlights", "Should Render Highlights"));
		public static final ConfigInteger RENDER_DISTANCE 	= Generic.register(new ConfigInteger("renderDistance", 2, 0, 10, "Distance to render Highlights at"));

		private static <T extends IConfigBase> T register(final T config) {
			Generic.OPTIONS.add(config);
			return config;
		}
	}

	public static class Color {
		public static final ArrayList<IConfigBase> OPTIONS = new ArrayList<>();
	}

	public static void loadFromFile()
	{
		final File configFile = new File(FileUtils.getConfigDirectory(), Config.CONFIG_FILE_NAME);

		if (configFile.exists() && configFile.isFile() && configFile.canRead())
		{
			final JsonElement element = JsonUtils.parseJsonFile(configFile);

			if (element != null && element.isJsonObject())
			{
				final JsonObject root = element.getAsJsonObject();

				ConfigUtils.readConfigBase(root, "Generic", Generic.OPTIONS);
				ConfigUtils.readConfigBase(root, "Color", Color.OPTIONS);
			}
		}
	}

	public static void saveToFile()
	{
		final File dir = FileUtils.getConfigDirectory();

		if (dir.exists() && dir.isDirectory() || dir.mkdirs())
		{
			final JsonObject root = new JsonObject();

			ConfigUtils.writeConfigBase(root, "Generic", Generic.OPTIONS);
			ConfigUtils.writeConfigBase(root, "Color", Color.OPTIONS);

			JsonUtils.writeJsonToFile(root, new File(dir, Config.CONFIG_FILE_NAME));
		}
	}

	@Override
	public void load()
	{
		Config.loadFromFile();
	}

	@Override
	public void save()
	{
		Config.saveToFile();
	}
}
