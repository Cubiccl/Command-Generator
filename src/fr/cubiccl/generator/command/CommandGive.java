package fr.cubiccl.generator.command;

import java.awt.GridBagConstraints;

import fr.cubiccl.generator.gameobject.ItemStack;
import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.gameobject.tags.NBTParser;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.target.Target;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelItem;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelTarget;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;

public class CommandGive extends Command
{
	private PanelItem panelItem;
	private PanelTarget panelTarget;

	public CommandGive()
	{
		super("give", "give <player> <item> [amount] [data] [dataTag]", 3, 4, 5, 6);
	}

	@Override
	public CGPanel createUI()
	{
		CGPanel panel = new CGPanel();
		GridBagConstraints gbc = panel.createGridBagLayout();

		panel.add(this.labelDescription(), gbc);
		++gbc.gridy;
		panel.add(this.panelTarget = new PanelTarget(PanelTarget.PLAYERS_ONLY), gbc);
		++gbc.gridy;
		panel.add(this.panelItem = new PanelItem("give.item"), gbc);

		this.panelTarget.addArgumentChangeListener(this);
		this.panelItem.addArgumentChangeListener(this);

		return panel;
	}

	@Override
	protected void resetUI()
	{
		this.panelItem.setCount(1);
		this.panelItem.setDamage(0);
		this.panelItem.setTags(new Tag[0]);
	}

	@Override
	protected Text description()
	{
		return this.defaultDescription().addReplacement("<target>", this.panelTarget.displayTarget())
				.addReplacement("<item>", this.panelItem.selectedItem().name(this.panelItem.selectedDamage()))
				.addReplacement("<quantity>", Integer.toString(this.panelItem.selectedAmount()));
	}

	@Override
	public String generate() throws CommandGenerationException
	{
		ItemStack item = this.panelItem.generate();
		return this.id + " " + this.panelTarget.generate().toCommand() + " " + item.getItem().id() + " " + item.amount + " " + item.getDamage() + " "
				+ item.getNbt().valueForCommand();
	}

	@Override
	protected void readArgument(int index, String argument, String[] fullCommand) throws CommandGenerationException
	{
		// give <player> <item> [amount] [data] [dataTag]
		if (index == 1) this.panelTarget.setupFrom(Target.createFrom(argument));
		if (index == 2) this.panelItem.setItem(ObjectRegistry.items.find(argument));
		if (index == 3) try
		{
			this.panelItem.setCount(Integer.parseInt(argument));
		} catch (Exception e)
		{}
		if (index == 4) try
		{
			this.panelItem.setDamage(Integer.parseInt(argument));
		} catch (Exception e)
		{}
		if (index == 5) this.panelItem.setTags(((TagCompound) NBTParser.parse(argument, true, false)).value());
	}

}
