package parsers;

import models.Equation;
import models.Variable;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class EquationParser {

    final int numberOfVariables;

    public EquationParser(int numberOfVariables) {
        this.numberOfVariables = numberOfVariables;
    }

    public Equation parse(String equation) {
        // Ici j'enleve les espaces pour eviter les erreurs
        equation = equation.replaceAll("\\s+", "");

        // Je cree mon expression reguliere pour recuperer les coefficients et les inconnus
        var pattern = Pattern.compile("([+-]?\\d*\\.?\\d*)([a-zA-Z]?)");
        var matcher = pattern.matcher(equation);

        var variables = new ArrayList<Variable>();
        double constant = 0;

        while (matcher.find()) {
            String coefficientStr = matcher.group(1);
            String variable = matcher.group(2);

            if (variable.isEmpty() && coefficientStr.isEmpty()) continue;

            double coefficient;
            if (coefficientStr.isEmpty() || coefficientStr.equals("+")) {
                coefficient = 1;
            } else if (coefficientStr.equals("-")) {
                coefficient = -1;
            } else {
                coefficient = Double.parseDouble(coefficientStr);
            }

            if (!variable.isEmpty()) {
                variables.add(new Variable(variable, coefficient));
            } else {
                constant = coefficient;
            }
        }
        return new Equation(variables, constant);
    }
}
