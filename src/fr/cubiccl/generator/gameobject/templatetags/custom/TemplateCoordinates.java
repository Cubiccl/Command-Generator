package fr.cubiccl.generator.gameobject.templatetags.custom;

import fr.cubiccl.generator.CommandGenerator;
import fr.cubiccl.generator.gameobject.Coordinates;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelCoordinates;
import fr.cubiccl.generator.gui.component.panel.utils.ConfirmPanel;
import fr.cubiccl.generator.utils.WrongValueException;

public class TemplateCoordinates extends TemplateCompound
{

	public TemplateCoordinates(String id, byte tagType, String[] applicable)
	{
		super(id, tagType, applicable);
	}

	@Override
	protected ConfirmPanel createPanel(String objectId, Tag previousValue)
	{
		PanelCoordinates p = new PanelCoordinates(null);
		if (previousValue != null)
		{
			TagCompound coord = (TagCompound) previousValue;
			Coordinates c = new Coordinates((float) (int) coord.getTagFromId(Tags.COORD_X.id).value(), (float) (int) coord.getTagFromId(Tags.COORD_Y.id)
					.value(), (float) (int) coord.getTagFromId(Tags.COORD_Z.id).value());
			p.setupFrom(c);
		}
		return new ConfirmPanel("tag.title." + this.id, p);
	}

	@Override
	public TagCompound generateTag(ConfirmPanel panel)
	{
		try
		{
			return ((PanelCoordinates) panel.component).generateCoordinates().toTag(this);
		} catch (WrongValueException e)
		{
			CommandGenerator.report(e);
			return null;
		}
	}

	@Override
	protected boolean isInputValid(ConfirmPanel panel)
	{
		try
		{
			((PanelCoordinates) panel.component).generateCoordinates();
		} catch (WrongValueException e)
		{
			CommandGenerator.report(e);
			return false;
		}
		return true;
	}

}
