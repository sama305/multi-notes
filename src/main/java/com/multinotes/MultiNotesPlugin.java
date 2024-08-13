package com.multinotes;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.util.ImageUtil;

import java.awt.image.BufferedImage;

@Slf4j
@PluginDescriptor(
	name = "Multi Notes",
	description = "Multi Notes adds a simple and powerful note editor that has note organization baked in.",
	loadWhenOutdated = true
)
public class MultiNotesPlugin extends Plugin
{
	@Inject
	private ClientToolbar clientToolbar;

	@Inject
	private MultiNotesConfig config;

	private MultiNotesPanel panel;
	private NavigationButton navButton;

	@Override
	protected void startUp() throws Exception
	{
		panel = injector.getInstance(MultiNotesPanel.class);
		panel.init(config);

		final BufferedImage icon = ImageUtil.loadImageResource(getClass(), "/multi_notes_icon.png");

		navButton = NavigationButton.builder()
				.tooltip("Multi Notes")
				.priority(7)
				.icon(icon)
				.panel(panel)
				.build();

		clientToolbar.addNavigation(navButton);
	}

	@Override
	protected void shutDown() throws Exception
	{
		panel.deinit();
		clientToolbar.removeNavigation(navButton);
		panel = null;
		navButton = null;
	}

	@Provides
	MultiNotesConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(MultiNotesConfig.class);
	}
}
