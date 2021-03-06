/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2015 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the Apache
 * License, Version 2.0. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.sld.geometry;

import java.io.Serializable;
import org.geomajas.annotation.Api;
import org.geomajas.sld.xlink.SimpleLinkInfo;

/**
 * 
 These attributes can be attached to any element, thus allowing it to act as a pointer. The 'remoteSchema' attribute
 * allows an element that carries link attributes to indicate that the element is declared in a remote schema rather
 * than by the schema that constrains the current document instance.
 * 
 * 
 * Schema fragment(s) for this class:...
 * 
 * <pre>
 * &lt;xs:attributeGroup
 * xmlns:gml="http://www.opengis.net/gml"
 * 
 * xmlns:ns="http://www.w3.org/1999/xlink"
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" 
 *  name="AssociationAttributeGroup">
 *   &lt;xs:attributeGroup ref="ns:simpleLink"/>
 *   &lt;xs:attribute use="optional" ref="gml:remoteSchema">
 *     &lt;!-- Reference to inner class RemoteSchemaInfo -->
 *   &lt;/xs:attribute>
 * &lt;/xs:attributeGroup>
 * </pre>
 * 
 * @author Jan De Moerloose
 * @since 1.0.0
 */

@Api(allMethods = true)
public class AssociationAttributeGroupInfo implements Serializable {

	private static final long serialVersionUID = 100;

	private SimpleLinkInfo simpleLink;

	private RemoteSchemaInfo remoteSchema;

	/**
	 * Get the 'simpleLink' attributeGroup value.
	 * 
	 * @return value
	 */
	public SimpleLinkInfo getSimpleLink() {
		return simpleLink;
	}

	/**
	 * Set the 'simpleLink' attributeGroup value.
	 * 
	 * @param simpleLink
	 */
	public void setSimpleLink(SimpleLinkInfo simpleLink) {
		this.simpleLink = simpleLink;
	}

	/**
	 * Get the 'remoteSchema' attribute value.
	 * 
	 * @return value
	 */
	public RemoteSchemaInfo getRemoteSchema() {
		return remoteSchema;
	}

	/**
	 * Set the 'remoteSchema' attribute value.
	 * 
	 * @param remoteSchema
	 */
	public void setRemoteSchema(RemoteSchemaInfo remoteSchema) {
		this.remoteSchema = remoteSchema;
	}

	/**
	 * Schema fragment(s) for this class:...
	 * 
	 * <pre>
	 * &lt;xs:attribute
	 * xmlns:ns="http://www.opengis.net/gml"
	 * 
	 * xmlns:xs="http://www.w3.org/2001/XMLSchema" use="optional" ref="ns:remoteSchema"/>
	 * 
	 * &lt;xs:attribute
	 * xmlns:ns="http://www.opengis.net/gml"
	 * 
	 * xmlns:xs="http://www.w3.org/2001/XMLSchema" type="xs:string" name="remoteSchema"/>
	 * </pre>
	 */
	public static class RemoteSchemaInfo implements Serializable {

		private static final long serialVersionUID = 100;

		private String remoteSchema;

		/**
		 * Get the 'remoteSchema' attribute value.
		 * 
		 * @return value
		 */
		public String getRemoteSchema() {
			return remoteSchema;
		}

		/**
		 * Set the 'remoteSchema' attribute value.
		 * 
		 * @param remoteSchema
		 */
		public void setRemoteSchema(String remoteSchema) {
			this.remoteSchema = remoteSchema;
		}

		@Override
		@java.lang.SuppressWarnings("all")
		public java.lang.String toString() {
			return "AssociationAttributeGroupInfo.RemoteSchemaInfo(remoteSchema=" + this.getRemoteSchema() + ")";
		}

		@Override
		@java.lang.SuppressWarnings("all")
		public boolean equals(final java.lang.Object o) {
			if (o == this) {
				return true;
			}
			if (!(o instanceof RemoteSchemaInfo)) {
				return false;
			}
			final RemoteSchemaInfo other = (RemoteSchemaInfo) o;
			if (!other.canEqual((java.lang.Object) this)) {
				return false;
			}
			if (this.getRemoteSchema() == null ? other.getRemoteSchema() != null : !this.getRemoteSchema().equals(
					(java.lang.Object) other.getRemoteSchema())) {
				return false;
			}
			return true;
		}

		/**
		 * Is there a chance that the object are equal? Verifies that the other object has a comparable type.
		 *
		 * @param other other object
		 * @return true when other is an instance of this type
		 */
		public boolean canEqual(final java.lang.Object other) {
			return other instanceof RemoteSchemaInfo;
		}

		@Override
		@java.lang.SuppressWarnings("all")
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = result * prime + (this.getRemoteSchema() == null ? 0 : this.getRemoteSchema().hashCode());
			return result;
		}
	}

	@Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "AssociationAttributeGroupInfo(simpleLink=" + this.getSimpleLink() + ", remoteSchema="
				+ this.getRemoteSchema() + ")";
	}

	@Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof AssociationAttributeGroupInfo)) {
			return false;
		}
		final AssociationAttributeGroupInfo other = (AssociationAttributeGroupInfo) o;
		if (!other.canEqual((java.lang.Object) this)) {
			return false;
		}
		if (this.getSimpleLink() == null ? other.getSimpleLink() != null : !this.getSimpleLink().equals(
				(java.lang.Object) other.getSimpleLink())) {
			return false;
		}
		if (this.getRemoteSchema() == null ? other.getRemoteSchema() != null : !this.getRemoteSchema().equals(
				(java.lang.Object) other.getRemoteSchema())) {
			return false;
		}
		return true;
	}

	/**
	 * Is there a chance that the object are equal? Verifies that the other object has a comparable type.
	 *
	 * @param other other object
	 * @return true when other is an instance of this type
	 */
	public boolean canEqual(final java.lang.Object other) {
		return other instanceof AssociationAttributeGroupInfo;
	}

	@Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = result * prime + (this.getSimpleLink() == null ? 0 : this.getSimpleLink().hashCode());
		result = result * prime + (this.getRemoteSchema() == null ? 0 : this.getRemoteSchema().hashCode());
		return result;
	}
}