package io.github.drautb.swf.test.api;

import com.amazonaws.services.simpleworkflow.flow.annotations.Execute;
import com.amazonaws.services.simpleworkflow.flow.annotations.Workflow;
import com.amazonaws.services.simpleworkflow.flow.annotations.WorkflowRegistrationOptions;
import com.amazonaws.services.simpleworkflow.flow.core.Promise;
import io.github.drautb.swf.test.SwfRetryTest;

/**
 * @author drautb
 */
@Workflow
@WorkflowRegistrationOptions(defaultExecutionStartToCloseTimeoutSeconds = SwfRetryTest.START_TO_CLOSE_TIMEOUT,
        defaultTaskList = SwfRetryTest.WORKFLOW_NAME_PREFIX + "-child-workflow-" + SwfRetryTest.DECIDER_VERSION)
public interface ChildWorkflowDecider {

    @Execute(name = SwfRetryTest.WORKFLOW_NAME_PREFIX + ".childWorkflow", version = SwfRetryTest.DECIDER_VERSION)
    Promise<String> run();

}
