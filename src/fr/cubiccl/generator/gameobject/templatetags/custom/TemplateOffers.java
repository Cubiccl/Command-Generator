package fr.cubiccl.generator.gameobject.templatetags.custom;

import fr.cubiccl.generator.CommandGenerator;
import fr.cubiccl.generator.gameobject.TradeOffer;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.tags.TagList;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.tag.PanelTradeOffers;
import fr.cubiccl.generator.utils.CommandGenerationException;

public class TemplateOffers extends TemplateCompound
{

	public TemplateOffers(String id, byte tagType, String[] applicable)
	{
		super(id, tagType, applicable);
	}

	@Override
	protected CGPanel createPanel(String objectId, Tag previousValue)
	{
		PanelTradeOffers p = new PanelTradeOffers();

		if (previousValue != null)
		{
			TagList previous = (TagList) ((TagCompound) previousValue).getTagFromId(Tags.OFFER_RECIPES.id);
			TradeOffer[] trades = new TradeOffer[previous.size()];
			for (int i = 0; i < trades.length; i++)
				trades[i] = TradeOffer.createFrom((TagCompound) previous.getTag(i));
			p.setupFrom(trades);
		}

		p.setName(this.title());
		return p;
	}

	@Override
	public TagList generateTag(CGPanel panel)
	{
		TradeOffer[] trades = new TradeOffer[0];
		try
		{
			trades = ((PanelTradeOffers) panel).generateOffers();
		} catch (CommandGenerationException e)
		{
			e.printStackTrace();
		}
		TagCompound[] tags = new TagCompound[trades.length];
		for (int i = 0; i < tags.length; i++)
			tags[i] = trades[i].toTag(Tags.DEFAULT_COMPOUND);
		return new TagCompound(this, new TagList(Tags.OFFER_RECIPES, tags));
	}

	@Override
	protected boolean isInputValid(CGPanel panel)
	{
		try
		{
			((PanelTradeOffers) panel).generateOffers();
			return true;
		} catch (CommandGenerationException e)
		{
			CommandGenerator.report(e);
			return false;
		}
	}
}
