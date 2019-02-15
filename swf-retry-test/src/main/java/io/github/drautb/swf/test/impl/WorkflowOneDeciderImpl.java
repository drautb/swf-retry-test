package io.github.drautb.swf.test.impl;

import com.amazonaws.services.simpleworkflow.flow.DecisionContextProvider;
import com.amazonaws.services.simpleworkflow.flow.DecisionContextProviderImpl;
import com.amazonaws.services.simpleworkflow.flow.WorkflowClock;
import com.amazonaws.services.simpleworkflow.flow.annotations.Asynchronous;
import com.amazonaws.services.simpleworkflow.flow.core.OrPromise;
import com.amazonaws.services.simpleworkflow.flow.core.Promise;
import com.amazonaws.services.simpleworkflow.flow.core.Settable;
import io.github.drautb.swf.test.api.ActivityClient;
import io.github.drautb.swf.test.api.ActivityClientImpl;
import io.github.drautb.swf.test.api.WorkflowOneDecider;

/**
 * @author drautb
 */
public class WorkflowOneDeciderImpl implements WorkflowOneDecider {

  private final ActivityClient activityClient = new ActivityClientImpl();

  private DecisionContextProvider contextProvider = new DecisionContextProviderImpl();
  private WorkflowClock clock = contextProvider.getDecisionContext().getWorkflowClock();

  private Settable<Void> signalReceived = new Settable<>();

  public Promise<Void> run() {
    Promise<Void> timer = startDaemonTimer(300);
    OrPromise signalOrTimer = new OrPromise(timer, signalReceived);
    return processNextStep(signalOrTimer);
  }

  @Asynchronous(daemon = true)
  private Promise<Void> startDaemonTimer(int seconds) {
    Promise<Void> timer = clock.createTimer(seconds);
    return timer;
  }


  @Asynchronous
  private Promise<Void> processNextStep(Promise<?> waitFor) {
    if (!signalReceived.isReady()) {
      // No signal was received, so skip everything and finish now.
      return Promise.Void();
    }

    // Otherwise, continue with the rest of the workflow.
    return activityClient.spin();
  }

  @Asynchronous
  public void proceed() {
    signalReceived.chain(Promise.Void());
  }

}
