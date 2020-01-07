package io.github.drautb.swf.test.impl;

import com.amazonaws.services.dynamodbv2.*;
import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflowClientBuilder;
import com.amazonaws.services.simpleworkflow.model.UnknownResourceException;
import io.github.drautb.swf.test.SwfRetryTest;
import io.github.drautb.swf.test.api.Activity;
import io.github.drautb.swf.test.api.WorkflowOneDeciderClientExternalFactory;
import io.github.drautb.swf.test.api.WorkflowOneDeciderClientExternalFactoryImpl;
import org.familysearch.paas.sps.common.annotations.AdaptExceptionForSwf;

import java.util.concurrent.TimeUnit;

@AdaptExceptionForSwf
public class ActivityImpl implements Activity {

  private WorkflowOneDeciderClientExternalFactory workflowOneDeciderClientExternalFactory =
      new WorkflowOneDeciderClientExternalFactoryImpl(
          AmazonSimpleWorkflowClientBuilder.standard().withRegion("us-east-1").build(),
          SwfRetryTest.DOMAIN);

  public void spin() {
      AmazonDynamoDB dynamoDB = AmazonDynamoDBClientBuilder.standard().withRegion("us-east-1").build();

      AmazonDynamoDBLockClient client = new AmazonDynamoDBLockClient(AmazonDynamoDBLockClientOptions.builder(dynamoDB, "adhoc-drautb-test")
//          .withCreateHeartbeatBackgroundThread(true)
          .withHeartbeatPeriod(3L)
          .withLeaseDuration(10L)
//          .withOwnerName("test-owner")
          .withTimeUnit(TimeUnit.SECONDS)
          .withHoldLockOnServiceUnavailable(false)
          .withPartitionKeyName("lock_name")
          .build());

      try {
        client.assertLockTableExists();
        LockItem lock = client.acquireLock(AcquireLockOptions.builder("this-is-the-lock").build());
        int i = 42;
        System.out.println(i);
        System.out.println(lock);
//        LockItem lock = client.acquireLock(AcquireLockOptions.builder("this-is-the-lock").build());
//        client.tryAcquireLock()

      }
      catch (InterruptedException e) {
        throw new RuntimeException(e);
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
