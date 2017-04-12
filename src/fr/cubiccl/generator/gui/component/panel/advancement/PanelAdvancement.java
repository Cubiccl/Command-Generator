package fr.cubiccl.generator.gui.component.panel.advancement;

import java.awt.Font;
import java.awt.GridBagConstraints;

import javax.swing.SwingConstants;

import fr.cubiccl.generator.gameobject.advancements.Advancement;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelItem;
import fr.cubiccl.generator.utils.Text;

public class PanelAdvancement extends CGPanel
{
	private static final long serialVersionUID = -773099244243224959L;

	public final Advancement advancement;
	private PanelItem panelItem;

	public PanelAdvancement(Advancement advancement)
	{
		this.advancement = advancement;
		CGLabel l = new CGLabel(new Text(this.advancement.customName(), false));
		l.setFont(l.getFont().deriveFont(Font.BOLD, 30));
		l.setHorizontalAlignment(SwingConstants.CENTER);

		GridBagConstraints gbc = this.createGridBagLayout();
		this.add(l, gbc);
		++gbc.gridy;
		this.add(this.panelItem = new PanelItem("advancement.display_item"), gbc);

		this.panelItem.setHasDurability(false);
		this.panelItem.setHasData(false);
		this.panelItem.setHasNBT(false);
		this.panelItem.setHasAmount(false);
		this.panelItem.addArgumentChangeListener(this);
	}

	@Override
	public void updateTranslations()
	{
		super.updateTranslations();

		// OK this is like not how you code properly but whatever
		this.advancement.setItem(this.panelItem.selectedItem());
	}

}
