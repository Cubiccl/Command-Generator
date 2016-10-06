package fr.cubiccl.generator.gui.component.panel.gameobject;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import fr.cubiccl.generator.gameobject.ObjectRegistry;
import fr.cubiccl.generator.gameobject.baseobjects.Achievement;
import fr.cubiccl.generator.gui.component.combobox.ObjectCombobox;
import fr.cubiccl.generator.gui.component.label.ImageLabel;
import fr.cubiccl.generator.gui.component.panel.CPanel;

public class PanelAchievement extends CPanel implements ActionListener
{
	private static final long serialVersionUID = 6778070675272037343L;

	private ObjectCombobox<Achievement> comboboxAchievement;
	private ImageLabel labelImage;

	public PanelAchievement()
	{
		Achievement[] achievements = ObjectRegistry.getAchievements();
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
		this.labelImage.setImage(this.getAchievement().texture());
	}

	public Achievement getAchievement()
	{
		return this.comboboxAchievement.getSelectedObject();
	}

}
