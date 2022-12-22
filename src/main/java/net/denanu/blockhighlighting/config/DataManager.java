package net.denanu.blockhighlighting.config;

import net.denanu.blockhighlighting.config.GuiConfigs.ConfigGuiTab;

public class DataManager {
	private static ConfigGuiTab currentTab = ConfigGuiTab.GENERIC;

	public static void setConfigGuiTab(ConfigGuiTab tab) {
		currentTab = tab;
	}
	
	public static ConfigGuiTab getConfigGuiTab() {
		return currentTab;
	}

}
