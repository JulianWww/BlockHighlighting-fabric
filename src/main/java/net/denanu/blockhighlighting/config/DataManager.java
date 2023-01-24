package net.denanu.blockhighlighting.config;

import net.denanu.blockhighlighting.config.GuiConfigs.ConfigGuiTab;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class DataManager {
	private static ConfigGuiTab currentTab = ConfigGuiTab.GENERIC;

	public static void setConfigGuiTab(final ConfigGuiTab tab) {
		DataManager.currentTab = tab;
	}

	public static ConfigGuiTab getConfigGuiTab() {
		return DataManager.currentTab;
	}

}
