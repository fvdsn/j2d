package engine;

/** The Effects are used to define global and temporary events or behaviour
 * that exist independently of other entities.
 */
public class Effect {
	/** The time the effect is applied */
	public float time = 0.0f;	
	/** The duration of the effect. If zero, it is applied only one frame */
	public float duration = 0.0f;
	/** This method is called when the effect is applied */
	public void effect(){}
}
