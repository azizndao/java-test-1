package models;

import java.util.List;

public record Equation(List<Variable> variables, double constant) {
}
