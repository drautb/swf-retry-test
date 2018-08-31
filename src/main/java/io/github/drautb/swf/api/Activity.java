package io.github.drautb.swf.api;

import com.amazonaws.services.simpleworkflow.flow.annotations.Activities;
import com.amazonaws.services.simpleworkflow.flow.annotations.ActivityRegistrationOptions;
import com.amazonaws.services.simpleworkflow.flow.annotations.ExponentialRetry;

import io.github.drautb.swf.SwfRetryTest;

@ActivityRegistrationOptions(defaultTaskScheduleToStartTimeoutSeconds = SwfRetryTest.SCHEDULE_TO_START_TIMEOUT,
                             defaultTaskStartToCloseTimeoutSeconds = SwfRetryTest.START_TO_CLOSE_TIMEOUT,
                             defaultTaskList = SwfRetryTest.WORKFLOW_NAME_PREFIX + "-" + SwfRetryTest.ACTIVITY_VERSION + "-activity",
                             defaultTaskHeartbeatTimeoutSeconds = SwfRetryTest.HEARTBEAT_TIMEOUT)
@Activities(activityNamePrefix = SwfRetryTest.WORKFLOW_NAME_PREFIX + "Activities.", version = SwfRetryTest.ACTIVITY_VERSION)
public interface Activity {

  @ExponentialRetry(initialRetryIntervalSeconds = SwfRetryTest.INITIAL_RETRY_INTERVAL_SECONDS,
      maximumAttempts = SwfRetryTest.MAX_ATTEMPTS)
  void spin();

}
