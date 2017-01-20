package fr.cubiccl.generator.gui.component.panel.tag;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import fr.cubiccl.generator.CommandGenerator;
import fr.cubiccl.generator.gameobject.ItemStack;
import fr.cubiccl.generator.gameobject.TradeOffer;
import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.gui.component.button.CGButton;
import fr.cubiccl.generator.gui.component.button.CGCheckBox;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.textfield.CGEntry;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Replacement;
import fr.cubiccl.generator.utils.Text;

public class PanelTradeOffers extends CGPanel implements ActionListener
{
	private static final long serialVersionUID = 6861089234157024457L;

	private CGButton buttonAdd, buttonRemove, buttonPrevious, buttonNext;
	private CGCheckBox checkboxReward;
	private int current;
	private CGEntry entryUses, entryMaxUses;
	private CGLabel labelCurrent;
	private ContainerPanel panelCurrent;
	private CGPanel panelOptions;
	private ArrayList<TradeOffer> trades = new ArrayList<TradeOffer>();

	public PanelTradeOffers()
	{
		GridBagConstraints gbc = this.createGridBagLayout();
		this.current = -1;
		this.panelOptions = new CGPanel("trade.properties");
		CGPanel panelNavigation = new CGPanel("trade.navigation");

		this.panelOptions.add(this.checkboxReward = new CGCheckBox("trade.reward"), gbc);
		this.panelOptions.add((this.entryUses = new CGEntry(new Text("trade.uses"), "0", Text.INTEGER)).container, gbc);
		this.panelOptions.add((this.entryMaxUses = new CGEntry(new Text("trade.uses_max"), "0", Text.INTEGER)).container, gbc);

		panelNavigation.add(this.buttonPrevious = new CGButton("trade.previous"), gbc);
		panelNavigation.add(this.buttonRemove = new CGButton("trade.remove"), gbc);
		panelNavigation.add(this.buttonAdd = new CGButton("trade.add"), gbc);
		panelNavigation.add(this.buttonNext = new CGButton("trade.next"), gbc);

		gbc.fill = GridBagConstraints.NONE;
		this.add(this.labelCurrent = new CGLabel(new Text("trade.current", new Replacement("<id>", "1"))), gbc);
		++gbc.gridy;
		this.add(this.panelCurrent = new ContainerPanel(ObjectRegistry.containers.find("trade")), gbc);
		++gbc.gridy;
		this.add(this.panelOptions, gbc);
		++gbc.gridy;
		this.add(panelNavigation, gbc);

		this.entryMaxUses.addIntFilter();
		this.entryUses.addIntFilter();

		this.buttonAdd.addActionListener(this);
		this.buttonRemove.addActionListener(this);
		this.buttonPrevious.addActionListener(this);
		this.buttonNext.addActionListener(this);

		this.labelCurrent.setVisible(false);
		this.panelCurrent.setVisible(false);
		this.panelOptions.setVisible(false);
		this.buttonRemove.setEnabled(false);
		this.buttonPrevious.setEnabled(false);
		this.buttonNext.setEnabled(false);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() != this.buttonRemove) try
		{
			this.applyCurrent();
		} catch (CommandGenerationException e1)
		{
			CommandGenerator.report(e1);
			return;
		}
		if (e.getSource() == this.buttonAdd)
		{
			this.trades.add(new TradeOffer());
			this.setCurrent(this.trades.size() - 1);
		} else if (e.getSource() == this.buttonRemove)
		{
			this.trades.remove(this.current);
			this.setCurrent(this.current == this.trades.size() ? this.current - 1 : this.current);
		} else if (e.getSource() == this.buttonPrevious) this.setCurrent(this.current - 1);
		else if (e.getSource() == this.buttonNext) this.setCurrent(this.current + 1);
	}

	private void applyCurrent() throws CommandGenerationException
	{
		if (this.current == -1) return;
		this.entryMaxUses.checkValue(CGEntry.INTEGER);
		this.entryUses.checkValue(CGEntry.INTEGER);
		ItemStack[] items = this.panelCurrent.generateItems(true);

		TradeOffer current = this.currentOffer();
		current.experienceReward = this.checkboxReward.isSelected();
		current.maxUses = Integer.parseInt(this.entryMaxUses.getText());
		current.uses = Integer.parseInt(this.entryUses.getText());
		for (ItemStack item : items)
		{
			if (item.slot == 0)
			{
				current.buy = item;
				current.buy.slot = -1;
			} else if (item.slot == 1)
			{
				current.sell = item;
				current.sell.slot = -1;
			} else if (item.slot == 2)
			{
				current.buySecondary = item;
				current.buySecondary.slot = -1;
			}
		}

		if (current.buy == null && current.buySecondary != null)
		{
			current.buy = current.buySecondary;
			current.buySecondary = null;
		}

		if (!current.isValid()) throw new CommandGenerationException(new Text("error.trade.incomplete"));
	}

	private TradeOffer currentOffer()
	{
		return this.trades.get(this.current);
	}

	public TradeOffer[] generateOffers() throws CommandGenerationException
	{
		this.applyCurrent();
		return this.trades.toArray(new TradeOffer[this.trades.size()]);
	}

	private void setCurrent(int index)
	{
		if (index < -1 || index >= this.trades.size()) return;
		this.current = index;
		this.updateDisplay();
	}

	public void setupFrom(TradeOffer... trades)
	{
		for (TradeOffer tradeOffer : trades)
			this.trades.add(tradeOffer);
		if (trades.length != 0) this.setCurrent(0);
	}

	private void updateDisplay()
	{
		boolean shown = this.current != -1;
		this.labelCurrent.setVisible(shown);
		this.panelCurrent.setVisible(shown);
		this.panelOptions.setVisible(shown);
		this.buttonRemove.setEnabled(shown);
		this.buttonPrevious.setEnabled(this.current > 0);
		this.buttonNext.setEnabled(this.current < this.trades.size() - 1);

		if (!shown) return;
		TradeOffer trade = this.currentOffer();
		this.labelCurrent.setTextID(new Text("trade.current", new Replacement("<id>", Integer.toString(this.current + 1))));
		this.checkboxReward.setSelected(trade.experienceReward);
		this.entryMaxUses.setText(Integer.toString(trade.maxUses));
		this.entryUses.setText(Integer.toString(trade.uses));

		ItemStack[] items = new ItemStack[]
		{ trade.buy, trade.sell, trade.buySecondary };
		for (int i = 0; i < items.length; i++)
			if (items[i] != null) items[i].slot = i;
		this.panelCurrent.empty();
		this.panelCurrent.setupFrom(items);
	}

}
