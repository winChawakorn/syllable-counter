package OOSyllableCounter;

/**
 * The state class for the OOSylbleCounter.
 * 
 * @author Chawakorn Suphepre
 * @version 2017.04.02
 */
abstract class State {
	public abstract void handleChar(char c);

	public void enterState() {
	};
}
