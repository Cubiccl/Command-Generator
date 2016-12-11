package fr.cubiccl.generator.gui.component.panel.gameobject;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

import fr.cubiccl.generator.CommandGenerator;
import fr.cubiccl.generator.gameobject.JsonMessage;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.tags.TagList;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gui.component.button.CGButton;
import fr.cubiccl.generator.gui.component.button.IconButton;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.utils.ConfirmPanel;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.IStateListener;

public class PanelListJsonMessage extends CGPanel implements ActionListener, IStateListener<ConfirmPanel>
{
	class PanelSingleMessage extends JPanel implements ActionListener
	{
		private static final long serialVersionUID = 349507741634702290L;

		private IconButton buttonRemove;
		private JsonMessage message;
		private PanelListJsonMessage parent;

		public PanelSingleMessage(JsonMessage message, PanelListJsonMessage parent)
		{
			super();
			this.message = message;
			this.parent = parent;
			this.add(this.message.displayInLabel());
			this.add(this.buttonRemove = new IconButton(new ImageIcon("resources/textures/gui/delete.png")));

			this.buttonRemove.addActionListener(this);
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{
			this.parent.deleteMessage(this.message);
		}

	}

	private static final long serialVersionUID = -2937152202773615069L;
	private CGButton buttonAdd;
	private boolean hasEvents;
	private ArrayList<JsonMessage> messages;
	private ArrayList<PanelSingleMessage> messagesPanels;

	private JPanel panelMessages;

	public PanelListJsonMessage()
	{
		this(true);
	}

	public PanelListJsonMessage(boolean hasEvents)
	{
		this("json.title");
		this.hasEvents = hasEvents;
		this.messages = new ArrayList<JsonMessage>();
		this.messagesPanels = new ArrayList<PanelSingleMessage>();
	}

	public PanelListJsonMessage(String titleID)
	{
		super(titleID);

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
		CommandGenerator.stateManager.setState(new ConfirmPanel("json.title", new PanelJsonMessage(this.hasEvents)), this);
	}

	public void deleteMessage(JsonMessage message)
	{
		int index = this.messages.indexOf(message);
		this.messagesPanels.get(index).setVisible(false);
		this.remove(this.messagesPanels.get(index));
		this.messages.remove(index);
		this.messagesPanels.remove(index);
	}

	public TagList generateMessage()
	{
		TagCompound[] values = new TagCompound[this.messages.size()];
		for (int i = 0; i < values.length; ++i)
			values[i] = this.messages.get(i).toTag(Tags.JSON_CONTAINER);

		return new TagList(Tags.JSON_LIST, values);
	}

	@Override
	public boolean shouldStateClose(ConfirmPanel panel)
	{
		JsonMessage message;

		try
		{
			message = ((PanelJsonMessage) panel.component).generateMessage();
		} catch (CommandGenerationException e)
		{
			CommandGenerator.report(e);
			return false;
		}

		PanelSingleMessage panelM = new PanelSingleMessage(message, this);
		this.messages.add(message);
		this.messagesPanels.add(panelM);
		this.panelMessages.add(panelM);
		return true;
	}
}
