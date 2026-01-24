import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        // TODO: Uncomment the code below to pass the first stage

        boolean flag = true;
        while(flag)
        {
            System.out.print("$ ");
            Scanner scanner=new Scanner(System.in);
            String command=scanner.nextLine();

            // This "exit".equals(command) for NULL safe
            if("exit".equals(command)) {flag=false;}
            else System.out.println(command+": command not found");


        }


    }
}
