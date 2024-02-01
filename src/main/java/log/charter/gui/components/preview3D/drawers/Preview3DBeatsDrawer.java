package log.charter.gui.components.preview3D.drawers;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static log.charter.gui.components.preview3D.Preview3DUtils.closeDistance;
import static log.charter.gui.components.preview3D.Preview3DUtils.getChartboardYPosition;
import static log.charter.gui.components.preview3D.Preview3DUtils.getFretPosition;
import static log.charter.gui.components.preview3D.Preview3DUtils.getTimePosition;
import static log.charter.gui.components.preview3D.Preview3DUtils.visibility;
import static log.charter.song.notes.IPosition.findFirstIdAfterEqual;
import static log.charter.song.notes.IPosition.findLastIdBeforeEqual;
import static log.charter.util.ColorUtils.setAlpha;

import java.awt.Color;

import org.lwjgl.opengl.GL30;

import log.charter.data.ChartData;
import log.charter.data.config.Config;
import log.charter.gui.ChartPanelColors.ColorLabel;
import log.charter.gui.components.preview3D.Preview3DUtils;
import log.charter.gui.components.preview3D.glUtils.BufferedTextureData;
import log.charter.gui.components.preview3D.glUtils.Matrix4;
import log.charter.gui.components.preview3D.glUtils.Point3D;
import log.charter.gui.components.preview3D.glUtils.TextTexturesHolder;
import log.charter.gui.components.preview3D.shaders.ShadersHolder;
import log.charter.gui.components.preview3D.shaders.ShadersHolder.BaseShaderDrawData;
import log.charter.song.Beat;
import log.charter.util.CollectionUtils.ArrayList2;
import log.charter.util.CollectionUtils.Pair;
import log.charter.util.ColorUtils;

public class Preview3DBeatsDrawer {
	private ChartData data;
	private TextTexturesHolder textTexturesHolder;

	public Matrix4 currentMatrix;

	public void init(final ChartData data, final TextTexturesHolder textTexturesHolder) {
		this.data = data;
		this.textTexturesHolder = textTexturesHolder;
	}

	private Pair<Integer, Integer> getBeatIdsRange() {
		final ArrayList2<Beat> beats = data.songChart.beatsMap.beats;
		int beatsFrom = findFirstIdAfterEqual(beats, data.time);
		if (beatsFrom < 0) {
			beatsFrom = 0;
		}
		final int beatsTo = findLastIdBeforeEqual(beats, data.time + visibility);

		return new Pair<>(beatsFrom, beatsTo);
	}

	private void drawBeats(final ShadersHolder shadersHolder) {
		final BaseShaderDrawData drawData = shadersHolder.new BaseShaderDrawData();
		final ArrayList2<Beat> beats = data.songChart.beatsMap.beats;
		final Pair<Integer, Integer> beatIdsRange = getBeatIdsRange();

		final double x0 = getFretPosition(0);
		final double x1 = getFretPosition(Config.frets);
		final double y = getChartboardYPosition(data.currentStrings()) + 0.0001;
		final Color color = ColorLabel.PREVIEW_3D_BEAT.color();
		final Color alpha = ColorUtils.transparent(color);

		for (int i = beatIdsRange.a; i <= beatIdsRange.b; i++) {
			final Beat beat = beats.get(i);
			final int beatTime = beat.position() - data.time;
			final Color beatColor = beatTime > closeDistance ? color
					: setAlpha(color, max(0, min(255, 255 * beatTime / closeDistance)));

			final double z = getTimePosition(beat.position() - data.time);

			if (beat.firstInMeasure) {
				drawData.addVertex(new Point3D(x0, y, z - 0.2), alpha)//
						.addVertex(new Point3D(x1, y, z - 0.2), alpha)//
						.addVertex(new Point3D(x1, y, z - 0.1), beatColor)//
						.addVertex(new Point3D(x0, y, z - 0.1), beatColor)//

						.addVertex(new Point3D(x0, y, z - 0.1), beatColor)//
						.addVertex(new Point3D(x1, y, z - 0.1), beatColor)//
						.addVertex(new Point3D(x1, y, z + 0.1), beatColor)//
						.addVertex(new Point3D(x0, y, z + 0.1), beatColor)//

						.addVertex(new Point3D(x0, y, z + 0.1), beatColor)//
						.addVertex(new Point3D(x1, y, z + 0.1), beatColor)//
						.addVertex(new Point3D(x1, y, z + 0.2), alpha)//
						.addVertex(new Point3D(x0, y, z + 0.2), alpha);
			} else {
				drawData.addVertex(new Point3D(x0, y, z - 0.1), alpha)//
						.addVertex(new Point3D(x1, y, z - 0.1), alpha)//
						.addVertex(new Point3D(x1, y, z), beatColor)//
						.addVertex(new Point3D(x0, y, z), beatColor)//

						.addVertex(new Point3D(x0, y, z), beatColor)//
						.addVertex(new Point3D(x1, y, z), beatColor)//
						.addVertex(new Point3D(x1, y, z + 0.1), alpha)//
						.addVertex(new Point3D(x0, y, z + 0.1), alpha);
			}
		}

		drawData.draw(GL30.GL_QUADS, Matrix4.identity);
	}

	private void drawFretNumber(final ShadersHolder shadersHolder, final int fret, final double y, final double z) {
		final BufferedTextureData textureData = textTexturesHolder.setTextInTexture("" + fret, 128f, Color.WHITE);

		final double x = Preview3DUtils.getFretMiddlePosition(fret);
		final double x0 = x - 0.04;
		final double x1 = x + 0.04;

		final double heightMultiplier = 2 * (x1 - x0) * textureData.height / textureData.width;
		final double y1 = y - heightMultiplier;
		final double y0 = y + heightMultiplier;

		shadersHolder.new BaseTextureShaderDrawData()//
				.addZQuad(x0, x1, y0, y1, z, 0, 1, 0, 1)//
				.draw(GL30.GL_QUADS, Matrix4.identity, textTexturesHolder.getTextureId());
	}

	private void drawFretNumbers(final ShadersHolder shadersHolder) {
		final ArrayList2<Beat> beats = data.songChart.beatsMap.beats;
		final Pair<Integer, Integer> beatIdsRange = getBeatIdsRange();

		final double y = getChartboardYPosition(data.currentStrings());

		GL30.glDisable(GL30.GL_DEPTH_TEST);
		for (int i = beatIdsRange.a; i <= beatIdsRange.b; i++) {
			final Beat beat = beats.get(i);
			if (!beat.firstInMeasure) {
				continue;
			}

			final double z = getTimePosition(beat.position() - data.time);

			for (int fret = 0; fret < Config.frets; fret += 12) {
				drawFretNumber(shadersHolder, fret + 3, y, z);
				drawFretNumber(shadersHolder, fret + 5, y, z);
				drawFretNumber(shadersHolder, fret + 7, y, z);
				drawFretNumber(shadersHolder, fret + 9, y, z);
				drawFretNumber(shadersHolder, fret + 12, y, z);
			}
		}

		GL30.glEnable(GL30.GL_DEPTH_TEST);
	}

	public void draw(final ShadersHolder shadersHolder) {
		drawBeats(shadersHolder);
		drawFretNumbers(shadersHolder);
	}
}
