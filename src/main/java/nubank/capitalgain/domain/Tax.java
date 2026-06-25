package nubank.capitalgain.domain;

public class Tax {

    private double value;

    public Tax(double tax) {
        this.value = tax;
    }

    public double getValue() {
        return this.value;
    }
}