package tw.com.deathhit.comparators;

import java.util.Comparator;

import tw.com.deathhit.DataHandler;

abstract class BaseComparator implements Comparator<String>{
    protected DataHandler dataHandler;

    BaseComparator(DataHandler dataHandler){
        this.dataHandler = dataHandler;
    }
}
