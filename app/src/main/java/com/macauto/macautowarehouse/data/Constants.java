package com.macauto.macautowarehouse.data;

public class Constants {
    public interface ACTION {
        String SOAP_CONNECTION_FAIL = "com.macauto.MacautoWarehoouse.SoapConnectionFail";

        String ACTION_CHECK_EMP_EXIST_ACTION = "com.macauto.MacautoWarehoouse.CheckEmpExistAction";
        String ACTION_CHECK_EMP_EXIST_SUCCESS = "com.macauto.MacautoWarehoouse.CheckEmpExistSuccess";
        String ACTION_CHECK_EMP_EXIST_NOT_EXIST = "com.macauto.MacautoWarehoouse.CheckEmpExistNotExist";
        String ACTION_CHECK_EMP_PASSWORD_ACTION = "com.macauto.MacautoWarehoouse.CheckEmpPasswordAction";
        String ACTION_CHECK_EMP_PASSWORD_SUCCESS = "com.macauto.MacautoWarehoouse.CheckEmpPasswordSuccess";
        String ACTION_CHECK_EMP_PASSWORD_FAILED = "com.macauto.MacautoWarehoouse.CheckEmpPasswordFailed";
        String ACTION_SET_INSPECTED_RECEIVE_ITEM_CLEAN = "com.macauto.MacautoWarehoouse.SetInspectedReceiveItemClean";
        String ACTION_GET_INSPECTED_RECEIVE_ITEM_SUCCESS = "com.macauto.MacautoWarehoouse.GetInspectedReceiveItemSuccess";
        String ACTION_GET_INSPECTED_RECEIVE_ITEM_FAILED = "com.macauto.MacautoWarehoouse.GetInspectedReceiveItemFailed";
        String ACTION_MODIFIED_ITEM_COMPLETE = "com.macauto.MacautoWarehoouse.ModifiedItemComplete";
        String ACTION_CONFIRM_ENTERING_WAREHOUSE_ACTION = "com.macauto.MacautoWarehoouse.ConfirmEnteringWarehouse";
        String ACTION_UPDATE_TT_RECEIVE_IN_RVV33_FAILED = "com.macauto.MacautoWarehoouse.UpdateTTReceiveInEvv33Failed";
        String ACTION_UPDATE_TT_RECEIVE_IN_RVV33_SUCCESS = "com.macauto.MacautoWarehoouse.UpdateTTReceiveInEvv33Success";
        String ACTION_SCAN_RESET = "com.macauto.MacautoWarehoouse.ScanReset";

        String ACTION_LOGIN_FAIL = "com.macauto.MacautoWarehoouse.Login.Fail";
        String ACTION_LOGIN_SUCCESS = "com.macauto.MacautoWarehoouse.Login.Success";
        String ACTION_LOGOUT_ACTION = "com.macauto.MacautoWarehoouse.LogoutAction";

        String ACTION_SCAN = "com.macauto.MacautoWarehoouse.client.android.SCAN";
        String ACTION_GET_BARCODE_MESSGAGE_COMPLETE = "com.macauto.MacautoWarehoouse.GetBarcodeMessageComplete";
        String ACTION_CHECK_RECEIVE_GOODS = "com.macauto.MacautoWarehoouse.CheckReceiveGoods.Action";
    }
}
