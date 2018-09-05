package xyz.sky731.programming.lab3;

public abstract class Place extends Building {
    private Place(int cost) {
        super(cost);
    }

    public static class Coast extends Place {
        private Crystals crystals = null;
        public Coast(int cost, Crystals crystals) {
            super(cost);
            this.crystals = crystals;
        }

        boolean useCrystals(int count) {
            if (crystals.getCount() >= count) {
                for (int i = 0; i < count; i++) {
                    crystals.useCrystal();
                }
                return true;
            } else return false;
        }
    }
}
