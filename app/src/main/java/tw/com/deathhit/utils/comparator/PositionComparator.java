package tw.com.deathhit.utils.comparator;

import tw.com.deathhit.DataHandler;

public final class PositionComparator extends BaseComparator{
    public PositionComparator(DataHandler dataHandler) {
        super(dataHandler);
    }

    @Override
    public int compare(String s, String t1) {
        int v1 = positionToValue(dataHandler.getValue(s + "/Position"));
        int v2 = positionToValue(dataHandler.getValue(t1 + "/Position"));

        return v1 - v2;
    }

    private int positionToValue(String position){
        switch (position){
            case "Head" :
                return 0;
            case "Body" :
                return 1;
            case "Hand" :
                return 2;
            case "Pants" :
                return 3;
            case "Shoes" :
                return 4;
        }

        return 5;
    }
}
