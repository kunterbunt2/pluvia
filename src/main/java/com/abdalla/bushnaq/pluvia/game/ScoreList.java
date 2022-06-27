package com.abdalla.bushnaq.pluvia.game;

import java.io.File;
import java.io.IOException;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.abdalla.bushnaq.pluvia.desktop.Context;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

public class ScoreList extends TreeSet<Score> {
	long				changed;											// last time anything changed
	protected Logger	logger	= LoggerFactory.getLogger(this.getClass());
	int					size;

	public ScoreList() {

	}

	public ScoreList(int size) {
		this.size = size;
	}

	private boolean addScore(Score score) {
		boolean	newHeiscore	= false;
		Score	s			= findScore(score);
		if (s != null) {
			if (s.getScore() < score.getScore()) {
				s.set(score);
				newHeiscore = true;
			}
		} else {
			add(score);
			newHeiscore = true;
		}
		if (newHeiscore) {
			changed = System.currentTimeMillis();
			writeToDisk();

		}
		return newHeiscore;
	}

	private Score findScore(Score score) {
		for (Score s : this) {
			if (s.getGame().equals(score.getGame()) && (s.getSeed() == score.getSeed()))
				return s;
		}
		return null;
	}

	public boolean add(String game, int seed, int score, int steps, long relativeTime, String userName) {
		if (score > 0) {
			return addScore(new Score(game, seed, score, steps, relativeTime, System.currentTimeMillis(), userName));
		}
		return false;
	}

	public long getChanged() {
		return changed;
	}

	public int getSize() {
		return size;
	}

	protected void writeToDisk() {
		try {
			ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
			mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
			mapper.writeValue(new File(Context.getConfigFolderName() + "/score.yaml"), this);
		} catch (StreamWriteException e) {
			logger.warn(e.getMessage(), e);
		} catch (DatabindException e) {
			logger.warn(e.getMessage(), e);
		} catch (IOException e) {
			logger.warn(e.getMessage(), e);
		}
	}

}
