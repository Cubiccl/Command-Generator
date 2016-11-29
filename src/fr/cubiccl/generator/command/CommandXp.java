package fr.cubiccl.generator.command;

import java.awt.GridBagConstraints;

import fr.cubiccl.generator.gui.component.button.CGCheckBox;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelTarget;
import fr.cubiccl.generator.gui.component.textfield.CGEntry;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;
import fr.cubiccl.generator.utils.WrongValueException;

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
		panel.add((this.entryAmount = new CGEntry("xp.amount", "0")).container, gbc);
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
		try
		{
			Integer.parseInt(this.entryAmount.getText());
		} catch (NumberFormatException e)
		{
			throw new WrongValueException(this.entryAmount.label.getAbsoluteText(), new Text("error.integer"), this.entryAmount.getText());
		}
		return "/xp " + this.entryAmount.getText() + (this.checkboxLevel.isSelected() ? "L " : " ") + this.panelTarget.generateTarget().toCommand();
	}

}
