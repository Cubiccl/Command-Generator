package fr.cubiccl.generator.gameobject.templatetags.custom;

import java.util.ArrayList;
import java.util.HashMap;

import fr.cubiccl.generator.gameobject.baseobjects.BaseObject;
import fr.cubiccl.generator.gameobject.baseobjects.Block;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;
import fr.cubiccl.generator.gameobject.templatetags.TemplateString;
import fr.cubiccl.generator.gui.Dialogs;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelStateSelection;
import fr.cubiccl.generator.utils.Text;

public class TemplateBlockState extends TemplateCompound
{
	public Block block;

	public TemplateBlockState(String id, byte applicationType, String... applicable)
	{
		super(id, applicationType, applicable);
	}

	@Override
	protected CGPanel createPanel(BaseObject<?> object, Tag previousValue)
	{
		if (this.block == null)
		{
			Dialogs.showMessage(new Text("error.tag.state").toString());
			return null;
		}

		PanelStateSelection p = new PanelStateSelection(this.block);
		if (previousValue != null)
		{
			for (Tag t : ((TagCompound) previousValue).value())
				p.setState(t.id(), (String) t.value());
		}

		return p;
	}

	@Override
	protected TagCompound generateTag(BaseObject<?> object, CGPanel panel)
	{
		HashMap<String, String> values = ((PanelStateSelection) panel).values;
		ArrayList<Tag> tags = new ArrayList<Tag>();
		for (String state : values.keySet())
			if (values.get(state) != null) tags.add(new TemplateString(state, Tag.UNKNOWN).create(values.get(state)));
		return this.create(tags.toArray(new Tag[tags.size()]));
	}

}
