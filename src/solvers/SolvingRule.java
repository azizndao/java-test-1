package solvers;

import models.Equation;
import models.Solutions;

import java.util.List;

public interface SolvingRule {
    Solutions solve(List<Equation> equations);
}
