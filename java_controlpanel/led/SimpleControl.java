package led;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * {@link SimpleControl} displays a slider for every LED channel
 * 
 * @author Ingo Kauffmann
 * @version 1.0
 */
@SuppressWarnings("serial")
public class SimpleControl extends JPanel implements ChangeListener {

	private final ExternallyDimmedLED r;
	private final ExternallyDimmedLED g;
	private final ExternallyDimmedLED b;
	private final InternallyDimmedLED w;
	private final InternallyDimmedLED t;

	private JSlider rSlider;
	private JSlider gSlider;
	private JSlider bSlider;
	private JSlider wSlider;
	private JSlider tSlider;

	private JTextField rField;
	private JTextField gField;
	private JTextField bField;
	private JTextField wField;
	private JTextField tField;

	/**
	 * Constructor
	 * 
	 * @param r
	 * @param g
	 * @param b
	 * @param w
	 * @param t
	 */
	public SimpleControl(ExternallyDimmedLED r, ExternallyDimmedLED g, ExternallyDimmedLED b, InternallyDimmedLED w, InternallyDimmedLED t) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.w = w;
		this.t = t;

		/**
		 * Setting up the text fields for manual input
		 */
		rField = new JTextField(2);
		rField.setHorizontalAlignment(JTextField.TRAILING);
		gField = new JTextField(2);
		gField.setHorizontalAlignment(JTextField.TRAILING);
		bField = new JTextField(2);
		bField.setHorizontalAlignment(JTextField.TRAILING);
		wField = new JTextField(2);
		wField.setHorizontalAlignment(JTextField.TRAILING);
		tField = new JTextField(2);
		tField.setHorizontalAlignment(JTextField.TRAILING);

		/**
		 * KeyListener to detect [Enter] strokes
		 */
		KeyListener kl = new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
				if (e.getKeyChar() == (char) 10) {
					if (e.getSource().equals(rField)) {
						SimpleControl.this.rSlider.setValue(Integer.valueOf(rField.getText()));
					} else if (e.getSource().equals(gField)) {
						SimpleControl.this.gSlider.setValue(Integer.valueOf(gField.getText()));
					} else if (e.getSource().equals(bField)) {
						SimpleControl.this.bSlider.setValue(Integer.valueOf(bField.getText()));
					} else if (e.getSource().equals(wField)) {
						SimpleControl.this.wSlider.setValue(Integer.valueOf(wField.getText()));
					} else if (e.getSource().equals(tField)) {
						SimpleControl.this.tSlider.setValue(Integer.valueOf(tField.getText()));
					}
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}
		};

		rField.addKeyListener(kl);
		gField.addKeyListener(kl);
		bField.addKeyListener(kl);
		wField.addKeyListener(kl);
		tField.addKeyListener(kl);

		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		/**
		 * Setting up all 5 sliders
		 */
		int pos = r.getDutyCycle();
		assert (pos < 0 || pos > Config.servo_PWM_steps);
		rSlider = new JSlider(JSlider.VERTICAL, 0, Config.servo_PWM_steps, pos);
		rSlider.setPreferredSize(new Dimension(50, 250));
		rSlider.setMajorTickSpacing(Config.servo_PWM_steps / 4);
		rSlider.setMinorTickSpacing(1);
		rSlider.setPaintTicks(true);
		rSlider.setPaintLabels(true);
		rSlider.addChangeListener(this);
		rField.setText("" + pos);

		JPanel rPanel = new JPanel();
		rPanel.setLayout(new BoxLayout(rPanel, BoxLayout.Y_AXIS));
		rPanel.setPreferredSize(new Dimension(100, 300));
		rPanel.setSize(rPanel.getPreferredSize());
		rPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(r.getName()), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		rPanel.add(rSlider);
		rPanel.add(Box.createRigidArea(new Dimension(0, 5)));
		JPanel p = new JPanel();
		p.add(Box.createRigidArea(new Dimension(2, 5)), BorderLayout.WEST);
		p.add(rField);
		p.add(Box.createRigidArea(new Dimension(2, 5)), BorderLayout.EAST);
		rPanel.add(p);

		c.gridx = 1;

		add(rPanel, c);

		pos = g.getDutyCycle();
		assert (pos < 0 || pos > Config.servo_PWM_steps);
		gSlider = new JSlider(JSlider.VERTICAL, 0, Config.servo_PWM_steps, pos);
		gSlider.setPreferredSize(new Dimension(50, 250));
		gSlider.setMajorTickSpacing(Config.servo_PWM_steps / 4);
		gSlider.setMinorTickSpacing(1);
		gSlider.setPaintTicks(true);
		gSlider.setPaintLabels(true);
		gSlider.addChangeListener(this);
		gField.setText("" + pos);

		JPanel gPanel = new JPanel();
		gPanel.setLayout(new BoxLayout(gPanel, BoxLayout.Y_AXIS));
		gPanel.setPreferredSize(new Dimension(100, 300));
		gPanel.setSize(gPanel.getPreferredSize());
		gPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(g.getName()), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		gPanel.add(gSlider);
		gPanel.add(Box.createRigidArea(new Dimension(0, 5)));
		p = new JPanel();
		p.add(Box.createRigidArea(new Dimension(2, 5)), BorderLayout.WEST);
		p.add(gField);
		p.add(Box.createRigidArea(new Dimension(2, 5)), BorderLayout.EAST);
		gPanel.add(p);

		c.gridx = 2;

		add(gPanel, c);

		pos = b.getDutyCycle();
		assert (pos < 0 || pos > Config.servo_PWM_steps);
		bSlider = new JSlider(JSlider.VERTICAL, 0, Config.servo_PWM_steps, pos);
		bSlider.setPreferredSize(new Dimension(50, 250));
		bSlider.setMajorTickSpacing(Config.servo_PWM_steps / 4);
		bSlider.setMinorTickSpacing(1);
		bSlider.setPaintTicks(true);
		bSlider.setPaintLabels(true);
		bSlider.addChangeListener(this);
		bField.setText("" + pos);

		JPanel bPanel = new JPanel();
		bPanel.setLayout(new BoxLayout(bPanel, BoxLayout.Y_AXIS));
		bPanel.setPreferredSize(new Dimension(100, 300));
		bPanel.setSize(bPanel.getPreferredSize());
		bPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(b.getName()), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		bPanel.add(bSlider);
		bPanel.add(Box.createRigidArea(new Dimension(0, 5)));
		p = new JPanel();
		p.add(Box.createRigidArea(new Dimension(2, 5)), BorderLayout.WEST);
		p.add(bField);
		p.add(Box.createRigidArea(new Dimension(2, 5)), BorderLayout.EAST);
		bPanel.add(p);

		c.gridx = 3;

		add(bPanel, c);

		pos = w.getDutyCycle();
		assert (pos < 0 || pos > Config.servo_PWM_steps);
		wSlider = new JSlider(JSlider.VERTICAL, 0, Config.servo_PWM_steps, pos);
		wSlider.setPreferredSize(new Dimension(50, 250));
		wSlider.setMajorTickSpacing(Config.servo_PWM_steps / 4);
		wSlider.setMinorTickSpacing(1);
		wSlider.setPaintTicks(true);
		wSlider.setPaintLabels(true);
		wSlider.addChangeListener(this);
		wField.setText("" + pos);

		JPanel wPanel = new JPanel();
		wPanel.setLayout(new BoxLayout(wPanel, BoxLayout.Y_AXIS));
		wPanel.setPreferredSize(new Dimension(100, 300));
		wPanel.setSize(wPanel.getPreferredSize());
		wPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(w.getName()), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		wPanel.add(wSlider);
		wPanel.add(Box.createRigidArea(new Dimension(0, 5)));
		p = new JPanel();
		p.add(Box.createRigidArea(new Dimension(2, 5)), BorderLayout.WEST);
		p.add(wField);
		p.add(Box.createRigidArea(new Dimension(2, 5)), BorderLayout.EAST);
		wPanel.add(p);

		c.gridx = 4;

		add(wPanel, c);

		pos = t.getDutyCycle();
		assert (pos < 0 || pos > Config.servo_PWM_steps);
		tSlider = new JSlider(JSlider.VERTICAL, 0, Config.servo_PWM_steps, pos);
		tSlider.setPreferredSize(new Dimension(50, 250));
		tSlider.setMajorTickSpacing(Config.servo_PWM_steps / 4);
		tSlider.setMinorTickSpacing(1);
		tSlider.setPaintTicks(true);
		tSlider.setPaintLabels(true);
		tSlider.addChangeListener(this);
		tField.setText("" + pos);

		JPanel tPanel = new JPanel();
		tPanel.setLayout(new BoxLayout(tPanel, BoxLayout.Y_AXIS));
		tPanel.setPreferredSize(new Dimension(100, 300));
		tPanel.setSize(tPanel.getPreferredSize());
		tPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(t.getName()), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		tPanel.add(tSlider);
		tPanel.add(Box.createRigidArea(new Dimension(0, 5)));
		p = new JPanel();
		p.add(Box.createRigidArea(new Dimension(2, 5)), BorderLayout.WEST);
		p.add(tField);
		p.add(Box.createRigidArea(new Dimension(2, 5)), BorderLayout.EAST);
		tPanel.add(p);

		c.gridx = 5;

		add(tPanel, c);

		/**
		 * Adding a MouseWheelListener to the sliders
		 */
		MouseWheelListener mwl = new MouseWheelListener() {

			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				if (e.getSource() instanceof JSlider) {
					JSlider slider = (JSlider) e.getSource();
					int notches = e.getWheelRotation();
					if (slider.equals(rSlider)) {
						rSlider.setValue(rSlider.getValue() - notches * e.getScrollAmount());
					} else if (slider.equals(gSlider)) {
						gSlider.setValue(gSlider.getValue() - notches * e.getScrollAmount());
					} else if (slider.equals(bSlider)) {
						bSlider.setValue(bSlider.getValue() - notches * e.getScrollAmount());
					} else if (slider.equals(wSlider)) {
						wSlider.setValue(wSlider.getValue() - notches * e.getScrollAmount());
					} else if (slider.equals(tSlider)) {
						tSlider.setValue(tSlider.getValue() - notches * e.getScrollAmount());
					}
				}
			}
		};
		rSlider.addMouseWheelListener(mwl);
		gSlider.addMouseWheelListener(mwl);
		bSlider.addMouseWheelListener(mwl);
		wSlider.addMouseWheelListener(mwl);
		tSlider.addMouseWheelListener(mwl);
	}

	/**
	 * Updates the {@link JSlider} and {@link JTextField} when switching tabs.
	 */
	@Override
	public void repaint() {
		if (rSlider != null) {
			rSlider.setValue(r.getDutyCycle());
			rField.setText("" + r.getDutyCycle());
			gSlider.setValue(g.getDutyCycle());
			gField.setText("" + g.getDutyCycle());
			bSlider.setValue(b.getDutyCycle());
			bField.setText("" + b.getDutyCycle());
			wSlider.setValue(w.getDutyCycle());
			wField.setText("" + w.getDutyCycle());
			tSlider.setValue(t.getDutyCycle());
			tField.setText("" + t.getDutyCycle());
		}
		super.repaint();
	}

	/**
	 * Detects changes in the {@link JSlider} and modifies the brightness value
	 * of the LEDs
	 */
	@Override
	public void stateChanged(ChangeEvent e) {
		JSlider slider = (JSlider) e.getSource();
		if (slider.equals(rSlider)) {
			if (!slider.getValueIsAdjusting()) {
				r.setBrightness(slider.getValue());
			}
			rField.setText("" + slider.getValue());
		} else if (slider.equals(gSlider)) {
			if (!slider.getValueIsAdjusting()) {
				g.setBrightness(slider.getValue());
			}

			gField.setText("" + slider.getValue());
		} else if (slider.equals(bSlider)) {
			if (!slider.getValueIsAdjusting()) {
				b.setBrightness(slider.getValue());
			}

			bField.setText("" + slider.getValue());
		} else if (slider.equals(wSlider)) {
			if (!slider.getValueIsAdjusting()) {
				w.setBrightness(slider.getValue());
			}

			wField.setText("" + slider.getValue());
		} else if (slider.equals(tSlider)) {
			if (!slider.getValueIsAdjusting()) {
				t.setBrightness(slider.getValue());
			}

			tField.setText("" + slider.getValue());
		} else {
			return;
		}
	}
}
