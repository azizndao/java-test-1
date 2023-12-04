package solvers;

import models.Equation;
import models.Solution;

import java.util.List;

public interface SolvingRule {
    List<Solution> solve(List<Equation> equations);
}
