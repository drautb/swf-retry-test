package io.github.drautb.swf.test.impl;

import com.amazonaws.services.simpleworkflow.flow.core.Promise;
import io.github.drautb.swf.test.api.ChildWorkflowDecider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author drautb
 */
public class ChildWorkflowDeciderImpl implements ChildWorkflowDecider {

    private static final Logger LOG = LoggerFactory.getLogger(ChildWorkflowDeciderImpl.class);

    public Promise<String> run() {
        LOG.info("Starting workflow!");

        return Promise.asPromise("child workflow result");
    }

}

