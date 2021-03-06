package fr.cubiccl.generator.gameobject.baseobjects;

import java.awt.Component;
import java.util.*;
import java.util.function.Predicate;

import org.jdom2.Element;

import fr.cubiccl.generator.gameobject.registries.ObjectCreator;
import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.gui.component.interfaces.IObjectList;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.label.ImageLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelBlockSelection;
import fr.cubiccl.generator.gui.component.panel.utils.ListProperties;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Utils;

public class Block extends BlockItem<Block> implements IObjectList<Block>
{

	/** The possible {@link BlockState Block states} for this Block. */
	private HashMap<String, BlockState> states;
	/** Damage values that would be created with the {@link Block#states Block states} but are not used. */
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

	/** Adds a State to this Block.
	 * 
	 * @param state - The Block state to add. */
	public void addState(BlockState state)
	{
		this.states.put(state.id, state);
		this.updateDamageValues();
	}

	/** Adds a damage value to the list of unused damage values.
	 * 
	 * @param unused - The damage that shouldn't be used.
	 * @see Block#unusedDamage */
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

	/** Initializes the Block States arrays. */
	private void createStates()
	{
		this.states = new HashMap<String, BlockState>();
		this.unusedDamage = new HashSet<Integer>();
	}

	/** Parses the input Block states to determine the corresponding damage value.
	 * 
	 * @param state - The states to apply.
	 * @return The resulting damage value.
	 * @throws CommandGenerationException - If State parsing fails. */
	public int damageFromState(String state) throws CommandGenerationException
	{
		HashMap<String, String> parsed = BlockState.parseState(state);
		int damage = 0;
		for (String id : parsed.keySet())
			if (this.states.keySet().contains(id) && this.states.get(id).hasValue(parsed.get(id))) damage += this.states.get(id).damageForValue(parsed.get(id));
		if (this.isDamageValid(damage)) return damage;
		return this.getDamageValues()[0];
	}

	@Override
	public Block duplicate(Block object)
	{
		return this;
	}

	/** Finds the Block states described by the input damage value.
	 * 
	 * @param damage - A damage value.
	 * @return A Hashmap containing a value for each used Block state. */
	public HashMap<String, String> findStatesFromDamage(int damage)
	{
		ArrayList<BlockState> s = new ArrayList<BlockState>();
		s.addAll(this.states.values());
		s.removeIf(new Predicate<BlockState>()
		{

			@Override
			public boolean test(BlockState t)
			{
				return !t.isDamageCustom() && t.damageValue == -1;
			}
		});
		s.sort(Comparator.naturalOrder());
		HashMap<String, String> states = new HashMap<String, String>();
		for (int i = s.size() - 1; i >= 0; --i)
		{
			ArrayList<Integer> d = s.get(i).damageValues();
			for (int j = d.size() - 1; j >= 0; --j)
				if (damage >= d.get(j))
				{
					damage -= d.get(j);
					states.put(s.get(i).id, s.get(i).valueForDamage(d.get(j)));
					break;
				}
		}

		return states;
	}

	@Override
	public Block fromXML(Element xml)
	{
		super.fromXML(xml);

		if (xml.getChild("states") != null) for (Element state : xml.getChild("states").getChildren("state"))
		{
			ArrayList<String> values = new ArrayList<String>();
			for (Element v : state.getChildren("v"))
				values.add(v.getText());
			if (state.getAttribute("max") != null) for (int i = 0; i <= Integer.parseInt(state.getAttributeValue("max")); ++i)
				values.add(Integer.toString(i));

			BlockState s;
			if (state.getAttribute("damagecustom") != null) s = new BlockState(state.getAttributeValue("id"), Byte.parseByte(state.getAttributeValue("type")),
					ObjectCreator.createDamage(state.getAttributeValue("damagecustom")), values.toArray(new String[values.size()]));
			else s = new BlockState(state.getAttributeValue("id"), Byte.parseByte(state.getAttributeValue("type")), Integer.parseInt(state
					.getAttributeValue("damage")), values.toArray(new String[values.size()]));
			if (state.getAttribute("startsat") != null) s.setStartsAt(Integer.parseInt(state.getAttributeValue("startsat")));
			this.addState(s);
		}

		if (xml.getChild("unuseddamage") != null) for (Element d : xml.getChild("unuseddamage").getChildren("d"))
			this.addUnusedDamage(Integer.parseInt(d.getText()));

		return this;
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

	/** @return The possible {@link BlockState Block states} for this Block. */
	public Collection<BlockState> getStates()
	{
		return this.states.values();
	}

	@Override
	public Block register()
	{
		ObjectRegistry.blocks.register(this);
		return this;
	}

	/** Removes a State from this Block.
	 * 
	 * @param state - The Block state to remove. */
	public void removeState(BlockState state)
	{
		this.states.remove(state.id);
		this.updateDamageValues();
	}

	/** @param state - A Block state.
	 * @return <code>true</code> if the input Block state should be saved in XML. */
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
			if (state.isDamageCustom())
			{
				String d = Integer.toString(state.customDamageValues[0]);
				for (int i = 1; i < state.customDamageValues.length; ++i)
					d += ":" + state.customDamageValues[i];
				s.setAttribute("damagecustom", d);
			} else s.setAttribute("damage", Integer.toString(state.damageValue));
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

	/** Reloads damage values to match this Block's states. */
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
