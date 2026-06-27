package nubank.capitalgain;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import nubank.capitalgain.calculator.CapitalGainCalculator;
import nubank.capitalgain.domain.Operation;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class Main {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) {
        File outputFile = new File("output.txt");
        var calculator = new CapitalGainCalculator();

        try (var reader = new BufferedReader(new InputStreamReader(System.in));
             var writer = new PrintWriter(new BufferedWriter(new FileWriter(outputFile, StandardCharsets.UTF_8)))) {

            String currentLine;

            while ((currentLine = reader.readLine()) != null) {
                if (currentLine.trim().isEmpty()) continue;

                System.out.println("input = %s".formatted(currentLine));
                var operations = mapper.readValue(currentLine, new TypeReference<List<Operation>>() {
                });

                var taxes = calculator.processAll(operations);

                System.out.println("output = %s".formatted(mapper.writeValueAsString(taxes)));
                writer.println(mapper.writeValueAsString(taxes));
            }

            System.out.println("Processamento concluído. Verifique o arquivo: " + outputFile.getAbsolutePath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}