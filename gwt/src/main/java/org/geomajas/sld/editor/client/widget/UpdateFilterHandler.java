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

import org.geomajas.sld.filter.FilterTypeInfo;

/**
 * Provides call-back to inform the receiver that the filter of the current style rule has changed.
 *
 * @author An Buyle
 *
 */
public interface UpdateFilterHandler {

	void execute(FilterTypeInfo filter);
}
