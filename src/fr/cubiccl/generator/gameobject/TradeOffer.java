package fr.cubiccl.generator.gameobject;

import java.awt.Component;
import java.util.ArrayList;

import org.jdom2.Element;

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

/** Represents a Villager's Trade Offer. */
public class TradeOffer extends GameObject<TradeOffer> implements IObjectList<TradeOffer>
{

	/** The bought Item. */
	private ItemStack buy = null;
	/** The secondary bought Item. Can be <code>null</code> if only one Item is bought. */
	private ItemStack buySecondary = null;
	/** <code>true</code> if this Trade Offer rewards the Player with experience when completed. */
	public boolean experienceReward = false;
	/** The maximum number of uses this Trade Offer is authorized for. */
	public int maxUses = 0;
	/** The sold Item. */
	private ItemStack sell = null;
	/** The current number of uses. */
	public int uses = 0;

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
	public TradeOffer fromNBT(TagCompound nbt)
	{
		for (Tag t : nbt.value())
		{
			if (t.id().equals(Tags.OFFER_EXP.id())) this.experienceReward = ((TagNumber) t).value() == 1;
			else if (t.id().equals(Tags.OFFER_MAX_USES.id())) this.maxUses = ((TagNumber) t).valueInt();
			else if (t.id().equals(Tags.OFFER_USES.id())) this.uses = ((TagNumber) t).valueInt();
			else if (t.id().equals(Tags.OFFER_BUY.id())) this.buy = new ItemStack().fromNBT((TagCompound) t);
			else if (t.id().equals(Tags.OFFER_BUYB.id())) this.buySecondary = new ItemStack().fromNBT((TagCompound) t);
			else if (t.id().equals(Tags.OFFER_SELL.id())) this.sell = new ItemStack().fromNBT((TagCompound) t);
		}

		this.findName(nbt);
		return this;
	}

	@Override
	public TradeOffer fromXML(Element xml)
	{
		this.buy = new ItemStack().fromXML(xml.getChild("buy"));
		if (xml.getChild("buy2") != null) this.buySecondary = new ItemStack().fromXML(xml.getChild("buy2"));
		this.sell = new ItemStack().fromXML(xml.getChild("sell"));
		this.experienceReward = Boolean.parseBoolean(xml.getChildText("exp"));
		this.uses = Integer.parseInt(xml.getChildText("uses"));
		this.maxUses = Integer.parseInt(xml.getChildText("usesmax"));
		this.findProperties(xml);
		return this;
	}

	/** Getter for {@link TradeOffer#buy}. */
	public ItemStack getBuy()
	{
		return buy;
	}

	/** Getter for {@link TradeOffer#buySecondary}. */
	public ItemStack getBuySecondary()
	{
		return buySecondary;
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

	/** Getter for {@link TradeOffer#sell}. */
	public ItemStack getSell()
	{
		return sell;
	}

	/** @return <code>true</code> if this Trade Offer is valid, i.e. if the {@link TradeOffer#buy bought Item} and the {@link TradeOffer#sell sold Item} are not null. */
	public boolean isValid()
	{
		return this.buy != null && this.sell != null;
	}

	/** Setter for {@link TradeOffer#buy}. */
	public void setBuy(ItemStack buy)
	{
		this.buy = buy;
		this.onChange();
	}

	/** Setter for {@link TradeOffer#buySecondary}. */
	public void setBuySecondary(ItemStack buySecondary)
	{
		this.buySecondary = buySecondary;
		this.onChange();
	}

	/** Setter for {@link TradeOffer#sell}. */
	public void setSell(ItemStack sell)
	{
		this.sell = sell;
		this.onChange();
	}

	@Override
	public String toCommand()
	{
		return this.toString();
	}

	@Override
	public TagCompound toNBT(TemplateCompound container)
	{
		ArrayList<Tag> tags = new ArrayList<Tag>();
		tags.add(Tags.OFFER_EXP.create(this.experienceReward ? 1 : 0));
		tags.add(Tags.OFFER_MAX_USES.create(this.maxUses));
		tags.add(Tags.OFFER_USES.create(this.uses));
		tags.add(this.buy.toNBT(Tags.OFFER_BUY));
		if (this.buySecondary != null) tags.add(this.buySecondary.toNBT(Tags.OFFER_BUYB));
		tags.add(this.sell.toNBT(Tags.OFFER_SELL));
		return container.create(tags.toArray(new Tag[tags.size()]));
	}

	@Override
	public String toString()
	{
		return this.buy.toString() + (this.buySecondary != null ? " + " + this.buySecondary.toString() : "") + " -> " + this.sell.toString();
	}

	@Override
	public Element toXML()
	{
		Element root = this.createRoot("trade");
		root.addContent(this.buy.toXML());
		root.getChild("item").setName("buy");
		if (this.buySecondary != null)
		{
			root.addContent(this.buySecondary.toXML());
			root.getChild("item").setName("buy2");
		}
		root.addContent(this.sell.toXML());
		root.getChild("item").setName("sell");
		root.addContent(new Element("exp").setText(Boolean.toString(this.experienceReward)));
		root.addContent(new Element("uses").setText(Integer.toString(this.uses)));
		root.addContent(new Element("usesmax").setText(Integer.toString(this.maxUses)));
		return root;
	}

	@Override
	public TradeOffer update(CGPanel panel) throws CommandGenerationException
	{
		TradeOffer t = ((PanelTrade) panel).generate();
		this.buy = t.buy;
		this.buySecondary = t.buySecondary;
		this.sell = t.sell;
		this.experienceReward = t.experienceReward;
		this.maxUses = t.maxUses;
		this.uses = t.uses;
		return this;
	}

}
