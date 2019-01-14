package io.github.drautb.swf.decider.api;

import com.amazonaws.services.simpleworkflow.flow.annotations.Execute;
import com.amazonaws.services.simpleworkflow.flow.annotations.Workflow;
import com.amazonaws.services.simpleworkflow.flow.annotations.WorkflowRegistrationOptions;
import com.amazonaws.services.simpleworkflow.flow.core.Promise;

import io.github.drautb.swf.SwfRetryTest;

@Workflow
@WorkflowRegistrationOptions(defaultExecutionStartToCloseTimeoutSeconds = SwfRetryTest.START_TO_CLOSE_TIMEOUT,
                             defaultTaskList = SwfRetryTest.WORKFLOW_NAME_PREFIX + "-dynamic-" + SwfRetryTest.DECIDER_VERSION)
public interface DynamicClientDecider {

  @Execute(name = SwfRetryTest.WORKFLOW_NAME_PREFIX + ".dynamic", version = SwfRetryTest.DECIDER_VERSION)
  Promise<Void> run();

}
