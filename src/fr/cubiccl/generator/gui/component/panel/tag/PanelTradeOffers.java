package fr.cubiccl.generator.gui.component.panel.tag;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import fr.cubiccl.generator.CommandGenerator;
import fr.cubiccl.generator.gameobject.TradeOffer;
import fr.cubiccl.generator.gui.component.button.CGButton;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelTrade;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Replacement;
import fr.cubiccl.generator.utils.Text;

public class PanelTradeOffers extends CGPanel implements ActionListener
{
	private static final long serialVersionUID = 6861089234157024457L;

	private CGButton buttonAdd, buttonRemove, buttonPrevious, buttonNext;
	private int current;
	private CGLabel labelCurrent;
	private PanelTrade panelCurrent;
	private ArrayList<TradeOffer> trades = new ArrayList<TradeOffer>();

	public PanelTradeOffers()
	{
		GridBagConstraints gbc = this.createGridBagLayout();
		this.current = -1;
		CGPanel panelNavigation = new CGPanel("trade.navigation");

		panelNavigation.add(this.buttonPrevious = new CGButton("trade.previous"), gbc);
		panelNavigation.add(this.buttonRemove = new CGButton("trade.remove"), gbc);
		panelNavigation.add(this.buttonAdd = new CGButton("trade.add"), gbc);
		panelNavigation.add(this.buttonNext = new CGButton("trade.next"), gbc);

		gbc.fill = GridBagConstraints.NONE;
		this.add(this.labelCurrent = new CGLabel(new Text("trade.current", new Replacement("<id>", "1"))), gbc);
		++gbc.gridy;
		this.add(this.panelCurrent = new PanelTrade(), gbc);
		++gbc.gridy;
		this.add(panelNavigation, gbc);

		this.buttonAdd.addActionListener(this);
		this.buttonRemove.addActionListener(this);
		this.buttonPrevious.addActionListener(this);
		this.buttonNext.addActionListener(this);

		this.labelCurrent.setVisible(false);
		this.panelCurrent.setVisible(false);
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
		this.trades.set(this.current, this.panelCurrent.generateTrade());
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
		this.trades.clear();
		for (TradeOffer tradeOffer : trades)
			this.trades.add(tradeOffer);
		this.setCurrent(trades.length == 0 ? -1 : 0);
	}

	private void updateDisplay()
	{
		boolean shown = this.current != -1;
		this.labelCurrent.setVisible(shown);
		this.panelCurrent.setVisible(shown);
		this.buttonRemove.setEnabled(shown);
		this.buttonPrevious.setEnabled(this.current > 0);
		this.buttonNext.setEnabled(this.current < this.trades.size() - 1);

		if (!shown) return;
		TradeOffer trade = this.currentOffer();
		this.labelCurrent.setTextID(new Text("trade.current", new Replacement("<id>", Integer.toString(this.current + 1))));
		this.panelCurrent.setupFrom(trade);
	}

}
