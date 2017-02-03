package fr.cubiccl.generator.gameobject.templatetags.custom;

import fr.cubi.cubigui.DisplayUtils;
import fr.cubiccl.generator.CommandGenerator;
import fr.cubiccl.generator.gameobject.JsonMessage;
import fr.cubiccl.generator.gameobject.baseobjects.BaseObject;
import fr.cubiccl.generator.gameobject.tags.NBTReader;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.tags.TagString;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateString;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelJsonMessage;
import fr.cubiccl.generator.gui.component.panel.utils.EntryPanel;
import fr.cubiccl.generator.gui.component.panel.utils.PanelRadio;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;

public class TemplateText extends TemplateString
{

	public TemplateText(String id, byte applicationType, String... applicable)
	{
		super(id, applicationType, applicable);
	}

	@Override
	protected CGPanel createPanel(BaseObject object, Tag previousValue)
	{
		PanelRadio p = new PanelRadio(new Text("text.choose"), "text", "string", "json");
		DisplayUtils.showPopup(CommandGenerator.window, "", p.component);

		if (p.getSelected() == 0) return super.createPanel(object, previousValue);

		PanelJsonMessage pj = new PanelJsonMessage();
		if (previousValue != null) pj.setupFrom(JsonMessage.createFrom((TagCompound) NBTReader.read((String) previousValue.value(), true, true)));
		pj.setName(this.title());
		return pj;
	}

	@Override
	public TagString generateTag(BaseObject object, CGPanel panel)
	{
		if (panel instanceof EntryPanel) return super.generateTag(object, panel);
		try
		{
			return new TagString(this, ((PanelJsonMessage) panel).generateMessage().toTag(Tags.JSON_CONTAINER).valueForCommand());
		} catch (CommandGenerationException e)
		{
			CommandGenerator.report(e);
			return null;
		}
	}

}
