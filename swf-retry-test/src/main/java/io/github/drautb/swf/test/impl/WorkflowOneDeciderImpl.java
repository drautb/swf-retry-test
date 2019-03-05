package io.github.drautb.swf.test.impl;

import com.amazonaws.services.simpleworkflow.flow.DecisionContextProvider;
import com.amazonaws.services.simpleworkflow.flow.DecisionContextProviderImpl;
import com.amazonaws.services.simpleworkflow.flow.WorkflowClock;
import com.amazonaws.services.simpleworkflow.flow.annotations.Asynchronous;
import com.amazonaws.services.simpleworkflow.flow.core.Promise;
import com.amazonaws.services.simpleworkflow.flow.core.TryCatch;
import io.github.drautb.swf.test.api.WorkflowOneDecider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author drautb
 */
public class WorkflowOneDeciderImpl implements WorkflowOneDecider {

  private static final Logger LOG = LoggerFactory.getLogger(WorkflowOneDeciderImpl.class);

  private DecisionContextProvider contextProvider = new DecisionContextProviderImpl();
  private WorkflowClock clock = contextProvider.getDecisionContext().getWorkflowClock();

  private Promise<?> allDone;

  public Promise<Void> run() {
    LOG.info("Starting workflow!");

    new TryCatch() { //NOSONAR This uses the SWF TryCatch pattern correctly
      @Override
      protected void doTry() throws Throwable {
        Promise<Void> timer = clock.createTimer(5);
        Promise<Void> waitForTimer = waitForTimer(timer);
        allDone = timerIsDone(waitForTimer);
      }

      @Override
      protected void doCatch(Throwable e) throws Throwable {
        LOG.error("Error in doTry", e);
      }
    };

    return finished(allDone);
  }

  @Asynchronous
  private Promise<Void> waitForTimer(Promise<?> waitFor) {
    return Promise.Void();
  }

  @Asynchronous
  public Promise<Void> timerIsDone(Promise<?> waitFor) {
    return Promise.Void();
  }

  @Asynchronous
  public Promise<Void> finished(Promise<?> waitFor) {
    LOG.info("Exiting workflow! Promise value: {}", waitFor);
    return Promise.Void();
  }


}
