package nubank.capitalgain.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import static java.math.BigDecimal.valueOf;

public record Operation(OperationType operation, @JsonProperty("unit-cost") BigDecimal unitCost,
                        Integer quantity) {

    public BigDecimal value() {
        return unitCost.multiply(valueOf(quantity));
    }

}