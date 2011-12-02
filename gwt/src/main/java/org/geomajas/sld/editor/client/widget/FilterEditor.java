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

import org.geomajas.sld.expression.LiteralTypeInfo;
import org.geomajas.sld.expression.PropertyNameInfo;
import org.geomajas.sld.expression.ExpressionInfo;
import org.geomajas.sld.filter.BinaryComparisonOpTypeInfo;
import org.geomajas.sld.filter.ComparisonOpsTypeInfo;
import org.geomajas.sld.filter.FilterTypeInfo;
import org.geomajas.sld.filter.LogicOpsTypeInfo;
import org.geomajas.sld.filter.LowerBoundaryTypeInfo;
import org.geomajas.sld.filter.PropertyIsBetweenTypeInfo;
import org.geomajas.sld.filter.PropertyIsEqualToInfo;
import org.geomajas.sld.filter.PropertyIsGreaterThanInfo;
import org.geomajas.sld.filter.PropertyIsGreaterThanOrEqualToInfo;
import org.geomajas.sld.filter.PropertyIsLessThanInfo;
import org.geomajas.sld.filter.PropertyIsLessThanOrEqualToInfo;
import org.geomajas.sld.filter.PropertyIsLikeTypeInfo;
import org.geomajas.sld.filter.PropertyIsNotEqualToInfo;
import org.geomajas.sld.filter.PropertyIsNullTypeInfo;
import org.geomajas.sld.filter.UnaryLogicOpTypeInfo;
import org.geomajas.sld.filter.UpperBoundaryTypeInfo;

import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.StaticTextItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * Widget for displaying/editing a rule filter of an SLD style rule.
 * 
 * @author An Buyle
 * 
 */
public class FilterEditor {

	/** private members for filter form **/
	private static final String DEFAULT_WILD_CARD = "*";

	private static final String DEFAULT_WILD_CARD_SINGLE_CHAR = "?";

	private static final String DEFAULT_ESCAPE = "\\";

	private DynamicForm filterForm;

	private boolean isFilterFormFirstSetup = true;

	private TextItem filterAttributeValue;

	private SelectItem filterOperatorSelect;

	private TextItem filterAttributeName;

	private TextItem filterAttributeLowerBoundaryValue;

	private TextItem filterAttributeUpperBoundaryValue;

	// private TextAreaItem likeFilterSpec;

	private FilterTypeInfo currentFilterInfo;

	private boolean isSupportedFilter;

	private StaticTextItem likeFilterSpec;

	private String currentPatternMatchingWildCard;

	private String currentPatternMatchingSingleChar;

	private String currentPatternMatchingEscape;

	private FilterHasChangedHandler filterHasChangedHandler;

	private Label filterIsNotSupportedMessage;

	private VLayout filterDetailContainer;

	public FilterEditor() {
		filterDetailContainer = new VLayout();
		filterForm = new DynamicForm();
	}

	public Canvas getForm() {
		return filterDetailContainer;
	}

	public boolean createOrUpdateFilterForm(FilterTypeInfo filterTypeInfo) {
		isSupportedFilter = true;

		currentFilterInfo = filterTypeInfo;
		if (null == currentFilterInfo) {
			currentFilterInfo = new FilterTypeInfo();
		}

		if (isFilterFormFirstSetup) {
			setupFilterForm();
		} else {
			filterIsNotSupportedMessage.hide();
			filterForm.clearValues();
			filterForm.hideItem("attributeLowerBoundaryValue");
			filterForm.hideItem("attributeUpperBoundaryValue");
		}

		filterForm.show();
		filterForm.showItem("attributeValue");
		filterAttributeValue.setDisabled(false);
		filterOperatorSelect.setDisabled(false);
		filterForm.hideItem("likeFilterSpec");
		// filterAttributeValue.setHint(null);

		if (filterTypeInfo.ifComparisonOps()) {

			ComparisonOpsTypeInfo op = filterTypeInfo.getComparisonOps();

			if (op.getClass().equals(PropertyIsLikeTypeInfo.class)) {
				String propertyName = null;
				String propertyValue = null;

				filterOperatorSelect.setValue("PropertyIsLikeTypeInfo");
				propertyName = ((PropertyIsLikeTypeInfo) op).getPropertyName().getValue();
				propertyValue = ((PropertyIsLikeTypeInfo) op).getLiteral().getValue();
				filterAttributeName.setValue(propertyName);
				filterAttributeValue.setValue(propertyValue);
				filterForm.showItem("likeFilterSpec");
				setLikeFilterSpec(((PropertyIsLikeTypeInfo) op).getWildCard(),
						((PropertyIsLikeTypeInfo) op).getSingleChar(), ((PropertyIsLikeTypeInfo) op).getEscape());

			} else if (op.getClass().equals(PropertyIsEqualToInfo.class)) {
				filterOperatorSelect.setValue("PropertyIsEqualToInfo");
				processBinaryComparisonOp((BinaryComparisonOpTypeInfo) op);

			} else if (op.getClass().equals(PropertyIsNotEqualToInfo.class)) {
				filterOperatorSelect.setValue("PropertyIsNotEqualToInfo");
				processBinaryComparisonOp((BinaryComparisonOpTypeInfo) op);

			} else if (op.getClass().equals(PropertyIsGreaterThanInfo.class)) {
				filterOperatorSelect.setValue("PropertyIsGreaterThanInfo");
				processBinaryComparisonOp((BinaryComparisonOpTypeInfo) op);

			} else if (op.getClass().equals(PropertyIsGreaterThanOrEqualToInfo.class)) {
				filterOperatorSelect.setValue("PropertyIsGreaterThanOrEqualToInfo");
				processBinaryComparisonOp((BinaryComparisonOpTypeInfo) op);

			} else if (op.getClass().equals(PropertyIsLessThanInfo.class)) {
				filterOperatorSelect.setValue("PropertyIsLessThanInfo");
				processBinaryComparisonOp((BinaryComparisonOpTypeInfo) op);

			} else if (op.getClass().equals(PropertyIsLessThanOrEqualToInfo.class)) {
				filterOperatorSelect.setValue("PropertyIsLessThanOrEqualToInfo");
				processBinaryComparisonOp((BinaryComparisonOpTypeInfo) op);

			} else if (op.getClass().equals(PropertyIsBetweenTypeInfo.class)) {
				filterOperatorSelect.setValue("PropertyIsBetweenTypeInfo");

				ExpressionInfo expressionInfo = ((PropertyIsBetweenTypeInfo) op).getExpression();

				if (expressionInfo.getClass().equals(PropertyNameInfo.class)) {
					filterAttributeName.setValue(expressionInfo.getValue());
				} else {
					SC.warn("Het filter voor deze regel wordt niet ondersteund en kan dus niet getoond worden.");
					filterForm.hide();
					isSupportedFilter = false;
					return isSupportedFilter; /* ABORT */
				}

				filterForm.hideItem("attributeValue");
				filterForm.showItem("attributeLowerBoundaryValue");
				filterForm.showItem("attributeUpperBoundaryValue");

				filterAttributeLowerBoundaryValue.setDisabled(false);
				filterAttributeUpperBoundaryValue.setDisabled(false);

				ExpressionInfo lowerBoundaryExpression = ((PropertyIsBetweenTypeInfo) op).getLowerBoundary()
						.getExpression();
				filterAttributeLowerBoundaryValue.setValue(lowerBoundaryExpression.getValue());

				ExpressionInfo upperBoundaryExpression = ((PropertyIsBetweenTypeInfo) op).getUpperBoundary()
						.getExpression();
				filterAttributeUpperBoundaryValue.setValue(upperBoundaryExpression.getValue());

			} else if (op.getClass().equals(PropertyIsNullTypeInfo.class)) {
				filterOperatorSelect.setValue("PropertyIsNullTypeInfo");
				filterAttributeName.setValue(((PropertyIsNullTypeInfo) op).getPropertyName().getValue());
				filterAttributeValue.setDisabled(true);
			} else {
				SC.warn("Het filter voor deze regel wordt niet ondersteund en kan dus niet getoond worden.");
				filterForm.hide();
				isSupportedFilter = false;
				return isSupportedFilter; /* ABORT */
			}
		} else if (currentFilterInfo.ifLogicOps()) {
			LogicOpsTypeInfo op = filterTypeInfo.getLogicOps();
			/** Only the NOT operator is (partially) supported **/
			if (op.getClass().equals(UnaryLogicOpTypeInfo.class)) {
				UnaryLogicOpTypeInfo info = (UnaryLogicOpTypeInfo) op;
				if (info.ifComparisonOps()) {
					ComparisonOpsTypeInfo innerOp = info.getComparisonOps();

					if (innerOp.getClass().equals(PropertyIsNullTypeInfo.class)) {
						filterOperatorSelect.setValue("PropertyIsNotNullTypeInfo");
						filterAttributeName.setValue(((PropertyIsNullTypeInfo) info.getComparisonOps())
								.getPropertyName().getValue());
						filterAttributeValue.setDisabled(true);
					} else if (innerOp.getClass().equals(PropertyIsLikeTypeInfo.class)) {
						String propertyName = null;
						String propertyValue = null;

						filterOperatorSelect.setValue("PropertyIsNotLikeTypeInfo");
						propertyName = ((PropertyIsLikeTypeInfo) innerOp).getPropertyName().getValue();
						propertyValue = ((PropertyIsLikeTypeInfo) innerOp).getLiteral().getValue();
						filterAttributeName.setValue(propertyName);
						filterAttributeValue.setValue(propertyValue);
						filterForm.showItem("likeFilterSpec");
						setLikeFilterSpec(((PropertyIsLikeTypeInfo) innerOp).getWildCard(),
								((PropertyIsLikeTypeInfo) innerOp).getSingleChar(),
								((PropertyIsLikeTypeInfo) innerOp).getEscape());

					} else if (innerOp.getClass().equals(PropertyIsBetweenTypeInfo.class)) {
						ExpressionInfo expressionInfo = ((PropertyIsBetweenTypeInfo) innerOp).getExpression();

						if (expressionInfo.getClass().equals(PropertyNameInfo.class)) {
							filterAttributeName.setValue(expressionInfo.getValue());
						} else {
							SC.warn(
								"Het filter voor deze regel wordt niet ondersteund en kan dus niet getoond worden.");
							filterForm.hide();
							isSupportedFilter = false;
							return isSupportedFilter; /* ABORT */
						}
						filterOperatorSelect.setValue("PropertyIsNotBetweenTypeInfo");
						filterForm.hideItem("attributeValue");
						filterForm.showItem("attributeLowerBoundaryValue");
						filterForm.showItem("attributeUpperBoundaryValue");
						filterAttributeLowerBoundaryValue.setDisabled(false);
						filterAttributeUpperBoundaryValue.setDisabled(false);

						ExpressionInfo lowerBoundaryExpression = ((PropertyIsBetweenTypeInfo) innerOp)
								.getLowerBoundary().getExpression();
						filterAttributeLowerBoundaryValue.setValue(lowerBoundaryExpression.getValue());

						ExpressionInfo upperBoundaryExpression = ((PropertyIsBetweenTypeInfo) innerOp)
								.getUpperBoundary().getExpression();
						filterAttributeUpperBoundaryValue.setValue(upperBoundaryExpression.getValue());
					}
				}

			} else {
				SC.warn("Een filter met een logische operatie verschillend van NOT wordt niet ondersteund en "
						+ "wordt dus niet getoond.");
				filterForm.hide();
				isSupportedFilter = false;
				setFilterIsNotSupported();
				return isSupportedFilter; /* ABORT */
			}
		}

		attemptConvertFormToFilter(); /* should normally succeed, unless unsupported filter */
		filterForm.markForRedraw();
		return isSupportedFilter;
	}

	private void setFilterIsNotSupported() {

		filterIsNotSupportedMessage.show();

	}

	public void createOrUpdateFilterForm(IncompleteFilterInfo incompleteFilter) {
		isSupportedFilter = true;
		currentFilterInfo = new FilterTypeInfo();

		if (isFilterFormFirstSetup) {
			setupFilterForm();
		} else {
			filterForm.clearValues();
			filterForm.hideItem("attributeLowerBoundaryValue");
			filterForm.hideItem("attributeUpperBoundaryValue");
		}
		filterIsNotSupportedMessage.hide();

		filterForm.showItem("attributeValue");
		filterAttributeValue.setDisabled(true);
		filterOperatorSelect.setDisabled(true);
		filterForm.hideItem("likeFilterSpec");

		if (null != incompleteFilter.getAttributeName()) {
			filterAttributeName.setValue(incompleteFilter.getAttributeName());
			filterOperatorSelect.setDisabled(false);
		}
		if (null != incompleteFilter.getOperator()) {
			String operator = incompleteFilter.getOperator();
			filterOperatorSelect.setValue(operator);

			filterForm.showItem("attributeValue");

			if ("PropertyIsNullTypeInfo".equals(operator) || "PropertyIsNotNullTypeInfo".equals(operator)) {
				filterAttributeValue.setDisabled(true); /* unary operator ! */
			} else if ("PropertyIsLikeTypeInfo".equals("PropertyIsLikeTypeInfo")) {
				setLikeFilterSpec(DEFAULT_WILD_CARD, DEFAULT_WILD_CARD_SINGLE_CHAR, DEFAULT_ESCAPE);
				filterForm.showItem("likeFilterSpec");
				filterAttributeValue.setDisabled(false);

			} else if ("PropertyIsBetweenTypeInfo".equals("PropertyIsBetweenTypeInfo")) {
				filterForm.hideItem("attributeValue");
				filterForm.showItem("attributeLowerBoundaryValue");
				filterForm.showItem("attributeUpperBoundaryValue");
				filterAttributeLowerBoundaryValue.setDisabled(false);
				filterAttributeUpperBoundaryValue.setDisabled(false);
			} else {
				filterAttributeValue.setDisabled(false);
			}

		}

		filterForm.show();
	}

	public void setFilterHasChangedHandler(FilterHasChangedHandler filterHasChangedHandler) {
		this.filterHasChangedHandler = filterHasChangedHandler;

	}

	// public void setUpdateFilterHandler(UpdateFilterHandler updateFilterHandler) {
	//
	// }

	private void setupFilterForm() {

		filterIsNotSupportedMessage = new Label(
				"Filter in de SLD wordt niet ondersteund, maar blijft wel in de SLD behouden.");
		filterDetailContainer.addMember(filterIsNotSupportedMessage);
		filterIsNotSupportedMessage.hide();

		filterAttributeName = new TextItem("attributeName", "Attribuut");
		filterAttributeName.setWidth(300);

		filterOperatorSelect = new SelectItem();
		filterOperatorSelect.setName("operator");
		filterOperatorSelect.setTitle("Operator");
		filterOperatorSelect.setWidth(300);
		// filterOperatorSelect.setDisabled(true);
		LinkedHashMap<String, String> operatorMap = new LinkedHashMap<String, String>();
		operatorMap.put("PropertyIsEqualToInfo", "="); // PropertyIsEqualTo
		operatorMap.put("PropertyIsNotEqualToInfo", "<>"); // PropertyIsNotEqualTo
		operatorMap.put("PropertyIsLessThanInfo", "<"); // PropertyIsLessThan
		operatorMap.put("PropertyIsLessThanOrEqualToInfo", "<="); // PropertyIsLessThanOrEqualTo
		operatorMap.put("PropertyIsGreaterThanInfo", ">"); // PropertyIsGreaterThan
		operatorMap.put("PropertyIsGreaterThanOrEqualToInfo", ">="); // PropertyIsGreaterThanOrEqualTo

		operatorMap.put("PropertyIsBetweenTypeInfo", "ligt tussen"); // PropertyIsBetween
		operatorMap.put("PropertyIsNotBetweenTypeInfo", "ligt niet tussen"); // extension for NOT(PropertyIsBetween())

		operatorMap.put("PropertyIsLikeTypeInfo", "voldaat aan patroon (string)"); // PropertyIsLike
		operatorMap.put("PropertyIsNotLikeTypeInfo", "voldaat niet aan patroon (string)"); // extension for
																							// NOT(PropertyIsLike())

		operatorMap.put("PropertyIsNullTypeInfo", "is null"); // PropertyIsNull
		operatorMap.put("PropertyIsNotNullTypeInfo", "is niet null"); // extension for NOT(PropertyIsNull())

		filterOperatorSelect.setValueMap(operatorMap);
		filterOperatorSelect.setDisabled(true); /* The attribute name must be filled in first */

		filterAttributeValue = new TextItem("attributeValue", "Waarde");
		filterAttributeValue.setWidth(300);
		/* The operator field value must be non-null before enabling the value fields */
		filterAttributeValue.setDisabled(true);

		filterAttributeLowerBoundaryValue = new TextItem("attributeLowerBoundaryValue", "Ondergrens");
		filterAttributeUpperBoundaryValue = new TextItem("attributeUpperBoundaryValue", "Bovengrens");
		/* The operator field value must be non-null before enabling the value fields */
		filterAttributeLowerBoundaryValue.setDisabled(true);
		filterAttributeUpperBoundaryValue.setDisabled(true);

		likeFilterSpec = new StaticTextItem("likeFilterSpec"); // com.smartgwt.client.widgets.HTMLPane();

		likeFilterSpec.setTitle("Filter notaties");
		// likeFilterSpec.setCellStyle("formCell"); /* TODO */

		likeFilterSpec.setWidth(300);

		filterAttributeName.addChangedHandler(new ChangedHandler() {

			public void onChanged(ChangedEvent event) {

				if (null != event.getValue()) {
					filterAttributeValue.clearValue();
					filterOperatorSelect.setDisabled(false);
				} else {
					filterOperatorSelect.clearValue();
					filterOperatorSelect.setDisabled(true); /* The attribute name must be filled in first */
					filterAttributeValue.clearValue();
					filterAttributeValue.setDisabled(true); /* The attribute name must be filled in first */
					filterAttributeLowerBoundaryValue.setDisabled(true);
					filterAttributeUpperBoundaryValue.setDisabled(true);
					filterAttributeLowerBoundaryValue.clearValue();
					filterAttributeUpperBoundaryValue.clearValue();

				}
				ChoiceFilterInfo choiceFilterInfo = attemptConvertFormToFilter(); /*
																				 * try, may fail if some fields haven't
																				 * been filled in yet
																				 */
				filterHasChangedCaller(choiceFilterInfo.ifFilter());
			}

		});

		filterOperatorSelect.addChangedHandler(new ChangedHandler() {

			public void onChanged(ChangedEvent event) {
				String operator = event.getValue().toString();
				if (null != operator) {
					/** note: filterOperatorSelect can only be changed when filterAttributeName value is non-null **/
					filterForm.showItem("attributeValue");

					filterForm.hideItem("likeFilterSpec");
					filterForm.hideItem("attributeLowerBoundaryValue");
					filterForm.hideItem("attributeUpperBoundaryValue");

					if ("PropertyIsNullTypeInfo".equals(operator) || "PropertyIsNotNullTypeInfo".equals(operator)) {
						filterAttributeValue.clearValue();
						filterAttributeValue.setDisabled(true); /* unary operator ! */
					} else if ("PropertyIsLikeTypeInfo".equals(operator)) {
						setLikeFilterSpec(DEFAULT_WILD_CARD, DEFAULT_WILD_CARD_SINGLE_CHAR, DEFAULT_ESCAPE);
						filterForm.showItem("likeFilterSpec");
						filterAttributeValue.setDisabled(false);

					} else if ("PropertyIsBetweenTypeInfo".equals(operator)
							|| "PropertyIsNotBetweenTypeInfo".equals(operator)) {
						filterForm.hideItem("attributeValue");
						filterForm.showItem("attributeLowerBoundaryValue");
						filterForm.showItem("attributeUpperBoundaryValue");
						filterAttributeLowerBoundaryValue.setDisabled(false);
						filterAttributeUpperBoundaryValue.setDisabled(false);
					} else {
						/* Assume Binary comparisson */
						filterAttributeValue.setDisabled(false);
					}
					ChoiceFilterInfo choiceFilterInfo = attemptConvertFormToFilter(); /*
																					 * try, may fail if some fields
																					 * haven't been filled in yet
																					 */
					filterHasChangedCaller(choiceFilterInfo.ifFilter());

				} else {
					/* The operator field value must be non-null before enabling the value fields */
					filterHasChangedCaller(false);
					filterAttributeValue.setDisabled(true);
					filterAttributeLowerBoundaryValue.setDisabled(true);
					filterAttributeUpperBoundaryValue.setDisabled(false);
				}
			}
		});

		filterAttributeValue.addChangedHandler(new ChangedHandler() {

			public void onChanged(ChangedEvent event) {
				ChoiceFilterInfo choiceFilterInfo = attemptConvertFormToFilter(); /*
																				 * try, may fail if some fields haven't
																				 * been filled in yet
																				 */
				filterHasChangedCaller(choiceFilterInfo.ifFilter());
			}
		});

		filterAttributeLowerBoundaryValue.addChangedHandler(new ChangedHandler() {

			public void onChanged(ChangedEvent event) {
				ChoiceFilterInfo choiceFilterInfo = attemptConvertFormToFilter(); /*
																				 * try, may fail if some fields haven't
																				 * been filled in yet
																				 */
				filterHasChangedCaller(choiceFilterInfo.ifFilter());
			}
		});

		filterAttributeUpperBoundaryValue.addChangedHandler(new ChangedHandler() {

			public void onChanged(ChangedEvent event) {
				ChoiceFilterInfo choiceFilterInfo = attemptConvertFormToFilter(); /*
																				 * try, may fail if some fields haven't
																				 * been filled in yet
																				 */
				filterHasChangedCaller(choiceFilterInfo.ifFilter());
			}
		});

		filterForm.setItems(filterAttributeName, filterOperatorSelect, filterAttributeValue, likeFilterSpec,
				filterAttributeLowerBoundaryValue, filterAttributeUpperBoundaryValue);

		filterForm.hideItem("likeFilterSpec");

		filterForm.hideItem("attributeLowerBoundaryValue");
		filterForm.hideItem("attributeUpperBoundaryValue");

		filterDetailContainer.addMember(filterForm);
		isFilterFormFirstSetup = false;
	}

	private void filterHasChangedCaller(boolean isComplete) {
		if (null != filterHasChangedHandler) {
			filterHasChangedHandler.execute(isComplete);
		}
	}

	public ChoiceFilterInfo attemptConvertFormToFilter() {
		if (!isSupportedFilter) {
			return null;
		}

		boolean couldConvert = false;
		FilterTypeInfo retFilterInfo = currentFilterInfo;

		String operator = filterOperatorSelect.getValueAsString();
		if (null != operator) {
			if ("PropertyIsNullTypeInfo".equals(operator)) {
				couldConvert = convertFormToNullFilter();
			} else if ("PropertyIsNotNullTypeInfo".equals(operator)) {
				couldConvert = convertFormToNotNullFilter();
			} else if ("PropertyIsBetweenTypeInfo".equals(operator)) {
				couldConvert = attemptConvertToBetweenFilter();
			} else if ("PropertyIsNotBetweenTypeInfo".equals(operator)) {
				couldConvert = attemptConvertFormToNotBetweenFilter();
			} else if ("PropertyIsNotLikeTypeInfo".equals(operator)) {
				couldConvert = attemptConvertFormToNotLikeFilter();
			} else {
				filterAttributeValue.setDisabled(false);
				/*
				 * Try, may do nothing if value field hasn't been filled in yet
				 */
				couldConvert = attemptConvertFormToBinaryComparisonFilter();
			}
		}

		if (!couldConvert) {
			if (null == operator && null == filterAttributeName.getValue()) {
				couldConvert = true; /* Empty filter is specified */
				// updateFilterHandler.execute(null);
				retFilterInfo = null;
			}
		}
		ChoiceFilterInfo choiceFilterInfo = new ChoiceFilterInfo();
		if (couldConvert) {
			choiceFilterInfo.setFilter(retFilterInfo);

		} else {
			choiceFilterInfo.setIncompleteFilter(getIncompleteFilterInfo());

		}
		return choiceFilterInfo;
	}

	private boolean convertFormToNullFilter() {

		PropertyIsNullTypeInfo propertyIsNullTypeInfo = new PropertyIsNullTypeInfo();
		// Assume filterAttributeName form field value not null (must be selected first)
		propertyIsNullTypeInfo.setPropertyName(new PropertyNameInfo(filterAttributeName.getValueAsString()));

		currentFilterInfo.clearChoiceSelect();
		currentFilterInfo.setComparisonOps(propertyIsNullTypeInfo);
		return true;
	}

	/**
	 * @return true if filter object could be created/updated completely, else false
	 */

	private boolean convertFormToNotNullFilter() {

		UnaryLogicOpTypeInfo logicOps = new UnaryLogicOpTypeInfo(); /* create a NOT filter */
		PropertyIsNullTypeInfo propertyIsNullTypeInfo = new PropertyIsNullTypeInfo();

		// Assume filterAttributeName form field value not null (must be selected first)
		propertyIsNullTypeInfo.setPropertyName(new PropertyNameInfo(filterAttributeName.getValueAsString()));

		logicOps.setComparisonOps(propertyIsNullTypeInfo);
		currentFilterInfo.clearChoiceSelect();
		currentFilterInfo.setLogicOps(logicOps);

		return true;
	}

	private boolean attemptConvertToBetweenFilter() {

		PropertyIsBetweenTypeInfo info = new PropertyIsBetweenTypeInfo();

		if (null == filterAttributeName.getValueAsString()) {
			return false; /* ABORT */
		}
		info.setExpression(new PropertyNameInfo(filterAttributeName.getValueAsString()));

		if (null == filterAttributeLowerBoundaryValue.getValueAsString()) {
			return false; /* ABORT */
		}
		LowerBoundaryTypeInfo lowerBoundary = new LowerBoundaryTypeInfo();
		lowerBoundary.setExpression(new LiteralTypeInfo(filterAttributeLowerBoundaryValue.getValueAsString()));

		info.setLowerBoundary(lowerBoundary);

		if (null == filterAttributeUpperBoundaryValue.getValueAsString()) {
			return false; /* ABORT */
		}

		UpperBoundaryTypeInfo upperBoundary = new UpperBoundaryTypeInfo();
		upperBoundary.setExpression(new LiteralTypeInfo(filterAttributeUpperBoundaryValue.getValueAsString()));

		info.setUpperBoundary(upperBoundary);

		currentFilterInfo.clearChoiceSelect();
		currentFilterInfo.setComparisonOps(info);
		return true;
	}

	private boolean attemptConvertFormToNotBetweenFilter() {

		// Normally filterAttributeName form field value will not be null (must be selected first)
		if (null == filterAttributeName.getValueAsString()) {
			return false; /* ABORT */
		}

		UnaryLogicOpTypeInfo logicOps = new UnaryLogicOpTypeInfo(); /* create a NOT filter */

		PropertyIsBetweenTypeInfo info = new PropertyIsBetweenTypeInfo();

		info.setExpression(new PropertyNameInfo(filterAttributeName.getValueAsString()));

		if (null == filterAttributeLowerBoundaryValue.getValueAsString()) {
			return false; /* ABORT */
		}
		LowerBoundaryTypeInfo lowerBoundary = new LowerBoundaryTypeInfo();
		lowerBoundary.setExpression(new LiteralTypeInfo(filterAttributeLowerBoundaryValue.getValueAsString()));

		info.setLowerBoundary(lowerBoundary);

		if (null == filterAttributeUpperBoundaryValue.getValueAsString()) {
			return false; /* ABORT */
		}

		UpperBoundaryTypeInfo upperBoundary = new UpperBoundaryTypeInfo();
		upperBoundary.setExpression(new LiteralTypeInfo(filterAttributeUpperBoundaryValue.getValueAsString()));

		info.setUpperBoundary(upperBoundary);

		logicOps.setComparisonOps(info);
		currentFilterInfo.clearChoiceSelect();
		currentFilterInfo.setLogicOps(logicOps);

		return true;
	}

	/**
	 * @return true if filter object could be created/updated completely, else false
	 */
	private boolean attemptConvertFormToNotLikeFilter() {

		// Normally filterAttributeName form field value will not be null (must be selected first)
		if (null == filterAttributeName.getValueAsString()) {
			return false; /* ABORT */
		}
		if (null == filterAttributeValue.getValueAsString()) {
			return false; /* ABORT */
		}

		UnaryLogicOpTypeInfo logicOps = new UnaryLogicOpTypeInfo(); /* create a NOT filter */

		PropertyIsLikeTypeInfo info = new PropertyIsLikeTypeInfo();

		PropertyNameInfo propertyName = new PropertyNameInfo(filterAttributeName.getValueAsString());
		info.setPropertyName(propertyName);

		LiteralTypeInfo literal = new LiteralTypeInfo(filterAttributeValue.getValueAsString());
		info.setLiteral(literal);

		info.setWildCard(currentPatternMatchingWildCard);
		info.setSingleChar(currentPatternMatchingSingleChar);
		info.setEscape(currentPatternMatchingEscape);

		logicOps.setComparisonOps(info);
		currentFilterInfo.clearChoiceSelect();
		currentFilterInfo.setLogicOps(logicOps);

		return true;
	}

	/**
	 * @return true if filter object could be created/updated completely, else false
	 */
	private boolean attemptConvertFormToBinaryComparisonFilter() {

		boolean retValue = true;

		if (null == filterAttributeName.getValueAsString()) {
			return false; /* ABORT: too early to convert * */
		}

		String operator = filterOperatorSelect.getValueAsString();
		/* operator must be none-null! */
		if (null == operator) {
			return false; /* too early to convert * */
		}
		if (null == filterAttributeValue.getValueAsString()) {
			return false; /* too early to convert * */
		}

		if ("PropertyIsLikeTypeInfo".equals(operator)) {
			/** Like operator **/

			PropertyIsLikeTypeInfo info = new PropertyIsLikeTypeInfo();

			PropertyNameInfo propertyName = new PropertyNameInfo(filterAttributeName.getValueAsString());
			info.setPropertyName(propertyName);

			LiteralTypeInfo literal = new LiteralTypeInfo(filterAttributeValue.getValueAsString());
			info.setLiteral(literal);

			info.setWildCard(currentPatternMatchingWildCard);
			info.setSingleChar(currentPatternMatchingSingleChar);
			info.setEscape(currentPatternMatchingEscape);
			currentFilterInfo.clearChoiceSelect();
			currentFilterInfo.setComparisonOps(info);

		} else if ("PropertyIsEqualToInfo".equals(operator)) {
			/** == operator **/
			PropertyIsEqualToInfo info = new PropertyIsEqualToInfo();
			setBinaryComparisonOp((BinaryComparisonOpTypeInfo) info);

		} else if ("PropertyIsNotEqualToInfo".equals(operator)) {
			/** <> operator **/
			PropertyIsNotEqualToInfo info = new PropertyIsNotEqualToInfo();
			setBinaryComparisonOp((BinaryComparisonOpTypeInfo) info);

		} else if ("PropertyIsGreaterThanInfo".equals(operator)) {
			PropertyIsGreaterThanInfo info = new PropertyIsGreaterThanInfo();
			setBinaryComparisonOp((BinaryComparisonOpTypeInfo) info);

		} else if ("PropertyIsGreaterThanOrEqualToInfo".equals(operator)) {
			PropertyIsGreaterThanOrEqualToInfo info = new PropertyIsGreaterThanOrEqualToInfo();
			setBinaryComparisonOp((BinaryComparisonOpTypeInfo) info);

		} else if ("PropertyIsLessThanInfo".equals(operator)) {
			PropertyIsLessThanInfo info = new PropertyIsLessThanInfo();
			setBinaryComparisonOp((BinaryComparisonOpTypeInfo) info);

		} else if ("PropertyIsLessThanOrEqualToInfo".equals(operator)) {
			PropertyIsLessThanOrEqualToInfo info = new PropertyIsLessThanOrEqualToInfo();
			setBinaryComparisonOp((BinaryComparisonOpTypeInfo) info);

		} else {
			// TODO
			retValue = false;
		}
		return retValue;
	}

	private void setBinaryComparisonOp(BinaryComparisonOpTypeInfo op) {

		List<ExpressionInfo> expList = new ArrayList<ExpressionInfo>(2);

		expList.add(new PropertyNameInfo(filterAttributeName.getValueAsString()));
		expList.add(new LiteralTypeInfo(filterAttributeValue.getValueAsString()));

		op.setExpressionList(expList);
		currentFilterInfo.clearChoiceSelect();
		currentFilterInfo.setComparisonOps(op);
	}

	private void setLikeFilterSpec(String wildCard, String singleChar, String escape) {
		currentPatternMatchingWildCard = wildCard;
		currentPatternMatchingSingleChar = singleChar;
		currentPatternMatchingEscape = escape;

		likeFilterSpec.setValue("<table style='font-size:11px;' border='1' cellpadding='10px'>"
				+ "<tr><td>Willekeurige reeks tekens (wildcard):</td>" + "<td>" + wildCard + "</td></tr>"
				+ "<tr><td>E" + "\u00E9" + "n Willekeurig karakter:</td>" + "<td>" + singleChar + "</td>"
				+ "<tr><td>Escape-karakter</td>" + "<td>" + escape + "</td>" + "</tr></table>");
	}

	private void processBinaryComparisonOp(BinaryComparisonOpTypeInfo op) {

		for (ExpressionInfo expressionInfo : op.getExpressionList()) {

			if (expressionInfo.getClass().equals(PropertyNameInfo.class)) {
				filterAttributeName.setValue(expressionInfo.getValue());
			} else if (expressionInfo.getClass().equals(LiteralTypeInfo.class)) {
				filterAttributeValue.setDisabled(false);
				filterAttributeValue.setValue(expressionInfo.getValue());
			}
		}
	}

	public IncompleteFilterInfo getIncompleteFilterInfo() {

		IncompleteFilterInfo incompleteFilterInfo = new IncompleteFilterInfo(filterAttributeName.getValueAsString(),
				filterOperatorSelect.getValueAsString());
		incompleteFilterInfo.setPatternMatchingWildCard(currentPatternMatchingWildCard);
		incompleteFilterInfo.setPatternMatchingSingleChar(currentPatternMatchingSingleChar);
		incompleteFilterInfo.setPatternMatchingEscape(currentPatternMatchingEscape);
		return incompleteFilterInfo;

	}

	public void clearValues() {
		currentPatternMatchingWildCard = null;
		currentPatternMatchingSingleChar = null;
		currentPatternMatchingEscape = null;
		filterForm.clearValues();
	}

}
