package fr.cubiccl.generator.gui.component.panel.gameobject;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import fr.cubiccl.generator.CommandGenerator;
import fr.cubiccl.generator.gameobject.ObjectRegistry;
import fr.cubiccl.generator.gameobject.PlacedBlock;
import fr.cubiccl.generator.gameobject.baseobjects.Block;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;
import fr.cubiccl.generator.gui.component.button.CGButton;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.label.ImageLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.utils.IStateListener;

public class PanelBlock extends CGPanel implements ActionListener, IStateListener<PanelBlockSelection>
{
	private static final long serialVersionUID = -8600189753659710473L;

	private Block block;
	private CGButton buttonSelectBlock;
	private int damage;
	private boolean hasData;
	private CGLabel labelName;
	private ImageLabel labelTexture;
	private PanelTags panelTags;

	public PanelBlock(String titleID)
	{
		this(titleID, true);
	}

	public PanelBlock(String titleID, boolean hasData)
	{
		super(titleID);
		this.hasData = hasData;
		this.block = ObjectRegistry.getBlocks(ObjectRegistry.SORT_NUMERICALLY)[1];
		this.damage = 0;

		GridBagConstraints gbc = this.createGridBagLayout();
		gbc.anchor = GridBagConstraints.CENTER;
		this.add(this.labelTexture = new ImageLabel(), gbc);
		++gbc.gridx;
		this.add(this.labelName = new CGLabel(""), gbc);
		++gbc.gridx;
		this.add(this.buttonSelectBlock = new CGButton("block.select"), gbc);
		gbc.gridx = 0;
		++gbc.gridy;
		gbc.gridwidth = 3;
		this.add(this.panelTags = new PanelTags("block.tags", Tag.BLOCK), gbc);

		this.buttonSelectBlock.addActionListener(this);
		this.updateDisplay();
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == this.buttonSelectBlock) CommandGenerator.stateManager.setState(new PanelBlockSelection(), this);
	}

	public PlacedBlock generateBlock()
	{
		return this.generateBlock(Tags.BLOCK);
	}

	public PlacedBlock generateBlock(TemplateCompound container)
	{
		return new PlacedBlock(this.selectedBlock(), this.damage, this.getNBT(container));
	}

	public TagCompound getNBT(TemplateCompound container)
	{
		return this.panelTags.generateTags(container);
	}

	public Block selectedBlock()
	{
		return this.block;
	}

	public int selectedDamage()
	{
		return this.damage;
	}

	public void setHasData(boolean hasData)
	{
		this.hasData = hasData;
		if (!this.hasData)
		{
			this.damage = 0;
			this.updateDisplay();
		}
	}

	@Override
	public boolean shouldStateClose(PanelBlockSelection panel)
	{
		this.block = panel.selectedBlock();
		this.damage = panel.selectedDamage();
		this.updateDisplay();
		this.panelTags.setObjectForTags(this.block.idString);
		return true;
	}

	private void updateDisplay()
	{
		if (this.hasData) this.labelName.setText(this.selectedBlock().name(this.damage).toString());
		else this.labelName.setText(this.selectedBlock().mainName().toString());
		this.labelTexture.setImage(this.selectedBlock().texture(this.damage));
	}

}
