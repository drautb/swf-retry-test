package io.github.drautb.swf.test.impl;

import com.amazonaws.services.simpleworkflow.flow.DynamicWorkflowClient;
import com.amazonaws.services.simpleworkflow.flow.DynamicWorkflowClientImpl;
import com.amazonaws.services.simpleworkflow.flow.StartWorkflowOptions;
import com.amazonaws.services.simpleworkflow.flow.annotations.Asynchronous;
import com.amazonaws.services.simpleworkflow.flow.core.Promise;
import com.amazonaws.services.simpleworkflow.flow.core.Settable;
import com.amazonaws.services.simpleworkflow.flow.core.TryCatch;
import com.amazonaws.services.simpleworkflow.model.WorkflowExecution;
import com.amazonaws.services.simpleworkflow.model.WorkflowType;
import io.github.drautb.swf.test.SwfRetryTest;
import io.github.drautb.swf.test.api.ActivityClient;
import io.github.drautb.swf.test.api.ActivityClientImpl;
import io.github.drautb.swf.test.api.WorkflowOneDecider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author drautb
 */
public class WorkflowOneDeciderImpl implements WorkflowOneDecider {

  private static final Logger LOG = LoggerFactory.getLogger(WorkflowOneDeciderImpl.class);

  public Promise<Void> run() {
    LOG.info("Starting workflow!");

    DynamicWorkflowClient client = new DynamicWorkflowClientImpl(
            new WorkflowExecution().withWorkflowId("child-workflow-id"),
            new WorkflowType().withName(SwfRetryTest.WORKFLOW_NAME_PREFIX + ".childWorkflow").withVersion(SwfRetryTest.DECIDER_VERSION));
    Promise<?> result = client.startWorkflowExecution(new Object[]{}, null, String.class);

    return finished(result);
  }

  @Asynchronous
  public Promise<Void> finished(Promise<?> waitFor) {
    LOG.info("Exiting workflow! Promise value: {}", waitFor);
    return Promise.Void();
  }


}
