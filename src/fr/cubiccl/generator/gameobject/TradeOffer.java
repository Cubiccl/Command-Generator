package fr.cubiccl.generator.gameobject;

import java.awt.Component;
import java.util.ArrayList;

import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.tags.TagNumber;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;
import fr.cubiccl.generator.gui.component.interfaces.IObjectList;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelTrade;
import fr.cubiccl.generator.gui.component.panel.utils.ListProperties;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;

public class TradeOffer extends GameObject implements IObjectList<TradeOffer>
{

	public static TradeOffer createFrom(TagCompound tag)
	{
		TradeOffer trade = new TradeOffer();

		for (Tag t : tag.value())
		{
			if (t.id().equals(Tags.OFFER_EXP.id())) trade.experienceReward = ((TagNumber) t).value() == 1;
			else if (t.id().equals(Tags.OFFER_MAX_USES.id())) trade.maxUses = ((TagNumber) t).value();
			else if (t.id().equals(Tags.OFFER_USES.id())) trade.uses = ((TagNumber) t).value();
			else if (t.id().equals(Tags.OFFER_BUY.id())) trade.buy = ItemStack.createFrom((TagCompound) t);
			else if (t.id().equals(Tags.OFFER_BUYB.id())) trade.buySecondary = ItemStack.createFrom((TagCompound) t);
			else if (t.id().equals(Tags.OFFER_SELL.id())) trade.sell = ItemStack.createFrom((TagCompound) t);
		}

		trade.findName(tag);
		return trade;
	}

	public ItemStack buy = null, buySecondary = null, sell = null;
	public boolean experienceReward = false;
	public int maxUses = 0, uses = 0;

	public TradeOffer()
	{}

	@Override
	public CGPanel createPanel(ListProperties properties)
	{
		PanelTrade p = new PanelTrade(properties.hasCustomObjects());
		p.setupFrom(this);
		return p;
	}

	@Override
	public Component getDisplayComponent()
	{
		return new CGLabel(new Text(this.toString(), false));
	}

	@Override
	public String getName(int index)
	{
		return this.customName() == null || this.customName().equals("") ? "Trade " + (index + 1) : this.customName();
	}

	public boolean isValid()
	{
		return this.buy != null && this.sell != null;
	}

	@Override
	public TradeOffer setupFrom(CGPanel panel) throws CommandGenerationException
	{
		return ((PanelTrade) panel).generate();
	}

	@Override
	public String toCommand()
	{
		return this.toString();
	}

	@Override
	public String toString()
	{
		return this.buy.toString() + (this.buySecondary != null ? " + " + this.buySecondary.toString() : "") + " -> " + this.sell.toString();
	}

	@Override
	public TagCompound toTag(TemplateCompound container, boolean includeName)
	{
		ArrayList<Tag> tags = new ArrayList<Tag>();
		tags.add(Tags.OFFER_EXP.create(this.experienceReward ? 1 : 0));
		tags.add(Tags.OFFER_MAX_USES.create(this.maxUses));
		tags.add(Tags.OFFER_USES.create(this.uses));
		tags.add(this.buy.toTag(Tags.OFFER_BUY));
		if (this.buySecondary != null) tags.add(this.buySecondary.toTag(Tags.OFFER_BUYB));
		tags.add(this.sell.toTag(Tags.OFFER_SELL));
		if (includeName) tags.add(this.nameTag());
		return container.create(tags.toArray(new Tag[tags.size()]));
	}

}
