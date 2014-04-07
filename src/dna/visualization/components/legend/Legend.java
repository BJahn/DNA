package dna.visualization.components.legend;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EtchedBorder;

import dna.visualization.MainDisplay;
import dna.visualization.components.BoundsPopupMenuListener;
import dna.visualization.components.ColorHandler;
import dna.visualization.components.MetricVisualizer;

/**
 * Used within a metric visualizer component to give the user control over which
 * data is shown in the visualizer and how.
 * 
 * @author RWilmes
 * 
 */
public class Legend extends JPanel {
	private Font defaultFont = MainDisplay.defaultFont;

	private MetricVisualizer parent;
	private Legend thisLegend;
	private LegendList list;
	private JScrollPane scrollBar;

	private JPanel addButtonPanel;
	private JButton addButton;

	private JComboBox<String> addBox;
	private String[] addBoxMenu;

	private ColorHandler colorHandler;

	public Legend(MetricVisualizer parent) {
		super();
		this.parent = parent;
		thisLegend = this;
		this.colorHandler = new ColorHandler();
		this.setPreferredSize(new Dimension(190, 320));
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		// init list and add it
		this.list = new LegendList(this);

		c.gridx = 0;
		c.gridy = 0;
		this.add(list, c);
		this.scrollBar = new JScrollPane(this.list);
		this.scrollBar.setPreferredSize(new Dimension(187, 295));
		this.scrollBar
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		this.add(scrollBar);

		// init addbutton panel and add it
		this.addButtonPanel = new JPanel();
		this.addButtonPanel.setPreferredSize(new Dimension(187, 20));
		this.addButtonPanel.setBorder(BorderFactory
				.createEtchedBorder((EtchedBorder.LOWERED)));
		this.addButtonPanel.setLayout(new BoxLayout(this.addButtonPanel,
				BoxLayout.X_AXIS));

		c.gridx = 0;
		c.gridy = 1;
		this.add(addButtonPanel, c);

		this.initAddBox(null);
	}

	public void addItemToList(String name) {
		boolean alreadyAdded = false;
		for (Component c : this.list.getComponents()) {
			if (c instanceof LegendItem) {
				if (c.getName().equals(name)) {
					alreadyAdded = true;
				}
			}
		}

		if (!alreadyAdded) {
			Color color = this.colorHandler.getNextColor();

			LegendItem i = new LegendItem(this.list, name, color);
			i.setToolTipText(name);
			this.list.add(i);
			this.parent.addTrace(name, color);
		}
		this.validate();
	}

	public LegendList getLegendList() {
		return this.list;
	}

	public void addItem2(LegendItem i) {
		this.add(i);
		this.validate();
	}

	public void removeItem(String name, Color color) {
		this.parent.removeTrace(name);
		this.colorHandler.removeColor(color);
	}

	/** updated addbox with new selectable values **/
	public void updateAddBox(String[] addBoxChoices) {
		this.addBoxMenu = addBoxChoices;
		this.addButtonPanel.remove(this.addBox);
		this.addBox = new JComboBox<String>(addBoxChoices);
		this.addBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				if (thisLegend.addBox.getSelectedItem() instanceof String) {
					thisLegend.addSelection(thisLegend.addBox
							.getSelectedIndex());
				}
			}
		});
		this.addBox.setToolTipText("Add selected value to the list");
		this.addBox.setFont(this.defaultFont);
		BoundsPopupMenuListener listener = new BoundsPopupMenuListener(true,
				false);
		this.addBox.addPopupMenuListener(listener);
		this.addBox.setPrototypeDisplayValue("Test");
		this.addButtonPanel.add(addBox);
		this.validate();
	}

	/** initializes the addbox **/
	private void initAddBox(String[] addBoxChoices) {
		this.addBoxMenu = addBoxChoices;
		if (addBoxChoices != null)
			this.addBox = new JComboBox<String>(addBoxChoices);
		else
			this.addBox = new JComboBox<String>();
		this.addBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				if (thisLegend.addBox.getSelectedItem() instanceof String)
					thisLegend.addSelection(thisLegend.addBox
							.getSelectedIndex());
			}
		});
		this.addBox.setToolTipText("Add selected value to the list");
		this.addBox.setFont(this.defaultFont);
		BoundsPopupMenuListener listener = new BoundsPopupMenuListener(true,
				false);
		this.addBox.addPopupMenuListener(listener);
		this.addBox.setPrototypeDisplayValue("Test");
		this.addButtonPanel.add(addBox);
		this.validate();
	}

	/**
	 * Called when an item from the checkbox is selected. Calculates which
	 * elements will be added to the legend list.
	 * 
	 * @param selectionIndex
	 *            index of the selected element
	 */
	public void addSelection(int selectionIndex) {
		// if selected element is a category
		if (this.addBoxMenu[selectionIndex].charAt(0) != '-') {
			// if selected element is "metrics" category
			// means it probably has different metrics with different values
			// each
			if (this.addBoxMenu[selectionIndex].equals("metrics")) {
				int x = selectionIndex + 1;
				while (x != this.addBoxMenu.length
						&& this.addBoxMenu[x].charAt(0) == '-') {
					if (this.addBoxMenu[x].substring(0, 6).equals("----- ")) {
						this.addItemToList(this.addBoxMenu[x].substring(6));
					}
					x++;
				}
			} else {
				// if selected element is a category other than "metrics"
				int x = selectionIndex + 1;
				while (x != this.addBoxMenu.length
						&& this.addBoxMenu[x].charAt(0) == '-') {
					this.addItemToList(this.addBoxMenu[selectionIndex] + "."
							+ this.addBoxMenu[x].substring(3));
					x++;
				}
			}
		}

		// if selected element starts with "---"
		if (this.addBoxMenu[selectionIndex].substring(0, 3).equals("---")) {
			// if selected element starts with "--- "
			// means element is metric with different values
			if (this.addBoxMenu[selectionIndex].substring(0, 4).equals("--- ")) {
				int x = selectionIndex + 1;
				while (x != this.addBoxMenu.length
						&& this.addBoxMenu[x].substring(0, 6).equals("----- ")) {
					this.addItemToList(this.addBoxMenu[x].substring(6));
					x++;
				}
			} else {
				// if selected element is single element starting with "----- "
				// means it is a metric value without sub-elements
				if (this.addBoxMenu[selectionIndex].substring(0, 6).equals(
						"----- ")) {
					this.addItemToList(this.addBoxMenu[selectionIndex]
							.substring(6));
				} else {
					// if selected element starts with "---"
					// means it is a value of runtimes or batch statistics
					int x = selectionIndex - 1;
					while (x > 0
							&& (this.addBoxMenu[x].substring(0, 3)
									.equals("---"))) {
						x--;
					}
					this.addItemToList(this.addBoxMenu[x] + "."
							+ this.addBoxMenu[selectionIndex].substring(3));
				}
			}
		}
	}

	/** updates the value of an item **/
	public void updateItem(String name, double value) {
		this.list.updateItem(name, value);
	}

	/** resets the legend list **/
	public void reset() {
		this.list.reset();
	}

	/** toggles y axis of an trace **/
	public void toggleYAxis(LegendItem item) {
		this.parent.toggleYAxis(item.getName());
	}

}
