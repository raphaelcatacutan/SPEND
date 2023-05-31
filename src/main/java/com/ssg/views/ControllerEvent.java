package com.ssg.views;

public class ControllerEvent {

    private String eventId;
    private Object[] simpleArgs;
    private Object[][] arrayArgs;

    public ControllerEvent(String eventId) {
        this.eventId = eventId;
    }

    public ControllerEvent(String eventId, Object[] args) {
        this.eventId = eventId;
        this.simpleArgs = args;
    }
    public ControllerEvent(String eventId, Object[][] args) {
        this.eventId = eventId;
        this.arrayArgs = args;
    }


    public Object[] getSimpleArgs() {
        return simpleArgs;
    }

    public void setSimpleArgs(Object[] simpleArgs) {
        this.simpleArgs = simpleArgs;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public Object[][] getArrayArgs() {
        return arrayArgs;
    }

    public void setArrayArgs(Object[][] arrayArgs) {
        this.arrayArgs = arrayArgs;
    }
}
