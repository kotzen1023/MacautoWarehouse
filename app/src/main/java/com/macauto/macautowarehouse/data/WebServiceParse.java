package com.macauto.macautowarehouse.data;


import android.util.Log;
import android.util.Xml;


import com.macauto.macautowarehouse.table.DataRow;
import com.macauto.macautowarehouse.table.DataTable;

import org.ksoap2.serialization.SoapObject;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.StringWriter;


public class WebServiceParse {
    private static final String TAG = WebServiceParse.class.getName();

    public static DataTable parseXmlToDataTable(SoapObject soapObject) {

        Log.e(TAG, "=== parseXmlToDataTable start === ");

        DataTable dataTable = new DataTable();

        if (soapObject.getPropertyCount() == 2) { //data declare and real data,
            SoapObject property = (SoapObject) soapObject.getProperty(1);
            int count = property.getPropertyCount();

            if (count > 0) {
                SoapObject sub_property = (SoapObject) property.getProperty(0);

                Log.d(TAG, "object = "+sub_property);
                Log.d(TAG, "count = "+sub_property.getPropertyCount());

                /*dataTable.Columns.Add("check_sp");
                dataTable.Columns.Add("rvu01");
                dataTable.Columns.Add("rvu02");
                dataTable.Columns.Add("rvb05");
                dataTable.Columns.Add("pmn041");
                dataTable.Columns.Add("ima021");
                dataTable.Columns.Add("rvv32");
                dataTable.Columns.Add("rvv33");
                dataTable.Columns.Add("rvv34");
                dataTable.Columns.Add("rvb33");
                dataTable.Columns.Add("pmc03");
                dataTable.Columns.Add("gen02");*/

                for (int i=0; i < sub_property.getPropertyCount(); i++) {


                    //Object property = s_deals.getProperty(i);
                    //SoapObject category_list = (SoapObject) property;
                    Log.d(TAG, "object["+i+"] = "+sub_property.getProperty(i));

                    SoapObject min_property = (SoapObject) sub_property.getProperty(i);

                    //ArrayList detailArrayList = new ArrayList();
                    DataRow dataRow = dataTable.NewRow();


                    for (int j=0; j < min_property.getPropertyCount(); j++) {

                        Log.e(TAG, "sub_object = "+min_property.getProperty(j));

                        if (i == 0 || j == dataTable.Columns.size()) {
                            dataTable.Columns.Add(min_property.getPropertyInfo(j).toString().split(":")[0].trim());
                        }

                        dataRow.setValue(dataTable.Columns.get(j).ColumnName, min_property.getProperty(j).toString());
                        /*if (j==0) {
                            dataRow.setValue(dataTable.Columns.get(j).ColumnName, min_property.getProperty(j).toString());
                        } else if (j==1) {
                            dataRow.setValue("rvu01", min_property.getProperty(j).toString());
                        } else if (j==2) {
                            dataRow.setValue("rvu02", min_property.getProperty(j).toString());
                        } else if (j==3) {
                            dataRow.setValue("rvb05", min_property.getProperty(j).toString());
                        } else if (j==4) {
                            dataRow.setValue("pmn041", min_property.getProperty(j).toString());
                        } else if (j==5) {
                            dataRow.setValue("ima021", min_property.getProperty(j).toString());
                        } else if (j==6) {
                            dataRow.setValue("rvv32", min_property.getProperty(j).toString());
                        } else if (j==7) {
                            dataRow.setValue("rvv33", min_property.getProperty(j).toString());
                        } else if (j==8) {
                            dataRow.setValue("rvv34", min_property.getProperty(j).toString());
                        } else if (j==9) {
                            dataRow.setValue("rvb33", min_property.getProperty(j).toString());
                        } else if (j==10) {
                            dataRow.setValue("pmc03", min_property.getProperty(j).toString());
                        } else if (j==11) {
                            dataRow.setValue("gen02", min_property.getProperty(j).toString());
                        }*/

                    }



                    dataTable.Rows.add(dataRow);


                }

                Log.e(TAG, "================= Column name ==========================");
                for (int i=0; i<dataTable.Columns.size(); i++) {
                    System.out.print(dataTable.Columns.get(i).ColumnName);
                    if (i < dataTable.Columns.size() - 1) {
                        System.out.print(", ");
                    }
                }
                System.out.print("\n");
                Log.e(TAG, "========================================================");
                for (int i=0; i<dataTable.Rows.size(); i++) {

                    for (int j=0; j<dataTable.Columns.size(); j++) {
                        System.out.print(dataTable.Rows.get(i).getValue(j));
                        if (j < dataTable.Columns.size() - 1) {
                            System.out.print(", ");
                        }
                    }
                    System.out.print("\n");
                }
                Log.e(TAG, "========================================================");

            }
        } else {
            dataTable = null;
        }

        Log.e(TAG, "=== parseXmlToDataTable end === ");

        return dataTable;
    }

    public static String parseDataTableToXml(DataTable dataTable) {
        Log.e(TAG, "=== parseDataTableToXml start === ");

        StringWriter writer = new StringWriter();

        XmlSerializer xmlSerializer = Xml.newSerializer();

        String remove_start_xml_tag="";

        try {
            /*
            xmlSerializer.setOutput(writer);

            xmlSerializer.startDocument("UTF-8", true);

            xmlSerializer.startTag("", "printx");
            //declare
            //xs:schema
            xmlSerializer.startTag("", "xs:schema");
            xmlSerializer.attribute("", "id", "printx");
            xmlSerializer.attribute("", "xmlns:xs", "http://www.w3.org/2001/XMLSchema");
            xmlSerializer.attribute("", "xmlns:msdata", "urn:schemas-microsoft-com:xml-msdata");
            //xs:element
            xmlSerializer.startTag("", "xs:element");
            xmlSerializer.attribute("", "name", "printx");
            xmlSerializer.attribute("", "msdata:IsDataSet", "true");
            xmlSerializer.attribute("", "msdata:UseCurrentLocale", "true");
            //xs:complexType
            xmlSerializer.startTag("", "xs:complexType");
            //xs:choice
            xmlSerializer.startTag("", "xs:choice");
            xmlSerializer.attribute("", "minOccurs", "0");
            xmlSerializer.attribute("", "maxOccurs", "unbounded");
            //xs:element
            xmlSerializer.startTag("", "xs:element");
            if (dataTable.TableName != null && !dataTable.TableName.equals("")) {
                xmlSerializer.attribute("", "name", dataTable.TableName);
            } else {
                xmlSerializer.attribute("", "name", "Table");
            }

            //xs:complexType
            xmlSerializer.startTag("", "xs:complexType");
            //xs:sequence
            xmlSerializer.startTag("", "xs:sequence");
            //xs:element

            for (int i=0; i<dataTable.Columns.size(); i++) {
                xmlSerializer.startTag("", "xs:element");
                xmlSerializer.attribute("", "name", dataTable.Columns.get(i).ColumnName);

                if (dataTable.getValue(0, i) != null) {
                    if (dataTable.getValue(0, i).toString().equals("true") || dataTable.getValue(0, i).toString().equals("false")) {
                        xmlSerializer.attribute("", "type", "xs:boolean");
                    } else if (dataTable.Columns.get(i).ColumnName.equals("tc_obf013")) {
                        xmlSerializer.attribute("", "type", "xs:decimal");
                    } else if (dataTable.Columns.get(i).ColumnName.equals("inv_qty")) {
                        xmlSerializer.attribute("", "type", "xs:decimal");
                    } else if (dataTable.Columns.get(i).ColumnName.equals("sfa06")) {
                        xmlSerializer.attribute("", "type", "xs:decimal");
                    } else if (dataTable.Columns.get(i).ColumnName.equals("sfa063")) {
                        xmlSerializer.attribute("", "type", "xs:decimal");
                    } else if (dataTable.Columns.get(i).ColumnName.equals("sfa161")) {
                        xmlSerializer.attribute("", "type", "xs:decimal");
                    } else if (dataTable.Columns.get(i).ColumnName.equals("rvb33")) { //數量
                        xmlSerializer.attribute("", "type", "xs:decimal");
                    } else if (dataTable.Columns.get(i).ColumnName.equals("rvv02")) { //項次
                        xmlSerializer.attribute("", "type", "xs:integer");
                    }
                    else {
                        xmlSerializer.attribute("", "type", "xs:string");
                        xmlSerializer.attribute("", "minOccurs", "0");
                    }
                } else {
                    xmlSerializer.attribute("", "type", "xs:string");
                    xmlSerializer.attribute("", "minOccurs", "0");
                }



                xmlSerializer.endTag("", "xs:element");
            }

            xmlSerializer.endTag("", "xs:sequence");
            xmlSerializer.endTag("", "xs:complexType");
            xmlSerializer.endTag("", "xs:element");
            xmlSerializer.endTag("", "xs:choice");
            xmlSerializer.endTag("", "xs:complexType");
            xmlSerializer.endTag("", "xs:element");
            xmlSerializer.endTag("", "xs:schema");
            //end tag <schema>
            //table start
            for (int i = 0; i < dataTable.Rows.size(); i++) {
                xmlSerializer.startTag("", "Table");
                for (int j = 0; j < dataTable.Columns.size(); j++) {

                    xmlSerializer.startTag("", dataTable.Columns.get(j).ColumnName);
                    if (dataTable.getValue(i, j) != null) {
                        xmlSerializer.text(dataTable.getValue(i,j).toString());
                    } else {
                        xmlSerializer.text("null");
                    }

                    xmlSerializer.endTag("", dataTable.Columns.get(j).ColumnName);

                }
                //System.out.print("\n");
                xmlSerializer.endTag("", "Table");
            }
            //table end
            xmlSerializer.endTag("", "printx");
            xmlSerializer.endDocument();*/

            xmlSerializer.setOutput(writer);

            xmlSerializer.startDocument("UTF-8", true);

            //xmlSerializer.startTag("", "NewDataSet");
            //declare
            //xs:schema
            xmlSerializer.startTag("", "xs:schema");
            xmlSerializer.attribute("", "id", "NewDataSet");
            xmlSerializer.attribute("", "xmlns", "");
            xmlSerializer.attribute("", "xmlns:xs", "http://www.w3.org/2001/XMLSchema");
            xmlSerializer.attribute("", "xmlns:msdata", "urn:schemas-microsoft-com:xml-msdata");
            //xs:element
            xmlSerializer.startTag("", "xs:element");
            xmlSerializer.attribute("", "name", "NewDataSet");
            xmlSerializer.attribute("", "msdata:IsDataSet", "true");

            if (dataTable.TableName != null && !dataTable.TableName.equals("")) {
                xmlSerializer.attribute("", "msdata:MainDataTable", dataTable.TableName);
            } else {
                xmlSerializer.attribute("", "msdata:MainDataTable", "MainTable");
            }

            xmlSerializer.attribute("", "msdata:UseCurrentLocale", "true");
            //xs:complexType
            xmlSerializer.startTag("", "xs:complexType");
            //xs:choice
            xmlSerializer.startTag("", "xs:choice");
            xmlSerializer.attribute("", "minOccurs", "0");
            xmlSerializer.attribute("", "maxOccurs", "unbounded");
            //xs:element
            xmlSerializer.startTag("", "xs:element");
            if (dataTable.TableName != null && !dataTable.TableName.equals("")) {
                xmlSerializer.attribute("", "name", dataTable.TableName);
            } else {
                xmlSerializer.attribute("", "name", "MainTable");
            }

            //xs:complexType
            xmlSerializer.startTag("", "xs:complexType");
            //xs:sequence
            xmlSerializer.startTag("", "xs:sequence");
            //xs:element

            for (int i=0; i<dataTable.Columns.size(); i++) {
                xmlSerializer.startTag("", "xs:element");
                xmlSerializer.attribute("", "name", dataTable.Columns.get(i).ColumnName);

                if (dataTable.getValue(0, i) != null) {
                    if (dataTable.getValue(0, i).toString().equals("true") || dataTable.getValue(0, i).toString().equals("false")) {
                        xmlSerializer.attribute("", "type", "xs:boolean");
                    } else if (dataTable.Columns.get(i).ColumnName.equals("tc_obf013")) {
                        xmlSerializer.attribute("", "type", "xs:decimal");
                        xmlSerializer.attribute("", "minOccurs", "0");
                    } else if (dataTable.Columns.get(i).ColumnName.equals("inv_qty")) {
                        xmlSerializer.attribute("", "type", "xs:decimal");
                        xmlSerializer.attribute("", "minOccurs", "0");
                    } else if (dataTable.Columns.get(i).ColumnName.equals("sfa06")) {
                        xmlSerializer.attribute("", "type", "xs:decimal");
                        xmlSerializer.attribute("", "minOccurs", "0");
                    } else if (dataTable.Columns.get(i).ColumnName.equals("sfa063")) {
                        xmlSerializer.attribute("", "type", "xs:decimal");
                        xmlSerializer.attribute("", "minOccurs", "0");
                    } else if (dataTable.Columns.get(i).ColumnName.equals("sfa161")) {
                        xmlSerializer.attribute("", "type", "xs:decimal");
                        xmlSerializer.attribute("", "minOccurs", "0");
                    } else if (dataTable.Columns.get(i).ColumnName.equals("rvb33")) { //數量
                        xmlSerializer.attribute("", "type", "xs:decimal");
                        xmlSerializer.attribute("", "minOccurs", "0");
                    } else if (dataTable.Columns.get(i).ColumnName.equals("rvv02")) { //項次
                        xmlSerializer.attribute("", "type", "xs:int");
                        xmlSerializer.attribute("", "minOccurs", "0");
                    }
                    else {
                        xmlSerializer.attribute("", "type", "xs:string");
                        xmlSerializer.attribute("", "minOccurs", "0");
                    }
                } else {
                    xmlSerializer.attribute("", "type", "xs:string");
                    xmlSerializer.attribute("", "minOccurs", "0");
                }



                xmlSerializer.endTag("", "xs:element");
            }

            xmlSerializer.endTag("", "xs:sequence");
            xmlSerializer.endTag("", "xs:complexType");
            xmlSerializer.endTag("", "xs:element");
            xmlSerializer.endTag("", "xs:choice");
            xmlSerializer.endTag("", "xs:complexType");
            xmlSerializer.endTag("", "xs:element");
            xmlSerializer.endTag("", "xs:schema");
            //end tag <schema>


            //table start
            //diffgr:diffgram
            xmlSerializer.startTag("", "diffgr:diffgram");
            xmlSerializer.attribute("", "xmlns:msdata", "urn:schemas-microsoft-com:xml-msdata");
            xmlSerializer.attribute("", "xmlns:diffgr", "urn:schemas-microsoft-com:xml-diffgram-v1");
            //DocumentElement
            xmlSerializer.startTag("", "DocumentElement");
            xmlSerializer.attribute("", "xmlns", "");
            //start table rows
            for (int i = 0; i < dataTable.Rows.size(); i++) {

                if (dataTable.TableName != null && !dataTable.TableName.equals("")) {
                    xmlSerializer.startTag("", dataTable.TableName);
                } else {
                    xmlSerializer.startTag("", "MainTable");
                }
                //table row start from 1, not 0
                String id;
                if (dataTable.TableName != null && !dataTable.TableName.equals("")) {
                    id = dataTable.TableName+String.valueOf(i+1);
                } else {
                    id = "MainTable"+String.valueOf(i+1);
                }
                xmlSerializer.attribute("", "diffgr:id", id);
                //here rowOrder start with 0
                xmlSerializer.attribute("", "msdata:rowOrder", String.valueOf(i));
                xmlSerializer.attribute("", "diffgr:hasChanges", "inserted");



                for (int j = 0; j < dataTable.Columns.size(); j++) {

                    xmlSerializer.startTag("", dataTable.Columns.get(j).ColumnName);
                    if (dataTable.getValue(i, j) != null) {
                        xmlSerializer.text(dataTable.getValue(i,j).toString());
                    } else {
                        xmlSerializer.text("null");
                    }

                    xmlSerializer.endTag("", dataTable.Columns.get(j).ColumnName);

                }
                //System.out.print("\n");

                if (dataTable.TableName != null && !dataTable.TableName.equals("")) {
                    xmlSerializer.endTag("", dataTable.TableName);
                } else {
                    xmlSerializer.endTag("", "MainTable");
                }


            }
            //end table rows

            xmlSerializer.endTag("", "DocumentElement");
            xmlSerializer.endTag("", "diffgr:diffgram");
            //end tag <schema>


            //table end
            //xmlSerializer.endTag("", "NewDataSet");
            xmlSerializer.endDocument();

            Log.e(TAG, "xml = "+writer.toString());

            remove_start_xml_tag = writer.toString().replace("<?xml version='1.0' encoding='UTF-8' standalone='yes' ?>", "");
            Log.d(TAG, "remove_start_xml_tag = "+remove_start_xml_tag);

        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.e(TAG, "=== parseDataTableToXml end === ");

        return remove_start_xml_tag;
    }

    public static SoapObject parseDataTableToSoapObject(String tagName, DataTable dataTable) {
        SoapObject xmlxml = new SoapObject("", tagName);


        //start schema
        SoapObject schema = new SoapObject("", "xs:schema");

        schema.addAttribute("id", "NewDataSet");
        schema.addAttribute("xmlns", "");
        schema.addAttribute("xmlns:xs", "http://www.w3.org/2001/XMLSchema");
        schema.addAttribute("xmlns:msdata", "urn:schemas-microsoft-com:xml-msdata");
        //element
        SoapObject element = new SoapObject("", "xs:element");
        element.addAttribute("name", "NewDataSet");
        element.addAttribute("msdata:IsDataSet", "true");
        if (dataTable.TableName != null && !dataTable.TableName.equals("")) {
            element.addAttribute("msdata:MainDataTable", dataTable.TableName);
        } else {
            element.addAttribute("msdata:MainDataTable", "MainTable");
        }
        element.addAttribute("msdata:UseCurrentLocale", "true");
        //complexType
        SoapObject complexType = new SoapObject("", "xs:complexType");
        //choice
        SoapObject choice = new SoapObject("", "xs:choice");
        choice.addAttribute("minOccurs", "0");
        choice.addAttribute("maxOccurs", "unbounded");
        //element
        SoapObject element2 = new SoapObject("", "xs:element");
        if (dataTable.TableName != null && !dataTable.TableName.equals("")) {
            element2.addAttribute("name", dataTable.TableName);
        } else {
            element2.addAttribute("name", "MainTable");
        }
        //complexType
        SoapObject complexType2 = new SoapObject("", "xs:complexType");
        //sequence
        SoapObject sequence = new SoapObject("", "xs:sequence");
        //element

        for (int i=0; i<dataTable.Columns.size(); i++) {

            SoapObject element_define = new SoapObject("", "xs:element");
            element_define.addAttribute("name", dataTable.Columns.get(i).ColumnName);


            if (dataTable.getValue(0, i) != null) {
                if (dataTable.getValue(0, i).toString().equals("true") || dataTable.getValue(0, i).toString().equals("false")) {
                    element_define.addAttribute("type", "xs:boolean");
                } else if (dataTable.Columns.get(i).ColumnName.equals("tc_obf013")) {
                    element_define.addAttribute("type", "xs:decimal");
                    element_define.addAttribute("minOccurs", "0");
                } else if (dataTable.Columns.get(i).ColumnName.equals("inv_qty")) {
                    element_define.addAttribute("type", "xs:decimal");
                    element_define.addAttribute("minOccurs", "0");
                } else if (dataTable.Columns.get(i).ColumnName.equals("sfa06")) {
                    element_define.addAttribute("type", "xs:decimal");
                    element_define.addAttribute("minOccurs", "0");
                } else if (dataTable.Columns.get(i).ColumnName.equals("sfa063")) {
                    element_define.addAttribute("type", "xs:decimal");
                    element_define.addAttribute("minOccurs", "0");
                } else if (dataTable.Columns.get(i).ColumnName.equals("sfa161")) {
                    element_define.addAttribute("type", "xs:decimal");
                    element_define.addAttribute("minOccurs", "0");
                } else if (dataTable.Columns.get(i).ColumnName.equals("rvb33")) { //數量
                    element_define.addAttribute("type", "xs:decimal");
                    element_define.addAttribute("minOccurs", "0");
                } else if (dataTable.Columns.get(i).ColumnName.equals("rvv02")) { //項次
                    element_define.addAttribute("type", "xs:int");
                    element_define.addAttribute("minOccurs", "0");
                }
                else {
                    element_define.addAttribute("type", "xs:string");
                    element_define.addAttribute("minOccurs", "0");
                }
            } else {
                element_define.addAttribute("type", "xs:string");
                element_define.addAttribute("minOccurs", "0");
            }

            sequence.addSoapObject(element_define);
        }




        complexType2.addSoapObject(sequence);
        element2.addSoapObject(complexType2);
        choice.addSoapObject(element2);
        complexType.addSoapObject(choice);
        element.addSoapObject(complexType);
        schema.addSoapObject(element);
        //end schema
        xmlxml.addSoapObject(schema);


        SoapObject diffgram = new SoapObject("", "diffgr:diffgram");
        diffgram.addAttribute("xmlns:msdata", "urn:schemas-microsoft-com:xml-msdata");
        diffgram.addAttribute("xmlns:diffgr", "urn:schemas-microsoft-com:xml-diffgram-v1");
        //DocumentElement
        SoapObject document = new SoapObject("", "DocumentElement");
        document.addAttribute("xmlns", "");
        //start table
        for (int i = 0; i < dataTable.Rows.size(); i++) {

            SoapObject table;
            if (dataTable.TableName != null && !dataTable.TableName.equals("")) {
                table = new SoapObject("", dataTable.TableName);
                table.addAttribute("diffgr:id", dataTable.TableName+String.valueOf(i+1));
            } else {
                table = new SoapObject("", "MainTable");
                table.addAttribute("diffgr:id", "MainTable"+String.valueOf(i+1));
            }
            table.addAttribute("msdata:rowOrder", String.valueOf(i));
            table.addAttribute("diffgr:hasChanges", "inserted");



            for (int j = 0; j < dataTable.Columns.size(); j++) {

                if (dataTable.getValue(i,j) != null) {
                    table.addProperty(dataTable.Columns.get(j).ColumnName, dataTable.getValue(i,j).toString());
                } else {
                    table.addProperty(dataTable.Columns.get(j).ColumnName, "");
                }


            }

            document.addSoapObject(table);
        }


        //end table
        diffgram.addSoapObject(document);
        xmlxml.addSoapObject(diffgram);



        return xmlxml;
    }

    public static boolean parseToBoolean(SoapObject soapObject) {
        boolean ret = false;

        Log.e(TAG, "=== parseToBoolean start === "+soapObject.getPropertyCount());

        if (soapObject.getPropertyCount() == 1) {
            Log.e(TAG, "soapObject getPropertyInfo = "+soapObject.getPropertyInfo(0).toString());
            ret = Boolean.valueOf(soapObject.getProperty(0).toString());
        }

        Log.e(TAG, "ret = "+ret );
        Log.e(TAG, "=== parseToBoolean end === ");

        return ret;
    }

    public static String parseToString(SoapObject soapObject) {
        String ret = "";

        Log.e(TAG, "=== parseToString === "+soapObject.getPropertyCount());

        if (soapObject.getPropertyCount() == 1) {
            Log.e(TAG, "soapObject getPropertyInfo = "+soapObject.getPropertyInfo(0).toString());
            ret = soapObject.getProperty(0).toString();
        }

        Log.e(TAG, "ret = "+ret );
        Log.e(TAG, "=== parseToString end === ");

        return ret;
    }
}
