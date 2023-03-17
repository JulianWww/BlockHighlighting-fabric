package net.denanu.blockhighlighting.rendering;

import java.util.Collection;
import java.util.HashSet;
import java.util.concurrent.Semaphore;

import org.jetbrains.annotations.Nullable;

import com.mojang.blaze3d.systems.RenderSystem;

import net.denanu.blockhighlighting.components.ChunkComponents;
import net.denanu.blockhighlighting.components.component.IChunkComponent;
import net.denanu.blockhighlighting.config.Config;
import net.denanu.blockhighlighting.config.HighlightType;
import net.denanu.blockhighlighting.config.HighlightTypes;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.EmptyChunk;

public class ClientPosHighlighter {
	private static Semaphore mutex = new Semaphore(1);

	protected static final int RANGE = 10000*1000;

	private static HashSet<BlockPos> poses = new HashSet<>();

	public static void highlight(final BlockPos pos) {
		try {
			ClientPosHighlighter.mutex.acquire();
			ClientPosHighlighter.poses.add(pos);
		} catch (final InterruptedException e) {
			e.printStackTrace();
		}
		ClientPosHighlighter.mutex.release();
	}

	public static void unHighlight(final BlockPos pos) {
		try {
			ClientPosHighlighter.mutex.acquire();
			ClientPosHighlighter.poses.remove(pos);
		} catch (final InterruptedException e) {
			e.printStackTrace();
		}
		ClientPosHighlighter.mutex.release();
	}

	public static void highlight(final BlockPos pos, final boolean active) {
		if (active) {
			ClientPosHighlighter.highlight(pos);
		}
		else {
			ClientPosHighlighter.unHighlight(pos);
		}
	}

	public static <VillageBoundingBox> void render(
			final MatrixStack matrixStack,
			@Nullable final VertexConsumerProvider consumers,
			final Camera camera,
			final GameRenderer gameRenderer,
			final ClientWorld world,
			final int fillColor,
			final int outlineColor,
			final Collection<BlockPos> poses
			) {
		RenderSystem.enableDepthTest();
		RenderSystem.setShader(GameRenderer::getPositionColorShader);
		final Tessellator tessellator = Tessellator.getInstance();
		final BufferBuilder bufferBuilder = tessellator.getBuffer();
		RenderSystem.disableTexture();

		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();

		RenderSystem.lineWidth(100.0f);
		bufferBuilder.begin(VertexFormat.DrawMode.DEBUG_LINE_STRIP, VertexFormats.POSITION_COLOR);

		//final Vec3d camPos = camera.getPos();

		for (final BlockPos pos : poses) {
			ClientPosHighlighter.drawOutlineBox(pos, bufferBuilder, camera, world.getBlockState(pos).getOutlineShape(world, pos), outlineColor);
		}

		// render script here

		tessellator.draw();
		bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);


		for (final BlockPos pos : poses) {
			ClientPosHighlighter.drawFillBox(pos, bufferBuilder, camera, world.getBlockState(pos).getOutlineShape(world, pos), fillColor);
		}


		tessellator.draw();
		RenderSystem.lineWidth(1.0f);
		RenderSystem.enableBlend();
		RenderSystem.enableTexture();

	}

	private static void drawFillBox(final BlockPos pos, final BufferBuilder buf, final Camera camera, final VoxelShape voxel, final int color) {
		ClientPosHighlighter.drawBox(pos, buf, camera, voxel, ClientPosHighlighter::drawFilledBox, color, 0.01);
	}

	private static void drawOutlineBox(final BlockPos pos, final BufferBuilder buf, final Camera camera, final VoxelShape voxel, final int color) {
		ClientPosHighlighter.drawBox(pos, buf, camera, voxel, ClientPosHighlighter::drawOutlineBox, color, 0.01);
	}

	private static void drawBox(final BlockPos pos, final BufferBuilder buf, final Camera camera, final VoxelShape voxel, final Renderer renderer, final int color, final double offset) {
		final double cameraX = camera.getPos().x;
		final double cameraY = camera.getPos().y;
		final double cameraZ = camera.getPos().z;

		final double xpos = pos.getX() - cameraX;
		final double ypos = pos.getY() - cameraY;
		final double zpos = pos.getZ() - cameraZ;

		double x, y, z, topx, topy, topz;

		if (voxel.isEmpty()) {
			x = xpos;
			y = ypos;
			z = zpos;
			topx = x+1;
			topy = y+1;
			topz = z+1;
		}
		else  {
			x = xpos - offset + voxel.getMin(Direction.Axis.X);
			y = ypos - offset + voxel.getMin(Direction.Axis.Y);
			z = zpos - offset + voxel.getMin(Direction.Axis.Z);

			topx = xpos + offset + voxel.getMax(Direction.Axis.X);
			topy = ypos + offset + voxel.getMax(Direction.Axis.Y);
			topz = zpos + offset + voxel.getMax(Direction.Axis.Z);
		}

		renderer.draw(x, y, z, topx, topy, topz, color, buf);
	}

	private static void drawFilledBox(final double x, final double y, final double z, final double topx, final double topy, final double topz, final int color, final BufferBuilder buf) {
		buf.vertex(x, 		y, 		z).		color(color).next();
		buf.vertex(topx, 	y, 		z).		color(color).next();
		buf.vertex(topx, 	y, 		topz).	color(color).next();
		buf.vertex(x, 		y, 		topz).	color(color).next();

		buf.vertex(x, 		topy,	z).		color(color).next();
		buf.vertex(x, 		topy,	topz).	color(color).next();
		buf.vertex(topx, 	topy,	topz).	color(color).next();
		buf.vertex(topx, 	topy,	z).		color(color).next();

		buf.vertex(x, 		y, 		z).		color(color).next();
		buf.vertex(x, 		topy,	z).		color(color).next();
		buf.vertex(topx, 	topy,	z).		color(color).next();
		buf.vertex(topx,	y,		z).		color(color).next();

		buf.vertex(x, 		y, 		topz).	color(color).next();
		buf.vertex(topx, 	y, 		topz).	color(color).next();
		buf.vertex(topx, 	topy,	topz).	color(color).next();
		buf.vertex(x, 		topy,	topz).	color(color).next();

		buf.vertex(topx, 	y, 		z).		color(color).next();
		buf.vertex(topx, 	topy, 	z).		color(color).next();
		buf.vertex(topx, 	topy,	topz).	color(color).next();
		buf.vertex(topx, 	y,		topz).	color(color).next();

		buf.vertex(x, 		y, 		z).		color(color).next();
		buf.vertex(x, 		y,		topz).	color(color).next();
		buf.vertex(x, 		topy,	topz).	color(color).next();
		buf.vertex(x, 		topy, 	z).		color(color).next();
	}

	private static void drawOutlineBox(final double x, final double y, final double z, final double topx, final double topy, final double topz, final int color, final BufferBuilder buf) {
		ClientPosHighlighter.drawYalignedSquare	(x, 	y, 		z, 		topx, 	topz, 			buf, color);
		ClientPosHighlighter.drawYalignedSquare	(x, 	topy, 	z, 		topx, 	topz, 			buf, color);

		ClientPosHighlighter.drawLine			(x, 	y, 		z, 		x, 		topy, 	z, 		buf, color);
		ClientPosHighlighter.drawLine			(topx, 	y, 		z, 		topx, 	topy, 	z, 		buf, color);
		ClientPosHighlighter.drawLine			(x, 	y, 		topz, 	x, 		topy, 	topz, 	buf, color);
		ClientPosHighlighter.drawLine			(topx, 	y, 		topz, 	topx, 	topy, 	topz,	buf, color);
	}

	public static void drawYalignedSquare(final double x, final double y, final double z, final double topx, final double topz, final BufferBuilder buf, final int color) {
		buf.vertex(x, 		y, z).		color(0).next();
		buf.vertex(x, 		y, z).		color(color).next();
		buf.vertex(x, 		y, topz).	color(color).next();
		buf.vertex(topx, 	y, topz).	color(color).next();
		buf.vertex(topx, 	y, z).		color(color).next();
		buf.vertex(x, 		y, z).		color(color).next();
		buf.vertex(x, 		y, z).		color(0).next();
	}



	public static void drawLine(final double fx, final double fy, final double fz, final double tx, final double ty, final double tz, final BufferBuilder bufferBuilder, final int color) {
		bufferBuilder.vertex(fx, fy, fz).color(0).next();
		bufferBuilder.vertex(fx, fy, fz).color(color).next();
		bufferBuilder.vertex(tx, ty, tz).color(color).next();
		bufferBuilder.vertex(tx, ty, tz).color(0).next();
	}

	public static void render(final WorldRenderContext wrc)
	{
		if (Config.Generic.SHOULD_RENDER.getBooleanValue())
		{
			ChunkPos.stream(new ChunkPos(wrc.camera().getBlockPos()), Math.min(Config.Generic.RENDER_DISTANCE.getIntegerValue(), wrc.world().getSimulationDistance()))
			.forEach(pos -> {
				ClientPosHighlighter.renderChunk(wrc, wrc.world().getChunk(pos.getBlockPos(0, 0, 0)));
			});

		}
	}

	private static void renderChunk(final WorldRenderContext wrc, final Chunk chunk) {
		if (chunk instanceof EmptyChunk) {
			return;
		}
		final IChunkComponent highlighter = ChunkComponents.HIGHLIGHTS.get(chunk);
		for (final HighlightType type : HighlightTypes.HIGHLIGHT_TYPES) {
			if (type.getShouldRender().getBooleanValue()) {
				ClientPosHighlighter.renderChunkType(wrc, highlighter.getPoses(type), type);
			}
		}
	}

	private static void renderChunkType(final WorldRenderContext wrc, final Collection<BlockPos> poses, final HighlightType type) {
		if (!poses.isEmpty()) {
			ClientPosHighlighter.render(wrc.matrixStack(), wrc.consumers(), wrc.camera(), wrc.gameRenderer(), wrc.world(), type.getFillColor().getIntegerValue(), type.getOutlineColor().getIntegerValue(), poses);
		}
	}

	interface Renderer {
		void draw(final double x, final double y, final double z, final double topx, final double topy, final double topz, final int color, final BufferBuilder buf);
	}
}
