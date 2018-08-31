package io.github.drautb.swf.impl;

import com.amazonaws.services.simpleworkflow.flow.ActivityExecutionContext;
import com.amazonaws.services.simpleworkflow.flow.ActivityExecutionContextProviderImpl;
import io.github.drautb.swf.api.Activity;

public class ActivityImpl implements Activity {

  public void spin() {
    for (int n=0; n<10; n++) {
      try {
        Thread.sleep(10000);
      }
      catch (InterruptedException e) {
        throw new RuntimeException(e);
      }

      ActivityExecutionContext context = new ActivityExecutionContextProviderImpl().getActivityExecutionContext();
      context.recordActivityHeartbeat("sleeping");
    }
  }

}
