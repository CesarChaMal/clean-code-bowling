package jhunovis.experiments.bowling;

import static org.junit.Assert.*;
import org.junit.Test;

public class BowlingGameTests {

	protected BowlingGame rollNineFrames() {
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
	public void firstNinesFrameNoStrikeNoSpare() {
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
	public void gameCompleteAfterTenthFrameIsStrike() {
		rollTenFramesLastFrameIsStrike();
	}

	@Test
	public void strikesAfterTenthFrameIsStrike() {
		BowlingGame game = rollTenFramesLastFrameIsStrike();

		game.addRoll(10);
		assertTrue(!game.isComplete());

		assertEquals(10, game.getCurrentFrame());
		assertEquals(3, game.getCurrentRoll());
		game.addRoll(10);
		assertTrue(game.isComplete());
	}

	@Test
	public void spareAfterTenthFrameIsStrike() {
		BowlingGame game = rollTenFramesLastFrameIsStrike();

		game.addRoll(4);
		assertTrue(!game.isComplete());

		assertEquals(10, game.getCurrentFrame());
		assertEquals(3, game.getCurrentRoll());
		game.addRoll(6);
		assertTrue(game.isComplete());
	}

	@Test
	public void notClearedAfterTenthFrameIsStrike() {
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
	public void gameCompleteAfterSpareInLastFrame() {
		rollTenFramesLastFrameIsSpare();
	}

	@Test
	public void noStrikeAfterSpareInLastFrame() {
		BowlingGame game = rollTenFramesLastFrameIsSpare();
		game.addRoll(7);
		assertTrue(game.isComplete());
	}

	@Test
	public void strikeAfterSpareInLastFrame() {
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
	public void frameIsNeitherStrikeNorSpare() {
		BowlingGame game = new BowlingGame();

		game.addRoll(5);
		assertTrue(!game.isSpare(1));
		assertTrue(!game.isStrike(1));
	}

	@Test
	public void frameIsSpare() {
		BowlingGame game = new BowlingGame();

		game.addRoll(5);
		game.addRoll(5);
		assertTrue(game.isSpare(1));
		assertTrue(!game.isStrike(1));
	}

	@Test
	public void frameIsStrike() {
		BowlingGame game = new BowlingGame();

		game.addRoll(10);
		assertTrue(!game.isSpare(1));
		assertTrue(game.isStrike(1));
	}

	@Test
	public void isFirstRoll() {
		BowlingGame game = new BowlingGame();
		assertTrue(game.isFirstRoll());
	}

	@Test
	public void isSecondRoll() {
		BowlingGame game = new BowlingGame();
		game.addRoll(1);
		assertFalse(game.isFirstRoll());
	}

}
