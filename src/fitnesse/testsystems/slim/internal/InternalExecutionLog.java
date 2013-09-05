package fitnesse.testsystems.slim.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fitnesse.testsystems.ExecutionLog;
import util.TimeMeasurement;

public class InternalExecutionLog implements ExecutionLog {
  private final List<Throwable> exceptions = new ArrayList<Throwable>();
  private final TimeMeasurement timeMeasurement = new TimeMeasurement();

  public InternalExecutionLog() {
    timeMeasurement.start();
  }

  @Override
  public void addException(Throwable e) {
    exceptions.add(e);
  }

  @Override
  public List<Throwable> getExceptions() {
    return Collections.unmodifiableList(exceptions);
  }

  @Override
  public String getCommand() {
    return "Internal";
  }

  @Override
  public long getExecutionTime() {
    return timeMeasurement.elapsed();
  }

  @Override
  public int getExitCode() {
    return 0;
  }

  @Override
  public String getCapturedOutput() {
    return null;
  }

  @Override
  public String getCapturedError() {
    return null;
  }

  @Override
  public boolean hasCapturedOutput() {
    return false;
  }
}
