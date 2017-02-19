package fr.cubiccl.generator.gameobject.templatetags.custom;

import java.awt.GridBagConstraints;
import java.util.concurrent.ThreadLocalRandom;

import fr.cubiccl.generator.CommandGenerator;
import fr.cubiccl.generator.gameobject.baseobjects.BaseObject;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;
import fr.cubiccl.generator.gui.Dialogs;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelCoordinates;
import fr.cubiccl.generator.gui.component.panel.utils.PanelRadio;
import fr.cubiccl.generator.gui.component.textfield.CGEntry;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Lang;
import fr.cubiccl.generator.utils.Text;

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

	public TemplateLeash(String id, byte applicationType, String[] applicable)
	{
		super(id, applicationType, applicable);
	}

	@Override
	protected CGPanel createPanel(BaseObject object, Tag previousValue)
	{
		PanelRadio p = new PanelRadio(new Text("leash.choose"), "leash", "fence", "entity");
		Dialogs.showConfirmDialog(p.component, Lang.translate("general.confirm"), null);

		if (p.getSelected() == 0) return new PanelCoordinates("leash.coordinates", false);
		return new LeashEntityPanel();
	}

	@Override
	public TagCompound generateTag(BaseObject object, CGPanel panel)
	{
		if (panel instanceof PanelCoordinates) try
		{
			return ((PanelCoordinates) panel).generate().toTag(this);
		} catch (CommandGenerationException e)
		{
			e.printStackTrace();
		}
		return this.create(Tags.ATTRIBUTE_UUIDMOST.create(Double.parseDouble(((LeashEntityPanel) panel).entryMost.getText())),
				Tags.ATTRIBUTE_UUIDLEAST.create(Double.parseDouble(((LeashEntityPanel) panel).entryLeast.getText())));
	}

	@Override
	protected boolean isInputValid(BaseObject object, CGPanel panel)
	{
		if (panel instanceof PanelCoordinates) try
		{
			((PanelCoordinates) panel).generate();
		} catch (CommandGenerationException e)
		{
			CommandGenerator.report(e);
			return false;
		}
		else try
		{
			((LeashEntityPanel) panel).entryMost.checkValue(CGEntry.NUMBER);
			((LeashEntityPanel) panel).entryLeast.checkValue(CGEntry.NUMBER);
		} catch (CommandGenerationException e)
		{
			CommandGenerator.report(e);
			return false;
		}
		return true;
	}

}
