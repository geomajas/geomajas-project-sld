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
 * xmlns:gml="http://www.opengis.net/gml" 
 * 
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" 
 * type="gml:GeometryCollectionType" substitutionGroup="gml:_Geometry" name="MultiGeometry"/>
 * </pre>
 * 
 * @author Jan De Moerloose
 * @since 1.0.0
 */

@Api(allMethods = true)
public class MultiGeometryInfo extends AbstractGeometryCollectionInfo implements Serializable {

	private static final long serialVersionUID = 1100;

	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "MultiGeometryInfo()";
	}

	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof MultiGeometryInfo)) {
			return false;
		}
		final MultiGeometryInfo other = (MultiGeometryInfo) o;
		if (!other.canEqual((java.lang.Object) this)) {
			return false;
		}
		if (!super.equals(o)) {
			return false;
		}
		return true;
	}

	@java.lang.SuppressWarnings("all")
	public boolean canEqual(final java.lang.Object other) {
		return other instanceof MultiGeometryInfo;
	}

	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = result * prime + super.hashCode();
		return result;
	}
}