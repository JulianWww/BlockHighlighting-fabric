package net.denanu.clientblockhighlighting.rendering;

import java.util.HashSet;
import java.util.concurrent.Semaphore;

import org.jetbrains.annotations.Nullable;

import com.mojang.blaze3d.systems.RenderSystem;

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
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;

public class ClientBoxHighlighter {
	private static Semaphore mutex = new Semaphore(1);

	protected static final int RANGE = 10000*1000;

	private static int BASE_COLOR = ColorHelper.Argb.getArgb(255, 255, 255, 255);

	private static HashSet<BlockPos> poses = new HashSet<>();

	public static void highlight(final BlockPos pos) {
		try {
			ClientBoxHighlighter.mutex.acquire();
			ClientBoxHighlighter.poses.add(pos);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		ClientBoxHighlighter.mutex.release();
	}

	public static void unHighlight(final BlockPos pos) {
		try {
			ClientBoxHighlighter.mutex.acquire();
			ClientBoxHighlighter.poses.remove(pos);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		ClientBoxHighlighter.mutex.release();
	}

	public static void highlight(final BlockPos pos, final boolean active) {
		if (active) {
			ClientBoxHighlighter.highlight(pos);
		}
		else {
			ClientBoxHighlighter.unHighlight(pos);
		}
	}

	public static <VillageBoundingBox> void render(final MatrixStack matrixStack, @Nullable final VertexConsumerProvider consumers, final Camera camera, final GameRenderer gameRenderer, final ClientWorld world) {
		RenderSystem.enableDepthTest();
		RenderSystem.setShader(GameRenderer::getPositionColorProgram);
		final Tessellator tessellator = Tessellator.getInstance();
		final BufferBuilder bufferBuilder = tessellator.getBuffer();
		RenderSystem.disableTexture();
		RenderSystem.disableBlend();
		RenderSystem.lineWidth(100.0f);
		bufferBuilder.begin(VertexFormat.DrawMode.DEBUG_LINE_STRIP, VertexFormats.POSITION_COLOR);

		//final Vec3d camPos = camera.getPos();

		for (BlockPos pos : ClientBoxHighlighter.poses) {
			ClientBoxHighlighter.drawBox(pos, bufferBuilder, camera, world.getBlockState(pos).getOutlineShape(world, pos));
		}

		// render script here

		tessellator.draw();
		RenderSystem.lineWidth(1.0f);
		RenderSystem.enableBlend();
		RenderSystem.enableTexture();

	}

	private static void drawBox(final BlockPos pos, final BufferBuilder buf, final Camera camera, final VoxelShape voxel) {
		final double cameraX = camera.getPos().x;
		final double cameraY = camera.getPos().y;
		final double cameraZ = camera.getPos().z;

		double offset = 0.01;

		double xpos = pos.getX() - cameraX;
		double ypos = pos.getY() - cameraY;
		double zpos = pos.getZ() - cameraZ;

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

		ClientBoxHighlighter.drawYalignedSquare(x, 	y, 		z, 	topx, 	topz, 	buf);
		ClientBoxHighlighter.drawYalignedSquare(x, 	topy, 	z, 	topx, 	topz, 	buf);

		ClientBoxHighlighter.drawLine(x, 	y, 	z, 		x, 		topy, z, 	buf);
		ClientBoxHighlighter.drawLine(topx, y, 	z, 		topx, 	topy, z, 	buf);
		ClientBoxHighlighter.drawLine(x, 	y, 	topz, 	x, 		topy, topz, buf);
		ClientBoxHighlighter.drawLine(topx, y, 	topz, 	topx, 	topy, topz,	buf);

	}

	public static void drawYalignedSquare(final double x, final double y, final double z, final double topx, final double topz, final BufferBuilder buf) {
		buf.vertex(x, 		y, z).		color(0).next();
		buf.vertex(x, 		y, z).		color(ClientBoxHighlighter.BASE_COLOR).next();
		buf.vertex(x, 		y, topz).	color(ClientBoxHighlighter.BASE_COLOR).next();
		buf.vertex(topx, 	y, topz).	color(ClientBoxHighlighter.BASE_COLOR).next();
		buf.vertex(topx, 	y, z).		color(ClientBoxHighlighter.BASE_COLOR).next();
		buf.vertex(x, 		y, z).		color(ClientBoxHighlighter.BASE_COLOR).next();
		buf.vertex(x, 		y, z).		color(0).next();
	}



	public static void drawLine(final double fx, final double fy, final double fz, final double tx, final double ty, final double tz, final BufferBuilder bufferBuilder) {
		bufferBuilder.vertex(fx, fy, fz).color(0f, 0f, 0f, 0f).next();
		bufferBuilder.vertex(fx, fy, fz).color(1f, 1f, 1f, 1f).next();
		bufferBuilder.vertex(tx, ty, tz).color(1f, 1f, 1f, 1f).next();
		bufferBuilder.vertex(tx, ty, tz).color(0f, 0f, 0f, 0f).next();
	}

	public static void render(final WorldRenderContext wrc)
	{
		if (!ClientBoxHighlighter.poses.isEmpty())
		{
			ClientBoxHighlighter.render(wrc.matrixStack(), wrc.consumers(), wrc.camera(), wrc.gameRenderer(), wrc.world());
		}
	}
}
