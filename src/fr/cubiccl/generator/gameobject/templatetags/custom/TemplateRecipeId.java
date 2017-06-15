package fr.cubiccl.generator.gameobject.templatetags.custom;

import fr.cubiccl.generator.gameobject.baseobjects.BaseObject;
import fr.cubiccl.generator.gameobject.baseobjects.RecipeType;
import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagString;
import fr.cubiccl.generator.gameobject.templatetags.TemplateString;
import fr.cubiccl.generator.gui.component.combobox.ObjectCombobox;
import fr.cubiccl.generator.gui.component.panel.CGPanel;

public class TemplateRecipeId extends TemplateString
{

	public TemplateRecipeId(String id, byte applicationType, String... applicable)
	{
		super(id, applicationType, applicable);
	}

	@Override
	protected CGPanel createPanel(BaseObject<?> object, Tag previousValue)
	{
		ObjectCombobox<RecipeType> box = new ObjectCombobox<RecipeType>(ObjectRegistry.recipes.list());
		if (previousValue != null) box.setSelected(ObjectRegistry.recipes.find(((TagString) previousValue).value()));
		return box.container;
	}

	@Override
	public TagString generateTag(BaseObject<?> object, CGPanel panel)
	{
		@SuppressWarnings("unchecked")
		ObjectCombobox<RecipeType> box = (ObjectCombobox<RecipeType>) panel.getComponent(1);
		return super.create(box.getSelectedObject().id());
	}

	@Override
	protected boolean isInputValid(BaseObject<?> object, CGPanel panel)
	{
		return true;
	}

}
