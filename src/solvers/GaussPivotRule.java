package solvers;

import models.Equation;
import models.Solution;

import java.util.List;

public class GaussPivotRule implements SolvingRule {
    @Override
    public  List<Solution> solve(List<Equation> equations) {
        int numVariables = equations.get(0).variables().size();

        double[][] augmentedMatrix = new double[equations.size()][numVariables + 1];

        for (int i = 0; i < equations.size(); i++) {
            Equation equation = equations.get(i);

            for (int j = 0; j < numVariables; j++) {
                augmentedMatrix[i][j] = equation.variables().get(j).coefficient();
            }

            augmentedMatrix[i][numVariables] = equation.constant();
        }

        for (int i = 0; i < numVariables; i++) {
            int pivotRow = i;
            for (int j = i + 1; j < equations.size(); j++) {
                if (Math.abs(augmentedMatrix[j][i]) > Math.abs(augmentedMatrix[pivotRow][i])) {
                    pivotRow = j;
                }
            }

            double[] temp = augmentedMatrix[i];
            augmentedMatrix[i] = augmentedMatrix[pivotRow];
            augmentedMatrix[pivotRow] = temp;

            // Make the diagonal element 1
            double pivot = augmentedMatrix[i][i];
            for (int j = i; j <= numVariables; j++) {
                augmentedMatrix[i][j] /= pivot;
            }

            for (int j = 0; j < equations.size(); j++) {
                if (j != i) {
                    double factor = augmentedMatrix[j][i];
                    for (int k = i; k <= numVariables; k++) {
                        augmentedMatrix[j][k] -= factor * augmentedMatrix[i][k];
                    }
                }
            }
        }

        List<Solution> variables = new java.util.ArrayList<>();
        for (int i = 0; i < equations.size(); i++) {
            variables.add(new Solution(equations.get(i).variables().get(i).name(), augmentedMatrix[i][numVariables]));
        }

        return variables;
    }
}
