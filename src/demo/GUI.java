package demo;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import demoPanels.DefaultPanel;
import demoPanels.DispersePanel;
import demoPanels.DotSimulator;
import demoPanels.GameDemo;
import demoPanels.MouseKeyInputDisplay;
import util.AnimationPanel;
import util.GamePanel;

public class GUI {

	private JLabel message;
	private JPanel contentPanel;

	public GUI() {
		JFrame frame = new JFrame();
		frame.setTitle("Simple Game Mechanics Demo");
		// 16/9 aspect ratio within 1 pixel
		frame.setSize(new Dimension(1067, 600));
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		centerFrame(frame);
		frame.getContentPane().setLayout(new BorderLayout());

		// The selection panel will be on the top of the window, and contain the
		// selection buttons.
		JPanel selection = new JPanel() {
			@Override
			public Dimension getPreferredSize() {
				// bump out the height a little bit.
				return new Dimension(super.getPreferredSize().width, 30);
			}
		};

		selection.add(new SwitcherButton("Mouse & Key Input", new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				replaceContent("Try the mouse buttons, scroll wheel, and keyboard.",
						new MouseKeyInputDisplay().start());
			}
		}));

		selection.add(new SwitcherButton("Dispersion Particle", new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				replaceContent("Click, drag, and release!", new DispersePanel().start());
			}
		}));

		selection.add(new SwitcherButton("Dot Sim", new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				replaceContent("Sit back, and watch!", new DotSimulator().start());
			}
		}));

		selection.add(new SwitcherButton("Game Demo", new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				replaceContent("Mouse to move; WASD or arrow keys to shoot.", new GameDemo().start());
			}
		}));

		// With GridLayout, buttons will be added on same row and expand width.
		selection.setLayout(new GridLayout());

		frame.getContentPane().add(selection, BorderLayout.NORTH);

		// The message label will be at the bottom of the window.
		message = new JLabel();
		message.setHorizontalAlignment(SwingConstants.CENTER);
		message.setFont(new Font("sans-serif", Font.PLAIN, 20));
		frame.getContentPane().add(message, BorderLayout.SOUTH);

		// Components will be switched out of the centered content panel to display new
		// content.
		contentPanel = new JPanel();
		contentPanel.setLayout(new BorderLayout());

		frame.getContentPane().add(contentPanel);

		// Start by showing the default panel.
		replaceContent("", new DefaultPanel());

		frame.setVisible(true);
	}

	/**
	 * @param bottomMessage
	 *            Sets the description message on the bottom panel, <br>
	 *            briefly describing how to use each demo.
	 * @param panel
	 *            The panel, AnimationPanel, or GamePanel to add.<br>
	 *            The function will handle starting the timer loop and giving it
	 *            focus.
	 */
	public void replaceContent(String bottomMessage, JPanel panel) {
		message.setText(bottomMessage);
		for (Component c : contentPanel.getComponents()) {
			contentPanel.remove(c);
			if (c instanceof AnimationPanel) {
				((AnimationPanel) c).pause();
				// Being extra sure to remove all reference to object.
				// I don't want a stray animation playing in the background.
				c = null;
			}
		}
		contentPanel.add(panel);
		if (panel instanceof AnimationPanel) {
			((AnimationPanel) panel).start();
			if (panel instanceof GamePanel) {
				panel.requestFocusInWindow();
			}
		}
		contentPanel.revalidate();
	}

	/**
	 * @param frame
	 *            The frame to be centered on the screen. Uses the computer monitor
	 *            resolution.
	 */
	private void centerFrame(JFrame frame) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation((int) (screenSize.getWidth() / 2 - frame.getWidth() / 2),
				(int) (screenSize.getHeight() / 2 - frame.getHeight() / 2));
	}
}
