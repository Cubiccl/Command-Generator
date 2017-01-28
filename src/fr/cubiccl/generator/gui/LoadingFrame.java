package fr.cubiccl.generator.gui;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JProgressBar;

import fr.cubiccl.generator.utils.Text;

public class LoadingFrame extends JFrame
{
	private static final long serialVersionUID = -6063783972007851906L;

	private final JProgressBar progressbar;

	public LoadingFrame(int progressTotal, boolean isVisible)
	{
		super(new Text("loading.generator").toString());
		this.setIconImage(new ImageIcon("resources/textures/gui/icon.png").getImage());
		this.setVisible(isVisible);
		this.setSize(400, 200);
		this.setResizable(false);
		this.setLocationRelativeTo(null);

		this.add(this.progressbar = new JProgressBar(0, progressTotal));
		this.progressbar.setString(this.getTitle());
		this.progressbar.setStringPainted(true);
	}

	public void setText(String textID)
	{
		this.setText(new Text(textID));
	}

	public void setText(Text text)
	{
		this.progressbar.setValue(this.progressbar.getValue() + 1);
		this.progressbar.setString(text.toString());
	}

}
