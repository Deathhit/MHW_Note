package tw.com.deathhit.mhw_note.utils.comparator;

import java.util.Comparator;

import tw.com.deathhit.mhw_note.DataHandler;

abstract class BaseComparator implements Comparator<String>{
    protected DataHandler dataHandler;

    BaseComparator(DataHandler dataHandler){
        this.dataHandler = dataHandler;
    }
}
