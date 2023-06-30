package com.ssg.views;

import com.ssg.database.models.Officer;
import com.ssg.database.models.Project;

import java.util.Arrays;
import java.util.Objects;

public class MainEvents {
    public static void signIn() {
        ControllerUtils.triggerEvent("signIn");
    }
    public static void signOut() {
        ControllerUtils.triggerEvent("signOut");
    }
    public static void focusOfficer(Officer officer) {
        ControllerUtils.triggerEvent("focusOfficer", officer);
    }
    public static void focusProject(Project project) {
        ControllerUtils.triggerEvent("focusProject", project);
    }
    public static void quickAddProject() {
        ControllerUtils.triggerEvent("quickAddProject");
    }
    public static void quickAddOfficer() {
        ControllerUtils.triggerEvent("quickAddOfficer");
    }
    public static void showDialogMessage(String title, String description, String... buttonText) {
        String[] args = Arrays.stream(buttonText).map(Object::toString).toArray(String[]::new);
        String[] mArgs = Arrays.copyOf(args, 3);
        Arrays.fill(mArgs, args.length, mArgs.length, "");
        if (Objects.equals(mArgs[0], "")) mArgs[0] = "Okay";
        ControllerUtils.triggerEvent("showDialog", "message", title, description, mArgs[0], mArgs[1], mArgs[2]);
    }
    public static void restrictedAccount() {
        showDialogMessage("Restricted Access Alert", "Only admin accounts have access to some feature. Some of the features might not be available for you", "Okay");
    }
}
