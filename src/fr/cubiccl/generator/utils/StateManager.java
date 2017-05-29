package fr.cubiccl.generator.utils;

import java.util.Stack;

import fr.cubiccl.generator.CommandGenerator;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.utils.ConfirmPanel;

/** Manages the Generator's states. */
@SuppressWarnings("rawtypes")
public class StateManager
{
	/** A Single state.
	 * 
	 * @param <T> - The type of the State's UI. */
	public class State<T extends CGPanel>
	{
		/** <code>true</code> if the OK/Cancel buttons are included in this State's UI. */
		public final boolean isConfirmIncluded;
		/** This State's UI. */
		public final T panel;
		/** The listener. Will be called when this State closes. */
		private final IStateListener<T> stateListener;

		public State(T panel, IStateListener<T> stateListener)
		{
			this.panel = panel;
			this.stateListener = stateListener;
			this.isConfirmIncluded = this.panel instanceof ConfirmPanel;
		}
	}

	/** The list of States for each mode. */
	private Stack<State>[] states;

	@SuppressWarnings("unchecked")
	public StateManager()
	{
		this.states = new Stack[] {};
	}

	/** Clears all States for the current mode. */
	public void clear()
	{
		this.currentManager().clear();
		this.onStateChange();
	}

	/** Clears the current State.
	 * 
	 * @param shouldCheck - <code>true</code> if the listener should be called.
	 * @return The UI that was closed. */
	@SuppressWarnings("unchecked")
	public <T extends CGPanel> T clearState(boolean shouldCheck)
	{
		if (this.currentManager().isEmpty()) return null;
		if (shouldCheck && this.currentManager().peek().stateListener != null
				&& !this.currentManager().peek().stateListener.shouldStateClose(this.currentManager().peek().panel)) return null;
		T panel = (T) this.currentManager().pop().panel;
		this.onStateChange();
		return panel;
	}

	/** @return The manager for the current mode. */
	private Stack<State> currentManager()
	{
		int current = CommandGenerator.getCurrentMode();
		if (current >= this.states.length)
		{
			@SuppressWarnings("unchecked")
			Stack<State>[] s = new Stack[current + 1];
			for (int i = 0; i < s.length; ++i)
			{
				if (i < this.states.length) s[i] = this.states[i];
				if (s[i] == null) s[i] = new Stack<State>();
			}
			this.states = s;
		}
		return this.states[current];
	}

	/** @return The current State. */
	public State getState()
	{
		if (this.currentManager().isEmpty()) return null;
		return this.currentManager().peek();
	}

	/** Displays the UI of the current State and updates its translations. Called when changing mode or State. */
	public void onStateChange()
	{
		State state = this.getState();
		CGPanel p = state == null ? null : state.panel;
		if (p != null) p.updateTranslations();
		if (this.currentManager().size() <= 1 || (state != null && state.isConfirmIncluded)) CommandGenerator.window.setMainPanel(p);
		else CommandGenerator.window.setMainPanel(new ConfirmPanel(state.panel.getStateName(), p, true));
	}

	/** Sets the State for the Command Generator mode.
	 * 
	 * @param panel - The new State's UI.
	 * @param stateListener - The new State's listener. */
	public <T extends CGPanel> void setCommandState(T panel, IStateListener<T> stateListener)
	{
		this.states[CommandGenerator.COMMANDS].add(new State<T>(panel, stateListener));
		this.onStateChange();
	}

	/** Sets the State for the current mode.
	 * 
	 * @param panel - The new State's UI.
	 * @param stateListener - The new State's listener. */
	public <T extends CGPanel> void setState(T panel, IStateListener<T> stateListener)
	{
		this.currentManager().add(new State<T>(panel, stateListener));
		this.onStateChange();
	}

	/** @return The number of States for the current mode. */
	public int stateCount()
	{
		return this.currentManager().size();
	}
}
