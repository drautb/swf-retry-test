package io.github.drautb.swf.test.impl;

import com.amazonaws.services.simpleworkflow.flow.annotations.Asynchronous;
import com.amazonaws.services.simpleworkflow.flow.core.Promise;
import com.amazonaws.services.simpleworkflow.flow.core.Settable;
import com.amazonaws.services.simpleworkflow.flow.core.TryCatch;
import io.github.drautb.swf.test.api.ActivityClient;
import io.github.drautb.swf.test.api.ActivityClientImpl;
import io.github.drautb.swf.test.api.WorkflowOneDecider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author drautb
 */
public class WorkflowOneDeciderImpl implements WorkflowOneDecider {

  private static final Logger LOG = LoggerFactory.getLogger(WorkflowOneDeciderImpl.class);

  private ActivityClient activityClient = new ActivityClientImpl();

  private Settable<Void> allDone = new Settable<>();

  public Promise<Void> run() {
    LOG.info("Starting workflow!");

    new TryCatch() { //NOSONAR This uses the SWF TryCatch pattern correctly
      @Override
      protected void doTry() throws Throwable {
        Promise<Void> doneWaiting = activityClient.spin();
        allDone.chain(doneWaiting);
      }

      @Override
      protected void doCatch(Throwable e) throws Throwable {
        LOG.error("Error in doTry", e);
      }
    };

    return finished(allDone);
  }

  @Asynchronous
  public Promise<Void> finished(Promise<?> waitFor) {
    LOG.info("Exiting workflow! Promise value: {}", waitFor);
    return Promise.Void();
  }


}
