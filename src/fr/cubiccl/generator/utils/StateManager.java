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

	private Stack<State> states;

	public StateManager()
	{
		this.states = new Stack<State>();
	}

	public void clear()
	{
		this.states.clear();
	}

	@SuppressWarnings("unchecked")
	public <T extends CGPanel> T clearState(boolean shouldCheck)
	{
		if (this.states.isEmpty()) return null;
		if (shouldCheck && this.states.peek().stateListener != null && !this.states.peek().stateListener.shouldStateClose(this.states.peek().panel)) return null;
		T panel = (T) this.states.pop().panel;
		this.updatePanel();
		return panel;
	}

	public State getState()
	{
		if (this.states.isEmpty()) return null;
		return this.states.peek();
	}

	public <T extends CGPanel> void setState(T panel, IStateListener<T> stateListener)
	{
		this.states.add(new State<T>(panel, stateListener));
		this.updatePanel();
	}

	public int stateCount()
	{
		return this.states.size();
	}

	private void updatePanel()
	{
		State state = this.getState();
		if (state != null)
		{
			state.panel.updateTranslations();
			if (this.states.size() == 1 || state.isConfirmIncluded) CommandGenerator.window.setMainPanel(state.panel);
			else CommandGenerator.window.setMainPanel(new ConfirmPanel(state.panel.getStateName(), state.panel, true));
		}
	}
}
