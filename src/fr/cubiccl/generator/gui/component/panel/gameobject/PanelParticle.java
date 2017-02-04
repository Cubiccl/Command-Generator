package fr.cubiccl.generator.gui.component.panel.gameobject;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import fr.cubiccl.generator.gameobject.ItemStack;
import fr.cubiccl.generator.gameobject.PlacedBlock;
import fr.cubiccl.generator.gameobject.baseobjects.Particle;
import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gui.component.combobox.ObjectCombobox;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.utils.Text;

public class PanelParticle extends CGPanel implements ActionListener
{
	private static final long serialVersionUID = 6583418140511741226L;

	private ObjectCombobox<Particle> comboboxParticle;
	private CGLabel labelParticle;
	private PanelBlock panelBlockParticle;
	private PanelItem panelItemParticle;

	public PanelParticle(String titleID)
	{
		super(titleID);
		GridBagConstraints gbc = this.createGridBagLayout();
		this.add((this.comboboxParticle = new ObjectCombobox<Particle>(ObjectRegistry.particles.list())).container, gbc);
		++gbc.gridy;
		this.add(this.labelParticle = new CGLabel("particle." + this.comboboxParticle.getSelectedObject().id), gbc);
		++gbc.gridy;
		this.add(this.panelBlockParticle = new PanelBlock("particle.block"), gbc);
		this.add(this.panelItemParticle = new PanelItem("particle.item"), gbc);

		this.panelBlockParticle.setHasNBT(false);
		this.panelItemParticle.setHasNBT(false);
		this.panelItemParticle.setHasAmount(false);
		this.panelItemParticle.setHasDurability(false);
		this.panelBlockParticle.setVisible(false);
		this.panelItemParticle.setVisible(false);
		this.comboboxParticle.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		Particle particle = this.comboboxParticle.getSelectedObject();
		this.panelBlockParticle.setVisible(particle.id.equals("minecraft:blockcrack") || particle.id.equals("minecraft:blockdust")
				|| particle.id.equals("minecraft:fallingdust"));
		this.panelItemParticle.setVisible(particle.id.equals("minecraft:iconcrack"));
		this.labelParticle.setTextID(new Text("particle." + particle.id));
	}

	public int generateParam1()
	{
		Particle p = this.selectedParticle();
		if (p.id.equals("minecraft:iconcrack")) return this.panelItemParticle.selectedItem().idNum();
		if (p.id.equals("minecraft:blockdust") || p.id.equals("minecraft:blockcrack") || p.id.equals("minecraft:fallingdust")) return this.panelBlockParticle
				.selectedBlock().idNum() + this.panelBlockParticle.selectedDamage() * 4096;
		return 0;
	}

	public int generateParam2()
	{
		return this.selectedParticle().id.equals("minecraft:iconcrack") ? this.panelItemParticle.selectedDamage() : 0;
	}

	public Particle selectedParticle()
	{
		return this.comboboxParticle.getSelectedObject();
	}

	public void setParam1(int param1)
	{
		int id = param1 % 4096;
		int damage = (param1 - id) / 4096;
		this.panelBlockParticle.setupFrom(new PlacedBlock(ObjectRegistry.blocks.find(id), damage, new TagCompound(Tags.BLOCK_NBT)));
		this.panelItemParticle.setupFrom(new ItemStack(ObjectRegistry.items.find(param1), 0, 0));
	}

	public void setParam2(int param2)
	{
		this.panelItemParticle.setDamage(param2);
	}

	public void setSelected(Particle particle)
	{
		this.comboboxParticle.setSelected(particle);
	}

	@Override
	public void updateTranslations()
	{
		super.updateTranslations();
		if (this.comboboxParticle != null) this.labelParticle.setTextID(new Text("particle." + this.selectedParticle().id));
	}

}
