package led;

import com.tinkerforge.BrickletDualRelay;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

/**
 * {@link RelayWrapper} switches a {@link BrickletDualRelay} only after
 * Config.relay_safeguard_time milliseconds have passed. It can be interruped to
 * cancel the switching process.
 * 
 * @author Ingo Kauffmann
 */
public class RelayWrapper extends Thread {
	private BrickletDualRelay relay;
	private boolean state1;
	private boolean state2;

	/**
	 * Constructor
	 * 
	 * @param relay
	 * @param relay1State
	 * @param relay2State
	 */
	public RelayWrapper(BrickletDualRelay relay, boolean relay1State, boolean relay2State) {
		this.relay = relay;
		this.state1 = relay1State;
		this.state2 = relay2State;
		if (Config.debug)
			System.out.println("Creating new RelayWrapper");
	}

	@Override
	public void run() {
		try {
			sleep(Config.relay_safeguard_time);
			relay.setState(state1, state2);
		} catch (InterruptedException | TimeoutException | NotConnectedException e) {
			e.printStackTrace();
		}
	}
}
