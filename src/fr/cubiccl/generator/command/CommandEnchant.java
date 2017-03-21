package fr.cubiccl.generator.command;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.gameobject.target.Target;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelEnchantment;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelTarget;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;

public class CommandEnchant extends Command
{
	private PanelEnchantment panelEnchant;
	private PanelTarget panelTarget;

	public CommandEnchant()
	{
		super("enchant", "enchant <player> <enchantment> <level>", 4);
	}

	@Override
	public CGPanel createGUI()
	{
		CGPanel panel = new CGPanel();
		GridBagConstraints gbc = panel.createGridBagLayout();

		panel.add(this.labelDescription(), gbc);
		++gbc.gridy;
		panel.add(this.panelTarget = new PanelTarget(PanelTarget.PLAYERS_ONLY), gbc);
		++gbc.gridy;
		panel.add(this.panelEnchant = new PanelEnchantment(true), gbc);

		this.panelEnchant.addActionListener(new ActionListener()
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
				.addReplacement("<enchantment>", this.panelEnchant.selectedEnchantment().name());
	}

	@Override
	public String generate() throws CommandGenerationException
	{
		return this.id + " " + this.panelTarget.generate().toCommand() + " " + this.panelEnchant.generate().toCommand();
	}

	@Override
	protected void readArgument(int index, String argument, String[] fullCommand) throws CommandGenerationException
	{
		// enchant <player> <enchantment> <level>

		if (index == 1) this.panelTarget.setupFrom(Target.createFrom(argument));
		if (index == 2) this.panelEnchant.setEnchantment(ObjectRegistry.enchantments.find(argument));
		if (index == 3) try
		{
			this.panelEnchant.setLevel(Integer.parseInt(argument));
		} catch (Exception e)
		{}
	}

}
