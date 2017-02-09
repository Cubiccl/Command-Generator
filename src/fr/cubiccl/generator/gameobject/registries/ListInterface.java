package fr.cubiccl.generator.gameobject.registries;

import java.awt.Component;

import fr.cubiccl.generator.gameobject.*;
import fr.cubiccl.generator.gameobject.target.Target;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.label.ImageLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.*;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Replacement;
import fr.cubiccl.generator.utils.Text;

public abstract class ListInterface<T extends GameObject>
{
	public static class AttributeList extends ListInterface<AppliedAttribute>
	{
		@Override
		public CGPanel createEditionPanel(AppliedAttribute a)
		{
			PanelAttribute p = new PanelAttribute(false);
			if (a != null) p.setupFrom(a);
			p.setName(new Text("objects.title", new Replacement("<object>", new Text("objects.attribute"))));
			return p;
		}

		@Override
		public AppliedAttribute createObject(CGPanel panel) throws CommandGenerationException
		{
			return ((PanelAttribute) panel).generate();
		}

		@Override
		public Component getDisplayComponent(AppliedAttribute a)
		{
			return new CGLabel(new Text(a.toString(), false));
		}
	}

	public static class BlockList extends ListInterface<PlacedBlock>
	{
		@Override
		public CGPanel createEditionPanel(PlacedBlock b)
		{
			PanelBlock p = new PanelBlock(null, true, true, false);
			if (b != null) p.setupFrom(b);
			p.setName(new Text("objects.title", new Replacement("<object>", new Text("objects.block"))));
			return p;
		}

		@Override
		public PlacedBlock createObject(CGPanel panel) throws CommandGenerationException
		{
			return ((PanelBlock) panel).generate();
		}

		@Override
		public Component getDisplayComponent(PlacedBlock b)
		{
			CGPanel p = new CGPanel();
			p.add(new CGLabel(b.block.name(b.data)));
			p.add(new ImageLabel(b.block.texture(b.data)));
			return p;
		}
	}

	public static class CoordinatesList extends ListInterface<Coordinates>
	{
		@Override
		public CGPanel createEditionPanel(Coordinates c)
		{
			PanelCoordinates p = new PanelCoordinates(null, true, false);
			if (c != null) p.setupFrom(c);
			p.setName(new Text("objects.title", new Replacement("<object>", new Text("objects.coordinates"))));
			return p;
		}

		@Override
		public Coordinates createObject(CGPanel panel) throws CommandGenerationException
		{
			return ((PanelCoordinates) panel).generate();
		}

		@Override
		public Component getDisplayComponent(Coordinates c)
		{
			return new CGLabel(new Text(c.toString(), false));
		}
	}

	public static class EffectList extends ListInterface<Effect>
	{
		@Override
		public CGPanel createEditionPanel(Effect e)
		{
			PanelEffect p = new PanelEffect(false);
			if (e != null) p.setupFrom(e);
			p.setName(new Text("objects.title", new Replacement("<object>", new Text("objects.effect"))));
			return p;
		}

		@Override
		public Effect createObject(CGPanel panel) throws CommandGenerationException
		{
			return ((PanelEffect) panel).generate();
		}

		@Override
		public Component getDisplayComponent(Effect e)
		{
			CGPanel p = new CGPanel();
			p.add(new CGLabel(new Text(e.toString(), false)));
			p.add(new ImageLabel(e.type.texture()));
			return p;
		}
	}

	public static class EnchantmentList extends ListInterface<Enchantment>
	{
		@Override
		public CGPanel createEditionPanel(Enchantment e)
		{
			PanelEnchantment p = new PanelEnchantment(false, false);
			if (e != null) p.setupFrom(e);
			p.setName(new Text("objects.title", new Replacement("<object>", new Text("objects.enchantment"))));
			return p;
		}

		@Override
		public Enchantment createObject(CGPanel panel) throws CommandGenerationException
		{
			return ((PanelEnchantment) panel).generate();
		}

		@Override
		public Component getDisplayComponent(Enchantment e)
		{
			return new CGLabel(new Text(e.toString(), false));
		}
	}

	public static class EntityList extends ListInterface<LivingEntity>
	{
		@Override
		public CGPanel createEditionPanel(LivingEntity e)
		{
			PanelEntity p = new PanelEntity(null, true, false, true);
			if (e != null) p.setupFrom(e);
			p.setName(new Text("objects.entity"));
			return p;
		}

		@Override
		public LivingEntity createObject(CGPanel panel) throws CommandGenerationException
		{
			return ((PanelEntity) panel).generate();
		}

		@Override
		public Component getDisplayComponent(LivingEntity e)
		{
			CGPanel p = new CGPanel();
			p.add(new CGLabel(e.entity.name()));
			p.add(new ImageLabel(e.entity.texture()));
			return p;
		}
	}

	public static class ItemList extends ListInterface<ItemStack>
	{
		@Override
		public CGPanel createEditionPanel(ItemStack i)
		{
			PanelItem p = new PanelItem(null, true, true, false, ObjectRegistry.items.list());
			if (i != null) p.setupFrom(i);
			p.setName(new Text("objects.title", new Replacement("<object>", new Text("objects.item"))));
			return p;
		}

		@Override
		public ItemStack createObject(CGPanel panel) throws CommandGenerationException
		{
			return ((PanelItem) panel).generate();
		}

		@Override
		public Component getDisplayComponent(ItemStack i)
		{
			CGPanel p = new CGPanel();
			p.add(new CGLabel(new Text(i.toString(), false)));
			p.add(new ImageLabel(i.item.texture(i.damage)));
			return p;
		}
	}

	public static class JsonList extends ListInterface<JsonMessage>
	{
		@Override
		public CGPanel createEditionPanel(JsonMessage m)
		{
			PanelJsonMessage p = new PanelJsonMessage(true, false);
			if (m != null) p.setupFrom(m);
			p.setName(new Text("objects.title", new Replacement("<object>", new Text("objects.json"))));
			return p;
		}

		@Override
		public JsonMessage createObject(CGPanel panel) throws CommandGenerationException
		{
			return ((PanelJsonMessage) panel).generate();
		}

		@Override
		public Component getDisplayComponent(JsonMessage m)
		{
			return new CGLabel(new Text(m.text, false));
		}
	}

	public static class ModifierList extends ListInterface<AttributeModifier>
	{
		@Override
		public CGPanel createEditionPanel(AttributeModifier m)
		{
			PanelAttributeModifier p = new PanelAttributeModifier(false, false);
			if (m != null) p.setupFrom(m);
			p.setName(new Text("objects.title", new Replacement("<object>", new Text("objects.modifier"))));
			return p;
		}

		@Override
		public AttributeModifier createObject(CGPanel panel) throws CommandGenerationException
		{
			return ((PanelAttributeModifier) panel).generate();
		}

		@Override
		public Component getDisplayComponent(AttributeModifier m)
		{
			return new CGLabel(new Text(m.toString(), false));
		}
	}

	public static class TargetList extends ListInterface<Target>
	{
		@Override
		public CGPanel createEditionPanel(Target t)
		{
			PanelTarget p = new PanelTarget(null, PanelTarget.ALL_ENTITIES, false);
			if (t != null) p.setupFrom(t);
			p.setName(new Text("objects.title", new Replacement("<object>", new Text("objects.target"))));
			return p;
		}

		@Override
		public Target createObject(CGPanel panel) throws CommandGenerationException
		{
			return ((PanelTarget) panel).generate();
		}

		@Override
		public Component getDisplayComponent(Target t)
		{
			return new CGLabel(new Text(t.toString(), false));
		}
	}

	public static class TradeList extends ListInterface<TradeOffer>
	{
		@Override
		public CGPanel createEditionPanel(TradeOffer t)
		{
			PanelTrade p = new PanelTrade(false);
			if (t != null) p.setupFrom(t);
			p.setName(new Text("objects.title", new Replacement("<object>", new Text("objects.trade"))));
			return p;
		}

		@Override
		public TradeOffer createObject(CGPanel panel) throws CommandGenerationException
		{
			return ((PanelTrade) panel).generate();
		}

		@Override
		public Component getDisplayComponent(TradeOffer t)
		{
			return new CGLabel(new Text(t.toString(), false));
		}
	}

	public abstract CGPanel createEditionPanel(T object);

	public abstract T createObject(CGPanel panel) throws CommandGenerationException;

	public abstract Component getDisplayComponent(T object);

}
