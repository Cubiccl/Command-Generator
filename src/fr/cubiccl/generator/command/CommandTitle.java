package fr.cubiccl.generator.command;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import fr.cubiccl.generator.gameobject.JsonMessage;
import fr.cubiccl.generator.gameobject.tags.NBTParser;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.tags.TagList;
import fr.cubiccl.generator.gameobject.target.Target;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gui.component.combobox.OptionCombobox;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelListJsonMessage;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelTarget;
import fr.cubiccl.generator.gui.component.textfield.CGEntry;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;

public class CommandTitle extends Command implements ActionListener
{
	private OptionCombobox comboboxMode;
	private CGEntry entryFadeIn, entryStay, entryFadeOut;
	private PanelListJsonMessage panelJson;
	private PanelTarget panelTarget;

	public CommandTitle()
	{
		super("title", "title <player> <title|subtitle|actionbar> <json>\n" + "title <player> times <fadeIn> <stay> <fadeOut>\n"
				+ "title <player> <clear|reset>", 3, 4, 6);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		this.onParsingEnd();
		this.updateTranslations();
	}

	@Override
	public CGPanel createUI()
	{
		CGPanel panel = new CGPanel();
		GridBagConstraints gbc = panel.createGridBagLayout();

		panel.add(this.labelDescription(), gbc);
		++gbc.gridy;
		panel.add(this.comboboxMode = new OptionCombobox("title.mode", "title", "subtitle", "actionbar", "times", "clear", "reset"), gbc);
		++gbc.gridy;
		panel.add(this.panelTarget = new PanelTarget(PanelTarget.PLAYERS_ONLY), gbc);
		++gbc.gridy;
		panel.add(this.panelJson = new PanelListJsonMessage(), gbc);
		panel.add((this.entryFadeIn = new CGEntry(new Text("title.fade_in"), "0", Text.INTEGER)).container, gbc);
		++gbc.gridy;
		panel.add((this.entryStay = new CGEntry(new Text("title.stay"), "0", Text.INTEGER)).container, gbc);
		++gbc.gridy;
		panel.add((this.entryFadeOut = new CGEntry(new Text("title.fade_out"), "0", Text.INTEGER)).container, gbc);

		this.entryFadeIn.container.setVisible(false);
		this.entryStay.container.setVisible(false);
		this.entryFadeOut.container.setVisible(false);
		this.comboboxMode.addActionListener(this);

		this.entryFadeIn.addIntFilter();
		this.entryStay.addIntFilter();
		this.entryFadeOut.addIntFilter();

		this.panelTarget.addArgumentChangeListener(this);

		return panel;
	}

	@Override
	protected Text description()
	{
		if (this.comboboxMode.getValue().equals("times") || this.comboboxMode.getValue().equals("reset")) return new Text("command." + this.id + ".times")
				.addReplacement("<target>", this.panelTarget.displayTarget());
		return this.defaultDescription().addReplacement("<target>", this.panelTarget.displayTarget());
	}

	@Override
	protected void onParsingEnd()
	{
		String mode = this.comboboxMode.getValue();
		this.panelJson.setVisible(mode.equals("title") || mode.equals("subtitle") || mode.equals("actionbar"));
		this.entryFadeIn.container.setVisible(mode.equals("times"));
		this.entryStay.container.setVisible(mode.equals("times"));
		this.entryFadeOut.container.setVisible(mode.equals("times"));
	}

	@Override
	public String generate() throws CommandGenerationException
	{
		String mode = this.comboboxMode.getValue();
		String command = this.id + " " + this.panelTarget.generate().toCommand() + " " + mode;
		if (mode.equals("title") || mode.equals("subtitle") || mode.equals("actionbar")) return command + " "
				+ this.panelJson.generateMessage(Tags.DEFAULT_LIST).valueForCommand();
		if (mode.equals("times"))
		{
			this.entryFadeIn.checkValueSuperior(CGEntry.INTEGER, 0);
			this.entryStay.checkValueSuperior(CGEntry.INTEGER, 0);
			this.entryFadeOut.checkValueSuperior(CGEntry.INTEGER, 0);
			return command + " " + this.entryFadeIn.getText() + " " + this.entryStay.getText() + " " + this.entryFadeOut.getText();
		}
		return command;
	}

	@Override
	protected void readArgument(int index, String argument, String[] fullCommand) throws CommandGenerationException
	{
		// title <player> <title|subtitle|actionbar> <json>
		// title <player> times <fadeIn> <stay> <fadeOut>
		// title <player> <clear|reset>

		if (index == 1) this.panelTarget.setupFrom(Target.createFrom(argument));
		if (index == 2) this.comboboxMode.setValue(argument);
		if (index == 3) if (this.comboboxMode.getValue().equals("times")) try
		{
			if (Integer.parseInt(argument) >= 0) this.entryFadeIn.setText(argument);
		} catch (Exception e)
		{}
		else
		{
			this.panelJson.clear();
			Tag t = NBTParser.parse(argument, true, true);
			if (t instanceof TagCompound) this.panelJson.addMessage(JsonMessage.createFrom((TagCompound) t));
			else for (Tag tag : ((TagList) t).value())
				this.panelJson.addMessage(JsonMessage.createFrom((TagCompound) tag));
		}
		if (index == 4) try
		{
			if (Integer.parseInt(argument) >= 0) this.entryStay.setText(argument);
		} catch (Exception e)
		{}
		if (index == 5) try
		{
			if (Integer.parseInt(argument) >= 0) this.entryFadeOut.setText(argument);
		} catch (Exception e)
		{}
	}
}
