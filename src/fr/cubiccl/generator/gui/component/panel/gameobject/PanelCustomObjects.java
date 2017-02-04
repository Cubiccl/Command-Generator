package fr.cubiccl.generator.gui.component.panel.gameobject;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;

import fr.cubiccl.generator.CommandGenerator;
import fr.cubiccl.generator.gameobject.registries.ObjectSaver;
import fr.cubiccl.generator.gui.component.combobox.OptionCombobox;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.utils.ConfirmPanel;
import fr.cubiccl.generator.gui.component.panel.utils.PanelObjectList;
import fr.cubiccl.generator.utils.Text;

public class PanelCustomObjects extends ConfirmPanel
{
	private static final long serialVersionUID = 5654464660817660318L;

	private OptionCombobox comboboxType;
	private PanelObjectList listObjects;

	public PanelCustomObjects()
	{
		super(null, null, false);
		this.buttonCancel.setText(new Text("general.back"));
		this.setName(new Text("menu.objects"));

		String[] types = new String[ObjectSaver.savers.length];
		for (int i = 0; i < types.length; ++i)
			types[i] = ObjectSaver.savers[i].name.id.substring("objects.".length());

		CGPanel panel = new CGPanel();

		GridBagConstraints gbc = panel.createGridBagLayout();
		gbc.gridwidth = 3;
		panel.add(this.comboboxType = new OptionCombobox("objects", types), gbc);
		++gbc.gridy;
		panel.add(this.listObjects = new PanelObjectList(ObjectSaver.attributeModifiers), gbc);

		this.comboboxType.addActionListener(this);

		this.setMainComponent(panel);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		super.actionPerformed(e);
		if (e.getSource() == this.buttonCancel) CommandGenerator.window.menubar.toggleObjects(true);
		if (e.getSource() == this.comboboxType) this.onTypeSelection();
	}

	private void onTypeSelection()
	{
		this.listObjects.setList(ObjectSaver.savers[this.comboboxType.getSelectedIndex()]);
	}

}
