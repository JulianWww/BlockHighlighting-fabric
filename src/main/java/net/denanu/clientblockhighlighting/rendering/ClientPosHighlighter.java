package net.denanu.clientblockhighlighting.rendering;

import java.util.Collection;

import com.mojang.blaze3d.systems.RenderSystem;

import net.denanu.clientblockhighlighting.components.ChunkComponents;
import net.denanu.clientblockhighlighting.config.Config;
import net.denanu.clientblockhighlighting.config.HighlighterType;
import net.denanu.clientblockhighlighting.config.HighlighterTypes;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.EmptyChunk;

public class ClientPosHighlighter {
	private static <VillageBoundingBox> void render(final Camera camera, final ClientWorld world, final Collection<BlockPos> poses, final HighlighterType type) {
		RenderSystem.enableDepthTest();
		RenderSystem.setShader(GameRenderer::getPositionColorShader);
		final var tessellator = Tessellator.getInstance();
		final var bufferBuilder = tessellator.getBuffer();
		RenderSystem.disableTexture();

		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();

		RenderSystem.lineWidth(100.0f);
		bufferBuilder.begin(VertexFormat.DrawMode.DEBUG_LINE_STRIP, VertexFormats.POSITION_COLOR);

		//final Vec3d camPos = camera.getPos();

		for (final BlockPos pos : poses) {
			ClientPosHighlighter.drawOutlineBox(pos, bufferBuilder, camera, world.getBlockState(pos).getOutlineShape(world, pos), type.getOutlineColor().getIntegerValue());
		}

		// render script here

		tessellator.draw();
		bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);


		for (final BlockPos pos : poses) {
			ClientPosHighlighter.drawFillBox(pos, bufferBuilder, camera, world.getBlockState(pos).getOutlineShape(world, pos), type.getFillColor().getIntegerValue());
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
		final var cameraX = camera.getPos().x;
		final var cameraY = camera.getPos().y;
		final var cameraZ = camera.getPos().z;

		final var xpos = pos.getX() - cameraX;
		final var ypos = pos.getY() - cameraY;
		final var zpos = pos.getZ() - cameraZ;

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

	private static void drawYalignedSquare(final double x, final double y, final double z, final double topx, final double topz, final BufferBuilder buf, final int color) {
		buf.vertex(x, 		y, z).		color(0).next();
		buf.vertex(x, 		y, z).		color(color).next();
		buf.vertex(x, 		y, topz).	color(color).next();
		buf.vertex(topx, 	y, topz).	color(color).next();
		buf.vertex(topx, 	y, z).		color(color).next();
		buf.vertex(x, 		y, z).		color(color).next();
		buf.vertex(x, 		y, z).		color(0).next();
	}



	private static void drawLine(final double fx, final double fy, final double fz, final double tx, final double ty, final double tz, final BufferBuilder bufferBuilder, final int color) {
		bufferBuilder.vertex(fx, fy, fz).color(0).next();
		bufferBuilder.vertex(fx, fy, fz).color(color).next();
		bufferBuilder.vertex(tx, ty, tz).color(color).next();
		bufferBuilder.vertex(tx, ty, tz).color(0).next();
	}

	public static void render(final WorldRenderContext wrc)
	{
		if (Config.Generic.SHOULD_RENDER.getBooleanValue()) {
			final ChunkPos origin = new ChunkPos(wrc.camera().getBlockPos());

			final int range = Math.min(wrc.world().getSimulationDistance(), Config.Generic.RENDER_RANGE.getIntegerValue());

			ChunkPos.stream(origin, range).forEach(pos -> ClientPosHighlighter.renderChunk(
					wrc, wrc.world().getChunk(pos.getBlockPos(0, 0, 0)))
					);
		}
	}

	private static void renderChunk(final WorldRenderContext wrc, final Chunk chunk) {
		if (chunk instanceof EmptyChunk) {
			return;
		}
		final var data = ChunkComponents.CHUNK_HIGHLIGHTS.get(chunk);
		for (final HighlighterType idents : HighlighterTypes.HIGHLIGHTERS) {
			ClientPosHighlighter.renderChunkType(wrc, data.getPoses(idents), idents);
		}
	}

	private static void renderChunkType(final WorldRenderContext wrc, final Collection<BlockPos> poses, final HighlighterType type) {
		if (!poses.isEmpty()) {
			ClientPosHighlighter.render(wrc.camera(), wrc.world(), poses, type);
		}
	}

	private interface Renderer {
		void draw(final double x, final double y, final double z, final double topx, final double topy, final double topz, final int color, final BufferBuilder buf);
	}
}
