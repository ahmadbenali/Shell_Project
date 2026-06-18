import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PipelineExecutor {

    public static void executePipeline(List<String> commandLine, ShellContext context) {
        // 1. Break the command line into discrete processes
        List<ShellUtils.CommandData> pipelineData = ShellUtils.extractPipeline(commandLine);
        List<ProcessBuilder> processes = new ArrayList<>();

        // 2. Build the OS process configurations
        for (ShellUtils.CommandData data : pipelineData) {
            ProcessBuilder pb = new ProcessBuilder(data.ClearCommand);
            pb.directory(new File(context.getCurrentPath()));

            // Standard error inherits the terminal so error messages don't get lost
            pb.redirectError(ProcessBuilder.Redirect.INHERIT);
            processes.add(pb);
        }

        // 3. Configure the final process in the pipeline
        ProcessBuilder lastPb = processes.getLast();
        ShellUtils.CommandData lastData = pipelineData.getLast();

        // If the final command has a file redirection (e.g., ls | cat > output.txt)
        if (lastData.isStdout) {
            File outFile = ShellUtils.prepareOutputFile(lastData.WriteOnFile, context.getCurrentPath(), "pipeline");
            if (outFile != null) {
                if (lastData.isAppend) {
                    lastPb.redirectOutput(ProcessBuilder.Redirect.appendTo(outFile));
                } else {
                    lastPb.redirectOutput(ProcessBuilder.Redirect.to(outFile));
                }
            }
        } else {
            // Otherwise, let the final process print directly to the user's terminal
            lastPb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
        }

        // 4. Start the pipeline and wait for completion
        try {
            List<Process> startedProcesses = ProcessBuilder.startPipeline(processes);

            // The shell only needs to block until the very last process finishes
            Process lastProcess = startedProcesses.getLast();
            lastProcess.waitFor();

        } catch (Exception e) {
            System.err.println("pipeline: execution failed: " + e.getMessage());
        }
    }
}