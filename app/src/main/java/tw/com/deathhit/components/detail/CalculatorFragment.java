package tw.com.deathhit.components.detail;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;
import java.util.regex.Pattern;

import tw.com.deathhit.Constants;
import tw.com.deathhit.R;
import tw.com.deathhit.adapters.SpinnerAdapter;
import tw.com.deathhit.adapters.recycler_view.DataAdapter;
import tw.com.deathhit.comparators.JewelComparator;
import tw.com.deathhit.utils.NoScrollingLinearLayoutManager;

public final class CalculatorFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemSelectedListener, Dialog.OnClickListener{
    private static final String BUNDLE_KEY_POSITION_MAP = "positionMap";
    private static final String BUNDLE_KEY_JEWEL_MAP = "jewelMap";
    private static final String BUNDLE_KEY_GUARD_STONE = "guardStone";
    private static final String BUNDLE_KEY_GUARD_STONE_LEVEL = "guardStoneLevel";

    private static final int ID_INTRODUCTION_BLOCK = R.id.block;
    private static final int ID_ATTRIBUTES_BLOCK = R.id.block2;

    private static final int ID_RECYCLER_VIEW = R.id.recyclerView;

    private static final int ID_HEAD = R.id.tab;
    private static final int ID_BODY = R.id.tab2;
    private static final int ID_ARMS = R.id.tab3;
    private static final int ID_WAIST = R.id.tab4;
    private static final int ID_LEGS = R.id.tab5;
    private static final int ID_GUARD_STONE = R.id.tab6;
    private static final int ID_WEAPON = R.id.tab7;

    private static final int ID_EQUIPMENT_SPINNER = R.id.spinner;
    private static final int[] ID_JEWEL_SPINNERS = {R.id.spinner2, R.id.spinner3, R.id.spinner4};
    private static final int ID_LEVEL_SPINNER = R.id.spinner2;

    private static final int NUMBER_OF_POSITIONS = 5;
    private static final int NUMBER_OF_HOLES_FROM_EQUIPMENT = 3;
    private static final int NUMBER_OF_HOLE_LEVELS = 3;
    private static final int NUMBER_OF_ATTRIBUTES = 5;

    private static final int INDEX_HEAD = 0;
    private static final int INDEX_BODY = 1;
    private static final int INDEX_ARMS = 2;
    private static final int INDEX_WAIST = 3;
    private static final int INDEX_LEGS = 4;
    private static final int INDEX_GUARD_STONE = 5;
    private static final int INDEX_WEAPON = 6;

    private static final int INDEX_FIRE = 0;
    private static final int INDEX_WATER = 1;
    private static final int INDEX_THUNDER = 2;
    private static final int INDEX_ICE = 3;
    private static final int INDEX_DRAGON = 4;

    //For dialog operations
    private static WeakReference<View> dialogView;
    private static int tabId;

    //States to be preserved
    @SuppressLint("UseSparseArrays")
    private HashMap<Integer, String> posPathMap = new HashMap<>(NUMBER_OF_POSITIONS);
    @SuppressLint("UseSparseArrays")
    private HashMap<Integer, String[]> jewelPathMap = new HashMap<>((NUMBER_OF_POSITIONS+1)*NUMBER_OF_HOLES_FROM_EQUIPMENT);    //including weapon slots
    private String guardStonePath;
    private int guardStoneLevel = 1;

    //Variable holders for cross scope calculation
    private String[] tempJewelPaths;
    private int tempLevel;

    @Override
    public View onCreateViewOnce(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle args = getArguments();

        if (args == null)
            return null;

        String path = getArguments().getString(Constants.ARGUMENT_PATH, null);

        if(savedInstanceState == null)
            getDataFromSeries(path);
        else{
            posPathMap = (HashMap<Integer, String>)args.getSerializable(BUNDLE_KEY_POSITION_MAP);
            jewelPathMap = (HashMap<Integer, String[]>)args.getSerializable(BUNDLE_KEY_JEWEL_MAP);
            guardStonePath = args.getString(BUNDLE_KEY_GUARD_STONE);
            guardStoneLevel = args.getInt(BUNDLE_KEY_GUARD_STONE_LEVEL);
        }

        //Configure recycler view
        View view = inflater.inflate(R.layout.fragment_detail_calculator, container, false);

        RecyclerView recyclerView = view.findViewById(ID_RECYCLER_VIEW);

        recyclerView.setLayoutManager(new NoScrollingLinearLayoutManager(getContext()));

        recyclerView.addItemDecoration(new DividerItemDecoration(container.getContext(), DividerItemDecoration.VERTICAL));

        recyclerView.setHasFixedSize(true);

        //Configure tabs
        view.findViewById(ID_HEAD).setOnClickListener(this);
        view.findViewById(ID_BODY).setOnClickListener(this);
        view.findViewById(ID_ARMS).setOnClickListener(this);
        view.findViewById(ID_WAIST).setOnClickListener(this);
        view.findViewById(ID_LEGS).setOnClickListener(this);
        view.findViewById(ID_GUARD_STONE).setOnClickListener(this);
        view.findViewById(ID_WEAPON).setOnClickListener(this);

        //Calculate details
        calculate(view, path);

        return view;
    }

    @Override
    public void onDestroyView() {
        saveStateToArgs();

        super.onDestroyView();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        saveStateToArgs();

        super.onSaveInstanceState(outState);
    }

    /**View.OnClickListener for tabs.**/
    @Override
    public void onClick(final View view) {
        tabId = view.getId();

        StringBuilder text = new StringBuilder(getString(R.string.calculator) + " : ");

        int layoutId = R.layout.dialog_equipment_picker;

        switch (tabId){
            case ID_WEAPON :
                layoutId = R.layout.block_dialog_jewel_picker;
                break;
            case ID_GUARD_STONE :
                layoutId = R.layout.dialog_guard_stone_picker;
                break;
        }

        dialogView = new WeakReference<>(LayoutInflater.from(view.getContext()).inflate(layoutId, null));

        ArrayList<String> items = null;

        ArrayList<String> temp;

        switch (tabId){
            case ID_WEAPON :
                text.append(getString(R.string.weapon));
                break;
            case ID_GUARD_STONE :
                text.append(getString(R.string.guard_stone));

                items = dataHandler.getChildrenPaths("/Equipment/GuardStone");

                Collections.sort(items);

                items.add(0, null);
                break;
            default :
                String position = null;

                switch (tabId){
                    case ID_HEAD :
                        text.append(getString(R.string.position_head));

                        position = "Head";
                        break;
                    case ID_BODY :
                        text.append(getString(R.string.position_body));

                        position = "Body";
                        break;
                    case ID_ARMS :
                        text.append(getString(R.string.position_arms));

                        position = "Hand";
                        break;
                    case ID_WAIST :
                        text.append(getString(R.string.position_waist));

                        position = "Pants";
                        break;
                    case ID_LEGS :
                        text.append(getString(R.string.position_legs));

                        position = "Shoes";
                        break;
                }

                items = new ArrayList<>((int)(dataHandler.getChildrenCount("/Equipment/Armor/初階") + dataHandler.getChildrenCount("/Equipment/Armor/進階")));

                temp = new ArrayList<>((int) dataHandler.getChildrenCount("/Equipment/Armor/進階"));

                ArrayList<String> equipPaths = dataHandler.getChildrenPaths("/Equipment/Armor/初階");

                for(String s : equipPaths){
                    ArrayList<String> posPaths = dataHandler.getChildrenPaths(s + "/Pos");

                    for(String path : posPaths){
                        String value = dataHandler.getValue(path + "/Position");

                        assert value != null;
                        if(value.equals(position))
                            items.add(path);
                    }
                }

                Collections.sort(items);

                equipPaths = dataHandler.getChildrenPaths("/Equipment/Armor/進階");

                for(String s : equipPaths){
                    ArrayList<String> posPaths = dataHandler.getChildrenPaths(s + "/Pos");

                    for(String path : posPaths){
                        String value = dataHandler.getValue(path + "/Position");

                        assert value != null;
                        if(value.equals(position))
                            temp.add(path);
                    }
                }

                Collections.sort(temp);

                items.addAll(temp);

                items.add(0, null);

                break;
        }

        int index = getIndexFromId(tabId);

        if(items != null) {
            Spinner spinner = dialogView.get().findViewById(ID_EQUIPMENT_SPINNER);

            spinner.setAdapter(new SpinnerAdapter(dataHandler, items));

            spinner.setOnItemSelectedListener(this);

            String path;

            if(tabId != ID_GUARD_STONE) {
                path = posPathMap.get(index);
                tempJewelPaths = jewelPathMap.get(index);

            }else {
                path = guardStonePath;
                tempLevel = guardStoneLevel;
            }

            spinner.setSelection(items.indexOf(path));
        }else if(tabId == ID_WEAPON){
            int[] holes = new int[NUMBER_OF_HOLES_FROM_EQUIPMENT];

            for(int i=0;i<holes.length;i++)
                holes[i] = NUMBER_OF_HOLE_LEVELS;

            tempJewelPaths = jewelPathMap.get(index);

            configureJewelSpinnersForDialog(holes);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

        builder.setTitle(text)
                .setView(dialogView.get())
                .setPositiveButton(R.string.enter, this)
                .setNegativeButton(R.string.cancel, this)
                .show();
    }

    /**OnItemSelectedListener for dialog spinners.**/
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int index, long l) {
        String path = (String)adapterView.getItemAtPosition(index);

        switch (getIndexFromId(tabId)) {
            case INDEX_GUARD_STONE :
                switch (adapterView.getId()){
                    case ID_EQUIPMENT_SPINNER :
                        configureGuardStoneSpinnerForDialog(path);  //Get levels of guard stone
                        tempLevel = 1;

                        break;
                }
                break;
            default :   //Equipments and weapon
                int[] holes = getLevelsOfHoles(path);

                configureJewelSpinnersForDialog(holes); //Set up jewel spinners for selection

                tempJewelPaths = null;
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    /**Get result from picker.**/
    @Override
    public void onClick(DialogInterface dialogInterface, int buttonId) {
        int index = -1;

        switch (buttonId){
            case DialogInterface.BUTTON_POSITIVE :
                index = getIndexFromId(tabId);
                break;
            case DialogInterface.BUTTON_NEGATIVE :
                return;
        }

        Spinner spinner = dialogView.get().findViewById(ID_EQUIPMENT_SPINNER);

        switch (index) {
            case INDEX_GUARD_STONE :
                guardStonePath = (String)spinner.getSelectedItem();

                if(guardStonePath != null) {
                    spinner = dialogView.get().findViewById(R.id.spinner2);

                    guardStoneLevel = Integer.valueOf((String) spinner.getSelectedItem());
                }
                break;
            default :
                if(spinner != null)
                    posPathMap.put(index, (String) spinner.getSelectedItem());

                ArrayList<String> items = new ArrayList<>(NUMBER_OF_HOLES_FROM_EQUIPMENT);

                for (int spinnerId : ID_JEWEL_SPINNERS) {
                    spinner = dialogView.get().findViewById(spinnerId);

                    if (spinner.getSelectedItem() != null)
                        items.add((String) spinner.getSelectedItem());
                }

                jewelPathMap.put(index, items.toArray(new String[0]));

                break;
        }

        //Select not equipped
        switch (index){
            case INDEX_WEAPON :
                if(jewelPathMap.get(index).length == 0)
                    jewelPathMap.remove(index);
                break;
            default :
                if(posPathMap.get(index) == null){
                    posPathMap.remove(index);
                    jewelPathMap.remove(index);
                }
                break;
        }

        Bundle args = getArguments();

        assert args != null;
        calculate(getView(), args.getString(Constants.ARGUMENT_PATH));

        tabId = 0;
    }

    private void calculate(View view, String path){
        //get information
        int[] attributes = new int[NUMBER_OF_ATTRIBUTES];
        int[] holes = new int[NUMBER_OF_HOLE_LEVELS];
        int totalDef = 0;

        for(int i=0;i<NUMBER_OF_ATTRIBUTES;i++)
            attributes[i] = 0;

        for(int i=0;i<NUMBER_OF_HOLE_LEVELS;i++)
            holes[i] = 0;

        for (int i : posPathMap.keySet()) {
            String p = posPathMap.get(i);

            for (int j = 0; j < NUMBER_OF_ATTRIBUTES; j++) {
                switch (j) {
                    case INDEX_FIRE:
                        attributes[j] += Integer.valueOf(dataHandler.getValue(p + "/Attributes/火"));
                        break;
                    case INDEX_WATER:
                        attributes[j] += Integer.valueOf(dataHandler.getValue(p + "/Attributes/水"));
                        break;
                    case INDEX_THUNDER:
                        attributes[j] += Integer.valueOf(dataHandler.getValue(p + "/Attributes/雷"));
                        break;
                    case INDEX_ICE:
                        attributes[j] += Integer.valueOf(dataHandler.getValue(p + "/Attributes/冰"));
                        break;
                    case INDEX_DRAGON:
                        attributes[j] += Integer.valueOf(dataHandler.getValue(p + "/Attributes/龍"));
                        break;
                }
            }

            totalDef += Integer.valueOf(dataHandler.getValue(p + "/Defence"));

            //get slots
            int[] temp = getLevelsOfHoles(p);

            for(int j=0;j<NUMBER_OF_HOLE_LEVELS;j++) {
                if(temp[j]>0)
                    holes[temp[j]-1]++;
            }
        }

        //Set up introduction
        View block = view.findViewById(ID_INTRODUCTION_BLOCK);

        ImageView imageView = block.findViewById(R.id.imageView);

        Glide.with(imageView).load(getResources().getDrawable(R.drawable.ic_ticket)).into(imageView);

        TextView textView = block.findViewById(R.id.textView);

        StringBuilder text = new StringBuilder(getResources().getString(R.string.calculator)).append(" : ").append(dataHandler.getKey(path));

        textView.setText(text);

        text = new StringBuilder();

        for(int i=0;i<NUMBER_OF_HOLES_FROM_EQUIPMENT;i++)
            text.append(getString(R.string.slot)).append(i+1).append(" : ").append(holes[i]).append("  ");

        text = text.append("\n").append(getString(R.string.defence)).append(" : ").append(totalDef);

        textView = block.findViewById(R.id.textView2);

        textView.setText(text);

        //Set up attributes
        View block2 = view.findViewById(ID_ATTRIBUTES_BLOCK);

        textView = block2.findViewById(R.id.textView);

        text = new StringBuilder(getString(R.string.fire));

        text.append(" : ").append(attributes[INDEX_FIRE]);

        textView.setText(text);

        textView = block2.findViewById(R.id.textView2);

        text = new StringBuilder(getString(R.string.water));

        text.append(" : ").append(attributes[INDEX_WATER]);

        textView.setText(text);

        textView = block2.findViewById(R.id.textView3);

        text = new StringBuilder(getString(R.string.thunder));

        text.append(" : ").append(attributes[INDEX_THUNDER]);

        textView.setText(text);

        textView = block2.findViewById(R.id.textView4);

        text = new StringBuilder(getString(R.string.ice));

        text.append(" : ").append(attributes[INDEX_ICE]);

        textView.setText(text);

        textView = block2.findViewById(R.id.textView5);

        text = new StringBuilder(getString(R.string.dragon));

        text.append(" : ").append(attributes[INDEX_DRAGON]);

        textView.setText(text);

        //Set recycler view
        HashMap<String, Integer> skillMap = new HashMap<>();

        ArrayList<String> temp = getArrayListFromHashMap(posPathMap);

        int tempSize = temp.size();

        int count = 0;

        while(tempSize > 0 && count < posPathMap.keySet().size()){
            String parentPath = dataHandler.getParentPath(temp.get(0));

            assert parentPath != null;
            String seriesPath = dataHandler.getParentPath(parentPath);

            ArrayList<String> seriesSkillPaths = dataHandler.getChildrenPaths(seriesPath + "/SerieSkill/Skill");

            //Check if there is any series skill
            if(seriesSkillPaths.size() > 0) {
                int[] conditions = new int[seriesSkillPaths.size()];

                int match = 0;

                for(int i=0;i<seriesSkillPaths.size();i++) {
                    String value = dataHandler.getValue(seriesSkillPaths.get(i));

                    assert value != null;
                    value = value.replaceAll("[^\\d]", "");

                    if(value.length() > 0)
                        conditions[i] = Integer.valueOf(value);
                    else
                        conditions[i] = 1;
                }

                //Check if the positions are in the same series
                for (int i = 0;i<tempSize; i++) {
                    parentPath = dataHandler.getParentPath(temp.get(i));

                    assert parentPath != null;
                    String otherSeriesPath = dataHandler.getParentPath(parentPath);

                    String seriesName = dataHandler.getValue(otherSeriesPath + "/SerieSkill/Name");

                    if (seriesName != null && seriesName.equals(dataHandler.getValue(seriesPath + "/SerieSkill/Name"))) {
                        temp.remove(i);
                        i--;
                        tempSize--;
                        match++;
                    }
                }

                //Put skill to map if condition passes
                for(int i=0;i<seriesSkillPaths.size();i++){
                    if(match >= conditions[i])
                        writeSkillMap(skillMap, seriesSkillPaths.get(i));
                }
            }

            count++;
        }

        //Write skill map with positions
        for (int i : posPathMap.keySet())
            writeSkillMap(skillMap, dataHandler.getChildrenPaths(posPathMap.get(i) + "/Skill"));

        //Write skill map with guard stone
        if(guardStonePath != null)
            writeSkillMap(skillMap, dataHandler.getChildrenPaths(guardStonePath + "/Skill"));

        //Write skill map with jewels
        for (int i : jewelPathMap.keySet()) {
            for (String s : jewelPathMap.get(i))
                writeSkillMap(skillMap, dataHandler.getChildrenPaths(s + "/Skill"));
        }

        //Get skill items
        Set<String> skillSet = skillMap.keySet();

        ArrayList<String> items = new ArrayList<>(skillSet.size()+1);

        for(String s : skillSet) {
            String skillPath = "/Skill/" + s;

            int skillLevel = (int) dataHandler.getChildrenCount(skillPath + "/Level");

            //Check if exceeds max level
            if(skillMap.get(s) < skillLevel)
                skillLevel = skillMap.get(s);

            text = new StringBuilder(skillPath);

            String extraText = "_  " + getString(R.string.level) + " : " + skillLevel;

            items.add(text.append(extraText).toString());
        }

        Collections.sort(items);

        items.add(0, getString(R.string.skill));

        //Get position items
        Object[] keys = posPathMap.keySet().toArray();

        Arrays.sort(keys);  //Sort key set

        for(Object key : keys){
            int i = (int)key;

            switch (i){
                case INDEX_HEAD :
                    items.add(getString(R.string.position_head));
                    break;
                case INDEX_BODY :
                    items.add(getString(R.string.position_body));
                    break;
                case INDEX_ARMS :
                    items.add(getString(R.string.position_arms));
                    break;
                case INDEX_WAIST :
                    items.add(getString(R.string.position_waist));
                    break;
                case INDEX_LEGS :
                    items.add(getString(R.string.position_legs));
                    break;
            }

            items.add(posPathMap.get(i));

            //Add jewels which are related to the position
            if(jewelPathMap.get(i) != null)
                items.addAll(Arrays.asList(jewelPathMap.get(i)));
        }

        //Get guard stone item
        if(guardStonePath != null) {
            items.add(getString(R.string.guard_stone));

            items.add(guardStonePath + "_  " + getString(R.string.level) + " : " + guardStoneLevel);
        }

        //Get weapon item
        if(jewelPathMap.get(INDEX_WEAPON) != null) {
            items.add(getString(R.string.weapon));
            items.addAll(Arrays.asList(jewelPathMap.get(INDEX_WEAPON)));
        }

        RecyclerView recyclerView = view.findViewById(ID_RECYCLER_VIEW);

        recyclerView.setAdapter(new DataAdapter(dataHandler, items));
    }

    private void configureGuardStoneSpinnerForDialog(String guardStonePath){
        Spinner spinner = dialogView.get().findViewById(ID_LEVEL_SPINNER);

        ArrayList<String> items;

        int levelIndex;

        if(guardStonePath != null) {
            int maxLevel = (int) dataHandler.getChildrenCount(guardStonePath + "/Lv");

            spinner.setEnabled(true);

            items = new ArrayList<>(maxLevel);

            for (int i = 0; i < maxLevel; i++)
                items.add(String.valueOf(i + 1));

            Collections.sort(items);

            spinner.setAdapter(new SpinnerAdapter(dataHandler, items));

            levelIndex = tempLevel-1;   //index 0 is null

            spinner.setSelection(levelIndex);
        }else {
            spinner.setEnabled(false);

            items = new ArrayList<>(1);

            items.add(null);

            spinner.setAdapter(new SpinnerAdapter(dataHandler, items));
        }
    }

    private void configureJewelSpinnersForDialog(int[] holes){
        String[] jewels = tempJewelPaths;

        for (int i = 0; i < ID_JEWEL_SPINNERS.length; i++) {
            Spinner spinner = dialogView.get().findViewById(ID_JEWEL_SPINNERS[i]);

            if (spinner == null)
                break;

            ArrayList<String> items;

            if (holes != null && holes[i] > 0) {
                ArrayList<String> jewelPaths = dataHandler.getChildrenPaths("/Equipment/Jewelry");

                items = new ArrayList<>(jewelPaths.size());

                for (String s : jewelPaths) {
                    if (Integer.valueOf(dataHandler.getValue(s + "/Hole")) <= holes[i])
                        items.add(s);
                }

                Collections.sort(items, new JewelComparator(dataHandler));

                spinner.setEnabled(true);
            } else {
                items = new ArrayList<>(1);

                spinner.setEnabled(false);
            }

            items.add(0, null);

            spinner.setAdapter(new SpinnerAdapter(dataHandler, items));

            if (jewels != null && jewels.length > i)
                spinner.setSelection(items.indexOf(jewels[i]));
        }
    }

    private <Key, V> ArrayList<V> getArrayListFromHashMap(HashMap<Key, V> hashMap){
        Set<Key> keySet = hashMap.keySet();

        ArrayList<V> result = new ArrayList<>(keySet.size());

        for(Key key : keySet)
            result.add(hashMap.get(key));

        return result;
    }

    private void getDataFromSeries(String path){
        ArrayList<String> temp = dataHandler.getChildrenPaths(path + "/Pos");

        for(String p : temp){
            int i = -1;

            String position = dataHandler.getValue(p + "/Position");

            assert position != null;
            switch (position){
                case "Head" :
                    i = INDEX_HEAD;
                    break;
                case "Body" :
                    i = INDEX_BODY;
                    break;
                case "Hand" :
                    i = INDEX_ARMS;
                    break;
                case "Pants" :
                    i = INDEX_WAIST;
                    break;
                case "Shoes" :
                    i = INDEX_LEGS;
                    break;
            }

            posPathMap.put(i, p);
        }
    }

    private int[] getLevelsOfHoles(String equipmentPath){
        if(equipmentPath == null)
            return null;

        int[] holes = new int[NUMBER_OF_HOLE_LEVELS];

        for(int i=0;i<holes.length;i++)
            holes[i] = 0;

        String slotString = dataHandler.getValue(equipmentPath + "/Slot");

        if(slotString != null) {
            String[] slots = slotString.trim().split("\\s+");

            Pattern pattern = Pattern.compile("[0-9]");

            for (int j = 0; j < NUMBER_OF_HOLE_LEVELS; j++) {
                if (pattern.matcher(slots[j]).find()) //See if string is a number
                    holes[j] = Integer.valueOf(slots[j]);
            }

            return holes;
        }else
            return null;
    }

    private int getIndexFromId(int id){
        switch (id){
            case ID_HEAD :
                return INDEX_HEAD;
            case ID_BODY :
                return INDEX_BODY;
            case ID_ARMS :
                return INDEX_ARMS;
            case ID_WAIST :
                return INDEX_WAIST;
            case ID_LEGS :
                return INDEX_LEGS;
            case ID_WEAPON :
                return INDEX_WEAPON;
            case ID_GUARD_STONE :
                return INDEX_GUARD_STONE;
        }

        return -1;
    }

    private void saveStateToArgs(){
        if(getView() == null)
            return;

        Bundle args = getArguments();

        assert args != null;
        args.putSerializable(BUNDLE_KEY_POSITION_MAP, posPathMap);
        args.putSerializable(BUNDLE_KEY_JEWEL_MAP, jewelPathMap);
        args.putString(BUNDLE_KEY_GUARD_STONE, guardStonePath);
        args.putInt(BUNDLE_KEY_GUARD_STONE_LEVEL, guardStoneLevel);
    }

    private void writeSkillMap(HashMap<String, Integer> skillMap ,String path){
        String skill = dataHandler.getValue(path);

        assert skill != null;
        String key = skill.split("[0-9]+")[0];

        String temp = skill.replaceAll("[^\\d]", "");

        int skillLevel = 1;

        if(temp.length() > 0)
            skillLevel = Integer.valueOf(temp);
        else if(guardStonePath != null && path.contains(guardStonePath))
            skillLevel = guardStoneLevel;

        if (skillMap.containsKey(key))
            skillMap.put(key, skillMap.get(key) + skillLevel);
        else
            skillMap.put(key, skillLevel);

    }

    private void writeSkillMap(HashMap<String, Integer> skillMap, ArrayList<String> pathList){
        for(String skill : pathList)
            writeSkillMap(skillMap, skill);
    }
}
