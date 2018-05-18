package tw.com.deathhit.comparators;

import tw.com.deathhit.DataHandler;

public final class JewelComparator extends BaseComparator{
    public JewelComparator(DataHandler dataHandler) {
        super(dataHandler);
    }

    @Override
    public int compare(String s, String t1) {
        int h1 = Integer.valueOf(dataHandler.getValue(s + "/Hole"));
        int h2 = Integer.valueOf(dataHandler.getValue(t1 + "/Hole"));

        if(h1 != h2)
            return h2-h1;
        else
            return s.compareTo(t1);
    }
}
