package fr.cubiccl.generator.gui.component.panel.gameobject;

import java.awt.GridBagConstraints;

import fr.cubiccl.generator.gameobject.ItemStack;
import fr.cubiccl.generator.gameobject.TradeOffer;
import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.gameobject.registries.ObjectSaver;
import fr.cubiccl.generator.gui.component.button.CGCheckBox;
import fr.cubiccl.generator.gui.component.interfaces.ICustomObject;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.tag.PanelContainer;
import fr.cubiccl.generator.gui.component.panel.utils.PanelCustomObject;
import fr.cubiccl.generator.gui.component.textfield.CGEntry;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;

public class PanelTrade extends CGPanel implements ICustomObject<TradeOffer>
{
	private static final long serialVersionUID = -7818304541070644582L;

	private CGCheckBox checkboxReward;
	private CGEntry entryUses, entryMaxUses;
	private PanelContainer panelItems;
	private CGPanel panelOptions;

	public PanelTrade()
	{
		this(true);
	}

	public PanelTrade(boolean customObjects)
	{
		this.panelOptions = new CGPanel("trade.properties");
		GridBagConstraints gbc = this.panelOptions.createGridBagLayout();
		this.panelOptions.add(this.checkboxReward = new CGCheckBox("trade.reward"), gbc);
		++gbc.gridx;
		this.panelOptions.add((this.entryUses = new CGEntry(new Text("trade.uses"), "0", Text.INTEGER)).container, gbc);
		++gbc.gridx;
		this.panelOptions.add((this.entryMaxUses = new CGEntry(new Text("trade.uses_max"), "0", Text.INTEGER)).container, gbc);

		gbc = this.createGridBagLayout();
		gbc.fill = GridBagConstraints.NONE;
		this.add((this.panelItems = new PanelContainer(ObjectRegistry.containers.find("trade"))).container, gbc);
		++gbc.gridy;
		this.add(this.panelOptions, gbc);
		++gbc.gridy;
		if (customObjects) this.add(new PanelCustomObject<TradeOffer, TradeOffer>(this, ObjectSaver.trades), gbc);

		this.entryMaxUses.addIntFilter();
		this.entryUses.addIntFilter();
	}

	@Override
	public TradeOffer generate() throws CommandGenerationException
	{
		this.entryMaxUses.checkValue(CGEntry.INTEGER);
		this.entryUses.checkValue(CGEntry.INTEGER);
		ItemStack[] items = this.panelItems.generateItems(true);

		TradeOffer trade = new TradeOffer();
		trade.experienceReward = this.checkboxReward.isSelected();
		trade.maxUses = Integer.parseInt(this.entryMaxUses.getText());
		trade.uses = Integer.parseInt(this.entryUses.getText());
		for (ItemStack item : items)
		{
			if (item.slot == 0)
			{
				trade.buy = item;
				trade.buy.slot = -1;
			} else if (item.slot == 1)
			{
				trade.sell = item;
				trade.sell.slot = -1;
			} else if (item.slot == 2)
			{
				trade.buySecondary = item;
				trade.buySecondary.slot = -1;
			}
		}

		if (trade.buy == null && trade.buySecondary != null)
		{
			trade.buy = trade.buySecondary;
			trade.buySecondary = null;
		}

		if (!trade.isValid()) throw new CommandGenerationException(new Text("error.trade.incomplete"));

		return trade;
	}

	@Override
	public void setupFrom(TradeOffer trade)
	{
		this.checkboxReward.setSelected(trade.experienceReward);
		this.entryMaxUses.setText(Integer.toString(trade.maxUses));
		this.entryUses.setText(Integer.toString(trade.uses));

		ItemStack[] items = new ItemStack[]
		{ trade.buy, trade.sell, trade.buySecondary };
		for (int i = 0; i < items.length; i++)
			if (items[i] != null) items[i].slot = i;
		this.panelItems.empty();
		this.panelItems.setupFrom(items);
	}

}
