package nubank.capitalgain;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import nubank.capitalgain.calculator.CapitalGainCalculator;
import nubank.capitalgain.domain.Operation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class Main {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) {
        var calculator = new CapitalGainCalculator();

        try (var reader = new BufferedReader(new InputStreamReader(System.in))) {
            String currentLine;

            while ((currentLine = reader.readLine()) != null) {
                if (currentLine.trim().isEmpty()) continue;

                var operations = mapper.readValue(currentLine, new TypeReference<List<Operation>>() {
                });

                var taxes = calculator.processAll(operations);

                System.out.println(mapper.writeValueAsString(taxes));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}