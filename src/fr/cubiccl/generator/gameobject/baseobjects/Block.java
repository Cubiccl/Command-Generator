package fr.cubiccl.generator.gameobject.baseobjects;

import java.awt.Component;

import fr.cubiccl.generator.gui.component.interfaces.IObjectList;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.label.ImageLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelBlockSelection;
import fr.cubiccl.generator.gui.component.panel.utils.ListProperties;
import fr.cubiccl.generator.utils.CommandGenerationException;

public class Block extends BlockItem implements IObjectList<Block>
{

	public Block()
	{
		super(false, 0, null);
	}

	public Block(int idInt, String idString)
	{
		super(BLOCK, idInt, idString);
	}

	public Block(int idInt, String idString, int maxDamage)
	{
		super(BLOCK, idInt, idString, maxDamage);
	}

	public Block(int idInt, String idString, int... damage)
	{
		super(BLOCK, idInt, idString, damage);
	}

	@Override
	public CGPanel createPanel(ListProperties properties)
	{
		PanelBlockSelection p = new PanelBlockSelection(false);
		if (this.id() != null) p.setSelected(this);
		return p;
	}

	@Override
	public Component getDisplayComponent()
	{
		CGPanel p = new CGPanel();
		p.add(new CGLabel(this.mainName()));
		p.add(new ImageLabel(this.texture(0)));
		return p;
	}

	@Override
	public String getName(int index)
	{
		return this.mainName().toString();
	}

	@Override
	public Block setupFrom(CGPanel panel) throws CommandGenerationException
	{
		return ((PanelBlockSelection) panel).selectedBlock();
	}

}
