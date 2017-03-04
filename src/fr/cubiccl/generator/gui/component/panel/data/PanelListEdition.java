package fr.cubiccl.generator.gui.component.panel.data;

import java.awt.GridBagConstraints;

import javax.swing.JLabel;

import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.utils.ListListener;
import fr.cubiccl.generator.gui.component.panel.utils.PanelObjectList;
import fr.cubiccl.generator.utils.Text;

public class PanelListEdition extends CGPanel implements ListListener<Text>
{
	private static final long serialVersionUID = -880477144967274000L;

	private final String id;
	private PanelObjectList<Text> panelValues;

	public PanelListEdition(String id)
	{
		this.id = id;
		GridBagConstraints gbc = this.createGridBagLayout();
		this.add(new JLabel("ID: " + id), gbc);
		++gbc.gridy;
		this.add(this.panelValues = new PanelObjectList<Text>(null, (Text) null, Text.class));

		String[] v = ObjectRegistry.getList(id);
		Text[] values = new Text[v.length];
		for (int i = 0; i < values.length; ++i)
			values[i] = new Text(v[i], false);
		
		this.panelValues.setValues(values);
		this.panelValues.buttonEdit.setVisible(false);
		this.panelValues.addListListener(this);
	}

	@Override
	public void onAddition(int index, Text object)
	{
		ObjectRegistry.addToLists(object.id, id);
	}

	@Override
	public void onChange(int index, Text object)
	{}

	@Override
	public void onDeletion(int index, Text object)
	{
		ObjectRegistry.removeFromList(object.id, id);
	}
}
