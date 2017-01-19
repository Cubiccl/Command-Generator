package fr.cubiccl.generator.command;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import fr.cubiccl.generator.gameobject.Particle;
import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.gui.component.button.CGCheckBox;
import fr.cubiccl.generator.gui.component.combobox.ObjectCombobox;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelBlock;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelCoordinates;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelItem;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelTarget;
import fr.cubiccl.generator.gui.component.textfield.CGEntry;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;

public class CommandParticle extends Command implements ActionListener
{
	private CGCheckBox checkboxForce;
	private ObjectCombobox<Particle> comboboxParticle;
	private CGEntry entryXd, entryYd, entryZd, entrySpeed, entryCount;
	private CGLabel labelParticle;
	private PanelBlock panelBlockParticle;
	private PanelCoordinates panelCoordinates;
	private PanelItem panelItemParticle;
	private PanelTarget panelTarget;

	public CommandParticle()
	{
		super("particle");
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		Particle particle = this.comboboxParticle.getSelectedObject();
		this.panelBlockParticle.setVisible(particle.id.equals("blockcrack") || particle.id.equals("blockdust") || particle.id.equals("fallingdust"));
		this.panelItemParticle.setVisible(particle.id.equals("iconcrack"));
		this.labelParticle.setTextID(new Text("particle." + particle.id));
	}

	@Override
	public CGPanel createGUI()
	{
		CGPanel panel = new CGPanel();
		GridBagConstraints gbc = panel.createGridBagLayout();

		++gbc.gridwidth;
		gbc.fill = GridBagConstraints.NONE;
		panel.add(this.labelDescription(), gbc);
		++gbc.gridy;
		panel.add((this.comboboxParticle = new ObjectCombobox<Particle>(ObjectRegistry.particles.list())).container, gbc);
		++gbc.gridy;
		panel.add(this.labelParticle = new CGLabel("particle." + this.comboboxParticle.getSelectedObject().id), gbc);
		++gbc.gridy;
		panel.add(this.panelCoordinates = new PanelCoordinates("particle.coordinates"), gbc);
		++gbc.gridy;
		--gbc.gridwidth;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		panel.add((this.entryXd = new CGEntry(new Text("particle.xd"), "0", Text.NUMBER)).container, gbc);
		++gbc.gridx;
		panel.add((this.entrySpeed = new CGEntry(new Text("particle.speed"), "0", Text.INTEGER)).container, gbc);
		--gbc.gridx;
		++gbc.gridy;
		panel.add((this.entryYd = new CGEntry(new Text("particle.yd"), "0", Text.NUMBER)).container, gbc);
		++gbc.gridx;
		panel.add((this.entryCount = new CGEntry(new Text("particle.count"), "1", Text.INTEGER)).container, gbc);
		--gbc.gridx;
		++gbc.gridy;
		panel.add((this.entryZd = new CGEntry(new Text("particle.zd"), "0", Text.NUMBER)).container, gbc);
		++gbc.gridx;
		panel.add(this.checkboxForce = new CGCheckBox("particle.force"), gbc);
		--gbc.gridx;
		++gbc.gridy;
		++gbc.gridwidth;
		gbc.fill = GridBagConstraints.NONE;
		panel.add(this.panelTarget = new PanelTarget("particle.target", PanelTarget.PLAYERS_ONLY), gbc);
		++gbc.gridy;
		panel.add(this.panelBlockParticle = new PanelBlock("particle.block"), gbc);
		panel.add(this.panelItemParticle = new PanelItem("particle.item"), gbc);

		this.entryXd.addNumberFilter();
		this.entryYd.addNumberFilter();
		this.entryZd.addNumberFilter();
		this.entrySpeed.addIntFilter();
		this.entryCount.addIntFilter();
		this.panelBlockParticle.setVisible(false);
		this.panelItemParticle.setVisible(false);
		this.comboboxParticle.addActionListener(this);

		return panel;
	}

	@Override
	public String generate() throws CommandGenerationException
	{
		Particle particle = this.comboboxParticle.getSelectedObject();
		String xd = this.entryXd.getText(), yd = this.entryYd.getText(), zd = this.entryZd.getText(), speed = this.entrySpeed.getText(), count = this.entryCount
				.getText();
		this.entryXd.checkValueSuperior(CGEntry.FLOAT, 0);
		this.entryYd.checkValueSuperior(CGEntry.FLOAT, 0);
		this.entryZd.checkValueSuperior(CGEntry.FLOAT, 0);
		this.entrySpeed.checkValueSuperior(CGEntry.FLOAT, 0);
		this.entryCount.checkValueSuperior(CGEntry.INTEGER, 1);

		xd = Float.toString(Float.parseFloat(xd) / 8);
		yd = Float.toString(Float.parseFloat(yd) / 8);
		zd = Float.toString(Float.parseFloat(zd) / 8);

		String command = "/particle " + particle.id + " " + this.panelCoordinates.generateCoordinates().toCommand() + " " + xd + " " + yd + " " + zd + " "
				+ speed + " " + count + " ";
		if (this.checkboxForce.isSelected()) command += "force";
		else command += "normal";
		command += " " + this.panelTarget.generateTarget().toCommand();

		if (particle.id.equals("iconcrack")) command += " " + this.panelItemParticle.selectedItem().idInt + " " + this.panelItemParticle.selectedDamage();
		if (particle.id.equals("blockcrack") || particle.id.equals("blockdust") || particle.id.equals("fallingdust")) command += " "
				+ (this.panelBlockParticle.selectedBlock().idInt + 4096 * this.panelBlockParticle.selectedDamage());

		return command;
	}

}
