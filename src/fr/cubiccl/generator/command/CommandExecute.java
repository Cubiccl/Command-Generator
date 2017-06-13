package fr.cubiccl.generator.command;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import fr.cubiccl.generator.CommandGenerator;
import fr.cubiccl.generator.gameobject.Coordinates;
import fr.cubiccl.generator.gameobject.PlacedBlock;
import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.gameobject.target.Target;
import fr.cubiccl.generator.gui.component.button.CGCheckBox;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelBlock;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelCoordinates;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelTarget;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;

public class CommandExecute extends Command implements ActionListener
{
	private CGCheckBox checkboxBlock;
	private PanelBlock panelBlock;
	private PanelCoordinates panelCoordinates, panelBlockCoordinates;
	private PanelTarget panelTarget;

	public CommandExecute()
	{
		super("execute", "execute <entity> <x> <y> <z> <command ...>\n"
				+ "execute <entity> <x> <y> <z> detect <x2> <y2> <z2> <block> <dataValue> <command ...>", -6);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		boolean block = this.isDetecting();
		this.panelBlock.setVisible(block);
		this.panelBlockCoordinates.setVisible(block);
		this.updateTranslations();
	}

	@Override
	public CGPanel createUI()
	{
		CGPanel panel = new CGPanel();
		GridBagConstraints gbc = panel.createGridBagLayout();

		panel.add(this.labelDescription(), gbc);
		++gbc.gridy;
		panel.add(this.panelTarget = new PanelTarget("execute.target", PanelTarget.ALL_ENTITIES), gbc);
		++gbc.gridy;
		panel.add(this.panelCoordinates = new PanelCoordinates("execute.coordinates"), gbc);
		++gbc.gridy;
		panel.add(this.checkboxBlock = new CGCheckBox("execute.detect"), gbc);
		++gbc.gridy;
		panel.add(this.panelBlock = new PanelBlock("execute.block"), gbc);
		++gbc.gridy;
		panel.add(this.panelBlockCoordinates = new PanelCoordinates("execute.coordinates.block"), gbc);

		this.panelCoordinates.setRelativeText(new Text("execute.relative"));
		this.panelBlock.setHasNBT(false);
		this.panelBlock.setVisible(false);
		this.panelBlockCoordinates.setVisible(false);
		this.checkboxBlock.addActionListener(this);

		this.panelTarget.addArgumentChangeListener(this);
		this.panelCoordinates.addArgumentChangeListener(this);
		this.panelBlock.addArgumentChangeListener(this);
		this.panelBlockCoordinates.addArgumentChangeListener(this);

		return panel;
	}

	@Override
	protected Text description()
	{
		Text d = this.defaultDescription();
		if (this.isDetecting())
		{
			d = new Text("command." + this.id + ".detect");
			d.addReplacement("<block>", this.panelBlock.selectedBlock().name(this.panelBlock.selectedDamage()));
			d.addReplacement("<coordinates>", this.panelBlockCoordinates.displayCoordinates());
		}
		d.addReplacement("<target>", this.panelTarget.displayTarget());
		return d;
	}

	@Override
	public String generate() throws CommandGenerationException
	{
		String command = this.id + " " + this.panelTarget.generate().toCommand() + " " + this.panelCoordinates.generate().toCommand() + " ";
		if (this.isDetecting())
		{
			PlacedBlock block = this.panelBlock.generate();
			command += "detect " + this.panelBlockCoordinates.generate().toCommand() + " " + block.getBlock().id() + " " + block.getData() + " ";
		}
		return command;
	}

	private boolean isDetecting()
	{
		return this.checkboxBlock.isSelected();
	}

	@Override
	protected void readArgument(int index, String argument, String[] fullCommand) throws CommandGenerationException
	{
		// execute <entity> <x> <y> <z> <command ...>
		// execute <entity> <x> <y> <z> detect <x2> <y2> <z2> <block> <dataValue> <command ...>
		if (index == 1) this.panelTarget.setupFrom(new Target().fromString(argument));
		if (index == 2) this.panelCoordinates.setupFrom(new Coordinates().fromString(argument, fullCommand[3], fullCommand[4]));
		if (index == 5)
		{
			boolean block = argument.startsWith("detect ");
			this.checkboxBlock.setSelected(block);
			this.panelBlock.setVisible(block);
			this.panelBlockCoordinates.setVisible(block);
			if (block)
			{
				String[] args = argument.split(" ");
				if (args.length < 7) this.incorrectStructureError();
				this.panelBlockCoordinates.setupFrom(new Coordinates().fromString(args[1], args[2], args[3]));
				this.panelBlock.setBlock(ObjectRegistry.blocks.find(args[4]));
				try
				{
					this.panelBlock.setData(Integer.parseInt(args[5]));
				} catch (Exception e)
				{}
				CommandGenerator.setExecuteInput(argument.substring(args[0].length() + args[1].length() + args[2].length() + args[3].length()
						+ args[4].length() + args[5].length() + 6)); // 6 for each space
			} else CommandGenerator.setExecuteInput(argument);
		}
	}
}
