package jhunovis.experiments.bowling;

import org.junit.Test;
import static org.junit.Assert.*;
import static jhunovis.experiments.bowling.BowlingGame.RollProblem;

public class ErrorHandlingTests {

	@Test
	public void canAddRollBelowZero2(){
		BowlingGame game = new BowlingGame();
		assertEquals(RollProblem.ROLL_BELOW_ZERO, game.checkAddRollProblems(-5));
	}

	@Test
	public void canAddValidFirstRoll2(){
		BowlingGame game = new BowlingGame();
		assertEquals(RollProblem.NONE, game.checkAddRollProblems(5));
	}

	@Test
	public void canAddValidSecondRoll2(){
		BowlingGame game = new BowlingGame();
		game.addRoll(5);
		assertEquals(RollProblem.NONE, game.checkAddRollProblems(5));
	}

	@Test
	public void canAddRollForFrameAboveTen2() {
		BowlingGame game = new BowlingGame();
		game.addRoll(2);
		assertEquals(RollProblem.FRAME_SUM_ABOVE_TEN, game.checkAddRollProblems(9));
	}

	@Test
	public void canAddRollAboveTen2() {
		BowlingGame game = new BowlingGame();
		assertEquals(RollProblem.ROLL_ABOVE_TEN, game.checkAddRollProblems(11));		
	}

	@Test
	public void canAddSecondRollBelowZero2() {
		BowlingGame game = new BowlingGame();
		game.addRoll(2);
		assertEquals(RollProblem.ROLL_BELOW_ZERO, game.checkAddRollProblems(-5));
	}

	@Test
	public void canAddTooManyFrames2() {
		BowlingGame game = new BowlingGame();
		for (int i = 0; i < 10; i++) {
			game.addRoll(1);
			game.addRoll(2);
		}
		assertEquals(RollProblem.GAME_COMPLETE, game.checkAddRollProblems(9));
	}

	// ========================
	
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
