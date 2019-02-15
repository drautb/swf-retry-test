package io.github.drautb.swf.test.impl;

import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflowClientBuilder;
import com.amazonaws.services.simpleworkflow.flow.DecisionContextProvider;
import com.amazonaws.services.simpleworkflow.flow.DecisionContextProviderImpl;
import com.amazonaws.services.simpleworkflow.flow.WorkflowClock;
import com.amazonaws.services.simpleworkflow.flow.annotations.Asynchronous;
import com.amazonaws.services.simpleworkflow.flow.core.Promise;
import com.amazonaws.services.simpleworkflow.model.UnknownResourceException;
import io.github.drautb.swf.test.SwfRetryTest;
import io.github.drautb.swf.test.api.*;

/**
 * @author drautb
 */
public class WorkflowTwoDeciderImpl implements WorkflowTwoDecider {

  private DecisionContextProvider contextProvider = new DecisionContextProviderImpl();
  private WorkflowClock clock = contextProvider.getDecisionContext().getWorkflowClock();

  private WorkflowOneDeciderClientExternalFactory workflowOneDeciderClientExternalFactory =
      new WorkflowOneDeciderClientExternalFactoryImpl(
          AmazonSimpleWorkflowClientBuilder.standard().withRegion("us-east-1").build(),
          SwfRetryTest.DOMAIN);

  private ActivityClient activityClient = new ActivityClientImpl();

  public void run(boolean cancelWorkflowOne) {
    Promise<Void> started = waitAndStartWorkflowOne(cancelWorkflowOne, clock.createTimer(15));
    Promise<Void> spun = waitAndSpin(started);
    waitAndSignal(spun);
  }

  @Asynchronous
  private Promise<Void> waitAndStartWorkflowOne(boolean cancelWorkflowOne, Promise<Void> waitFor) {
    try {
      workflowOneDeciderClientExternalFactory.getClient("drautb-test-workflow-one").requestCancelWorkflowExecution();
    }
    catch (UnknownResourceException e) {
      System.out.println(e);
    }

    workflowOneDeciderClientExternalFactory.getClient("drautb-test-workflow-one").run();
    return Promise.Void();
  }

  @Asynchronous
  private Promise<Void> waitAndSpin(Promise<Void> waitFor) {
    return activityClient.spin();
  }

  @Asynchronous
  private void waitAndSignal(Promise<Void> waitFor) {
    workflowOneDeciderClientExternalFactory.getClient("drautb-test-workflow-one").proceed();
  }

}
