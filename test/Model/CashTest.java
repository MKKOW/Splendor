package Model;

import Exceptions.IllegalCostException;
import Exceptions.NotEnoughCashException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests for Cash class")
class CashTest extends CostTest {

    @Test
    @DisplayName("Constructor should throw IllegalCashException when given negative value in any field")
    void constructorThrow() {
        assertAll(
                () -> assertThrows(IllegalCostException.class, () -> new Cash(-7, 12, -3, 4, 1, 2)),
                () -> assertThrows(IllegalCostException.class, () -> new Cash(1, -7, 0, 0, 4, 0)),
                () -> assertThrows(IllegalCostException.class, () -> new Cash(0, 12, 9, -2, -4, 1)),
                () -> assertThrows(IllegalCostException.class, () -> new Cash(-6, -0, 2, 3, 0, 6))
        );
    }

    @Test
    @DisplayName("add() should add values to corresponding fields")
    void add1() {
        Cash cash1 = new Cash(0, 1, 2, 3, 4, 0);
        Cash cash2 = new Cash(9, 0, 8, 0, 7, 1);
        cash1.add(cash2);

        assertEquals(9, cash1.getWhite());
        assertEquals(1, cash1.getGreen());
        assertEquals(10, cash1.getBlue());
        assertEquals(3, cash1.getBlack());
        assertEquals(11, cash1.getRed());
        assertEquals(1, cash1.getYellow());
    }

    @Test
    @DisplayName("add() should return Cash object")
    void add2() {
        Cash cash1 = new Cash(0, 1, 2, 3, 4, 1);
        Cash cash2 = new Cash(9, 0, 8, 0, 7, 2);
        assertEquals(new Cash(9, 1, 10, 3, 11, 3), cash1.add(cash2));
    }

    @Test
    @DisplayName("sub without yellow should subtract corresponding fields and return itself")
    void sub1() throws NotEnoughCashException {
        Cash cash = new Cash(6, 1, 2, 3, 4, 5);
        Cost cost = new Cost(3, 1, 2, 0, 1);
        cash.sub(cost);
        assertEquals(new Cash(3, 0, 0, 3, 3, 5), cash);
    }

    @Test
    @DisplayName("sub without yellow should throw NotEnoughCashException when any field become negative")
    void sub2() {
        Cash cash = new Cash(6, 0, 2, 3, 4, 5);
        Cost cost = new Cost(3, 1, 2, 0, 1);
        assertThrows(NotEnoughCashException.class, () -> cash.sub(cost));
    }

    @Test
    @DisplayName("sub with yellow should subtract corresponding fields")
    void sub4() throws NotEnoughCashException {
        Cash cash = new Cash(6, 4, 5, 3, 4, 5);
        Cost cost = new Cost(3, 1, 2, 0, 1);
        cash.sub(cost, 0);
        assertEquals(new Cash(3, 3, 3, 3, 3, 5), cash);
    }

    @Test
    @DisplayName("sub with yellow should throw NotEnoughCashException when any field become negative and all yellow are used")
    void sub6() {
        Cash cash = new Cash(0, 0, 1, 3, 0, 5);
        Cost cost = new Cost(3, 1, 2, 0, 1);
        assertThrows(NotEnoughCashException.class, () -> cash.sub(cost, 0));
    }

    @Test
    @DisplayName("sub with yellow should throw IllegalArgumentExceptions when maxYellow > yellow")
    void sub7() {
        Cash cash = new Cash(0, 0, 1, 3, 0, 2);
        Cost cost = new Cost(3, 1, 2, 0, 1);
        assertThrows(IllegalArgumentException.class, () -> cash.sub(cost, 3));
    }

    @Test
    @DisplayName("sub with yellow should return Cash object")
    void sub8() {
        Cash cash1 = new Cash(0, 1, 2, 3, 4, 1);
        Cash cash2 = new Cash(9, 0, 8, 0, 7, 2);
        assertEquals(new Cash(9, 1, 10, 3, 11, 3), cash1.add(cash2));
    }

    @Test
    void enough() {
    }

    @Nested
    @DisplayName("sub with yellow should use yellow coins properly")
    class subWithYellowCoinsTest {
        @Test
        @DisplayName("Test 1 - Should use some yellow gems")
        void subyellow1() throws NotEnoughCashException {
            Cash cash = new Cash(6, 0, 2, 3, 4, 5);
            Cost cost = new Cost(3, 1, 2, 0, 1);
            assertEquals(new Cash(3, 0, 0, 3, 3, 4), cash.sub(cost, 1));
            assertEquals(4, cash.getYellow());
        }

        @Test
        @DisplayName("Test 2 - Should use all yellow gems")
        void subyellow2() throws NotEnoughCashException {
            Cash cash = new Cash(2, 0, 2, 0, 3, 5);
            Cost cost = new Cost(3, 1, 2, 2, 4);
            assertEquals(new Cash(0, 0, 0, 0, 0, 0), cash.sub(cost, 5));
            assertEquals(0, cash.getYellow());
        }

        @Test
        @DisplayName("Test 3 - Should not use any yellow gems")
        void subyellow3() throws NotEnoughCashException {
            Cash cash = new Cash(8, 2, 6, 3, 4, 5);
            Cost cost = new Cost(3, 1, 2, 0, 1);
            assertEquals(new Cash(5, 1, 4, 3, 3, 5), cash.sub(cost, 3));
            assertEquals(5, cash.getYellow());
        }
    }
}