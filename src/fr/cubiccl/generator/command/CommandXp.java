package fr.cubiccl.generator.command;

import java.awt.GridBagConstraints;

import fr.cubiccl.generator.gui.component.button.CCheckBox;
import fr.cubiccl.generator.gui.component.panel.CPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelTarget;
import fr.cubiccl.generator.gui.component.textfield.CEntry;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Lang;
import fr.cubiccl.generator.utils.WrongValueException;

public class CommandXp extends Command
{
	private CCheckBox checkboxLevel;
	private CEntry entryAmount;
	private PanelTarget panelTarget;

	public CommandXp()
	{
		super("xp");
	}

	@Override
	public CPanel createGUI()
	{
		CPanel panel = new CPanel();
		GridBagConstraints gbc = panel.createGridBagLayout();

		panel.add(this.labelDescription(), gbc);
		++gbc.gridy;
		panel.add((this.entryAmount = new CEntry("xp.amount", "0")).container, gbc);
		++gbc.gridy;
		panel.add(this.checkboxLevel = new CCheckBox("xp.level"), gbc);
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
			throw new WrongValueException(this.entryAmount.label.getAbsoluteText(), Lang.translate("error.integer"), this.entryAmount.getText());
		}
		return "/xp " + this.entryAmount.getText() + (this.checkboxLevel.isSelected() ? "L " : " ") + this.panelTarget.generateTarget().toCommand();
	}

}
