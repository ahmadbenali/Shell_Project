import java.io.File;
import java.util.Scanner;
import java.util.Set;

public class Main {


    private static final Set<String> Shell_BuiltIn = Set.of("type","exit","echo");

    private static String getPath(String inputPath)
    {
        //path is  Env variable, your program will ask os to check every file inside PATH
        //specifying a set of directories where executable programs are located.
        String pathEnv=System.getenv("PATH");

        if(pathEnv==null) return null;

        // because the input path will be "/usr/local/bin:/usr/bin:/bin:/usr/games"
        String []directories=pathEnv.split(":");

        for(String dir:directories)
        {
            //It doesn't actually create a file on your hard drive yet;
            // it simply creates a Java object that represents a potential file location.
            //Java intelligently combines these into a single path,
            //handling the slashes for you so you don't have to worry if dir ends with a / or not.
            File target=new File(dir,inputPath);
            if(target.exists() && target.canExecute())
                return target.getAbsolutePath();
        }
        return null;
    }



    public static void main(String[] args) throws Exception {


        while(true)
        {
            System.out.print("$ ");
            Scanner scanner=new Scanner(System.in);
            String input=scanner.nextLine();


            String []parts=input.split(" ",2);
            String command=parts[0];

            // This "exit".equals(command) for NULL safe
            switch (command)
            {
                case "exit" ->System.exit(0);
                case "echo" ->{
                    if(parts.length>1) System.out.println(parts[1]);
                }
                case "type" ->{
                    if(parts.length>1) {
                        if (Shell_BuiltIn.contains(parts[1]))
                            System.out.println(parts[1] + " is a shell builtin");
                        else
                        {
                            String path=getPath(parts[1]);
                            if(path !=null) System.out.println(parts[1]+" is "+path);
                            else System.out.println(parts[1]+" not found");
                        }
                    }
                }
                default -> {
                    String path = getPath(command);

                    if(path != null)
                    {
                        //It takes your array of strings (e.g., ["custom_exe_1234", "Alice"]) and tells Java,
                        //"I want to run the program named in index 0 and pass the rest of the strings as arguments to it".
                        ProcessBuilder pb=new ProcessBuilder(parts);

                        //Without this, the program (like custom_exe_1234) would run in the background,
                        // but you wouldn't see its output on your screen.
                        // By using inheritIO, when the program prints "Hello Alice",
                        // that message appears in your shell.
                        pb.inheritIO();

                        //It tells the Operating System to actually
                        //find the executable and begin running it as a separate process.
                        Process process=pb.start();

                        //It forces your while(true) loop to stop and wait
                        //until the external program finishes running.
                        process.waitFor();
                    }
                    else System.out.println(command+": command not found");
                }
            }


        }


    }
}
