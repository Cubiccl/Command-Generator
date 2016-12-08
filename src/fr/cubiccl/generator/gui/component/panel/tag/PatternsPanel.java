package fr.cubiccl.generator.gui.component.panel.tag;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JPanel;

import fr.cubi.cubigui.CButton;
import fr.cubiccl.generator.gameobject.Pattern;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gui.component.CGList;
import fr.cubiccl.generator.gui.component.button.CGButton;
import fr.cubiccl.generator.gui.component.combobox.OptionCombobox;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.utils.Utils;

public class PatternsPanel extends CGPanel implements ActionListener
{
	private static class BannerCanvas extends JPanel
	{
		private static final long serialVersionUID = 5685812167412531149L;

		private ArrayList<Pattern> patterns;

		public BannerCanvas(ArrayList<Pattern> patterns)
		{
			this.patterns = patterns;
			this.setPreferredSize(new Dimension(105, 200));
			this.setMaximumSize(new Dimension(105, 200));
			this.setBackground(Color.WHITE);
		}

		@Override
		public void paint(Graphics g)
		{
			super.paint(g);
			for (Pattern pattern : this.patterns)
				pattern.draw(g);
		}

	}

	public static final String[] PATTERNS =
	{ "bs", "ts", "ls", "rs", "ms", "cs", "ss", "drs", "dls", "cr", "sc", "ld", "rud", "lud", "rd", "hh", "vh", "bl", "br", "tl", "tr", "bt", "tt", "bts",
			"tts", "mc", "mr", "bo", "cbo", "bri", "cre", "sku", "flo", "moj", "gra" };

	private static final long serialVersionUID = 1500811177795494639L;

	private BannerCanvas banner;
	private CButton buttonAdd, buttonRemove, buttonUp, buttonDown;
	private OptionCombobox comboboxPattern, comboboxColor, comboboxBackground;
	private CGList list;
	private ArrayList<Pattern> patterns;

	public PatternsPanel()
	{
		this.patterns = new ArrayList<Pattern>();

		GridBagConstraints gbc = this.createGridBagLayout();
		gbc.fill = GridBagConstraints.BOTH;
		this.add(this.comboboxColor = new OptionCombobox("color", Utils.WOOL_COLORS));
		++gbc.gridx;
		this.add(this.comboboxPattern = new OptionCombobox("pattern", PATTERNS), gbc);
		--gbc.gridx;
		++gbc.gridy;
		this.add(this.buttonAdd = new CGButton("general.add"), gbc);
		++gbc.gridx;
		this.add(this.buttonRemove = new CGButton("general.remove"), gbc);
		--gbc.gridx;
		++gbc.gridy;
		gbc.fill = GridBagConstraints.NONE;
		this.add(this.banner = new BannerCanvas(this.patterns), gbc);
		++gbc.gridx;
		gbc.fill = GridBagConstraints.BOTH;
		this.add((this.list = new CGList()).scrollPane, gbc);

		++gbc.gridx;
		gbc.gridy = 0;
		gbc.gridheight = 3;

		CGPanel panel = new CGPanel();
		GridBagConstraints gbc2 = panel.createGridBagLayout();
		gbc2.fill = GridBagConstraints.HORIZONTAL;
		gbc2.anchor = GridBagConstraints.NORTH;
		panel.add(new CGLabel("banner.background"), gbc2);
		++gbc2.gridy;
		panel.add(this.comboboxBackground = new OptionCombobox("color", Utils.WOOL_COLORS), gbc2);
		++gbc2.gridy;
		panel.add(this.buttonUp = new CGButton("banner.up"), gbc2);
		++gbc2.gridy;
		panel.add(this.buttonDown = new CGButton("banner.down"), gbc2);

		this.add(panel, gbc);

		this.buttonAdd.addActionListener(this);
		this.buttonRemove.addActionListener(this);
		this.buttonUp.addActionListener(this);
		this.buttonDown.addActionListener(this);
		this.comboboxBackground.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		int index = this.list.getSelectedIndex();
		if (e.getSource() == this.buttonAdd)
		{
			this.patterns.add(new Pattern(this.comboboxColor.getSelectedIndex(), this.comboboxPattern.getValue()));
			this.updateDisplay();
		} else if (e.getSource() == this.buttonRemove && index != -1)
		{
			this.patterns.remove(index);
			this.updateDisplay();
		} else if (e.getSource() == this.buttonUp && index > 0)
		{
			Pattern p = this.patterns.remove(index);
			this.patterns.add(--index, p);
			this.list.setSelectedIndex(index);
			this.updateDisplay();
		} else if (e.getSource() == this.buttonDown && index < this.patterns.size() - 1)
		{
			Pattern p = this.patterns.remove(index);
			this.patterns.add(++index, p);
			this.list.setSelectedIndex(index);
			this.updateDisplay();
		} else if (e.getSource() == this.comboboxBackground) this.banner.setBackground(Pattern.COLORS[this.comboboxBackground.getSelectedIndex()]);
	}

	public int getBaseColor()
	{
		return this.comboboxBackground.getSelectedIndex();
	}

	public TagCompound[] getPatterns()
	{
		TagCompound[] tags = new TagCompound[this.patterns.size()];
		for (int i = 0; i < tags.length; ++i)
			tags[i] = this.patterns.get(i).toTag();
		return tags;
	}

	private void updateDisplay()
	{
		int index = this.list.getSelectedIndex();
		String[] values = new String[this.patterns.size()];
		for (int i = 0; i < values.length; ++i)
			values[i] = this.patterns.get(i).toString();
		this.list.setValues(values);
		this.list.setSelectedIndex(index == -1 ? 0 : (index >= values.length ? values.length - 1 : index));
		this.banner.repaint();
	}

}
