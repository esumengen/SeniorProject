package SeniorProject.DevelopmentCards;

public enum DevelopmentCardType {
    KNIGHT, ROADBUILDING, YEAROFPLENTY, MONOPOLY, VICTORYPOINT;

    @Override
    public String toString() {
        return String.valueOf(super.toString().charAt(0));
    }
}