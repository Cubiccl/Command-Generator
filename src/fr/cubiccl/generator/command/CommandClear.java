package fr.cubiccl.generator.command;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import fr.cubiccl.generator.gameobject.ItemStack;
import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.gameobject.tags.NBTParser;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.target.Target;
import fr.cubiccl.generator.gui.component.button.CGCheckBox;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelItem;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelTarget;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Replacement;
import fr.cubiccl.generator.utils.Text;

public class CommandClear extends Command implements ActionListener
{
	private CGCheckBox checkboxAllItem, checkboxIgnoreData, checkboxAll;
	private PanelItem panelItem;
	private PanelTarget panelTarget;

	public CommandClear()
	{
		super("clear", "clear <player> [item] [data] [maxCount] [dataTag]", 2, 3, 4, 5, 6);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == this.checkboxAllItem || e.getSource() == this.checkboxIgnoreData || e.getSource() == this.checkboxAll) this.onParsingEnd();
		this.updateTranslations();
	}

	private boolean clearAll()
	{
		return this.checkboxAll.isSelected();
	}

	private boolean clearAllItems()
	{
		return this.checkboxAllItem.isSelected();
	}

	@Override
	public CGPanel createUI()
	{
		CGPanel panel = new CGPanel();
		GridBagConstraints gbc = panel.createGridBagLayout();

		panel.add(this.labelDescription(), gbc);
		++gbc.gridy;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		panel.add(this.panelTarget = new PanelTarget(PanelTarget.PLAYERS_ONLY), gbc);
		++gbc.gridy;
		panel.add(this.checkboxAllItem = new CGCheckBox("clear.all_items"), gbc);
		++gbc.gridy;
		panel.add(this.checkboxIgnoreData = new CGCheckBox("clear.ignore_data"), gbc);
		++gbc.gridy;
		panel.add(this.checkboxAll = new CGCheckBox("clear.no_limit"), gbc);
		++gbc.gridy;
		panel.add(this.panelItem = new PanelItem("clear.item"), gbc);

		this.checkboxAllItem.addActionListener(this);
		this.checkboxIgnoreData.addActionListener(this);
		this.checkboxAll.addActionListener(this);

		this.panelTarget.addArgumentChangeListener(this);
		this.panelItem.addArgumentChangeListener(this);
		this.checkboxAll.addActionListener(this);
		this.checkboxAllItem.addActionListener(this);
		this.checkboxIgnoreData.addActionListener(this);

		return panel;
	}

	@Override
	protected void resetUI()
	{
		this.checkboxAllItem.setSelected(true);
		this.checkboxIgnoreData.setSelected(true);
		this.checkboxAll.setSelected(true);
		this.panelItem.setTags(new Tag[0]);
		this.panelItem.setVisible(false);
	}

	@Override
	protected Text description()
	{
		String target = this.panelTarget.displayTarget();

		if (this.clearAllItems()) return new Text("command." + this.id + ".all", new Replacement("<target>", target));
		Text d = this.defaultDescription();
		if (!this.clearAll())
		{
			d = new Text("command." + this.id + ".quantity");
			d.addReplacement("<quantity>", Integer.toString(this.panelItem.selectedAmount()));
		}
		if (this.ignoreData()) d.addReplacement("<item>", this.panelItem.selectedItem().name());
		else d.addReplacement("<item>", this.panelItem.selectedItem().name(this.panelItem.selectedDamage()));
		d.addReplacement("<target>", target);
		return d;
	}

	@Override
	protected void onParsingEnd()
	{
		boolean item = !this.clearAllItems();
		this.panelItem.setVisible(item);
		this.checkboxIgnoreData.setVisible(item);
		this.checkboxAll.setVisible(item);
		this.panelItem.setEnabledContent(!this.checkboxIgnoreData.isSelected(), !this.checkboxAll.isSelected());
	}

	@Override
	public String generate() throws CommandGenerationException
	{
		ItemStack item = this.panelItem.generate();
		if (this.clearAllItems()) return "/clear " + this.panelTarget.generate().toCommand();
		int data = item.getDamage(), amount = item.amount;
		if (this.ignoreData()) data = -1;
		if (this.clearAll()) amount = -1;
		return this.id + " " + this.panelTarget.generate().toCommand() + " " + item.getItem().id() + " " + data + " " + amount + " "
				+ item.getNbt().valueForCommand();
	}

	private boolean ignoreData()
	{
		return this.checkboxIgnoreData.isSelected();
	}

	@Override
	protected void readArgument(int index, String argument, String[] fullCommand) throws CommandGenerationException
	{
		// clear <player> [item] [data] [maxCount] [dataTag]
		if (index == 1) this.panelTarget.setupFrom(new Target().fromString(argument));
		else if (index == 2)
		{
			this.checkboxAllItem.setSelected(false);
			this.panelItem.setItem(ObjectRegistry.items.find(argument));
		} else if (index == 3)
		{
			this.checkboxIgnoreData.setSelected(argument.equals("-1"));
			if (!this.checkboxIgnoreData.isSelected()) try
			{
				this.panelItem.setDamage(Integer.parseInt(argument));
			} catch (NumberFormatException e)
			{}
		} else if (index == 4)
		{
			this.checkboxAll.setSelected(argument.equals("-1"));
			if (this.clearAll()) try
			{
				this.panelItem.setCount(Integer.parseInt(argument));
			} catch (NumberFormatException e)
			{}
		} else if (index == 5) this.panelItem.setTags(((TagCompound) NBTParser.parse(argument, true, false)).value());
	}

}
