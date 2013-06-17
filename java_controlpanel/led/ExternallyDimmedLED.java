package led;

import com.tinkerforge.BrickServo;
import com.tinkerforge.BrickletDualRelay;
import com.tinkerforge.BrickletDualRelay.State;
import com.tinkerforge.IPConnection;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

/**
 * This class is used to dim an LED stripe via the Servo Brick and control the DualRelay Bricklet.
 * 
 * @author Ingo Kauffmann
 */
public class ExternallyDimmedLED {

	protected String name;
	protected BrickServo servo;
	protected short servoNum;

	private BrickletDualRelay relay;
	protected long lastRelaySwitch;

	/**
	 * 0 = off 1 = dimmed 2 = on
	 */
	protected int state;

	/**
	 * Constructor.
	 * 
	 * @param name
	 * @param brickletUID
	 * @param servo
	 * @param servoNum
	 * @param ipcon
	 * @throws TimeoutException
	 */
	public ExternallyDimmedLED(String name, String brickletUID, BrickServo servo, int servoNum, IPConnection ipcon) {
		this.name = name;
		this.servo = servo;
		this.servoNum = (short) servoNum;

		relay = new BrickletDualRelay(brickletUID, ipcon);
		lastRelaySwitch = System.currentTimeMillis();

		try {
			State rState = relay.getState();
			if (rState.relay1) {
				/** Relay 1 on = B => GND */
				state = 2;
			} else {
				if (rState.relay2) {
					/** Relay 2 on = B => DIMMED */
					state = 1;
				} else {
					/** Relay 2 off = A => VBAT */
					state = 0;
				}
			}

			servo.setPeriod(this.servoNum, Config.servo_PWM_period);
			servo.setPulseWidth(this.servoNum, 1, Config.servo_PWM_period);
			servo.setDegree(this.servoNum, (short) 0, (short) Config.servo_PWM_steps);
			servo.setAcceleration(this.servoNum, 0xFFFF);
			servo.setVelocity(this.servoNum, Config.servo_PWM_speed);
			servo.enable(this.servoNum);
		} catch (TimeoutException | NotConnectedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Internal Constructor used by {@link LEDNoRelay}
	 * 
	 * @param name
	 * @param servo
	 * @param servoNum
	 */
	protected ExternallyDimmedLED(String name, BrickServo servo, int servoNum) {
		this.name = name;
		this.servo = servo;
		this.servoNum = (short) servoNum;
	}

	/**
	 * Changes the brightness between 0 and Config.servo_PWM_steps
	 * 
	 * @param brightness
	 */
	public void setBrightness(int brightness) {
		if (Config.debug)
			System.out.println(getName() +".setBrightness("+brightness+") old state " + state);
		if (state == 0) {
			if (brightness == 0)
				return;
			if (brightness >= Config.servo_PWM_steps) {
				changeState(2);
				setServo(Config.servo_PWM_steps);
			} else {
				changeState(1);
				setServo(brightness);
			}
		} else if (state == 1) {
			if (brightness <= 0) {
				setServo(0);
			} else if (brightness >= Config.servo_PWM_steps) {
				setServo(Config.servo_PWM_steps);
			} else {
				setServo(brightness);
			}
		} else {
			changeState(0);
			setServo(0);
		}
		if (Config.debug)
			System.out.println(getName() +".setBrightness("+brightness+") new state " + state);
	}

	/**
	 * Transmits the brightness values to the Servo Brick.
	 * 
	 * @param brightness
	 */
	private void setServo(int brightness) {
		try {
			servo.setPosition(this.servoNum, (short) brightness);
		} catch (TimeoutException | NotConnectedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Switches the DualRelay Bricklet and changes the internal state.
	 * 
	 * @param newState
	 */
	public void changeState(int newState) {
		if (newState == state)
			return;

		if (Config.debug)
			System.out.println(getName() +".changeState() time since last switch "+(System.currentTimeMillis() - lastRelaySwitch));

		try {
			if ((System.currentTimeMillis() - lastRelaySwitch) > Config.relay_safeguard_time) {
				if (newState == 1) {
					/** Switching to dimmed */
					relay.setState(false, true);
				} else if (newState == 2) {
					/** Switching to on */
					relay.setState(true, true);
				} else {
					/** Switching to off */
					relay.setState(false, false);
				}
				state = newState;
				lastRelaySwitch = System.currentTimeMillis();
			}
		} catch (TimeoutException | NotConnectedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns the name of the LED.
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the duty cycle of the LED.
	 * 
	 * @return
	 * @throws TimeoutException
	 */
	public int getDutyCycle() {
		if (state == 2)
			return Config.servo_PWM_steps;
		try {
			return (int) servo.getPosition(servoNum);
		} catch (TimeoutException | NotConnectedException e) {
			e.printStackTrace();
			if (Config.debug)
				System.out.println(getName() +".getDutyCycle() returns 0");
			return 0;
		}
	}

	/**
	 * Returns the brightness as a uint8 color value.
	 * 
	 * @return
	 */
	public int getColor() {
		int dc = getDutyCycle();
		if (state == 2) {
			if (Config.debug)
				System.out.println(getName() +".getColor() State is 2, returning 255");
			return 255;
		}
		return (int) Math.round((255.0 * dc) / ((double) Config.servo_PWM_steps));
	}

	/**
	 * Changes the brightness to a uint8 color value.
	 * 
	 * @param c
	 */
	public void setColor(int c) {
		setBrightness((int) Math.round(c * Config.servo_PWM_steps / (double) 255.0));
	}
}
