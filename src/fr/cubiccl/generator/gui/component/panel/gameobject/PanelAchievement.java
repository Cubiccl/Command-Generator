package fr.cubiccl.generator.gui.component.panel.gameobject;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import fr.cubiccl.generator.gameobject.baseobjects.Achievement;
import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.gui.component.combobox.ObjectCombobox;
import fr.cubiccl.generator.gui.component.label.ImageLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;

public class PanelAchievement extends CGPanel implements ActionListener
{
	private static final long serialVersionUID = 6778070675272037343L;

	private ObjectCombobox<Achievement> comboboxAchievement;
	private ImageLabel labelImage;

	public PanelAchievement()
	{
		Achievement[] achievements = ObjectRegistry.achievements.list();
		GridBagConstraints gbc = this.createGridBagLayout();
		this.comboboxAchievement = new ObjectCombobox<Achievement>(achievements);

		this.add(this.comboboxAchievement.container, gbc);
		++gbc.gridx;
		this.add(this.labelImage = new ImageLabel(achievements[0].texture()), gbc);

		this.comboboxAchievement.addActionListener(this);

		this.updateTranslations();
	}

	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		this.onSelection();
	}

	public Achievement getAchievement()
	{
		return this.comboboxAchievement.getSelectedObject();
	}

	private void onSelection()
	{
		this.labelImage.setImage(this.getAchievement().texture());
	}

	public void setSelection(Achievement achievement)
	{
		if (achievement == null) return;
		this.comboboxAchievement.setSelected(achievement);
		this.onSelection();
	}

}
