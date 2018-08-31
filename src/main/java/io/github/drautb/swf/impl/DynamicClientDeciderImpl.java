package io.github.drautb.swf.impl;

import com.amazonaws.services.simpleworkflow.flow.DynamicActivitiesClient;
import com.amazonaws.services.simpleworkflow.flow.DynamicActivitiesClientImpl;
import com.amazonaws.services.simpleworkflow.flow.core.Promise;
import com.amazonaws.services.simpleworkflow.model.ActivityType;
import io.github.drautb.swf.api.DynamicClientDecider;

import static io.github.drautb.swf.SwfRetryTest.ACTIVITY_VERSION;
import static io.github.drautb.swf.SwfRetryTest.WORKFLOW_NAME_PREFIX;

public class DynamicClientDeciderImpl implements DynamicClientDecider {

  private DynamicActivitiesClient activitiesClient = new DynamicActivitiesClientImpl();

  public Promise<Void> run() {
    ActivityType activityType = new ActivityType()
        .withName(WORKFLOW_NAME_PREFIX + "Activities.spin")
        .withVersion(ACTIVITY_VERSION);

    Object[] args = {};
    return activitiesClient.scheduleActivity(activityType, args, null, Void.class);
  }

}
