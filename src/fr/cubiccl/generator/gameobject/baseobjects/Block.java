package fr.cubiccl.generator.gameobject.baseobjects;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;

import org.jdom2.Element;

import fr.cubiccl.generator.gui.component.interfaces.IObjectList;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.label.ImageLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelBlockSelection;
import fr.cubiccl.generator.gui.component.panel.utils.ListProperties;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Utils;

public class Block extends BlockItem implements IObjectList<Block>
{

	private HashMap<String, BlockState> states;
	/** Damage values that are possible but not used. */
	private HashSet<Integer> unusedDamage;

	public Block()
	{
		super(false, 0, null);
		this.createStates();
	}

	public Block(int idInt, String idString)
	{
		super(BLOCK, idInt, idString);
		this.createStates();
	}

	public Block(int idInt, String idString, int maxDamage)
	{
		super(BLOCK, idInt, idString, maxDamage);
		this.createStates();
	}

	public Block(int idInt, String idString, int... damage)
	{
		super(BLOCK, idInt, idString, damage);
		this.createStates();
	}

	public void addState(BlockState state)
	{
		this.states.put(state.id, state);
		this.updateDamageValues();
	}

	public void addUnusedDamage(int unused)
	{
		this.unusedDamage.add(unused);
		this.updateDamageValues();
	}

	@Override
	public CGPanel createPanel(ListProperties properties)
	{
		PanelBlockSelection p = new PanelBlockSelection(false);
		if (this.id() != null) p.setSelected(this);
		return p;
	}

	private void createStates()
	{
		this.states = new HashMap<String, BlockState>();
		this.unusedDamage = new HashSet<Integer>();
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

	protected boolean shouldSaveState(BlockState state)
	{
		return this.customObjectName == null;
	}

	@Override
	public Element toXML()
	{
		Element root = super.toXML();
		if (root.getChild("customdamage") != null) root.removeChild("customdamage");
		if (root.getChild("maxdamage") != null) root.removeChild("maxdamage");
		root.addContent(new Element("states"));
		for (BlockState state : this.states.values())
		{
			if (!this.shouldSaveState(state)) continue;
			Element s = new Element("state");
			s.setAttribute("id", state.id);
			s.setAttribute("type", Byte.toString(state.type));
			s.setAttribute("damage", Integer.toString(state.damageValue));
			if (state.getStartsAt() != 0) s.setAttribute("startsat", Integer.toString(state.getStartsAt()));

			boolean detailValues = true;
			if (state.type == BlockState.INTEGER)
			{
				int[] values = new int[state.values.length];
				for (int i = 0; i < values.length; ++i)
					values[i] = Integer.parseInt(state.values[i]);

				if (Utils.isArrayConsecutive(values))
				{
					detailValues = false;
					s.setAttribute("max", Integer.toString(values.length - 1));
				}
			}
			if (detailValues) for (String v : state.values)
				s.addContent(new Element("v").setText(v));
			root.getChild("states").addContent(s);
		}
		if (root.getChild("states").getChildren().size() == 0) root.removeChild("states");

		if (!this.unusedDamage.isEmpty())
		{
			root.addContent(new Element("unuseddamage"));
			for (Integer i : this.unusedDamage)
				root.getChild("unuseddamage").addContent(new Element("d").setText(i.toString()));
		}
		return root;
	}

	@Override
	public Block update(CGPanel panel) throws CommandGenerationException
	{
		return ((PanelBlockSelection) panel).selectedBlock();
	}

	private void updateDamageValues()
	{
		ArrayList<Integer> damage = new ArrayList<Integer>(), previousDamage = new ArrayList<Integer>(), temp = new ArrayList<Integer>();
		ArrayList<BlockState> s = new ArrayList<BlockState>();
		s.addAll(this.states.values());
		s.sort(Comparator.naturalOrder());
		damage.add(0);

		for (BlockState state : s)
		{
			// Store previous damage values in temporary array
			previousDamage.clear();
			previousDamage.addAll(damage);
			damage.clear();

			// Get damage values
			temp.clear();
			temp.addAll(state.damageValues());

			for (Integer previous : previousDamage)
				for (Integer d : temp)
					damage.add(d + previous);
		}

		damage.sort(Comparator.naturalOrder());
		damage.removeAll(this.unusedDamage);

		int[] d = new int[damage.size()];
		for (int i = 0; i < d.length; ++i)
			d[i] = damage.get(i);
		if (Utils.isArrayConsecutive(d)) this.setMaxDamage(d.length - 1);
		else this.setDamageCustom(d);
	}

}
