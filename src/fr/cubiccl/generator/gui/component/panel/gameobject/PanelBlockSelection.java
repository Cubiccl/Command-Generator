package fr.cubiccl.generator.gui.component.panel.gameobject;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;

import fr.cubi.cubigui.CPanel;
import fr.cubiccl.generator.gameobject.ObjectRegistry;
import fr.cubiccl.generator.gameobject.baseobjects.Block;
import fr.cubiccl.generator.gui.component.CScrollPane;
import fr.cubiccl.generator.gui.component.combobox.ObjectCombobox;
import fr.cubiccl.generator.gui.component.interfaces.IImageSelectionListener;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.label.ImageLabel;
import fr.cubiccl.generator.gui.component.panel.utils.ConfirmPanel;
import fr.cubiccl.generator.gui.component.textfield.CGSpinner;

public class PanelBlockSelection extends ConfirmPanel implements ComponentListener
{
	public static final int BLOCK_SIZE = 64, GAP = 5;
	private static final long serialVersionUID = -3259302480348824205L;

	private Block[] blocks;
	private ObjectCombobox<Block> comboboxBlock;
	private PanelImageList damageSelector, blockSelector;
	private boolean hasData;
	private ImageLabel labelImage;
	private CGLabel labelName;
	private CScrollPane scrollpane;
	private int selected, damage;
	private CGSpinner spinnerDamage;

	public PanelBlockSelection()
	{
		this(true);
	}

	public PanelBlockSelection(boolean hasData)
	{
		super("block.select", null);
		this.hasData = hasData;
		this.selected = 0;
		this.damage = 0;
		this.blocks = ObjectRegistry.getBlocks(ObjectRegistry.SORT_NUMERICALLY);

		CPanel p = new CPanel();
		GridBagConstraints gbc = p.createGridBagLayout();
		gbc.anchor = GridBagConstraints.CENTER;
		p.add(new CGLabel("block.id").setHasColumn(true), gbc);
		++gbc.gridx;
		p.add((this.comboboxBlock = new ObjectCombobox<Block>(this.blocks)).container, gbc);
		++gbc.gridx;
		p.add((this.spinnerDamage = new CGSpinner("block.data", this.blocks[0].damage)).container, gbc);
		++gbc.gridx;
		p.add(this.labelImage = new ImageLabel(), gbc);
		++gbc.gridx;
		p.add(this.labelName = new CGLabel(""), gbc);
		gbc.gridx = 0;
		++gbc.gridy;
		gbc.gridwidth = 5;
		p.add(this.scrollpane = new CScrollPane(this.blockSelector = new PanelImageList(new IImageSelectionListener()
		{

			@Override
			public int currentSelection()
			{
				return selected;
			}

			@Override
			public void selectObject(int objectIndex)
			{
				setSelected(objectIndex, true);
			}
		})), gbc);
		++gbc.gridy;
		if (this.hasData)
		{
			p.add(new CGLabel("block.data").setHasColumn(true), gbc);
			++gbc.gridy;
		}
		p.add(this.damageSelector = new PanelImageList(new IImageSelectionListener()
		{

			@Override
			public int currentSelection()
			{
				return damage;
			}

			@Override
			public void selectObject(int objectIndex)
			{
				setDamage(objectIndex);
			}
		}), gbc);

		BufferedImage[] images = new BufferedImage[this.blocks.length];
		for (int i = 0; i < images.length; ++i)
			images[i] = this.blocks[i].texture(0);
		this.blockSelector.setImages(images);

		if (!this.hasData)
		{
			this.damageSelector.setVisible(false);
			this.spinnerDamage.container.setVisible(false);
		}
		this.comboboxBlock.addActionListener(this);
		this.spinnerDamage.addActionListener(this);
		this.setMainComponent(p);
		this.addComponentListener(this);
		this.setSelected(0, true);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		super.actionPerformed(e);
		if (e.getSource() == this.comboboxBlock)
		{
			int index = 0;
			String value = this.comboboxBlock.getValue();
			for (int i = 0; i < this.blocks.length; ++i)
				if (this.blocks[i].idString.equals(value))
				{
					index = i;
					break;
				}
			this.setSelected(index, false);
		}
		if (e.getSource() == this.spinnerDamage) this.setDamage(this.spinnerDamage.getValue());
	}

	@Override
	public void componentHidden(ComponentEvent e)
	{}

	@Override
	public void componentMoved(ComponentEvent e)
	{}

	@Override
	public void componentResized(ComponentEvent e)
	{
		int width = this.getWidth() - 80;
		int height = this.getHeight() - 300;
		width -= width % (BLOCK_SIZE + GAP);
		height -= height % (BLOCK_SIZE + GAP);
		this.scrollpane.setPreferredSize(new Dimension(width + 15, height));
		this.blockSelector.setObjectsPerLine(width / (BLOCK_SIZE + GAP));
	}

	@Override
	public void componentShown(ComponentEvent e)
	{}

	public Block selectedBlock()
	{
		return this.blocks[this.selected];
	}

	public int selectedDamage()
	{
		if (!this.hasData) return this.selectedBlock().damage[0];
		return this.selectedBlock().damage[this.damage];
	}

	public void setDamage(int damage)
	{
		this.damage = damage;
		this.damageSelector.repaint();
		this.spinnerDamage.setText(Integer.toString(damage));
		this.updateDisplay();
	}

	public void setSelected(int selected, boolean sendUpdates)
	{
		this.selected = selected;
		if (sendUpdates) this.comboboxBlock.setSelectedItem(this.selectedBlock().name().toString());
		this.spinnerDamage.setValues(this.selectedBlock().damage);
		this.blockSelector.repaint();

		BufferedImage[] images = new BufferedImage[this.selectedBlock().damage.length];
		for (int i = 0; i < images.length; i++)
			images[i] = this.selectedBlock().texture(this.selectedBlock().damage[i]);
		this.damageSelector.setImages(images);
		this.setDamage(Math.min(this.damage, this.selectedBlock().damage.length - 1));
		this.updateDisplay();
	}

	private void updateDisplay()
	{
		if (this.hasData) this.labelName.setTextID(this.selectedBlock().name(this.selectedDamage()));
		else this.labelName.setTextID(this.selectedBlock().mainName());
		this.labelImage.setImage(this.selectedBlock().texture(this.selectedDamage()));
	}

}