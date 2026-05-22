import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.impl.completer.StringsCompleter;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class InitializeInput {

    private final Map<String,Command> BuiltIn;
    private final LineReader reader;

    public InitializeInput(Map<String,Command> builtIn) {
        this.BuiltIn = builtIn;
        this.reader = createLineReader();
    }
    private LineReader createLineReader(){
        //BuiltIn.keySet() is first set of strings to complete
        Set<String> allCommands = new HashSet<>(BuiltIn.keySet());
        allCommands.addAll(getExternalCommands());
        try{
            Terminal terminal = TerminalBuilder.builder()
                    .system(true)
                    .build();

            return LineReaderBuilder.builder()
                    .terminal(terminal)
                    .completer(new StringsCompleter(allCommands))
                    .build();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String ReadInputWithAutoComplete(){
        return reader.readLine("$ ");
    }

    public Set<String> getExternalCommands(){
        Set<String> externalCommands = new HashSet<>();
        String pathEnv = System.getenv("PATH");

        if(pathEnv == null){return externalCommands;}

        String []directories=pathEnv.split(":");

        for(String dir:directories){
            //see if there are a directory and can be read
            File directory=new File(dir);
            if(directory.exists() && directory.isDirectory()){
                File [] files=directory.listFiles();
                if(files!=null){
                    for(File file:files){
                        if(file.isFile() && file.canRead()){
                            externalCommands.add(file.getName());
                        }
                    }
                }
            }
        }
        return externalCommands;
    }
}
