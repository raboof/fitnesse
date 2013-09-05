package fitnesse.testsystems.slim.internal;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fitnesse.slim.JavaSlimFactory;
import fitnesse.slim.SlimClient;
import fitnesse.slim.SlimFactory;
import fitnesse.slim.StatementExecutorInterface;
import fitnesse.slim.instructions.Instruction;
import fitnesse.slim.instructions.InstructionResult;
import fitnesse.testsystems.Descriptor;
import fitnesse.testsystems.ExecutionLog;

public class InternalSlimClient implements SlimClient {

  private final InternalExecutionLog executionLog = new InternalExecutionLog();

  private final StatementExecutorInterface executor;

  public InternalSlimClient(Descriptor descriptor) {
    // TODO initialize a classloader with the classpath described in the descriptor, and use it to execute
    // the instructions
    SlimFactory slimFactory = new JavaSlimFactory();
    this.executor = slimFactory.getStatementExecutor();
  }

  @Override
  public void start() throws IOException {
    // Nothing special to initialize
  }

  @Override
  public Map<String, Object> invokeAndGetResponse(List<Instruction> statements) throws IOException {
    Map<String, Object> result = new HashMap<String, Object>();

    for (Instruction instruction : statements) {
      if (!executor.stopHasBeenRequested()) {
        result.put(instruction.getId(), execute(instruction));
      }
    }
    return result;
  }

  private Object execute(Instruction instruction) {
    InstructionResult instructionResult = instruction.execute(executor);
    if (instructionResult.hasResult() || instructionResult.hasError()) {
      return instructionResult.getResult().toString();
    } else {
      return "null";
    }
  }

  @Override
  public void bye() throws IOException {
    cleanup();
  }

  @Override
  public void kill() throws IOException {
    cleanup();
  }

  private void cleanup() {
    // nothing to clean up
  }

  @Override
  public String getTestRunner() {
    return "InternalSlimServer";
  }

  @Override
  public ExecutionLog getExecutionLog() {
    return executionLog;
  }
}
