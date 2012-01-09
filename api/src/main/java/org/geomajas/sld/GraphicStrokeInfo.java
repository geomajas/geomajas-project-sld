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
package org.geomajas.sld;

import java.io.Serializable;
import org.geomajas.annotation.Api;

/**
 * 
 A "GraphicStroke" defines a repated-linear graphic pattern to be used for stroking a line.
 * 
 * 
 * Schema fragment(s) for this class:...
 * 
 * <pre>
 * &lt;xs:element
 * xmlns:ns="http://www.opengis.net/sld"
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" name="GraphicStroke">
 *   &lt;xs:complexType>
 *     &lt;xs:sequence>
 *       &lt;xs:element ref="ns:Graphic"/>
 *     &lt;/xs:sequence>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 * 
 * @author Jan De Moerloose
 * @since 1.0.0
 */

@Api(allMethods = true)
public class GraphicStrokeInfo implements Serializable {

	private static final long serialVersionUID = 100;

	private GraphicInfo graphic;

	/**
	 * Get the 'Graphic' element value.
	 * 
	 * @return value
	 */
	public GraphicInfo getGraphic() {
		return graphic;
	}

	/**
	 * Set the 'Graphic' element value.
	 * 
	 * @param graphic
	 */
	public void setGraphic(GraphicInfo graphic) {
		this.graphic = graphic;
	}

	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "GraphicStrokeInfo(graphic=" + this.getGraphic() + ")";
	}

	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof GraphicStrokeInfo)) {
			return false;
		}
		final GraphicStrokeInfo other = (GraphicStrokeInfo) o;
		if (!other.canEqual((java.lang.Object) this)) {
			return false;
		}
		if (this.getGraphic() == null ? other.getGraphic() != null : !this.getGraphic().equals(
				(java.lang.Object) other.getGraphic())) {
			return false;
		}
		return true;
	}

	@java.lang.SuppressWarnings("all")
	public boolean canEqual(final java.lang.Object other) {
		return other instanceof GraphicStrokeInfo;
	}

	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = result * prime + (this.getGraphic() == null ? 0 : this.getGraphic().hashCode());
		return result;
	}
}