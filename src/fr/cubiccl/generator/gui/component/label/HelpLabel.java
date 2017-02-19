package fr.cubiccl.generator.gui.component.label;

import fr.cubiccl.generator.gui.component.interfaces.ITranslated;
import fr.cubiccl.generator.utils.Text;
import fr.cubiccl.generator.utils.Textures;

public class HelpLabel extends ImageLabel implements ITranslated
{
	private static final long serialVersionUID = -8291853753633429133L;

	private Text text;

	public HelpLabel()
	{
		this((Text) null);
	}

	public HelpLabel(String textID)
	{
		this(textID == null ? null : new Text(textID));
	}

	public HelpLabel(Text text)
	{
		super(Textures.getTexture("gui/help"));
		this.setText(text);
	}

	public void setText(Text text)
	{
		this.text = text;
		this.updateTranslations();
	}

	public void setTextID(String textID)
	{
		this.setText(new Text(textID));
	}
	
	@Override
	public void updateTranslations()
	{
		this.setToolTipText(this.text == null ? null : "<html>" + this.text.toString() + "</html>");
	}

}
