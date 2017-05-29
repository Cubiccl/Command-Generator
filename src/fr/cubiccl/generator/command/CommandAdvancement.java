package fr.cubiccl.generator.command;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import fr.cubiccl.generator.CommandGenerator;
import fr.cubiccl.generator.gameobject.baseobjects.DefaultAdvancement;
import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.gameobject.target.Target;
import fr.cubiccl.generator.gui.component.button.CGButton;
import fr.cubiccl.generator.gui.component.button.CGCheckBox;
import fr.cubiccl.generator.gui.component.combobox.CGComboBox;
import fr.cubiccl.generator.gui.component.combobox.ObjectCombobox;
import fr.cubiccl.generator.gui.component.combobox.OptionCombobox;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelTarget;
import fr.cubiccl.generator.gui.component.textfield.CGEntry;
import fr.cubiccl.generator.gui.component.textfield.CGTextField;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.IStateListener;
import fr.cubiccl.generator.utils.Text;

public class CommandAdvancement extends Command implements ActionListener, IStateListener<CGPanel>
{

	private CGButton buttonPredefinedAdvancement;
	private CGCheckBox checkboxCriterion;
	private CGComboBox comboboxCriterion;
	private OptionCombobox comboboxMode, comboboxAdvMode;
	private CGEntry entryAdvancement;
	private boolean isPredefined = false;
	private PanelTarget panelTarget;
	private CGTextField textfieldCriterion;

	public CommandAdvancement()
	{
		super("advancement", "advancement <grant|revoke|test> <player> <everything|from|only|through|until> [<advancement>] [criterion]", 4);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == this.checkboxCriterion) this.onCheckbox();
		else if (e.getSource() == this.buttonPredefinedAdvancement) CommandGenerator.stateManager.setState(new ObjectCombobox<DefaultAdvancement>(
				ObjectRegistry.advancements.list()).container, this);
		else if (e.getSource() == this.comboboxAdvMode) this.onModeChange();
		this.updateTranslations();
	}

	@Override
	public CGPanel createUI()
	{
		CGPanel panel = new CGPanel();
		GridBagConstraints gbc = panel.createGridBagLayout();

		++gbc.gridwidth;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		panel.add(this.labelDescription(), gbc);
		++gbc.gridy;
		panel.add(this.comboboxMode = new OptionCombobox("advancement", "grant", "revoke", "test"), gbc);
		++gbc.gridy;
		panel.add(this.comboboxAdvMode = new OptionCombobox("advancement", "everything", "from", "only", "through", "until"), gbc);
		++gbc.gridy;
		--gbc.gridwidth;
		panel.add((this.entryAdvancement = new CGEntry("advancement.name")).container, gbc);
		++gbc.gridx;
		panel.add(this.buttonPredefinedAdvancement = new CGButton("advancement.predefined"), gbc);
		--gbc.gridx;
		++gbc.gridy;
		panel.add(this.checkboxCriterion = new CGCheckBox("advancement.criterion"), gbc);
		++gbc.gridx;
		panel.add(this.textfieldCriterion = new CGTextField(), gbc);
		panel.add(this.comboboxCriterion = new CGComboBox(""), gbc);
		--gbc.gridx;
		++gbc.gridy;
		++gbc.gridwidth;
		panel.add(this.panelTarget = new PanelTarget("target.title.player", PanelTarget.PLAYERS_ONLY), gbc);

		this.buttonPredefinedAdvancement.addActionListener(this);
		this.entryAdvancement.addKeyListener(new KeyListener()
		{
			@Override
			public void keyPressed(KeyEvent e)
			{}

			@Override
			public void keyReleased(KeyEvent e)
			{
				onAdvancementChange();
			}

			@Override
			public void keyTyped(KeyEvent e)
			{}
		});
		this.textfieldCriterion.addKeyListener(new KeyListener()
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
		this.comboboxAdvMode.addActionListener(this);
		this.comboboxCriterion.addActionListener(this);
		this.comboboxCriterion.setVisible(false);
		this.checkboxCriterion.addActionListener(this);

		this.onModeChange();

		return panel;
	}

	@Override
	protected void resetUI()
	{
		this.checkboxCriterion.setSelected(false);
	}

	@Override
	protected Text description()
	{
		String d = "command." + this.id;
		if (!this.comboboxMode.getValue().equals("grant")) d += "." + this.comboboxMode.getValue();
		if (this.checkboxCriterion.isSelected()) d += ".criterion";

		Text t = new Text(d).addReplacement("<target>", this.panelTarget.displayTarget());
		if (this.isPredefined) return t.addReplacement("<advancement>", ObjectRegistry.advancements.find(this.entryAdvancement.getText()).name())
				.addReplacement("<criterion>", this.comboboxCriterion.getValue());
		return t.addReplacement("<advancement>", this.entryAdvancement.getText()).addReplacement("<criterion>", this.textfieldCriterion.getText());
	}

	@Override
	protected void onParsingEnd()
	{
		this.onCheckbox();
		this.onModeChange();
	}

	@Override
	public String generate() throws CommandGenerationException
	{
		String command = this.id + " " + this.comboboxMode.getValue() + " " + this.panelTarget.generate().toCommand() + " " + this.comboboxAdvMode.getValue();
		if (this.comboboxAdvMode.getValue().equals("everything")) return command;
		this.entryAdvancement.checkValue(CGEntry.STRING);
		command += " " + this.entryAdvancement.getText();
		if (this.checkboxCriterion.isSelected()) if (this.isPredefined) return command + " " + this.comboboxCriterion.getValue();
		else return command + " " + this.textfieldCriterion.getText();
		return command;
	}

	private void onAdvancementChange()
	{
		this.isPredefined = ObjectRegistry.advancements.find(this.entryAdvancement.getText()) != null;
		String mode = this.comboboxAdvMode.getValue();
		this.comboboxCriterion.setVisible(mode.equals("only") && this.isPredefined);
		this.textfieldCriterion.setVisible(mode.equals("only") && !this.isPredefined);
		if (this.isPredefined) this.comboboxCriterion.setValues(ObjectRegistry.advancements.find(this.entryAdvancement.getText()).criteria);
		this.updateTranslations();
	}

	private void onCheckbox()
	{
		this.comboboxCriterion.setEnabled(this.checkboxCriterion.isSelected());
		this.textfieldCriterion.setEnabled(this.checkboxCriterion.isSelected());
	}

	private void onModeChange()
	{
		String mode = this.comboboxAdvMode.getValue();
		this.entryAdvancement.container.setVisible(!mode.equals("everything"));
		this.buttonPredefinedAdvancement.setVisible(!mode.equals("everything"));
		this.checkboxCriterion.setVisible(mode.equals("only"));
		this.textfieldCriterion.setVisible(mode.equals("only") && !this.isPredefined);
		this.comboboxCriterion.setVisible(mode.equals("only") && this.isPredefined);
		if (mode.equals("only")) this.onAdvancementChange();
	}

	@Override
	protected void readArgument(int index, String argument, String[] fullCommand) throws CommandGenerationException
	{
		// advancement <grant|revoke|test> <player> <everything|from|only|through|until> [<advancement>] [criterion]
		if (index == 1) this.comboboxMode.setValue(argument);
		else if (index == 2) this.panelTarget.setupFrom(Target.createFrom(argument));
		else if (index == 3) this.comboboxAdvMode.setValue(argument);
		else if (index == 4)
		{
			this.entryAdvancement.setText(argument);
			this.onAdvancementChange();
		} else if (index == 5)
		{
			this.checkboxCriterion.setSelected(true);
			if (this.isPredefined) this.comboboxCriterion.setSelectedItem(argument);
			else this.textfieldCriterion.setText(argument);
		}
	}

	@Override
	public boolean shouldStateClose(CGPanel panel)
	{
		@SuppressWarnings("unchecked")
		DefaultAdvancement a = ((ObjectCombobox<DefaultAdvancement>) panel.getComponent(1)).getSelectedObject();
		if (a != null)
		{
			this.entryAdvancement.setText(a.id());
			this.onAdvancementChange();
		}
		return a != null;
	}

}
