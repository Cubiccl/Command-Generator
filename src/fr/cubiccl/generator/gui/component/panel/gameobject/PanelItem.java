package fr.cubiccl.generator.gui.component.panel.gameobject;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import fr.cubiccl.generator.gameobject.ItemStack;
import fr.cubiccl.generator.gameobject.ObjectRegistry;
import fr.cubiccl.generator.gameobject.baseobjects.Item;
import fr.cubiccl.generator.gui.component.combobox.ObjectCombobox;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.label.ImageLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.textfield.CGEntry;
import fr.cubiccl.generator.gui.component.textfield.CGSpinner;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;
import fr.cubiccl.generator.utils.WrongValueException;

public class PanelItem extends CGPanel implements ActionListener, ListSelectionListener
{
	private static final long serialVersionUID = -8600189753659710473L;

	private ObjectCombobox<Item> comboboxItem;
	private CGEntry entryAmount;
	private CGLabel labelName;
	private ImageLabel labelTexture;
	private CGSpinner spinnerData;

	public PanelItem(String titleID)
	{
		this(titleID, ObjectRegistry.getItems());
	}

	public PanelItem(String titleID, Item[] items)
	{
		super(titleID);

		GridBagConstraints gbc = this.createGridBagLayout();
		++gbc.gridheight;
		this.add(new CGLabel("item.id").setHasColumn(true), gbc);
		++gbc.gridx;
		this.add((this.comboboxItem = new ObjectCombobox<Item>(items)).container, gbc);
		++gbc.gridx;
		--gbc.gridheight;
		this.add(this.labelTexture = new ImageLabel(), gbc);
		++gbc.gridy;
		this.add(this.labelName = new CGLabel(""), gbc);
		--gbc.gridx;
		++gbc.gridy;
		gbc.gridwidth = 3;
		this.add((this.spinnerData = new CGSpinner(new Text("block.data"), items[0].damage)).container, gbc);
		++gbc.gridy;
		this.add((this.entryAmount = new CGEntry(new Text("item.amount"), "1")).container, gbc);

		this.comboboxItem.addActionListener(this);
		this.spinnerData.addActionListener(this);
		this.entryAmount.addIntFilter();
		this.updateDisplay();
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == this.comboboxItem) this.spinnerData.setValues(this.selectedItem().damage);
		this.updateDisplay();
	}

	public ItemStack generateItem() throws CommandGenerationException
	{
		try
		{
			int amount = Integer.parseInt(this.entryAmount.getText());
			if (amount < 0) throw new WrongValueException(this.entryAmount.label.getAbsoluteText(), new Text("error.integer.positive"),
					this.entryAmount.getText());
			return new ItemStack(this.selectedItem(), this.spinnerData.getValue(), amount);
		} catch (NumberFormatException e)
		{
			throw new WrongValueException(this.entryAmount.label.getAbsoluteText(), new Text("error.integer.positive"), this.entryAmount.getText());
		}
	}

	public int selectedDamage()
	{
		return this.spinnerData.getValue();
	}

	public Item selectedItem()
	{
		return this.comboboxItem.getSelectedObject();
	}

	public void setEnabledContent(boolean data, boolean amount)
	{
		this.spinnerData.container.setVisible(data);
		if (!data) this.spinnerData.setText(Integer.toString(this.selectedItem().damage[0]));
		this.entryAmount.container.setVisible(amount);
		this.updateDisplay();
	}

	private void updateDisplay()
	{
		if (this.spinnerData.container.isVisible()) this.labelName.setText(this.selectedItem().name(this.spinnerData.getValue()).toString());
		else this.labelName.setText(this.selectedItem().mainName().toString());
		this.labelTexture.setImage(this.selectedItem().texture(this.spinnerData.getValue()));
	}

	@Override
	public void valueChanged(ListSelectionEvent e)
	{
		this.updateDisplay();
	}

}
