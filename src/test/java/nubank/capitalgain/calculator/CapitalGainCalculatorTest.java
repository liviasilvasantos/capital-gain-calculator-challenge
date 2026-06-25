package nubank.capitalgain.calculator;

import nubank.capitalgain.domain.Operation;
import nubank.capitalgain.domain.OperationType;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.math.BigDecimal.ZERO;
import static java.math.BigDecimal.valueOf;
import static java.math.RoundingMode.HALF_UP;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CapitalGainCalculatorTest {

    @Test
    @DisplayName("given a new calculator when no operations run then state remains zeroed")
    void testEmptyInitialState() {
        var calculator = new CapitalGainCalculator();

        assertEquals(ZERO, calculator.getProcessOperation().getCurrentAverage());
        assertEquals(0, calculator.getProcessOperation().getQuantity());
    }

    @Test
    @DisplayName("when two buy operations happen, the average should be correctly calculated")
    void weightedAverageAfterTwoBuys() {
        var calculator = new CapitalGainCalculator();

        calculator.processAll(List.of(
                new Operation(OperationType.buy, valueOf(10000), 2),
                new Operation(OperationType.buy, valueOf(20000), 3)
        ));

        assertEquals(valueOf(16000), calculator.getProcessOperation().getCurrentAverage().setScale(0));
        assertEquals(5, calculator.getProcessOperation().getQuantity());
    }

    @Test
    @DisplayName("when a buy operations is processed then the total quantity is correctly updated")
    void buyOperationUpdatesQuantity() {
        var calculator = new CapitalGainCalculator();

        // initial quantity is zero
        assertEquals(0, calculator.getProcessOperation().getQuantity());

        // process a buy of 4 units
        calculator.processAll(List.of(new Operation(OperationType.buy, valueOf(123.45), 4)));

        // quantity should reflect the bought units
        assertEquals(4, calculator.getProcessOperation().getQuantity());
    }

    @Test
    @DisplayName("when all stocks sold then quantity becomes zero and average should be reset")
    void testAverageResetsWhenQuantityBecomesZero() {
        var calculator = new CapitalGainCalculator();
        // Buy stocks at $10.00
        calculator.processAll(List.of(new Operation(OperationType.buy, valueOf(10.00), 10000)));

        assertEquals(valueOf(10.00).setScale(2, HALF_UP), calculator.getProcessOperation().getCurrentAverage());
        assertEquals(10000, calculator.getProcessOperation().getQuantity());

        // Sell all stocks - this should reset average to ZERO
        calculator.processAll(List.of(new Operation(OperationType.sell, valueOf(15.00), 10000)));

        // Verify quantity is 0
        assertEquals(0, calculator.getProcessOperation().getQuantity());
        assertEquals(ZERO, calculator.getProcessOperation().getCurrentAverage(),
                "Average must be reset to ZERO when all stocks are sold");
    }

    @Test
    @DisplayName("when the operation is sell then global quantity decreases accordingly")
    void sellReducesQuantity() {
        var calculator = new CapitalGainCalculator();

        // buy 5 units
        calculator.processAll(List.of(new Operation(OperationType.buy, valueOf(1000), 5)));

        assertEquals(5, calculator.getProcessOperation().getQuantity());

        // sell 3 units -> remaining should be 2
        calculator.processAll(List.of(new Operation(OperationType.sell, valueOf(1200), 3)));

        assertEquals(2, calculator.getProcessOperation().getQuantity());
    }

    @Test
    @DisplayName("when selling with profit then tax equals 20% of the gain")
    void sellWithCalculateGainCalculatesTax() {
        var calculator = new CapitalGainCalculator();

        // buy 2 units at 10000 -> average 10000, quantity 2
        calculator.processAll(List.of(new Operation(OperationType.buy, valueOf(10000), 2)));

        // sell 1 unit at 15000 -> profit = 15000 - 10000 = 5000 -> tax = 0.2 * 5000 = 1000
        var result = calculator.processAll(List.of(new Operation(OperationType.sell, valueOf(15000), 1)));

        assertEquals(1000d, result.get(0).getValue());
    }

    @Test
    @DisplayName("when selling at loss (bellow average on buying) should not pay taxes")
    void testSellWithLossHasNoTax() {
        var calculator = new CapitalGainCalculator();
        var operationBuy = new Operation(OperationType.buy, valueOf(1000), 1);
        var operationSell = new Operation(OperationType.sell, valueOf(500), 1);

        var result = calculator.processAll(List.of(operationBuy, operationSell));

        assertEquals(0d, result.getLast().getValue());
        assertEquals(0, calculator.getProcessOperation().getQuantity());
    }

}
