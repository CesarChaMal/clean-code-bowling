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

	/** For bulk setup. See {@link #addRoll(int)}. */
	public BowlingGame(int[] rolls) throws BowlingException {
		for (int roll : rolls) {
			addRoll(roll);
		}
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

	/**
	 * Add the given roll to the game. Advances the game until 10 frames have
	 * been rolled.
	 * 
	 * @param roll
	 *            A number between 1 and 10; Both rolls of a frame may not
	 *            exceed the sum of 10.
	 * @throws BowlingException
	 *             if a invalid number has been passed in, of the game is
	 *             already finished.
	 */
	public void addRoll(int roll) throws BowlingException {
		if (isComplete())
			throw new BowlingException("No more rolls allowed.");
		if (roll < 0 || roll > 10)
			throw new BowlingException("Roll out of bounds.");
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
		} else if (roll + mRolls[mCurrentRoll - 1] <= 10) {
			// second roll
			mRolls[mCurrentRoll] = roll;
			if (roll + mRolls[mCurrentRoll - 1] == 10)
				mFrameState[mCurrentFrame] = FrameState.SPARE;
			else
				mFrameState[mCurrentFrame] = FrameState.NOT_CLEARED;
			mCurrentRoll++;
			nextFrame();
		} else
			throw new BowlingException("Frame out of bounds.");
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
			// This awkward piece counts the roll of last last frame, which my be up to 3. 
			return (mFrameState[mCurrentFrame] == FrameState.SPARE) ? 3 : 4-mExtraRolls;
		} else {
			return (mFrameState[mCurrentFrame] == FrameState.FIRST_ROLLED) ? 2 : 1;
		} 
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
			try {
				game.addRoll(roll);
			} catch (BowlingException ex) {
				ex.printStackTrace();
				System.err.println(ex.getMessage());
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
