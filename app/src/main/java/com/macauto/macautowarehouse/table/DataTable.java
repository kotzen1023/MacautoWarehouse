package com.macauto.macautowarehouse.table;


import android.util.Log;

public class DataTable {
    /**
     * 保存DataRow的集合，在DataTable初始化時，便會建立
     */
    public DataRowCollection Rows;
    /**
     * 保存DataColumn的集合，在DataTable初始化時，便會建立
     */
    public DataColumnCollection Columns;
    /**
     * DataTable的名稱，沒什麼用到
     */
    public String TableName;

    /**
     * 初始化DataTable，並建立DataColumnCollection，DataRowCollection
     */
    public DataTable() {
        this.Columns = new DataColumnCollection(this);
        this.Rows = new DataRowCollection(this);

    }

    /**
     * 除了初始化DataTable， 可以指定DataTable的名字(沒什麼意義)
     * @param tableName //dataTableName DataTable的名字
     */
    public DataTable(String tableName) {
        this();
        this.TableName = tableName;
    }



    /**
     * 由此DataTable物件來建立一個DataRow物件
     * @return DataRow
     */
    public DataRow NewRow()  {

        DataRow row = new DataRow(this);//DataRow為呼叫此方法DataTable的成員

        return row;
    }


    /**
     * 把DataTable當做二維陣列，給列索引和行索引，設定值的方法
     * <br/>(發佈者自行寫的方法)
     * @param rowIndex 列索引(從0算起)
     * @param columnIndex 行索引(從0算起)
     * @param value 要給的值
     */
    public void setValue(int rowIndex, int columnIndex,Object value) {
        this.Rows.get(rowIndex).setValue(columnIndex, value);
    }

    /**
     * 把DataTable當做二維陣列，給列索引和行名稱，設定值的方法
     * <br/>(發佈者自行寫的方法)
     * @param rowIndex 列索引(從0算起)
     * @param columnName //columnIndex 行名稱
     * @param value 要給的值
     */
    public void setValue(int rowIndex,String columnName,Object value) {
        this.Rows.get(rowIndex).setValue(columnName.toLowerCase(), value);
    }


    /**
     * 把DataTable當做二維陣列，給列索引和行索引，取得值的方法
     * <br/>(發佈者自行寫的方法)
     * @param rowIndex 列索引(從0算起)
     * @param columnIndex 行索引(從0算起)
     * @return 回傳該位置的值
     */
    public Object getValue(int rowIndex,int columnIndex) {
        return this.Rows.get(rowIndex).getValue(columnIndex);
    }


    /**
     * 把DataTable當做二維陣列，給列索引和行名稱，取得值的方法
     * <br/>(發佈者自行寫的方法)
     * @param rowindex //rowIndex 列索引(從0算起)
     * @param columnName 行名稱
     * @return 回傳該位置的值
     */
    public Object getValue(int rowindex,String columnName) {
        return this.Rows.get(rowindex).getValue(columnName.toLowerCase());
    }

    public void clear() {
        for (int i = this.Rows.size()-1; i >= 0; i--) {
            this.Rows.remove(i);
        }

        for (int i = this.Columns.size()-1; i >= 0; i--) {
            this.Columns.remove(i);
        }
    }

    private void rowSwitch(int row1, int row2) {

        if (row2 > row1) {
            DataRow rowTemp = this.Rows.get(row1);
            this.Rows.add(row2+1, rowTemp);
            this.Rows.remove(row1);

            rowTemp = this.Rows.get(row2-1);
            this.Rows.add(row1, rowTemp);
            this.Rows.remove(row2);
        } else {
            DataRow rowTemp = this.Rows.get(row2);
            this.Rows.add(row1+1, rowTemp);
            this.Rows.remove(row2);

            rowTemp = this.Rows.get(row1-1);
            this.Rows.add(row2, rowTemp);
            this.Rows.remove(row1);
        }
    }

    public void SortByColumn(String columnName) {

        Log.e(TableName, "[SortByColumn start]");

        int current_compare_row = 0;
        int current_switch_row = 0;

        if (this.Rows.size() > 2) {

            while (current_compare_row < this.Rows.size()-1) {
                current_switch_row = current_compare_row + 1;

                String currentCompareRowValue = this.Rows.get(current_compare_row).getValue(columnName).toString();



                if (currentCompareRowValue.equals(this.Rows.get(current_switch_row).getValue(columnName).toString())) { //compare row match switch row, compare and switch will add 1
                    Log.e(TableName, "compare row match switch row, compare and switch will add 1");
                    //current_compare_row = current_compare_row + 1;
                } else {
                    //check from the last
                    Log.e(TableName, "[compare start]");
                    for (int j=this.Rows.size()-1; j > current_switch_row; j--) {
                        if (currentCompareRowValue.equals(this.Rows.get(j).getValue(columnName).toString())) { //match
                            Log.e(TableName, "switch row["+j+"] with row["+current_switch_row+"]");

                            //make row switch
                            this.rowSwitch(current_switch_row, j);
                            //current_compare_row add 1
                            //current_compare_row = current_compare_row + 1;
                            break;
                        }
                    }
                    Log.e(TableName, "[compare end]");
                }
                current_compare_row = current_compare_row + 1;


            }

        } else {
            Log.e(TableName, "Table size is <= 2");
        }

        Log.e(TableName, "[SortByColumn end]");
        //for (int i=0; i < this.Rows.size(); i++) {
        /*while (current_compare_row < this.Rows.size()) {

            String currentCompareRowValue = this.Rows.get(current_compare_row).getValue(columnName).toString();

            found_index = -1;



            if (current_switch_row < this.Rows.size() - 1) { //at least the last two can switch
                for (int j=current_compare_row+1; j < this.Rows.size(); j++) {
                    if (currentCompareRowValue.equals(this.Rows.get(j).getValue(columnName))) {
                        found_index = j;
                        break;
                    }
                }

                if (found_index != -1) { //found same
                    if (found_index > current_switch_row) { //exchange
                        this.rowSwitch(current_switch_row, found_index);
                    } else { //no need to exchange
                        Log.e(TableName, "No need to exchange!");
                    }
                }
            } else {
                Log.e(TableName, "current_switch_row is equal  table size < 3");
                current_compare_row = this.Rows.size();
            }




        }*/
    }
}
