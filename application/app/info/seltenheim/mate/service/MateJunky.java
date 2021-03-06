package info.seltenheim.mate.service;

public class MateJunky {
    private Integer id;
    private String name;
    private int count;
    //in cents
    private int credit;

    public MateJunky() {
        this(null, "", 0, 0);
    }

    public MateJunky(String name, int count, int remaining) {
        this(null, name, count, remaining);
    }

    public MateJunky(Integer id, String name, int count, int credit) {
        super();
        this.id = id;
        this.name = name;
        this.count = count;
        this.credit = credit;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public int getCredit() {
        return credit;
    }
    
    public void setCredit(int credit) {
        this.credit = credit;
    }

}
