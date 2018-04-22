import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

public class ControllerTest {

    private static Controller controller;

    @Before
    public void before() {
        controller = new Controller();
    }

    @Test
    public void testGet() {
        Assert.assertEquals(Collections.EMPTY_LIST, controller.Get("A"));

        controller.Put("A","e");
        Assert.assertEquals(Collections.singletonList("e").toString(), controller.Get("A").toString());

        controller.Put("B","d");
        controller.Put("A", "c");
        Assert.assertEquals(Arrays.asList("e", "c").toString(), controller.Get("A").toString());
        Assert.assertEquals(Collections.singletonList("d").toString(), controller.Get("B").toString());

        controller.Put("A", "cc");
        controller.Put("A", "bc");
        controller.Put("A", "ac");
        controller.Put("A", "dc");

        Assert.assertEquals(
                Arrays.asList("e", "c", "cc", "bc", "ac", "dc").toString(),
                controller.Get("A").toString()
        );
    }

    @Test
    public void testGetWithTime() {
        long time0 = System.nanoTime();

        controller.Put("A","c");
        long time1 = System.nanoTime();

        controller.Put("A", "e");
        long time2 = System.nanoTime();

        Assert.assertEquals(Collections.EMPTY_LIST.toString(), controller.GetWithTime("A", time0).toString());
        Assert.assertEquals(Arrays.asList("c").toString(), controller.GetWithTime("A", time1).toString());
        Assert.assertEquals(Arrays.asList("c","e").toString(), controller.GetWithTime("A", time2).toString());
    }

    @Test
    public void testDiff() throws Exception {
        long time0 = System.nanoTime();

        controller.Put("A","c");
        long time1 = System.nanoTime();

        controller.Put("B","d");
        controller.Put("A", "e");
        long time2 = System.nanoTime();

        controller.Put("A", "cc");
        controller.Put("A", "bc");
        controller.Put("A", "ac");
        controller.Put("A", "dc");
        long time3 = System.nanoTime();

        Assert.assertEquals(Arrays.asList("c").toString(), controller.Diff("A", time0, time1).toString());
        Assert.assertEquals(Arrays.asList("c", "e").toString(), controller.Diff("A", time0, time2).toString());
        Assert.assertEquals(Arrays.asList("e").toString(), controller.Diff("A", time1, time2).toString());
        Assert.assertEquals(
                Arrays.asList("cc", "bc", "ac", "dc").toString(),
                controller.Diff("A", time2, time3).toString()
        );
    }

    @Test
    public void testDelete() {
        controller.Put("A","c");
        Assert.assertEquals(Arrays.asList("c").toString(), controller.Get("A").toString());

        Assert.assertTrue(controller.Delete("A"));
        Assert.assertEquals(Collections.EMPTY_LIST.toString(), controller.Get("A").toString());
    }

    @Test
    public void testDeleteWithValue() {
        controller.Put("A","c");
        controller.Put("A", "e");

        Assert.assertTrue(controller.DeleteWithValue("A", "c"));
        Assert.assertEquals(Arrays.asList("e").toString(), controller.Get("A").toString());

        Assert.assertFalse(controller.DeleteWithValue("A", "c"));
        Assert.assertTrue(controller.DeleteWithValue("A", "e"));
        Assert.assertEquals(Collections.EMPTY_LIST.toString(), controller.Get("A").toString());
    }

    @Test(expected = Exception.class)
    public void testDiffError() throws Exception{
        long time0 = System.nanoTime();
        controller.Put("A","c");
        long time1 = System.nanoTime();
        controller.Diff("A", time1, time0);
    }
}
