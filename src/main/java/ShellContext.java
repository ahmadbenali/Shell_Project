import java.io.File;
import java.io.IOException;
import java.util.List;

import static java.lang.System.getenv;

//Encapsulation: Each shell object keeps track of its own location.
//Flexibility: You can have two shells open in different folders at the same time without them crashing into each other.
public class ShellContext {

    private String currentPath=System.getProperty("user.dir");
    private static final String home=System.getProperty("user.home");

    public  void ChangeDirectory(List<String> parts) throws IOException
    {
        String targetPath = parts.get(1);
        File newDir ;

        if(targetPath.startsWith("/"))
        {
            newDir=new File(targetPath);
            if(newDir.exists() && newDir.isDirectory())
                // To convert java.io.file to java.lang.String
                currentPath=newDir.getAbsolutePath();
            else System.out.println("cd: " + parts.get(1) + ": No such file or directory");
        }
        else if(targetPath.equals("~"))
        {
            currentPath=getenv("HOME");
        }
        else{
            newDir=new File(currentPath,targetPath);
            if(newDir.exists() && newDir.isDirectory())
                currentPath= newDir.getCanonicalPath();
            else System.out.println("cd: " + parts.get(1) + ": No such file or directory");

        }
    }

    public void setCurrentPath(String currentPath) {
        this.currentPath = currentPath;
    }

    public String getCurrentPath() {
        return this.currentPath;
    }

    public static String getHome() {
        return home;
    }
}
