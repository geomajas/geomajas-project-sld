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
package org.geomajas.sld.xlink;

import java.io.Serializable;
import org.geomajas.annotation.Api;

/**
 * Schema fragment(s) for this class:...
 * 
 * <pre>
 * &lt;xs:attributeGroup
 * xmlns:xlink="http://www.w3.org/1999/xlink"
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" name="resourceLink">
 *   &lt;xs:attribute type="xs:string" fixed="resource" form="qualified" name="type"/>
 *   &lt;xs:attribute use="optional" ref="xlink:role">
 *     &lt;!-- Reference to inner class RoleInfo -->
 *   &lt;/xs:attribute>
 *   &lt;xs:attribute use="optional" ref="xlink:title">
 *     &lt;!-- Reference to inner class TitleInfo -->
 *   &lt;/xs:attribute>
 *   &lt;xs:attribute use="optional" ref="xlink:label">
 *     &lt;!-- Reference to inner class LabelInfo -->
 *   &lt;/xs:attribute>
 * &lt;/xs:attributeGroup>
 * </pre>
 * 
 * @author Jan De Moerloose
 * @since 1.0.0
 */

@Api(allMethods = true)
public class ResourceLinkInfo implements Serializable {

	private static final long serialVersionUID = 100;

	private String type;

	private RoleInfo role;

	private TitleInfo title;

	private LabelInfo label;

	/**
	 * Get the 'type' attribute value.
	 * 
	 * @return value
	 */
	public String getType() {
		return type;
	}

	/**
	 * Set the 'type' attribute value.
	 * 
	 * @param type
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Get the 'role' attribute value.
	 * 
	 * @return value
	 */
	public RoleInfo getRole() {
		return role;
	}

	/**
	 * Set the 'role' attribute value.
	 * 
	 * @param role
	 */
	public void setRole(RoleInfo role) {
		this.role = role;
	}

	/**
	 * Get the 'title' attribute value.
	 * 
	 * @return value
	 */
	public TitleInfo getTitle() {
		return title;
	}

	/**
	 * Set the 'title' attribute value.
	 * 
	 * @param title
	 */
	public void setTitle(TitleInfo title) {
		this.title = title;
	}

	/**
	 * Get the 'label' attribute value.
	 * 
	 * @return value
	 */
	public LabelInfo getLabel() {
		return label;
	}

	/**
	 * Set the 'label' attribute value.
	 * 
	 * @param label
	 */
	public void setLabel(LabelInfo label) {
		this.label = label;
	}

	/**
	 * Schema fragment(s) for this class:...
	 * 
	 * <pre>
	 * &lt;xs:attribute
	 * xmlns:ns="http://www.w3.org/1999/xlink"
	 * xmlns:xs="http://www.w3.org/2001/XMLSchema" use="optional" ref="ns:role"/>
	 * 
	 * &lt;xs:attribute
	 * xmlns:ns="http://www.w3.org/1999/xlink"
	 * xmlns:xs="http://www.w3.org/2001/XMLSchema" type="xs:string" name="role"/>
	 * </pre>
	 */
	public static class RoleInfo implements Serializable {

		private static final long serialVersionUID = 100;

		private String role;

		/**
		 * Get the 'role' attribute value.
		 * 
		 * @return value
		 */
		public String getRole() {
			return role;
		}

		/**
		 * Set the 'role' attribute value.
		 * 
		 * @param role
		 */
		public void setRole(String role) {
			this.role = role;
		}

		@java.lang.Override
		@java.lang.SuppressWarnings("all")
		public java.lang.String toString() {
			return "ResourceLinkInfo.RoleInfo(role=" + this.getRole() + ")";
		}

		@java.lang.Override
		@java.lang.SuppressWarnings("all")
		public boolean equals(final java.lang.Object o) {
			if (o == this) {
				return true;
			}
			if (!(o instanceof RoleInfo)) {
				return false;
			}
			final RoleInfo other = (RoleInfo) o;
			if (!other.canEqual((java.lang.Object) this)) {
				return false;
			}
			if (this.getRole() == null ? other.getRole() != null : !this.getRole().equals(
					(java.lang.Object) other.getRole())) {
				return false;
			}
			return true;
		}

		@java.lang.SuppressWarnings("all")
		public boolean canEqual(final java.lang.Object other) {
			return other instanceof RoleInfo;
		}

		@java.lang.Override
		@java.lang.SuppressWarnings("all")
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = result * prime + (this.getRole() == null ? 0 : this.getRole().hashCode());
			return result;
		}
	}

	/**
	 * Schema fragment(s) for this class:...
	 * 
	 * <pre>
	 * &lt;xs:attribute
	 * xmlns:ns="http://www.w3.org/1999/xlink"
	 * xmlns:xs="http://www.w3.org/2001/XMLSchema" use="optional" ref="ns:title"/>
	 * 
	 * &lt;xs:attribute
	 * xmlns:ns="http://www.w3.org/1999/xlink"
	 * xmlns:xs="http://www.w3.org/2001/XMLSchema" type="xs:string" name="title"/>
	 * </pre>
	 */
	public static class TitleInfo implements Serializable {

		private static final long serialVersionUID = 100;

		private String title;

		/**
		 * Get the 'title' attribute value.
		 * 
		 * @return value
		 */
		public String getTitle() {
			return title;
		}

		/**
		 * Set the 'title' attribute value.
		 * 
		 * @param title
		 */
		public void setTitle(String title) {
			this.title = title;
		}

		@java.lang.Override
		@java.lang.SuppressWarnings("all")
		public java.lang.String toString() {
			return "ResourceLinkInfo.TitleInfo(title=" + this.getTitle() + ")";
		}

		@java.lang.Override
		@java.lang.SuppressWarnings("all")
		public boolean equals(final java.lang.Object o) {
			if (o == this) {
				return true;
			}
			if (!(o instanceof TitleInfo)) {
				return false;
			}
			final TitleInfo other = (TitleInfo) o;
			if (!other.canEqual((java.lang.Object) this)) {
				return false;
			}
			if (this.getTitle() == null ? other.getTitle() != null : !this.getTitle().equals(
					(java.lang.Object) other.getTitle())) {
				return false;
			}
			return true;
		}

		@java.lang.SuppressWarnings("all")
		public boolean canEqual(final java.lang.Object other) {
			return other instanceof TitleInfo;
		}

		@java.lang.Override
		@java.lang.SuppressWarnings("all")
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = result * prime + (this.getTitle() == null ? 0 : this.getTitle().hashCode());
			return result;
		}
	}

	/**
	 * Schema fragment(s) for this class:...
	 * 
	 * <pre>
	 * &lt;xs:attribute
	 * xmlns:ns="http://www.w3.org/1999/xlink"
	 * xmlns:xs="http://www.w3.org/2001/XMLSchema" use="optional" ref="ns:label"/>
	 * 
	 * &lt;xs:attribute
	 * xmlns:ns="http://www.w3.org/1999/xlink"
	 * xmlns:xs="http://www.w3.org/2001/XMLSchema" type="xs:string" name="label"/>
	 * </pre>
	 */
	public static class LabelInfo implements Serializable {

		private static final long serialVersionUID = 100;

		private String label;

		/**
		 * Get the 'label' attribute value.
		 * 
		 * @return value
		 */
		public String getLabel() {
			return label;
		}

		/**
		 * Set the 'label' attribute value.
		 * 
		 * @param label
		 */
		public void setLabel(String label) {
			this.label = label;
		}

		@java.lang.Override
		@java.lang.SuppressWarnings("all")
		public java.lang.String toString() {
			return "ResourceLinkInfo.LabelInfo(label=" + this.getLabel() + ")";
		}

		@java.lang.Override
		@java.lang.SuppressWarnings("all")
		public boolean equals(final java.lang.Object o) {
			if (o == this) {
				return true;
			}
			if (!(o instanceof LabelInfo)) {
				return false;
			}
			final LabelInfo other = (LabelInfo) o;
			if (!other.canEqual((java.lang.Object) this)) {
				return false;
			}
			if (this.getLabel() == null ? other.getLabel() != null : !this.getLabel().equals(
					(java.lang.Object) other.getLabel())) {
				return false;
			}
			return true;
		}

		@java.lang.SuppressWarnings("all")
		public boolean canEqual(final java.lang.Object other) {
			return other instanceof LabelInfo;
		}

		@java.lang.Override
		@java.lang.SuppressWarnings("all")
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = result * prime + (this.getLabel() == null ? 0 : this.getLabel().hashCode());
			return result;
		}
	}

	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "ResourceLinkInfo(type=" + this.getType() + ", role=" + this.getRole() + ", title=" + this.getTitle()
				+ ", label=" + this.getLabel() + ")";
	}

	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof ResourceLinkInfo)) {
			return false;
		}
		final ResourceLinkInfo other = (ResourceLinkInfo) o;
		if (!other.canEqual((java.lang.Object) this)) {
			return false;
		}
		if (this.getType() == null ? other.getType() != null : !this.getType().equals(
				(java.lang.Object) other.getType())) {
			return false;
		}
		if (this.getRole() == null ? other.getRole() != null : !this.getRole().equals(
				(java.lang.Object) other.getRole())) {
			return false;
		}
		if (this.getTitle() == null ? other.getTitle() != null : !this.getTitle().equals(
				(java.lang.Object) other.getTitle())) {
			return false;
		}
		if (this.getLabel() == null ? other.getLabel() != null : !this.getLabel().equals(
				(java.lang.Object) other.getLabel())) {
			return false;
		}
		return true;
	}

	@java.lang.SuppressWarnings("all")
	public boolean canEqual(final java.lang.Object other) {
		return other instanceof ResourceLinkInfo;
	}

	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = result * prime + (this.getType() == null ? 0 : this.getType().hashCode());
		result = result * prime + (this.getRole() == null ? 0 : this.getRole().hashCode());
		result = result * prime + (this.getTitle() == null ? 0 : this.getTitle().hashCode());
		result = result * prime + (this.getLabel() == null ? 0 : this.getLabel().hashCode());
		return result;
	}
}