package trappedtildoom.gamelogic.item;

public class VolumeItem extends Item {

    private int quantity;

    protected VolumeItem(String name, int tier,int dur) {
        super(name, tier,dur);
    }

    @Override
    public boolean use(Object target) {
        quantity -= 1;
        return quantity > 0;
    }
}
