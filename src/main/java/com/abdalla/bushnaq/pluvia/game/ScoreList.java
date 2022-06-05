package com.abdalla.bushnaq.pluvia.game;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.abdalla.bushnaq.pluvia.desktop.Context;

public class ScoreList extends HashMap<String, TreeSet<Score>> {
	protected Logger	logger	= LoggerFactory.getLogger(this.getClass());
	int					size;

	public int getSize() {
		return size;
	}

	long changed;

	public long getChanged() {
		return changed;
	}

	public ScoreList(int size) {
		this.size = size;
	}

	public void add(String game, int score, int steps, long relativeTime, String userName) {
		if (score > 0) {
			add(new Score(game, score, steps, relativeTime, System.currentTimeMillis(), userName));
		}
	}

	private void add(Score score) {
		TreeSet<Score> treeSet = get(score.getGame());
		if (treeSet == null) {
			treeSet = new TreeSet<Score>();
			put(score.getGame(), treeSet);
		}
		treeSet.add(score);
		if (treeSet.size() > size) {
			treeSet.remove(treeSet.last());
		}
		changed = System.currentTimeMillis();
		writeToDisk();
	}

	public void init(GameList gameList) {
		initGames(gameList);
		try {
			readFromDisk();
		} catch (Exception e) {
			logger.warn("score file corrupt, cleared score!");
			clear();
			initGames(gameList);
			logger.warn(e.getMessage(), e);
		}
//		writeToDisk();
	}

	private void initGames(GameList gameList) {
		for (Game game : gameList) {
			if (game.getName().equals(GameName.FIVE.name()))
				break;
			for (int i = 0; i < size; i++) {
				TreeSet<Score> treeSet = get(game.getName());
				if (treeSet == null) {
					treeSet = new TreeSet<Score>();
					put(game.getName(), treeSet);
				}
			}
		}
	}

	protected void writeToDisk() {
		try {
			XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(Context.getConfigFolderName() + "/score.xml")));
			write(encoder);
			encoder.close();
		} catch (FileNotFoundException e) {
			logger.warn(e.getMessage(), e);
		}
	}

//	protected void readFromDisk() {
//		try {
//			XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(getLastGameName())));
//			readFromDisk(decoder);
//			NrOfTotalStones = ((Integer) decoder.readObject()).intValue();
//			game.score = ((Integer) decoder.readObject()).intValue();
//			game.relativeTime = ((Long) decoder.readObject()).longValue();
//			decoder.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	protected void readFromDisk(XMLDecoder aDecoder) {
//		for (int y = height - 1; y >= 0; y--) {
//			for (int x = 0; x < width; x++) {
//				boolean exits = ((Boolean) aDecoder.readObject()).booleanValue();
//				if (exits) {
//					int type = ((Integer) aDecoder.readObject()).intValue();
//					patch[x][y] = createStone(x, y, type);
//					patch[x][y].readFromDisk(aDecoder);
//				} else {
//				}
//			}
//		}
//	}
	protected void write(XMLEncoder e) {
		e.writeObject(keySet().size());
		for (String name : keySet()) {
			e.writeObject(name);
			e.writeObject(get(name).size());
			for (Score score : get(name)) {
				score.write(e);
			}
		}
	}

	protected void readFromDisk() throws FileNotFoundException {
		XMLDecoder encoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(Context.getConfigFolderName() + "/score.xml")));
		read(encoder);
		encoder.close();
	}

	private void read(XMLDecoder e) {
		Integer numberOfGames = (Integer) e.readObject();
		for (int gameIndex = 0; gameIndex < numberOfGames; gameIndex++) {
			String	name			= (String) e.readObject();
			Integer	numberOfScores	= (Integer) e.readObject();
			for (int scoreIndex = 0; scoreIndex < numberOfScores; scoreIndex++) {
				Score score = new Score();
				score.read(e);
				add(score);
			}
		}
	}

}
