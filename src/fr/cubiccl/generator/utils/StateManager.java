package fr.cubiccl.generator.utils;

import java.util.Stack;

import fr.cubiccl.generator.CommandGenerator;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.utils.ConfirmPanel;

@SuppressWarnings("rawtypes")
public class StateManager
{
	public class State<T extends CGPanel>
	{
		public final boolean isConfirmIncluded;
		public final T panel;
		private final IStateListener<T> stateListener;

		public State(T panel, IStateListener<T> stateListener)
		{
			this.panel = panel;
			this.stateListener = stateListener;
			this.isConfirmIncluded = this.panel instanceof ConfirmPanel;
		}
	}

	private Stack<State>[] states;
	private Stack<State> statesCommand, statesTable, statesData, statesSpeedrun;

	@SuppressWarnings("unchecked")
	public StateManager()
	{
		this.statesCommand = new Stack<State>();
		this.statesTable = new Stack<State>();
		this.statesSpeedrun = new Stack<State>();
		this.statesData = new Stack<State>();
		this.states = new Stack[]
		{ this.statesCommand, this.statesTable, this.statesData, this.statesSpeedrun };
	}

	public void clear()
	{
		this.currentManager().clear();
		this.updatePanel();
	}

	@SuppressWarnings("unchecked")
	public <T extends CGPanel> T clearState(boolean shouldCheck)
	{
		if (this.currentManager().isEmpty()) return null;
		if (shouldCheck && this.currentManager().peek().stateListener != null
				&& !this.currentManager().peek().stateListener.shouldStateClose(this.currentManager().peek().panel)) return null;
		T panel = (T) this.currentManager().pop().panel;
		this.updatePanel();
		return panel;
	}

	private Stack<State> currentManager()
	{
		return this.states[CommandGenerator.getCurrentMode()];
	}

	public State getState()
	{
		if (this.currentManager().isEmpty()) return null;
		return this.currentManager().peek();
	}

	public <T extends CGPanel> void setCommandState(T panel, IStateListener<T> stateListener)
	{
		this.statesCommand.add(new State<T>(panel, stateListener));
		this.updatePanel();
	}

	public <T extends CGPanel> void setState(T panel, IStateListener<T> stateListener)
	{
		this.currentManager().add(new State<T>(panel, stateListener));
		this.updatePanel();
	}

	public int stateCount()
	{
		return this.currentManager().size();
	}

	public void updateMode()
	{
		this.updatePanel();
	}

	private void updatePanel()
	{
		State state = this.getState();
		CGPanel p = state == null ? null : state.panel;
		if (p != null) p.updateTranslations();
		if (this.currentManager().size() <= 1 || (state != null && state.isConfirmIncluded)) CommandGenerator.window.setMainPanel(p);
		else CommandGenerator.window.setMainPanel(new ConfirmPanel(state.panel.getStateName(), p, true));
	}
}
