package models;

public record Variable(String name, Double coefficient) {
    @Override
    public String toString() {
        String coefStr = coefficient() == 1 ? "" : coefficient().toString();
        if (coefStr.endsWith(".0")) {
            coefStr = coefStr.substring(0, coefStr.length() - 2);
        } else if (coefStr.startsWith("+1")){
            coefStr = coefStr.substring(1);
        } else if (coefStr.startsWith("-1")){
            coefStr = "-" + coefStr.substring(1);
        }
        return String.format("%s%s", coefStr, name());
    }
}