package io.github.drautb.swf.test;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflow;
import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflowClientBuilder;
import com.amazonaws.services.simpleworkflow.flow.ActivityWorker;
import com.amazonaws.services.simpleworkflow.flow.WorkflowWorker;
import io.github.drautb.swf.test.impl.*;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@SpringBootApplication
public class Launcher {

  private WorkflowWorker dynamicClientWorker;
  private WorkflowWorker workflowOneWorker;
  private WorkflowWorker workflowTwoWorker;
  private WorkflowWorker childWorkflowWorker;

  private ActivityWorker activityWorker;

  public static void main(String[] args) {
    SpringApplication app = new SpringApplication(Launcher.class);
    app.setBannerMode(Banner.Mode.OFF);
    app.run(args);
  }

  @PostConstruct
  public void launch() {
    AmazonSimpleWorkflow swfClient = AmazonSimpleWorkflowClientBuilder.standard().withRegion(Regions.US_EAST_1).build();

    dynamicClientWorker = new WorkflowWorker(swfClient, SwfRetryTest.DOMAIN, getDeciderTaskList("dynamic"));
    workflowOneWorker = new WorkflowWorker(swfClient, SwfRetryTest.DOMAIN, getDeciderTaskList("workflow-1"));
    workflowTwoWorker = new WorkflowWorker(swfClient, SwfRetryTest.DOMAIN, getDeciderTaskList("workflow-2"));
    childWorkflowWorker = new WorkflowWorker(swfClient, SwfRetryTest.DOMAIN, getDeciderTaskList("child-workflow"));

    activityWorker = new ActivityWorker(swfClient, SwfRetryTest.DOMAIN, getActivityTaskList());

    try {
      dynamicClientWorker.addWorkflowImplementationType(DynamicClientDeciderImpl.class);
      workflowOneWorker.addWorkflowImplementationType(WorkflowOneDeciderImpl.class);
      workflowTwoWorker.addWorkflowImplementationType(WorkflowTwoDeciderImpl.class);
      childWorkflowWorker.addWorkflowImplementationType(ChildWorkflowDeciderImpl.class);

      activityWorker.addActivitiesImplementation(new ActivityImpl());
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }

    dynamicClientWorker.start();
    workflowOneWorker.start();
    workflowTwoWorker.start();
    childWorkflowWorker.start();

    activityWorker.start();
  }

  @PreDestroy
  public void shutdown() {
    dynamicClientWorker.shutdownNow();
    workflowOneWorker.shutdownNow();
    workflowTwoWorker.shutdownNow();
    childWorkflowWorker.shutdownNow();

    activityWorker.shutdownNow();
  }

  private static String getDeciderTaskList(String deciderType) {
    return SwfRetryTest.WORKFLOW_NAME_PREFIX + "-" + deciderType + "-" + SwfRetryTest.DECIDER_VERSION;
  }

  private static String getActivityTaskList() {
    return SwfRetryTest.WORKFLOW_NAME_PREFIX + "-" + SwfRetryTest.ACTIVITY_VERSION + "-activity";
  }

}
