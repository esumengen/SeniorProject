package SeniorProject.Actions;

import SeniorProject.*;

import java.io.Serializable;

public class RollDice implements IAction, Serializable {
    Player player;
    Board board;
    int[] dices = new int[2];

    public RollDice(Player player, int dice0, int dice1, Board board) {
        this.player = player;

        this.board = board;

        this.dices[0] = dice0;
        this.dices[1] = dice1;
    }

    @Override
    public void execute () {
        board.rollDice(player, dices[0], dices[1]);
    }

    @Override
    public String getCommand() {
        return null;
    }

    @Override
    public String toString() {
        return player + " RO " + dices[0] + "-" + dices[1];
    }
}