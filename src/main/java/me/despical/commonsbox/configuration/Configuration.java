package me.despical.commonsbox.configuration;

/**
 * @author Despical
 * <p>
 * Created at 30.05.2020
 */
public interface Configuration<E> {

	E getConfiguration(String file);
}