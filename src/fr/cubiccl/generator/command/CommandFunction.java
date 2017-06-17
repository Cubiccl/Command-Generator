package fr.cubiccl.generator.command;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import fr.cubiccl.generator.gameobject.target.Target;
import fr.cubiccl.generator.gui.component.combobox.OptionCombobox;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelTarget;
import fr.cubiccl.generator.gui.component.textfield.CGEntry;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;

public class CommandFunction extends Command implements ActionListener
{
	private OptionCombobox comboboxMode;
	private CGEntry entryMessage;
	private PanelTarget panelTarget;

	public CommandFunction()
	{
		super("function", "function <path> [<if|unless> <target>]", 2, 4);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		this.panelTarget.setVisible(!this.comboboxMode.getValue().equals("normal"));
		this.updateTranslations();
	}

	@Override
	public CGPanel createUI()
	{
		CGPanel panel = new CGPanel();
		GridBagConstraints gbc = panel.createGridBagLayout();

		panel.add(this.labelDescription(), gbc);
		++gbc.gridy;
		panel.add((this.entryMessage = new CGEntry(new Text("function.path"), new Text("function.path"))).container, gbc);
		++gbc.gridy;
		panel.add(this.comboboxMode = new OptionCombobox("function", "normal", "if", "unless"), gbc);
		++gbc.gridy;
		panel.add(this.panelTarget = new PanelTarget(PanelTarget.ALL_ENTITIES), gbc);

		this.entryMessage.addKeyListener(new KeyListener()
		{
			@Override
			public void keyPressed(KeyEvent e)
			{}

			@Override
			public void keyReleased(KeyEvent e)
			{
				updateTranslations();
			}

			@Override
			public void keyTyped(KeyEvent e)
			{}
		});
		this.comboboxMode.addActionListener(this);
		this.panelTarget.setVisible(false);
		this.panelTarget.addArgumentChangeListener(this);

		return panel;
	}

	@Override
	protected Text description()
	{
		if (this.comboboxMode.getValue().equals("normal")) return this.defaultDescription();
		return new Text("command." + this.id + "." + this.comboboxMode.getValue()).addReplacement("<target>", this.panelTarget.displayTarget());
	}

	@Override
	public String generate() throws CommandGenerationException
	{
		if (this.comboboxMode.getValue().equals("normal")) return this.id + " " + this.entryMessage.getText();
		return this.id + " " + this.entryMessage.getText() + " " + this.comboboxMode.getValue() + " " + this.panelTarget.generate().toCommand();
	}

	@Override
	protected void readArgument(int index, String argument, String[] fullCommand) throws CommandGenerationException
	{
		// function <path> [<if|unless> <target>]
		if (index == 1) this.entryMessage.setText(argument);
		if (index == 2) this.comboboxMode.setValue(argument);
		if (index == 3) this.panelTarget.setupFrom(new Target().fromString(argument));
	}

	@Override
	protected void resetUI()
	{
		this.comboboxMode.setValue("normal");
		this.panelTarget.setVisible(false);
	}

}
