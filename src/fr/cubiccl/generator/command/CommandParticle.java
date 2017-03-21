package fr.cubiccl.generator.command;

import java.awt.GridBagConstraints;

import fr.cubiccl.generator.gameobject.Coordinates;
import fr.cubiccl.generator.gameobject.baseobjects.Particle;
import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.gameobject.target.Target;
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
		super("particle", "particle <name> <x> <y> <z> <xd> <yd> <zd> <speed> [count] [mode] [player] [params ...]", 9, 10, 11, 12, 13, 14);
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
	protected void defaultGui()
	{
		this.entryCount.setText("1");
		this.checkboxForce.setSelected(false);
		this.panelParticle.setParam1(0);
		this.panelParticle.setParam2(0);
	}

	@Override
	protected Text description()
	{
		return this.defaultDescription().addReplacement("<particle>", this.panelParticle.selectedParticle().name())
				.addReplacement("<coordinates>", this.panelCoordinates.displayCoordinates());
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
		this.entryCount.checkValueSuperior(CGEntry.INTEGER, 0);

		xd = Float.toString(Float.parseFloat(xd) / 8);
		yd = Float.toString(Float.parseFloat(yd) / 8);
		zd = Float.toString(Float.parseFloat(zd) / 8);

		String command = this.id + " " + particle.id + " " + this.panelCoordinates.generate().toCommand() + " " + xd + " " + yd + " " + zd + " " + speed + " "
				+ count + " ";
		if (this.checkboxForce.isSelected()) command += "force";
		else command += "normal";
		command += " " + this.panelTarget.generate().toCommand();

		if (particle.id.equals("minecraft:iconcrack")) command += " " + this.panelParticle.generateParam1() + " " + this.panelParticle.generateParam2();
		if (particle.id.equals("minecraft:blockcrack") || particle.id.equals("minecraft:blockdust") || particle.id.equals("minecraft:fallingdust")) command += " "
				+ this.panelParticle.generateParam1();

		return command;
	}

	@Override
	protected void readArgument(int index, String argument, String[] fullCommand) throws CommandGenerationException
	{
		// particle <name> <x> <y> <z> <xd> <yd> <zd> <speed> [count] [mode] [player] [params ...]
		if (index == 1) this.panelParticle.setParticle(ObjectRegistry.particles.find(argument));
		if (index == 2) this.panelCoordinates.setupFrom(Coordinates.createFrom(argument, fullCommand[3], fullCommand[4]));
		if (index >= 5 && index <= 8) try
		{
			if (Float.parseFloat(argument) >= 0) if (index == 5) this.entryXd.setText(argument);
			else if (index == 6) this.entryYd.setText(argument);
			else if (index == 7) this.entryZd.setText(argument);
			else if (index == 8) this.entrySpeed.setText(argument);
		} catch (Exception e)
		{}
		if (index == 9) try
		{
			if (Integer.parseInt(argument) >= 0) this.entryCount.setText(argument);
		} catch (Exception e)
		{}
		if (index == 10) this.checkboxForce.setSelected(argument.equals("force"));
		if (index == 11) this.panelTarget.setupFrom(Target.createFrom(argument));
		if (index == 12) try
		{
			this.panelParticle.setParam1(Integer.parseInt(argument));
		} catch (Exception e)
		{}
		if (index == 13) try
		{
			this.panelParticle.setParam2(Integer.parseInt(argument));
		} catch (Exception e)
		{}
	}

}
