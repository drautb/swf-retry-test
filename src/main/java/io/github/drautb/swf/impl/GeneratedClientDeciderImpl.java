package io.github.drautb.swf.impl;

import com.amazonaws.services.simpleworkflow.flow.core.Promise;
import io.github.drautb.swf.api.GeneratedClientDecider;
import io.github.drautb.swf.api.ActivityClient;
import io.github.drautb.swf.api.ActivityClientImpl;

public class GeneratedClientDeciderImpl implements GeneratedClientDecider {

  private ActivityClient activityClient = new ActivityClientImpl();

  public Promise<Void> run() {
    return activityClient.spin();
  }

}

