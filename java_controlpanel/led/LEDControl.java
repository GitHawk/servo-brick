package led;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import com.tinkerforge.BrickMaster;
import com.tinkerforge.BrickServo;
import com.tinkerforge.BrickServo.PositionReachedListener;
import com.tinkerforge.BrickletDualRelay;
import com.tinkerforge.BrickletIO4;
import com.tinkerforge.IPConnection;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

/**
 * Grafical  control interface for multiple common anode RGB+WW stripes.
 * 
 * @author Ingo Kauffmann
 * @version 1.0
 */
@SuppressWarnings("serial")
public class LEDControl extends JFrame implements WindowListener {

	/**
	 * Launches the control panel
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				LEDControl ledsteuerung = new LEDControl();
				ledsteuerung.setVisible(true);
			}
		});
	}

	/**
	 * Connection to brick daemon/ethernet bricklet
	 */
	private IPConnection ipcon;

	/**
	 * Master Bricks
	 */
	private BrickMaster master1;
	private BrickMaster master2;

	/**
	 * Servo Brick for PWM generation for LowSideSwitches
	 */
	private BrickServo servo;

	/**
	 * Remotely switched DualRelayBricklets
	 */
	private ExternallyDimmedLED r;
	private ExternallyDimmedLED g;
	private ExternallyDimmedLED b;
	
	/**
	 * Internally switched DualRelayBricklets
	 */
	private InternallyDimmedLED w;
	private InternallyDimmedLED t;

	/**
	 * IO4 Bricklet to detect light switch position
	 */
	private BrickletIO4 onOff;

	/**
	 * Simple channelwise control panel 
	 */
	private JPanel simpleControl;

	/**
	 * HSL control panel
	 */
	private JPanel chooserControl;
	
	/**
	 * Listener that detects 0 / 100 % and switches the DualRelayBricklets
	 */
	private BrickServo.PositionReachedListener listener;
	
	/**
	 * Constructor
	 */
	public LEDControl() {
		super("LED Control");
		setSize(620, 380);
		addWindowListener(this);
		try {
			ipcon = new IPConnection();
			ipcon.connect(Config.host, Config.port);
			init(ipcon);

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}

		JTabbedPane tabbedPane = new JTabbedPane();
		simpleControl = new SimpleControl(r, g, b, w, t);
		chooserControl = new ChooserControl(r, g, b);

		tabbedPane.addTab("Kanalsteuerung", simpleControl);
		tabbedPane.addTab("Farbwahl", chooserControl);

		add(tabbedPane);
	}

	/**
	 * Initializes all Bricks and Bricklets.
	 * 
	 * @param ipcon
	 * @throws Exception
	 */
	private void init(IPConnection ipcon) {
		master1 = new BrickMaster(Config.UID_MasterBrick_1, ipcon);
		master2 = new BrickMaster(Config.UID_MasterBrick_2, ipcon);

		servo = new BrickServo(Config.UID_ServoBrick_1, ipcon);

		listener = new BrickServo.PositionReachedListener() {
			@Override
			public void positionReached(short servoNum, short position) {
				if (Config.debug)
					System.out.println("LEDControl:PositionReachedListener:positionReached(" + servoNum + "," + position + ")");
				if (servoNum == r.servoNum) {
					if (position <= 1) {
						r.changeState(0);
					} else if (position == Config.servo_PWM_steps) {
						r.changeState(2);
					}
				} else if (servoNum == g.servoNum) {
					if (position <= 1) {
						g.changeState(0);
					} else if (position == Config.servo_PWM_steps) {
						g.changeState(2);
					}
				} else if (servoNum == b.servoNum) {
					if (position <= 1) {
						b.changeState(0);
					} else if (position == Config.servo_PWM_steps) {
						b.changeState(2);
					}
				}
			}
		};

		try {
			servo.addPositionReachedListener(listener);
			servo.enablePositionReachedCallback();
		} catch (TimeoutException | NotConnectedException e) {
			e.printStackTrace();
		}
		
		r = new ExternallyDimmedLED("Rot", Config.UID_DualRelayBricklet_R, servo, 0, ipcon);
		g = new ExternallyDimmedLED("Grün", Config.UID_DualRelayBricklet_G, servo, 1, ipcon);
		b = new ExternallyDimmedLED("Blau", Config.UID_DualRelayBricklet_B, servo, 2, ipcon);

		BrickletDualRelay relayLow = new BrickletDualRelay(Config.UID_DualRelayBricklet_LowSide, ipcon);
		w = new InternallyDimmedLED("Weiß", relayLow, true, servo, 3, ipcon);
		t = new InternallyDimmedLED("Tisch", relayLow, false, servo, 4, ipcon);

		try {
			onOff = new BrickletIO4(Config.UID_IO4Bricklet_1, ipcon);
			onOff.setConfiguration((short) 15, 'i', true);
		} catch (TimeoutException | NotConnectedException e) {
			e.printStackTrace();
		}
	}

	
	@Override
	public void windowOpened(WindowEvent e) {
	}

	/**
	 * Detects window close event, removes the {@link PositionReachedListener} and disconnects before closing the GUI
	 */
	@Override
	public void windowClosing(WindowEvent e) {
		try {
			servo.removePositionReachedListener(listener);
			ipcon.disconnect();
		} catch (NotConnectedException nce) {
			nce.printStackTrace();
		}
		dispose();
	}

	@Override
	public void windowClosed(WindowEvent e) {
		System.exit(0);
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}
}
