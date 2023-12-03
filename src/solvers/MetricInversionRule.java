package solvers;

import models.Equation;
import models.Solutions;
import models.Variable;

import java.util.List;

public class MetricInversionRule implements SolvingRule {
    @Override
    public Solutions solve(List<Equation> equations) {
        int numVariables = equations.get(0).variables().size();

        double[][] coefficientsMatrix = new double[equations.size()][numVariables];
        double[] constants = new double[equations.size()];

        for (int i = 0; i < equations.size(); i++) {
            Equation equation = equations.get(i);

            for (int j = 0; j < numVariables; j++) {
                coefficientsMatrix[i][j] = equation.variables().get(j).coefficient();
            }

            constants[i] = equation.constant();
        }

        double[][] inverseMatrix = invertMatrix(coefficientsMatrix);

        double[] solutionVector = multiplyMatrixVector(inverseMatrix, constants);

        List<Solutions.Solution> variables = createVariableRecords(equations.get(0).variables(), solutionVector);

        return new Solutions(variables);
    }

    public static double[][] invertMatrix(double[][] matrix) {
        int n = matrix.length;

        // Créer une matrice pour stocker l'inverse
        double[][] identityMatrix = new double[n][n];
        for (int i = 0; i < n; i++) {
            identityMatrix[i][i] = 1.0;
        }

        // Transformer la matrice en une matrice identité
        for (int i = 0; i < n; i++) {
            // Trouver le pivot (l'élément sur la diagonale)
            double pivot = matrix[i][i];

            // Diviser la ligne entière par le pivot
            for (int j = 0; j < n; j++) {
                matrix[i][j] /= pivot;
                identityMatrix[i][j] /= pivot;
            }

            // Soustraire des autres lignes pour obtenir des zéros en dessous du pivot
            for (int k = 0; k < n; k++) {
                if (k != i) {
                    double factor = matrix[k][i];
                    for (int j = 0; j < n; j++) {
                        matrix[k][j] -= factor * matrix[i][j];
                        identityMatrix[k][j] -= factor * identityMatrix[i][j];
                    }
                }
            }
        }

        return identityMatrix;
    }


    private double[] multiplyMatrixVector(double[][] matrix, double[] vector) {
        int numRows = matrix.length;
        int numCols = matrix[0].length;

        if (numCols != vector.length) {
            throw new IllegalArgumentException("Matrix and vector dimensions do not match.");
        }

        double[] result = new double[numRows];

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                result[i] += matrix[i][j] * vector[j];
            }
        }

        return result;
    }

    private List<Solutions.Solution> createVariableRecords(List<Variable> variables, double[] values) {
        List<Solutions.Solution> variableRecords = new java.util.ArrayList<>();

        for (int i = 0; i < variables.size(); i++) {
            variableRecords.add(new Solutions.Solution(variables.get(i).name(), values[i]));
        }

        return variableRecords;
    }
}
