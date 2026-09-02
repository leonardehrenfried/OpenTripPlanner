package org.opentripplanner.utils.logging;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.junit.jupiter.api.Test;

public class ProgressTrackerTest {

  private String msg;
  private boolean breakOut = false;

  @Test
  public void testStepLessThan100() {
    ProgressTracker p = new ProgressTracker("Pete", 1, -1, 0, false);

    msg = null;
    p.step(m -> msg = m);
    assertEquals("Pete progress: 1 done", msg);

    msg = null;
    p.step(m -> msg = m);
    assertEquals("Pete progress: 2 done", msg);

    msg = p.completeMessage();
    assertTrue(msg.startsWith("Pete progress tracking complete. 2 done in"), msg);
  }

  @Test
  public void testStepMoreThan100() {
    ProgressTracker p = new ProgressTracker("Pete", 2, 200, 0, true);

    assertEquals("Pete progress tracking started.", p.startMessage());

    msg = null;
    p.step(m -> msg = m);
    assertNull(msg, msg);
    assertNull(msg, "Pete progress: 2 bytes of 200 bytes ( 1%)");

    p.step(m -> msg = m);
    assertEquals("Pete progress: 2 bytes of 200 bytes ( 1%)", msg);

    msg = p.completeMessage();
    assertTrue(msg.startsWith("Pete progress tracking complete. 2 bytes done in"), msg);
  }

  @Test
  public void testNoOutputInQuietPeriod() {
    long QUIET_PERIOD = 1000;
    ProgressTracker subject = new ProgressTracker("Pete", 1, 100, QUIET_PERIOD, true);
    long start = System.currentTimeMillis();

    sleep10ms();
    subject.step(m -> breakOut = true);

    sleep10ms();
    subject.step(m -> breakOut = true);

    long time = System.currentTimeMillis() - start;
    // If test was able to run within the quiet period
    if (time < QUIET_PERIOD) {
      assertFalse(breakOut, "No steps should log anything within the quiet period. Time: " + time);
    }
  }

  @Test
  public void testSkipCountsTowardsProgress() throws IOException {
    InputStream source = new ByteArrayInputStream(new byte[100]);
    InputStream tracked = ProgressTracker.track("Pete", 1, 100, source, m -> msg = m);

    long skipped = tracked.skip(60);
    assertEquals(60, skipped);

    tracked.close();
    assertTrue(msg.startsWith("Pete progress tracking complete. 60 bytes done in"), msg);
  }

  void sleep10ms() {
    try {
      Thread.sleep(10);
    } catch (InterruptedException e) {
      throw new RuntimeException(e.getMessage(), e);
    }
  }
}
