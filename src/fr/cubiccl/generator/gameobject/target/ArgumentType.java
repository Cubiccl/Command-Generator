package fr.cubiccl.generator.gameobject.target;

import java.awt.GridBagConstraints;
import java.util.ArrayList;
import java.util.Comparator;

import fr.cubi.cubigui.CPanel;
import fr.cubiccl.generator.gameobject.baseobjects.Entity;
import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.gui.component.button.CGCheckBox;
import fr.cubiccl.generator.gui.component.combobox.ObjectCombobox;
import fr.cubiccl.generator.gui.component.combobox.OptionCombobox;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.textfield.CGEntry;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Replacement;
import fr.cubiccl.generator.utils.Text;
import fr.cubiccl.generator.utils.WrongValueException;

/** Types of Target Arguments. */
public class ArgumentType
{
	/** Stores available types. */
	private static ArrayList<ArgumentType> arguments;
	public static ArgumentType X, Y, Z, R, RM, DX, DY, DZ, C, L, LM, M, RX, RXM, RY, RYM, NAME, TAG, TEAM, TYPE, SCORE, SCORE_MIN;

	/** @return The list of available types. */
	public static ArgumentType[] arguments()
	{
		return arguments.toArray(new ArgumentType[arguments.size()]);
	}

	/** Creates the Argument types. */
	public static void createArguments()
	{
		arguments = new ArrayList<ArgumentType>();
		X = new ArgumentType("x");
		Y = new ArgumentType("y");
		Z = new ArgumentType("z");
		R = new ArgumentType("r");
		RM = new ArgumentType("rm");
		DX = new ArgumentType("dx");
		DY = new ArgumentType("dy");
		DZ = new ArgumentType("dz");
		C = new ArgumentType("c");
		L = new ArgumentType("l");
		LM = new ArgumentType("lm");
		M = new ArgumentType("m", true, true);
		RX = new ArgumentType("rx");
		RXM = new ArgumentType("rxm");
		RY = new ArgumentType("ry");
		RYM = new ArgumentType("rym");
		NAME = new ArgumentType("name", true, true);
		TAG = new ArgumentType("tag", false, true);
		TEAM = new ArgumentType("team", false, true);
		TYPE = new ArgumentType("type", true, true);
		SCORE = new ArgumentType("score", false, false);
		SCORE_MIN = new ArgumentType("score_min", false, false);

		arguments.sort(new Comparator<ArgumentType>()
		{
			@Override
			public int compare(ArgumentType o1, ArgumentType o2)
			{
				return o1.id.compareTo(o2.id);
			}
		});
	}

	/** @param id - An Argument ID.
	 * @return The Argument type with the input ID. */
	public static ArgumentType find(String id)
	{
		for (ArgumentType targetArgument : arguments)
			if (targetArgument.id.equals(id)) return targetArgument;
		return null;
	}

	/** @return The list of names of Arguments. */
	public static String[] names()
	{
		ArgumentType[] args = arguments();
		String[] names = new String[args.length];
		for (int i = 0; i < names.length; ++i)
			names[i] = args[i].id;
		return names;
	}

	/** <code>true</code> if the value can be reversed. */
	public final boolean canReverse;
	/** This Argument's ID. */
	public final String id;
	/** <code>true</code> if a target can only have one occurrence of this argument. */
	public final boolean isUnique;

	public ArgumentType(String id)
	{
		this(id, true, false);
	}

	private ArgumentType(String id, boolean isUnique, boolean canReverse)
	{
		this.id = id;
		this.isUnique = isUnique;
		this.canReverse = canReverse;
		arguments.add(this);
	}

	/** Reads the user input.
	 * 
	 * @param panel - The UI the user interacted with.
	 * @return The value to use.
	 * @throws CommandGenerationException - If the user input is wrong. */
	public String checkValue(CGPanel panel) throws CommandGenerationException
	{
		if (this == M)
		{
			String value = ((OptionCombobox) panel.getComponent(1)).getValue();
			if (((CGCheckBox) panel.getComponent(2)).isSelected()) return "!" + (value.equals("all") ? "-1" : value);
			return value.equals("all") ? "-1" : value;
		}
		if (this == TYPE)
		{
			@SuppressWarnings("unchecked")
			Entity value = ((ObjectCombobox<Entity>) panel.getComponent(1)).getSelectedObject();
			if (((CGCheckBox) panel.getComponent(2)).isSelected()) return "!" + value.id().replaceAll("minecraft:", "");
			return value.id().replaceAll("minecraft:", "");
		}

		CGEntry entry = (CGEntry) ((CPanel) panel.getComponent(0)).getComponent(1);
		String value = entry.getText();
		Text name = entry.label.getAbsoluteText();

		if (this == SCORE || this == SCORE_MIN)
		{
			// Do u like dat code lel | WTF is that srsly
			String value2 = ((CGEntry) ((CPanel) panel.getComponent(1)).getComponent(1)).getText();
			if (value.equals("")) throw new CommandGenerationException(new Text("error.value"));
			if (value.contains(" ")) throw new WrongValueException(name, new Text("error.space"), value);
			try
			{
				Integer.parseInt(value2);
			} catch (NumberFormatException e)
			{
				throw new WrongValueException(name, new Text("error.integer"), value2);
			}
			return value + " " + value2;
		}

		boolean reversed = false;
		if (this.canReverse) reversed = ((CGCheckBox) panel.getComponent(1)).isSelected();

		if (this.valueInteger()) try
		{
			int i = Integer.parseInt(value);
			if (this == R || this == RM || this == DX || this == DY || this == DZ || this == C || this == L || this == LM)
			{
				if (i < 0) throw new WrongValueException(name, new Text("error.integer.positive"), value);
			}
			if (this == RX || this == RXM) if (i < -90 || i > 90) throw new WrongValueException(name, new Text("error.number.bounds", new Replacement("<min>",
					"-90"), new Replacement("<max>", "90"), new Replacement("<max>", "180")), value);
		} catch (NumberFormatException e)
		{
			throw new WrongValueException(name, new Text("error.integer"), value);
		}
		else
		{
			if (value.equals("") && this != TAG && this != TEAM) throw new CommandGenerationException(new Text("error.value"));
			if (value.contains(" ")) throw new WrongValueException(name, new Text("error.space"), value);
		}

		if (reversed) return "!" + value;
		return value;
	}

	/** @return The UI to show to the user to create this Argument. */
	public CGPanel createUI()
	{

		if (this == SCORE || this == SCORE_MIN)
		{
			CGPanel panel = new CGPanel();
			GridBagConstraints gbc = panel.createGridBagLayout();
			gbc.gridx = 0;
			gbc.gridy = 0;
			panel.add(new CGEntry(Text.OBJECTIVE, Text.OBJECTIVE).container, gbc);
			++gbc.gridy;
			panel.add(new CGEntry(Text.VALUE, "0", Text.INTEGER).container, gbc);
			panel.setName(this.name().toString());
			return panel;
		}

		CGPanel panel = new CGPanel();
		GridBagConstraints gbc = panel.createGridBagLayout();
		gbc.gridx = 0;
		gbc.gridy = 0;

		if (this == M || this == TYPE)
		{
			panel.add(new CGLabel(this.description()), gbc);
			++gbc.gridx;
			if (this == M)
			{
				panel.add(new OptionCombobox("gamemode", "all", "survival", "creative", "adventure", "spectator"), gbc);
			}
			if (this == TYPE)
			{
				Entity[] entities = ObjectRegistry.entities.list(true);
				String[] ids = new String[entities.length];
				for (int i = 0; i < ids.length; ++i)
					ids[i] = entities[i].id;
				panel.add(new ObjectCombobox<Entity>(entities), gbc);
			}
			--gbc.gridx;
			++gbc.gridy;
			++gbc.gridwidth;
		} else panel.add(new CGEntry(this.description(), this.name()).container, gbc);

		if (this.canReverse)
		{
			++gbc.gridy;
			panel.add(new CGCheckBox("target.argument.reverse"), gbc);
		}
		panel.setName(this.name());
		return panel;
	}

	/** @return This Argument's description. */
	public Text description()
	{
		return new Text("argument.description." + this.id);
	}

	/** @return This Argument's description. */
	public Text name()
	{
		return new Text("argument." + this.id);
	}

	/** @return <code>true</code> if this Argument's value should be an integer. */
	private boolean valueInteger()
	{
		return this == X || this == Y || this == Z || this == R || this == RM || this == DX || this == DY || this == DZ || this == C || this == L || this == LM
				|| this == RX || this == RXM || this == RY || this == RYM;
	}

}
