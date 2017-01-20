package fr.cubiccl.generator.gameobject;

import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.tags.TagNumber;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;

public class TradeOffer
{

	public static TradeOffer createFrom(TagCompound tag)
	{
		TradeOffer trade = new TradeOffer();

		for (Tag t : tag.value())
		{
			if (t.id().equals(Tags.OFFER_EXP.id)) trade.experienceReward = ((TagNumber) t).value() == 1;
			else if (t.id().equals(Tags.OFFER_MAX_USES.id)) trade.maxUses = ((TagNumber) t).value();
			else if (t.id().equals(Tags.OFFER_USES.id)) trade.uses = ((TagNumber) t).value();
			else if (t.id().equals(Tags.OFFER_BUY.id)) trade.buy = ItemStack.createFrom((TagCompound) t);
			else if (t.id().equals(Tags.OFFER_BUYB.id)) trade.buySecondary = ItemStack.createFrom((TagCompound) t);
			else if (t.id().equals(Tags.OFFER_SELL.id)) trade.sell = ItemStack.createFrom((TagCompound) t);
		}

		return trade;
	}

	public ItemStack buy = null, buySecondary = null, sell = null;
	public boolean experienceReward = false;
	public int maxUses = 0, uses = 0;

	public TradeOffer()
	{}

	public boolean isValid()
	{
		return this.buy != null && this.sell != null;
	}

	public TagCompound toTag(TemplateCompound container)
	{
		if (this.buySecondary == null) return new TagCompound(container, new TagNumber(Tags.OFFER_EXP, this.experienceReward ? 1 : 0), new TagNumber(
				Tags.OFFER_MAX_USES, this.maxUses), new TagNumber(Tags.OFFER_USES, this.uses), this.buy.toTag(Tags.OFFER_BUY), this.sell.toTag(Tags.OFFER_SELL));
		return new TagCompound(container, new TagNumber(Tags.OFFER_EXP, this.experienceReward ? 1 : 0), new TagNumber(Tags.OFFER_MAX_USES, this.maxUses),
				new TagNumber(Tags.OFFER_USES, this.uses), this.buy.toTag(Tags.OFFER_BUY), this.buySecondary.toTag(Tags.OFFER_BUYB),
				this.sell.toTag(Tags.OFFER_SELL));
	}

}
