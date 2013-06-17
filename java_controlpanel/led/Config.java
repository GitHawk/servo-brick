package led;

/**
 * {@link Config} contains all constants required for the LEDControl.
 * 
 * @author Ingo Kauffmann
 */
public class Config {
	/**
	 * Enabled debugging information
	 */
	protected static final boolean debug = false;

	/**
	 * Address of the stack or its controlling pc/raspberry/ethernet extension...
	 */
	protected static final String host = new String("192.168.26.11");
	
	/**
	 * Port of the stack or its controlling pc/raspberry/ethernet extension...
	 */
	protected static final int port = 4223;
	
	/**
	 * Unique identifier of the first Master Brick
	 */
	protected static final String UID_MasterBrick_1 = new String("6DesVg");

	/**
	 * Unique identifier of the second Master Brick
	 */
	protected static final String UID_MasterBrick_2 = new String("68yGUZ");

	/**
	 * Unique identifier of the first Servo Brick
	 */
	protected static final String UID_ServoBrick_1 = new String("6CrLvF");

	/**
	 * Unique identifier of the red DualRelay Bricklet
	 */
	protected static final String UID_DualRelayBricklet_R = new String("9bw"); /** Master1 D */

	/**
	 * Unique identifier of the green DualRelay Bricklet
	 */
	protected static final String UID_DualRelayBricklet_G = new String("9b7"); /** Master1 B */

	/**
	 * Unique identifier of the blue DualRelay Bricklet
	 */
	protected static final String UID_DualRelayBricklet_B = new String("9bu"); /** Master1 A */

	/**
	 * Unique identifier of the highside DualRelay Bricklet
	 */
	protected static final String UID_DualRelayBricklet_LowSide = new String("9bs"); /** Master1 C */

	/**
	 * Unique identifier of the lowside DualRelay Bricklet
	 */
	protected static final String UID_DualRelayBricklet_HighSide = new String("9gN"); /** Servo A */

	/**
	 * Unique identifier of the first IO4 Bricklet
	 */
	protected static final String UID_IO4Bricklet_1 = new String("8R7"); /** Servo B */

	/**
	 * Unique identifier of the first Current Bricklet
	 */
	protected static final String UID_CurrentBricklet_1 = new String("dKm"); /** Master2 A */
	
	/**
	 * Number of available steps for the dimmer
	 */
	protected static final short	servo_PWM_steps = 100;
	
	/**
	 * Base period of the pwm signal in Hertz
	 */
	protected static final int		servo_PWM_period = 1000;
	
	/**
	 * Maximum speed of servo - determines the time needed to fully dim the LED
	 */
	protected static final int		servo_PWM_speed = servo_PWM_steps/5;
	
	/**
	 * Relay safeguard time in milliseconds
	 */
	protected static final int		relay_safeguard_time = 1000;
}
