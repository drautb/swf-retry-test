package io.github.drautb.swf.activity;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflow;
import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflowClientBuilder;
import com.amazonaws.services.simpleworkflow.flow.ActivityWorker;
import io.github.drautb.swf.SwfRetryTest;
import io.github.drautb.swf.activity.impl.ActivityImpl;

@WebListener
public class Launcher implements ServletContextListener {

  private ActivityWorker activityWorker;

  public void contextInitialized(ServletContextEvent event) {
    AmazonSimpleWorkflow swfClient = AmazonSimpleWorkflowClientBuilder.standard().withRegion(Regions.US_EAST_1).build();

    activityWorker = new ActivityWorker(swfClient, SwfRetryTest.DOMAIN, getActivityTaskList());

    try {
      activityWorker.addActivitiesImplementation(new ActivityImpl());
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }

    activityWorker.start();
  }

  public void contextDestroyed(ServletContextEvent event) {
    activityWorker.shutdownNow();
  }

  private static String getActivityTaskList() {
    return SwfRetryTest.WORKFLOW_NAME_PREFIX + "-" + SwfRetryTest.ACTIVITY_VERSION + "-activity";
  }
}
