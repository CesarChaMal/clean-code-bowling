package jhunovis.experiments.bowling;

import static org.junit.Assert.*;
import org.junit.Test;

public class BowlingGameTests {

	@Test(expected = BowlingException.class)
	public void invalidRollBelowZero() throws BowlingException {
		BowlingGame game = new BowlingGame();
		game.addRoll(-1);
	}

	@Test(expected = BowlingException.class)
	public void invalidRollAboveTen() throws BowlingException {
		BowlingGame game = new BowlingGame();
		game.addRoll(11);
	}

	@Test(expected = BowlingException.class)
	public void invalidSecondRollBelowZero() throws BowlingException {
		BowlingGame game = new BowlingGame();
		game.addRoll(2);
		game.addRoll(-2);
	}

	@Test(expected = BowlingException.class)
	public void invalidFrameAboveTen() throws BowlingException {
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

	protected BowlingGame rollNineFrames() throws BowlingException {
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
		return game;
	}

	@Test
	public void firstNinesFrameNoStrikeNoSpare() throws BowlingException {
		rollNineFrames();
	}

	protected BowlingGame rollTenFramesLastFrameIsStrike()
			throws BowlingException {
		BowlingGame game = rollNineFrames();

		game.addRoll(10);
		assertTrue(!game.isComplete());
		assertEquals(10, game.getCurrentFrame());
		assertEquals(2, game.getCurrentRoll());
		return game;
	}

	@Test
	public void gameCompleteAfterTenthFrameIsStrike() throws BowlingException {
		rollTenFramesLastFrameIsStrike();
	}

	@Test
	public void strikesAfterTenthFrameIsStrike() throws BowlingException {
		BowlingGame game = rollTenFramesLastFrameIsStrike();

		game.addRoll(10);
		assertTrue(!game.isComplete());

		assertEquals(10, game.getCurrentFrame());
		assertEquals(3, game.getCurrentRoll());
		game.addRoll(10);
		assertTrue(game.isComplete());
	}

	@Test
	public void spareAfterTenthFrameIsStrike() throws BowlingException {
		BowlingGame game = rollTenFramesLastFrameIsStrike();

		game.addRoll(4);
		assertTrue(!game.isComplete());

		assertEquals(10, game.getCurrentFrame());
		assertEquals(3, game.getCurrentRoll());
		game.addRoll(6);
		assertTrue(game.isComplete());
	}

	@Test
	public void notClearedAfterTenthFrameIsStrike() throws BowlingException {
		BowlingGame game = rollTenFramesLastFrameIsStrike();

		game.addRoll(4);
		assertTrue(!game.isComplete());

		assertEquals(10, game.getCurrentFrame());
		assertEquals(3, game.getCurrentRoll());
		game.addRoll(3);
		assertTrue(game.isComplete());
	}

	protected BowlingGame rollTenFramesLastFrameIsSpare()
			throws BowlingException {
		BowlingGame game = rollNineFrames();

		game.addRoll(4);
		assertTrue(!game.isComplete());

		assertEquals(2, game.getCurrentRoll());
		game.addRoll(6);

		assertTrue(!game.isComplete());
		assertEquals(10, game.getCurrentFrame());
		assertEquals(3, game.getCurrentRoll());
		return game;
	}

	@Test
	public void gameCompleteAfterSpareInLastFrame() throws BowlingException {
		rollTenFramesLastFrameIsSpare();
	}

	@Test
	public void noStrikeAfterSpareInLastFrame() throws BowlingException {
		BowlingGame game = rollTenFramesLastFrameIsSpare();
		game.addRoll(7);
		assertTrue(game.isComplete());
	}

	@Test
	public void strikeAfterSpareInLastFrame() throws BowlingException {
		BowlingGame game = rollTenFramesLastFrameIsSpare();

		game.addRoll(10);
		assertTrue(game.isComplete());
	}

	protected void rollTenFramesLastFrameIsNeitherStrikeNorSpare()
			throws BowlingException {
		BowlingGame game = rollNineFrames();

		game.addRoll(4);
		assertTrue(!game.isComplete());

		assertEquals(2, game.getCurrentRoll());
		game.addRoll(5);
		assertTrue(game.isComplete());
	}

	@Test
	public void gameCompleteAfterTenthFrameNoSpareNorStrike()
			throws BowlingException {
		rollTenFramesLastFrameIsNeitherStrikeNorSpare();
	}

	@Test
	public void frameIsNeitherStrikeNorSpare() throws BowlingException {
		BowlingGame game = new BowlingGame();

		game.addRoll(5);
		assertTrue(!game.isSpare(1));
		assertTrue(!game.isStrike(1));
	}

	@Test
	public void frameIsSpare() throws BowlingException {
		BowlingGame game = new BowlingGame();

		game.addRoll(5);
		game.addRoll(5);
		assertTrue(game.isSpare(1));
		assertTrue(!game.isStrike(1));
	}

	@Test
	public void frameIsStrike() throws BowlingException {
		BowlingGame game = new BowlingGame();

		game.addRoll(10);
		assertTrue(!game.isSpare(1));
		assertTrue(game.isStrike(1));
	}


}
