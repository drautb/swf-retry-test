package io.github.drautb.swf.test.impl;

import com.amazonaws.services.simpleworkflow.flow.ActivityExecutionContext;
import com.amazonaws.services.simpleworkflow.flow.ActivityExecutionContextProviderImpl;
import io.github.drautb.swf.test.api.Activity;
import org.familysearch.paas.sps.common.annotations.AdaptExceptionForSwf;

@AdaptExceptionForSwf
public class ActivityImpl implements Activity {

  public void spin() {
    for (int n=0; n<10; n++) {
      try {
        Thread.sleep(1000);
      }
      catch (InterruptedException e) {
        throw new RuntimeException(e);
      }

      ActivityExecutionContext context = new ActivityExecutionContextProviderImpl().getActivityExecutionContext();
      context.recordActivityHeartbeat("sleeping");
    }
  }

}
