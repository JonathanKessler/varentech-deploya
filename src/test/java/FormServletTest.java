import com.varentech.deploya.form.FormServlet;
import com.varentech.deploya.form.Resource;
import org.junit.Before;
import org.junit.Test;

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

    public void tearDown(  ) {
    }
}
