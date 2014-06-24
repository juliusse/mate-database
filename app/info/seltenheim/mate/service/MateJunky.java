package info.seltenheim.mate.service;

public class MateJunky {
    private String name;
    private int count;
    private int remaining;

    public MateJunky(String name, int count, int remaining) {
        super();
        this.name = name;
        this.count = count;
        this.remaining = remaining;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getRemaining() {
        return remaining;
    }

    public void setRemaining(int remaining) {
        this.remaining = remaining;
    }

}
