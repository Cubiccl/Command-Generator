package fr.cubiccl.generator.command;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import fr.cubiccl.generator.gameobject.target.Target;
import fr.cubiccl.generator.gui.component.combobox.OptionCombobox;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelTarget;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;

public class CommandGamemode extends Command
{
	private OptionCombobox comboboxGamemode;
	private PanelTarget panelTarget;

	public CommandGamemode()
	{
		super("gamemode", "gamemode <mode> <player>", 3);
	}

	@Override
	public CGPanel createUI()
	{
		CGPanel panel = new CGPanel();
		GridBagConstraints gbc = panel.createGridBagLayout();

		++gbc.gridwidth;
		panel.add(this.labelDescription(), gbc);
		++gbc.gridy;
		--gbc.gridwidth;
		panel.add(new CGLabel("gamemode.select").setHasColumn(true), gbc);
		++gbc.gridx;
		panel.add(this.comboboxGamemode = new OptionCombobox("gamemode", "survival", "creative", "adventure", "spectator"), gbc);
		--gbc.gridx;
		++gbc.gridy;
		++gbc.gridwidth;
		panel.add(this.panelTarget = new PanelTarget("target.title.player", PanelTarget.PLAYERS_ONLY), gbc);

		this.comboboxGamemode.addActionListener(new ActionListener()
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
		return this.defaultDescription().addReplacement("<target>", this.panelTarget.displayTarget())
				.addReplacement("<mode>", new Text("gamemode." + this.comboboxGamemode.getValue()));
	}

	@Override
	public String generate() throws CommandGenerationException
	{
		return this.id + " " + this.comboboxGamemode.getValue() + " " + this.panelTarget.generate().toCommand();
	}

	@Override
	protected void readArgument(int index, String argument, String[] fullCommand) throws CommandGenerationException
	{
		if (index == 1)
		{
			for (int i = 0; i < CommandDefaultgamemode.acceptable.length; ++i)
				for (int j = 0; j < CommandDefaultgamemode.acceptable[i].length; ++j)
					if (CommandDefaultgamemode.acceptable[i][j].equals(argument))
					{
						this.comboboxGamemode.setSelectedIndex(i);
						return;
					}
		} else if (index == 2) this.panelTarget.setupFrom(Target.createFrom(argument));

	}

}
