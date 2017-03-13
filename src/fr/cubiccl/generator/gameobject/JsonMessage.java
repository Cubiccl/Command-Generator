package fr.cubiccl.generator.gameobject;

import static fr.cubiccl.generator.gameobject.templatetags.Tags.*;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;

import javax.swing.JLabel;

import org.jdom2.Element;

import fr.cubiccl.generator.gameobject.tags.NBTReader;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;
import fr.cubiccl.generator.gui.component.interfaces.IObjectList;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelJsonMessage;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelListJsonMessage.PanelSingleMessage;
import fr.cubiccl.generator.gui.component.panel.utils.ListProperties;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Utils;

public class JsonMessage extends GameObject implements IObjectList<JsonMessage>
{
	public static final Color[] LABEL_COLOR =
	{ Color.WHITE, new Color(85, 255, 255), Color.BLACK, new Color(85, 85, 255), new Color(0, 170, 170), new Color(0, 0, 170), new Color(85, 85, 85),
			new Color(0, 170, 0), new Color(170, 0, 170), new Color(170, 0, 0), new Color(255, 170, 0), new Color(170, 170, 170), new Color(85, 255, 85),
			new Color(255, 85, 255), new Color(255, 85, 85), new Color(255, 255, 85) };
	public static final byte TEXT = 0, TRANSLATE = 1, SCORE = 2, SELECTOR = 3;

	public static JsonMessage createFrom(Element json)
	{
		JsonMessage message = createFrom((TagCompound) NBTReader.read(json.getChildText("message"), true, false, true));
		message.findProperties(json);
		return message;
	}

	public static JsonMessage createFrom(TagCompound tag)
	{
		JsonMessage message = new JsonMessage(TEXT, "", 0);
		for (Tag t : tag.value())
		{
			if (t.id().equals(JSON_TRANSLATE.id()))
			{
				message.mode = TRANSLATE;
				message.text = (String) t.value();
			}

			if (t.id().equals(JSON_SCORE.id()))
			{
				message.mode = SCORE;
				message.text = (String) ((TagCompound) t).getTagFromId(SCORE_OBJECTIVE.id()).value();
				message.target = (String) ((TagCompound) t).getTagFromId(SCORE_TARGET.id()).value();
			}

			if (t.id().equals(JSON_SELECTOR.id()))
			{}

			if (t.id().equals(JSON_SELECTOR.id()))
			{
				message.mode = SELECTOR;
				message.text = (String) t.value();
			}

			if (t.id().equals(JSON_TEXT.id()))
			{
				message.mode = TEXT;
				message.text = (String) t.value();
			}

			if (t.id().equals(TEXT_COLOR.id())) message.color = getColorID((String) t.value());
			if (t.id().equals(TEXT_BOLD.id())) message.bold = t.value().equals("true");
			if (t.id().equals(TEXT_UNDERLINED.id())) message.underlined = t.value().equals("true");
			if (t.id().equals(TEXT_ITALIC.id())) message.italic = t.value().equals("true");
			if (t.id().equals(TEXT_STRIKETHROUGH.id())) message.strikethrough = t.value().equals("true");
			if (t.id().equals(TEXT_OBFUSCATED.id())) message.obfuscated = t.value().equals("true");
			if (t.id().equals(TEXT_INSERTION.id())) message.insertion = (String) t.value();

			if (t.id().equals(EVENT_CLICK.id()))
			{
				message.clickAction = (String) ((TagCompound) t).getTagFromId(EVENT_ACTION.id()).value();
				message.clickValue = (String) ((TagCompound) t).getTagFromId(EVENT_VALUE.id()).value();
			}
			if (t.id().equals(EVENT_HOVER.id()))
			{
				message.hoverAction = (String) ((TagCompound) t).getTagFromId(EVENT_ACTION.id()).value();
				message.hoverValue = (String) ((TagCompound) t).getTagFromId(EVENT_VALUE.id()).value();
			}
		}
		message.findName(tag);
		return message;
	}

	private static int getColorID(String value)
	{
		for (int i = 0; i < Utils.COLORS.length; ++i)
			if (Utils.COLORS[i].equals(value)) return i;
		return 0;
	}

	public boolean bold, underlined, italic, strikethrough, obfuscated;
	public String clickAction, clickValue;
	public int color;
	public String hoverAction, hoverValue;
	public String insertion;
	public byte mode;
	public String target;
	public String text;

	public JsonMessage()
	{
		this((byte) 0, "", 0);
	}

	public JsonMessage(byte mode, String text, int color)
	{
		this.mode = mode;
		this.text = text;
		this.color = color;
	}

	@Override
	public CGPanel createPanel(ListProperties properties)
	{
		PanelJsonMessage p = new PanelJsonMessage(true, properties.hasCustomObjects());
		p.setupFrom(this);
		return p;
	}

	public JLabel displayInLabel(JLabel label)
	{
		String display = this.text;
		if (this.obfuscated) display = "0bfUsC4t3D";
		if (this.bold) display = "<b>" + display + "</b>";
		if (this.underlined) display = "<u>" + display + "</u>";
		if (this.italic) display = "<i>" + display + "</i>";
		if (this.strikethrough) display = "<strike>" + display + "</strike>";
		if (this.insertion != null) display += "<br />Inserts: " + this.insertion;

		label.setText("<html>" + display + "</htlm>");
		label.setForeground(LABEL_COLOR[this.color]);
		label.setBackground(Color.GRAY);
		label.setOpaque(true);

		return label;
	}

	@Override
	public Component getDisplayComponent()
	{
		return new PanelSingleMessage(this, 0, null);
	}

	@Override
	public String getName(int index)
	{
		return this.customName() != null && !this.customName().equals("") ? this.customName() : this.text;
	}

	public void setClick(String clickAction, String clickValue)
	{
		this.clickAction = clickAction;
		this.clickValue = clickValue;
	}

	public void setHover(String hoverAction, String hoverValue)
	{
		this.hoverAction = hoverAction;
		this.hoverValue = hoverValue;
	}

	@Override
	public String toCommand()
	{
		return this.toString();
	}

	@Override
	public String toString()
	{
		return this.toTag(DEFAULT_COMPOUND).valueForCommand();
	}

	public TagCompound toTag(TemplateCompound container)
	{
		ArrayList<Tag> tags = new ArrayList<Tag>();

		switch (this.mode)
		{
			case TRANSLATE:
				tags.add(JSON_TRANSLATE.create(this.text));
				break;

			case SCORE:
				tags.add(JSON_SCORE.create(SCORE_OBJECTIVE.create(this.text), SCORE_TARGET.create(this.target)));
				break;

			case SELECTOR:
				tags.add(JSON_SELECTOR.create(this.text));
				break;

			default:
				tags.add(JSON_TEXT.create(this.text));
				break;
		}

		tags.add(TEXT_COLOR.create(Utils.COLORS[this.color]));
		if (this.bold) tags.add(TEXT_BOLD.create("true"));
		if (this.underlined) tags.add(TEXT_UNDERLINED.create("true"));
		if (this.italic) tags.add(TEXT_ITALIC.create("true"));
		if (this.strikethrough) tags.add(TEXT_STRIKETHROUGH.create("true"));
		if (this.obfuscated) tags.add(TEXT_OBFUSCATED.create("true"));
		if (this.insertion != null) tags.add(TEXT_INSERTION.create(this.insertion));
		if (this.clickAction != null) tags.add(EVENT_CLICK.create(EVENT_ACTION.create(this.clickAction), EVENT_VALUE.create(this.clickValue)));
		if (this.hoverAction != null) tags.add(EVENT_HOVER.create(EVENT_ACTION.create(this.hoverAction), EVENT_VALUE.create(this.hoverValue)));

		TagCompound tag = container.create(tags.toArray(new Tag[tags.size()]));
		tag.setJson(true);
		return tag;
	}

	@Override
	public Element toXML()
	{
		return this.createRoot("json").addContent(new Element("message").setText(this.toTag(Tags.DEFAULT_COMPOUND).valueForCommand()));
	}

	@Override
	public JsonMessage update(CGPanel panel) throws CommandGenerationException
	{
		JsonMessage m = ((PanelJsonMessage) panel).generate();
		this.bold = m.bold;
		this.underlined = m.underlined;
		this.italic = m.italic;
		this.strikethrough = m.strikethrough;
		this.obfuscated = m.obfuscated;
		this.clickAction = m.clickAction;
		this.clickValue = m.clickValue;
		this.color = m.color;
		this.hoverAction = m.hoverAction;
		this.hoverValue = m.hoverValue;
		this.insertion = m.insertion;
		this.mode = m.mode;
		this.target = m.target;
		this.text = m.text;
		return this;
	}
}
