import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.nuvalence.RA.NRectangle;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RectangleOperationsTest {

    @Test
    public void intersectionTestHappyPath() {
        NRectangle first = new NRectangle(5, 3, 20, 20);
        NRectangle second = new NRectangle(7, 10, 20, 20);

        Optional<NRectangle> intersection = first.doesIntersectWith(second);
        assertFalse(intersection.isEmpty());

        NRectangle intersectionRectangle = intersection.get();
        assertTrue(intersectionRectangle.getBottomLeft().getX() == 7);
        assertTrue(intersectionRectangle.getBottomLeft().getY() == 10);
        assertTrue(intersectionRectangle.getTopRight().getX() == 25);
        assertTrue(intersectionRectangle.getTopRight().getY() == 23);
    }

    @Test
    public void intersectionTestNegativeContains() {
        NRectangle first = new NRectangle(5, 3, 20, 20);
        NRectangle second = new NRectangle(7, 10, 1, 1);

        Optional<NRectangle> intersection = first.doesIntersectWith(second);
        assertTrue(intersection.isEmpty());
    }

    @Test
    public void testIntersectionOverflow() {
        NRectangle first = new NRectangle(5, 3, Integer.MAX_VALUE, Integer.MAX_VALUE);
        NRectangle second = new NRectangle(7, 10, Integer.MAX_VALUE, Integer.MAX_VALUE);

        Optional<NRectangle> intersection = first.doesIntersectWith(second);
        assertFalse(intersection.isEmpty());

        NRectangle intersectionRectangle = intersection.get();
        assertTrue(intersectionRectangle.getBottomLeft().getX() == 7);
        assertTrue(intersectionRectangle.getBottomLeft().getY() == 10);
        assertTrue(intersectionRectangle.getTopRight().getX() ==   ((long)Integer.MAX_VALUE + 5));
        assertTrue(intersectionRectangle.getTopRight().getY() == ((long)Integer.MAX_VALUE + 3));
    }

    @Test
    public void containsTestHappyPath() {
        NRectangle first = new NRectangle(5, 3, 20, 20);
        NRectangle second = new NRectangle(7, 10, 1, 1);

        assertTrue(first.doesContain(second));
    }

    @Test
    public void containsTestNegative() {
        NRectangle first = new NRectangle(5, 3, 5, 5);
        NRectangle second = new NRectangle(7, 10, 5, 5);

        assertFalse(first.doesContain(second));
    }

    @Test
    public void TestInvalidArgument() {
        try {
            NRectangle second = new NRectangle(7, 7, 0, 0);
        }
        catch (IllegalArgumentException illegalArgumentException) {
            assertTrue(!illegalArgumentException.getMessage().isEmpty());
            return;

        }
        catch (Exception ex) {
            fail("expected IllegalArgumentException, got" + ex.getMessage());
        }
        fail("expected IllegalArgumentException, but no exception was thrown.");
    }

}