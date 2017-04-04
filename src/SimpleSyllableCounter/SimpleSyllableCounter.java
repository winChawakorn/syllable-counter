package SimpleSyllableCounter;

/**
 * A simple syllable counter(non O-O style), can count syllable from a String by
 * using state machine.
 * 
 * @author Chawakorn Suphepre
 * @version 2017.04.02
 */
public class SimpleSyllableCounter {
	/* Current state */
	private State state = State.START;
	/* Current character in a String */
	private char c = ' ';
	private int totalSyllable;

	/**
	 * Count the syllables of a string.
	 * 
	 * @param word
	 *            is a string to be counted syllable.
	 * @return the number of syllables in the string.
	 */
	public int countSyllables(String word) {
		if (word != null) {
			int syllables = 0;
			if (word.length() == 0) {
				state = State.START;
				totalSyllable = 0;
				return 0;
			}
			c = word.charAt(0);
			if (c == '\'')
				countSyllables(word.substring(1));
			switch (state) {
			case START:
				if (isLetter(c)) {
					if (isVowelOrY(c)) {
						state = State.SINGLE_VOWEL;
					} else {
						state = State.CONSONANT;
					}
				} else if (isHyphen(c)) {
					state = State.NONWORD;
				}
				break;
			case CONSONANT:
				if (isVowelOrY(c)) {
					state = State.SINGLE_VOWEL;
				} else if (isLetter(c)) {
					break;
				} else if (isHyphen(c)) {
					state = State.HYPHEN;
				} else {
					state = State.NONWORD;
				}
				break;
			case MULTI_VOWEL:
				if (isLetter(c)) {
					if (!isVowel(c)) {
						state = State.CONSONANT;
					}
				} else {
					if (isHyphen(c)) {
						state = State.HYPHEN;
					} else {
						state = State.NONWORD;
					}
				}
				break;
			case HYPHEN:
				if (isLetter(c)) {
					if (isVowel(c)) {
						state = State.SINGLE_VOWEL;
					} else {
						state = State.CONSONANT;
					}
				} else {
					state = State.NONWORD;
				}
				break;
			case SINGLE_VOWEL:
				if (isLetter(c)) {
					if (isVowel(c)) {
						state = State.MULTI_VOWEL;
					} else {
						state = State.CONSONANT;
					}
				} else {
					if (isHyphen(c)) {
						state = State.HYPHEN;
					} else {
						state = State.NONWORD;
					}
				}
				break;
			case NONWORD:
				break;
			}
			if (!(word.length() == 1 && (c == 'e' || c == 'E') && totalSyllable != 0)
					&& state == State.SINGLE_VOWEL)
				syllables++;
			totalSyllable += syllables;
			return syllables + countSyllables(word.substring(1));
		} else {
			return 0;
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
