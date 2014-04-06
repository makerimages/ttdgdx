package trappedtildoom.gamelogic.item;

import trappedtildoom.gamelogic.gameobject.Player;

public abstract class FoodItem extends Item {

    protected FoodItem(String name, int tier,int dur) {
        super(name, tier,dur);
    }

    @Override
    public boolean use(Object target) {
        Player player = (Player)target;
        player.consumeFood(getMaxFoodLevel(tier) + 1);
        return true;
    }

    private static int getMaxFoodLevel(int tier) {
        switch (tier) {
            case 1: return 4;
            case 2: return 6;
            case 3: return 10;
        }
        return 0;
    }
}
