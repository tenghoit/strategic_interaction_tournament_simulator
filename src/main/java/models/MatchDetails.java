package models;

import java.util.ArrayList;

public record MatchDetails(String opponentName, ArrayList<History> history) {}
