package io.github.drautb.swf.test;

public final class SwfRetryTest {

  public static final String DOMAIN = "Samples";

  public static final String DECIDER_VERSION = "1.0.27";
  public static final String ACTIVITY_VERSION = "1.0.26";

  public static final int INITIAL_RETRY_INTERVAL_SECONDS = 10;
  public static final int MAX_ATTEMPTS = 4;

  public static final int START_TO_CLOSE_TIMEOUT = 600;
  public static final int SCHEDULE_TO_START_TIMEOUT = 60;
  public static final int HEARTBEAT_TIMEOUT = 60;

  public static final String WORKFLOW_NAME_PREFIX = "SwfRetryTest";

  private SwfRetryTest() {}

}
