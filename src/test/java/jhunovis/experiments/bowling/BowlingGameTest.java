package jhunovis.experiments.bowling;

import static org.junit.Assert.*;
import org.junit.Test;

public class BowlingGameTest {

	@Test(expected=BowlingException.class)
	public void invalidRollBelowZero() throws BowlingException {
		BowlingGame game = new BowlingGame();
		game.addRoll(-1);
	}

	@Test(expected=BowlingException.class)
	public void invalidRollAboveTen()  throws BowlingException {
		BowlingGame game = new BowlingGame();
		game.addRoll(11);
	}

	@Test(expected=BowlingException.class)
	public void invalidSecondRollBelowZero() throws BowlingException {
		BowlingGame game = new BowlingGame();
		game.addRoll(2);
		game.addRoll(-2);
	}

	@Test(expected=BowlingException.class)
	public void invalidFrameAboveTen()  throws BowlingException {
		BowlingGame game = new BowlingGame();
		game.addRoll(2);
		game.addRoll(9);
	}

	@Test(expected = BowlingException.class)
	public void tooManyFrames() throws BowlingException {
		BowlingGame game = new BowlingGame();
		for (int i = 0; i < 10; i++) {
			game.addRoll(1);
			game.addRoll(2);
		}
		game.addRoll(9);
	}

	@Test
	public void gameProgress() throws BowlingException {
		BowlingGame game = new BowlingGame();

		for (int i = 0; i < 9; i++) {
			assertEquals(i + 1, game.getCurrentFrame());
			assertEquals(1, game.getCurrentRoll());
			game.addRoll(2);
			assertTrue(!game.isComplete());

			assertEquals(2, game.getCurrentRoll());
			game.addRoll(3);
			assertTrue(!game.isComplete());
		}

		assertEquals(10, game.getCurrentFrame());
		assertEquals(1, game.getCurrentRoll());
		game.addRoll(4);
		assertTrue(!game.isComplete());

		assertEquals(2, game.getCurrentRoll());
		game.addRoll(5);
		assertTrue(game.isComplete());

		// 10th frame is spare
		game = new BowlingGame();
		for (int i = 0; i < 9; i++) {
			game.addRoll(2);
			game.addRoll(3);
		}
		assertEquals(10, game.getCurrentFrame());
		assertEquals(1, game.getCurrentRoll());
		game.addRoll(4);
		assertTrue(!game.isComplete());

		assertEquals(2, game.getCurrentRoll());
		game.addRoll(6);
		assertTrue(!game.isComplete());

		assertEquals(10, game.getCurrentFrame());
		assertEquals(3, game.getCurrentRoll());
		game.addRoll(7);
		assertTrue(game.isComplete());

		// 10th frame is strike
		game = new BowlingGame();
		for (int i = 0; i < 9; i++) {
			game.addRoll(2);
			game.addRoll(3);
		}

		assertEquals(10, game.getCurrentFrame());
		assertEquals(1, game.getCurrentRoll());
		game.addRoll(10);
		assertTrue(!game.isComplete());

		assertEquals(10, game.getCurrentFrame());
		assertEquals(2, game.getCurrentRoll());
		game.addRoll(10);
		assertTrue(!game.isComplete());

		assertEquals(10, game.getCurrentFrame());
		assertEquals(3, game.getCurrentRoll());
		game.addRoll(10);
		assertTrue(game.isComplete());
	}

	@Test
	public void strikesAndSpares() throws BowlingException {
		BowlingGame game = new BowlingGame();

		game.addRoll(5);
		assertTrue(!game.isSpare(1));
		assertTrue(!game.isStrike(1));

		game.addRoll(5);
		assertTrue(game.isSpare(1));
		assertTrue(!game.isStrike(1));

		game.addRoll(10);
		assertTrue(!game.isSpare(2));
		assertTrue(game.isStrike(2));
	}

	@Test
	public void score() throws BowlingException {
		BowlingGame game = new BowlingGame(new int[] { 10, 10, 10, 10, 10, 10,
				10, 10, 10, 10, 10, 10 });
		assertEquals(300, game.getScore());

		game = new BowlingGame(new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0 });
		assertEquals(0, game.getScore());

		game = new BowlingGame(new int[] { 1, 2, 3, 4, 5, 1, 2, 3, 4, 5, 1, 2,
				3, 4, 5, 1, 2, 3, 4, 5 });
		assertEquals(60, game.getScore());

		game = new BowlingGame(new int[] { 1, 2, 3, 4, 5, 1, 2, 3, 4, 5, 1, 2,
				3, 4, 5, 1, 2, 3, 5, 5, 10 });
		assertEquals(71, game.getScore());

		game = new BowlingGame(new int[] { 0, 10, 1, 9, 2, 8, 3, 7, 4, 6, 5, 5,
				6, 4, 7, 3, 8, 2, 9, 1, 10 });
		assertEquals(155, game.getScore());

		// last a strike
		game = new BowlingGame(new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 10, 10, 10 });
		assertEquals(30, game.getScore());

		// last a spare
		game = new BowlingGame(new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 5, 5, 7 });
		assertEquals(17, game.getScore());

		// incomplete games

		game = new BowlingGame(new int[] { 10, 10, 10 });
		assertEquals(60, game.getScore());

		game = new BowlingGame(new int[] { 10, 10, 10, 5 });
		assertEquals(75, game.getScore());

		game = new BowlingGame(new int[] { 1, 2, 3, 4 });
		assertEquals(10, game.getScore());

		game = new BowlingGame(new int[] { 3, 7, 2, 8, 1 });
		assertEquals(24, game.getScore());
	}

}
