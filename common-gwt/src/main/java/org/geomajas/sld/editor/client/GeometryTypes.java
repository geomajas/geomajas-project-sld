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
package org.geomajas.sld.editor.client;

/**
 * GeometryTypes enumeration.
 * 
 * @author An Buyle
 * 
 */
public enum GeometryTypes {
	UNSPECIFIED("unspecified"), POINT("point"), LINE("line"), POLYGON("polygon");

	private final String value;

	GeometryTypes(String v) {
		value = v;
	}

	public String value() {
		return value;
	}

	public static GeometryTypes fromValue(String v) {
		for (GeometryTypes c : GeometryTypes.values()) {
			if (c.value.equals(v)) {
				return c;
			}
		}
		throw new IllegalArgumentException(v.toString());
	}

}
