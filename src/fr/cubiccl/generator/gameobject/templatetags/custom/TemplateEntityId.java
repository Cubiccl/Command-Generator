package fr.cubiccl.generator.gameobject.templatetags.custom;

import fr.cubiccl.generator.gameobject.baseobjects.BaseObject;
import fr.cubiccl.generator.gameobject.baseobjects.Entity;
import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagString;
import fr.cubiccl.generator.gameobject.templatetags.TemplateString;
import fr.cubiccl.generator.gui.component.combobox.ObjectCombobox;
import fr.cubiccl.generator.gui.component.panel.CGPanel;

public class TemplateEntityId extends TemplateString
{

	public TemplateEntityId(String id, byte applicationType, String... applicable)
	{
		super(id, applicationType, applicable);
	}

	@Override
	protected CGPanel createPanel(BaseObject object, Tag previousValue)
	{
		ObjectCombobox<Entity> box = new ObjectCombobox<Entity>(ObjectRegistry.entities.list(true));
		if (previousValue != null) box.setSelected(ObjectRegistry.entities.find(((TagString) previousValue).value()));
		return box.container;
	}

	@Override
	public TagString generateTag(BaseObject object, CGPanel panel)
	{
		@SuppressWarnings("unchecked")
		ObjectCombobox<Entity> box = (ObjectCombobox<Entity>) panel.getComponent(1);
		return super.create(box.getSelectedObject().id());
	}

	@Override
	protected boolean isInputValid(BaseObject object, CGPanel panel)
	{
		return true;
	}

}
