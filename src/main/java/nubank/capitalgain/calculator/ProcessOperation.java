package nubank.capitalgain.calculator;

import nubank.capitalgain.domain.Operation;
import nubank.capitalgain.domain.Tax;

import java.math.BigDecimal;

import static java.math.BigDecimal.ZERO;
import static java.math.BigDecimal.valueOf;
import static java.math.RoundingMode.HALF_UP;

public class ProcessOperation {

    private static final BigDecimal TAX = valueOf(0.20D).setScale(2, HALF_UP);

    private BigDecimal average;
    private Integer quantity;

    public ProcessOperation() {
        this.average = ZERO;
        this.quantity = 0;
    }

    public Tax buy(Operation operation) {
        average = calculateAverage(operation.value(), operation.quantity());
        quantity += operation.quantity();

        return new Tax(0);
    }

    private BigDecimal calculateAverage(BigDecimal value, Integer quantity) {
        return ((valueOf(this.quantity).multiply(average)).add(value))
                .divide(valueOf(quantity + this.quantity), 2, HALF_UP);
    }

    public Tax sell(Operation operation) {
        quantity -= operation.quantity();

        // Calculate profit (gain)
        var operationGain = calculateGain(operation);

        // reset of currentAverage when all stocks are sold
        if (quantity == 0) {
            average = ZERO;
        }
        return new Tax(calculateTax(operationGain));
    }

    private BigDecimal calculateGain(Operation operation) {
        BigDecimal accumulatedValue = average.multiply(valueOf(operation.quantity()));
        var operationGain = operation.value().subtract(accumulatedValue);

        return operationGain;
    }

    private double calculateTax(BigDecimal operationGain) {
        // fix bug
        if (operationGain.compareTo(ZERO) < 0) {
            return 0.0;
        }

        return TAX.multiply(operationGain).doubleValue();
    }

    public BigDecimal getCurrentAverage() {
        return average;
    }

    public Integer getQuantity() {
        return quantity;
    }
}
