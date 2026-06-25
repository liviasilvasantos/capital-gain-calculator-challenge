package nubank.capitalgain.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import static java.math.BigDecimal.valueOf;

public record Operation(OperationType operation, @JsonProperty("unit-cost") BigDecimal unitCost,
                        Integer quantity, String ticker) {

    public final static String DEFAULT_TICKER =  "DEFAULT_TICKER";

    public Operation(OperationType operation, BigDecimal unitCost,
                     Integer quantity) {
        this(operation, unitCost, quantity, DEFAULT_TICKER); // Define 18 como o valor padrão para idade
    }

    public BigDecimal value() {
        return unitCost.multiply(valueOf(quantity));
    }

}