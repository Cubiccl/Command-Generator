package fr.cubiccl.generator.gameobject.tags;

import org.jdom2.Element;

import fr.cubiccl.generator.gameobject.GameObject;
import fr.cubiccl.generator.gameobject.templatetags.*;

/** Represents an instantiated NBT Tag. */
public abstract class Tag extends GameObject
{
	/** Identifiers for Application types. <br />
	 * <br />
	 * <table border="1">
	 * <tr>
	 * <td>ID</td>
	 * <td>Variable</td>
	 * <td>Mode</td>
	 * </tr>
	 * <tr>
	 * <td>0</td>
	 * <td>BLOCK</td>
	 * <td>Tile Entity NBT Tag</td>
	 * </tr>
	 * <tr>
	 * <td>1</td>
	 * <td>ITEM</td>
	 * <td>Item NBT Tag</td>
	 * </tr>
	 * <tr>
	 * <td>2</td>
	 * <td>ENTITY</td>
	 * <td>Entity NBT Tag</td>
	 * </tr>
	 * <tr>
	 * <td>3</td>
	 * <td>UNAVAILABLE</td>
	 * <td>Are not saved.</td>
	 * </tr>
	 * <tr>
	 * <td>4</td>
	 * <td>UNKNOWN</td>
	 * <td>Are not registered.</td>
	 * </tr>
	 * </table>
	 * <br />
	 * <code>UNAVAILABLE</code> Tags can be read by the {@link NBTParser} but <code>UNKNOWN</code> tags can't. <code>UNKNOWN</code> tags are used for NBT Tags with variable IDs, for example in the Recipe book. */
	public static final byte BLOCK = 0, ITEM = 1, ENTITY = 2, UNAVAILABLE = 3, UNKNOWN = 4;
	/** Indentation for NBT formatting. */
	protected static final String INDENT = "    ";
	/** Identifiers for Tag types. <br />
	 * <br />
	 * <table border="1">
	 * <tr>
	 * <td>ID</td>
	 * <td>Variable</td>
	 * <td>Mode</td>
	 * </tr>
	 * <tr>
	 * <td>0</td>
	 * <td>STRING</td>
	 * <td>{@link TemplateString String}</td>
	 * </tr>
	 * <tr>
	 * <td>1</td>
	 * <td>BYTE</td>
	 * <td>{@link TemplateNumber Byte} (-128 - 127)</td>
	 * </tr>
	 * <tr>
	 * <td>2</td>
	 * <td>SHORT</td>
	 * <td>{@link TemplateNumber Short} (-32768 - 32767)</td>
	 * </tr>
	 * <tr>
	 * <td>3</td>
	 * <td>INT</td>
	 * <td>{@link TemplateNumber Integer} (0x80000000 - 0x7fffffff)</td>
	 * </tr>
	 * <tr>
	 * <td>4</td>
	 * <td>LONG</td>
	 * <td>{@link TemplateNumber Long} (0x8000000000000000 - 0x7fffffffffffffff)</td>
	 * </tr>
	 * <tr>
	 * <td>5</td>
	 * <td>FLOAT</td>
	 * <td>{@link TemplateNumber Float} (-3.4028235e+38f - 3.4028235e+38f)</td>
	 * </tr>
	 * <tr>
	 * <td>6</td>
	 * <td>DOUBLE</td>
	 * <td>{@link TemplateNumber Double} (-1.7976931348623157e+308 - 1.7976931348623157e+308)</td>
	 * </tr>
	 * <tr>
	 * <td>7</td>
	 * <td>LIST</td>
	 * <td>{@link TemplateList List} (contains ordered unnamed Tags)</td>
	 * </tr>
	 * <tr>
	 * <td>8</td>
	 * <td>COMPOUND</td>
	 * <td>{@link TemplateCompound Compound} (contains unordered named Tags)</td>
	 * </tr>
	 * <tr>
	 * <td>9</td>
	 * <td>Boolean</td>
	 * <td>{@link TemplateBoolean Boolean} (<code>true</code> of <code>false</code>)</td>
	 * </tr>
	 * <tr>
	 * <td>10</td>
	 * <td>RANGE</td>
	 * <td>{@link TemplateRange Range} (either number or compound)</td>
	 * </tr>
	 * </table>
	 * <br />
	 * <code>UNAVAILABLE</code> Tags can be read by the {@link NBTParser} but <code>UNKNOWN</code> tags can't. <code>UNKNOWN</code> tags are used for NBT Tags with variable IDs, for example in the Recipe book. */
	public static final byte STRING = 0, BYTE = 1, SHORT = 2, INT = 3, LONG = 4, FLOAT = 5, DOUBLE = 6, LIST = 7, COMPOUND = 8, BOOLEAN = 9, RANGE = 10;

	/** <code>true</code> if this Tag should be generated with strict JSON format. */
	protected boolean isJson;
	/** The Template used for this Tag. */
	public final TemplateTag template;

	public Tag(TemplateTag template)
	{
		this.template = template;
	}

	/** @return This Tag's ID. */
	public String id()
	{
		return this.template.id();
	}

	/** Setter for {@link Tag#isJson}.
	 * 
	 * @return This tag. */
	public Tag setJson(boolean isJson)
	{
		this.isJson = isJson;
		return this;
	}

	/** No indentation. */
	@Override
	@Deprecated
	public String toCommand()
	{
		return this.toCommand(-1);
	}

	/** @param indent - The current indentation. -1 if no indentation.
	 * @return This Tag in Command format. */
	public String toCommand(int indent)
	{
		String display = "";
		if (indent != -1) for (int i = 0; i < indent; ++i)
			display += INDENT;

		if (this.isJson) display += "\"";
		display += this.id();
		if (this.isJson) display += "\"";
		display += ":" + this.valueForCommand(indent);
		return display;
	}

	@Override
	public String toString()
	{
		return this.toCommand();
	}

	@Override
	@Deprecated
	public TagCompound toTag(TemplateCompound container)
	{
		return null;
	}

	@Override
	@Deprecated
	public Element toXML()
	{
		return null;
	}

	/** @return This Tag's type. */
	public int type()
	{
		return this.template.applicationType;
	}

	/** @return This Tag's value. */
	public abstract Object value();

	/** @return This Tag in Command format, without the ID. */
	public String valueForCommand()
	{
		return this.valueForCommand(-1);
	}

	/** @param indent - Current indentation for Json formatting.
	 * @return This Tag in Command format, without the ID. */
	public abstract String valueForCommand(int indent);

}
