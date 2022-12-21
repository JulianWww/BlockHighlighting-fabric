package net.denanu.clientblockhighlighting.config;

import fi.dy.masa.malilib.config.options.ConfigBoolean;
import fi.dy.masa.malilib.config.options.ConfigColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.InvalidIdentifierException;

public class HighlightType extends Identifier {
	ConfigColor outlineColor;
	ConfigColor fillColor;
	ConfigBoolean shouldRender;

	public HighlightType(final String id) {
		super(id);
		this.assignDefalultValues(this.path);
	}

	public HighlightType(final String namespace, final String path) {
		super(namespace, path);
		this.assignDefalultValues(path);
	}

	public HighlightType(final String id, final ConfigColor outline, final ConfigColor fill, final ConfigBoolean shouldRender) {
		super(id);
		this.outlineColor = outline;
		this.fillColor = fill;
		this.shouldRender = shouldRender;

		this.register();
	}

	public HighlightType(final String namespace, final String path, final ConfigColor outline, final ConfigColor fill, final ConfigBoolean shouldRender) {
		super(namespace, path);
		this.outlineColor = outline;
		this.fillColor = fill;
		this.shouldRender = shouldRender;

		this.register();
	}


	public static HighlightType of(final String namespace, final String id) {
		try {
			return new HighlightType(namespace, id);
		}
		catch (final InvalidIdentifierException invalidIdentifierException) {
			return null;
		}
	}

	public static HighlightType of(final String namespace, final String id, final String fillColor, final String outlineColor) {
		try {
			return new HighlightType(namespace, id, HighlightType.getOutlineColor(id, outlineColor), HighlightType.getFillColor(id, fillColor), HighlightType.getShouldRender(id, false));
		}
		catch (final InvalidIdentifierException invalidIdentifierException) {
			return null;
		}
	}

	private void assignDefalultValues(final String name) {
		this.outlineColor = HighlightType.getOutlineColor(name, "#FFFFFFFF");
		this.fillColor = HighlightType.getFillColor(name, "#20FFFFFF");
		this.shouldRender = HighlightType.getShouldRender(name, false);

		this.register();
	}

	private static ConfigColor getOutlineColor(final String name, final String color) {
		return new ConfigColor(
				new StringBuilder().append(name).append("Outline").toString(),
				color,
				new StringBuilder().append("Outline color of the ").append(name).append(" highlight group").toString()
				);
	}

	private static ConfigColor getFillColor(final String name, final String color) {
		return new ConfigColor(
				new StringBuilder().append(name).append("Fill").toString(),
				color,
				new StringBuilder().append("Fill color of the ").append(name).append(" highlight group").toString()
				);
	}

	private static ConfigBoolean getShouldRender(final String name, final boolean val) {
		return new ConfigBoolean(
				"Render " + name,
				val,
				new StringBuilder().append("whether or not to render the ").append(name).append(" Highlighter").toString()
				);
	}

	private void register() {
		Config.Color.OPTIONS.add(this.outlineColor);
		Config.Color.OPTIONS.add(this.fillColor);

		Config.Generic.OPTIONS.add(this.shouldRender);
	}

	public ConfigColor getOutlineColor() {
		return this.outlineColor;
	}

	public ConfigColor getFillColor() {
		return this.fillColor;
	}

	public ConfigBoolean getShouldRender() {
		return this.shouldRender;
	}
}
