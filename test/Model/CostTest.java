package Model;

import Exceptions.IllegalCostException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests for Cost class")
class CostTest {

    @Test
    @DisplayName("Constructor should throw IllegalArgumentExemption when given negative value in any field")
    void constructorThrow() {
        assertAll(
                () -> assertThrows(IllegalCostException.class, () -> new Cost(-7, 12, -3, 4, 1)),
                () -> assertThrows(IllegalCostException.class, () -> new Cost(1, -7, 0, 0, 4)),
                () -> assertThrows(IllegalCostException.class, () -> new Cost(0, 12, 9, -2, -4)),
                () -> assertThrows(IllegalCostException.class, () -> new Cost(-6, -0, 2, 3, 0))
        );
    }

    @Test
    @DisplayName("add() should add values to corresponding fields")
    void add1() {
        Cost cost0 = new Cost(0, 1, 2, 3, 4);
        Cost cost1 = new Cost(9, 0, 8, 0, 7);
        cost0.add(cost1);

        assertEquals(9, cost0.getWhite());
        assertEquals(1, cost0.getGreen());
        assertEquals(10, cost0.getBlue());
        assertEquals(3, cost0.getBlack());
        assertEquals(11, cost0.getRed());
    }

    @Test
    @DisplayName("add() should return object")
    void add2() {
        Cost cost0 = new Cost(0, 1, 2, 3, 4);
        Cost cost1 = new Cost(9, 0, 8, 0, 7);
        assertEquals(new Cost(9, 1, 10, 3, 11), cost0.add(cost1));
    }

    @Test
    @DisplayName("sum() should properly sum all values")
    void sum() {
        assertEquals(0, new Cost(0, 0, 0, 0, 0).sum());
        assertEquals(23, new Cost(5, 1, 8, 2, 7).sum());
        assertEquals(11, new Cost(1, 4, 2, 0, 4).sum());
    }

    @Test
    @DisplayName("equals() should be symmetric")
    public void testEqualsSymmetric() {
        Cost x = new Cost(5, 4, 3, 2, 1);
        Cost y = new Cost(5, 4, 3, 2, 1);
        assertNotSame(x, y);
        assertTrue(x.equals(y) && y.equals(x));
    }

    @Test
    @DisplayName("hashCode() should be symmetric")
    void testHashCodeSymmetric() {
        Cost x = new Cost(5, 4, 3, 2, 1);
        Cost y = new Cost(5, 4, 3, 2, 1);
        assertNotSame(x, y);
        assertEquals(x.hashCode(), y.hashCode());
    }
}