package led;

import com.tinkerforge.BrickServo;
import com.tinkerforge.BrickletDualRelay;
import com.tinkerforge.BrickletDualRelay.State;
import com.tinkerforge.IPConnection;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

/**
 * This class is used to dim an LED stripe which is switched internally.
 * 
 * A Servo Brick has merely two Bricklet connectors. The first is connected to
 * an IO4 Bricklet, the second to a DualRelay Bricklet. In order to limit
 * programming complexity, only the Servo Brick firmware is modified. Hence two
 * internally dimmed LED stripes have to share two DualRelay Bricklets.
 * 
 * The DualRelay bricklet connected to the Servo brick is switched by the
 * modified Servo Brick firmware between OFF and DIMMED, while the other
 * Bricklet can be controlled remotely and switches between DIMMED and ON!
 * 
 * 
 * @author Ingo Kauffmann
 */
public class InternallyDimmedLED extends ExternallyDimmedLED {

	private boolean relay1_GND;

	private BrickletDualRelay relay;

	/**
	 * 0 = off 1 = dimmed 2 = on
	 */
	protected int state;

	/**
	 * Constructor
	 * 
	 * @param name
	 * @param brickletlow
	 * @param brickletLow
	 * @param relay1_GND
	 * @param servo
	 * @param servoNum
	 * @param ipcon
	 * @throws TimeoutException
	 */
	public InternallyDimmedLED(String name, BrickletDualRelay brickletLow, boolean relay1_GND, BrickServo servo, int servoNum, IPConnection ipcon) {
		super(name, servo, servoNum);
		this.relay1_GND = relay1_GND;
		this.relay = brickletLow;

		lastRelaySwitch = System.currentTimeMillis();
		try {
			State highState = relay.getState();

			if (relay1_GND) {
				if (highState.relay1) {
					/** Relay 1 on = B => GND */
					state = 2;
				} else {
					/** Relay 1 off = B => DIMMED */
					state = 1;
				}
			} else {
				if (highState.relay2) {
					/** Relay 2 on = B => GND */
					state = 2;
				} else {
					/** Relay 2 off = B => DIMMED */
					state = 1;
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
	 * Switches the DualRelay Bricklet and changes the internal state.
	 * 
	 * @param newState
	 */
	@Override
	public void changeState(int newState) {
		if (newState == state)
			return;

		try {
			State high = relay.getState();
			if (Config.debug) {
				System.out.println(getName() + ".changeState() " + relay1_GND + " " + newState + " " + high.toString());
			}
			if ((System.currentTimeMillis() - lastRelaySwitch) > Config.relay_safeguard_time) {
				if (relay1_GND) {
					if (newState == 1) {
						/** Switching to dimmed */
						relay.setState(false, high.relay2);
					} else if (newState == 2) {
						/** Switching to on */
						relay.setState(true, high.relay2);
					} else {
						/** Switching to off */
						relay.setState(false, high.relay2);
					}
				} else {
					if (newState == 1) {
						/** Switching to dimmed */
						relay.setState(high.relay1, false);
					} else if (newState == 2) {
						/** Switching to on */
						relay.setState(high.relay1, true);
					} else {
						/** Switching to off */
						relay.setState(high.relay1, false);
					}
				}
			}
			lastRelaySwitch = System.currentTimeMillis();

			if (Config.debug) {
				high = relay.getState();
				System.out.println(getName() + ".changeState() " + relay1_GND + " " + newState + " " + high.toString());
			}
			state = newState;
		} catch (TimeoutException | NotConnectedException e) {
			e.printStackTrace();
		}
	}
}
