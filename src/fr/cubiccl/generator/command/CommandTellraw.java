package fr.cubiccl.generator.command;

import java.awt.GridBagConstraints;

import fr.cubiccl.generator.gameobject.tags.NBTParser;
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
	public CGPanel createUI()
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
		return this.id + " " + this.panelTarget.generate().toCommand() + " " + this.panelJson.generateMessage(Tags.DEFAULT_LIST).valueForCommand();
	}

	@Override
	protected void readArgument(int index, String argument, String[] fullCommand) throws CommandGenerationException
	{
		if (index == 1) this.panelTarget.setupFrom(new Target().fromString(argument));
		if (index == 2) this.panelJson.setupFrom(NBTParser.parse(argument, true, true));
	}

}
