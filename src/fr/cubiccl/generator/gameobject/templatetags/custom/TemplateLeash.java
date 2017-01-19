package fr.cubiccl.generator.gameobject.templatetags.custom;

import java.awt.GridBagConstraints;
import java.util.concurrent.ThreadLocalRandom;

import fr.cubi.cubigui.DisplayUtils;
import fr.cubiccl.generator.CommandGenerator;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagBigNumber;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelCoordinates;
import fr.cubiccl.generator.gui.component.panel.utils.PanelRadio;
import fr.cubiccl.generator.gui.component.textfield.CGEntry;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;
import fr.cubiccl.generator.utils.Utils;

public class TemplateLeash extends TemplateCompound
{

	private class LeashEntityPanel extends CGPanel
	{
		private static final long serialVersionUID = 4549377741906583512L;

		private CGEntry entryLeast, entryMost;

		public LeashEntityPanel()
		{
			GridBagConstraints gbc = this.createGridBagLayout();
			this.add((this.entryMost = new CGEntry(new Text("leash.uuidmost"), Long.toString(ThreadLocalRandom.current().nextLong()), Text.INTEGER)).container,
					gbc);
			++gbc.gridy;
			this.add(
					(this.entryLeast = new CGEntry(new Text("leash.uuidleast"), Long.toString(ThreadLocalRandom.current().nextLong()), Text.INTEGER)).container,
					gbc);
		}

	}

	public TemplateLeash(String id, byte tagType, String[] applicable)
	{
		super(id, tagType, applicable);
	}

	@Override
	protected CGPanel createPanel(String objectId, Tag previousValue)
	{
		PanelRadio p = new PanelRadio(new Text("leash.choose"), "leash", "fence", "entity");
		DisplayUtils.showPopup(CommandGenerator.window, "", p.component);

		if (p.getSelected() == 0) return new PanelCoordinates("leash.coordinates", false);
		return new LeashEntityPanel();
	}

	@Override
	public TagCompound generateTag(CGPanel panel)
	{
		if (panel instanceof PanelCoordinates) try
		{
			return ((PanelCoordinates) panel).generateCoordinates().toTag(this);
		} catch (CommandGenerationException e)
		{
			e.printStackTrace();
		}
		return new TagCompound(this, new TagBigNumber(Tags.ATTRIBUTE_UUIDMOST, Double.parseDouble(((LeashEntityPanel) panel).entryMost.getText())),
				new TagBigNumber(Tags.ATTRIBUTE_UUIDLEAST, Double.parseDouble(((LeashEntityPanel) panel).entryLeast.getText())));
	}

	@Override
	protected boolean isInputValid(CGPanel panel)
	{
		if (panel instanceof PanelCoordinates) try
		{
			((PanelCoordinates) panel).generateCoordinates();
		} catch (CommandGenerationException e)
		{
			CommandGenerator.report(e);
			return false;
		}
		else try
		{
			Utils.checkNumber(((LeashEntityPanel) panel).entryMost.label.getAbsoluteText(), ((LeashEntityPanel) panel).entryMost.getText());
			Utils.checkNumber(((LeashEntityPanel) panel).entryLeast.label.getAbsoluteText(), ((LeashEntityPanel) panel).entryLeast.getText());
		} catch (CommandGenerationException e)
		{
			CommandGenerator.report(e);
			return false;
		}
		return true;
	}

}
