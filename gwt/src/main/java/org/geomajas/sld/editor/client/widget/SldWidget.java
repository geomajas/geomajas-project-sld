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

package org.geomajas.sld.editor.client.widget;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.geomajas.gwt.client.util.WidgetLayout;
import org.geomajas.sld.CssParameterInfo;
import org.geomajas.sld.ExternalGraphicInfo;
import org.geomajas.sld.FeatureTypeStyleInfo;
import org.geomajas.sld.FillInfo;
import org.geomajas.sld.FormatInfo;
import org.geomajas.sld.GeometryInfo;
import org.geomajas.sld.GraphicInfo;
import org.geomajas.sld.LineSymbolizerInfo;
import org.geomajas.sld.MarkInfo;
import org.geomajas.sld.NamedLayerInfo;
import org.geomajas.sld.NamedLayerInfo.ChoiceInfo;
import org.geomajas.sld.NamedStyleInfo;
import org.geomajas.sld.OnlineResourceInfo;
import org.geomajas.sld.PointSymbolizerInfo;
import org.geomajas.sld.PolygonSymbolizerInfo;
import org.geomajas.sld.RotationInfo;
import org.geomajas.sld.RuleInfo;
import org.geomajas.sld.SizeInfo;
import org.geomajas.sld.SldConstants;
import org.geomajas.sld.StrokeInfo;
import org.geomajas.sld.StyledLayerDescriptorInfo;
import org.geomajas.sld.WellKnownNameInfo;
import org.geomajas.sld.client.SldGwtServiceAsync;
import org.geomajas.sld.filter.FilterTypeInfo;
import org.geomajas.sld.xlink.SimpleLinkInfo.HrefInfo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Side;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.ColorPickerItem;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.SpinnerItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.form.validator.IntegerRangeValidator;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;

/**
 * Provides functionality, including GUI, to view or edit an SLD document.
 *
 *
 * The current version will support a limited set of features.
 * <p>Not supported are, amongst others:
 * <br/>- no style symbol preview
 * <br/>- no XML view of SLD
 * <br/>- no XML editing of SLD (advanced editing)
 * <br/>- no filling of polygon with pattern
 * <br/>- no support for external graphic for point symbolizer
 * 
 * @author An Buyle
 *
 */
public class SldWidget {

	private static final String SELECT_TYPE_OF_GRAPHIC_EXTERNE_FIGUUR = "Externe figuur";
	private static final String SELECT_TYPE_OF_GRAPHIC_MARKER = "Marker";
	
	private static final int WAITING_FOR_OPTIONAL_SAVE_SEC = 4;
	
	private SldGwtServiceAsync service;

	private StyledLayerDescriptorInfo currentSld;

	private VLayout widgetLayout;

	private VLayout ruleDetailContainer;

	private RuleSelector ruleSelector;

	private DynamicForm generalForm;

	private DynamicForm specificFormPoint;

	private HLayout savePanel;

	private IButton saveButton;

	private CancelButton cancelButton;

	// generalForm items
	private TextItem nameOfLayerItem;

	private TextItem styleTitleItem;

	private TextItem geomTypeItem;

	// SpecificFormPoint form items
	private SelectItem typeOfGraphicItem;

	private GraphicInfo currrentGraphicInfo;

	/** private members for point symbolizer - type=marker **/
	private DynamicForm markerSymbolizerForm;

	private boolean isMarkerSymbolizerFormFirstSetup = true;

	private MarkInfo currentMark;

	// markerSymbolizerForm form items
	private SelectItem markerSymbolName;

	private SpinnerItem markerSizeItem;

	private SpinnerItem markerRotationItem;

	private CheckboxItem markerFillCheckBoxItem;

	private ColorPickerItem markerFillColorPicker;

	private SpinnerItem markerFillOpacityItem;

	private CheckboxItem markerBorderCheckBoxItem;

	private ColorPickerItem markerStrokeColorPicker;

	private SpinnerItem markerStrokeWidthItem;

	private SpinnerItem markerStrokeOpacityItem;

	/** private members for point symbolizer - type=external graphic **/
	private DynamicForm externalGraphicForm;

	private boolean isExternalGraphicFormFirstSetup = true;

	// externalGraphicForm form items
	private SpinnerItem externalGraphicSizeItem;

	private SelectItem graphicFormatItem;

	private TextItem urlItem;

	private VLayout symbolPane;

	/** private members for line symbolizer **/
	private DynamicForm lineSymbolizerForm;

	private boolean isLineSymbolizerFormFirstSetup = true;

	// lineSymbolizerForm form items
	private ColorPickerItem lineStrokeColorPicker;

	private SpinnerItem strokeWidthItem;

	private LineSymbolizerInfo currentLineSymbolizerInfo;

	private SpinnerItem strokeOpacityItem;

	protected FillInfo prevFillInfo;

	/** private members for polygon symbolizer **/
	private DynamicForm polygonSymbolizerForm;

	private boolean isPolygonSymbolizerFormFirstSetup = true;

	private PolygonSymbolizerInfo currentPolygonSymbolizerInfo;

	private CheckboxItem polygonFillCheckBoxItem;

	private ColorPickerItem polygonFillColorPicker;

	private SpinnerItem polygonFillOpacityItem;

	private CheckboxItem polygonBorderCheckBoxItem;

	private ColorPickerItem polygonStrokeColorPicker;

	private SpinnerItem polygonStrokeWidthItem;

	private SpinnerItem polygonStrokeOpacityItem;

	private RuleInfo currentRule;

	// RuleTreeNode currentLeaf;
	private Canvas noRuleSelectedMessage;

	private VLayout filterPane;

	private Canvas filterForm;

	private FilterEditor filterEditor;

	private boolean isSupportedFilter;

	private RefreshSldHandler refreshSldHandler;

	private boolean sldHasChanged;
	
	private boolean answerForConfirmSaveReceived = true; 
	private StyledLayerDescriptorInfo sldLatestRequest;

	
	private RefuseSldLoadingHandler refuseSldLoadingHandler;
	

	public SldWidget() {
		init(null);
	}

	public SldWidget(SldGwtServiceAsync service) {
		init(service);
	}

	public void addRefuseSldLoadingHandler(RefuseSldLoadingHandler refuseSldLoadingHandler) {
		this.refuseSldLoadingHandler = refuseSldLoadingHandler;

	}

	public Canvas getCanvasForSLD(StyledLayerDescriptorInfo sld) {
		GWT.log("Entering getCanvasForSLD() for sld = "
				+ (null != sld ? sld.getName() : "null"));
		sldLatestRequest = sld;
		
		if (!answerForConfirmSaveReceived) {
			
				GWT.log("getCanvasForSLD(): waiting for save of previous one. Aborting immediate loading of "
						+ (null != sld ? sld.getName() : "null"));
				
				return null;  // TODO: call-back to inform caller that switch to another or no SLD loaded failed 
		}
		
		
		final StyledLayerDescriptorInfo sldNew = sld;

		if (!saveButton.getDisabled() && null != this.currentSld) {
			answerForConfirmSaveReceived = false;
			
			GWT.log("getCanvasForSLD(): giving the user the option to save the SLD before unloading it");
					
			
			SC.confirm("Wenst u uw wijzigingen aan de SLD '" + this.currentSld.getName()
					+ "' eerst te bewaren alvorens de nieuwe SLD in te laden? Zo ja, druk dan op 'OK'.",
					new BooleanCallback() {

						public void execute(Boolean value) {
							if (value != null && value) {
								/* First save the current SLD, then load the new SLD */
								saveSld();
							}
							answerForConfirmSaveReceived = true;
							final Timer timer = new Timer() {
								@Override
								public void run() {
									GWT.log("getCanvasForSLD(): calling MYSELF after optional save and sleeping");
									getCanvasForSLD(sldLatestRequest);
								}
							};
							// sleep 200 msec before calling getCanvasForSLD() again
							timer.schedule(200);
						} /* execute */
				});
			/* confirm returns immediately */

			return widgetLayout;
		} else if (!cancelButton.getDisabled() && null != this.currentSld) {
			
			answerForConfirmSaveReceived = false;

			SC.confirm(
					"U hebt de huidige SLD '"
							+ currentSld.getName()
							+ "'  gewijzigd, maar nog niet bewaard.<break/>"
							+ "Wenst u eerst die SLD af te werken, druk dan 'OK'."
							+ "  Indien u op 'Annuleer' drukt, zijn uw wijzigingen verloren.",
					new BooleanCallback() {

						public void execute(Boolean value) {
							answerForConfirmSaveReceived = true;
							if (value != null && value) {
								if (null != refuseSldLoadingHandler) {
									refuseSldLoadingHandler.execute(sldNew.getName(), currentSld.getName());
								}
								return; /* ABORT loading the new SLD */

							} else {
								enableSave(false);
								enableCancel(false);
								getCanvasForSLD(sldNew);
							}

						} /* execute */
					});

			return widgetLayout;
		}

		this.currentSld = sld;
		
		GWT.log("getCanvasForSLD(): executing clear(false)");
		
		clear(false);

		if (null == sld) { /* We only needed to optionally save the already loaded SLD */ 
			return widgetLayout;
		}

		// widgetLayout.enable();

		if (null == sld.getChoiceList() || sld.getChoiceList().isEmpty()) {
			SC.warn("Empty SLD's are not supported.");
			return widgetLayout; // ABORT
		}

		// make sure only 1 layer per SLD
		if (sld.getChoiceList().size() > 1) {
			SC.warn("Having more than 1 layer in an SLD is not supported.");
			return widgetLayout; // ABORT
		}
		/* retrieve the first choice */
		StyledLayerDescriptorInfo.ChoiceInfo info = sld.getChoiceList().iterator().next();
		if (!info.ifNamedLayer()) {
			// warning that invalid SLD
			SC.warn("Only SLD's with a &lt;NamedLayer&gt; element are supported.");
			return widgetLayout; // ABORT
		}

		NamedLayerInfo namedLayerInfo = info.getNamedLayer();

		String nameValue = (null != namedLayerInfo.getName()) ? namedLayerInfo.getName() : "{Niet gespecifieerd}";
		nameOfLayerItem.setValue(nameValue);

		// <LayerFeatureConstraints> is not supported
		if (namedLayerInfo.getLayerFeatureConstraints() != null) {
			SC.warn("The present <LayerFeatureConstraints> info in the SLD cannot be viewed/updated.");
		}

		List<ChoiceInfo> choiceList = namedLayerInfo.getChoiceList();
		if (null == choiceList) {
			SC.warn("Ongeldige SLD: een leeg &lt;NamedLayer&gt; element wordt niet ondersteund.");
			return widgetLayout; // ABORT
		}

		ChoiceInfo choiceInfo = choiceList.iterator().next(); // retrieve the first constraint

		if (choiceInfo.ifNamedStyle()) {
			// Only the name is specialized
			styleTitleItem.setValue(choiceInfo.getNamedStyle());
			SC.warn("De SLD verwijst naar een externe stijl met naam '" + choiceInfo.getNamedStyle()
					+ "'.  Deze kan hier niet getoond worden.");
		} else if (choiceInfo.ifUserStyle()) {
			styleTitleItem.setValue(choiceInfo.getUserStyle().getTitle());

			if (null == choiceInfo.getUserStyle().getFeatureTypeStyleList()
					|| choiceInfo.getUserStyle().getFeatureTypeStyleList().size() == 0) {

				List<FeatureTypeStyleInfo> featureTypeStyleList = new ArrayList<FeatureTypeStyleInfo>();
				featureTypeStyleList.add(new FeatureTypeStyleInfo());
				choiceInfo.getUserStyle().setFeatureTypeStyleList(featureTypeStyleList);

				enableSave(true);
			}

			ruleSelector.setRules(choiceInfo.getUserStyle().getFeatureTypeStyleList());

		}

		return widgetLayout;
	}
	
	
	
	public void clear(boolean optionallySaveCurrentSld) {
		GWT.log("Entering clear() with optionallySaveCurrentSld = " + optionallySaveCurrentSld);
		
		if (optionallySaveCurrentSld) {
			getCanvasForSLD(null);
		} else {
		
			sldHasChanged = false;
	
			if (null != saveButton) {
				saveButton.disable();
			}
	
			if (null != cancelButton) {
				cancelButton.disable();
			}
	
			if (null != ruleSelector) {
				ruleSelector.reset();
			}
			if (null != generalForm) {
				generalForm.clearValues();
			}
			clearForCurrentRule();
		}
	}


	private void init(SldGwtServiceAsync service) {

		this.service = service;
		
		widgetLayout = new VLayout();
		widgetLayout.setWidth("100%");
		widgetLayout.setHeight100();

		VLayout topContainer = new VLayout(5); /* Top container of widgetLayout */
		topContainer.setShowResizeBar(true); /* Resizebar to make the height of the ruleSelector bigger/smaller */
		topContainer.setMinHeight(200);

		topContainer.setLayoutBottomMargin(5);

		generalForm = new DynamicForm();
		generalForm.setNumCols(4);

		nameOfLayerItem = new TextItem("Laag", "Laag");
		nameOfLayerItem.setWidth(200);
		nameOfLayerItem.addChangedHandler(new ChangedHandler() {

			public void onChanged(ChangedEvent event) {
				setSldHasChangedTrue();
				if (!nameOfLayerItem.validate()) {
					return;
				}
				String nameOfLayer = null;
				if (null == event.getValue()) {
					nameOfLayer = "";
				} else {
					nameOfLayer = event.getValue().toString();
				}
				currentSld.getChoiceList().get(0).getNamedLayer().setName(nameOfLayer);
			}
		});

		nameOfLayerItem.setRequired(true);
		nameOfLayerItem.setRequiredMessage("De naam van de laag mag niet leeg zijn");
		nameOfLayerItem.setValidateOnChange(true);

		geomTypeItem = new TextItem("Geometrie-type");
		geomTypeItem.setWidth(150);
		geomTypeItem.disable(); // cannot be changed by the user

		styleTitleItem = new TextItem("Stijl", "Stijl");
		styleTitleItem.setWidth(300);
		styleTitleItem.setColSpan(4);

		styleTitleItem.addChangedHandler(new ChangedHandler() {

			public void onChanged(ChangedEvent event) {
				if (!styleTitleItem.validate()) {
					return;
				}
				setSldHasChangedTrue();
				String styleTitle = null;
				if (null == event.getValue()) {
					styleTitle = "";
				} else {
					styleTitle = event.getValue().toString();
				}

				StyledLayerDescriptorInfo.ChoiceInfo info = currentSld.getChoiceList().iterator().next(); /*
																										  * retrieve the
																										  * first choice
																										  */
				List<ChoiceInfo> choiceList = info.getNamedLayer().getChoiceList();
				ChoiceInfo choiceInfo = choiceList.iterator().next(); // retrieve the first constraint

				if (choiceInfo.ifNamedStyle()) {
					// Only the name is specialized
					if (null == choiceInfo.getNamedStyle()) {
						choiceInfo.setNamedStyle(new NamedStyleInfo());
					}
					choiceInfo.getNamedStyle().setName(styleTitle);
				} else if (choiceInfo.ifUserStyle()) {
					choiceInfo.getUserStyle().setTitle(styleTitle);
				}

			}
		});

		styleTitleItem.setRequired(true);
		styleTitleItem.setRequiredMessage("De naam van de stijl mag niet leeg zijn");
		styleTitleItem.setValidateOnChange(true);

		generalForm.setGroupTitle("Algemeen");
		generalForm.setIsGroup(true);

		generalForm.setItems(nameOfLayerItem, geomTypeItem, styleTitleItem);

		ruleSelector = new RuleSelector();

		ruleSelector.addSelectRuleHandler(new SelectRuleHandler() {

			public void execute(boolean ruleSelected, Object ruleInfo) {
				if (ruleSelected) {
					setRule(ruleInfo);
				} else {
					setNoRuleSelected();
				}

			}

		});

		ruleSelector.addGetCurrentRuleStateHandler(new GetCurrentRuleStateHandler() {

			public Object execute() {
				return getCurrentRuleState();
			}

		});

		ruleSelector.addSldHasChangedHandler(new SldHasChangedHandler() {

			public void execute() {
				setSldHasChangedTrue();
			}

		});

		ruleSelector.addUpdateRuleHeaderHandler(new UpdateRuleHeaderHandler() {

			public void execute(String ruleTitle, String ruleName) {
				if (null != currentRule) {
					setSldHasChangedTrue();
					currentRule.setTitle(ruleTitle);
					currentRule.setName(ruleName);
				}
			}

		});

		topContainer.addMember(generalForm);
		ruleSelector.setWidth("100%");
		topContainer.addMember(ruleSelector);

		topContainer.setHeight("35%"); /* SLD top-level items + rule selector take 30% of widget layout */

		topContainer.setWidth("100%");
		topContainer.setMinHeight(120);
		widgetLayout.addMember(topContainer); // in the top pane of the widget

		ruleDetailContainer = new VLayout(5);
		ruleDetailContainer.setMinHeight(200);
		ruleDetailContainer.setWidth("100%");
		ruleDetailContainer.setMinWidth(100);
		ruleDetailContainer.setLayoutTopMargin(10);
		ruleDetailContainer.setLayoutLeftMargin(5);
		ruleDetailContainer.setLayoutBottomMargin(5);
		ruleDetailContainer.setGroupTitle("Stijl in detail"); // TODO i18n
		ruleDetailContainer.setIsGroup(true);

		widgetLayout.addMember(ruleDetailContainer); /* In the bottom pane of the widget */

		noRuleSelectedMessage = new Label("<i>Geen stijl geselecteerd!</i>");
		noRuleSelectedMessage.setAlign(Alignment.CENTER);
		ruleDetailContainer.addMember(noRuleSelectedMessage);
		noRuleSelectedMessage.hide();

		TabSet topTabSet = new TabSet();
		topTabSet.setTabBarPosition(Side.TOP);

		symbolPane = new VLayout();
		symbolPane.setMembersMargin(5);
		symbolPane.setMargin(5);

		filterPane = new VLayout();

		Tab tTab1 = new Tab("Symbologie");

		tTab1.setPane(symbolPane);

		Tab tTab2 = new Tab("Filter");
		filterPane = new VLayout();
		filterPane.setMembersMargin(5);
		filterPane.setMargin(5);

		tTab2.setPane(filterPane);

		topTabSet.addTab(tTab1);
		topTabSet.addTab(tTab2);

		ruleDetailContainer.addMember(topTabSet);

		specificFormPoint = new DynamicForm();

		symbolPane.addMember(specificFormPoint);

		markerSymbolizerForm = new DynamicForm();
		markerSymbolizerForm.hide();
		symbolPane.addMember(markerSymbolizerForm);

		externalGraphicForm = new DynamicForm();
		externalGraphicForm.hide();
		symbolPane.addMember(externalGraphicForm);

		lineSymbolizerForm = new DynamicForm();
		lineSymbolizerForm.hide();
		symbolPane.addMember(lineSymbolizerForm);

		polygonSymbolizerForm = new DynamicForm();
		polygonSymbolizerForm.hide();
		symbolPane.addMember(polygonSymbolizerForm);

		filterEditor = new FilterEditor();

		filterEditor.setFilterHasChangedHandler(new FilterHasChangedHandler() {

			public void execute(boolean isComplete) {
				sldHasChanged = true;
				enableCancel(true);
				enableSave(isComplete);

			}
		});

		filterForm = filterEditor.getForm();
		filterForm.hide();
		filterPane.addMember(filterForm);

		savePanel = new HLayout(10);
		saveButton = new SaveButton();
		saveButton.disable();

		savePanel.addMember(saveButton);
		cancelButton = new CancelButton();
		cancelButton.disable();
		savePanel.addMember(cancelButton);

		// savePanel.addMember(resetButton);
		// savePanel.setVisible(false);
		savePanel.setAlign(Alignment.CENTER);
		// savePanel.setPadding(10);
		savePanel.setHeight(30);
		ruleDetailContainer.addMember(savePanel);


	}

	private Object getCurrentRuleState() {
		Object ruleData = null;
		if (null != currentRule) {
			if (isSupportedFilter) {
				ChoiceFilterInfo choiceFilterInfo = filterEditor.attemptConvertFormToFilter();

				if (choiceFilterInfo.ifIncompleteFilter()) {
					IncompleteRuleInfo incompleteRuleInfo = new IncompleteRuleInfo();

					IncompleteFilterInfo incompleteFilterInfo = choiceFilterInfo.getIncompleteFilter();

					incompleteRuleInfo.setIncompleteFilterInfo(incompleteFilterInfo);
					incompleteRuleInfo.setRuleInfo(currentRule);

					ruleData = incompleteRuleInfo;
					// currentLeaf.setRuleData(incompleteRuleInfo);
					enableSave(false);
				} else {
					setFilter(choiceFilterInfo.getFilter());
					enableSave(ruleSelector.checkIfAllRulesComplete());
					// currentLeaf.setRuleData(currentRule);
					ruleData = currentRule;
				}
			} else {
				enableSave(ruleSelector.checkIfAllRulesComplete());
				ruleData = currentRule;
				// currentLeaf.setRuleData(currentRule);
			}
		}
		return ruleData;
	}

	private void setNoRuleSelected() {
		currentRule = null;
		clearForCurrentRule();
		noRuleSelectedMessage.show();
	}

	private void setFilter(FilterTypeInfo filter) {
		/* set the filter for the current rule */
		if (null != currentRule) {

			if (null == filter) {
//				if (null != currentRule.getChoice()) {
//					SC.warn("De huidige filter wordt verwijderd!");
//				}
				// Remove the filter from the current rule!
				currentRule.setChoice(null);
			} else {

				if (null == currentRule.getChoice()
						|| (!currentRule.getChoice().ifElseFilter() && !currentRule.getChoice().ifFilter())) {
					/* There was no filter, create a filter */
					org.geomajas.sld.RuleInfo.ChoiceInfo choiceInfo = new org.geomajas.sld.RuleInfo.ChoiceInfo();

					choiceInfo.setFilter(filter);
					currentRule.setChoice(choiceInfo);
				} else {
					if (!currentRule.getChoice().ifFilter()) {
						/* There was no If filter */
						currentRule.getChoice().clearChoiceSelect();
					}

					currentRule.getChoice().setFilter(filter);
				}
			}
		}
	}

	private boolean synchronizeFilter() {
		boolean filterIsComplete = true;
		if (null != currentRule) {
			if (isSupportedFilter) {
				ChoiceFilterInfo choiceFilterInfo = filterEditor.attemptConvertFormToFilter();
				if (choiceFilterInfo.ifIncompleteFilter()) {
					filterIsComplete = false;
				} else {
					setFilter(choiceFilterInfo.getFilter());
				}
			}
		}
		return filterIsComplete;
	}

	/**
	 * Update the rule detail form items (incl. the associated filter).
	 * 
	 * @param object
	 * @return
	 */
	private Canvas setRule(Object object) {
		clearForCurrentRule();

		if (object.getClass().equals(IncompleteRuleInfo.class)) {
			enableSave(false);
			IncompleteRuleInfo incompleteRuleInfo = (IncompleteRuleInfo) object;

			if (null != incompleteRuleInfo.getIncompleteFilter()) {
				filterEditor.createOrUpdateFilterForm(incompleteRuleInfo.getIncompleteFilter());
			} else {
				// TODO: needed ?: currentFilterInfo = new FilterTypeInfo();
				/* create/empty filter form */
				filterEditor.createOrUpdateFilterForm(new IncompleteFilterInfo(null, null));
			}
			filterForm.show();
			// this.currentGeomType = SldUtils.getGeometryType(incompleteRuleInfo.getRule());
			return setRuleExclFilter(incompleteRuleInfo.getRule());
		} else if (object.getClass().equals(RuleInfo.class)) {
			RuleInfo rule = (RuleInfo) object;
			currentRule = (RuleInfo) object;
			// this.currentGeomType = SldUtils.getGeometryType(rule);

			if (null != rule.getChoice() && rule.getChoice().ifFilter()) {
				FilterTypeInfo filter = rule.getChoice().getFilter();

				isSupportedFilter = filterEditor.createOrUpdateFilterForm(filter);

			} else {
				// TODO: needed ?: currentFilterInfo = new FilterTypeInfo();
				/* create/empty filter form */
				filterEditor.createOrUpdateFilterForm(new IncompleteFilterInfo(null, null));
			}

			noRuleSelectedMessage.hide();

			filterForm.show();

			enableSave(ruleSelector.checkIfAllRulesComplete());

			return setRuleExclFilter((RuleInfo) rule);
		}
		return null; /* TODO: ERROR -> throw */
	}

	private void enableSave(boolean enable) {
		if (enable && sldHasChanged) {
			saveButton.enable();
		} else {
			saveButton.disable();
		}
	}

	private void enableCancel(boolean enable) {
		if (enable && null != this.refreshSldHandler) {
			cancelButton.enable();
		} else {
			cancelButton.disable();
		}
	}

	private Canvas setRuleExclFilter(RuleInfo rule) {
		DynamicForm specificForm = null;
		currentRule = rule;

		if (null == rule || null == rule.getSymbolizerList() || 0 == rule.getSymbolizerList().size()) {
			SC.warn("Ongeldige SLD: stijlregel dient een <xxxSymbolizer> te specifi&euml;ren.");
			return widgetLayout;
		}

		Object symbolizerInfo = rule.getSymbolizerList().get(0); // retrieve the first symbolizer specification

		if (symbolizerInfo.getClass().equals(PointSymbolizerInfo.class)) {
			geomTypeItem.setValue("Punt");

			PointSymbolizerInfo currentPointSymbolizerInfo = (PointSymbolizerInfo) symbolizerInfo;
			if (null == typeOfGraphicItem) {
				setupPointInfoTypeOfGraphicItem();
			}
			specificForm = specificFormPoint;
			GeometryInfo geometryInfo = currentPointSymbolizerInfo.getGeometry();
			if (null != geometryInfo) {
				if (null != geometryInfo.getPropertyName() && null != geometryInfo.getPropertyName().getValue()) {
					// Check if point (locatedAt)
					if (!"locatedAt".equalsIgnoreCase(geometryInfo.getPropertyName().getValue())
							&& !"location".equalsIgnoreCase(geometryInfo.getPropertyName().getValue())
							&& !"position".equalsIgnoreCase(geometryInfo.getPropertyName().getValue())
							&& !"centerOf".equalsIgnoreCase(geometryInfo.getPropertyName().getValue())) {

						SC.warn("The point symbolizer specifies as geometry property "
								+ geometryInfo.getPropertyName().getValue()
								+ "; this is assumed to be a point geometry.");
					}
				}
			}
			if (null != currentPointSymbolizerInfo.getGraphic()) {

				org.geomajas.sld.GraphicInfo.ChoiceInfo choiceInfoGraphic = currentPointSymbolizerInfo.getGraphic()
						.getChoiceList().get(0);
				if (null == choiceInfoGraphic) {
					List<org.geomajas.sld.GraphicInfo.ChoiceInfo> list = 
						new ArrayList<org.geomajas.sld.GraphicInfo.ChoiceInfo>();
					choiceInfoGraphic = new org.geomajas.sld.GraphicInfo.ChoiceInfo();
					list.add(choiceInfoGraphic);

					currentPointSymbolizerInfo.getGraphic().setChoiceList(list);
				}
				if (choiceInfoGraphic.ifExternalGraphic()) {
					typeOfGraphicItem.setValue(SELECT_TYPE_OF_GRAPHIC_EXTERNE_FIGUUR);
					createOrUpdateExternalGraphicForm(currentPointSymbolizerInfo.getGraphic());
				} else {
					/* default = marker */
					typeOfGraphicItem.setValue(SELECT_TYPE_OF_GRAPHIC_MARKER);
					createOrUpdateMarkerForm(currentPointSymbolizerInfo.getGraphic());
				}
			}
		} else if (symbolizerInfo.getClass().equals(LineSymbolizerInfo.class)) {
			geomTypeItem.setValue("Lijn");

			final LineSymbolizerInfo lineSymbolizerInfo = (LineSymbolizerInfo) symbolizerInfo;
			GeometryInfo geometryInfo = lineSymbolizerInfo.getGeometry();
			if (null != geometryInfo) {
				if (null != geometryInfo.getPropertyName() && null != geometryInfo.getPropertyName().getValue()) {
					// Check if line geometry: center-line, CenterLine, centerLineOf, edgeOf
					if (!"center-line".equalsIgnoreCase(geometryInfo.getPropertyName().getValue())
							&& !"CenterLine".equalsIgnoreCase(geometryInfo.getPropertyName().getValue())
							&& !"centerLineOf".equalsIgnoreCase(geometryInfo.getPropertyName().getValue())
							&& !"edgeOf".equalsIgnoreCase(geometryInfo.getPropertyName().getValue())) {

						SC.warn("The line symbolizer specifies as geometry property "
								+ geometryInfo.getPropertyName().getValue()
								+ "; this is assumed to be a line geometry.");
					}
				}
			}
			createOrUpdateLineSymbolizerForm(lineSymbolizerInfo);

		} else if (symbolizerInfo.getClass().equals(PolygonSymbolizerInfo.class)) {
			geomTypeItem.setValue("Polygoon");
			final PolygonSymbolizerInfo polygonSymbolizerInfo = (PolygonSymbolizerInfo) symbolizerInfo;
			GeometryInfo geometryInfo = polygonSymbolizerInfo.getGeometry();
			if (null != geometryInfo) {
				if (null != geometryInfo.getPropertyName() && null != geometryInfo.getPropertyName().getValue()) {
					// Check if polygon geom (the_area, extentOf, coverage)
					if (!"the_area".equalsIgnoreCase(geometryInfo.getPropertyName().getValue())
							&& !"extentOf".equalsIgnoreCase(geometryInfo.getPropertyName().getValue())
							&& !"coverage".equalsIgnoreCase(geometryInfo.getPropertyName().getValue())) {
						SC.warn("The polygon symbolizer specifies as geometry property "
								+ geometryInfo.getPropertyName().getValue()
								+ "; this is assumed to be a polygon geometry.");
					}
				}
			}
			createOrUpdatePolygonForm(polygonSymbolizerInfo);
		} else {
			SC.warn("Ongeldige stijlregel: <xxxSymbolizer> werd niet herkend.");
		}

		if (null != specificForm) {
			specificForm.show();
		}

		ruleDetailContainer.markForRedraw();
		widgetLayout.markForRedraw();
		return widgetLayout;

	}

	private void createOrUpdateLineSymbolizerForm(LineSymbolizerInfo lineSymbolizerInfo) {

		currentLineSymbolizerInfo = lineSymbolizerInfo;
		if (isLineSymbolizerFormFirstSetup) {
			setupLineSymbolizerForm();
			isLineSymbolizerFormFirstSetup = false;
		} else {
			lineSymbolizerForm.hide();
			lineSymbolizerForm.clearValues();
		}

		if (null == lineSymbolizerInfo.getStroke()) {
			lineSymbolizerInfo.setStroke(new StrokeInfo());
		}

		List<CssParameterInfo> cssParameterList = lineSymbolizerInfo.getStroke().getCssParameterList();

		if (null != cssParameterList) {
			for (CssParameterInfo cssParameterInfo : cssParameterList) {
				if (SldConstants.STROKE.equals(cssParameterInfo.getName())) {
					lineStrokeColorPicker.setValue(cssParameterInfo.getValue());
				} else if (SldConstants.STROKE_WIDTH.equals(cssParameterInfo.getName())) {
					strokeWidthItem.setValue(Float.parseFloat(cssParameterInfo.getValue()));
				} else if (SldConstants.STROKE_OPACITY.equals(cssParameterInfo.getName())) {
					strokeOpacityItem.setValue(factorToPercentage(cssParameterInfo.getValue()));
				} else if ("stroke-dasharray".equals(cssParameterInfo.getName())) {
					// TODO
				}
			}
		}

		lineSymbolizerForm.show();
	}

	private void createOrUpdateMarkerForm(GraphicInfo graphicInfo) {

		currrentGraphicInfo = graphicInfo;
		if (isMarkerSymbolizerFormFirstSetup) {
			setupMarkerForm();
			isMarkerSymbolizerFormFirstSetup = false;
		} else {
			markerSymbolizerForm.hide();
			markerSymbolizerForm.clearValues();
		}

		markerSizeItem.setValue(SldConstants.DEFAULT_SIZE_MARKER); /* init with default */
		if (null != graphicInfo.getSize()) {
			String size = graphicInfo.getSize().getValue();
			if (null != size) {
				markerSizeItem.setValue(Float.parseFloat(size));
			}
		}

		if (null != graphicInfo.getRotation()) {
			String rotation = graphicInfo.getRotation().getValue();
			if (null != rotation) {
				markerRotationItem.setValue(Double.parseDouble(rotation));
			}
		}

		org.geomajas.sld.GraphicInfo.ChoiceInfo choiceInfoGraphic = graphicInfo.getChoiceList().get(0);

		if (!choiceInfoGraphic.ifMark()) {
			/* create a default marker with default fill, stroke and wellKnownName */
			MarkInfo mark = new MarkInfo();
			mark.setFill(new FillInfo());
			mark.setStroke(new StrokeInfo());
			mark.setWellKnownName(new WellKnownNameInfo());
			mark.getWellKnownName().setWellKnownName(SldConstants.DEFAULT_WKNAME_FOR_MARKER); // TODO!!!

			mark.setFill(new FillInfo());
			mark.getFill().setCssParameterList(
					updateCssParameterList(null, SldConstants.FILL, SldConstants.DEFAULT_FILL_FOR_MARKER));

			mark.getFill().setCssParameterList(
					updateCssParameterList(mark.getFill().getCssParameterList(), SldConstants.FILL_OPACITY,
							percentageToFactor(SldConstants.DEFAULT_FILL_OPACITY_PERCENTAGE_FOR_MARKER)));

			mark.setStroke(new StrokeInfo());

			mark.getStroke().setCssParameterList(
					updateCssParameterList(mark.getStroke().getCssParameterList(), SldConstants.STROKE,
							SldConstants.DEFAULT_FILL_FOR_LINE));

			mark.getStroke().setCssParameterList(
					updateCssParameterList(mark.getStroke().getCssParameterList(), SldConstants.STROKE_WIDTH,
							(SldConstants.DEFAULT_STROKE_WIDTH).toString()));
			choiceInfoGraphic.setMark(mark);
			if (null == currrentGraphicInfo.getSize()) {
				currrentGraphicInfo.setSize(new SizeInfo());
				currrentGraphicInfo.getSize().setValue(SldConstants.DEFAULT_SIZE_MARKER.toString());
			}
		}
		currentMark = choiceInfoGraphic.getMark();

		if (null == currentMark.getFill()) { /* no filling */
			markerFillCheckBoxItem.setValue(false);
			markerFillColorPicker.setDisabled(true);
			markerFillOpacityItem.setDisabled(true);
			prevFillInfo = new FillInfo(); /* default fill */
		} else {
			markerFillCheckBoxItem.setValue(true);
			markerFillColorPicker.setDisabled(false);
			markerFillOpacityItem.setDisabled(false);
			prevFillInfo = currentMark.getFill();
		}

		WellKnownNameInfo nameInfo = choiceInfoGraphic.getMark().getWellKnownName();

		if (null != nameInfo) {
			markerSymbolName.setValue(nameInfo.getWellKnownName());
		}

		if (null != choiceInfoGraphic.getMark().getFill()) {

			List<CssParameterInfo> cssParameterList = choiceInfoGraphic.getMark().getFill().getCssParameterList();

			if (null != cssParameterList) {

				for (CssParameterInfo cssParameterInfo : cssParameterList) {
					if (cssParameterInfo.getName().equals(SldConstants.FILL)) {
						markerFillColorPicker.setValue(cssParameterInfo.getValue());
					} else if (cssParameterInfo.getName().equals(SldConstants.FILL_OPACITY)) {
						markerFillOpacityItem.setValue(factorToPercentage(cssParameterInfo.getValue()));
					}
				}
			}
		}

		/** A missing Stroke sub-element for a Marker means that the geometry will not be stroked **/
		if (null != choiceInfoGraphic.getMark().getStroke()) {
			markerBorderCheckBoxItem.setValue(true);

			markerSymbolizerForm.showItem("borderColor");
			markerSymbolizerForm.showItem("borderOpacity");
			markerSymbolizerForm.showItem("borderWidth");

			List<CssParameterInfo> cssParameterList = choiceInfoGraphic.getMark().getStroke().getCssParameterList();

			if (null != cssParameterList) {
				for (CssParameterInfo cssParameterInfo : cssParameterList) {
					if (cssParameterInfo.getName().equals(SldConstants.STROKE)) {
						markerStrokeColorPicker.setValue(cssParameterInfo.getValue());
					} else if (cssParameterInfo.getName().equals(SldConstants.STROKE_WIDTH)) {
						markerStrokeWidthItem.setValue(Float.parseFloat(cssParameterInfo.getValue()));
					} else if (cssParameterInfo.getName().equals(SldConstants.STROKE_OPACITY)) {
						markerStrokeOpacityItem.setValue(factorToPercentage(cssParameterInfo.getValue()));
					} else if ("stroke-dasharray".equals(cssParameterInfo.getName())) {
						// TODO
					}
				}
			}
		} else {
			markerBorderCheckBoxItem.setValue(false);
			markerSymbolizerForm.hideItem("borderColor");
			markerSymbolizerForm.hideItem("borderOpacity");
			markerSymbolizerForm.hideItem("borderWidth");
		}

		markerSymbolizerForm.show();
	}

	private void createOrUpdateExternalGraphicForm(GraphicInfo graphicInfo) {
		currrentGraphicInfo = graphicInfo;

		if (isExternalGraphicFormFirstSetup) {
			setupExternalGraphic();
		} else {
			externalGraphicForm.hide();
			externalGraphicForm.clearValues();
		}

		if (null != graphicInfo.getSize()) {
			String size = graphicInfo.getSize().getValue();
			externalGraphicSizeItem.setValue(size);
		}
		final org.geomajas.sld.GraphicInfo.ChoiceInfo choiceInfoGraphic = graphicInfo.getChoiceList().get(0);

		if (null != choiceInfoGraphic.getExternalGraphic().getFormat()) {
			String formatValue = choiceInfoGraphic.getExternalGraphic().getFormat().getFormat();
			graphicFormatItem.setValue(formatValue);
		}

		if (null != choiceInfoGraphic.getExternalGraphic().getOnlineResource()) {
			String urlValue = choiceInfoGraphic.getExternalGraphic().getOnlineResource().getHref().getHref();
			urlItem.setValue(urlValue);
		}

		isExternalGraphicFormFirstSetup = false;
		externalGraphicForm.show();
	}

	private void createOrUpdatePolygonForm(PolygonSymbolizerInfo polygonSymbolizerInfo) {

		currentPolygonSymbolizerInfo = polygonSymbolizerInfo;

		if (isPolygonSymbolizerFormFirstSetup) {
			setupPolygonSymbolizerForm();
			isPolygonSymbolizerFormFirstSetup = false;
		} else {
			polygonSymbolizerForm.hide();
			polygonSymbolizerForm.clearValues();
		}

		if (null == currentPolygonSymbolizerInfo.getFill()) { /* no filling */
			// OGC 02-070: If the Fill element is omitted from its parent element, then no fill will be rendered.
			polygonFillCheckBoxItem.setValue(false);
			polygonFillColorPicker.setDisabled(true);
			polygonFillOpacityItem.setDisabled(true);
			prevFillInfo = new FillInfo(); /* default fill */
		} else {
			polygonFillCheckBoxItem.setValue(true);
			polygonFillColorPicker.setDisabled(false);
			polygonFillOpacityItem.setDisabled(false);
			prevFillInfo = currentPolygonSymbolizerInfo.getFill();
		}

		if (null != currentPolygonSymbolizerInfo.getFill()) {

			List<CssParameterInfo> cssParameterList = currentPolygonSymbolizerInfo.getFill().getCssParameterList();

			if (null != cssParameterList) {

				for (CssParameterInfo cssParameterInfo : cssParameterList) {
					if (cssParameterInfo.getName().equals(SldConstants.FILL)) {
						polygonFillColorPicker.setValue(cssParameterInfo.getValue());
					} else if (cssParameterInfo.getName().equals(SldConstants.FILL_OPACITY)) {
						polygonFillOpacityItem.setValue(factorToPercentage(cssParameterInfo.getValue()));
					}
				}
			}
		}

		/**
		 * OGC 02-070: A missing Stroke sub-element for a polygon symbolizer means that the geometry will not be stroked
		 * (ANB: so no separate border)
		 **/
		if (null != currentPolygonSymbolizerInfo.getStroke()) {
			polygonBorderCheckBoxItem.setValue(true);

			polygonSymbolizerForm.showItem("borderColor");
			polygonSymbolizerForm.showItem("borderOpacity");
			polygonSymbolizerForm.showItem("borderWidth");

			List<CssParameterInfo> cssParameterList = currentPolygonSymbolizerInfo.getStroke().getCssParameterList();

			if (null != cssParameterList) {
				for (CssParameterInfo cssParameterInfo : cssParameterList) {
					if (cssParameterInfo.getName().equals(SldConstants.STROKE)) {
						polygonStrokeColorPicker.setValue(cssParameterInfo.getValue());
					} else if (cssParameterInfo.getName().equals(SldConstants.STROKE_WIDTH)) {
						polygonStrokeWidthItem.setValue(Float.parseFloat(cssParameterInfo.getValue()));
					} else if (cssParameterInfo.getName().equals(SldConstants.STROKE_OPACITY)) {
						polygonStrokeOpacityItem.setValue(factorToPercentage(cssParameterInfo.getValue()));
					} else if ("stroke-dasharray".equals(cssParameterInfo.getName())) {
						// TODO
					}
				}
			}
		} else {
			polygonBorderCheckBoxItem.setValue(false);
			polygonSymbolizerForm.hideItem("borderColor");
			polygonSymbolizerForm.hideItem("borderOpacity");
			polygonSymbolizerForm.hideItem("borderWidth");
		}

		polygonSymbolizerForm.show();
	}

	private void setupPointInfoTypeOfGraphicItem() {
		typeOfGraphicItem = new SelectItem();
		typeOfGraphicItem.setTitle("Type van grafische specificatie");

		final LinkedHashMap<String, String> typeOfGraphicList = new LinkedHashMap<String, String>();
		typeOfGraphicList.put(SELECT_TYPE_OF_GRAPHIC_MARKER, SELECT_TYPE_OF_GRAPHIC_MARKER);
		typeOfGraphicList.put(SELECT_TYPE_OF_GRAPHIC_EXTERNE_FIGUUR, SELECT_TYPE_OF_GRAPHIC_EXTERNE_FIGUUR);
		typeOfGraphicItem.setValueMap(typeOfGraphicList);

		typeOfGraphicItem.addChangedHandler(new ChangedHandler() {

			public void onChanged(ChangedEvent event) {
				ListGridRecord record = ((FormItem) event.getSource()).getSelectedRecord();

				if (record != null) {
					setSldHasChangedTrue();

					String newValue = (String) event.getValue();

					if (null != markerSymbolizerForm) {
						markerSymbolizerForm.hide();
					}
					if (null != externalGraphicForm) {
						externalGraphicForm.hide();
					}

					/*
					 * clear the old marker/external graphics specific info in the SLD domain object
					 */
					List<org.geomajas.sld.GraphicInfo.ChoiceInfo> list =
						new ArrayList<org.geomajas.sld.GraphicInfo.ChoiceInfo>();

					org.geomajas.sld.GraphicInfo.ChoiceInfo choiceInfoGraphic =
						new org.geomajas.sld.GraphicInfo.ChoiceInfo();
					list.add(choiceInfoGraphic);

					PointSymbolizerInfo pointSymbolizerInfo = (PointSymbolizerInfo) currentRule.getSymbolizerList()
							.get(0); // Assume only 1 symbolizer!

					if (null == pointSymbolizerInfo.getGraphic()) {
						pointSymbolizerInfo.setGraphic(new GraphicInfo());
					}
					pointSymbolizerInfo.getGraphic().setChoiceList(list);

					if (newValue.equals(SELECT_TYPE_OF_GRAPHIC_EXTERNE_FIGUUR)) {
						choiceInfoGraphic.setExternalGraphic(new ExternalGraphicInfo());
						createOrUpdateExternalGraphicForm(pointSymbolizerInfo.getGraphic());
					} else { // Default: marker
						createOrUpdateMarkerForm(pointSymbolizerInfo.getGraphic());
					}
				}
			}
		});
		specificFormPoint.setItems(typeOfGraphicItem);
	}

	private void setupExternalGraphic() {
		externalGraphicSizeItem = new SpinnerItem();
		externalGraphicSizeItem.setTitle("Grootte");
		externalGraphicSizeItem.setDefaultValue(12);
		externalGraphicSizeItem.setMin(0);
		externalGraphicSizeItem.setMax(200);
		externalGraphicSizeItem.setStep(1.0f);

		externalGraphicSizeItem.addChangedHandler(new ChangedHandler() {

			public void onChanged(ChangedEvent event) {
				String newValue = numericalToString(event.getValue(), null);

				if (newValue != null) {
					setSldHasChangedTrue();

					if (null == currrentGraphicInfo.getSize()) {
						currrentGraphicInfo.setSize(new SizeInfo());
					}
					currrentGraphicInfo.getSize().setValue(newValue);
					// Debugging: updateStyleDesc();
				}
			}
		});

		graphicFormatItem = new SelectItem();
		graphicFormatItem.setTitle("Type van grafische specificatie");

		final LinkedHashMap<String, String> graficFormatList = new LinkedHashMap<String, String>();
		graficFormatList.put("image/png", "image/png");
		graficFormatList.put("image/gif", "image/gif");
		graphicFormatItem.setValueMap(graficFormatList);
		graphicFormatItem.addChangedHandler(new ChangedHandler() {

			public void onChanged(ChangedEvent event) {
				ListGridRecord record = graphicFormatItem.getSelectedRecord();

				if (record != null) {
					setSldHasChangedTrue();

					org.geomajas.sld.GraphicInfo.ChoiceInfo choiceInfoGraphic = currrentGraphicInfo.getChoiceList()
							.get(0);
					String selected = (String) graphicFormatItem.getValue();
					/*
					 * clear the old onlineResource (also in the SLD domain object)
					 */
					urlItem.setValue("");

					FormatInfo format = choiceInfoGraphic.getExternalGraphic().getFormat();
					if (null == format) {
						format = new FormatInfo();
						choiceInfoGraphic.getExternalGraphic().setFormat(format);
					}
					format.setFormat(selected);
					choiceInfoGraphic.getExternalGraphic().setOnlineResource(null);
					// Debugging: updateStyleDesc();
				}
			}
		});

		/** **/
		urlItem = new TextItem("Href", "Href");
		urlItem.addChangedHandler(new ChangedHandler() {

			public void onChanged(ChangedEvent event) {
				setSldHasChangedTrue();
				org.geomajas.sld.GraphicInfo.ChoiceInfo choiceInfoGraphic = currrentGraphicInfo.getChoiceList().get(0);
				OnlineResourceInfo onlineResourceInfo = choiceInfoGraphic.getExternalGraphic().getOnlineResource();
				if (null == onlineResourceInfo) {
					onlineResourceInfo = new OnlineResourceInfo();
					HrefInfo href = new HrefInfo();
					onlineResourceInfo.setHref(href);
					choiceInfoGraphic.getExternalGraphic().setOnlineResource(onlineResourceInfo);
				}
				onlineResourceInfo.getHref().setHref(event.getValue().toString());
				// Debugging: updateStyleDesc();
			}
		});

		externalGraphicForm.setItems(graphicFormatItem, urlItem, externalGraphicSizeItem);
	}

	private void setupMarkerForm() {
		/** Construct and setup "marker symbol size" form field **/

		markerSizeItem = new SpinnerItem();
		markerSizeItem.setTitle("Grootte");
		markerSizeItem.setDefaultValue(SldConstants.DEFAULT_SIZE_MARKER);
		markerSizeItem.setMin(0);
		markerSizeItem.setMax(200);
		markerSizeItem.setStep(1.0f);
		markerSizeItem.addChangedHandler(new ChangedHandler() {

			public void onChanged(ChangedEvent event) {
				String newValue = numericalToString(event.getValue(), SldConstants.DEFAULT_SIZE_MARKER.toString());

				if (newValue != null) {
					setSldHasChangedTrue();

					if (null == currrentGraphicInfo.getSize()) {
						currrentGraphicInfo.setSize(new SizeInfo());
					}
					currrentGraphicInfo.getSize().setValue(newValue);
					// Debugging: updateStyleDesc();
				}
			}
		});

		/** Construct and setup "marker symbol rotation" form field **/

		markerRotationItem = new SpinnerItem();
		markerRotationItem.setTitle("Rotatie");
		markerRotationItem.setDefaultValue(SldConstants.DEFAULT_ROTATION_MARKER);
		markerRotationItem.setMin(-360);
		markerRotationItem.setMax(360);
		markerRotationItem.setStep(15.0f);
		markerRotationItem.addChangedHandler(new ChangedHandler() {

			public void onChanged(ChangedEvent event) {
				// ((FormItem) event.getSource()).validate();
				setSldHasChangedTrue();

				String newValue = numericalToString(event.getValue(), SldConstants.DEFAULT_ROTATION_MARKER_AS_STRING);

				if (null == currrentGraphicInfo.getRotation()) {
					currrentGraphicInfo.setRotation(new RotationInfo());
				}
				currrentGraphicInfo.getRotation().setValue(newValue);
				// Debugging: updateStyleDesc();
			}
		});

		/** Construct and setup "marker symbol name" form field **/

		markerSymbolName = new SelectItem();
		markerSymbolName.setTitle("Symbool-naam");
		markerSymbolName.setDefaultValue(SldConstants.DEFAULT_WKNAME_FOR_MARKER);

		final LinkedHashMap<String, String> markerSymbolList = new LinkedHashMap<String, String>();
		// See http://docs.geoserver.org/stable/en/user/styling/sld-extensions/pointsymbols.html
		// The SLD specification mandates the support of the following symbols:
		markerSymbolList.put("cross", "cross (+)");
		markerSymbolList.put("circle", "circle");
		markerSymbolList.put("square", "square");
		markerSymbolList.put("star", "star");
		markerSymbolList.put("triangle", "triangle");
		markerSymbolList.put("X", "X");

		markerSymbolName.setValueMap(markerSymbolList);
		markerSymbolName.addChangedHandler(new ChangedHandler() {

			public void onChanged(ChangedEvent event) {
				setSldHasChangedTrue();

				String selected = null;
				ListGridRecord record = markerSymbolName.getSelectedRecord();
				if (record == null) {
					selected = SldConstants.DEFAULT_WKNAME_FOR_MARKER;
				} else {
					selected = (String) event.getValue();
				}
				WellKnownNameInfo nameInfo = currentMark.getWellKnownName();

				if (null == nameInfo && record != null) {
					nameInfo = new WellKnownNameInfo();
					currentMark.setWellKnownName(nameInfo);
				}
				if (null != nameInfo) {
					currentMark.getWellKnownName().setWellKnownName(selected);
				}
			}
		});

		/** Construct and setup "marker FillCheckBoxItem" form field **/
		markerFillCheckBoxItem = new CheckboxItem();
		markerFillCheckBoxItem.setTitle("Inschakelen van opvulling");
		markerFillCheckBoxItem.setDefaultValue(true);

		markerFillCheckBoxItem.addChangedHandler(new ChangedHandler() {

			public void onChanged(ChangedEvent event) {
				setSldHasChangedTrue();

				Boolean newValue = (Boolean) event.getValue();

				if (newValue == null) {
					newValue = true;
				}
				if (newValue) {
					/* restore prev info */
					currentMark.setFill(prevFillInfo);
					markerFillColorPicker.setDisabled(false);
					markerFillOpacityItem.setDisabled(false);

				} else {

					markerFillColorPicker.setDisabled(true);
					markerFillOpacityItem.setDisabled(true);
					if (null != currentMark.getFill()) {
						prevFillInfo = currentMark.getFill();
					}
					currentMark.setFill(null); /* No filling at the moment */
				}
			}
		});

		/** Construct and setup "marker symbol fill color" form field **/

		markerFillColorPicker = new ColorPickerItem();
		markerFillColorPicker.setTitle("Opvulling");
		markerFillColorPicker.setDefaultValue(SldConstants.DEFAULT_FILL_FOR_MARKER);

		markerFillColorPicker.addChangedHandler(new ChangedHandler() {

			public void onChanged(ChangedEvent event) {

				setSldHasChangedTrue();

				String newValue = (String) event.getValue();
				if (null == newValue) {
					newValue = SldConstants.DEFAULT_FILL_FOR_MARKER;
				}
				if (null == currentMark.getFill()) {
					currentMark.setFill(new FillInfo());
				}
				currentMark.getFill()
						.setCssParameterList(
								updateCssParameterList(currentMark.getFill().getCssParameterList(), SldConstants.FILL,
										newValue));

				// Debugging: updateStyleDesc();
			}
		});

		/** Construct and setup "marker symbol fill opacity" form field **/

		markerFillOpacityItem = new SpinnerItem();
		markerFillOpacityItem.setTitle("Opaciteit opvulling (%)");
		markerFillOpacityItem.setDefaultValue(SldConstants.DEFAULT_FILL_OPACITY_PERCENTAGE_FOR_MARKER);
		markerFillOpacityItem.setMin(0);
		markerFillOpacityItem.setMax(100);
		markerFillOpacityItem.setStep(10.0f);
		markerFillOpacityItem.addChangedHandler(new ChangedHandler() {

			public void onChanged(ChangedEvent event) {
				// ((DynamicForm) event.getSource()).validate();
				setSldHasChangedTrue();

				float newValue = numericalToFloat(event.getValue(),
						SldConstants.DEFAULT_FILL_OPACITY_PERCENTAGE_FOR_MARKER);

				if (null == currentMark.getFill()) {
					currentMark.setFill(new FillInfo());
				}
				currentMark.getFill().setCssParameterList(
						updateCssParameterList(currentMark.getFill().getCssParameterList(), SldConstants.FILL_OPACITY,
								percentageToFactor(newValue)));
				// Debugging: updateStyleDesc();
			}
		});

		IntegerRangeValidator rangeValidator = new IntegerRangeValidator();
		rangeValidator.setMin(0);
		rangeValidator.setMax(100);
		rangeValidator.setErrorMessage("Vul een opaciteit in tussen 0 en 100%");

		markerFillOpacityItem.setValidators(rangeValidator);
		markerFillOpacityItem.setValidateOnChange(true);

		/*
		 * Border (stroke) fields for a marker symbol
		 */
		markerBorderCheckBoxItem = new CheckboxItem();
		markerBorderCheckBoxItem.setTitle("Inschakelen van omranding");
		markerBorderCheckBoxItem.setDefaultValue(false);

		markerBorderCheckBoxItem.addChangedHandler(new ChangedHandler() {

			public void onChanged(ChangedEvent event) {
				setSldHasChangedTrue();
				Boolean newValue = (Boolean) event.getValue();

				if (newValue == null) {
					newValue = false;
				}
				if (newValue) {
					markerSymbolizerForm.showItem("borderColor");
					markerSymbolizerForm.showItem("borderOpacity");
					markerSymbolizerForm.showItem("borderWidth");

					currentMark.setStroke(new StrokeInfo()); /* default border */

				} else {
					markerSymbolizerForm.hideItem("borderColor");
					markerSymbolizerForm.hideItem("borderOpacity");
					markerSymbolizerForm.hideItem("borderWidth");
					currentMark.setStroke(null); /* No border */
				}
			}
		});

		markerStrokeColorPicker = new ColorPickerItem();
		markerStrokeColorPicker.setName("borderColor");
		markerStrokeColorPicker.setTitle("Lijnkleur rand");
		markerStrokeColorPicker.setDefaultValue(SldConstants.DEFAULT_FILL_FOR_LINE);

		markerStrokeColorPicker.addChangedHandler(new ChangedHandler() {

			public void onChanged(ChangedEvent event) {

				setSldHasChangedTrue();
				String newValue = (String) event.getValue();
				if (null == newValue) {
					newValue = SldConstants.DEFAULT_FILL_FOR_LINE;
				}
				if (null == currentMark.getStroke()) {
					currentMark.setStroke(new StrokeInfo());
				}

				currentMark.getStroke().setCssParameterList(
						updateCssParameterList(currentMark.getStroke().getCssParameterList(), SldConstants.STROKE,
								newValue));

				// Debugging: updateStyleDesc();
			}
		});

		markerStrokeWidthItem = new SpinnerItem();
		markerStrokeWidthItem.setName("borderWidth");
		markerStrokeWidthItem.setTitle("Lijndikte rand");
		markerStrokeWidthItem.setDefaultValue(SldConstants.DEFAULT_STROKE_WIDTH);
		markerStrokeWidthItem.setMin(0);
		markerStrokeWidthItem.setMax(100);
		markerStrokeWidthItem.setStep(1.0f);
		markerStrokeWidthItem.addChangedHandler(new ChangedHandler() {

			public void onChanged(ChangedEvent event) {
				setSldHasChangedTrue();

				float newValue = numericalToFloat(event.getValue(), SldConstants.DEFAULT_STROKE_WIDTH);

				if (null == currentMark.getStroke()) {
					currentMark.setStroke(new StrokeInfo());
				}

				currentMark.getStroke().setCssParameterList(
						updateCssParameterList(currentMark.getStroke().getCssParameterList(),
								SldConstants.STROKE_WIDTH, new Float(newValue).toString()));

				// Debugging: updateStyleDesc();

			}
		});
		/** Stroke opacity **/
		markerStrokeOpacityItem = new SpinnerItem();
		markerStrokeOpacityItem.setName("borderOpacity");
		markerStrokeOpacityItem.setTitle("Opaciteit rand (%)");
		markerStrokeOpacityItem.setDefaultValue(SldConstants.DEFAULT_STROKE_OPACITY_PERCENTAGE);
		markerStrokeOpacityItem.setMin(0);
		markerStrokeOpacityItem.setMax(100);
		markerStrokeOpacityItem.setStep(10.0f);
		markerStrokeOpacityItem.addChangedHandler(new ChangedHandler() {

			public void onChanged(ChangedEvent event) {
				setSldHasChangedTrue();

				float newValue = numericalToFloat(event.getValue(), SldConstants.DEFAULT_STROKE_OPACITY_PERCENTAGE);

				if (null == currentMark.getStroke()) {
					currentMark.setStroke(new StrokeInfo());
				}

				currentMark.getStroke().setCssParameterList(
						updateCssParameterList(currentMark.getStroke().getCssParameterList(),
								SldConstants.STROKE_OPACITY, percentageToFactor(newValue)));
				// Debugging: updateStyleDesc();

			}
		});

		IntegerRangeValidator rangeValidatorStrokeOpacity = new IntegerRangeValidator();
		rangeValidatorStrokeOpacity.setMin(0);
		rangeValidatorStrokeOpacity.setMax(100);
		rangeValidatorStrokeOpacity.setErrorMessage("Vul een opaciteit in tussen 0 en 100%");

		markerStrokeOpacityItem.setValidators(rangeValidatorStrokeOpacity);
		markerStrokeOpacityItem.setValidateOnChange(true);

		markerSymbolizerForm.hide();
		markerSymbolizerForm.setItems(markerSymbolName, markerSizeItem, markerRotationItem, markerFillCheckBoxItem,
				markerFillColorPicker, markerFillOpacityItem, markerBorderCheckBoxItem, markerStrokeColorPicker,
				markerStrokeWidthItem, markerStrokeOpacityItem);

		markerSymbolizerForm.hideItem("borderColor");
		markerSymbolizerForm.hideItem("borderOpacity");
		markerSymbolizerForm.hideItem("borderWidth");

	}

	/**
	 * Construct and setup "polygon symbolizer" form.
	 */
	private void setupPolygonSymbolizerForm() {

		polygonFillCheckBoxItem = new CheckboxItem();
		polygonFillCheckBoxItem.setTitle("Inschakelen van opvulling");
		polygonFillCheckBoxItem.setDefaultValue(true);

		polygonFillCheckBoxItem.addChangedHandler(new ChangedHandler() {

			public void onChanged(ChangedEvent event) {
				setSldHasChangedTrue();

				Boolean newValue = (Boolean) event.getValue();

				if (newValue == null) {
					newValue = true;
				}
				if (newValue) {
					/* restore prev info */
					currentPolygonSymbolizerInfo.setFill(prevFillInfo);
					polygonFillColorPicker.setDisabled(false);
					polygonFillOpacityItem.setDisabled(false);

				} else {

					polygonFillColorPicker.setDisabled(true);
					polygonFillOpacityItem.setDisabled(true);
					if (null != currentPolygonSymbolizerInfo.getFill()) {
						prevFillInfo = currentPolygonSymbolizerInfo.getFill();
					}
					currentPolygonSymbolizerInfo.setFill(null); /* No filling at the moment */
				}
			}
		});

		/** Construct and setup "polygon symbol fill color" form field **/

		polygonFillColorPicker = new ColorPickerItem();
		polygonFillColorPicker.setTitle("Opvulling");
		polygonFillColorPicker.setDefaultValue(SldConstants.DEFAULT_FILL_FOR_POLYGON);

		polygonFillColorPicker.addChangedHandler(new ChangedHandler() {

			public void onChanged(ChangedEvent event) {

				setSldHasChangedTrue();
				String newValue = (String) event.getValue();
				if (null == newValue) {
					newValue = SldConstants.DEFAULT_FILL_FOR_POLYGON;
				}
				if (null == currentPolygonSymbolizerInfo.getFill()) {
					currentPolygonSymbolizerInfo.setFill(new FillInfo());
				}
				currentPolygonSymbolizerInfo.getFill().setCssParameterList(
						updateCssParameterList(currentPolygonSymbolizerInfo.getFill().getCssParameterList(),
								SldConstants.FILL, newValue));

				// Debugging: updateStyleDesc();
			}
		});

		/** Construct and setup "polygon symbol fill opacity" form field **/

		polygonFillOpacityItem = new SpinnerItem();
		polygonFillOpacityItem.setTitle("Opaciteit opvulling (%)");
		polygonFillOpacityItem.setDefaultValue(SldConstants.DEFAULT_FILL_OPACITY_PERCENTAGE_FOR_POLYGON);
		polygonFillOpacityItem.setMin(0);
		polygonFillOpacityItem.setMax(100);
		polygonFillOpacityItem.setStep(10.0f);
		polygonFillOpacityItem.addChangedHandler(new ChangedHandler() {

			public void onChanged(ChangedEvent event) {
				setSldHasChangedTrue();

				float newValue = numericalToFloat(event.getValue(),
						SldConstants.DEFAULT_FILL_OPACITY_PERCENTAGE_FOR_POLYGON);

				if (null == currentPolygonSymbolizerInfo.getFill()) {
					currentPolygonSymbolizerInfo.setFill(new FillInfo());
				}
				currentPolygonSymbolizerInfo.getFill().setCssParameterList(
						updateCssParameterList(currentPolygonSymbolizerInfo.getFill().getCssParameterList(),
								SldConstants.FILL_OPACITY, percentageToFactor(newValue)));
				// Debugging: updateStyleDesc();
			}
		});

		IntegerRangeValidator rangeValidator = new IntegerRangeValidator();
		rangeValidator.setMin(0);
		rangeValidator.setMax(100);
		rangeValidator.setErrorMessage("Vul een opaciteit in tussen 0 en 100%");

		polygonFillOpacityItem.setValidators(rangeValidator);
		polygonFillOpacityItem.setValidateOnChange(true);

		/*
		 * Border (stroke) attributes for a polygon symbol
		 */
		polygonBorderCheckBoxItem = new CheckboxItem();
		polygonBorderCheckBoxItem.setTitle("Inschakelen van omranding");
		polygonBorderCheckBoxItem.setDefaultValue(true);

		polygonBorderCheckBoxItem.addChangedHandler(new ChangedHandler() {

			public void onChanged(ChangedEvent event) {
				Boolean newValue = (Boolean) event.getValue();
				setSldHasChangedTrue();

				if (newValue == null) {
					newValue = false;
				}
				if (newValue) {
					polygonSymbolizerForm.showItem("borderColor");
					polygonSymbolizerForm.showItem("borderOpacity");
					polygonSymbolizerForm.showItem("borderWidth");

					currentPolygonSymbolizerInfo.setStroke(new StrokeInfo()); /* default border */

				} else {
					polygonSymbolizerForm.hideItem("borderColor");
					polygonSymbolizerForm.hideItem("borderOpacity");
					polygonSymbolizerForm.hideItem("borderWidth");
					currentPolygonSymbolizerInfo.setStroke(null); /* No border */
				}
			}
		});

		polygonStrokeColorPicker = new ColorPickerItem();
		polygonStrokeColorPicker.setName("borderColor");
		polygonStrokeColorPicker.setTitle("Lijnkleur rand");
		polygonStrokeColorPicker.setDefaultValue(SldConstants.DEFAULT_FILL_FOR_LINE);

		polygonStrokeColorPicker.addChangedHandler(new ChangedHandler() {

			public void onChanged(ChangedEvent event) {

				setSldHasChangedTrue();
				String newValue = (String) event.getValue();
				if (null == newValue) {
					newValue = SldConstants.DEFAULT_FILL_FOR_LINE;
				}
				if (null == currentPolygonSymbolizerInfo.getStroke()) {
					currentPolygonSymbolizerInfo.setStroke(new StrokeInfo());
				}

				currentPolygonSymbolizerInfo.getStroke().setCssParameterList(
						updateCssParameterList(currentPolygonSymbolizerInfo.getStroke().getCssParameterList(),
								SldConstants.STROKE, newValue));

				// Debugging: updateStyleDesc();
			}
		});

		polygonStrokeWidthItem = new SpinnerItem();
		polygonStrokeWidthItem.setName("borderWidth");
		polygonStrokeWidthItem.setTitle("Lijndikte rand");
		polygonStrokeWidthItem.setDefaultValue(SldConstants.DEFAULT_STROKE_WIDTH);
		polygonStrokeWidthItem.setMin(0);
		polygonStrokeWidthItem.setMax(100);
		polygonStrokeWidthItem.setStep(1.0f);
		polygonStrokeWidthItem.addChangedHandler(new ChangedHandler() {

			public void onChanged(ChangedEvent event) {
				setSldHasChangedTrue();

				float newValue = numericalToFloat(event.getValue(), (float) SldConstants.DEFAULT_STROKE_WIDTH);

				if (null == currentPolygonSymbolizerInfo.getStroke()) {
					currentPolygonSymbolizerInfo.setStroke(new StrokeInfo());
				}

				currentPolygonSymbolizerInfo.getStroke().setCssParameterList(
						updateCssParameterList(currentPolygonSymbolizerInfo.getStroke().getCssParameterList(),
								SldConstants.STROKE_WIDTH, new Float(newValue).toString()));

				// Debugging: updateStyleDesc();

			}
		});
		/** Stroke opacity **/
		polygonStrokeOpacityItem = new SpinnerItem();
		polygonStrokeOpacityItem.setName("borderOpacity");
		polygonStrokeOpacityItem.setTitle("Opaciteit rand (%)");
		polygonStrokeOpacityItem.setDefaultValue(SldConstants.DEFAULT_STROKE_OPACITY_PERCENTAGE);
		polygonStrokeOpacityItem.setMin(0);
		polygonStrokeOpacityItem.setMax(100);
		polygonStrokeOpacityItem.setStep(10.0f);
		polygonStrokeOpacityItem.addChangedHandler(new ChangedHandler() {

			public void onChanged(ChangedEvent event) {
				setSldHasChangedTrue();

				float newValue = numericalToFloat(event.getValue(), SldConstants.DEFAULT_STROKE_OPACITY_PERCENTAGE);

				if (null == currentPolygonSymbolizerInfo.getStroke()) {
					currentPolygonSymbolizerInfo.setStroke(new StrokeInfo());
				}

				currentPolygonSymbolizerInfo.getStroke().setCssParameterList(
						updateCssParameterList(currentPolygonSymbolizerInfo.getStroke().getCssParameterList(),
								SldConstants.STROKE_OPACITY, percentageToFactor(newValue)));
				// Debugging: updateStyleDesc();

			}
		});

		IntegerRangeValidator rangeValidatorStrokeOpacity = new IntegerRangeValidator();
		rangeValidatorStrokeOpacity.setMin(0);
		rangeValidatorStrokeOpacity.setMax(100);
		rangeValidatorStrokeOpacity.setErrorMessage("Vul een opaciteit in tussen 0 en 100%");

		polygonStrokeOpacityItem.setValidators(rangeValidatorStrokeOpacity);
		polygonStrokeOpacityItem.setValidateOnChange(true);

		polygonSymbolizerForm.hide();
		polygonSymbolizerForm.setItems(polygonFillCheckBoxItem, polygonFillColorPicker, polygonFillOpacityItem,
				polygonBorderCheckBoxItem, polygonStrokeColorPicker, polygonStrokeWidthItem, polygonStrokeOpacityItem);

		polygonSymbolizerForm.hideItem("borderColor");
		polygonSymbolizerForm.hideItem("borderOpacity");
		polygonSymbolizerForm.hideItem("borderWidth");

	}

	// assumes currentLineSymbolizerInfo.getStroke() is always non-null when entering a ChangedHandler

	private void setupLineSymbolizerForm() {
		lineStrokeColorPicker = new ColorPickerItem();
		lineStrokeColorPicker.setTitle("Lijnkleur");
		lineStrokeColorPicker.setDefaultValue(SldConstants.DEFAULT_FILL_FOR_LINE);

		lineStrokeColorPicker.addChangedHandler(new ChangedHandler() {

			public void onChanged(ChangedEvent event) {
				setSldHasChangedTrue();

				String newValue = (String) event.getValue();
				if (null == newValue) {
					newValue = SldConstants.DEFAULT_FILL_FOR_LINE;
				}

				currentLineSymbolizerInfo.getStroke().setCssParameterList(
						updateCssParameterList(currentLineSymbolizerInfo.getStroke().getCssParameterList(),
								SldConstants.STROKE, newValue));

				// Debugging: updateStyleDesc();
			}
		});

		strokeWidthItem = new SpinnerItem();
		strokeWidthItem.setTitle("Lijndikte");
		strokeWidthItem.setDefaultValue(SldConstants.DEFAULT_STROKE_WIDTH_FOR_LINE);
		strokeWidthItem.setMin(0);
		strokeWidthItem.setMax(100);
		strokeWidthItem.setStep(1.0f);
		strokeWidthItem.addChangedHandler(new ChangedHandler() {

			public void onChanged(ChangedEvent event) {
				setSldHasChangedTrue();

				Integer newValue = (Integer) event.getValue();

				if (newValue == null) {
					newValue = SldConstants.DEFAULT_STROKE_WIDTH_FOR_LINE;
				}

				currentLineSymbolizerInfo.getStroke().setCssParameterList(
						updateCssParameterList(currentLineSymbolizerInfo.getStroke().getCssParameterList(),
								SldConstants.STROKE_WIDTH, newValue.toString()));

				// Debugging: updateStyleDesc();

			}
		});
		IntegerRangeValidator rangeValidatorWidth = new IntegerRangeValidator();
		rangeValidatorWidth.setMin(0);
		rangeValidatorWidth.setMax(100);
		rangeValidatorWidth.setErrorMessage("Vul een dikte in tussen 0 en 100");

		strokeWidthItem.setValidators(rangeValidatorWidth);
		strokeWidthItem.setValidateOnChange(true);

		/** Stroke opacity **/
		strokeOpacityItem = new SpinnerItem();
		strokeOpacityItem.setTitle("Opaciteit (%)");
		strokeOpacityItem.setDefaultValue(SldConstants.DEFAULT_STROKE_OPACITY_PERCENTAGE);
		strokeOpacityItem.setMin(0);
		strokeOpacityItem.setMax(100);
		strokeOpacityItem.setStep(10.0f);
		strokeOpacityItem.addChangedHandler(new ChangedHandler() {

			public void onChanged(ChangedEvent event) {
				setSldHasChangedTrue();

				Integer newValue = (Integer) event.getValue();

				if (null == newValue) {
					newValue = SldConstants.DEFAULT_STROKE_OPACITY_PERCENTAGE;
				}

				currentLineSymbolizerInfo.getStroke().setCssParameterList(
						updateCssParameterList(currentLineSymbolizerInfo.getStroke().getCssParameterList(),
								SldConstants.STROKE_OPACITY, percentageToFactor(newValue)));
				// Debugging: updateStyleDesc();

			}
		});
		IntegerRangeValidator rangeValidator = new IntegerRangeValidator();
		rangeValidator.setMin(0);
		rangeValidator.setMax(100);
		rangeValidator.setErrorMessage("Vul een opaciteit in tussen 0 en 100%");

		strokeOpacityItem.setValidators(rangeValidator);
		strokeOpacityItem.setValidateOnChange(true);

		lineSymbolizerForm.hide();
		lineSymbolizerForm.setItems(lineStrokeColorPicker, strokeWidthItem, strokeOpacityItem);
	}

	private String numericalToString(Object value, String defaultStringValue) {
		String stringValue = defaultStringValue;

		if (value != null) {
			stringValue = value.toString();
		}
		// if (value.getClass().equals(Float.class)) {
		// Float doubleValue = (Float) value;
		// stringValue = Double.toString(doubleValue);
		// } else if (value.getClass().equals(Integer.class)) {
		// Integer intValue = (Integer) value;
		// stringValue = Integer.toString(intValue);
		// }

		return stringValue;
	}

	private float numericalToFloat(Object value, float defaultValue) {
		float floatValue = defaultValue;

		if (null != value) {
			if (value.getClass().equals(Float.class)) {
				floatValue = ((Float) value).floatValue();
			} else if (value.getClass().equals(Integer.class)) {
				Integer intValue = (Integer) value;
				floatValue = (float) intValue;
			}
		}

		return floatValue;
	}

	private int factorToPercentage(String value) {
		float factor = Float.parseFloat(value);

		return (int) Math.round(factor * 100.0);
	}

	private String percentageToFactor(int percentage) {
		if (percentage > 100) {
			percentage = 100;
		} else if (percentage < 0) {
			percentage = 0;
		}
		// Note alternative using String.format() cannot be compiled by GWT
		if (percentage == 100) {
			return "1.0";
		} else if (percentage >= 10) { /* 10 to 99 -> 0.10 to 0.99 */
			return "0." + Integer.toString(percentage);
		} else { /* 0 to 9 -> 0.00 to 0.09 */
			return "0.0" + Integer.toString(percentage);
		}
	}

	private String percentageToFactor(float percentage) {
		if (percentage > 100.0) {
			percentage = 100.0F;
		} else if (percentage < 0.0) {
			percentage = 0.0F;
		}
		// Cannot be compiled by GWT: Float factor = new Float((float)percentage / 100.0F);
		// String format = "%.3f"; // decimal fraction notation with fractional part of 3 digits
		//
		// return String.format(format, factor);
		int percentageRounded = Math.round(percentage);
		if (percentageRounded == 100) {
			return "1.0";
		} else if (percentageRounded >= 10) { /* 10 to 99 -> 0.10 to 0.99 */
			return "0." + Integer.toString(percentageRounded);
		} else { /* 0 to 9 -> 0.00 to 0.09 */
			return "0.0" + Integer.toString(percentageRounded);
		}
	}

	private List<CssParameterInfo> updateCssParameterList(List<CssParameterInfo> currentCssParameterList,
			String parameterName, String newValue) {
		CssParameterInfo cssParameterInfoTarget = null;

		if (currentCssParameterList == null) {
			currentCssParameterList = new ArrayList<CssParameterInfo>();
		}
		for (CssParameterInfo cssParameterInfo : currentCssParameterList) {
			if (cssParameterInfo.getName().equals(parameterName)) {
				cssParameterInfoTarget = cssParameterInfo;
				break;
			}
		}
		if (null == cssParameterInfoTarget) {
			// Only create when really needed (onChange) of fillColorPicker
			CssParameterInfo cssParameterInfo = new CssParameterInfo();
			cssParameterInfo.setName(parameterName);
			currentCssParameterList.add(cssParameterInfo);
			cssParameterInfoTarget = cssParameterInfo;
		}
		cssParameterInfoTarget.setValue(newValue);
		return currentCssParameterList;

	}


	private void clearForCurrentRule() {
		isSupportedFilter = true;

		if (null != noRuleSelectedMessage) {
			noRuleSelectedMessage.show();
		}
		if (null != specificFormPoint) {
			specificFormPoint.hide();
			specificFormPoint.clearValues();
		}
		if (null != markerSymbolizerForm) {
			markerSymbolizerForm.hide();
			markerSymbolizerForm.clearValues();
		}
		if (null != externalGraphicForm) {
			externalGraphicForm.hide();
			externalGraphicForm.clearValues();
		}
		if (null != lineSymbolizerForm) {
			lineSymbolizerForm.hide();
			lineSymbolizerForm.clearValues();
		}
		if (null != polygonSymbolizerForm) {
			polygonSymbolizerForm.hide();
			polygonSymbolizerForm.clearValues();
		}
		if (null != filterForm) {
			filterForm.hide();
			filterEditor.clearValues();
		}
		if (null != symbolPane) {
			symbolPane.markForRedraw();
		}
		if (null != filterPane) {
			filterPane.markForRedraw();
		}

	}

	private void setSldHasChangedTrue() {
		sldHasChanged = true;

		enableSave(ruleSelector.checkIfAllRulesComplete());

		enableCancel(true);
	}

	private void saveSld() {

		enableSave(false);
		enableCancel(false);

		GWT.log("Entering Saving of SLD " + currentSld.getName());
		
		synchronizeFilter();
		service.saveOrUpdate(currentSld, new AsyncCallback<StyledLayerDescriptorInfo>() {

			/** call-back for handling saveOrUpdate() success return **/
			public void onSuccess(StyledLayerDescriptorInfo sld) {
				GWT.log("Successfully saved SLD " + sld.getName());
			}

			public void onFailure(Throwable caught) {
				GWT.log("could not save SLD ", caught);
				SC.warn("De SLD kon niet bewaard worden. (Interne fout: " + caught.getMessage() + ")");
				enableSave(true);
				enableCancel(true);
			}
		});
	}

	// -------------------------------------------------------------------------
	// Private class SaveButton:
	// -------------------------------------------------------------------------

	/** Definition of the Save button. */
	private class SaveButton extends IButton implements com.smartgwt.client.widgets.events.ClickHandler {

		public SaveButton() {
			setIcon(WidgetLayout.iconSave);
			setShowDisabledIcon(false);
			setTitle("Bewaar SLD"); // TODO i18n
			// TODO: validate form first
			setDisabled(false);

			// TODO: setTitle(I18nProvider.getAttribute().btnSaveTitle());
			// TODO: setTooltip(I18nProvider.getAttribute().btnSaveTooltip());

			addClickHandler(this);
		}

		public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
			saveSld();
		}

	}

	// -------------------------------------------------------------------------
	// Private class CancelButton:
	// -------------------------------------------------------------------------

	/** Definition of the Cancel button. */
	private class CancelButton extends IButton implements com.smartgwt.client.widgets.events.ClickHandler {

		public CancelButton() {
			setIcon(WidgetLayout.iconCancel);
			setShowDisabledIcon(false);
			setTitle("Annuleer"); // TODO i18n
			setTooltip("Annuleer alle veranderingen sinds de laatste bewaaroperatie.");

			setDisabled(false);

			// TODO: setTitle(I18nProvider.getAttribute().btnSaveTitle());
			// TODO: setTooltip(I18nProvider.getAttribute().btnSaveTooltip());

			addClickHandler(this);
		}

		public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {

			if (null != refreshSldHandler) {
				refreshSldHandler.execute(currentSld.getName());
			}
		}

	}

	public void addRefreshHandler(RefreshSldHandler refreshSldHandler) {
		this.refreshSldHandler = refreshSldHandler;

	}

}
