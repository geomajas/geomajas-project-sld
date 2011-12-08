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

import java.util.List;

import org.geomajas.sld.StyledLayerDescriptorInfo;
import org.geomajas.sld.client.SldGwtService;
import org.geomajas.sld.client.SldGwtServiceAsync;
import org.geomajas.sld.editor.client.widget.CloseSldHandler;
import org.geomajas.sld.editor.client.widget.RefreshSldHandler;
import org.geomajas.sld.editor.client.widget.RefuseSldLoadingHandler;
import org.geomajas.sld.editor.client.widget.SldManager;
import org.geomajas.sld.editor.client.widget.SldWidget;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.RootPanel;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * Entry point of SmartGWT SLD editor.
 * 
 * @author An Buyle
 * @author Jan De Moerloose
 * 
 */
public class SldEditorEntryPoint implements EntryPoint {

	private HLayout mainLayout;

	private VLayout vLayoutLeft;

	private SldWidget sldWidget;

	private Canvas canvasForSLD;

	private SldGwtServiceAsync service;

	private SldManager sldManager;

	private VLayout vLayoutRight;

	private String selectedSLDName;

	public void onModuleLoad() {
		// Add it to the root panel.
		RootPanel.get().add(getViewPanel());
	}

	public Canvas getViewPanel() {

		Cookies.setCookie("skin_name", "Enterprise");

		mainLayout = new HLayout();
		
		mainLayout.setWidth("95%"); // when setting to 100% the panels resize annoyingly ('dancing pannels')
									// when the mouse pointer is moved   
		mainLayout.setHeight("95%");
		//mainLayout.setMaxHeight(1000);
		// mainLayout.setMinWidth(300);
		// mainLayout.setMinHeight(400);

		vLayoutLeft = new VLayout();
		vLayoutLeft.setWidth("30%");
		
		//vLayoutLeft.setMembersMargin(5);
		vLayoutLeft.setMargin(5);
		// Possibility for the user to make vLayoutLeft more/less wide and consequently vlayoutRight
		// less/more wide
		vLayoutLeft.setShowResizeBar(true); 
		vLayoutLeft.setMinWidth(100); /* min 100px wide */
		vLayoutLeft.setOverflow(Overflow.AUTO);


		mainLayout.addMember(vLayoutLeft);
		vLayoutLeft.setHeight100();
		
		service = GWT.create(SldGwtService.class);
		ServiceDefTarget endpoint = (ServiceDefTarget) service;
		endpoint.setServiceEntryPoint(GWT.getHostPageBaseURL() + "d/sld");

		/** Setup SLD manager widget (left panel) **/ 
		sldManager = new SldManager(service);
		vLayoutLeft.addMember(sldManager.getCanvas());
		sldManager.getCanvas().setWidth100();
		sldManager.getCanvas().setHeight100();
		
		/** Add ChangedHandler for SLD Manager widget **/
		sldManager.addSelectionChangedHandler(new SelectionChangedHandler() {

			public void onSelectionChanged(SelectionEvent event) {
				GWT.log("SldgEditorEntryPoint: onSelectionChanged of sldManager for SLD "
						+ (null == event.getSelectedRecord()
							? "null" : (String) event.getSelectedRecord().getAttribute(
									SldManager.SLD_NAME_ATTRIBUTE_NAME)));
				
				if (sldManager.getUserFlagDuringSelect()) {
					GWT.log("SldgEditorEntryPoint: onSelectionChanged :  getUserFlagDuringSelect() == true,~"
							+ " so do nothing");
					return; /* ABORT */
				}
				ListGridRecord record = event.getSelectedRecord();

				if (record == null) {
					sldWidget.clear(true);
					canvasForSLD.disable();
					//vLayoutRight.markForRedraw();
				} else {

					selectedSLDName = (String) record.getAttribute(SldManager.SLD_NAME_ATTRIBUTE_NAME);

					service.findByName(selectedSLDName, new AsyncCallback<StyledLayerDescriptorInfo>() {

						/** call-back for handling service.findByName() success return **/
						public void onSuccess(StyledLayerDescriptorInfo sld) {
							if (!sld.getChoiceList().isEmpty()) {
								sldWidget.getCanvasForSLD(sld);
								canvasForSLD.enable();
							}
						}
						public void onFailure(Throwable caught) {
							GWT.log("could not access SLD", caught);
							SC.warn("De SLD " + selectedSLDName
									+ " kan niet gevonden worden. Interne fout: "
									+ caught.getMessage());
						}
					}); /* call service */
				}
			}

		});

		/** Setup the right panel (which will show the currently loaded SLD in detail) **/
		
		sldWidget = new SldWidget(service);
		
//		sldWidget.addOpenSldHandler(new OpenSldHandler() {
//			
//			public void execute(String sldName) {
//				sldManager.selectSld(sldName, true/* do not load the SLD again in onSelectionChanged */);
//			}
//		});
		
		sldWidget.setCloseFunctionality(true, new CloseSldHandler() {
			
			public void execute(String sldName, boolean closeTriggeredByCloseButton) {
				GWT.log("SldgEditorEntryPoint: execute CloseSldHandler of sldWidget for SLD "
						+ (null == sldName ? "null" : sldName)
						+ "; closeTriggeredByCloseButton=" + closeTriggeredByCloseButton);
				
				if (closeTriggeredByCloseButton) {
					sldManager.selectSld(null, true/* do not load the "null" SLD */);
				}
			}
		});

		sldWidget.addRefuseSldLoadingHandler(new RefuseSldLoadingHandler() {

			public void execute(String refusedSldName, String currentSldName) {

				GWT.log(getClass().getName() + ": execute RefuseSldLoadingHandler of sldWidget for SLD "
						+ (null == refusedSldName ? "null" : refusedSldName));

				if (null != currentSldName) {
					sldManager.selectSld(currentSldName, true);
									// 2nd arg = true: no need to load SLD currentSldName, 
									// since already loaded by sldWidget
				} else {
					// Do nothing
				}

			}

		});
		
		sldWidget.addRefreshHandler(new RefreshSldHandler() {

			public void execute(String sldName) {
				GWT.log(getClass().getName() + ": execute addRefreshHandler of sldWidget for SLD "
						+ (null == sldName ? "null" : sldName));

				service.findByName(sldName,
						new AsyncCallback<StyledLayerDescriptorInfo>() {

							public void onSuccess(StyledLayerDescriptorInfo sld) {
								sldWidget.getCanvasForSLD(sld);
							}

							public void onFailure(Throwable caught) {
								SC.warn("De SLD " + selectedSLDName
										+ " kan niet gevonden worden. Interne fout: "
										+ caught.getMessage());
								GWT.log("could not access SLD " + selectedSLDName, caught);
							}
						});
			}
		});


		vLayoutRight = new VLayout();
		vLayoutRight.setWidth("70%");
		vLayoutRight.setHeight100();
		//vLayoutRight.setMembersMargin(5);
		vLayoutRight.setMargin(5);
		//No ResizeBar needed here
		vLayoutRight.setMinWidth(150);

		canvasForSLD = sldWidget.getCanvas();
		if (null == canvasForSLD) {
			SC.warn("The SLD editor widget could not be created successfully");
			GWT.log("The SLD editor widget could not be created successfully");
			return mainLayout;  /** ABORT !!! **/
		}
		canvasForSLD.disable();

		vLayoutRight.addMember(canvasForSLD);
		canvasForSLD.setHeight("100%");		

		mainLayout.addMember(vLayoutRight);

		/** Populate select list of SLD Manager widget **/
		service.findAll(new AsyncCallback<List<String>>() {

			public void onSuccess(List<String> result) {

				GWT.log("got " + result.size() + " SLDs");

				sldManager.setData(result);
			}

			public void onFailure(Throwable caught) {
				GWT.log("could not access SLDs", caught);
			}
		});


		return mainLayout;
	}

}
