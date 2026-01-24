import java.util.Scanner;

public class Main {
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
                        if ("exit".equals(parts[1]) || "echo".equals(parts[1]) || "type".equals(parts[1]))
                            System.out.println(parts[1] + " is a shell builtin");
                        else System.out.println(parts[1]+ ": not found");
                    }
                }
                default -> System.out.println(command+": command not found");
            }
//            if("exit".equals(command)) {
//                System.exit(0);
//                //flag=false;}
//            }
//
//            else if("echo".equals(command))
//            {
//                System.out.println();
//            }
//
//            else System.out.println(command+": command not found");


        }


    }
}
