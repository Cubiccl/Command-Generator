package fr.cubiccl.generator.gameobject;

import static fr.cubiccl.generator.gameobject.templatetags.Tags.*;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;

import javax.swing.JLabel;

import org.jdom2.Element;

import fr.cubiccl.generator.gameobject.tags.NBTParser;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.tags.TagString;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;
import fr.cubiccl.generator.gui.component.interfaces.IObjectList;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelJsonMessage;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelListJsonMessage.PanelSingleMessage;
import fr.cubiccl.generator.gui.component.panel.utils.ListProperties;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Utils;

/** Represents a Message in Json format. */
public class JsonMessage extends GameObject<JsonMessage> implements IObjectList<JsonMessage>
{
	/** Colors values for Json colors. */
	public static final Color[] LABEL_COLOR =
	{ Color.WHITE, new Color(85, 255, 255), Color.BLACK, new Color(85, 85, 255), new Color(0, 170, 170), new Color(0, 0, 170), new Color(85, 85, 85),
			new Color(0, 170, 0), new Color(170, 0, 170), new Color(170, 0, 0), new Color(255, 170, 0), new Color(170, 170, 170), new Color(85, 255, 85),
			new Color(255, 85, 255), new Color(255, 85, 85), new Color(255, 255, 85) };
	/** Identifiers for Json Message modes. <br />
	 * <br />
	 * <table border="1">
	 * <tr>
	 * <td>ID</td>
	 * <td>Variable</td>
	 * <td>Mode</td>
	 * </tr>
	 * <tr>
	 * <td>0</td>
	 * <td>TEXT</td>
	 * <td>Raw text</td>
	 * </tr>
	 * <tr>
	 * <td>1</td>
	 * <td>TRANSLATE</td>
	 * <td>ID of a text to translate</td>
	 * </tr>
	 * <tr>
	 * <td>2</td>
	 * <td>SCORE</td>
	 * <td>Scoreboard value</td>
	 * </tr>
	 * <tr>
	 * <td>3</td>
	 * <td>SELECTOR</td>
	 * <td>Entity selector</td>
	 * </tr>
	 * </table> */
	public static final byte TEXT = 0, TRANSLATE = 1, SCORE = 2, SELECTOR = 3;

	/** @param value - A Color as a string.
	 * @return The numerical ID of the Color. */
	private static int getColorID(String value)
	{
		for (int i = 0; i < Utils.COLORS.length; ++i)
			if (Utils.COLORS[i].equals(value)) return i;
		return 0;
	}

	/** <code>true</code> if this Message is in bold format. */
	public boolean bold;
	/** The type of action to execute when this Message is clicked on. */
	public String clickAction;
	/** The value of the action to execute when this Message is clicked on. */
	public String clickValue;
	/** This Message's Color. */
	public int color;
	/** The type of action to execute when this Message is hovered over. */
	public String hoverAction;
	/** The value of the action to execute when this Message is hovered over. */
	public Object hoverValue;
	/** The text to insert into the Player's chat box when this Message is clicked on. */
	public String insertion;
	/** <code>true</code> if this Message is in italic format. */
	public boolean italic;
	/** This Message's mode.
	 * 
	 * @see JsonMessage#TEXT */
	public byte mode;
	/** <code>true</code> if this Message is in obfuscated format. */
	public boolean obfuscated;
	/** <code>true</code> if this Message is in strikethrough format. */
	public boolean strikethrough;
	/** When mode is {@link JsonMessage#SELECTOR selector}, the target to display. <br />
	 * When mode is {@link JsonMessage#SCORE score}, the target whose score should be displayed. */
	public String target;
	/** When mode is {@link JsonMessage#TEXT text} or {@link JsonMessage#TRANSLATE translate}, the text to display/translate. <br />
	 * When mode is {@link JsonMessage#SCORE score}, the objective name. */
	public String text;
	/** <code>true</code> if this Message is in underlined format. */
	public boolean underlined;

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

	/** Displays this Message in a label.
	 * 
	 * @param label - The label to display into.
	 * @return The modified label. */
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
	public JsonMessage duplicate(JsonMessage object)
	{
		this.bold = object.bold;
		this.clickAction = object.clickAction;
		this.clickValue = object.clickValue;
		this.color = object.color;
		this.hoverAction = object.hoverAction;
		this.hoverValue = object.hoverValue;
		this.insertion = object.insertion;
		this.italic = object.italic;
		this.mode = object.mode;
		this.obfuscated = object.obfuscated;
		this.strikethrough = object.strikethrough;
		this.target = object.target;
		this.text = object.text;
		this.underlined = object.underlined;
		return this;
	}

	@Override
	public JsonMessage fromNBT(TagCompound nbt)
	{
		for (Tag t : nbt.value())
		{
			if (t.id().equals(JSON_TRANSLATE.id()))
			{
				this.mode = TRANSLATE;
				this.text = (String) t.value();
			}

			if (t.id().equals(JSON_SCORE.id()))
			{
				this.mode = SCORE;
				this.text = (String) ((TagCompound) t).getTagFromId(SCORE_OBJECTIVE.id()).value();
				this.target = (String) ((TagCompound) t).getTagFromId(SCORE_TARGET.id()).value();
			}

			if (t.id().equals(JSON_SELECTOR.id()))
			{}

			if (t.id().equals(JSON_SELECTOR.id()))
			{
				this.mode = SELECTOR;
				this.text = (String) t.value();
			}

			if (t.id().equals(JSON_TEXT.id()))
			{
				this.mode = TEXT;
				this.text = (String) t.value();
			}

			if (t.id().equals(TEXT_COLOR.id())) this.color = getColorID((String) t.value());
			if (t.id().equals(TEXT_BOLD.id())) this.bold = t.value().equals("true");
			if (t.id().equals(TEXT_UNDERLINED.id())) this.underlined = t.value().equals("true");
			if (t.id().equals(TEXT_ITALIC.id())) this.italic = t.value().equals("true");
			if (t.id().equals(TEXT_STRIKETHROUGH.id())) this.strikethrough = t.value().equals("true");
			if (t.id().equals(TEXT_OBFUSCATED.id())) this.obfuscated = t.value().equals("true");
			if (t.id().equals(TEXT_INSERTION.id())) this.insertion = (String) t.value();

			if (t.id().equals(EVENT_CLICK.id()))
			{
				this.clickAction = (String) ((TagCompound) t).getTagFromId(EVENT_ACTION.id()).value();
				this.clickValue = (String) ((TagCompound) t).getTagFromId(EVENT_VALUE.id()).value();
			}
			if (t.id().equals(EVENT_HOVER.id()))
			{
				this.hoverAction = (String) ((TagCompound) t).getTagFromId(EVENT_ACTION.id()).value();
				Tag value = ((TagCompound) t).getTagFromId(EVENT_VALUE.id());
				if (value instanceof TagString) this.hoverValue = ((TagString) value).value();
				else this.hoverValue = new JsonMessage().fromNBT((TagCompound) value);
			}
		}
		this.findName(nbt);
		return this;
	}

	@Override
	public JsonMessage fromXML(Element xml)
	{
		this.fromNBT((TagCompound) NBTParser.parse(xml.getChildText("message"), true, false, true));
		this.findProperties(xml);
		return this;
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

	/** Sets the Click event.
	 * 
	 * @param clickAction - see {@link JsonMessage#clickAction}.
	 * @param clickValue - see {@link JsonMessage#clickValue}. */
	public void setClick(String clickAction, String clickValue)
	{
		this.clickAction = clickAction;
		this.clickValue = clickValue;
	}

	/** Sets the Hover event.
	 * 
	 * @param hoverAction - see {@link JsonMessage#hoverAction}.
	 * @param hoverValue - see {@link JsonMessage#hoverValue}. */
	public void setHover(String hoverAction, Object hoverValue)
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
	public TagCompound toNBT(TemplateCompound container)
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
		if (this.hoverAction != null)
		{
			if (this.hoverValue instanceof String) tags.add(EVENT_HOVER.create(EVENT_ACTION.create(this.hoverAction),
					EVENT_VALUE.create((String) this.hoverValue)));
			else tags.add(EVENT_HOVER.create(EVENT_ACTION.create(this.hoverAction), ((JsonMessage) this.hoverValue).toNBT(Tags.EVENT_VALUE_JSON)));
		}

		TagCompound tag = container.create(tags.toArray(new Tag[tags.size()]));
		tag.setJson(true);
		return tag;
	}

	@Override
	public String toString()
	{
		return this.toNBT(DEFAULT_COMPOUND).valueForCommand();
	}

	@Override
	public Element toXML()
	{
		return this.createRoot("json").addContent(new Element("message").setText(this.toNBT(Tags.DEFAULT_COMPOUND).valueForCommand()));
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
