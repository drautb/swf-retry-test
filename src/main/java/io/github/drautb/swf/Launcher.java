package io.github.drautb.swf;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflow;
import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflowClientBuilder;
import com.amazonaws.services.simpleworkflow.flow.ActivityWorker;
import com.amazonaws.services.simpleworkflow.flow.WorkflowWorker;
import io.github.drautb.swf.impl.DynamicClientDeciderImpl;
import io.github.drautb.swf.impl.GeneratedClientDeciderImpl;
import io.github.drautb.swf.impl.ActivityImpl;

@WebListener
public class Launcher implements ServletContextListener {

  private ActivityWorker activityWorker;
  private WorkflowWorker generatedClientWorker;
  private WorkflowWorker dynamicClientWorker;

  public void contextInitialized(ServletContextEvent event) {
    AmazonSimpleWorkflow swfClient = AmazonSimpleWorkflowClientBuilder.standard().withRegion(Regions.US_EAST_1).build();

    activityWorker = new ActivityWorker(swfClient, SwfRetryTest.DOMAIN, getActivityTaskList());
    generatedClientWorker = new WorkflowWorker(swfClient, SwfRetryTest.DOMAIN, getDeciderTaskList("generated"));
    dynamicClientWorker = new WorkflowWorker(swfClient, SwfRetryTest.DOMAIN, getDeciderTaskList("dynamic"));

    try {
      activityWorker.addActivitiesImplementation(new ActivityImpl());
      generatedClientWorker.addWorkflowImplementationType(GeneratedClientDeciderImpl.class);
      dynamicClientWorker.addWorkflowImplementationType(DynamicClientDeciderImpl.class);
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }

    activityWorker.start();
    generatedClientWorker.start();
    dynamicClientWorker.start();
  }

  public void contextDestroyed(ServletContextEvent event) {
    activityWorker.shutdownNow();
    generatedClientWorker.shutdownNow();
    dynamicClientWorker.shutdownNow();
  }

  private static String getDeciderTaskList(String deciderType) {
    return SwfRetryTest.WORKFLOW_NAME_PREFIX + "-" + deciderType + "-" + SwfRetryTest.DECIDER_VERSION;
  }

  private static String getActivityTaskList() {
    return SwfRetryTest.WORKFLOW_NAME_PREFIX + "-" + SwfRetryTest.ACTIVITY_VERSION + "-activity";
  }
}
