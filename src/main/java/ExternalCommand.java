import java.io.File;
import java.util.List;

public class ExternalCommand implements Command{

    @Override
    public void execute(List<String> args, ShellContext context) {
        ShellUtils.CommandData data = ShellUtils.extractRedirection(args);
            try {
                //It takes your array of strings (e.g., ["custom_exe_1234", "Alice"]) and tells Java,
                //"I want to run the program named in index 0 and pass the rest of the strings as arguments to it".
                ProcessBuilder pb = new ProcessBuilder(data.args);

                pb.directory(new File(context.getCurrentPath()));
                //Without this, the program (like custom_exe_1234) would run in the background,
                // but you wouldn't see its output on your screen.
                // By using inheritIO, when the program prints "Hello Alice",
                // that message appears in your shell.
                if(data.isRedirected)
                {
                    pb.redirectOutput(new File(context.getCurrentPath(), data.redirectFile));
                }
                else pb.inheritIO();

                //It tells the Operating System to actually
                //find the executable and begin running it as a separate process.
                pb.start().waitFor();
                //It forces your while(true) loop to stop and wait
                //until the external program finishes running.
            }
            catch (Exception e){
                System.exit(0);
            }
    }
}
