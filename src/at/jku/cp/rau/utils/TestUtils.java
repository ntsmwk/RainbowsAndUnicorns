package at.jku.cp.rau.utils;

import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Assert;

public abstract class TestUtils {
    private TestUtils() {

    }

    public static <T> void assertListEquals(List<T> e, List<T> a) {
        if (e.size() != a.size()) {
            System.out.println(String.format("list sizes differ! (%d != %d)", e.size(), a.size()));
            for (int i = 0; i < Math.min(e.size(), a.size()); i++) {
                System.out.println("------------------------------------");
                System.out.println(String.format("expected:%d", i));
                System.out.println(e.get(i));
                System.out.println(String.format("actual:%d", i));
                System.out.println(a.get(i));
                System.out.println("----------------");
            }
            fail();
        }
        StringBuilder sb = new StringBuilder();
        boolean fail = false;
        for (int i = 0; i < e.size(); i++) {
            T te = e.get(i);
            T ta = a.get(i);

            if (!te.equals(ta)) {
                sb.append("pos: " + i + "\texpected: " + te + "\tactual: " + ta + "\n");
                // System.out.println("elements at position '" + i +
                // "' differ!");
                // System.out.println("expected : " + te);
                // System.out.println("actual   : " + ta);
                // fail();
                fail = true;
            }
        }
        if (fail) {
            System.out.println(sb.toString());
            fail();
        }
    }

    public static <T> void checkOptimalPath(List<T> oneOptimal, List<T> actual, boolean uniqueOptimal) {
        if (uniqueOptimal)
            TestUtils.assertListEquals(oneOptimal, actual);
        else {
            checkPathValid(oneOptimal, actual);
            Assert.assertTrue("Actual path is longer than optimal path", actual.size() == oneOptimal.size());
        }
    }

    public static <T> void checkPathValid(List<T> oneOptimal, List<T> actual) {
        Assert.assertNotNull("Actual path is null", actual);
        Assert.assertFalse("Actual path is empty but expected path not", actual.isEmpty() && !oneOptimal.isEmpty());

        if (!oneOptimal.isEmpty()) {
            Assert.assertEquals("Actual path starts with the wrong element", oneOptimal.get(0), actual.get(0));
            Assert.assertEquals("Actual path ends with the wrong element", oneOptimal.get(oneOptimal.size() - 1),
                    actual.get(actual.size() - 1));

            Assert.assertTrue("Actual path is shorter than optimal path --> something is wrong",
                    actual.size() >= oneOptimal.size());

            for (int i = 0; i < actual.size(); i++) {
                T t1 = actual.get(i);
                Assert.assertNotNull("No element in the path is allowed to be null", t1);
                for (int j = i + 1; j < actual.size(); j++) {
                    Assert.assertNotEquals("Elements at positions" + i + " and " + j + " are equal.", t1, actual.get(j));
                }
            }
        }
    }
}
