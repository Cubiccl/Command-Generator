package fr.cubiccl.generator.command;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import fr.cubiccl.generator.gameobject.target.Target;
import fr.cubiccl.generator.gui.component.button.CGCheckBox;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelTarget;
import fr.cubiccl.generator.gui.component.textfield.CGEntry;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;
import fr.cubiccl.generator.utils.Utils;

public class CommandXp extends Command
{
	private CGCheckBox checkboxLevel;
	private CGEntry entryAmount;
	private PanelTarget panelTarget;

	public CommandXp()
	{
		super("xp", "xp <level>[L] <player>", 3);
	}

	@Override
	public CGPanel createUI()
	{
		CGPanel panel = new CGPanel();
		GridBagConstraints gbc = panel.createGridBagLayout();

		panel.add(this.labelDescription(), gbc);
		++gbc.gridy;
		panel.add((this.entryAmount = new CGEntry(new Text("xp.amount"), "0", Text.INTEGER)).container, gbc);
		++gbc.gridy;
		panel.add(this.checkboxLevel = new CGCheckBox("xp.level"), gbc);
		++gbc.gridy;
		panel.add(this.panelTarget = new PanelTarget(PanelTarget.PLAYERS_ONLY), gbc);

		this.entryAmount.addIntFilter();
		this.entryAmount.addKeyListener(new KeyListener()
		{
			@Override
			public void keyPressed(KeyEvent e)
			{}

			@Override
			public void keyReleased(KeyEvent e)
			{
				updateTranslations();
			}

			@Override
			public void keyTyped(KeyEvent e)
			{}
		});
		this.checkboxLevel.setSelected(true);
		this.checkboxLevel.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				updateTranslations();
			}
		});

		this.panelTarget.addArgumentChangeListener(this);

		return panel;
	}

	@Override
	protected Text description()
	{
		String value = this.entryAmount.getText();
		try
		{
			Utils.checkInteger(null, value);
		} catch (CommandGenerationException e)
		{
			value = "???";
		}
		if (this.isLevels()) return new Text("command." + this.id + ".levels").addReplacement("<target>", this.panelTarget.displayTarget()).addReplacement(
				"<value>", value);
		return this.defaultDescription().addReplacement("<target>", this.panelTarget.displayTarget()).addReplacement("<value>", value);
	}

	@Override
	public String generate() throws CommandGenerationException
	{
		this.entryAmount.checkValue(CGEntry.INTEGER);
		if (!this.isLevels()) this.entryAmount.checkValueSuperior(CGEntry.INTEGER, 0);
		return this.id + " " + this.entryAmount.getText() + (this.isLevels() ? "L " : " ") + this.panelTarget.generate().toCommand();
	}

	private boolean isLevels()
	{
		return this.checkboxLevel.isSelected();
	}

	@Override
	protected void readArgument(int index, String argument, String[] fullCommand) throws CommandGenerationException
	{
		if (index == 1)
		{
			this.checkboxLevel.setSelected(argument.endsWith("L"));
			String lvl = argument.endsWith("L") ? argument.substring(0, argument.length() - 1) : argument;
			try
			{
				Integer.parseInt(lvl);
			} catch (NumberFormatException e)
			{
				this.incorrectStructureError();
			}
			this.entryAmount.setText(lvl);
		} else if (index == 2) this.panelTarget.setupFrom(Target.createFrom(argument));
	}

}
