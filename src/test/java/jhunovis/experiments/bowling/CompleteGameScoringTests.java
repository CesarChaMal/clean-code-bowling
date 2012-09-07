package jhunovis.experiments.bowling;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CompleteGameScoringTests {
	
	@Test
	public void allStrikes() throws BowlingException {
		BowlingGame game = new BowlingGame(new int[] { 10, 10, 10, 10, 10, 10,
				10, 10, 10, 10, 10, 10 });
		assertEquals(300, game.getScore());
	}

	@Test
	public void allZero() throws BowlingException {
		BowlingGame game = new BowlingGame(new int[] { 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 });
		assertEquals(0, game.getScore());
	}

	@Test
	public void neitherStrikeNorSpare() throws BowlingException {
		BowlingGame game = new BowlingGame(new int[] { 1, 2, 3, 4, 5, 1, 2, 3,
				4, 5, 1, 2, 3, 4, 5, 1, 2, 3, 4, 5 });
		assertEquals(60, game.getScore());
	}

	@Test
	public void lastSpareExtraRollIsStrike() throws BowlingException {
		BowlingGame game = new BowlingGame(new int[] { 1, 2, 3, 4, 5, 1, 2, 3,
				4, 5, 1, 2, 3, 4, 5, 1, 2, 3, 5, 5, 10 });
		assertEquals(71, game.getScore());
	}

	@Test
	public void allSparesExtraRollIsStrike() throws BowlingException {
		BowlingGame game = new BowlingGame(new int[] { 0, 10, 1, 9, 2, 8, 3, 7,
				4, 6, 5, 5, 6, 4, 7, 3, 8, 2, 9, 1, 10 });
		assertEquals(155, game.getScore());
	}

	@Test
	public void allZeroLastFrameStrikeExtraRollsStrikes()
			throws BowlingException {
		BowlingGame game = new BowlingGame(new int[] { 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 10, 10, 10 });
		assertEquals(30, game.getScore());
	}

	@Test
	public void allZeroLastFrameSpare() throws BowlingException {
		BowlingGame game = new BowlingGame(new int[] { 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 5, 7 });
		assertEquals(17, game.getScore());
	}

}
