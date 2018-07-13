package tw.com.deathhit.mhw_note;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.ArraySet;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import tw.com.deathhit.mhw_note.core.BaseActivity;


public final class DataHandler{
    private static final String ATTRIBUTE_CHILDREN_KEYS = "ChildrenKeys";

    private static final String OPERATOR_ATTRIBUTE = Constants.OPERATOR_ATTRIBUTES;
    private static final String OPERATOR_PATH = Constants.OPERATOR_PATH;

    private static final String NODE_DATA_VERSION = Constants.NODE_DATA_VERSION;

    private static ArrayList<OnDataRequestedListener> listeners = new ArrayList<>(1);

    private final SharedPreferences data;

    static void requestData(@NonNull Context context, @NonNull String storageName){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(NODE_DATA_VERSION);

        databaseReference.addListenerForSingleValueEvent(new VersionEventListener(context, storageName));
    }

    static void addOnDataRequestedListener(OnDataRequestedListener listener){
        listeners.add(listener);
    }

    static void removeOnDataRequestedListener(OnDataRequestedListener listener){
        listeners.remove(listener);
    }

    private static void writeData(SharedPreferences.Editor editor, DataSnapshot snapshot, String path){
        ArraySet<String> childrenKeys = new ArraySet<>();

        for(DataSnapshot data : snapshot.getChildren()){
            String subPath = path + OPERATOR_PATH + data.getKey();

            childrenKeys.add(data.getKey());

            writeData(editor, data, subPath);
        }

        long childrenCount = snapshot.getChildrenCount();

        if(snapshot.getValue() != null && childrenCount == 0)   //Not null and not array
            editor.putString(path, snapshot.getValue().toString());

        editor.putStringSet(path + OPERATOR_ATTRIBUTE + ATTRIBUTE_CHILDREN_KEYS, childrenKeys);
    }

    private static void triggerListeners(boolean isNewData){
        ArrayList<OnDataRequestedListener> temp = new ArrayList<>(listeners);

        for(OnDataRequestedListener listener : temp)
            listener.onDataRequested(isNewData);
    }

    public DataHandler(@NonNull Context context, @NonNull String storageName) {
        data = context.getSharedPreferences(storageName, Context.MODE_PRIVATE);
    }

    public long getChildrenCount(@NonNull String path){
        return getChildrenKeys(path).size();
    }

    public Set<String> getChildrenKeys(@NonNull String path){
        return data.getStringSet(path + OPERATOR_ATTRIBUTE + ATTRIBUTE_CHILDREN_KEYS, new HashSet<String>());
    }

    public ArrayList<String> getChildrenPaths(@NonNull String path){
        ArrayList<String> result = new ArrayList<>();

        Set<String> childrenKeys = getChildrenKeys(path);

        for(String key : childrenKeys)
            result.add(path + OPERATOR_PATH + key);

        return result;
    }

    public SharedPreferences getData(){
        return data;
    }

    public String getKey(@NonNull String path){
        return path.substring(path.lastIndexOf(OPERATOR_PATH)+1, path.length());
    }

    @Nullable
    public String getParentKey(@NonNull String path){
        String parentPath = getParentPath(path);

        if(parentPath != null)
            return getKey(parentPath);
        else
            return null;
    }

    @Nullable
    public String getParentPath(@NonNull String path){
        int index = path.lastIndexOf(OPERATOR_PATH);

        if(index != -1)
            return path.substring(0 ,path.lastIndexOf(OPERATOR_PATH));
        else
            return null;
    }

    public ArrayList<String> getSiblingsPaths(@NonNull String path){
        ArrayList<String> result = new ArrayList<>();

        Set<String> childrenKeys = getChildrenKeys(path);

        for(String key : childrenKeys) {
            String childPath = path + OPERATOR_PATH + key;

            result.add(childPath);

            ArrayList<String> temp = getSiblingsPaths(childPath);

            result.addAll(temp);
        }

        return result;
    }

    @Nullable
    public String getValue(@NonNull String path){
        return data.getString(path, null);
    }

    private static final class VersionEventListener implements ValueEventListener{
        private final SharedPreferences storage;

        VersionEventListener(@NonNull Context context, @NonNull String storageName){
            storage = context.getSharedPreferences(storageName, Context.MODE_PRIVATE);
        }

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            String currentDataVersion = storage.getString(OPERATOR_PATH + NODE_DATA_VERSION, "-1");

            String newDataVersion = (String)dataSnapshot.getValue();

            if(!currentDataVersion.equals(newDataVersion)){
                FirebaseDatabase.getInstance().getReference().addListenerForSingleValueEvent(new DataEventListener(storage));
            }else
                triggerListeners(false);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            BaseActivity.get().toast(databaseError.getMessage());
        }
    }

    private static final class DataEventListener implements ValueEventListener{
        private final SharedPreferences storage;

        DataEventListener(@NonNull SharedPreferences storage){
            this.storage = storage;
        }

        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            SharedPreferences.Editor editor = storage.edit();

            editor.clear();

            writeData(editor, dataSnapshot, "");

            editor.apply();

            triggerListeners(true);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            BaseActivity.get().toast(databaseError.getMessage());
        }
    }

    public interface OnDataRequestedListener{
        void onDataRequested(boolean isNewData);
    }
}

