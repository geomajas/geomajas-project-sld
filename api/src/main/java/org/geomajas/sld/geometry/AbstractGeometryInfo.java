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
package org.geomajas.sld.geometry;

import java.io.Serializable;
import org.geomajas.annotation.Api;

/**
 * Schema fragment(s) for this class:...
 * 
 * <pre>
 * &lt;xs:element
 * xmlns:ns="http://www.opengis.net/gml"
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" type="xs:anyType" abstract="true" name="_Geometry"/>
 * </pre>
 * 
 * @author Jan De Moerloose
 * @since 1.10.0
 */

@Api(allMethods = true)
public abstract class AbstractGeometryInfo implements Serializable {

	private static final long serialVersionUID = 1100;

	protected AbstractGeometryInfo() {
	}

	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "AbstractGeometryInfo()";
	}

	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof AbstractGeometryInfo)) {
			return false;
		}
		final AbstractGeometryInfo other = (AbstractGeometryInfo) o;
		if (!other.canEqual((java.lang.Object) this)) {
			return false;
		}
		return true;
	}

	@java.lang.SuppressWarnings("all")
	public boolean canEqual(final java.lang.Object other) {
		return other instanceof AbstractGeometryInfo;
	}

	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		int result = 1;
		return result;
	}
}