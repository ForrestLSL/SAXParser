package com.lsl.utils;



import com.lsl.model.DistrictModel;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;


public class DistrictXmlParserHandler extends DefaultHandler {

	public DistrictXmlParserHandler(String cityName) {
		this.cityName = cityName ;
	}

	@Override
	public void startDocument() throws SAXException {
	}
	private boolean FLAG = false ;
	private String cityName = "" ;
	private List<DistrictModel> districtModels = new ArrayList<DistrictModel>() ;
	DistrictModel districtModel = new DistrictModel();
	public List<DistrictModel> getDistrictModels()
	{
		return districtModels ;
	}
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		if (qName.equals("city")) {
			if(attributes.getValue(0).startsWith(cityName))
			{
				FLAG = true ;
			}
			else
			{
				FLAG = false ;
			}
		}
		else if (qName.equals("district")&&FLAG) 
		{
			districtModel = new DistrictModel();
			districtModel.setName(attributes.getValue(0));
			districtModel.setZipcode(attributes.getValue(1));
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		//
		if(qName.equals("district")&&FLAG)
		{
			districtModels.add(districtModel) ;
		}
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
	}

}