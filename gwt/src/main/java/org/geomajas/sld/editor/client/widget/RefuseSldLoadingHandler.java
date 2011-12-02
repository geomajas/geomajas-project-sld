/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.sld.editor.client.widget;

/**
 * Provides call-back to signal that the user has aborted the loading of a new SLD.
 *
 * @author An Buyle
 *
 */
public interface RefuseSldLoadingHandler {

	void execute(String refusedSldName, String currentSldName);
}