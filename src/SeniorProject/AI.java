package SeniorProject;

public class AI implements I_AI{
    Player owner;

    public AI(Player player) {

        this.owner = player;
    }

    public String createMoves(boolean isInitial) {
        String moves = "";
        if(isInitial && !isPHMove(isInitial)) {

        }
        if(isPHMove(isInitial)) {

        }
        return moves;
    }

    public boolean isPHMove(boolean isInitial) {
        return isInitial && owner.getStructures().size() == 2;
    }
}
