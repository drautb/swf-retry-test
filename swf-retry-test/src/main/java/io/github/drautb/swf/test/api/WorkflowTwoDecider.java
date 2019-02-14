package io.github.drautb.swf.test.api;

import com.amazonaws.services.simpleworkflow.flow.annotations.Execute;
import com.amazonaws.services.simpleworkflow.flow.annotations.Workflow;
import com.amazonaws.services.simpleworkflow.flow.annotations.WorkflowRegistrationOptions;
import io.github.drautb.swf.test.SwfRetryTest;

/**
 * @author drautb
 */
@Workflow
@WorkflowRegistrationOptions(defaultExecutionStartToCloseTimeoutSeconds = SwfRetryTest.START_TO_CLOSE_TIMEOUT,
    defaultTaskList = SwfRetryTest.WORKFLOW_NAME_PREFIX + "-workflow-2-" + SwfRetryTest.DECIDER_VERSION)
public interface WorkflowTwoDecider {

  @Execute(name = SwfRetryTest.WORKFLOW_NAME_PREFIX + ".workflowTwo", version = SwfRetryTest.DECIDER_VERSION)
  void run(boolean cancelWorkflowOne);

}
