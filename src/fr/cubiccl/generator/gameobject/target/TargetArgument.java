package fr.cubiccl.generator.gameobject.target;

import java.awt.GridBagConstraints;
import java.util.ArrayList;
import java.util.Comparator;

import fr.cubi.cubigui.CPanel;
import fr.cubiccl.generator.gameobject.ObjectRegistry;
import fr.cubiccl.generator.gameobject.baseobjects.Entity;
import fr.cubiccl.generator.gui.component.button.CGCheckBox;
import fr.cubiccl.generator.gui.component.combobox.OptionCombobox;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.textfield.CGEntry;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Replacement;
import fr.cubiccl.generator.utils.Text;
import fr.cubiccl.generator.utils.WrongValueException;

public class TargetArgument
{
	private static ArrayList<TargetArgument> arguments;
	public static TargetArgument X, Y, Z, R, RM, DX, DY, DZ, C, L, LM, M, RX, RXM, RY, RYM, NAME, TAG, TEAM, TYPE, SCORE, SCORE_MIN;

	public static TargetArgument[] arguments()
	{
		return arguments.toArray(new TargetArgument[arguments.size()]);
	}

	public static void createArguments()
	{
		arguments = new ArrayList<TargetArgument>();
		X = new TargetArgument("x");
		Y = new TargetArgument("y");
		Z = new TargetArgument("z");
		R = new TargetArgument("r");
		RM = new TargetArgument("rm");
		DX = new TargetArgument("dx");
		DY = new TargetArgument("dy");
		DZ = new TargetArgument("dz");
		C = new TargetArgument("c");
		L = new TargetArgument("l");
		LM = new TargetArgument("lm");
		M = new TargetArgument("m", true, true);
		RX = new TargetArgument("rx");
		RXM = new TargetArgument("rxm");
		RY = new TargetArgument("ry");
		RYM = new TargetArgument("rym");
		NAME = new TargetArgument("name", true, true);
		TAG = new TargetArgument("tag", false, true);
		TEAM = new TargetArgument("team", false, true);
		TYPE = new TargetArgument("type", true, true);
		SCORE = new TargetArgument("score", false, false);
		SCORE_MIN = new TargetArgument("score_min", false, false);

		arguments.sort(new Comparator<TargetArgument>()
		{
			@Override
			public int compare(TargetArgument o1, TargetArgument o2)
			{
				return o1.id.compareTo(o2.id);
			}
		});
	}

	public static TargetArgument getArgumentFromId(String id)
	{
		for (TargetArgument targetArgument : arguments)
			if (targetArgument.id.equals(id)) return targetArgument;
		return null;
	}

	public static String[] names()
	{
		TargetArgument[] args = arguments();
		String[] names = new String[args.length];
		for (int i = 0; i < names.length; ++i)
			names[i] = args[i].id;
		return names;
	}

	/** True if the value can be reversed. */
	public final boolean canReverse;
	public final String id;
	/** True if a target can only have one occurrence of this argument. */
	public final boolean isUnique;

	public TargetArgument(String id)
	{
		this(id, true, false);
	}

	private TargetArgument(String id, boolean isUnique, boolean canReverse)
	{
		this.id = id;
		this.isUnique = isUnique;
		this.canReverse = canReverse;
		arguments.add(this);
	}

	public String checkValue(CGPanel panel) throws CommandGenerationException
	{
		if (this == M || this == TYPE)
		{
			String value = ((OptionCombobox) panel.getComponent(1)).getValue();
			if (((CGCheckBox) panel.getComponent(2)).isSelected()) return "!" + (value.equals("all") ? "-1" : value);
			return value.equals("all") ? "-1" : value;
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

	public CGPanel createGui()
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
				Entity[] entities = ObjectRegistry.getEntities();
				String[] ids = new String[entities.length];
				for (int i = 0; i < ids.length; ++i)
					ids[i] = entities[i].id;
				panel.add(new OptionCombobox("entity", ids), gbc);
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
		panel.setName(this.name().toString());
		return panel;
	}

	public Text description()
	{
		return new Text("argument.description." + this.id);
	}

	public Text name()
	{
		return new Text("argument." + this.id);
	}

	private boolean valueInteger()
	{
		return this == X || this == Y || this == Z || this == R || this == RM || this == DX || this == DY || this == DZ || this == C || this == L || this == LM
				|| this == RX || this == RXM || this == RY || this == RYM;
	}

}
