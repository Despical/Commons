package me.despical.commons.scoreboard;

/**
 * @author Despical
 * <p>
 * Created at 29.01.2025
 */
public interface AutoUpdatable {

    void disableAutoUpdate();

    long getUpdateInterval();

    void setUpdateInterval(long updateInterval);
}
