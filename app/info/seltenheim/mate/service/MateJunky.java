package info.seltenheim.mate.service;

public class MateJunky {
    private String name;
    private int count;
    private double credit;

    public MateJunky(String name, int count, double credit) {
        super();
        this.name = name;
        this.count = count;
        this.credit = credit;
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

    public double getCredit() {
        return credit;
    }
    
    public String getCreditAsString() {
    	return String.format("%.2f", getCredit());
    }

    public void setCredit(double credit) {
        this.credit = credit;
    }

}
