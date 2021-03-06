import junit.framework.TestCase;
import org.jvnet.winp.WinProcess;
import org.jvnet.winp.WinpException;

/**
 * @author Kohsuke Kawaguchi
 */
public class TheTest extends TestCase {
    public void testEnumProcesses() {
        for (WinProcess p : WinProcess.all()) {
            System.out.print(p.getPid());
            System.out.print(' ');
        }
        System.out.println();
    }

    public void testGetCommandLine() {
        for (WinProcess p : WinProcess.all()) {
            if(p.getPid()<10)   continue;
            System.out.println(p.getCommandLine());
        }
    }

    public void testErrorHandling() {
        try {
            new WinProcess(0).getEnvironmentVariables();
            fail();
        } catch (WinpException e) {
            // exception expected
            e.printStackTrace();
        }
    }

    public void testKill() throws Exception {
        ProcessBuilder pb = new ProcessBuilder("notepad");
        pb.environment().put("TEST","foobar");
        Process p = pb.start();
        try {
            WinProcess wp = new WinProcess(p);
            System.out.println("pid="+wp.getPid());
            
            System.out.println(wp.getCommandLine());
            assertTrue(wp.getCommandLine().contains("notepad"));

            System.out.println(wp.getEnvironmentVariables());
            assertEquals("foobar",wp.getEnvironmentVariables().get("TEST"));

            Thread.sleep(100);
            new WinProcess(p).killRecursively();
        } finally {
//            p.destroy();
        }
    }

    static {
        WinProcess.enableDebugPrivilege();
    }
}
