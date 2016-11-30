package fr.cubiccl.generator.gui.component.panel.gameobject;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import fr.cubiccl.generator.gameobject.ObjectRegistry;
import fr.cubiccl.generator.gameobject.PlacedBlock;
import fr.cubiccl.generator.gameobject.baseobjects.Block;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;
import fr.cubiccl.generator.gui.component.combobox.ObjectCombobox;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.label.ImageLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.textfield.CGSpinner;

public class PanelBlock extends CGPanel implements ActionListener, ListSelectionListener
{
	private static final long serialVersionUID = -8600189753659710473L;

	private ObjectCombobox<Block> comboboxBlock;
	private CGLabel labelName;
	private ImageLabel labelTexture;
	private PanelTags panelTags;
	private CGSpinner spinnerData;

	public PanelBlock(String titleID)
	{
		this(titleID, true);
	}

	public PanelBlock(String titleID, boolean hasData)
	{
		super(titleID);
		Block[] blocks = ObjectRegistry.getBlocks();

		GridBagConstraints gbc = this.createGridBagLayout();
		++gbc.gridheight;
		this.add(new CGLabel("block.id").setHasColumn(true), gbc);
		++gbc.gridx;
		this.add((this.comboboxBlock = new ObjectCombobox<Block>(blocks)).container, gbc);
		++gbc.gridx;
		--gbc.gridheight;
		this.add(this.labelTexture = new ImageLabel(), gbc);
		++gbc.gridy;
		this.add(this.labelName = new CGLabel(""), gbc);
		--gbc.gridx;
		++gbc.gridy;
		gbc.gridwidth = 3;
		this.add((this.spinnerData = new CGSpinner("block.data", blocks[0].damage)).container, gbc);
		++gbc.gridy;
		this.add(this.panelTags = new PanelTags("block.tags", Tag.BLOCK), gbc);

		this.spinnerData.container.setVisible(hasData);
		this.comboboxBlock.addActionListener(this);
		this.spinnerData.addActionListener(this);
		this.updateDisplay();
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == this.comboboxBlock)
		{
			this.spinnerData.setValues(this.selectedBlock().damage);
			this.panelTags.setObjectForTags(this.selectedBlock().idString);
		}
		this.updateDisplay();
	}

	public PlacedBlock generateBlock()
	{
		return this.generateBlock(Tags.BLOCK);
	}

	public PlacedBlock generateBlock(TemplateCompound container)
	{
		return new PlacedBlock(this.selectedBlock(), this.spinnerData.getValue(), this.getNBT(container));
	}

	public TagCompound getNBT(TemplateCompound container)
	{
		return this.panelTags.generateTags(container);
	}

	public Block selectedBlock()
	{
		return this.comboboxBlock.getSelectedObject();
	}

	public int selectedDamage()
	{
		return this.spinnerData.getValue();
	}

	public void setEnabledContent(boolean data)
	{
		this.spinnerData.container.setVisible(data);
		this.updateDisplay();
	}

	private void updateDisplay()
	{
		if (this.spinnerData.container.isVisible()) this.labelName.setText(this.selectedBlock().name(this.spinnerData.getValue()).toString());
		else this.labelName.setText(this.selectedBlock().mainName().toString());
		this.labelTexture.setImage(this.selectedBlock().texture(this.spinnerData.getValue()));
	}

	@Override
	public void valueChanged(ListSelectionEvent e)
	{
		this.updateDisplay();
	}

}
