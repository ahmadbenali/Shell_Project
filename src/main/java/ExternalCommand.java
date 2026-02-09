import java.io.File;
import java.io.IOException;
import java.util.List;
import static java.lang.System.*;

public class ExternalCommand implements Command{

    @Override
    public void execute(List<String> args, ShellContext context) {
        // Separate the command from the redirection info
        ShellUtils.CommandData data = ShellUtils.extractRedirection(args);

        try {
            ProcessBuilder processBuilder= new ProcessBuilder(data.CommandParts);
            processBuilder.directory(new File(context.getCurrentPath()));

            if (data.isRedirect) {
                // Ensure the file is created in the current directory
                File outputFile = new File(context.getCurrentPath(), data.WriteOnFile);

                // This is the missing piece!
                // It creates any missing folders like '/tmp/rat/' automatically.
                File parent = outputFile.getParentFile();
                if(parent != null && !parent.exists())
                    if (!parent.mkdirs()) {
                        err.println("Error: Could not create directory " + parent.getPath());
                        return;
                    }
                processBuilder.redirectOutput(outputFile);

            } else {
                processBuilder.inheritIO();
            }

            processBuilder.start().waitFor();
//            Process process = processBuilder.start();
//            process.waitFor();
        } catch (Exception e) {
            err.println("Error: " + e.getMessage());
        }
    }
}
