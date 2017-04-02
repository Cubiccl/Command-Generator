package fr.cubiccl.generator.gameobject;

import java.awt.Component;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import org.jdom2.Element;

import fr.cubiccl.generator.gameobject.baseobjects.Entity;
import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.gameobject.tags.NBTReader;
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

public class LivingEntity extends GameObject implements IObjectList<LivingEntity>
{

	public static LivingEntity createFrom(Element entity)
	{
		LivingEntity e = new LivingEntity(ObjectRegistry.entities.find(entity.getChildText("id")), null);
		e.nbt = (TagCompound) NBTReader.read(entity.getChildText("nbt"), true, false, true);
		e.findProperties(entity);
		return e;
	}

	public static LivingEntity createFrom(TagCompound tag)
	{
		Entity e = ObjectRegistry.entities.first();
		TagCompound nbt = tag;
		if (tag.hasTag("id")) e = ObjectRegistry.entities.find(((TagString) tag.getTagFromId("id")).value());

		ArrayList<Tag> tags = new ArrayList<Tag>();
		for (Tag t : nbt.value())
			if (!t.id().equals(Tags.ENTITY_ID) && !t.id().equals(Tags.OBJECT_NAME)) tags.add(t);
		nbt = ((TemplateCompound) nbt.template).create(tags.toArray(new Tag[tags.size()]));

		LivingEntity en = new LivingEntity(e, nbt);
		en.findName(tag);
		return en;
	}

	private Entity entity;
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
	public Component getDisplayComponent()
	{
		CGPanel p = new CGPanel();
		p.add(new CGLabel(this.entity.name()));
		p.add(new ImageLabel(this.entity.texture()));
		return p;
	}

	public Entity getEntity()
	{
		return entity;
	}

	@Override
	public String getName(int index)
	{
		return this.customName() != null && !this.customName().equals("") ? this.customName() : this.entity.name().toString();
	}

	public TagCompound getNbt()
	{
		return nbt;
	}

	public Text name()
	{
		return this.entity.name();
	}

	public void setEntity(Entity entity)
	{
		this.entity = entity;
		this.onChange();
	}

	public void setNbt(TagCompound nbt)
	{
		this.nbt = nbt;
		this.onChange();
	}

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
	public String toString()
	{
		return this.entity.name().toString();
	}

	@Override
	public TagCompound toTag(TemplateCompound container)
	{
		ArrayList<Tag> tags = new ArrayList<Tag>();
		tags.add(Tags.ENTITY_ID.create(this.entity.id()));
		for (Tag t : this.nbt.value())
			tags.add(t);
		return container.create(tags.toArray(new Tag[tags.size()]));
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
