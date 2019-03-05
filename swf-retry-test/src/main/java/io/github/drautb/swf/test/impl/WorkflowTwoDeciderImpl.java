package io.github.drautb.swf.test.impl;

import com.amazonaws.services.simpleworkflow.flow.DecisionContextProvider;
import com.amazonaws.services.simpleworkflow.flow.DecisionContextProviderImpl;
import com.amazonaws.services.simpleworkflow.flow.WorkflowClock;
import com.amazonaws.services.simpleworkflow.flow.annotations.Asynchronous;
import com.amazonaws.services.simpleworkflow.flow.core.Promise;
import io.github.drautb.swf.test.api.*;

/**
 * @author drautb
 */
public class WorkflowTwoDeciderImpl implements WorkflowTwoDecider {

  private DecisionContextProvider contextProvider = new DecisionContextProviderImpl();
  private WorkflowClock clock = contextProvider.getDecisionContext().getWorkflowClock();

  private WorkflowOneDeciderClientFactory workflowOneDeciderClientExternalFactory =
      new WorkflowOneDeciderClientFactoryImpl();

  private ActivityClient activityClient = new ActivityClientImpl();

  public void run(boolean cancelWorkflowOne) {
    Promise<Void> started = waitAndStartWorkflowOne(cancelWorkflowOne, clock.createTimer(15));
    Promise<Void> spun = waitAndSpin(started);
    Promise<Void> signaled = waitAndSignal(spun);
    Promise<Void> spunAgain = waitAndSpin(signaled);
    waitAndSignal(spunAgain);
  }

  @Asynchronous
  private Promise<Void> waitAndStartWorkflowOne(boolean cancelWorkflowOne, Promise<Void> waitFor) {
    if (cancelWorkflowOne) {
      workflowOneDeciderClientExternalFactory.getClient("drautb-test-workflow-one").requestCancelWorkflowExecution();
    }

    return Promise.Void();
  }

  @Asynchronous
  private Promise<Void> waitAndSpin(Promise<Void> waitFor) {
    return activityClient.spin();
  }

  @Asynchronous
  private Promise<Void> waitAndSignal(Promise<Void> waitFor) {
    return activityClient.signal();
  }

}
