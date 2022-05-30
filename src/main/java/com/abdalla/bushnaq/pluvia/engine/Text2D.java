package com.abdalla.bushnaq.pluvia.engine;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.utils.Align;

public class Text2D {
	BitmapFont		font;
	String			text;
	int				x;
	int				y;
	private Color	color;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public Text2D(final String text, int x, int y, Color color, final BitmapFont font) {
		this.text = text;
		this.x = x;
		this.y = y;
		this.color = color;
		this.font = font;
	}

	public void draw(PolygonSpriteBatch batch2d) {
		final GlyphLayout layout = new GlyphLayout();
		layout.setText(font, text);
		final float width = layout.width;// contains the width of the current set text
		font.setColor(color);
		font.draw(batch2d, text, x, y, width, Align.left, false);
	}
}