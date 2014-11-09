package sprites;

/**
 * Class which defines a set of animation instructions for a Sprite
 * These instructions include the desired animation row
 * And whether this animation should be repeated
 * @author Nex
 *
 */
public class AnimationInstruction {

	//Attributes
	public final int animationRowIndex;
	public final boolean repeatAnimation;
	
	/**
	 * Constructs a set of animation instructions
	 * @param animRowIndex The row of the spritesheet to play
	 * @param repeat Should this animation be repeated on completion if there is no other animation in sprite animQueue.
	 */
	public AnimationInstruction(int animRowIndex, boolean repeat) {
		animationRowIndex = animRowIndex;
		repeatAnimation = repeat;
	}

}
