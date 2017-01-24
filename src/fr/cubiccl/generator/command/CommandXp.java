package fr.cubiccl.generator.command;

import java.awt.GridBagConstraints;

import fr.cubiccl.generator.gui.component.button.CGCheckBox;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelTarget;
import fr.cubiccl.generator.gui.component.textfield.CGEntry;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;

public class CommandXp extends Command
{
	private CGCheckBox checkboxLevel;
	private CGEntry entryAmount;
	private PanelTarget panelTarget;

	public CommandXp()
	{
		super("xp");
	}

	@Override
	public CGPanel createGUI()
	{
		CGPanel panel = new CGPanel();
		GridBagConstraints gbc = panel.createGridBagLayout();

		panel.add(this.labelDescription(), gbc);
		++gbc.gridy;
		panel.add((this.entryAmount = new CGEntry(new Text("xp.amount"), "0", Text.INTEGER)).container, gbc);
		++gbc.gridy;
		panel.add(this.checkboxLevel = new CGCheckBox("xp.level"), gbc);
		++gbc.gridy;
		panel.add(this.panelTarget = new PanelTarget(PanelTarget.PLAYERS_ONLY), gbc);

		this.entryAmount.addIntFilter();
		this.checkboxLevel.setSelected(true);

		return panel;
	}

	@Override
	public String generate() throws CommandGenerationException
	{
		this.entryAmount.checkValue(CGEntry.INTEGER);
		if (!this.checkboxLevel.isSelected()) this.entryAmount.checkValueSuperior(CGEntry.INTEGER, 0);
		return this.id + " " + this.entryAmount.getText() + (this.checkboxLevel.isSelected() ? "L " : " ") + this.panelTarget.generateTarget().toCommand();
	}

}
