package io.github.drautb.swf.test;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflow;
import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflowClientBuilder;
import com.amazonaws.services.simpleworkflow.flow.ActivityWorker;
import com.amazonaws.services.simpleworkflow.flow.WorkflowWorker;
import io.github.drautb.swf.test.impl.ActivityImpl;
import io.github.drautb.swf.test.impl.DynamicClientDeciderImpl;
import io.github.drautb.swf.test.impl.WorkflowOneDeciderImpl;
import io.github.drautb.swf.test.impl.WorkflowTwoDeciderImpl;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class Launcher implements ServletContextListener {

  private WorkflowWorker dynamicClientWorker;
  private WorkflowWorker workflowOneWorker;
  private WorkflowWorker workflowTwoWorker;

  private ActivityWorker activityWorker;

  public void contextInitialized(ServletContextEvent event) {
    AmazonSimpleWorkflow swfClient = AmazonSimpleWorkflowClientBuilder.standard().withRegion(Regions.US_EAST_1).build();

    dynamicClientWorker = new WorkflowWorker(swfClient, SwfRetryTest.DOMAIN, getDeciderTaskList("dynamic"));
    workflowOneWorker = new WorkflowWorker(swfClient, SwfRetryTest.DOMAIN, getDeciderTaskList("workflow-1"));
    workflowTwoWorker = new WorkflowWorker(swfClient, SwfRetryTest.DOMAIN, getDeciderTaskList("workflow-2"));

    activityWorker = new ActivityWorker(swfClient, SwfRetryTest.DOMAIN, getActivityTaskList());

    try {
      dynamicClientWorker.addWorkflowImplementationType(DynamicClientDeciderImpl.class);
      workflowOneWorker.addWorkflowImplementationType(WorkflowOneDeciderImpl.class);
      workflowTwoWorker.addWorkflowImplementationType(WorkflowTwoDeciderImpl.class);

      activityWorker.addActivitiesImplementation(new ActivityImpl());
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }

    dynamicClientWorker.start();
    workflowOneWorker.start();
    workflowTwoWorker.start();

    activityWorker.start();
  }

  public void contextDestroyed(ServletContextEvent event) {
    dynamicClientWorker.shutdownNow();
    workflowOneWorker.shutdownNow();
    workflowTwoWorker.shutdownNow();

    activityWorker.shutdownNow();
  }

  private static String getDeciderTaskList(String deciderType) {
    return SwfRetryTest.WORKFLOW_NAME_PREFIX + "-" + deciderType + "-" + SwfRetryTest.DECIDER_VERSION;
  }

  private static String getActivityTaskList() {
    return SwfRetryTest.WORKFLOW_NAME_PREFIX + "-" + SwfRetryTest.ACTIVITY_VERSION + "-activity";
  }

}
