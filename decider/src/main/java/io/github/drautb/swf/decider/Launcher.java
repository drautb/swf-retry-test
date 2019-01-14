package io.github.drautb.swf.decider;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflow;
import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflowClientBuilder;
import com.amazonaws.services.simpleworkflow.flow.WorkflowWorker;
import io.github.drautb.swf.SwfRetryTest;
import io.github.drautb.swf.decider.impl.DynamicClientDeciderImpl;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class Launcher implements ServletContextListener {

  private WorkflowWorker dynamicClientWorker;

  public void contextInitialized(ServletContextEvent event) {
    AmazonSimpleWorkflow swfClient = AmazonSimpleWorkflowClientBuilder.standard().withRegion(Regions.US_EAST_1).build();

    dynamicClientWorker = new WorkflowWorker(swfClient, SwfRetryTest.DOMAIN, getDeciderTaskList("dynamic"));

    try {
      dynamicClientWorker.addWorkflowImplementationType(DynamicClientDeciderImpl.class);
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }

    dynamicClientWorker.start();
  }

  public void contextDestroyed(ServletContextEvent event) {
    dynamicClientWorker.shutdownNow();
  }

  private static String getDeciderTaskList(String deciderType) {
    return SwfRetryTest.WORKFLOW_NAME_PREFIX + "-" + deciderType + "-" + SwfRetryTest.DECIDER_VERSION;
  }

}
