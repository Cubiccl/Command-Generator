package fr.cubiccl.generator.gameobject.templatetags.custom;

import fr.cubiccl.generator.CommandGenerator;
import fr.cubiccl.generator.gameobject.Coordinates;
import fr.cubiccl.generator.gameobject.baseobjects.BaseObject;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelCoordinates;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;

public class TemplateCoordinates extends TemplateCompound
{
	private boolean isInteger = false;

	public TemplateCoordinates(String id, byte applicationType, String[] applicable)
	{
		super(id, applicationType, applicable);
	}

	@Override
	protected CGPanel createPanel(BaseObject object, Tag previousValue)
	{
		this.isInteger = object != null && object.id().equals("minecraft:ender_crystal");
		PanelCoordinates p = new PanelCoordinates(null, false);
		if (previousValue != null)
		{
			TagCompound coord = (TagCompound) previousValue;
			Coordinates c;
			if (this.isInteger) c = new Coordinates((float) (int) coord.getTagFromId(Tags.COORD_X.id()).value(), (float) (int) coord.getTagFromId(
					Tags.COORD_Y.id()).value(), (float) (int) coord.getTagFromId(Tags.COORD_Z.id()).value());
			else c = new Coordinates((float) (double) coord.getTagFromId(Tags.COORD_X.id()).value(), (float) (double) coord.getTagFromId(Tags.COORD_Y.id())
					.value(), (float) (double) coord.getTagFromId(Tags.COORD_Z.id()).value());
			p.setupFrom(c);
		}
		if (this.id().equals("Motion")) p.setEntryText(new Text("tag.Motion.x"), new Text("tag.Motion.y"), new Text("tag.Motion.z"));
		p.setName(this.title());
		return p;
	}

	@Override
	public TagCompound generateTag(BaseObject object, CGPanel panel)
	{
		try
		{
			if (this.isInteger)
			{
				Coordinates c = ((PanelCoordinates) panel).generate();
				int x = (int) c.x, y = (int) c.y, z = (int) c.z;
				return this.create(Tags.COORD_X_INT.create(x), Tags.COORD_Y_INT.create(y), Tags.COORD_Z_INT.create(z));
			}
			return ((PanelCoordinates) panel).generate().toNBT(this);
		} catch (CommandGenerationException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	@Override
	protected boolean isInputValid(BaseObject object, CGPanel panel)
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
