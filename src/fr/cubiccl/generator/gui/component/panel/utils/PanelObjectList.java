package fr.cubiccl.generator.gui.component.panel.utils;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Array;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import fr.cubiccl.generator.CommandGenerator;
import fr.cubiccl.generator.gui.component.CGList;
import fr.cubiccl.generator.gui.component.button.CGButton;
import fr.cubiccl.generator.gui.component.interfaces.IObjectList;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.IStateListener;
import fr.cubiccl.generator.utils.Replacement;
import fr.cubiccl.generator.utils.Text;

public class PanelObjectList<T extends IObjectList<T>> extends CGPanel implements ActionListener, ListSelectionListener, IStateListener<CGPanel>
{
	private static final long serialVersionUID = 2923920419688577940L;

	protected CGButton buttonAdd, buttonEdit, buttonRemove;
	private Class<T> c;
	private Component componentDisplay;
	protected int editing = -1;
	protected CGList list;
	private ArrayList<ListListener<T>> listeners;
	private T newObject;
	private ArrayList<T> objects;
	private Text popupTitle;
	public final ListProperties properties;

	public PanelObjectList(String titleID, String popupTitleID, Class<T> c, Object... properties)
	{
		this(titleID, popupTitleID == null ? null : new Text(popupTitleID), c, properties);
	}

	public PanelObjectList(String titleID, Text popupTitle, Class<T> c, Object... properties)
	{
		super(titleID);
		this.objects = new ArrayList<T>();
		this.listeners = new ArrayList<ListListener<T>>();
		this.c = c;
		this.popupTitle = popupTitle;
		this.properties = new ListProperties(properties);

		GridBagConstraints gbc = this.createGridBagLayout();
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.gridheight = 3;
		this.add((this.list = new CGList()).scrollPane, gbc);

		++gbc.gridx;
		gbc.gridheight = 1;
		this.add(this.buttonAdd = new CGButton("general.add"), gbc);
		++gbc.gridy;
		this.add(this.buttonEdit = new CGButton("general.edit"), gbc);
		++gbc.gridy;
		this.add(this.buttonRemove = new CGButton("general.remove"), gbc);

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridheight = 3;

		this.buttonEdit.setEnabled(false);
		this.buttonRemove.setEnabled(false);
		this.buttonAdd.addActionListener(this);
		this.buttonEdit.addActionListener(this);
		this.buttonRemove.addActionListener(this);
		this.list.addListSelectionListener(this);
		this.list.scrollPane.setPreferredSize(new Dimension(200, 100));

		this.updateList();
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == this.buttonAdd) try
		{
			this.editing = this.objects.size();
			this.newObject = this.c.newInstance();
			this.properties.set("new", true);
			this.properties.set("index", this.editing);
			CGPanel p = this.newObject.createPanel(this.properties);
			if (p == null) return;
			p.setName(this.setupPopupTitle());
			CommandGenerator.stateManager.setState(p, this);
		} catch (InstantiationException | IllegalAccessException e1)
		{
			e1.printStackTrace();
		}
		else if (e.getSource() == this.buttonEdit)
		{
			this.editing = this.selectedIndex();
			this.properties.set("new", false);
			this.properties.set("index", this.editing);
			CGPanel p = this.objects.get(this.editing).createPanel(this.properties);
			if (p == null) return;
			p.setName(this.setupPopupTitle());
			CommandGenerator.stateManager.setState(p, this);
		} else if (e.getSource() == this.buttonRemove) this.removeSelected();
		this.updateList();
	}

	public void add(T object)
	{
		this.objects.add(object);
		for (ListListener<T> listener : this.listeners)
			listener.onAddition(this.objects.size() - 1, object);
		this.updateList();
	}

	public void addListListener(ListListener<T> listener)
	{
		this.listeners.add(listener);
	}

	public void clear()
	{
		this.objects.clear();
		this.updateList();
	}

	public void delete(int index)
	{
		T object = this.get(index);
		this.objects.remove(index);
		for (ListListener<T> listener : this.listeners)
			listener.onDeletion(index, object);
	}

	public T get(int index)
	{
		return this.objects.get(index);
	}

	public String getSelectedValue()
	{
		return this.list.getSelectedValue();
	}

	public int length()
	{
		return this.objects.size();
	}

	protected void removeSelected()
	{
		int index = this.selectedIndex();
		if (index != -1)
		{
			if (JOptionPane.showConfirmDialog(CommandGenerator.window,
					new Text("general.delete.confirm", new Replacement("<object>", this.getSelectedValue())), null, JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
			this.delete(index);
			this.updateList();
		}
	}

	public int selectedIndex()
	{
		return this.list.getSelectedIndex();
	}

	public void set(int index, T object)
	{
		this.objects.set(index, object);
		for (ListListener<T> listener : this.listeners)
			listener.onChange(index, object);
		this.updateList();
	}

	@Override
	public void setEnabled(boolean enabled)
	{
		super.setEnabled(enabled);
		this.buttonAdd.setEnabled(enabled);
		if (enabled) this.updateDisplay();
		else
		{
			this.buttonEdit.setEnabled(false);
			this.buttonRemove.setEnabled(false);
		}
	}

	private Text setupPopupTitle()
	{
		if (this.popupTitle == null) return null;
		if (this.popupTitle.hasReplacement("<index>")) this.popupTitle.removeReplacements("<index>");
		this.popupTitle.addReplacement(new Replacement("<index>", Integer.toString(this.editing + 1)));
		return this.popupTitle;
	}

	public void setValues(T[] values)
	{
		this.clear();
		for (T object : values)
			this.add(object);
	}

	@Override
	public boolean shouldStateClose(CGPanel panel)
	{
		try
		{
			if (this.editing == this.objects.size())
			{
				T o = this.newObject.setupFrom(panel);
				this.objects.add(o);
			} else this.objects.set(this.editing, this.objects.get(this.editing).setupFrom(panel));
			this.updateList();
			return true;
		} catch (CommandGenerationException e)
		{
			CommandGenerator.report(e);
			return false;
		}
	}

	private void updateDisplay()
	{
		int index = this.selectedIndex();
		this.buttonEdit.setEnabled(index != -1);
		this.buttonRemove.setEnabled(index != -1);
		if (this.componentDisplay != null) this.remove(this.componentDisplay);
		if (index == -1) this.componentDisplay = null;
		else
		{
			this.componentDisplay = this.objects.get(index).getDisplayComponent();
			if (this.componentDisplay != null) this.add(this.componentDisplay, this.gbc);
		}
		this.revalidate();
		this.repaint();
	}

	private void updateList()
	{
		int index = this.selectedIndex();

		String[] names = new String[this.objects.size()];
		for (int i = 0; i < names.length; ++i)
			names[i] = this.objects.get(i).getName(i);

		this.list.setValues(names);
		this.list.setSelectedIndex(index);

		this.updateDisplay();
	}

	@Override
	public void valueChanged(ListSelectionEvent e)
	{
		this.updateDisplay();
	}

	@SuppressWarnings("unchecked")
	public T[] values()
	{
		return this.objects.toArray((T[]) Array.newInstance(this.c, this.objects.size()));
	}

}
