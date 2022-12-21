package net.denanu.clientblockhighlighting.config;

import fi.dy.masa.malilib.config.options.ConfigColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.InvalidIdentifierException;

public class HighlightType extends Identifier {
	ConfigColor outlineColor;
	ConfigColor fillColor;

	public HighlightType(final String id) {
		super(id);
		this.assignDefalultValues(this.path);
	}

	public HighlightType(final String namespace, final String path) {
		super(namespace, path);
		this.assignDefalultValues(path);
	}

	public HighlightType(final String id, final ConfigColor outline, final ConfigColor fill) {
		super(id);
		this.outlineColor = outline;
		this.fillColor = fill;

		this.register();
	}

	public HighlightType(final String namespace, final String path, final ConfigColor outline, final ConfigColor fill) {
		super(namespace, path);
		this.outlineColor = outline;
		this.fillColor = fill;

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

	private void assignDefalultValues(final String name) {
		this.outlineColor = new ConfigColor(
				new StringBuilder().append(name).append("Outline").toString(),
				"#FFFFFFFF",
				new StringBuilder().append("Outline color of the ").append(name).append(" highlight group").toString()
				);

		this.fillColor = new ConfigColor(
				new StringBuilder().append(name).append("Fill").toString(),
				"#20FFFFFF",
				new StringBuilder().append("Fill color of the ").append(name).append(" highlight group").toString()
				);

		this.register();
	}

	private void register() {
		Config.Color.OPTIONS.add(this.outlineColor);
		Config.Color.OPTIONS.add(this.fillColor);
	}

	public ConfigColor getOutlineColor() {
		return this.outlineColor;
	}

	public ConfigColor getFillColor() {
		return this.fillColor;
	}
}
