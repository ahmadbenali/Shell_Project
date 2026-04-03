import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.impl.completer.StringsCompleter;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.util.Map;

public class InitializeInput {

    private final Map<String,Command> BuiltIn;
    private final LineReader reader;

    public InitializeInput(Map<String,Command> builtIn) {
        this.BuiltIn = builtIn;
        this.reader = createLineReader();
    }
//dsdkak

    private LineReader createLineReader(){
        try{
            Terminal terminal = TerminalBuilder.builder()
                    .system(true)
                    .build();

            return LineReaderBuilder.builder()
                    .terminal(terminal)
                    .completer(new StringsCompleter(BuiltIn.keySet()))
                    .build();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String ReadInputWithAutoComplete(){
        return reader.readLine("$ ");
    }

}
