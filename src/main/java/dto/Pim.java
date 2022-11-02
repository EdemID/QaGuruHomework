package dto;

public class Pim {
    private String name;
    private Integer count;
    private Boolean flag;
    private Dim dim;

    public Dim getDim() {
        return dim;
    }

    public void setDim(Dim dim) {
        this.dim = dim;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }
}
