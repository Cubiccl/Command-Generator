package fr.cubiccl.generator.command;

import java.awt.GridBagConstraints;

import fr.cubiccl.generator.gameobject.Particle;
import fr.cubiccl.generator.gui.component.button.CGCheckBox;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelCoordinates;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelParticle;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelTarget;
import fr.cubiccl.generator.gui.component.textfield.CGEntry;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;

public class CommandParticle extends Command
{
	private CGCheckBox checkboxForce;
	private CGEntry entryXd, entryYd, entryZd, entrySpeed, entryCount;
	private PanelCoordinates panelCoordinates;
	private PanelParticle panelParticle;
	private PanelTarget panelTarget;

	public CommandParticle()
	{
		super("particle");
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
		panel.add(this.panelParticle = new PanelParticle("particle.title"), gbc);
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

		this.entryXd.addNumberFilter();
		this.entryYd.addNumberFilter();
		this.entryZd.addNumberFilter();
		this.entrySpeed.addIntFilter();
		this.entryCount.addIntFilter();

		return panel;
	}

	@Override
	public String generate() throws CommandGenerationException
	{
		Particle particle = this.panelParticle.selectedParticle();
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

		if (particle.id.equals("minecraft:iconcrack")) command += " " + this.panelParticle.generateParam1() + " " + this.panelParticle.generateParam2();
		if (particle.id.equals("minecraft:blockcrack") || particle.id.equals("minecraft:blockdust") || particle.id.equals("minecraft:fallingdust")) command += " "
				+ this.panelParticle.generateParam1();

		return command;
	}

}
