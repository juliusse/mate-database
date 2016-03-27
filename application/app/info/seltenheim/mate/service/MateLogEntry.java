package info.seltenheim.mate.service;

public class MateLogEntry {
    public static enum Type {
        drinking, payment, disabled, created
    }

    private final int id;
    private final MateJunky mateJunky;
    private final Type type;
    private final String timestamp;
    private final int credit_old;
    private final int credit_new;
    private final int bottles_old;
    private final int bottles_new;

    public MateLogEntry(int id, MateJunky mateJunky, Type type, String timestamp, int credit_old, int credit_new, int bottles_old, int bottles_new) {
        super();
        this.id = id;
        this.mateJunky = mateJunky;
        this.type = type;
        this.timestamp = timestamp;
        this.credit_old = credit_old;
        this.credit_new = credit_new;
        this.bottles_old = bottles_old;
        this.bottles_new = bottles_new;
    }

    public int getId() {
        return id;
    }

    public MateJunky getMateJunky() {
        return mateJunky;
    }

    public Type getType() {
        return type;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public int getCredit_old() {
        return credit_old;
    }

    public int getCredit_new() {
        return credit_new;
    }

    public int getBottles_old() {
        return bottles_old;
    }

    public int getBottles_new() {
        return bottles_new;
    }

}
