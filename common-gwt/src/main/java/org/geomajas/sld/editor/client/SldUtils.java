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

import java.util.ArrayList;
import java.util.List;

import org.geomajas.sld.GraphicInfo;
import org.geomajas.sld.PolygonSymbolizerInfo;
import org.geomajas.sld.RuleInfo;
import org.geomajas.sld.SymbolizerTypeInfo;
import org.geomajas.sld.LineSymbolizerInfo;
import org.geomajas.sld.PointSymbolizerInfo;

import com.google.gwt.core.client.GWT;


/**
 * SLD utilities.
 *
 * @author An Buyle
 *
 */
public final class SldUtils {

	private SldUtils() {
	}

	public static RuleInfo createDefaultRule(GeometryTypes geomType) {
		RuleInfo defaultRule = new RuleInfo();

		defaultRule.setTitle("Standaard stijl");

		List<SymbolizerTypeInfo> symbolizerList = new ArrayList<SymbolizerTypeInfo>();
		SymbolizerTypeInfo symbolizer = null;

		switch (geomType) {
			case POINT:
				symbolizer = new PointSymbolizerInfo();

				((PointSymbolizerInfo) symbolizer).setGraphic(new GraphicInfo());
				List<org.geomajas.sld.GraphicInfo.ChoiceInfo> list = 
					new ArrayList<org.geomajas.sld.GraphicInfo.ChoiceInfo>();

				org.geomajas.sld.GraphicInfo.ChoiceInfo choiceInfoGraphic = 
					new org.geomajas.sld.GraphicInfo.ChoiceInfo();
				list.add(choiceInfoGraphic);
				((PointSymbolizerInfo) symbolizer).getGraphic().setChoiceList(list);

				break;
			case LINE:
				symbolizer = new LineSymbolizerInfo();
				break;
			case POLYGON:
				symbolizer = new PolygonSymbolizerInfo();
				break;

			default:
				GWT.log("createDefaultRule: unsupported geometrie type " + geomType.toString()); // TODO
		}

		symbolizerList.add(symbolizer);

		defaultRule.setSymbolizerList(symbolizerList);
		return defaultRule;
	}

	public static GeometryTypes getGeometryType(RuleInfo rule) {
		GeometryTypes geomType = GeometryTypes.UNSPECIFIED;

		if (null == rule || null == rule.getSymbolizerList() || 0 == rule.getSymbolizerList().size()) {
			GWT.log("getGeometryType: rule must specify a <xxxSymbolizer> element to retrieve the geometry type.");
			return GeometryTypes.UNSPECIFIED;
		}
		Object symbolizerInfo = rule.getSymbolizerList().get(0); // retrieve the first symbolizer specification

		if (symbolizerInfo.getClass().equals(PointSymbolizerInfo.class)) {
			geomType = GeometryTypes.POINT;
		} else if (symbolizerInfo.getClass().equals(LineSymbolizerInfo.class)) {
			geomType = GeometryTypes.LINE;
		} else if (symbolizerInfo.getClass().equals(PolygonSymbolizerInfo.class)) {
			geomType = GeometryTypes.POLYGON;
		}
		return geomType;

	}

}
