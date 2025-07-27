/*
 * Commons - Box of the common utilities.
 * Copyright (C) 2025 Despical
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package me.despical.commons.engine;

import java.util.logging.Level;

import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * @author Despical
 * <p>
 * Created at 06.06.2020
 */
public class ScriptEngine {

	private final javax.script.ScriptEngine scriptEngine;

	public ScriptEngine() {
		scriptEngine = new ScriptEngineManager().getEngineByName("js");
	}

	public void setValue(String value, Object valueObject) {
		scriptEngine.put(value, valueObject);
	}

	public void execute(String executable) {
		try {
			scriptEngine.eval(executable);
		} catch (ScriptException e) {
			java.util.logging.Logger logger = org.bukkit.Bukkit.getLogger();
			logger.log(Level.SEVERE, "Script failed to parse expression! Expression was written wrongly!");
			logger.log(Level.SEVERE, "Expression value: " + executable);
			logger.log(Level.SEVERE, "Error log:");
			e.printStackTrace();
			logger.log(Level.SEVERE, "---- DO NOT REPORT THIS TO AUTHOR THIS IS NOT A BUG OR A CRASH ----");
		}
	}
}