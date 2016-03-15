package com.lsl.utils;

import com.lsl.model.CityModel;
import com.lsl.model.DistrictModel;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DragonWang on 2016/3/14.
 */
public class CityXmlParserHandler extends DefaultHandler {

    public CityXmlParserHandler(String provinceName) {
        this.provinceName = provinceName ;
    }

    @Override
    public void startDocument() throws SAXException {
    }
    private boolean FLAG = false ;
    private String provinceName = "" ;
    private List<CityModel> citytModels = new ArrayList<CityModel>() ;
    CityModel citytModel = new CityModel();
    public List<CityModel> getCityModels()
    {
        return citytModels ;
    }
    @Override
    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {
        if (qName.equals("province")) {
            if(attributes.getValue(0).startsWith(provinceName))
            {
                FLAG = true ;
            }
            else
            {
                FLAG = false ;
            }
        }
        else if (qName.equals("city")&&FLAG)
        {
            citytModel = new CityModel();
            citytModel.setName(attributes.getValue(0));
//            citytModel.setZipcode(attributes.getValue(1));
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        //
        if(qName.equals("city")&&FLAG)
        {
            citytModels.add(citytModel) ;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {
    }
}
