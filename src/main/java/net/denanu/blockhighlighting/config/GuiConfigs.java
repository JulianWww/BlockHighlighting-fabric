package net.denanu.blockhighlighting.config;

import java.util.Collections;
import java.util.List;

import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.gui.GuiConfigsBase;
import fi.dy.masa.malilib.gui.button.ButtonBase;
import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import fi.dy.masa.malilib.gui.button.IButtonActionListener;
import fi.dy.masa.malilib.util.StringUtils;
import net.denanu.blockhighlighting.BlockHighlighting;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class GuiConfigs extends GuiConfigsBase {
	public GuiConfigs()
	{
		super(10, 50, BlockHighlighting.MOD_ID, null, BlockHighlighting.MOD_ID + ".gui.title.configs");
	}

	@Override
	public void initGui()
	{
		super.initGui();
		this.clearOptions();

		int x = 10;
		final int y = 26;

		x += this.createButton(x, y, -1, ConfigGuiTab.GENERIC);
		x += this.createButton(x, y, -1, ConfigGuiTab.COLOR);
	}

	private int createButton(final int x, final int y, final int width, final ConfigGuiTab tab)
	{
		final ButtonGeneric button = new ButtonGeneric(x, y, width, 20, tab.getDisplayName());
		button.setEnabled(DataManager.getConfigGuiTab() != tab);
		this.addButton(button, new ButtonListener(tab, this));

		return button.getWidth() + 2;
	}

	@Override
	protected int getConfigWidth()
	{
		final ConfigGuiTab tab = DataManager.getConfigGuiTab();

		if (tab == ConfigGuiTab.GENERIC)
		{
			return 140;
		}

		return super.getConfigWidth();
	}

	@Override
	public List<ConfigOptionWrapper> getConfigs()
	{
		List<? extends IConfigBase> configs;
		final ConfigGuiTab tab = DataManager.getConfigGuiTab();

		configs = switch (tab) {
		case GENERIC -> Config.Generic.OPTIONS;
		case COLOR -> Config.Color.OPTIONS;
		default -> Collections.emptyList();
		};

		return ConfigOptionWrapper.createFor(configs);
	}

	@Override
	protected void onSettingsChanged()
	{
		super.onSettingsChanged();
	}

	private static class ButtonListener implements IButtonActionListener
	{
		private final GuiConfigs parent;
		private final ConfigGuiTab tab;

		public ButtonListener(final ConfigGuiTab tab, final GuiConfigs parent)
		{
			this.tab = tab;
			this.parent = parent;
		}

		@Override
		public void actionPerformedWithButton(final ButtonBase button, final int mouseButton)
		{
			DataManager.setConfigGuiTab(this.tab);

			this.parent.reCreateListWidget(); // apply the new config width
			this.parent.getListWidget().resetScrollbarPosition();
			this.parent.initGui();
		}
	}

	public enum ConfigGuiTab
	{
		GENERIC         (BlockHighlighting.MOD_ID + ".gui.button.config_gui.generic"),
		COLOR			(BlockHighlighting.MOD_ID + ".gui.button.config_gui.colors");

		private final String translationKey;

		ConfigGuiTab(final String translationKey)
		{
			this.translationKey = translationKey;
		}

		public String getDisplayName()
		{
			return StringUtils.translate(this.translationKey);
		}
	}
}
