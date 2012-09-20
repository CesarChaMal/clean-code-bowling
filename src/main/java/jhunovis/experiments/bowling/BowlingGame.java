package jhunovis.experiments.bowling;

import java.util.InputMismatchException;
import java.util.Scanner;

/** Compute the score of a bowling game. **/
public class BowlingGame {

	/*
	 * Classifies the frames of a game. Used for keeping track of the game and
	 * its results.
	 * 
	 * NOT_ROLLED is the default for each unrolled frame.
	 * 
	 * FIRST_ROLLED signals that the second roll is due for the current frame.
	 * There is no state for the first roll. This is signaled by the current
	 * frame being NOT_ROLLED.
	 * 
	 * NOT_CLEARED the frame was neither a strike nor a spare; valid only for
	 * rolled frames.
	 * 
	 * SPARE frame was a spare; valid only for rolled games.
	 * 
	 * STRIKE frame was a strike; valid only for rolled games
	 */
	private enum FrameState {
		NOT_ROLLED, FIRST_ROLLED, NOT_CLEARED, SPARE, STRIKE
	};

	private int mCurrentFrame = 0;
	private FrameState[] mFrameState = new FrameState[10];

	{
		for (int i = 0; i < 10; i++)
			mFrameState[i] = FrameState.NOT_ROLLED;
	}

	// stores the individual rolls of a game
	private int[] mRolls = new int[2 * 10 + 2];
	private int mCurrentRoll = 0;

	// Rolls needed to complete the game after a spare or strike in 10th frame
	// are kept track of by these two.
	private boolean mFramesComplete = false; // 10 frames played
	private int mExtraRolls = 0; // but extra rolls may be needed

	public BowlingGame() {
	}

	/** For bulk setup. See {@link #addRoll(int)}.
	 * 
	 * @throws BowlingException whenever {@link #addRolls(int[])} would
	 *     throw one.
	 *  */
	public BowlingGame(int[] rolls) {
		addRolls(rolls);
	}

	/*
	 * Advances the currently play frame. Rolls after a spare or strike in the
	 * 10th frame need special care. Both are counted as if the 11th frame, even
	 * if they exceed the total of 10, which normal frames could not!
	 */
	private void nextFrame() {
		if (mCurrentFrame < 9) {
			mCurrentFrame++;
		} else {
			switch (mFrameState[mCurrentFrame]) {
			case SPARE:
				mExtraRolls = 1;
				break;
			case STRIKE:
				mExtraRolls = 2;
				break;
			default:
				break;
			}
			mFramesComplete = true;
		}
	}
	
	/** Add all rolls in {@code rolls} to the game.
	 * 
	 * @param rolls The rolls to be added.
	 * @throws BowlingException whenever {@link #addRoll(int)} would throw 
	 *    one for any roll in rolls.
	 */
	public void addRolls(int[] rolls) {
		for (int roll : rolls) {
			addRoll(roll);
		}		
	}

	/**
	 * Add the given roll to the game. Advances the game until 10 frames have
	 * been rolled.
	 * 
	 * @param roll
	 *            A number between 1 and 10; Both rolls of a frame may not
	 *            exceed the sum of 10.
	 * @throws BowlingException
	 *             if the roll is not within range 0–10, if the current frame
	 *             would sum up to more than 10, or if the game is already
	 *             complete, i.e. 10 frames have been rolled.
	 */
	public void addRoll(int roll) {
		if (!canAddRoll(roll))
			throw new BowlingException(roll, mRolls);
		if (mExtraRolls > 0) {
			// Rolls after a strike or spare in the 10th frame are handled as
			// extra rolls.
			mRolls[mCurrentRoll++] = roll;
			mExtraRolls--;
		} else if (mFrameState[mCurrentFrame] != FrameState.FIRST_ROLLED) {
			// first roll
			mRolls[mCurrentRoll++] = roll;
			// next frame, if strike,
			if (roll == 10) {
				mFrameState[mCurrentFrame] = FrameState.STRIKE;
				nextFrame();
			} else {
				mFrameState[mCurrentFrame] = FrameState.FIRST_ROLLED;
			}
		} else {
			// second roll
			mRolls[mCurrentRoll] = roll;
			if (roll + mRolls[mCurrentRoll - 1] == 10)
				mFrameState[mCurrentFrame] = FrameState.SPARE;
			else
				mFrameState[mCurrentFrame] = FrameState.NOT_CLEARED;
			mCurrentRoll++;
			nextFrame();
		}
	}

	/**
	 * Game over?
	 * 
	 * @return true, only if last frame has been played. No more rolls are
	 *         allowed then.
	 **/
	public boolean isComplete() {
		return mFramesComplete && mExtraRolls == 0;
	}

	private boolean isFrameState(int frame, FrameState state) {
		if (frame >= 1 && frame <= 11) {
			return mFrameState[frame - 1] == state;
		} else {
			throw new IndexOutOfBoundsException(
					"Frame numbers must range between 1 and 11!");
		}

	}

	/**
	 * Check whether the given roll can be added to the currently played frame.
	 * 
	 * @param roll
	 *            The roll to be checked.
	 * @return true, only if an immediate call to {@code #addRoll(int)} will not
	 *         throw a BowlingException.
	 */
	public boolean canAddRoll(int roll) {
		return !(isComplete() 
				 || (roll < 0 || roll > 10) 
				 || (!isFirstRoll() && mExtraRolls==0 &&
				    (roll + mRolls[mCurrentRoll - 1] > 10)));
	}

	/**
	 * Is given frame a strike?
	 * 
	 * @param frame
	 *            the frame to test, first throw is frame 1!
	 * @return true if frame was a strike.
	 */
	public boolean isStrike(int frame) {
		return isFrameState(frame, FrameState.STRIKE);
	}

	/**
	 * Is given frame a spare?
	 * 
	 * @param frame
	 *            the frame to test, first throw is frame 1!
	 * @return true if frame was a strike.
	 */
	public boolean isSpare(int frame) {
		return isFrameState(frame, FrameState.SPARE);
	}

	/**
	 * Get the game score. Works for both finished and unfinished games.
	 * 
	 * @return The game score between 0 and 300.
	 */
	public int getScore() {
		int roll = 0;
		int score = 0;
		for (int frame = 0; frame < 10; frame++) {
			switch (mFrameState[frame]) {
			case FIRST_ROLLED:
				score += mRolls[roll++];
				break;
			case NOT_CLEARED:
				score += mRolls[roll++] + mRolls[roll++];
				break;
			case SPARE:
				roll += 2;
				score += 10 + mRolls[roll];
				break;
			case STRIKE:
				roll++;
				score += 10 + mRolls[roll] + mRolls[roll + 1];
				break;
			case NOT_ROLLED:
				return score;
			}
		}
		return score;
	}

	/**
	 * Return the current roll of the active frame.
	 * 
	 * @return 1 for the first roll, 2 for the second. The last frame may has a
	 *         third roll, if it was a spare or strike.
	 */
	public int getCurrentRoll() {
		if (mFramesComplete) {
			// This awkward piece counts the roll of last last frame, which my
			// be up to 3.
			return (mFrameState[mCurrentFrame] == FrameState.SPARE) 
					? 3	: 4 - mExtraRolls;
		} else {
			return (mFrameState[mCurrentFrame] == FrameState.FIRST_ROLLED) 
					? 2	: 1;
		}
	}

	/**
	 * Return whether the current roll is the first roll of the current frame.
	 * 
	 * @return getCurrentRoll() == 1
	 */
	public boolean isFirstRoll() {
		return getCurrentRoll() == 1;
	}

	/**
	 * Return the number of the active frame, e.g. where the next roll to be
	 * recorded for.
	 * 
	 * @return The number of the active frame, starting with 1 for the first
	 *         frame.
	 */
	public int getCurrentFrame() {
		return mCurrentFrame + 1;
	}

	public static void main(String[] args) {
		BowlingGame game = new BowlingGame();

		System.out.println("BOWLING! Bitte Ergebnisse eingeben!");
		int roll = 0;
		do {
			int curFrame = game.getCurrentFrame();
			int curRoll = game.getCurrentRoll();
			System.out.printf("Ergebnis für Frame %d, Wurf %d: ", curFrame,
					curRoll);
			try {
				roll = new Scanner(System.in).nextInt();
			} catch (InputMismatchException ex) {
				System.out
						.println("Bitte Ganzzahl zwischen 1 und 10 eingeben!");
				continue;
			}
			if (game.canAddRoll(roll)) {
				game.addRoll(roll);
			} else {
				System.out.println("This is not a valid roll!");
			}
			if (game.isSpare(curFrame))
				System.out.print("SPARE! ");
			else if (game.isStrike(curFrame))
				System.out.print("STRIKE! ");
			System.out.printf("Zwischenstand: %d%n", game.getScore());
		} while (!game.isComplete());
		System.out.printf("Endstand: %d%n", game.getScore());
	}

}
