import com.varentech.deploya.form.FormServlet;
import com.varentech.deploya.form.Resource;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.*;

public class FormServletTest {
    Resource res = new Resource();
    String file_name = "test.tar";
    Date date = new Date();
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");//format the date to have no spaces
    String formatted_time = formatter.format(date);

    @Before
    public void setUp() {
        res.entry.setTime(formatted_time);
    }

    @Test
    public void testRenaming() {
        assertEquals("test_" + formatted_time + ".tar", FormServlet.renaming(file_name));
    }

    @Test
    public void homeDirectoryTest(){
        FormServlet formServlet = new FormServlet();
        String home = System.getProperty("user.home");
        String path = home + File.separator + "Documents";
        assertEquals(path, formServlet.homeDirectory("~" + File.separator + "Documents"));
    }

    @Test
    public void createDirectoryDBTest(){
        FormServlet formServlet = new FormServlet();

        String path = "src/test/resources/Deploya/test.db";
        assertTrue(formServlet.createDirectoryDB(path));

        File file = new File("src/test/resources/Deploya");
        assertTrue(file.isDirectory());

        file.delete();

        path = "src/test/nope/Deploya/test.db";
        assertFalse(formServlet.createDirectoryDB(path));

        file = new File("src/test/nope/Deploya");
        assertFalse(file.exists());
    }

}

