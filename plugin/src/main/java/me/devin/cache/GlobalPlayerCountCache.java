package me.devin.cache;

public class GlobalPlayerCountCache {
    private static int globalPlayerCount = 0;

    public static synchronized void updateGlobalPlayerCount(int newCount) {
        globalPlayerCount = newCount;
    }

    public static synchronized int getGlobalPlayerCount() {
        return globalPlayerCount;
    }
}