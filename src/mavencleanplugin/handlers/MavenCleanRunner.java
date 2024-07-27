package mavencleanplugin.handlers;

//import org.apache.maven.shared.invoker.DefaultInvocationRequest;
//import org.apache.maven.shared.invoker.DefaultInvoker;
//import org.apache.maven.shared.invoker.InvocationRequest;
//import org.apache.maven.shared.invoker.InvocationResult;
//import org.apache.maven.shared.invoker.Invoker;
////import org.apache.maven.shared.invoker.MavenInvocationException;
//import org.apache.maven.shared.invoker.MavenInvocationException;
import java.io.BufferedReader;

import org.eclipse.ui.commands.*;
import java.io.IOException;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.resources.ResourcesPlugin.*;
import java.io.InputStreamReader;
import java.io.File;
import org.eclipse.ui.console.*;
import org.eclipse.ui.*;
import java.util.Collections;
import java.nio.file.Paths;
public class MavenCleanRunner {
    public void run(String[] args) throws PartInitException {
    	  // Command to run
        String command = "mvn clean install";
        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        IConsoleView view = (IConsoleView) page.showView(IConsoleConstants.ID_CONSOLE_VIEW);
        MessageConsole myConsole = findConsole("My Console");
        view.display(myConsole);
       
        MessageConsoleStream out = myConsole.newMessageStream();
       

        // Execute the command
        try {
            ProcessBuilder processBuilder = new ProcessBuilder();
            // Set the command to execute
            processBuilder.command("cmd.exe", "/c", command);
            // Set the working directory to the current folder
//            ResourcesPlugin.getWorkspace().getRoot().getProjects();
            String dir = ResourcesPlugin.getWorkspace().getRoot().getLocation().toPath().toString();
            String workingdir = ResourcesPlugin.getWorkspace().getRoot().getProjects()[0].getFullPath().toFile().getPath();
            System.out.println(dir + workingdir);
            File folder = new File(dir + "\\" + workingdir);
            System.out.println(folder);
//            File workingdir = ( ResourcesPlugin.getWorkspace().getRoot().getProject().getFullPath().toPath().toAbsolutePath().toFile());
            processBuilder.directory(folder);
            
           

            // Start the process
            Process process = processBuilder.start();

            // Capture the output
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                out.println(line);
            }

            // Capture any error messages
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            while ((line = errorReader.readLine()) != null) {
                System.err.println(line);
            }

            // Wait for the process to finish and get the exit value
            int exitCode = process.waitFor();
            System.out.println("Exited with code: " + exitCode);
           

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
       
    }
    private MessageConsole findConsole(String name) {
        ConsolePlugin plugin = ConsolePlugin.getDefault();
        IConsoleManager conMan = plugin.getConsoleManager();
        IConsole[] existing = conMan.getConsoles();
        for (int i = 0; i < existing.length; i++)
            if (name.equals(existing[i].getName()))
                return (MessageConsole) existing[i];
        // No console found, so create a new one
        MessageConsole myConsole = new MessageConsole(name, null);
        conMan.addConsoles(new IConsole[]{myConsole});
        return myConsole;
    }
   
}