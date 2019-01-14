package io.github.drautb.swf.activity.impl;

import com.amazonaws.services.route53.model.AmazonRoute53Exception;
import com.amazonaws.services.simpleworkflow.flow.ActivityExecutionContext;
import com.amazonaws.services.simpleworkflow.flow.ActivityExecutionContextProviderImpl;
import io.github.drautb.swf.activity.api.Activity;
import org.familysearch.paas.sps.common.annotations.AdaptExceptionForSwf;

@AdaptExceptionForSwf
public class ActivityImpl implements Activity {

  public void spin() {
//    for (int n=0; n<10; n++) {
//      try {
//        Thread.sleep(1000);
//      }
//      catch (InterruptedException e) {
//        throw new RuntimeException(e);
//      }
//
//      ActivityExecutionContext context = new ActivityExecutionContextProviderImpl().getActivityExecutionContext();
//      context.recordActivityHeartbeat("sleeping");
//    }

    throw new AmazonRoute53Exception("test r53 error");
  }

}
