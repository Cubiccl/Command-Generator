package fr.cubiccl.generator.gui.component.panel;

import java.awt.Component;
import java.util.HashMap;

import javax.swing.BorderFactory;

import fr.cubi.cubigui.CPanel;
import fr.cubiccl.generator.gui.component.interfaces.ITranslated;
import fr.cubiccl.generator.utils.Lang;
import fr.cubiccl.generator.utils.Text;

public class CGPanel extends CPanel implements ITranslated
{
	private static final long serialVersionUID = 1L;

	private Text title;
	private String titleID;
	private HashMap<Component, Boolean> wasEnabled = new HashMap<Component, Boolean>();

	public CGPanel()
	{
		this(null);
	}

	public CGPanel(String titleID)
	{
		this.titleID = titleID;
		this.updateTranslations();
	}

	public Text getStateName()
	{
		return this.title;
	}

	@Override
	public void setEnabled(boolean enabled)
	{
		for (Component component : this.getComponents())
			if (!enabled)
			{
				this.wasEnabled.put(component, component.isEnabled());
				component.setEnabled(false);
			} else if (this.wasEnabled.containsKey(component)) component.setEnabled(this.wasEnabled.get(component));
			else component.setEnabled(enabled);
	}

	public void setName(String nameID)
	{
		this.setName(new Text(nameID));
	}

	public void setName(Text name)
	{
		this.title = name;
	}

	public void setTitle(String titleID)
	{
		this.titleID = titleID;
		this.updateTranslations();
	}

	@Override
	public void updateTranslations()
	{
		if (this.titleID != null) this.setBorder(BorderFactory.createTitledBorder(Lang.translate(this.titleID)));
		for (Component component : this.getComponents())
			if (component instanceof ITranslated) ((ITranslated) component).updateTranslations();
		this.repaint();
	}

}
