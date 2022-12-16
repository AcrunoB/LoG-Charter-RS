package log.charter.gui.chartPanelDrawers.drawableShapes;

import java.awt.Color;
import java.awt.Graphics;

import log.charter.util.Position2D;

class CenteredTextWithBackground implements DrawableShape {
	private final Position2D position;
	private final String text;
	private final Color backgroundColor;
	private final Color textColor;

	public CenteredTextWithBackground(final Position2D position, final String text, final Color backgroundColor,
			final Color textColor) {
		this.position = position;
		this.text = text;
		this.backgroundColor = backgroundColor;
		this.textColor = textColor;
	}

	@Override
	public void draw(final Graphics g) {
		final int width = g.getFontMetrics().stringWidth(text);
		final int height = g.getFontMetrics().getAscent() - g.getFontMetrics().getDescent();
		final int offsetX = width / 2;
		final int offsetY = height / 2;

		final int textX = position.x - offsetX;
		final int textY = position.y + offsetY;

		if (backgroundColor != null) {
			final int bgY = textY - height;
			g.setColor(backgroundColor);
			g.fillRect(textX - 1, bgY - 2, width + 1, height + 4);
		}

		g.setColor(textColor);
		g.drawString(text, textX, textY - 1);
	}

}
