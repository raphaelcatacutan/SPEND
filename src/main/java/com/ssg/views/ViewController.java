package com.ssg.views;

import com.ssg.database.SpendBRead;
import com.ssg.database.SpendBUtils;
import javafx.collections.ObservableList;

import java.util.Map;

public abstract class ViewController {

    protected ObservableList<Object> projects;
    protected ObservableList<Object> expenses;
    protected ObservableList<Object> officers;
    protected ObservableList<Object> contributors;
    protected ObservableList<Object> settings;
    protected ObservableList<Object> users;

    protected String[] DBNEEDED = SpendBUtils.SPENDBTABLES;
    public abstract void initialize();
    public abstract void onNavigate();
    public abstract void refreshView(); // TODO: Add F5 to each viewer
    public void loadDatabase() {
        Map<String, ObservableList<Object>> tableDataMap = SpendBRead.readTablesData();
        for (String key : DBNEEDED) {
            if (!tableDataMap.containsKey(key)) continue;
            switch (key) {
                case "PROJECTS" -> projects = tableDataMap.get(key);
                case "EXPENSES" -> expenses = tableDataMap.get(key);
                case "OFFICERS" -> officers = tableDataMap.get(key);
                case "CONTRIBUTORS" -> contributors = tableDataMap.get(key);
                case "SETTINGS" -> settings = tableDataMap.get(key);
                case "USERS" -> users = tableDataMap.get(key);
            }
        }
    }
    public void forceRefreshView() {
        SpendBUtils.spendBUpdate(true, DBNEEDED);
        refreshView();
    }
}
