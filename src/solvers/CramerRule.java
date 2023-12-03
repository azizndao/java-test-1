package solvers;

import exceptions.NoSolutionException;
import models.Equation;
import models.Solutions;
import models.Variable;

import java.util.List;

public class CramerRule implements SolvingRule {
    @Override
    public Solutions solve(List<Equation> equations) {
        int numVariables = equations.get(0).variables().size();

        // Creer des matrices pour le système principal et chaque variable
        double[][] mainMatrix = new double[equations.size()][numVariables];
        double[] constants = new double[equations.size()];

        for (int i = 0; i < equations.size(); i++) {
            Equation equation = equations.get(i);

            for (int j = 0; j < numVariables; j++) {
                mainMatrix[i][j] = equation.variables().get(j).coefficient();
            }

            // Remplacer la colonne de la matrice principale avec le tableau de constantes
            constants[i] = equation.constant();
        }

        // Calculer la déterminante de la matrice principale
        double mainDeterminant = calculateDeterminant(mainMatrix);

        if (mainDeterminant == 0.0) {
            throw new NoSolutionException();
        }

        // Calculer les valeurs des variables
        var variableValues = calculateVariableValues(equations, mainMatrix, constants, mainDeterminant);

        // Create a list of Variable records with names and values
        var solutions = createVariableRecords(equations.get(0).variables(), variableValues);

        return new Solutions(solutions);
    }

    private double calculateDeterminant(double[][] matrix) {
        int n = matrix.length;

        if (n != matrix[0].length) throw new IllegalArgumentException("The matrix is not square.");

        if (n == 1) return matrix[0][0];

        double determinant = 0.0;
        int sign = 1;

        for (int i = 0; i < n; i++) {
            double[][] minor = getMinor(matrix, i);
            determinant += sign * matrix[0][i] * calculateDeterminant(minor);
            sign *= -1;
        }

        return determinant;
    }

    private List<Double> calculateVariableValues(List<Equation> equations, double[][] mainMatrix,
                                                 double[] constants, double mainDeterminant) {
        List<Double> variableValues = new java.util.ArrayList<>();

        for (int i = 0; i < equations.get(0).variables().size(); i++) {
            double[][] modifiedMatrix = new double[equations.size()][equations.get(0).variables().size()];

            // Remplacer la colonne de la matrice principale avec le tableau de constantes
            for (int j = 0; j < equations.size(); j++) {
                System.arraycopy(mainMatrix[j], 0, modifiedMatrix[j], 0, equations.get(0).variables().size());
                modifiedMatrix[j][i] = constants[j];
            }

            // Calculer la déterminante de la matrice modifie
            double variableDeterminant = calculateDeterminant(modifiedMatrix);

            double variableValue = variableDeterminant / mainDeterminant;
            variableValues.add(variableValue);
        }

        return variableValues;
    }

    private double[][] getMinor(double[][] matrix, int col) {
        int n = matrix.length;
        double[][] minor = new double[n - 1][n - 1];

        for (int i = 0, p = 0; i < n; i++) {
            if (i == 0) continue;

            for (int j = 0, q = 0; j < n; j++) {
                if (j == col) {
                    continue;
                }

                minor[p][q] = matrix[i][j];
                q++;
            }

            p++;
        }

        return minor;
    }

    private List<Solutions.Solution> createVariableRecords(List<Variable> variables, List<Double> values) {
        List<Solutions.Solution> solutionRecords = new java.util.ArrayList<>();

        for (int i = 0; i < variables.size(); i++) {
            solutionRecords.add(new Solutions.Solution(variables.get(i).name(), values.get(i)));
        }

        return solutionRecords;
    }
}
