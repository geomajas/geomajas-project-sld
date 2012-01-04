/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.sld.editor.client.widget;

/**
 * Provides call-back to be called when the current style rule needs to be unloaded and
 * optionally a new rule to be loaded (e.g. in rule detail window).
 *
 * @author An Buyle
 *
 */
public interface SelectRuleHandler {
	
	void  execute(boolean ruleSelected, Object ruleInfo);
}
