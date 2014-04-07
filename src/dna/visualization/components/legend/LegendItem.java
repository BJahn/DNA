package dna.visualization.components.legend;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

/**
 * An item in the legendlist.
 * 
 * @author Rwilmes
 * 
 */
public class LegendItem extends JPanel {

	private LegendItem thisItem;
	private LegendList parent;
	private Color color;

	// text panel containing the name and value
	private JPanel textPanel;
	private JLabel nameLabel;
	private JLabel valueLabel;
	// toggle y axis button and remove button
	private JButton toggleYAxisButton;
	private JButton removeButton;

	// constructor
	public LegendItem(LegendList parent, String name, Color color) {
		super();
		this.parent = parent;
		this.thisItem = this;
		this.setName(name);
		this.color = color;
		this.setPreferredSize(new Dimension(165, 35));
		this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		// init set-, text- and button panels
		this.textPanel = new JPanel();
		this.textPanel.setPreferredSize(new Dimension(120, 30));
		this.textPanel
				.setLayout(new BoxLayout(this.textPanel, BoxLayout.Y_AXIS));

		// config set-, text- and button panels
		this.textPanel
				.setLayout(new BoxLayout(this.textPanel, BoxLayout.Y_AXIS));

		// only show suffix b from the name, with name = a.b
		int i = name.length() - 1;
		while (i > 0 && name.charAt(i) != '.') {
			i--;
		}
		String nameSuffix = name.substring(i + 1);

		this.nameLabel = new JLabel(nameSuffix);
		this.nameLabel.setForeground(color);
		this.textPanel.add(this.nameLabel);
		this.valueLabel = new JLabel("V=" + 0.0);
		Font valueFont = new Font(this.valueLabel.getFont().getName(),
				Font.PLAIN, this.valueLabel.getFont().getSize() - 3);
		this.valueLabel.setFont(valueFont);
		this.textPanel.add(this.valueLabel);

		// remove button
		this.removeButton = new JButton("-");
		this.removeButton.setPreferredSize(new Dimension(20, 20));
		this.removeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				thisItem.parent.removeItem(thisItem);
			}
		});
		this.removeButton.setMargin(new Insets(0, 0, 0, 0));
		this.removeButton.setFont(new Font(this.removeButton.getFont()
				.getName(), this.removeButton.getFont().getStyle(), 17));

		// toggle y axis button
		this.toggleYAxisButton = new JButton("Yr");
		this.toggleYAxisButton.setPreferredSize(new Dimension(20, 20));
		this.toggleYAxisButton.setMargin(new Insets(0, 0, 0, 0));
		this.toggleYAxisButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				System.out.println("TOGGLE Y AXIS");
			}
		});

		// add set-, text- and button panels
		c.insets = new Insets(0, 0, 0, 0);
		c.gridx = 0;
		c.gridy = 0;
		this.add(this.textPanel, c);

		c.gridx = 1;
		c.gridy = 0;
		this.add(this.toggleYAxisButton, c);

		c.gridx = 2;
		c.gridy = 0;
		this.add(this.removeButton, c);

	}

	public void setNameLabel(String name) {
		this.nameLabel.setText(name);
	}

	public void setValue(double value) {
		this.valueLabel.setText("V=" + value);
	}

	public void setColor(Color color) {
		this.nameLabel.setForeground(color);
	}
}
