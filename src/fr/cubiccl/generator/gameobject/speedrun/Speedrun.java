package fr.cubiccl.generator.gameobject.speedrun;

import java.awt.Component;
import java.util.ArrayList;

import org.jdom2.Element;

import fr.cubiccl.generator.gameobject.GameObject;
import fr.cubiccl.generator.gameobject.speedrun.Checkpoint.CheckpointResult;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;
import fr.cubiccl.generator.gui.component.interfaces.IObjectList;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.utils.ListProperties;
import fr.cubiccl.generator.utils.CommandGenerationException;

public class Speedrun extends GameObject implements IObjectList<Speedrun>
{

	public static Speedrun createFrom(Element speedrun)
	{
		// TODO Auto-generated method stub
		return null;
	}

	private ArrayList<Checkpoint> checkpoints;
	private boolean isValid, isPost19;
	private ArrayList<MissingItemsError> missingItems;
	private ArrayList<MissingSpaceError> missingSpace;
	private ArrayList<ThrownItemsWarning> thrownItems;

	public Speedrun()
	{
		this.checkpoints = new ArrayList<Checkpoint>();
		this.missingItems = new ArrayList<MissingItemsError>();
		this.missingSpace = new ArrayList<MissingSpaceError>();
		this.thrownItems = new ArrayList<ThrownItemsWarning>();
		this.isPost19 = true;
		this.verify();
	}

	public void addCheckpoint(Checkpoint checkpoint)
	{
		this.checkpoints.add(checkpoint);
		this.verify();
	}

	@Override
	public CGPanel createPanel(ListProperties properties)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Component getDisplayComponent()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName(int index)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isPost19()
	{
		return this.isPost19;
	}

	public boolean isValid()
	{
		return this.isValid;
	}

	public MissingItemsError[] missingItems()
	{
		return this.missingItems.toArray(new MissingItemsError[this.missingItems.size()]);
	}

	public MissingSpaceError[] missingSpace()
	{
		return this.missingSpace.toArray(new MissingSpaceError[this.missingSpace.size()]);
	}

	public void moveCheckpointLeft(Checkpoint checkpoint)
	{
		int i = this.checkpoints.indexOf(checkpoint);
		if (i <= 0) return;

		Checkpoint previous = this.checkpoints.get(i - 1);
		this.checkpoints.set(i - 1, checkpoint);
		this.checkpoints.set(i, previous);
		this.verify();
	}

	public void moveCheckpointRight(Checkpoint checkpoint)
	{
		int i = this.checkpoints.indexOf(checkpoint);
		if (i >= this.checkpoints.size() - 1) return;

		Checkpoint previous = this.checkpoints.get(i + 1);
		this.checkpoints.set(i + 1, checkpoint);
		this.checkpoints.set(i, previous);
		this.verify();
	}

	public void removeCheckpoint(Checkpoint checkpoint)
	{
		this.checkpoints.remove(checkpoint);
		this.verify();
	}

	public void setPost19(boolean isPost19)
	{
		this.isPost19 = isPost19;
		this.verify();
	}

	public ThrownItemsWarning[] thrownItems()
	{
		return this.thrownItems.toArray(new ThrownItemsWarning[this.thrownItems.size()]);
	}

	@Override
	public String toCommand()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toString()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TagCompound toTag(TemplateCompound container)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Element toXML()
	{
		Element root = new Element("speedrun");
		root.setAttribute("name", this.customName());
		root.setAttribute("ispost19", Boolean.toString(this.isPost19));
		for (Checkpoint c : this.checkpoints)
		{
			Element checkpoint = new Element("checkpoint");
			checkpoint.setAttribute("position", Integer.toString(c.position));
			for (ItemMove m : c.moves())
				checkpoint.addContent(m.toXML());
			root.addContent(checkpoint);
		}
		return root;
	}

	@Override
	public Speedrun update(CGPanel panel) throws CommandGenerationException
	{
		// TODO Auto-generated method stub
		return null;
	}

	public void verify()
	{
		this.missingItems.clear();
		for (int i = 0; i < this.checkpoints.size(); ++i)
			this.checkpoints.get(i).position = i;

		Inventory inventory = new Inventory(this.isPost19 ? 37 : 36);
		for (Checkpoint checkpoint : this.checkpoints)
		{
			CheckpointResult result = inventory.apply(checkpoint);
			if (!result.missingItems.isEmpty()) this.missingItems.add(result.missingItems);
			if (!result.missingSpace.isEmpty()) this.missingSpace.add(result.missingSpace);
			if (!result.thrownItems.isEmpty()) this.thrownItems.add(result.thrownItems);
			try
			{
				checkpoint.currentInventory = inventory.clone();
			} catch (CloneNotSupportedException e)
			{
				e.printStackTrace();
			}
			checkpoint.result = result;
		}

		this.isValid = this.missingItems.isEmpty() && this.missingSpace.isEmpty();
	}
}
