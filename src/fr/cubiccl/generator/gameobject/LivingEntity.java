package fr.cubiccl.generator.gameobject;

import java.awt.Component;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import org.jdom2.Element;

import fr.cubiccl.generator.gameobject.baseobjects.Entity;
import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.gameobject.tags.NBTParser;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.tags.TagString;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;
import fr.cubiccl.generator.gui.component.interfaces.IObjectList;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.label.ImageLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelEntity;
import fr.cubiccl.generator.gui.component.panel.utils.ListProperties;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;

/** Represents an Entity in the world. */
public class LivingEntity extends GameObject<LivingEntity> implements IObjectList<LivingEntity>
{

	/** The {@link Entity} type. */
	private Entity entity;
	/** The NBT Tags of this Entity. */
	private TagCompound nbt;

	public LivingEntity()
	{
		this(ObjectRegistry.entities.find("area_effect_cloud"), Tags.DEFAULT_COMPOUND.create());
	}

	public LivingEntity(Entity entity, TagCompound nbt)
	{
		this.entity = entity;
		this.nbt = nbt;
	}

	@Override
	public CGPanel createPanel(ListProperties properties)
	{
		PanelEntity p = new PanelEntity(null, true, properties.hasCustomObjects(), false);
		p.setEntity(this.entity);
		p.setTags(this.nbt.value());
		return p;
	}

	@Override
	public LivingEntity duplicate(LivingEntity object)
	{
		this.entity = object.entity;
		this.nbt = object.nbt.duplicate();
		return this;
	}

	@Override
	public LivingEntity fromNBT(TagCompound nbt)
	{
		if (nbt.hasTag("id")) this.entity = ObjectRegistry.entities.find(((TagString) nbt.getTagFromId("id")).value());

		ArrayList<Tag> tags = new ArrayList<Tag>();
		for (Tag t : nbt.value())
			if (!t.id().equals(Tags.ENTITY_ID) && !t.id().equals(Tags.OBJECT_NAME)) tags.add(t);
		this.nbt = ((TemplateCompound) nbt.template).create(tags.toArray(new Tag[tags.size()]));

		this.findName(nbt);
		return this;
	}

	@Override
	public LivingEntity fromXML(Element xml)
	{
		this.entity = ObjectRegistry.entities.find(xml.getChildText("id"));
		this.nbt = (TagCompound) NBTParser.parse(xml.getChildText("nbt"), true, false, true);
		this.findProperties(xml);
		return this;
	}

	@Override
	public Component getDisplayComponent()
	{
		CGPanel p = new CGPanel();
		p.add(new CGLabel(this.entity.name()));
		p.add(new ImageLabel(this.entity.texture()));
		return p;
	}

	/** Getter for {@link LivingEntity#entity}. */
	public Entity getEntity()
	{
		return entity;
	}

	@Override
	public String getName(int index)
	{
		return this.customName() != null && !this.customName().equals("") ? this.customName() : this.entity.name().toString();
	}

	/** Getter for {@link LivingEntity#nbt}. */
	public TagCompound getNbt()
	{
		return nbt;
	}

	/** @return This Entity's name. */
	public Text name()
	{
		return this.entity.name();
	}

	/** Setter for {@link LivingEntity#entity}. */
	public void setEntity(Entity entity)
	{
		this.entity = entity;
		this.onChange();
	}

	/** Setter for {@link LivingEntity#nbt}. */
	public void setNbt(TagCompound nbt)
	{
		this.nbt = nbt;
		this.onChange();
	}

	/** @return This Entity's texture. */
	public BufferedImage texture()
	{
		return this.entity.texture();
	}

	@Override
	public String toCommand()
	{
		return this.entity.id();
	}

	@Override
	public TagCompound toNBT(TemplateCompound container)
	{
		ArrayList<Tag> tags = new ArrayList<Tag>();
		tags.add(Tags.ENTITY_ID.create(this.entity.id()));
		for (Tag t : this.nbt.value())
			tags.add(t);
		return container.create(tags.toArray(new Tag[tags.size()]));
	}

	@Override
	public String toString()
	{
		return this.entity.name().toString();
	}

	@Override
	public Element toXML()
	{
		Element root = this.createRoot("entity");
		root.addContent(new Element("id").setText(this.entity.id()));
		root.addContent(new Element("nbt").setText(this.nbt.valueForCommand()));
		return root;
	}

	@Override
	public LivingEntity update(CGPanel panel) throws CommandGenerationException
	{
		LivingEntity e = ((PanelEntity) panel).generate();
		this.entity = e.entity;
		this.nbt = e.nbt;
		this.onChange();
		return this;
	}

}
