package com.example.simplecounter;

import android.content.Context;
import android.content.SharedPreferences;

public class Storage {

    private Context appContext;

    public Storage(Context appContext){
        this.appContext = appContext;
    }

    //method to save counter value
    protected void saveCounterValue(int value) {
        //Saving the counter value from the shared preferences
        SharedPreferences myPrefs = appContext.getSharedPreferences("counterValue", appContext.MODE_PRIVATE);
        //Make editor object to write to the shared preferences
        SharedPreferences.Editor editor = myPrefs.edit();
        //Saving the counter value under the name of "savedCounterValue"
        editor.putInt("savedCounterValue", value);
        //Commit the action and save the value
        editor.commit();
    }

    //method to get counter saved value
    protected int getCounterValue() {
        //Retrieving the counter value from the shared preferences
        SharedPreferences myPrefs = appContext.getSharedPreferences("counterValue", appContext.MODE_PRIVATE);
        int intCountrValue = myPrefs.getInt("savedCounterValue", 0);
        return intCountrValue;
    }

    //Method to save the last counter value before reset
    protected void savePreResetValue(int value) {
        //Saving the counter value from the shared preferences
        SharedPreferences myPrefs = appContext.getSharedPreferences("preResetValue", appContext.MODE_PRIVATE);
        //Make editor object to write to the shared preferences
        SharedPreferences.Editor editor = myPrefs.edit();
        //Saving the counter value under the name of "preResetValue"
        editor.putInt("preResetValue", value);
        //Commit the action and save the value
        editor.commit();
    }

    //Method to get the last counter value before reset
    protected int getPreResetValue() {
        //Retrieving the counter value from the shared preferences
        SharedPreferences myPrefs = appContext.getSharedPreferences("preResetValue", appContext.MODE_PRIVATE);
        int intCountrValue = myPrefs.getInt("preResetValue", 0);
        return intCountrValue;
    }


    //Method to save the last CheckBox status
    protected void saveCheckBoxValue(String PrefName, boolean value, String name) {
        //Saving the CheckBox value from the shared preferences
        SharedPreferences myPrefs = appContext.getSharedPreferences(name, appContext.MODE_PRIVATE);
        //Make editor object to write to the shared preferences
        SharedPreferences.Editor editor = myPrefs.edit();
        //Saving the CheckBox value under the name of "preResetValue"
        editor.putBoolean(PrefName + name, value);
        //Commit the action and save the value
        editor.commit();
    }

    //Method to get the last CheckBox status before reset
    protected boolean getCheckBoxValue(String PrefName, String name) {
        //Retrieving the CheckBox value from the shared preferences
        SharedPreferences myPrefs = appContext.getSharedPreferences(name, appContext.MODE_PRIVATE);
        boolean CheckBoxstatus = myPrefs.getBoolean(PrefName + name, false);
        return CheckBoxstatus;
    }
}
