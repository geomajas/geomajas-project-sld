<?xml version="1.0" encoding="ISO-8859-1"?>
<!--
  ~ This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
  ~
  ~ Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
  ~
  ~ The program is available in open source according to the Apache
  ~ License, Version 2.0. All contributions in this program are covered
  ~ by the Geomajas Contributors License Agreement. For full licensing
  ~ details, see LICENSE.txt in the project root.
  -->

<StyledLayerDescriptor version="1.0.0"
 xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd"
 xmlns="http://www.opengis.net/sld"
 xmlns:ogc="http://www.opengis.net/ogc"
 xmlns:xlink="http://www.w3.org/1999/xlink"
 xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">

  <NamedLayer>
    <Name>dibe_orgels</Name>
    <UserStyle>
      <Title>Orgels</Title>

      <FeatureTypeStyle>
        <Rule>
          <Name>relict</Name>
          <Title>Relicten</Title>
          <MinScaleDenominator>0</MinScaleDenominator>
          <MaxScaleDenominator>50000</MaxScaleDenominator>
         
          <PointSymbolizer>
            <Graphic>
              <Mark>
                <WellKnownName>circle</WellKnownName>
                <Fill>
                  <CssParameter name="fill">#ffce23</CssParameter>
                </Fill>
                <Stroke>
                  <CssParameter name="stroke">#000000</CssParameter>
                  <CssParameter name="stroke-width">0.5</CssParameter>
                </Stroke>
              </Mark>
              <Size>8</Size>
            </Graphic>
          </PointSymbolizer>
        </Rule>
      </FeatureTypeStyle>


    </UserStyle>
  </NamedLayer>
</StyledLayerDescriptor>

