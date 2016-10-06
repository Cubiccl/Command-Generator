package fr.cubiccl.generator.gameobject;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.JLabel;

import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.tags.TagString;
import fr.cubiccl.generator.utils.Utils;

public class JsonMessage
{
	public static final Color[] LABEL_COLOR =
	{ Color.WHITE, new Color(85, 255, 255), Color.BLACK, new Color(85, 85, 255), new Color(0, 170, 170), new Color(0, 0, 170), new Color(85, 85, 85),
			new Color(0, 170, 0), new Color(170, 0, 170), new Color(170, 0, 0), new Color(255, 170, 0), new Color(170, 170, 170), new Color(85, 255, 85),
			new Color(255, 85, 255), new Color(255, 85, 85), new Color(255, 255, 85) };
	public static final int TEXT = 0, TRANSLATE = 1, SCORE = 2, SELECTOR = 3;

	public boolean bold, underlined, italic, strikethrough, obfuscated;
	private String clickAction, clickValue;
	private int color;
	private String hoverAction, hoverValue;
	public String insertion;
	public final int mode;
	public String target;
	private String text;

	public JsonMessage(int mode, String text, int color)
	{
		this.mode = mode;
		this.text = text;
		this.color = color;
	}

	public JLabel displayInLabel()
	{
		String display = this.text;
		if (this.obfuscated) display = "0bfUsC4t3D";
		if (this.bold) display = "<b>" + display + "</b>";
		if (this.underlined) display = "<u>" + display + "</u>";
		if (this.italic) display = "<i>" + display + "</i>";
		if (this.strikethrough) display = "<strike>" + display + "</strike>";
		if (this.insertion != null) display += "<br />Inserts: " + this.insertion;

		JLabel label = new JLabel("<html>" + display + "</htlm>");
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

	public TagCompound toTag(String id)
	{
		ArrayList<Tag> tags = new ArrayList<Tag>();

		switch (this.mode)
		{
			case TRANSLATE:
				tags.add(new TagString("translate", this.text));
				break;

			case SCORE:
				tags.add(new TagCompound("score", new TagString("objective", this.text), new TagString("name", this.target)));
				break;

			case SELECTOR:
				tags.add(new TagString("selector", this.text));
				break;

			default:
				tags.add(new TagString("text", this.text));
				break;
		}

		tags.add(new TagString("color", Utils.COLORS[this.color]));
		if (this.bold) tags.add(new TagString("bold", "true"));
		if (this.underlined) tags.add(new TagString("underlined", "true"));
		if (this.italic) tags.add(new TagString("italic", "true"));
		if (this.strikethrough) tags.add(new TagString("strikethrough", "true"));
		if (this.obfuscated) tags.add(new TagString("obfuscated", "true"));
		if (this.insertion != null) tags.add(new TagString("insertion", this.insertion));
		if (this.clickAction != null) tags
				.add(new TagCompound("ClickEvent", new TagString("action", this.clickAction), new TagString("value", this.clickValue)));
		if (this.hoverAction != null) tags
				.add(new TagCompound("HoverEvent", new TagString("action", this.hoverAction), new TagString("value", this.hoverValue)));

		TagCompound tag = new TagCompound(id, tags.toArray(new Tag[tags.size()]));
		tag.setJson(true);
		return tag;
	}

}
