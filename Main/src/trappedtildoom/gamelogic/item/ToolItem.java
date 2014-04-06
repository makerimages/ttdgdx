package trappedtildoom.gamelogic.item;

public abstract class ToolItem extends Item {

    private int worth;

    public ToolItem(String name, int tier,int dur) {
        super(name, tier,dur);

        worth = 38 * tier;
    }

    @Override
    public boolean use(Object target) {
        return --worth < 1;
    }
}
