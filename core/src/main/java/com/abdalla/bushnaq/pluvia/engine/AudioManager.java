package com.abdalla.bushnaq.pluvia.engine;

import java.util.HashMap;
import java.util.Map;

import com.abdalla.bushnaq.pluvia.desktop.Context;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class AudioManager {
	public static final String	DROP	= "drop";
	public static final String	SCORE	= "score";
	public static final String	STICKY	= "sticky";
	public static final String	TILT	= "score";
	public static final String	VANISH	= "vanish";
	private Context				context;
	Map<String, Sound>			map		= new HashMap<>();

	public AudioManager(Context context) {
		this.context = context;
		add(SCORE, "/sound/score.mp3");
		add(TILT, "/sound/tilt.mp3");
		add(STICKY, "/sound/sticky.mp3");
		add(DROP, "/sound/drop.mp3");
		add(VANISH, "/sound/vanish.mp3");
	}

	private void add(String tag, String fileName) {
		Sound sound = Gdx.audio.newSound(Gdx.files.internal(AtlasManager.getAssetsFolderName() + fileName));
		map.put(tag, sound);
	}

	public void dispose() {
		for (Sound sound : map.values()) {
			sound.dispose();
		}
		map.clear();
	}

//	public Sound get(String tag) {
//		return map.get(tag);
//	}

	public void play(String tag) {
		map.get(tag).play((context.getAmbientAudioVolumenProperty()) / 100f);
	}
}
