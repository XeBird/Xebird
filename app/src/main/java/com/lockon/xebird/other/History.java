package com.lockon.xebird.other;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.Editable;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Set;

public class History {

    private final String historySearch = "history search";
    private LinkedList<Long> history;
    private SharedPreferences sharedPreferences;
    private static History instance;

    private History(LinkedList<Long> history, SharedPreferences sharedPreferences) {
        this.history = history;
        this.sharedPreferences = sharedPreferences;
    }

    public static History initInstance(Context context) {
        if (instance == null) {
            SharedPreferences sharedPreferences = context.getSharedPreferences("history search", Context.MODE_PRIVATE);
            Set<String> res = sharedPreferences.getAll().keySet();
            LinkedList<Long> history = new LinkedList<>();
            for (String i : res) {
                history.add(Long.parseLong(i));
            }
            instance = new History(history, sharedPreferences);
            instance.generate();
        }
        return instance;
    }

    public void put(Editable input) {
        Long currTime = System.currentTimeMillis();
        history.add(currTime);
        LinkedList<String> remove = generate();
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(String.valueOf(currTime), String.valueOf(input));
        for (String s : remove) {
            editor.remove(s);
        }
        editor.apply();
    }

    public String getLatestInput() {
        if (history.size() == 0) {
            return " ";
        } else {
            return sharedPreferences.getString(String.valueOf(history.getLast()), " ");
        }

    }

    private LinkedList<String> generate() {
        assert history != null;
        Collections.sort(history);
        LinkedList<String> res = new LinkedList<>();
        while (history.size() > 10) {
            res.add(String.valueOf(history.removeFirst()));
        }
        return res;
    }

    void commit() {
        instance.commit();
    }

}
