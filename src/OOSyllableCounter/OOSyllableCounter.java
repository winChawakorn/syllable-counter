package OOSyllableCounter;

/**
 * A syllable counter, can count syllables from a String. Using O-O style State
 * Machine.
 * 
 * @author Chawakorn Suphepre
 * @version 2017.04.02
 */
public class OOSyllableCounter {
	private final State START = new StartState();
	private final State SINGLE_VOWEL = new SingleVowelState();
	private final State MULTIPLE_VOWEL = new MultipleVowelState();
	private final State CONSONANT = new ConsonantState();
	private final State HYPHEN = new HyphenState();
	private final State NONWORD = new NonWordState();
	private State state;
	private char c = ' ';
	private String word;
	private int index;
	private int syllablesCount = 0;

	/**
	 * Set the state to new state.
	 * 
	 * @param newState
	 *            is the newState to change to
	 */
	public void setState(State newState) {
		this.state = newState;
	}

	/**
	 * Count the syllables of a String.
	 * 
	 * @param word
	 *            is a word to be count the syllable.
	 * @return the number of syllable.
	 */
	public int countSyllables(String word) {
		if (word != null) {
			setState(START);
			syllablesCount = 0;
			this.word = word;
			for (int i = 0; i < word.length(); i++) {
				c = word.charAt(i);
				if (c == '\'')
					continue;
				this.index = i;
				state.handleChar(c);
				state.enterState();
			}
			return syllablesCount;
		} else {
			return 0;
		}
	}

	/**
	 * The start state of the OOSyllableCounter. This state is the start of the
	 * string, no characters processed yet.
	 * 
	 * @author Chawakorn Suphepre
	 * @version 2017.04.02
	 */
	class StartState extends State {
		/**
		 * Handle the character to enter next state. This won't count 'y' with a
		 * voewl.
		 */
		@Override
		public void handleChar(char c) {
			if (isLetter(c)) {
				if (isVowel(c)) {
					setState(SINGLE_VOWEL);
				} else {
					setState(CONSONANT);
				}
			} else if (isHyphen(c)) {
				setState(NONWORD);
			}
		}
	}

	/**
	 * The single vowel state of the OOSyllableCounter. The most recent char is
	 * a vowel, including 'y', that is the first vowel in a vowel group.
	 * 
	 * @author Chawakorn Suphepre
	 * @version 2017.04.02
	 */
	class SingleVowelState extends State {
		/**
		 * Handle the character to enter next state.
		 */
		@Override
		public void handleChar(char c) {
			if (isLetter(c)) {
				if (isVowel(c)) {
					setState(MULTIPLE_VOWEL);
				} else {
					setState(CONSONANT);
				}
			} else {
				if (isHyphen(c)) {
					setState(HYPHEN);
				} else {
					setState(NONWORD);
				}
			}
		}

		/**
		 * Count the syllable when enter this state. It won't count syllable
		 * when the last character is 'e' or 'E' and the syllable is already
		 * have.
		 */
		public void enterState() {
			if (!(index == word.length() - 1 && (c == 'e' || c == 'E') && syllablesCount != 0)) {
				syllablesCount++;
			}

		}

	}

	/**
	 * The multiple vowel state of the OOSyllableCounter. The most recent cahr
	 * is a vowel that follows another vowel(2 or more vowels together).
	 * 
	 * @author Chawakorn Suphepre
	 * @version 2017.04.02
	 */
	class MultipleVowelState extends State {
		/**
		 * Handle the character to enter next state. Stay in this state if the
		 * vowels are placed together
		 */
		@Override
		public void handleChar(char c) {
			if (isLetter(c)) {
				if (!isVowel(c)) {
					setState(CONSONANT);
				}
			} else {
				if (isHyphen(c)) {
					setState(HYPHEN);
				} else {
					setState(NONWORD);
				}
			}
		}
	}

	/**
	 * The hyphen state of the OOSyllableCounter. The most recent character is a
	 * hyphen. In the middle of a word, hyphen behaves like a consonant, but you
	 * cannot have two hyphen together.
	 * 
	 * @author Chawakorn Suphepre
	 * @version 2017.04.02
	 */
	class HyphenState extends State {
		/**
		 * Handle the character to enter next state.
		 */
		@Override
		public void handleChar(char c) {
			if (isLetter(c)) {
				if (isVowel(c)) {
					setState(SINGLE_VOWEL);
				} else {
					setState(CONSONANT);
				}
			} else {
				setState(NONWORD);
			}
		}
	}

	/**
	 * The non word state of the OOSyllableCounter. The character sequence is
	 * not a word. Enter this state if you see any character other than letter
	 * or hyphen.
	 * 
	 * @author Chawakorn Suphepre
	 * @version 2017.04.02
	 */
	class NonWordState extends State {
		/**
		 * Handle the character to enter next state. Make it stay in this state
		 * until the end.
		 */
		@Override
		public void handleChar(char c) {
			// It's not a word, so it's must stay in this state until the end
			// of the word.
		}

		/**
		 * Because it's already not a word, so set the syllable to 0.
		 */
		@Override
		public void enterState() {
			syllablesCount = 0;
		}
	}

	/**
	 * The consonant state of the OOSyllableCounter. The most recent character
	 * is a letter but not a vowel.
	 * 
	 * @author Chawakorn Suphepre
	 * @version 2017.04.02
	 */
	class ConsonantState extends State {
		/**
		 * Handle the character to enter next state.
		 */
		@Override
		public void handleChar(char c) {
			if (isLetter(c)) {
				if (isVowelOrY(c)) {
					setState(SINGLE_VOWEL);
				}
			} else {
				if (isHyphen(c)) {
					setState(HYPHEN);
				} else {
					setState(NONWORD);
				}
			}
		}
	}

	/**
	 * Checked if the character is a vowel,y or not.
	 * 
	 * @param ch
	 *            is a char to be checked
	 * @return true if ch is a vowel or y, false if ch is the another character.
	 */
	public boolean isVowelOrY(char ch) {
		if (isVowel(ch) || ch == 'y' || ch == 'Y')
			return true;
		return false;
	}

	/**
	 * Check if the character is a vowel.
	 * 
	 * @param ch
	 *            is a char to be checked.
	 * @return true if ch is a vowel, false if ch is the another character.
	 */
	public boolean isVowel(char ch) {
		if (ch == 'a' || ch == 'e' || ch == 'i' || ch == 'o' || ch == 'u'
				|| ch == 'A' || ch == 'E' || ch == 'I' || ch == 'O'
				|| ch == 'U')
			return true;
		return false;
	}

	/**
	 * Check if the character is a letter.
	 * 
	 * @param ch
	 *            is a char to be checked.
	 * @return true if ch is a letter, false if ch is not a letter(May be a
	 *         symbol,number or special character).
	 */
	public boolean isLetter(char ch) {
		return Character.isLetter(ch);
	}

	/**
	 * Check if the character is a dash('-').
	 * 
	 * @param ch
	 *            is a char to be checked.
	 * @return true if ch is a dash, false if ch is the another character.
	 */
	public boolean isHyphen(char ch) {
		if (ch == '-')
			return true;
		return false;
	}
}
