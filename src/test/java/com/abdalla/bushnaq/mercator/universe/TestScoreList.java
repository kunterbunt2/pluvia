package com.abdalla.bushnaq.mercator.universe;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.abdalla.bushnaq.pluvia.game.Score;
import com.abdalla.bushnaq.pluvia.game.ScoreList;

public class TestScoreList {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@BeforeEach
	void init(TestInfo testInfo) {
		logger.info(String.format("%s", testInfo.getDisplayName()));
	}

	@Test
	public void sortOneGame() {
		ScoreList scoreList = new ScoreList(3);
		{
			scoreList.add("game1", 100, 15, 0, "user-1");
			scoreList.add("game1", 101, 16, 0, "user-2");
			scoreList.add("game1", 102, 17, 0, "user-3");
			scoreList.add("game1", 103, 18, 0, "user-4");
		}

		printResult(scoreList);
	}

	private void printResult(ScoreList scoreList) {
		for (String game : scoreList.keySet()) {
			int lastScore = Integer.MAX_VALUE;
			for (Score score : scoreList.get(game)) {
				assertTrue(lastScore >= score.getScore(), String.format("sorting is wrong score %d is sorted after %d", score.getScore(), lastScore));
				logger.info(String.format("%s %s %d", score.getGame(), score.getUserName(), score.getScore()));
				lastScore = score.getScore();
			}
		}
	}

	@Test
	public void sortTwoGames() {
		ScoreList scoreList = new ScoreList(3);
		{
			scoreList.add("game1", 103, 18, 0, "user-4");
			scoreList.add("game2", 103, 18, 0, "user-4");
			scoreList.add("game1", 50, 18, 0, "user-4");
			scoreList.add("game2", 117, 18, 0, "user-4");
			scoreList.add("game1", 100, 15, 0, "user-1");
			scoreList.add("game2", 101, 16, 0, "user-2");
			scoreList.add("game1", 102, 17, 0, "user-3");
			scoreList.add("game1", 103, 18, 0, "user-4");
		}

		printResult(scoreList);
	}

}
