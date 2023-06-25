package com.ssg.views;

import com.ssg.database.SpendBRead;
import com.ssg.database.SpendBUtils;
import com.ssg.database.models.SchoolData;
import com.ssg.utils.RuntimeData;
import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.util.Map;

public abstract class ViewController {

    protected AnchorPane anpView;
    protected ObservableList<Object> projects;
    protected ObservableList<Object> expenses;
    protected ObservableList<Object> officers;
    protected ObservableList<Object> contributors;
    protected ObservableList<Object> users;
    protected ObservableList<Object> funds;
    protected SchoolData schoolData;

    protected String[] DBNEEDED = SpendBUtils.SPENDBTABLES;
    public void initialize() {

    }
    public void onNavigate() {

    }

    public void refreshView(boolean loadDB) {

    }

    public void resetAll() {

    }
    public void loadDatabase() {
        Map<String, ObservableList<Object>> tableDataMap = SpendBRead.readTablesData();
        for (String key : DBNEEDED) {
            if (!tableDataMap.containsKey(key)) continue;
            switch (key) {
                case "PROJECTS" -> projects = tableDataMap.get(key);
                case "EXPENSES" -> expenses = tableDataMap.get(key);
                case "OFFICERS" -> officers = tableDataMap.get(key);
                case "CONTRIBUTORS" -> contributors = tableDataMap.get(key);
                case "USERS" -> users = tableDataMap.get(key);
                case "SCHOOLDATA" -> schoolData = (SchoolData) tableDataMap.get(key).get(0);
                case "FUNDS" -> funds = tableDataMap.get(key);
            }
        }
    }
    public void forceRefreshView() {
        SpendBUtils.spendBUpdate(true, DBNEEDED);
        refreshView(true);
    }
    public boolean notAdmin() {
        if (RuntimeData.USER.isAdmin()) return false;
        MainEvents.restrictedAccount();
        return true;
    }
}
