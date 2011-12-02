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
 * Provides call-back to refresh the current SLD by e.g. retrieving it again from the server and to load it in the SLD
 * widget. This can be used to undo all client-side changes to the SLD.
 * 
 * @author An Buyle
 * 
 */
public interface RefreshSldHandler {

	void execute(String sldName);
}
