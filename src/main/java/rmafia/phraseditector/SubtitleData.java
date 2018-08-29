package rmafia.phraseditector;

public class SubtitleData {
    private String text;
    private float start;
    private float dur;

    public SubtitleData(String text, float start, float dur) {
        this.text = text;
        this.start = start;
        this.dur = dur;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public float getStart() {
        return start;
    }

    public void setStart(float start) {
        this.start = start;
    }

    public float getDur() {
        return dur;
    }

    public void setDur(float dur) {
        this.dur = dur;
    }
}
