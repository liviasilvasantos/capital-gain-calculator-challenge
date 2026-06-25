package nubank.capitalgain.calculator;

import nubank.capitalgain.domain.Operation;
import nubank.capitalgain.domain.OperationType;
import nubank.capitalgain.domain.Tax;

import java.util.ArrayList;
import java.util.List;

public class CapitalGainCalculator {

    private ProcessOperation processOperation = new ProcessOperation();

    public List<Tax> processAll(List<Operation> operations) {
        var taxes = new ArrayList<Tax>();

        operations.forEach((operation) -> {
            var tax = process(operation);
            taxes.add(tax);
        });

        return taxes;
    }

    private Tax process(Operation operation) {
        if (OperationType.buy.equals(operation.operation()))
            return processOperation.buy(operation);

        return processOperation.sell(operation);
    }

    public ProcessOperation getProcessOperation() {
        return processOperation;
    }
}