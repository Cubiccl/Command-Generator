package fr.cubiccl.generator.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JDialog;
import javax.swing.JPanel;

import fr.cubiccl.generator.CommandGenerator;
import fr.cubiccl.generator.gui.component.button.CButton;
import fr.cubiccl.generator.gui.component.label.CTextArea;

public class DisplayUtils
{

	public static final Color BACKGROUND = new Color(220, 220, 250), HOVERED = new Color(220, 250, 220), CLICKED = new Color(250, 220, 220),
			DISABLED = new Color(200, 200, 200);

	public static final Font FONT = new Font("Dialog", Font.BOLD, 14);

	public static void showMessage(String title, String message)
	{
		JDialog dialog = new JDialog(CommandGenerator.window, title, true);
		int width = CommandGenerator.window.getWidth() / 3, height = CommandGenerator.window.getHeight() / 3;

		JPanel panel = new JPanel(new GridBagLayout());
		CTextArea pane = new CTextArea(message);
		CButton button = new CButton("general.confirm");
		button.updateTranslations();
		button.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dialog.dispose();
			}
		});

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		panel.add(pane, gbc);
		gbc.gridy++;
		panel.add(Box.createRigidArea(new Dimension(100, 20)), gbc);
		gbc.gridy++;
		panel.add(button, gbc);

		dialog.getContentPane().add(panel);
		dialog.setSize(width, height);
		dialog.setLocationRelativeTo(CommandGenerator.window);
		dialog.setVisible(true);
	}

	public static int textWidth(String text)
	{
		return FONT.getSize() * 4 / 5 * text.length();
	}

}
