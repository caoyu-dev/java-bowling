package bowling.domain.state;

import bowling.domain.Score;

public class Strike implements State {
    private final Score score;

    public Strike() {
        this.score = Score.ofStrike();
    }

    @Override
    public State bowl(int countOfPins) {
        return null;
    }

    @Override
    public Score getScore() {
        return this.score;
    }

}
