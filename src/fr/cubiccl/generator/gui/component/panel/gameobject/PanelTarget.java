package fr.cubiccl.generator.gui.component.panel.gameobject;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import fr.cubiccl.generator.CommandGenerator;
import fr.cubiccl.generator.gameobject.registries.ObjectSaver;
import fr.cubiccl.generator.gameobject.target.TargetArgument;
import fr.cubiccl.generator.gameobject.target.Target;
import fr.cubiccl.generator.gameobject.target.Target.TargetType;
import fr.cubiccl.generator.gameobject.target.ArgumentType;
import fr.cubiccl.generator.gui.component.CGList;
import fr.cubiccl.generator.gui.component.button.CGButton;
import fr.cubiccl.generator.gui.component.combobox.OptionCombobox;
import fr.cubiccl.generator.gui.component.interfaces.ICustomObject;
import fr.cubiccl.generator.gui.component.interfaces.ITranslated;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.utils.PanelCustomObject;
import fr.cubiccl.generator.gui.component.textfield.CGEntry;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.IStateListener;
import fr.cubiccl.generator.utils.Text;

public class PanelTarget extends CGPanel implements ActionListener, IStateListener<CGPanel>, ListSelectionListener, ICustomObject<Target>
{
	public static final int ALL_ENTITIES = 0, PLAYERS_ONLY = 1, ENTITIES_ONLY = 2;
	private static final long serialVersionUID = 4720702579772411649L;
	private static final String[][] TARGETS =
	{
	{ "@a", "@e", "@p", "@r", "@s", "player" },
	{ "@a", "@p", "@r", "@s", "player" },
	{ "@e", "@s" } };
	private static final String[] TITLES =
	{ "target.title.any", "target.title.player", "target.title.entity" };

	private ArrayList<TargetArgument> arguments;
	private CGButton buttonAddArgument, buttonRemoveArgument;
	private OptionCombobox comboboxType, comboboxArgument;
	private CGEntry entryName;
	private CGLabel labelType, labelArguments;
	private CGList listArguments;
	private Set<ITranslated> listeners;
	private int mode;

	public PanelTarget(int mode)
	{
		this(TITLES[mode], mode, true);
	}

	/** @see PanelTarget#ALL_ENTITIES */
	public PanelTarget(String titleID, int mode)
	{
		this(titleID, mode, true);
	}

	/** @see PanelTarget#ALL_ENTITIES */
	public PanelTarget(String titleID, int mode, boolean customObjects)
	{
		super(titleID);
		this.mode = mode;
		this.arguments = new ArrayList<TargetArgument>();
		this.listeners = new HashSet<ITranslated>();
		this.setPreferredSize(new Dimension(450, 300));

		this.labelArguments = new CGLabel("target.arguments").setHasColumn(true);
		this.labelType = new CGLabel("target.type").setHasColumn(true);

		this.entryName = new CGEntry(new Text("target.name"), new Text("target.name"));
		this.entryName.container.setVisible(false);

		this.comboboxType = new OptionCombobox("target.type", TARGETS[this.mode]);
		this.comboboxArgument = new OptionCombobox("argument", ArgumentType.names());
		this.comboboxArgument.addActionListener(this);

		this.buttonAddArgument = new CGButton("general.add");
		this.buttonRemoveArgument = new CGButton("general.remove");
		this.buttonRemoveArgument.setEnabled(false);

		this.listArguments = new CGList();
		this.listArguments.scrollPane.setPreferredSize(new Dimension(200, 100));
		this.listArguments.scrollPane.setMinimumSize(new Dimension(200, 100));
		this.listArguments.addListSelectionListener(this);

		this.comboboxType.addActionListener(this);
		this.buttonAddArgument.addActionListener(this);
		this.buttonRemoveArgument.addActionListener(this);

		this.createLayout(customObjects);
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
			for (ITranslated listener : this.listeners)
				listener.updateTranslations();
		} else if (e.getSource() == this.comboboxArgument) this.buttonAddArgument.setEnabled(this.canAddArgument(this.getSelectedArgument()));
		else if (e.getSource() == this.buttonAddArgument)
		{
			ArgumentType argument = this.getSelectedArgument();
			if (argument == null) return;
			CommandGenerator.stateManager.setState(argument.createUI(), this);
		} else if (e.getSource() == this.buttonRemoveArgument)
		{
			int index = this.listArguments.getSelectedIndex();
			this.arguments.remove(this.listArguments.getSelectedIndex());
			this.onArgumentChange();
			this.listArguments.setSelectedIndex(index == this.arguments.size() ? index - 1 : index);
		}
	}

	public void addArgumentChangeListener(ITranslated listener)
	{
		this.listeners.add(listener);
	}

	private boolean canAddArgument(ArgumentType argument)
	{
		if (!argument.isUnique) return true;
		for (TargetArgument a : this.arguments)
			if (a.argument == argument) return false;
		return true;
	}

	private void createLayout(boolean customObjects)
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

		++gbc.gridy;
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.CENTER;
		if (customObjects) this.add(new PanelCustomObject<Target, Target>(this, ObjectSaver.targets), gbc);

		this.entryName.addKeyListener(new KeyListener()
		{
			@Override
			public void keyPressed(KeyEvent e)
			{}

			@Override
			public void keyReleased(KeyEvent e)
			{
				for (ITranslated listener : listeners)
					listener.updateTranslations();
			}

			@Override
			public void keyTyped(KeyEvent e)
			{}
		});
	}

	public String displayTarget()
	{
		try
		{
			Target t = this.generate();
			if (t.type == TargetType.PLAYER && t.playerName.equals("")) return "???";
			return t.toString();
		} catch (CommandGenerationException e)
		{
			return "???";
		}
	}

	@Override
	public Target generate() throws CommandGenerationException
	{
		if (this.player())
		{
			this.entryName.checkValue(CGEntry.STRING);
			return new Target(this.entryName.getText());
		}
		return new Target(Target.TargetType.find(this.comboboxType.getValue()), this.arguments.toArray(new TargetArgument[this.arguments.size()]));
	}

	private ArgumentType getSelectedArgument()
	{
		return ArgumentType.find(this.comboboxArgument.getValue());
	}

	private void onArgumentChange()
	{
		this.arguments.sort(new Comparator<TargetArgument>()
		{
			@Override
			public int compare(TargetArgument o1, TargetArgument o2)
			{
				int idComparison = o1.argument.id.compareTo(o2.argument.id);
				if (idComparison != 0) return idComparison;
				return o1.value.compareTo(o2.value);
			}
		});
		this.listArguments.clear();
		for (TargetArgument argument : this.arguments)
			this.listArguments.addValue(argument.toString());
		this.comboboxArgument.setSelectedIndex(this.comboboxArgument.getSelectedIndex()); // Trigger Add button update
		for (ITranslated listener : this.listeners)
			listener.updateTranslations();
	}

	/** @return True if the target type is a Player. */
	private boolean player()
	{
		return this.comboboxType.getValue().equals(TargetType.PLAYER.id);
	}

	public void removeArgumentChangeListener(ITranslated listener)
	{
		this.listeners.remove(listener);
	}

	@Override
	public void setupFrom(Target target)
	{
		if (target == null) return;

		for (int i = 0; i < TARGETS[this.mode].length; ++i)
			if (target.type.id.equals(TARGETS[this.mode][i]))
			{
				this.comboboxType.setSelectedIndex(i);
				break;
			}
		this.arguments.clear();

		if (target.type == TargetType.PLAYER)
		{
			this.entryName.setText(target.playerName);
			return;
		}

		if (target.arguments != null) for (TargetArgument a : target.arguments)
			this.arguments.add(a);

		this.onArgumentChange();
	}

	@Override
	public boolean shouldStateClose(CGPanel panel)
	{
		try
		{
			ArgumentType argument = this.getSelectedArgument();
			String value = this.getSelectedArgument().checkValue(panel);
			boolean reversed = value.startsWith("!");
			if (reversed) value = value.substring(1);

			TargetArgument a = new TargetArgument(argument, value, reversed);
			if (argument == ArgumentType.SCORE || argument == ArgumentType.SCORE_MIN) a = new TargetArgument(argument, value.split(" ")[0], value.split(" ")[1],
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
