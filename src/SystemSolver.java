import exceptions.NoSolutionException;
import models.Equation;
import models.Solution;
import parsers.EquationParser;
import solvers.CramerRule;
import solvers.GaussPivotRule;
import solvers.MetricInversionRule;
import solvers.SolvingRule;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SystemSolver {

    public void start() {
        var scanner = new Scanner(System.in);
        int numberOfVariables = getGetNumberOfVariables();
        List<Equation> equations = getEquations(numberOfVariables);
        SolvingRule solvingRule = getSolvingMethod(true);
        try {
            var solution = solvingRule.solve(equations);
            System.out.println(Solution.formatSystemSolution(solution));
        } catch (NoSolutionException e) {
            System.out.println("Pas de solution");
        }
        System.out.println("voulez-vous recommencer, tape 'q' pour quitter");
        if (!scanner.next().equals("q")) {
            scanner.close();
            start();
        } else {
            scanner.close();
        }
    }

    private SolvingRule getSolvingMethod(boolean first) {
        if (first) {
            System.out.println("Methode: [1] Cramer, [2] Inversion de metrice, [3] Pivot de Gauss");
        }
        System.out.print("Methode: ");

        try (var scanner = new Scanner(System.in)) {
            var methodId = scanner.nextInt();
            if (methodId == 1)
                return new CramerRule();
            if (methodId == 2)
                return new MetricInversionRule();
            if (methodId == 3)
                return new GaussPivotRule();
            throw new IllegalArgumentException();
        } catch (Exception e) {
            System.out.println("Methode invalide");
            return getSolvingMethod(false);
        }
    }

    private List<Equation> getEquations(int numberOfVariables) {
        var scanner = new Scanner(System.in);
        var equations = new ArrayList<Equation>(numberOfVariables);
        var parser = new EquationParser(numberOfVariables);
        int i = 0;
        while (i < numberOfVariables) {
            System.out.printf("Equation %d: ", i + 1);
            var input = scanner.nextLine();
            var equation = parser.parse(input.replace("\n", ""));
            if (numberOfVariables != equation.variables().size()) {
                System.out.println("Le nombre d'inconnus n'est pas le bon");
                continue;
            }
            equations.add(equation);
            i++;
        }
        scanner.close();
        return equations;
    }

    private int getGetNumberOfVariables() {
        try (var scanner = new Scanner(System.in)) {
            System.out.print("Donner le nombre incunnus (2 ou 3): ");
            int numberOfVariables = scanner.nextInt();
            if (numberOfVariables != 2 && numberOfVariables != 3)
                throw new IllegalArgumentException();
            return numberOfVariables;
        } catch (Exception e) {
            return getGetNumberOfVariables();
        }
    }
}
