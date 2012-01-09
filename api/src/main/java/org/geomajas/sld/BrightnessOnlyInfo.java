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
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" type="xs:boolean" name="BrightnessOnly"/>
 * </pre>
 * 
 * @author Jan De Moerloose
 * @since 1.0.0
 */

@Api(allMethods = true)
public class BrightnessOnlyInfo implements Serializable {

	private static final long serialVersionUID = 100;

	private boolean brightnessOnly;

	/**
	 * Get the 'BrightnessOnly' element value.
	 * 
	 * @return value
	 */
	public boolean isBrightnessOnly() {
		return brightnessOnly;
	}

	/**
	 * Set the 'BrightnessOnly' element value.
	 * 
	 * @param brightnessOnly
	 */
	public void setBrightnessOnly(boolean brightnessOnly) {
		this.brightnessOnly = brightnessOnly;
	}

	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "BrightnessOnlyInfo(brightnessOnly=" + this.isBrightnessOnly() + ")";
	}

	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof BrightnessOnlyInfo)) {
			return false;
		}
		final BrightnessOnlyInfo other = (BrightnessOnlyInfo) o;
		if (!other.canEqual((java.lang.Object) this)) {
			return false;
		}
		if (this.isBrightnessOnly() != other.isBrightnessOnly()) {
			return false;
		}
		return true;
	}

	@java.lang.SuppressWarnings("all")
	public boolean canEqual(final java.lang.Object other) {
		return other instanceof BrightnessOnlyInfo;
	}

	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = result * prime + (this.isBrightnessOnly() ? 1231 : 1237);
		return result;
	}
}