package fr.cubiccl.generator.gui.component.panel.gameobject;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Comparator;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import fr.cubiccl.generator.CommandGenerator;
import fr.cubiccl.generator.gameobject.target.Argument;
import fr.cubiccl.generator.gameobject.target.Target;
import fr.cubiccl.generator.gameobject.target.Target.TargetType;
import fr.cubiccl.generator.gameobject.target.TargetArgument;
import fr.cubiccl.generator.gui.component.CGList;
import fr.cubiccl.generator.gui.component.button.CGButton;
import fr.cubiccl.generator.gui.component.combobox.OptionCombobox;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.textfield.CGEntry;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.IStateListener;
import fr.cubiccl.generator.utils.MissingValueException;
import fr.cubiccl.generator.utils.Text;
import fr.cubiccl.generator.utils.WrongValueException;

public class PanelTarget extends CGPanel implements ActionListener, IStateListener<CGPanel>, ListSelectionListener
{
	public static final int ALL_ENTITIES = 0, PLAYERS_ONLY = 1, ENTITIES_ONLY = 2;
	private static final long serialVersionUID = 4720702579772411649L;
	private static final String[][] TARGETS =
	{
	{ "@a", "@e", "@p", "@r", "player" },
	{ "@a", "@p", "@r", "player" },
	{ "@e" } };
	private static final String[] TITLES =
	{ "target.title.any", "target.title.player", "target.title.entity" };

	private ArrayList<Argument> arguments;
	private CGButton buttonAddArgument, buttonRemoveArgument;
	private OptionCombobox comboboxType, comboboxArgument;
	private CGEntry entryName;
	private CGLabel labelType, labelArguments;
	private CGList listArguments;
	private int mode;

	public PanelTarget(int mode)
	{
		this(TITLES[mode], mode);
	}

	/** @see PanelTarget#ALL_ENTITIES */
	public PanelTarget(String titleID, int mode)
	{
		super(titleID);
		this.mode = mode;
		this.arguments = new ArrayList<Argument>();
		this.setPreferredSize(new Dimension(450, 300));

		this.labelArguments = new CGLabel("target.arguments").setHasColumn(true);
		this.labelType = new CGLabel("target.type").setHasColumn(true);

		this.entryName = new CGEntry(new Text("target.name"));
		this.entryName.container.setVisible(false);

		this.comboboxType = new OptionCombobox("target.type", TARGETS[this.mode]);
		this.comboboxArgument = new OptionCombobox("argument", TargetArgument.names());
		this.comboboxArgument.addActionListener(this);

		this.buttonAddArgument = new CGButton("general.add");
		this.buttonRemoveArgument = new CGButton("general.remove");
		this.buttonRemoveArgument.setEnabled(false);

		this.listArguments = new CGList();
		this.listArguments.scrollPane.setPreferredSize(new Dimension(200, 120));
		this.listArguments.addListSelectionListener(this);

		this.comboboxType.addActionListener(this);
		this.buttonAddArgument.addActionListener(this);
		this.buttonRemoveArgument.addActionListener(this);

		this.createLayout();
		this.updateTranslations();
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == this.comboboxType)
		{
			boolean player = this.comboboxType.getValue().equals(TargetType.PLAYER.id);
			this.entryName.container.setVisible(player);
			this.labelArguments.setVisible(!player);
			this.comboboxArgument.setVisible(!player);
			this.buttonAddArgument.setVisible(!player);
			this.buttonRemoveArgument.setVisible(!player);
			this.listArguments.scrollPane.setVisible(!player);
		} else if (e.getSource() == this.comboboxArgument) this.buttonAddArgument.setEnabled(this.canAddArgument(this.getSelectedArgument()));
		else if (e.getSource() == this.buttonAddArgument)
		{
			TargetArgument argument = this.getSelectedArgument();
			if (argument == null) return;
			CommandGenerator.stateManager.setState(argument.createGui(), this);
		} else if (e.getSource() == this.buttonRemoveArgument)
		{
			int index = this.listArguments.getSelectedIndex();
			this.arguments.remove(this.listArguments.getSelectedIndex());
			this.onArgumentChange();
			this.listArguments.setSelectedIndex(index == this.arguments.size() ? index - 1 : index);
		}
	}

	private boolean canAddArgument(TargetArgument argument)
	{
		if (!argument.isUnique) return true;
		for (Argument a : this.arguments)
			if (a.argument == argument) return false;
		return true;
	}

	private void createLayout()
	{
		GridBagConstraints gbc = this.createGridBagLayout();

		gbc.fill = GridBagConstraints.HORIZONTAL;
		this.add(this.labelType, gbc);
		++gbc.gridx;
		this.add(this.comboboxType, gbc);

		--gbc.gridx;
		++gbc.gridy;
		++gbc.gridwidth;
		this.add(this.entryName.container, gbc);
		--gbc.gridwidth;
		this.add(this.labelArguments, gbc);
		++gbc.gridx;
		this.add(this.comboboxArgument, gbc);

		--gbc.gridx;
		++gbc.gridy;
		this.add(this.buttonRemoveArgument, gbc);
		++gbc.gridx;
		this.add(this.buttonAddArgument, gbc);

		--gbc.gridx;
		++gbc.gridy;
		++gbc.gridwidth;
		gbc.fill = GridBagConstraints.BOTH;
		this.add(this.listArguments.scrollPane, gbc);
	}

	public Target generateTarget() throws CommandGenerationException
	{
		if (this.player())
		{
			String name = this.entryName.getText();
			if (name.equals("")) throw new MissingValueException(this.entryName.label.getAbsoluteText());
			if (name.contains(" ")) throw new WrongValueException(this.entryName.label.getAbsoluteText(), new Text("error.space"), name);
			return new Target(name);
		}
		return new Target(Target.typeFromID(this.comboboxType.getValue()), this.arguments.toArray(new Argument[this.arguments.size()]));
	}

	private TargetArgument getSelectedArgument()
	{
		return TargetArgument.getArgumentFromId(this.comboboxArgument.getValue());
	}

	private void onArgumentChange()
	{
		this.arguments.sort(new Comparator<Argument>()
		{
			@Override
			public int compare(Argument o1, Argument o2)
			{
				int idComparison = o1.argument.id.compareTo(o2.argument.id);
				if (idComparison != 0) return idComparison;
				return o1.value.compareTo(o2.value);
			}
		});
		this.listArguments.clear();
		for (Argument argument : this.arguments)
			this.listArguments.addValue(argument.toString());
		this.comboboxArgument.setSelectedIndex(this.comboboxArgument.getSelectedIndex()); // Trigger Add button update
	}

	/** @return True if the target type is a Player. */
	private boolean player()
	{
		return this.comboboxType.getValue().equals(TargetType.PLAYER.id);
	}

	@Override
	public boolean shouldStateClose(CGPanel panel)
	{
		try
		{
			TargetArgument argument = this.getSelectedArgument();
			String value = this.getSelectedArgument().checkValue(panel);
			boolean reversed = value.startsWith("!");
			if (reversed) value = value.substring(1);

			Argument a = new Argument(argument, value, reversed);
			if (argument == TargetArgument.SCORE || argument == TargetArgument.SCORE_MIN) a = new Argument(argument, value.split(" ")[0], value.split(" ")[1],
					reversed);
			this.arguments.add(a);
			this.onArgumentChange();
			this.listArguments.setSelectedValue(a.toString(), true);
			return true;
		} catch (CommandGenerationException e)
		{
			CommandGenerator.report(e);
			return false;
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent arg0)
	{
		this.buttonRemoveArgument.setEnabled(this.listArguments.getSelectedIndex() != -1);
	}
}
