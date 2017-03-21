package fr.cubiccl.generator.command;

import java.awt.GridBagConstraints;

import fr.cubiccl.generator.gameobject.JsonMessage;
import fr.cubiccl.generator.gameobject.tags.NBTReader;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.tags.TagList;
import fr.cubiccl.generator.gameobject.target.Target;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelListJsonMessage;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelTarget;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;

public class CommandTellraw extends Command
{
	private PanelListJsonMessage panelJson;
	private PanelTarget panelTarget;

	public CommandTellraw()
	{
		super("tellraw", "tellraw <player> <json>", 3);
	}

	@Override
	public CGPanel createGUI()
	{
		CGPanel panel = new CGPanel();
		GridBagConstraints gbc = panel.createGridBagLayout();

		panel.add(this.labelDescription(), gbc);
		++gbc.gridy;
		panel.add(this.panelTarget = new PanelTarget(PanelTarget.PLAYERS_ONLY), gbc);
		++gbc.gridy;
		panel.add(this.panelJson = new PanelListJsonMessage(), gbc);

		this.panelTarget.addArgumentChangeListener(this);

		return panel;
	}

	@Override
	protected Text description()
	{
		return this.defaultDescription().addReplacement("<target>", this.panelTarget.displayTarget());
	}

	@Override
	public String generate() throws CommandGenerationException
	{
		return this.id + " " + this.panelTarget.generate().toCommand() + " " + this.panelJson.generateMessage(Tags.JSON_LIST).valueForCommand();
	}

	@Override
	protected void readArgument(int index, String argument, String[] fullCommand) throws CommandGenerationException
	{
		if (index == 1) this.panelTarget.setupFrom(Target.createFrom(argument));
		if (index == 2)
		{
			this.panelJson.clear();
			Tag t = NBTReader.read(argument, true, true);
			if (t instanceof TagCompound) this.panelJson.addMessage(JsonMessage.createFrom((TagCompound) t));
			else for (Tag tag : ((TagList) t).value())
				this.panelJson.addMessage(JsonMessage.createFrom((TagCompound) tag));
		}
	}

}
