import ai.BoardEvaluator;

import java.util.HashMap;

public class App {
    public static void main(String[] args) {
        Game.play(new OurEvaluator(), new YourEvaluator());
    }
}

class YourEvaluator extends Evaluator {
    private HashMap<Position, Double> cachedEvalResults = new HashMap<Position, Double>();
    private final int CACHE_MAX_SIZE = 10000;
    
    @Override
    public double eval(Position position) {
        if (cachedEvalResults.containsKey(position)) {
            return cachedEvalResults.get(position);
        }

        double score = new BoardEvaluator().eval(position.board);
        if (cachedEvalResults.size() > CACHE_MAX_SIZE) {
            cachedEvalResults.clear();
        }

        cachedEvalResults.put(position, score);

        return score;
    }
}