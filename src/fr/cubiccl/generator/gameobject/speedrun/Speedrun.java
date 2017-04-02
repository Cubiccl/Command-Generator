package fr.cubiccl.generator.gameobject.speedrun;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Comparator;

import org.jdom2.Element;

import fr.cubiccl.generator.CommandGenerator;
import fr.cubiccl.generator.gameobject.GameObject;
import fr.cubiccl.generator.gameobject.speedrun.Checkpoint.CheckpointResult;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;
import fr.cubiccl.generator.gui.Dialogs;
import fr.cubiccl.generator.gui.component.interfaces.IObjectList;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.speedrun.PanelSpeedrun;
import fr.cubiccl.generator.gui.component.panel.utils.ListProperties;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;

public class Speedrun extends GameObject implements IObjectList<Speedrun>
{

	public static Speedrun createFrom(Element speedrun)
	{
		Speedrun s = new Speedrun();
		s.isPost19 = Boolean.parseBoolean(speedrun.getAttributeValue("ispost19"));
		s.findProperties(speedrun);
		for (Element checkpoint : speedrun.getChildren("checkpoint"))
			s.addCheckpoint(Checkpoint.createFrom(s, checkpoint));
		s.checkpoints.sort(Comparator.naturalOrder());
		return s;
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
		if ((boolean) properties.get("new"))
		{
			String name = Dialogs.showInputDialog(new Text("objects.name").toString());
			if (name != null) this.setCustomName(name);
			else return null;
			CommandGenerator.window.panelSpeedrunSelection.list.add(this);
		}
		CommandGenerator.stateManager.clear();
		return new PanelSpeedrun(this);
	}

	public Checkpoint getCheckpoint(int index)
	{
		return this.checkpoints.get(index);
	}

	@Override
	public Component getDisplayComponent()
	{
		return new CGLabel(new Text(this.customName(), false));
	}

	@Override
	public String getName(int index)
	{
		return this.customName();
	}

	public void insertCheckpoint(int index, Checkpoint checkpoint)
	{
		this.checkpoints.add(index, checkpoint);
		this.verify();
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

	@Override
	public void onChange()
	{
		super.onChange();
		PanelSpeedrun.instance.onSpeedrunUpdate();
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
	@Deprecated
	public String toCommand()
	{
		return this.toString();
	}

	@Override
	public String toString()
	{
		return this.customName();
	}

	@Deprecated
	@Override
	public TagCompound toTag(TemplateCompound container)
	{
		return null;
	}

	@Override
	public Element toXML()
	{
		Element root = this.createRoot("speedrun");
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

	@Deprecated
	@Override
	public Speedrun update(CGPanel panel) throws CommandGenerationException
	{
		return this;
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
		this.onChange();
	}
}
