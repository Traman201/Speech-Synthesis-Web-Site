package com.sergeev.hmm.misc;


import com.sergeev.hmm.model.SpeechModel;

public class LearningThread implements Runnable {
    SpeechModel s;

    public static enum functions {TRANSITION, EMISSION, INITIAL}

    ;
    functions f;
    String[] observation;
    String[] transcription;
    public boolean isDone = false;
    public boolean isOver = false;

    public LearningThread(SpeechModel s, functions f, String[] observation) {
        this.s = s;
        this.f = f;
        this.observation = observation;
        isDone = false;
        isOver = false;
    }

    public LearningThread(SpeechModel s) {
        this.s = s;
        isDone = true;
        isOver = true;
    }

    public void changeParams(functions f, String[] observation, String[] transcription) {
        this.f = f;
        this.observation = observation;
        this.transcription = transcription;
        isDone = false;
    }

    public void toggleTurn() {
        isOver = !isOver;
    }

    @Override
    public void run() {
        while (!isOver) {
            if (!isDone) {
                switch (f) {
                    case TRANSITION:
                        s.estimateTransitionProbability(observation, transcription);
                        break;
                    case INITIAL:
                        break;
                    case EMISSION:
                        break;
                }
                isDone = true;
            }
        }

    }
}
