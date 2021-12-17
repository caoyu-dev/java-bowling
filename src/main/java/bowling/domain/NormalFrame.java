package bowling.domain;

import bowling.domain.state.End;
import bowling.domain.state.Progress;
import bowling.domain.state.Start;
import bowling.domain.state.State;

public class NormalFrame extends TemplateFrame {

    private NormalFrame() {
        super();
    }

    private NormalFrame(FrameInfo frameInfo) {
        super(frameInfo, new Start());
    }

    public static Frame first() {
        return new NormalFrame();
    }

    @Override
    public Frame next() {
        if (frameInfo.nextLast()) {
            return FinalFrame.create(frameInfo.next());
        }
        return new NormalFrame(frameInfo.next());
    }

    @Override
    public FrameInfo info() {
        return frameInfo;
    }


    @Override
    public void changeState() {
        if (isStrike()) {
            changeState(new End());
            return;
        }
        checkProgress();
    }

    private void checkProgress() {
        if (state instanceof Progress) {
            changeState(new End());
            return;
        }
        changeState(new Progress(false));
    }

    private void changeState(State state) {
        this.state = state;
    }

    @Override
    public State state() {
        return state;
    }

    @Override
    public boolean isFinal() {
        return false;
    }

    @Override
    public boolean isStrike() {
        return frameInfo.isStrike();
    }

    @Override
    public boolean isSpare() {
        return frameInfo.isSpare();
    }
}
