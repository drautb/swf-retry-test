package io.github.drautb.swf.test.api;

import com.amazonaws.services.simpleworkflow.flow.annotations.Execute;
import com.amazonaws.services.simpleworkflow.flow.annotations.Signal;
import com.amazonaws.services.simpleworkflow.flow.annotations.Workflow;
import com.amazonaws.services.simpleworkflow.flow.annotations.WorkflowRegistrationOptions;
import io.github.drautb.swf.test.SwfRetryTest;

/**
 * @author drautb
 */
@Workflow
@WorkflowRegistrationOptions(defaultExecutionStartToCloseTimeoutSeconds = SwfRetryTest.START_TO_CLOSE_TIMEOUT,
    defaultTaskList = SwfRetryTest.WORKFLOW_NAME_PREFIX + "-workflow-1-" + SwfRetryTest.DECIDER_VERSION)
public interface WorkflowOneDecider {

  @Execute(name = SwfRetryTest.WORKFLOW_NAME_PREFIX + ".workflowOne", version = SwfRetryTest.DECIDER_VERSION)
  void run();

  @Signal
  void proceed();

}
