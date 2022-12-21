package net.denanu.clientblockhighlighting.config;

import org.jetbrains.annotations.Nullable;

import fi.dy.masa.malilib.config.options.ConfigColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.InvalidIdentifierException;

public class HighlighterType extends Identifier {
	ConfigColor outlineColor;
	ConfigColor fillColor;


	public HighlighterType(final String id) {
		super(id);
		this.setupDefaultColorConfigs(this.path);
	}

	public HighlighterType(final String namespace, final String path) {
		super(namespace, path);
		this.setupDefaultColorConfigs(this.path);
	}

	protected HighlighterType(final String[] id) {
		super(id);
		this.setupDefaultColorConfigs(this.path);
	}

	public HighlighterType(final String id, final ConfigColor outline, final ConfigColor fill) {
		super(id);
		this.outlineColor = outline;
		this.fillColor = fill;
	}

	public HighlighterType(final String namespace, final String path, final ConfigColor outline, final ConfigColor fill) {
		super(namespace, path);
		this.outlineColor = outline;
		this.fillColor = fill;
	}

	protected HighlighterType(final String[] id, final ConfigColor outline, final ConfigColor fill) {
		super(id);
		this.outlineColor = outline;
		this.fillColor = fill;
	}

	@Nullable
	public static HighlighterType of(final String namespace, final String path) {
		try {
			return new HighlighterType(namespace, path);
		}
		catch (final InvalidIdentifierException invalidIdentifierException) {
			return null;
		}
	}

	@Nullable
	public static HighlighterType of(final String namespace, final String path, final ConfigColor outline, final ConfigColor fill) {
		try {
			return new HighlighterType(namespace, path, outline, fill);
		}
		catch (final InvalidIdentifierException invalidIdentifierException) {
			return null;
		}
	}

	private void setupDefaultColorConfigs(final String name) {
		this.outlineColor = new ConfigColor(name + "Outline", 	"#ffffffff", new StringBuilder().append("Outline color of the ").append(name).append(" rendering Group").toString());
		this.fillColor = new ConfigColor(name + "Fill", 		"#ffffffff", new StringBuilder().append("Fill color of the ").append(name).append(" rendering Group").toString());

		this.addConfigs();
	}

	private void addConfigs() {
		Config.Color.OPTIONS.add(this.outlineColor);
		Config.Color.OPTIONS.add(this.fillColor);
	}

	public ConfigColor getFillColor() {
		return this.fillColor;
	}

	public ConfigColor getOutlineColor() {
		return this.outlineColor;
	}
}
