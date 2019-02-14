package io.github.drautb.swf.test.impl;

import com.amazonaws.services.simpleworkflow.flow.DynamicActivitiesClient;
import com.amazonaws.services.simpleworkflow.flow.core.Promise;
import com.amazonaws.services.simpleworkflow.model.ActivityType;
import io.github.drautb.swf.test.SwfRetryTest;
import io.github.drautb.swf.test.api.DynamicClientDecider;
import org.familysearch.paas.sps.common.DynamicActivitiesClientFactory;

public class DynamicClientDeciderImpl implements DynamicClientDecider {

  private DynamicActivitiesClient activitiesClient = new DynamicActivitiesClientFactory().getClient();

  public Promise<Void> run() {
    ActivityType activityType = new ActivityType()
        .withName(SwfRetryTest.WORKFLOW_NAME_PREFIX + "Activities.spin")
        .withVersion(SwfRetryTest.ACTIVITY_VERSION);

    Object[] args = {};
    return activitiesClient.scheduleActivity(activityType, args, null, Void.class);
  }

}
