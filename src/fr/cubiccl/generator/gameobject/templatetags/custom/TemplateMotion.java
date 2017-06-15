package fr.cubiccl.generator.gameobject.templatetags.custom;

import fr.cubiccl.generator.CommandGenerator;
import fr.cubiccl.generator.gameobject.Coordinates;
import fr.cubiccl.generator.gameobject.baseobjects.BaseObject;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagList;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateList;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelCoordinates;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;

public class TemplateMotion extends TemplateList
{

	@Override
	protected CGPanel createPanel(BaseObject<?> object, Tag previousValue)
	{
		PanelCoordinates p = new PanelCoordinates(null, false);
		if (previousValue != null)
		{
			TagList coord = (TagList) previousValue;
			Coordinates c;
			c = new Coordinates((float) (double) coord.getTag(0).value(), (float) (double) coord.getTag(1).value(), (float) (double) coord.getTag(2).value());
			p.setupFrom(c);
		}
		p.setEntryText(new Text("tag.Motion.x"), new Text("tag.Motion.y"), new Text("tag.Motion.z"));
		p.setName(this.title());
		return p;
	}

	@Override
	public TagList generateTag(BaseObject<?> object, CGPanel panel)
	{
		try
		{
			return ((PanelCoordinates) panel).generate().toTagList(this, Tags.DEFAULT_DOUBLE);
		} catch (CommandGenerationException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	@Override
	protected boolean isInputValid(BaseObject<?> object, CGPanel panel)
	{
		try
		{
			((PanelCoordinates) panel).generate();
		} catch (CommandGenerationException e)
		{
			CommandGenerator.report(e);
			return false;
		}
		return true;
	}

}
