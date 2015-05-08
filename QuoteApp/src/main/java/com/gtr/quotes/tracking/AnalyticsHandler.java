package com.gtr.quotes.tracking;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class AnalyticsHandler {

    public List<IAnalyticsAgent> agents;

    public AnalyticsHandler() {
        agents = new ArrayList<IAnalyticsAgent>();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                for (IAnalyticsAgent agent : agents)
                    agent.flush();
            }
        };
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(task, 0, 1000 * 60 * 60);
    }

    public void addAgent(IAnalyticsAgent agent) {
        agents.add(agent);
    }

    public void sendEvent(IEvent event) {
        for (IAnalyticsAgent agent : agents) {
            agent.sendEvent(event);
        }
    }

    public void updateUserValue(String name, String value) {
        for (IAnalyticsAgent agent : agents) {
            agent.updateUserValue(name, value);
        }
    }

    public void updateUserValue(String name, int value) {
        for (IAnalyticsAgent agent : agents) {
            agent.updateUserValue(name, value);
        }
    }

    public void dispose() {
        for (IAnalyticsAgent agent : agents)
            agent.dispose();
    }

}
