package fr.cubiccl.generator.gui.component.panel.utils;

import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.SwingConstants;

import fr.cubiccl.generator.CommandGenerator;
import fr.cubiccl.generator.gui.component.button.CGButton;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.utils.Text;

public class ConfirmPanel extends CGPanel implements ActionListener
{
	private static final long serialVersionUID = -7457797405576343429L;

	protected CGButton buttonOK, buttonCancel;
	public Component component;
	private GridBagConstraints gbc;

	public ConfirmPanel()
	{
		this((Text) null, null, true);
	}

	public ConfirmPanel(String titleID, Component component)
	{
		this(titleID == null ? null : new Text(titleID), component, true);
	}

	public ConfirmPanel(Text title, Component component, boolean hasOKButton)
	{
		this.gbc = this.createGridBagLayout();
		if (title != null)
		{
			CGLabel label = new CGLabel(title);
			label.setFont(new Font(label.getFont().getFontName(), Font.BOLD, 28));
			label.setHorizontalAlignment(SwingConstants.CENTER);
			this.add(label, gbc);
			++gbc.gridy;
		}
		++gbc.gridy;
		gbc.fill = GridBagConstraints.HORIZONTAL;

		if (hasOKButton)
		{
			JPanel p = new JPanel(new GridLayout(1, 2));
			((GridLayout) p.getLayout()).setVgap(10);
			p.add(this.buttonCancel = new CGButton("general.cancel"));
			p.add(this.buttonOK = new CGButton("general.confirm"));
			this.add(p, gbc);
		} else this.add(this.buttonCancel = new CGButton("general.cancel"), gbc);

		--gbc.gridy;
		gbc.fill = GridBagConstraints.NONE;
		if (component != null) this.setMainComponent(component);

		if (hasOKButton) this.buttonOK.addActionListener(this);
		this.buttonCancel.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == this.buttonOK || e.getSource() == this.buttonCancel) CommandGenerator.stateManager.clearState(e.getSource() == this.buttonOK);
	}

	public void setMainComponent(Component component)
	{
		if (this.component != null) this.remove(this.component);
		this.component = component;
		this.add(this.component, this.gbc);
		this.updateTranslations();
	}

}
