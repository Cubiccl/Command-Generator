package fr.cubiccl.generator.gui.component.panel.gameobject;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import fr.cubiccl.generator.CommandGenerator;
import fr.cubiccl.generator.gameobject.JsonMessage;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.tags.TagList;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateList;
import fr.cubiccl.generator.gui.component.button.CGButton;
import fr.cubiccl.generator.gui.component.button.IconButton;
import fr.cubiccl.generator.gui.component.interfaces.ITranslated;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.IStateListener;
import fr.cubiccl.generator.utils.Text;

public class PanelListJsonMessage extends CGPanel implements ActionListener, IStateListener<PanelJsonMessage>
{
	public static class PanelSingleMessage extends JPanel implements ActionListener, ITranslated
	{
		private static final long serialVersionUID = 349507741634702290L;

		private IconButton buttonEdit, buttonRemove;
		public int index;
		private JLabel labelMessage;
		private JsonMessage message;
		private PanelListJsonMessage parent;

		public PanelSingleMessage(JsonMessage message, int index, PanelListJsonMessage parent)
		{
			super();
			this.message = message;
			this.index = index;
			this.parent = parent;
			this.add(this.message.displayInLabel(this.labelMessage = new JLabel()));
			if (parent != null)
			{
				this.add(this.buttonEdit = new IconButton(new ImageIcon("resources/textures/gui/edit.png")));
				this.add(this.buttonRemove = new IconButton(new ImageIcon("resources/textures/gui/delete.png")));

				this.buttonEdit.addActionListener(this);
				this.buttonRemove.addActionListener(this);
			}
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{
			if (e.getSource() == this.buttonEdit) this.parent.editMessage(this.index);
			else if (e.getSource() == this.buttonRemove) this.parent.deleteMessage(this.index);
		}

		@Override
		public void updateTranslations()
		{
			this.message.displayInLabel(this.labelMessage);
		}

	}

	private static final long serialVersionUID = -2937152202773615069L;

	private CGButton buttonAdd;
	private int editing = -1;
	private boolean hasEvents;
	private ArrayList<JsonMessage> messages;
	private ArrayList<PanelSingleMessage> messagesPanels;
	private JPanel panelMessages;

	public PanelListJsonMessage()
	{
		this("json.title");
	}

	public PanelListJsonMessage(String titleID)
	{
		this(titleID, true);
	}

	public PanelListJsonMessage(String titleID, boolean hasEvents)
	{
		super(titleID);

		this.hasEvents = hasEvents;
		this.messages = new ArrayList<JsonMessage>();
		this.messagesPanels = new ArrayList<PanelSingleMessage>();

		GridBagConstraints gbc = this.createGridBagLayout();
		this.add(this.panelMessages = new JPanel(), gbc);
		++gbc.gridy;
		this.add(this.buttonAdd = new CGButton("json.add"), gbc);

		this.panelMessages.setLayout(new BoxLayout(this.panelMessages, BoxLayout.Y_AXIS));
		this.buttonAdd.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		this.editing = -1;
		PanelJsonMessage p = new PanelJsonMessage(this.hasEvents);
		p.setName(new Text("json.title"));
		CommandGenerator.stateManager.setState(p, this);
	}

	public void addMessage(JsonMessage message)
	{
		PanelSingleMessage panelM = new PanelSingleMessage(message, this.messages.size(), this);
		this.messages.add(message);
		this.messagesPanels.add(panelM);
		this.panelMessages.add(panelM);
	}

	public void clear()
	{
		while (this.messages.size() != 0)
			this.deleteMessage(0);
	}

	public void deleteMessage(int index)
	{
		this.messagesPanels.get(index).setVisible(false);
		this.remove(this.messagesPanels.get(index));
		this.messages.remove(index);
		this.messagesPanels.remove(index);

		for (int i = 0; i < this.messagesPanels.size(); ++i)
			this.messagesPanels.get(i).index = i;
	}

	public void editMessage(int index)
	{
		this.editing = index;
		PanelJsonMessage p = new PanelJsonMessage(this.hasEvents);
		p.setName(new Text("json.title"));
		p.setupFrom(this.messages.get(index));
		CommandGenerator.stateManager.setState(p, this);
	}

	public TagList generateMessage(TemplateList container)
	{
		TagCompound[] values = new TagCompound[this.messages.size()];
		for (int i = 0; i < values.length; ++i)
			values[i] = this.messages.get(i).toTag(Tags.DEFAULT_COMPOUND);

		return container.create(values);
	}

	public JsonMessage[] getMessages()
	{
		return this.messages.toArray(new JsonMessage[this.messages.size()]);
	}

	public void setupFrom(Tag t)
	{
		this.clear();
		if (t instanceof TagCompound) this.addMessage(JsonMessage.createFrom((TagCompound) t));
		else for (Tag tag : ((TagList) t).value())
			this.addMessage(JsonMessage.createFrom((TagCompound) tag));
	}

	@Override
	public boolean shouldStateClose(PanelJsonMessage panel)
	{
		JsonMessage message;

		try
		{
			message = panel.generate();
		} catch (CommandGenerationException e)
		{
			CommandGenerator.report(e);
			return false;
		}

		if (this.editing == -1) this.addMessage(message);
		else
		{
			this.messages.set(this.editing, message);
			this.messagesPanels.set(this.editing, new PanelSingleMessage(message, this.editing, this));
			this.panelMessages.removeAll();
			for (PanelSingleMessage p : this.messagesPanels)
				this.panelMessages.add(p);
		}
		return true;
	}
}
