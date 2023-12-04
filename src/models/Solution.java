package models;

import java.util.List;

public record Solution(String name, double value) {


        @Override
        public String toString() {
            var valueStr = "%.2f".formatted(value());
            if (valueStr.endsWith(".0")) valueStr = valueStr.substring(0, valueStr.length() - 2);
            return "%s = %s".formatted(name, valueStr);
        }

        public static String formatSystemSolution(List<Solution> solitions) {
            return "S=" + solitions.toString()
            .replace("[", "{")
            .replace("]", "}");
        }
}
