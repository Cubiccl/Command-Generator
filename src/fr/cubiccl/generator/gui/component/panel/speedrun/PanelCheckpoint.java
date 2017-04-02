package fr.cubiccl.generator.gui.component.panel.speedrun;

import java.awt.GridBagConstraints;

import fr.cubiccl.generator.gameobject.speedrun.Checkpoint;
import fr.cubiccl.generator.gameobject.speedrun.ItemMove;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.utils.ListListener;
import fr.cubiccl.generator.gui.component.panel.utils.PanelObjectList;
import fr.cubiccl.generator.utils.Text;

public class PanelCheckpoint extends CGPanel implements ListListener<ItemMove>
{
	private static final long serialVersionUID = -9059269199436502504L;

	private Checkpoint checkpoint = null;
	private CGLabel labelName;
	private PanelObjectList<ItemMove> listMoves;

	public PanelCheckpoint()
	{
		super("speedrun.checkpoint");

		GridBagConstraints gbc = new GridBagConstraints();
		this.add(this.labelName = new CGLabel(new Text("", false)), gbc);
		++gbc.gridy;
		this.add(this.listMoves = new PanelObjectList<ItemMove>(null, (String) null, ItemMove.class), gbc);

		this.listMoves.addListListener(this);
	}

	@Override
	public void onAddition(int index, ItemMove object)
	{
		this.checkpoint.addMove(object);
	}

	@Override
	public void onChange(int index, ItemMove object)
	{
		this.checkpoint.speedrun.onChange();
	}

	@Override
	public void onDeletion(int index, ItemMove object)
	{
		this.checkpoint.removeMove(object);
	}

	public void setCheckpoint(Checkpoint checkpoint)
	{
		this.checkpoint = checkpoint;
		if (this.checkpoint == null)
		{
			this.labelName.setTextID(new Text("", false));
			this.listMoves.setValues(new ItemMove[0]);
		} else
		{
			this.labelName.setTextID(new Text(this.checkpoint.getName(), false));
			this.listMoves.setValues(checkpoint.moves().toArray(new ItemMove[checkpoint.moves().size()]));
		}
	}

}
