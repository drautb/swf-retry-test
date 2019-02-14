package io.github.drautb.swf.test.impl;

import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflowClientBuilder;
import com.amazonaws.services.simpleworkflow.flow.DecisionContextProvider;
import com.amazonaws.services.simpleworkflow.flow.DecisionContextProviderImpl;
import com.amazonaws.services.simpleworkflow.flow.WorkflowClock;
import com.amazonaws.services.simpleworkflow.flow.annotations.Asynchronous;
import com.amazonaws.services.simpleworkflow.flow.core.Promise;
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

  public void run(boolean cancelWorkflowOne) {
    waitAndProceed(cancelWorkflowOne, clock.createTimer(30));
  }

  @Asynchronous
  private void waitAndProceed(boolean cancelWorkflowOne, Promise<Void> waitFor) {
    if (cancelWorkflowOne) {
      workflowOneDeciderClientExternalFactory.getClient("drautb-test-workflow-one").requestCancelWorkflowExecution();
    }
    else {
      workflowOneDeciderClientExternalFactory.getClient("drautb-test-workflow-one").proceed();
    }
  }

}
