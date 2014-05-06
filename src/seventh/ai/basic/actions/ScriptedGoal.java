/*
 * see license.txt 
 */
package seventh.ai.basic.actions;

import leola.vm.Leola;
import leola.vm.types.LeoObject;
import seventh.ai.basic.Brain;
import seventh.ai.basic.Goal;
import seventh.shared.Cons;
import seventh.shared.DebugDraw;
import seventh.shared.TimeStep;

/**
 * Allows for scripting actions
 * 
 * @author Tony
 *
 */
public class ScriptedGoal extends AdapterAction {

	private Goal goal;
	private Leola runtime;
	private LeoObject goalFunction;
	private boolean isFunctionDone;
	/**
	 * 
	 */
	public ScriptedGoal(Leola runtime, LeoObject goalFunction) {
		this.runtime = runtime;
		this.goalFunction = goalFunction;
		this.goal = new Goal();
		
		this.isFunctionDone = false;
	}
	
	/* (non-Javadoc)
	 * @see seventh.ai.basic.actions.AdapterAction#interrupt(seventh.ai.basic.Brain)
	 */
	@Override
	public void interrupt(Brain brain) {
		goal.interrupt(brain);
	}
	
	/* (non-Javadoc)
	 * @see seventh.ai.basic.actions.AdapterAction#resume(seventh.ai.basic.Brain)
	 */
	@Override
	public void resume(Brain brain) {
		goal.resume(brain);
	}
	
	/* (non-Javadoc)
	 * @see seventh.ai.basic.actions.AdapterAction#update(seventh.ai.basic.Brain, seventh.shared.TimeStep)
	 */
	@Override
	public void update(Brain brain, TimeStep timeStep) {
		goal.update(brain, timeStep);
		
		if(goal.isFinished(brain)) {
			try {
				LeoObject result = runtime.execute(goalFunction, Leola.toLeoObject(brain), Leola.toLeoObject(goal), Leola.toLeoObject(goal.getActionResult()));
				if(!LeoObject.isTrue(result)) {
					this.isFunctionDone = true;
				}
				else {
					Object obj = result.getValue();
					if(obj instanceof Action) {
						Action newAction = (Action)obj;
						goal.addFirstAction(newAction);
					}
				}
			}
			catch(Throwable e) {
				Cons.println("Unable to execute scripted goal: " + e);
			}
		}
		
		Action action = goal.currentAction();
		if(action != null) {
			DebugDraw.drawString("Script Action: " + action.getClass().getSimpleName(), 800, 70, 0xff00ff00);
		}
		else {
			DebugDraw.drawString("Script Action: ", 800, 70, 0xff00ff00);
		}
	}
	
	/* (non-Javadoc)
	 * @see seventh.ai.basic.actions.AdapterAction#isFinished(seventh.ai.basic.Brain)
	 */
	@Override
	public boolean isFinished(Brain brain) {
		return goal.isFinished(brain) && this.isFunctionDone;
	}
	
	/* (non-Javadoc)
	 * @see seventh.ai.basic.actions.AdapterAction#getActionResult()
	 */
	@Override
	public ActionResult getActionResult() {
		return goal.getActionResult();
	}

}