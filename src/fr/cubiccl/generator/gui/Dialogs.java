package fr.cubiccl.generator.gui;

import java.awt.Component;
import java.awt.Desktop;
import java.awt.Font;

import javax.swing.JEditorPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import fr.cubi.cubigui.DisplayUtils;
import fr.cubiccl.generator.CommandGenerator;
import fr.cubiccl.generator.gui.component.textfield.CGEntry;
import fr.cubiccl.generator.utils.Lang;
import fr.cubiccl.generator.utils.Text;

public final class Dialogs
{

	public static boolean showConfirmDialog(Component component)
	{
		return showConfirmDialog(component, Lang.translate("general.confirm"), Lang.translate("general.cancel"));
	}

	public static boolean showConfirmDialog(Component component, String okText, String cancelText)
	{
		return DisplayUtils.showPopup(CommandGenerator.window, "", component, okText, cancelText);
	}

	public static boolean showConfirmMessage(String message)
	{
		return showConfirmMessage(message, Lang.translate("general.confirm"), Lang.translate("general.cancel"));
	}

	public static boolean showConfirmMessage(String message, String okText, String cancelText)
	{
		JEditorPane jep = new JEditorPane();
		jep.setContentType("text/html");
		jep.setText(message);

		jep.setEditable(false);
		jep.setOpaque(false);
		jep.setFont(DisplayUtils.FONT.deriveFont(Font.PLAIN, 12));

		jep.addHyperlinkListener(new HyperlinkListener()
		{
			@Override
			public void hyperlinkUpdate(HyperlinkEvent hle)
			{
				if (HyperlinkEvent.EventType.ACTIVATED.equals(hle.getEventType()))
				{
					System.out.println(hle.getURL());
					Desktop desktop = Desktop.getDesktop();
					try
					{
						desktop.browse(hle.getURL().toURI());
					} catch (Exception ex)
					{
						ex.printStackTrace();
					}
				}
			}
		});
		return DisplayUtils.showPopup(CommandGenerator.window, "", jep, okText, cancelText);
	}

	public static String showInputDialog(String message)
	{
		return showInputDialog(message, "");
	}

	public static String showInputDialog(String message, String defaultValue)
	{
		return showInputDialog(message, Lang.translate("general.confirm"), Lang.translate("general.cancel"), defaultValue);
	}

	public static String showInputDialog(String message, String okText, String cancelText)
	{
		return showInputDialog(message, okText, cancelText, "");
	}

	public static String showInputDialog(String message, String okText, String cancelText, String defaultValue)
	{
		CGEntry entry = new CGEntry(new Text(message, false), null);
		entry.setText(defaultValue);
		if (!showConfirmDialog(entry.container, okText, cancelText)) return null;
		return entry.getText();
	}

	public static void showMessage(String message)
	{
		showConfirmMessage(message, Lang.translate("general.confirm"), null);
	}

	private Dialogs()
	{}

}
