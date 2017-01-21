package fr.cubiccl.generator.gameobject.templatetags.custom;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import fr.cubiccl.generator.gameobject.baseobjects.BaseObject;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagString;
import fr.cubiccl.generator.gameobject.templatetags.TemplateString;
import fr.cubiccl.generator.gui.component.combobox.CGComboBox;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.label.ImageLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.utils.ConfirmPanel;
import fr.cubiccl.generator.utils.FileUtils;
import fr.cubiccl.generator.utils.Text;

public class TemplatePainting extends TemplateString
{
	private class PanelPainting extends ConfirmPanel
	{
		private static final long serialVersionUID = 8026044195965959359L;

		public final CGComboBox combobox;
		private ImageLabel label;

		public PanelPainting(Text description, String... options)
		{
			super();
			JPanel panel = new JPanel(new GridBagLayout());
			GridBagConstraints gbc = new GridBagConstraints();
			this.combobox = new CGComboBox(options);
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.anchor = GridBagConstraints.NORTH;
			gbc.insets = new Insets(5, 5, 5, 5);
			panel.add(new CGLabel(description), gbc);
			++gbc.gridy;
			panel.add(this.combobox, gbc);
			++gbc.gridy;
			panel.add(this.label = new ImageLabel(), gbc);
			this.setMainComponent(panel);

			this.combobox.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					updateImage();
				}
			});
			this.updateImage();
		}

		private void updateImage()
		{
			this.label.setImage(FileUtils.readImage("textures/painting/" + this.combobox.getValue()));
		}

	}

	public static final String[] PAINTINGS =
	{ "Kebab", "Aztec", "Alban", "Aztec2", "Bomb", "Plant", "Wasteland", "Wanderer", "Graham", "Pool", "Courbet", "Sunset", "Sea", "Creebet", "Match", "Bust",
			"Stage", "Void", "SkullAnRoses", "Wither", "Fighters", "Skeleton", "DonkeyKong", "Pointer", "Pigscene", "BurningSkull" };

	public TemplatePainting(String id, byte tagType, String... applicable)
	{
		super(id, tagType, applicable);
	}

	@Override
	protected CGPanel createPanel(BaseObject object, Tag previousValue)
	{
		return new PanelPainting(this.description(), PAINTINGS);
	}

	@Override
	public TagString generateTag(BaseObject object, CGPanel panel)
	{
		return new TagString(this, ((PanelPainting) panel).combobox.getValue());
	}

	@Override
	protected boolean isInputValid(BaseObject object, CGPanel panel)
	{
		return true;
	}

}
