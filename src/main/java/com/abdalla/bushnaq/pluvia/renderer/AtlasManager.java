package com.abdalla.bushnaq.pluvia.renderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Colors;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.PixmapPacker;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.VisUI.SkinScale;

public class AtlasManager {
	public TextureAtlas	atlas;
	public BitmapFont	aeroFont;
	public BitmapFont	modelFont;
	public BitmapFont	logoFont;
	public FontData[]	fontDataList	= {										//
			new FontData("model-font", "assets/fonts/Roboto-Bold.ttf", 64),		//
			new FontData("Aero-font", "assets/fonts/Aero.ttf", 128),			//
			new FontData("logo-font", "assets/fonts/Roboto-Thin.ttf", 128),		//
			new FontData("small-font", "assets/fonts/Roboto-Bold.ttf", 10),		//
			new FontData("version-font", "assets/fonts/Roboto-Thin.ttf", 16),	//
	};
	AtlasRegion			systemTextureRegion;
	public BitmapFont	smallFont;
	public BitmapFont	versionFont;

	public AtlasManager() {
	}

	public void dispose() {
		for (final FontData fontData : fontDataList) {
			fontData.font.dispose();
		}
		atlas.dispose();
		VisUI.dispose();
	}

	public void init() {
		initTextures();
		initFonts();
	}

	private void initFonts() {
		for (FontData element : fontDataList) {
			final FontData		fontData	= element;
			final AtlasRegion	atlasRegion	= atlas.findRegion(fontData.name);
			atlasRegion.getRegionWidth();
			atlasRegion.getRegionHeight();
			final PixmapPacker			packer		= new PixmapPacker(atlasRegion.getRegionWidth(), atlasRegion.getRegionHeight(), Format.RGBA8888, 1, false);
			final FreeTypeFontGenerator	generator	= new FreeTypeFontGenerator(Gdx.files.internal(fontData.file));
			final FreeTypeFontParameter	parameter	= new FreeTypeFontParameter();
			parameter.size = (fontData.fontSize);
			parameter.packer = packer;
			final BitmapFont generateFont = generator.generateFont(parameter);
			generator.dispose(); // don't forget to dispose to avoid memory leaks!
			fontData.font = new BitmapFont(generateFont.getData(), atlas.findRegion(fontData.name), true);
			packer.dispose();
			fontData.font.setUseIntegerPositions(false);
		}
		modelFont = fontDataList[0].font;
		aeroFont = fontDataList[1].font;
		logoFont = fontDataList[2].font;
		smallFont = fontDataList[3].font;
		versionFont = fontDataList[4].font;
	}

	private void initTextures() {
		atlas = new TextureAtlas(Gdx.files.internal("assets/atlas/atlas.atlas"));
		systemTextureRegion = atlas.findRegion("system");
		VisUI.load(SkinScale.X2);
		VisUI.getSkin().getFont("default-font").getData().markupEnabled = true;
		VisUI.getSkin().getFont("small-font").getData().markupEnabled = true;
		Colors.put("BOLD", new Color(0x1BA1E2FF));
	}
}
