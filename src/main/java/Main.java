import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {


        boolean flag = true;
        while(flag)
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
