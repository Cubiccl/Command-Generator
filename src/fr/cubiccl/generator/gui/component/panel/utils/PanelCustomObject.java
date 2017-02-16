package fr.cubiccl.generator.gui.component.panel.utils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import fr.cubiccl.generator.CommandGenerator;
import fr.cubiccl.generator.gameobject.GameObject;
import fr.cubiccl.generator.gameobject.registries.ObjectSaver;
import fr.cubiccl.generator.gui.component.button.CGButton;
import fr.cubiccl.generator.gui.component.interfaces.ICustomObject;
import fr.cubiccl.generator.gui.component.interfaces.IObjectList;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.IStateListener;
import fr.cubiccl.generator.utils.Replacement;
import fr.cubiccl.generator.utils.Text;

public class PanelCustomObject<T extends GameObject, Y extends IObjectList<Y>> extends CGPanel implements ActionListener, IStateListener<PanelObjectList<Y>>
{
	private static final long serialVersionUID = -2533903048070707867L;

	private CGButton buttonSave, buttonLoad;
	private ICustomObject<T> parent;
	private ObjectSaver<T> saver;

	public PanelCustomObject(ICustomObject<T> parent, ObjectSaver<T> saver)
	{
		this.parent = parent;
		this.saver = saver;

		this.add(this.buttonSave = new CGButton("objects.save"));
		this.add(this.buttonLoad = new CGButton("objects.load"));

		this.buttonSave.setIcon(new ImageIcon("resources/textures/gui/save.png"));
		this.buttonLoad.setIcon(new ImageIcon("resources/textures/gui/load.png"));
		this.buttonSave.addActionListener(this);
		this.buttonLoad.addActionListener(this);
		this.updateTranslations();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == this.buttonSave) try
		{
			T object = this.parent.generate();
			object.setCustomName(JOptionPane.showInputDialog(new Text("objects.name")));
			if (object.customName() == null) return;
			this.saver.addObject(object);
		} catch (CommandGenerationException e1)
		{
			CommandGenerator.report(e1);
			e1.printStackTrace();
			return;
		}
		if (e.getSource() == this.buttonLoad)
		{
			PanelObjectList<Y> p = new PanelObjectList<Y>(null, (Text) null, (Class<Y>) this.saver.c, "customObjects", false);
			T[] list = this.saver.list();
			for (T t : list)
				p.add((Y) t);
			p.setName(new Text("objects.title", new Replacement("<object>", this.saver.name)));
			CommandGenerator.stateManager.setState(p, this);
		}
	}

	@Override
	public boolean shouldStateClose(PanelObjectList<Y> panel)
	{
		T object = this.saver.find(panel.list.getValue());
		if (object == null) return false;
		this.parent.setupFrom(object);
		return true;
	}

}
