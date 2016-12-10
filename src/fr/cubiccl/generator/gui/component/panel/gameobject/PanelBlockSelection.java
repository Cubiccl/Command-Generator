package fr.cubiccl.generator.gui.component.panel.gameobject;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JPanel;

import fr.cubi.cubigui.CPanel;
import fr.cubiccl.generator.gameobject.ObjectRegistry;
import fr.cubiccl.generator.gameobject.baseobjects.Block;
import fr.cubiccl.generator.gui.MCInventory;
import fr.cubiccl.generator.gui.MCInventoryDrawer;
import fr.cubiccl.generator.gui.component.CScrollPane;
import fr.cubiccl.generator.gui.component.combobox.ObjectCombobox;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.label.ImageLabel;
import fr.cubiccl.generator.gui.component.panel.utils.ConfirmPanel;
import fr.cubiccl.generator.gui.component.textfield.CGSpinner;

public class PanelBlockSelection extends ConfirmPanel implements ComponentListener
{
	private static class BlockSelector extends JPanel implements MCInventory
	{
		private static final long serialVersionUID = 2195482097558117484L;

		private Block[] blocks;
		private MCInventoryDrawer drawer;
		private int hovering;
		private int objectsPerLine;
		private PanelBlockSelection parent;

		public BlockSelector(PanelBlockSelection parent)
		{
			this.parent = parent;
			this.blocks = this.parent.blocks;
			this.hovering = -1;
			this.setObjectsPerLine(20);
			this.addMouseListener(this.drawer = new MCInventoryDrawer(this));
			this.addMouseMotionListener(this.drawer);
		}

		@Override
		public void onClick()
		{
			if (this.hovering != -1)
			{
				this.parent.setSelected(this.hovering, true);
				this.repaint();
			}
		}

		@Override
		public void onExit()
		{
			this.hovering = -1;
			this.repaint();
		}

		@Override
		public void onMove(int x, int y)
		{
			int previous = this.hovering;
			this.hovering = x / (BLOCK_SIZE + GAP) + y / (BLOCK_SIZE + GAP) * this.objectsPerLine;
			if (previous != this.hovering) this.repaint();
		}

		@Override
		public void paint(Graphics g)
		{
			super.paint(g);
			for (int i = 0; i < this.blocks.length; ++i)
			{
				int x = (i % this.objectsPerLine) * (BLOCK_SIZE + GAP) + 1;
				int y = i / this.objectsPerLine * (BLOCK_SIZE + GAP) + 1;
				this.drawer.drawBox(g, x, y, BLOCK_SIZE);
				if (i == this.parent.selected) this.drawer.drawSelection(g, x, y, BLOCK_SIZE);
				g.drawImage(this.blocks[i].texture(0), x, y, BLOCK_SIZE, BLOCK_SIZE, null);
				if (i == this.hovering && i != this.parent.selected) this.drawer.drawHovering(g, x, y, BLOCK_SIZE);
			}
		}

		public void setObjectsPerLine(int objectsPerLine)
		{
			this.objectsPerLine = Math.min(20, objectsPerLine);
			int width = this.objectsPerLine * (BLOCK_SIZE + GAP) - GAP, height = (this.blocks.length / this.objectsPerLine + 1) * (BLOCK_SIZE + GAP);
			this.setSize(width, height);
			this.setPreferredSize(new Dimension(width, height));
		}

	}

	private static class DamageSelector extends JPanel implements MCInventory
	{
		private static final long serialVersionUID = -3118157489090883128L;

		private Block block;
		private MCInventoryDrawer drawer;
		private int hovering;
		private PanelBlockSelection parent;

		public DamageSelector(PanelBlockSelection parent)
		{
			this.parent = parent;
			this.hovering = -1;
			this.setBlock(this.parent.selectedBlock());
			this.addMouseListener(this.drawer = new MCInventoryDrawer(this));
			this.addMouseMotionListener(this.drawer);
		}

		@Override
		public void onClick()
		{
			if (this.hovering != -1 && this.hovering < this.block.damage.length) this.parent.setDamage(this.hovering);
		}

		@Override
		public void onExit()
		{
			this.hovering = -1;
			this.repaint();
		}

		@Override
		public void onMove(int x, int y)
		{
			int previous = this.hovering;
			this.hovering = x / (BLOCK_SIZE + GAP);
			if (previous != this.hovering) this.repaint();
		}

		@Override
		public void paint(Graphics g)
		{
			super.paint(g);
			for (int i = 0; i < this.block.damage.length; ++i)
			{
				int x = i * (BLOCK_SIZE + GAP) + 1;
				int y = 1;
				this.drawer.drawBox(g, x, y, BLOCK_SIZE);
				if (i == this.parent.damage) this.drawer.drawSelection(g, x, y, BLOCK_SIZE);
				g.drawImage(this.block.texture(this.block.damage[i]), x, y, BLOCK_SIZE, BLOCK_SIZE, null);
				if (i == this.hovering && i != this.parent.damage) this.drawer.drawHovering(g, x, y, BLOCK_SIZE);
			}
		}

		public void setBlock(Block block)
		{
			this.block = block;
			this.setPreferredSize(new Dimension(this.block.damage.length * (BLOCK_SIZE + GAP), BLOCK_SIZE + 3));
			this.repaint();
		}

	}

	private static final int BLOCK_SIZE = 64, GAP = 5;
	private static final long serialVersionUID = -3259302480348824205L;

	private Block[] blocks;
	private BlockSelector blockSelector;
	private ObjectCombobox<Block> comboboxBlock;
	private DamageSelector damageSelector;
	private ImageLabel labelImage;
	private CGLabel labelName;
	private CScrollPane scrollpane;
	private int selected, damage;
	private CGSpinner spinnerDamage;

	public PanelBlockSelection()
	{
		super("block.select", null);
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
		p.add(this.scrollpane = new CScrollPane(this.blockSelector = new BlockSelector(this)), gbc);
		++gbc.gridy;
		p.add(new CGLabel("block.data").setHasColumn(true), gbc);
		++gbc.gridy;
		p.add(this.damageSelector = new DamageSelector(this), gbc);

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
		if (e.getSource() == this.comboboxBlock) this.setSelected(this.comboboxBlock.getSelectedIndex(), false);
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
		return this.selectedBlock().damage[this.damage];
	}

	public void setDamage(int damage)
	{
		this.damage = damage;
		this.damageSelector.repaint();
		this.updateDisplay();
	}

	public void setSelected(int selected, boolean sendUpdates)
	{
		this.selected = selected;
		if (sendUpdates) this.comboboxBlock.setSelectedItem(this.selectedBlock().name().toString());
		this.spinnerDamage.setValues(this.selectedBlock().damage);
		this.blockSelector.repaint();
		this.damageSelector.setBlock(this.selectedBlock());
		this.setDamage(Math.min(this.damage, this.selectedBlock().damage.length - 1));
		this.updateDisplay();
	}

	private void updateDisplay()
	{
		this.labelName.setTextID(this.selectedBlock().name(this.selectedDamage()));
		this.labelImage.setImage(this.selectedBlock().texture(this.selectedDamage()));
	}

}
