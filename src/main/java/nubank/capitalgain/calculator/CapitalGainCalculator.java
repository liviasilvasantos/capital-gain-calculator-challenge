package nubank.capitalgain.calculator;

import nubank.capitalgain.domain.Operation;
import nubank.capitalgain.domain.OperationType;
import nubank.capitalgain.domain.Tax;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CapitalGainCalculator {

    private Map<String, ProcessOperation> operationMap= new HashMap<>();

    public List<Tax> processAll(List<Operation> operations) {
        var taxes = new ArrayList<Tax>();

        operations.forEach((operation) -> {

            operationMap.computeIfAbsent(operation.ticker(), k -> new ProcessOperation());

            var tax = process(operation);
            taxes.add(tax);
        });

        return taxes;
    }

    private Tax process(Operation operation) {

        var processOperation = operationMap.get(operation.ticker());

        if (OperationType.buy.equals(operation.operation()))
            return processOperation.buy(operation);

        return processOperation.sell(operation);
    }

    public ProcessOperation getProcessOperation(){
        operationMap.computeIfAbsent(Operation.DEFAULT_TICKER, k -> new ProcessOperation());
        return operationMap.get(Operation.DEFAULT_TICKER);
    }

    public ProcessOperation getProcessOperation(String ticker) {
        return operationMap.get(ticker);
    }
}