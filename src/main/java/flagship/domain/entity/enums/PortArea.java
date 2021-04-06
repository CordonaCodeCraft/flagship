package flagship.domain.entity.enums;

public enum PortArea {

    FIRST(1),
    SECOND(2),
    THIRD(3),
    FOURTH(4);

    public int label;

    private PortArea(int label) {
        this.label = label;
    }

}
