package com.macauto.macautowarehouse.data;

import android.content.Intent;
import android.util.Log;
import android.util.Xml;

import com.macauto.macautowarehouse.R;
import com.macauto.macautowarehouse.table.DataRow;
import com.macauto.macautowarehouse.table.DataTable;

import org.ksoap2.serialization.SoapObject;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;

import static com.macauto.macautowarehouse.EnteringWarehouseFragmnet.check_stock_in;
import static com.macauto.macautowarehouse.EnteringWarehouseFragmnet.dataTable;
import static com.macauto.macautowarehouse.EnteringWarehouseFragmnet.detailList;
import static com.macauto.macautowarehouse.EnteringWarehouseFragmnet.no_list;

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

    public static StringWriter parseDataTableToXml(DataTable dataTable) {
        Log.e(TAG, "=== parseDataTableToXml start === ");

        StringWriter writer = new StringWriter();

        XmlSerializer xmlSerializer = Xml.newSerializer();



        try {

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
            xmlSerializer.attribute("", "name", "Table");
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
                    } else {
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
                    /*if (j==0) {
                        xmlSerializer.startTag("", "check_sp");
                        xmlSerializer.text(dataTable.Rows.get(i).getValue(j).toString());
                        xmlSerializer.endTag("", "check_sp");
                    } else if (j == 1) {
                        xmlSerializer.startTag("", "rvu01");
                        xmlSerializer.text(dataTable.Rows.get(i).getValue(j).toString());
                        xmlSerializer.endTag("", "rvu01");
                    } else if (j == 2) {
                        xmlSerializer.startTag("", "rvu02");
                        xmlSerializer.text(dataTable.Rows.get(i).getValue(j).toString());
                        xmlSerializer.endTag("", "rvu02");
                    } else if (j == 3) {
                        xmlSerializer.startTag("", "rvb05");
                        xmlSerializer.text(dataTable.Rows.get(i).getValue(j).toString());
                        xmlSerializer.endTag("", "rvb05");
                    } else if (j == 4) {
                        xmlSerializer.startTag("", "pmn041");
                        xmlSerializer.text(dataTable.Rows.get(i).getValue(j).toString());
                        xmlSerializer.endTag("", "pmn041");
                    } else if (j == 5) {
                        xmlSerializer.startTag("", "ima021");
                        xmlSerializer.text(dataTable.Rows.get(i).getValue(j).toString());
                        xmlSerializer.endTag("", "ima021");
                    } else if (j == 6) {
                        xmlSerializer.startTag("", "rvv32");
                        xmlSerializer.text(dataTable.Rows.get(i).getValue(j).toString());
                        xmlSerializer.endTag("", "rvv32");
                    } else if (j == 7) {
                        xmlSerializer.startTag("", "rvv33");
                        xmlSerializer.text(dataTable.Rows.get(i).getValue(j).toString());
                        xmlSerializer.endTag("", "rvv33");
                    } else if (j == 8) {
                        xmlSerializer.startTag("", "rvv34");
                        xmlSerializer.text(dataTable.Rows.get(i).getValue(j).toString());
                        xmlSerializer.endTag("", "rvv34");
                    } else if (j == 9) {
                        xmlSerializer.startTag("", "rvb33");
                        xmlSerializer.text(dataTable.Rows.get(i).getValue(j).toString());
                        xmlSerializer.endTag("", "rvb33");
                    } else if (j == 10) {
                        xmlSerializer.startTag("", "pmc03");
                        xmlSerializer.text(dataTable.Rows.get(i).getValue(j).toString());
                        xmlSerializer.endTag("", "pmc03");
                    } else if (j == 11) {
                        xmlSerializer.startTag("", "gen02");
                        xmlSerializer.text(dataTable.Rows.get(i).getValue(j).toString());
                        xmlSerializer.endTag("", "gen02");
                    }*/


                        /*System.out.print(dataTable.Rows.get(i).getValue(j));
                        if (j < dataTable.Columns.size() - 1) {
                            System.out.print(", ");
                        }*/
                }
                //System.out.print("\n");
                xmlSerializer.endTag("", "Table");
            }
            //table end
            xmlSerializer.endTag("", "printx");
            xmlSerializer.endDocument();

            Log.e(TAG, "xml = "+writer.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.e(TAG, "=== parseDataTableToXml end === ");

        return writer;
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
