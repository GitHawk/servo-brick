package led;

import java.awt.Color;

import javax.swing.JColorChooser;
import javax.swing.JPanel;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.colorchooser.ColorSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * {@link ChooserControl} uses an {@link JColorChooser} to manipulate the color
 * of the LEDs
 * 
 * @author Ingo Kauffmann
 * @version 1.0
 */
@SuppressWarnings("serial")
public class ChooserControl extends JPanel implements ChangeListener {

	private ExternallyDimmedLED r;
	private ExternallyDimmedLED g;
	private ExternallyDimmedLED b;

	private JColorChooser chooser;

	/**
	 * Constructor
	 * 
	 * @param r
	 * @param g
	 * @param b
	 */
	public ChooserControl(ExternallyDimmedLED r, ExternallyDimmedLED g, ExternallyDimmedLED b) {
		this.r = r;
		this.g = g;
		this.b = b;

		chooser = new JColorChooser(new Color(r.getColor(), g.getColor(), b.getColor()));
		AbstractColorChooserPanel[] accp = chooser.getChooserPanels();
		chooser.removeChooserPanel(accp[0]);
		chooser.removeChooserPanel(accp[1]);
		chooser.removeChooserPanel(accp[3]);
		chooser.removeChooserPanel(accp[4]);

		chooser.getSelectionModel().addChangeListener(this);
		add(chooser);
	}

	/**
	 * Detects changes in the {@link JColorChooser} and modifies the RGB value
	 * of the color LEDs
	 */
	@Override
	public void stateChanged(ChangeEvent e) {
		Color c = ((ColorSelectionModel) e.getSource()).getSelectedColor();
		r.setColor(c.getRed());
		g.setColor(c.getGreen());
		b.setColor(c.getBlue());
	}

	/**
	 * Updates the color, when switching tabs
	 */
	@Override
	public void repaint() {
		if (chooser != null) {
			chooser.setColor(new Color(r.getColor(), g.getColor(), b.getColor()));
		}
		super.repaint();
	}
}
