package jhunovis.experiments.bowling;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ErrorHandlingTests {

	@Test
	public void canAddRollBelowZero(){
		BowlingGame game = new BowlingGame();
		assertFalse(game.canAddRoll(-5));
	}

	@Test
	public void canAddValidFirstRoll(){
		BowlingGame game = new BowlingGame();
		assertTrue(game.canAddRoll(5));
	}

	@Test
	public void canAddValidSecondRoll(){
		BowlingGame game = new BowlingGame();
		game.addRoll(5);
		assertTrue(game.canAddRoll(5));
	}

	@Test
	public void canAddRollForFrameAboveTen() {
		BowlingGame game = new BowlingGame();
		game.addRoll(2);
		assertFalse(game.canAddRoll(9));
	}

	@Test
	public void canAddRollAboveTen() {
		BowlingGame game = new BowlingGame();
		assertFalse(game.canAddRoll(11));		
	}

	@Test
	public void canAddSecondRollBelowZero() {
		BowlingGame game = new BowlingGame();
		game.addRoll(2);
		assertFalse(game.canAddRoll(-5));
	}

	@Test
	public void canAddTooManyFrames() {
		BowlingGame game = new BowlingGame();
		for (int i = 0; i < 10; i++) {
			game.addRoll(1);
			game.addRoll(2);
		}
		assertFalse(game.canAddRoll(9));
	}
	
	@Test(expected = BowlingException.class)
	public void invalidFrameAboveTen() {
		BowlingGame game = new BowlingGame();
		game.addRoll(2);
		game.addRoll(9);
	}

	@Test(expected = BowlingException.class)
	public void invalidRollAboveTen() {
		BowlingGame game = new BowlingGame();
		game.addRoll(11);
	}

	@Test(expected = BowlingException.class)
	public void invalidRollBelowZero() {
		BowlingGame game = new BowlingGame();
		game.addRoll(-1);
	}

	@Test(expected = BowlingException.class)
	public void invalidSecondRollBelowZero() {
		BowlingGame game = new BowlingGame();
		game.addRoll(2);
		game.addRoll(-2);
	}

	@Test(expected = BowlingException.class)
	public void tooManyFrames() {
		BowlingGame game = new BowlingGame();
		for (int i = 0; i < 10; i++) {
			game.addRoll(1);
			game.addRoll(2);
		}
		game.addRoll(9);
	}

}
