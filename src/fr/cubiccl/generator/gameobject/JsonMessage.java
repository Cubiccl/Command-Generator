package fr.cubiccl.generator.gameobject;

import static fr.cubiccl.generator.gameobject.templatetags.Tags.*;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.JLabel;

import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.tags.TagString;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;
import fr.cubiccl.generator.utils.Utils;

public class JsonMessage
{
	public static final Color[] LABEL_COLOR =
	{ Color.WHITE, new Color(85, 255, 255), Color.BLACK, new Color(85, 85, 255), new Color(0, 170, 170), new Color(0, 0, 170), new Color(85, 85, 85),
			new Color(0, 170, 0), new Color(170, 0, 170), new Color(170, 0, 0), new Color(255, 170, 0), new Color(170, 170, 170), new Color(85, 255, 85),
			new Color(255, 85, 255), new Color(255, 85, 85), new Color(255, 255, 85) };
	public static final byte TEXT = 0, TRANSLATE = 1, SCORE = 2, SELECTOR = 3;

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

	public JsonMessage(byte mode, String text, int color)
	{
		this.mode = mode;
		this.text = text;
		this.color = color;
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

	public TagCompound toTag(TemplateCompound container)
	{
		ArrayList<Tag> tags = new ArrayList<Tag>();

		switch (this.mode)
		{
			case TRANSLATE:
				tags.add(new TagString(JSON_TRANSLATE, this.text));
				break;

			case SCORE:
				tags.add(new TagCompound(JSON_SCORE, new TagString(SCORE_OBJECTIVE, this.text), new TagString(SCORE_TARGET, this.target)));
				break;

			case SELECTOR:
				tags.add(new TagString(JSON_SELECTOR, this.text));
				break;

			default:
				tags.add(new TagString(JSON_TEXT, this.text));
				break;
		}

		tags.add(new TagString(Tags.TEXT_COLOR, Utils.COLORS[this.color]));
		if (this.bold) tags.add(new TagString(TEXT_BOLD, "true"));
		if (this.underlined) tags.add(new TagString(TEXT_UNDERLINED, "true"));
		if (this.italic) tags.add(new TagString(TEXT_ITALIC, "true"));
		if (this.strikethrough) tags.add(new TagString(TEXT_STRIKETHROUGH, "true"));
		if (this.obfuscated) tags.add(new TagString(TEXT_OBFUSCATED, "true"));
		if (this.insertion != null) tags.add(new TagString(TEXT_INSERTION, this.insertion));
		if (this.clickAction != null) tags.add(new TagCompound(EVENT_CLICK, new TagString(EVENT_ACTION, this.clickAction), new TagString(EVENT_VALUE,
				this.clickValue)));
		if (this.hoverAction != null) tags.add(new TagCompound(EVENT_HOVER, new TagString(EVENT_ACTION, this.hoverAction), new TagString(EVENT_VALUE,
				this.hoverValue)));

		TagCompound tag = new TagCompound(container, tags.toArray(new Tag[tags.size()]));
		tag.setJson(true);
		return tag;
	}

}
