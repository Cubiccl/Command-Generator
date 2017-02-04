package fr.cubiccl.generator.gameobject;

import java.util.ArrayList;

import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.tags.TagNumber;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;

public class TradeOffer extends GameObject
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

	public boolean isValid()
	{
		return this.buy != null && this.sell != null;
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
		tags.add(new TagNumber(Tags.OFFER_EXP, this.experienceReward ? 1 : 0));
		tags.add(new TagNumber(Tags.OFFER_MAX_USES, this.maxUses));
		tags.add(new TagNumber(Tags.OFFER_USES, this.uses));
		tags.add(this.buy.toTag(Tags.OFFER_BUY));
		if (this.buySecondary != null) tags.add(this.buySecondary.toTag(Tags.OFFER_BUYB));
		tags.add(this.sell.toTag(Tags.OFFER_SELL));
		if (includeName) tags.add(this.nameTag());
		return new TagCompound(container, tags.toArray(new Tag[tags.size()]));
	}

}
