package fr.cubiccl.generator.gui.component.label;

import javax.swing.JTextArea;

public class CTextArea extends JTextArea
{
	private static final long serialVersionUID = -2748415360656002559L;
	
	public CTextArea(String text)
	{
		super(text);
		this.setEditable(false);
		this.setBackground(null);
	}

}
