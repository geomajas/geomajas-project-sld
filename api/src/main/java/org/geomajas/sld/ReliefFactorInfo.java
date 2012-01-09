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
 * Schema fragment(s) for this class:...
 * 
 * <pre>
 * &lt;xs:element
 * xmlns:ns="http://www.opengis.net/sld"
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" type="xs:double" name="ReliefFactor"/>
 * </pre>
 * 
 * @author Jan De Moerloose
 * @since 1.0.0
 */

@Api(allMethods = true)
public class ReliefFactorInfo implements Serializable {

	private static final long serialVersionUID = 100;

	private Double reliefFactor;

	/**
	 * Get the 'ReliefFactor' element value.
	 * 
	 * @return value
	 */
	public Double getReliefFactor() {
		return reliefFactor;
	}

	/**
	 * Set the 'ReliefFactor' element value.
	 * 
	 * @param reliefFactor
	 */
	public void setReliefFactor(Double reliefFactor) {
		this.reliefFactor = reliefFactor;
	}

	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "ReliefFactorInfo(reliefFactor=" + this.getReliefFactor() + ")";
	}

	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof ReliefFactorInfo)) {
			return false;
		}
		final ReliefFactorInfo other = (ReliefFactorInfo) o;
		if (!other.canEqual((java.lang.Object) this)) {
			return false;
		}
		if (this.getReliefFactor() == null ? other.getReliefFactor() != null : !this.getReliefFactor().equals(
				(java.lang.Object) other.getReliefFactor())) {
			return false;
		}
		return true;
	}

	@java.lang.SuppressWarnings("all")
	public boolean canEqual(final java.lang.Object other) {
		return other instanceof ReliefFactorInfo;
	}

	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = result * prime + (this.getReliefFactor() == null ? 0 : this.getReliefFactor().hashCode());
		return result;
	}
}