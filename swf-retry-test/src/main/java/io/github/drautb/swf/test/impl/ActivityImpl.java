package io.github.drautb.swf.test.impl;

import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflowClientBuilder;
import com.amazonaws.services.simpleworkflow.flow.ActivityExecutionContext;
import com.amazonaws.services.simpleworkflow.flow.ActivityExecutionContextProviderImpl;
import com.amazonaws.services.simpleworkflow.model.UnknownResourceException;
import io.github.drautb.swf.test.SwfRetryTest;
import io.github.drautb.swf.test.api.Activity;
import io.github.drautb.swf.test.api.WorkflowOneDeciderClientExternalFactory;
import io.github.drautb.swf.test.api.WorkflowOneDeciderClientExternalFactoryImpl;
import org.familysearch.paas.sps.common.annotations.AdaptExceptionForSwf;

@AdaptExceptionForSwf
public class ActivityImpl implements Activity {

  private WorkflowOneDeciderClientExternalFactory workflowOneDeciderClientExternalFactory =
      new WorkflowOneDeciderClientExternalFactoryImpl(
          AmazonSimpleWorkflowClientBuilder.standard().withRegion("us-east-1").build(),
          SwfRetryTest.DOMAIN);

  public void spin() {
    for (int n=0; n<10; n++) {
      try {
        Thread.sleep(1000);
      }
      catch (InterruptedException e) {
        throw new RuntimeException(e);
      }

      ActivityExecutionContext context = new ActivityExecutionContextProviderImpl().getActivityExecutionContext();
      context.recordActivityHeartbeat("sleeping");
    }
  }

  public void signal() {
    try {
      //workflowOneDeciderClientExternalFactory.getClient("drautb-test-workflow-one").proceed();
    }
    catch (UnknownResourceException e) {
      // Error?
    }
  }

}
