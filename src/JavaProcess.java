import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public final class JavaProcess {
    private JavaProcess() {}

    public static Process exec(Class klass, LinkedList args) throws IOException,
            InterruptedException {
        String javaHome = System.getProperty("java.home");
        String javaBin = javaHome +
                File.separator + "bin" +
                File.separator + "java";
        String classpath = System.getProperty("java.class.path");
        String className = klass.getName();

        List<String> command = new LinkedList<String>();
        command.add(javaBin);
        command.add("-cp");
        command.add(classpath);
        command.add(className);
        command.addAll(args);
        ProcessBuilder builder = new ProcessBuilder(command);
        Process process = builder.inheritIO().start();
        Thread.sleep(1000);
        return process;
    }
}
