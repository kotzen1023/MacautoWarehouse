package com.macauto.macautowarehouse.data;

public class Constants {
    public interface ACTION {
        String SOAP_CONNECTION_FAIL = "com.macauto.MacautoWarehoouse.SoapConnectionFail";

        //login
        String ACTION_LOGIN_FAIL = "com.macauto.MacautoWarehoouse.Login.Fail";
        String ACTION_LOGIN_SUCCESS = "com.macauto.MacautoWarehoouse.Login.Success";
        String ACTION_LOGOUT_ACTION = "com.macauto.MacautoWarehoouse.LogoutAction";
        String ACTION_SOCKET_TIMEOUT = "com.macauto.MacautoWarehoouse.SocketTimeOut";
        //
        String ACTION_SHOW_VIRTUAL_KEYBOARD_ACTION = "com.macauto.MacautoWarehoouse.ShowVirtualKeyboardAction";
        String ACTION_HIDE_VIRTUAL_KEYBOARD_ACTION = "com.macauto.MacautoWarehoouse.HideVirtualKeyboardAction";
        //Main
        String ACTION_MAIN_RESET_TITLE = "com.macauto.MacautoWarehoouse.MainResetTitleAction";

        //setting
        String ACTION_SETTING_PDA_TYPE_ACTION = "com.macauto.MacautoWarehoouse.SettingPdaTypeAction";
        String ACTION_SETTING_WEB_SOAP_PORT_ACTION = "com.macauto.MacautoWarehoouse.SettingWebSoapPortAction";

        //into stock
        String ACTION_CHECK_EMP_EXIST_ACTION = "com.macauto.MacautoWarehoouse.CheckEmpExistAction";
        String ACTION_CHECK_EMP_EXIST_SUCCESS = "com.macauto.MacautoWarehoouse.CheckEmpExistSuccess";
        String ACTION_CHECK_EMP_EXIST_NOT_EXIST = "com.macauto.MacautoWarehoouse.CheckEmpExistNotExist";
        String ACTION_CHECK_EMP_PASSWORD_ACTION = "com.macauto.MacautoWarehoouse.CheckEmpPasswordAction";
        String ACTION_CHECK_EMP_PASSWORD_SUCCESS = "com.macauto.MacautoWarehoouse.CheckEmpPasswordSuccess";
        String ACTION_CHECK_EMP_PASSWORD_FAILED = "com.macauto.MacautoWarehoouse.CheckEmpPasswordFailed";
        String ACTION_SET_INSPECTED_RECEIVE_ITEM_CLEAN = "com.macauto.MacautoWarehoouse.SetInspectedReceiveItemClean";
        String ACTION_GET_INSPECTED_RECEIVE_ITEM_SUCCESS = "com.macauto.MacautoWarehoouse.GetInspectedReceiveItemSuccess";
        String ACTION_GET_INSPECTED_RECEIVE_ITEM_EMPTY = "com.macauto.MacautoWarehoouse.GetInspectedReceiveItemEmpty";
        String ACTION_GET_INSPECTED_RECEIVE_ITEM_FAILED = "com.macauto.MacautoWarehoouse.GetInspectedReceiveItemFailed";
        String ACTION_MODIFIED_ITEM_COMPLETE = "com.macauto.MacautoWarehoouse.ModifiedItemComplete";
        String ACTION_CONFIRM_ENTERING_WAREHOUSE_ACTION = "com.macauto.MacautoWarehoouse.ConfirmEnteringWarehouse";
        String ACTION_UPDATE_TT_RECEIVE_IN_RVV33_FAILED = "com.macauto.MacautoWarehoouse.UpdateTTReceiveInEvv33Failed";
        String ACTION_UPDATE_TT_RECEIVE_IN_RVV33_SUCCESS = "com.macauto.MacautoWarehoouse.UpdateTTReceiveInEvv33Success";
        String ACTION_GET_DOC_TYPE_IS_REG_OR_SUB_ACTION = "com.macauto.MacautoWarehoouse.GetDocTypeIsRegOrSub";
        String ACTION_GET_DOC_TYPE_IS_REG_OR_SUB_SUCCESS = "com.macauto.MacautoWarehoouse.GetDocTypeIsRegOrSubSuccess";
        String ACTION_GET_DOC_TYPE_IS_REG_OR_SUB_FAILED = "com.macauto.MacautoWarehoouse.GetDocTypeIsRegOrSubFailed";
        String ACTION_EXECUTE_TT_ACTION = "com.macauto.MacautoWarehoouse.ExcuteTtAction";
        String ACTION_EXECUTE_TT_SUCCESS = "com.macauto.MacautoWarehoouse.ExcuteTtSuccess";
        String ACTION_EXECUTE_TT_FAILED = "com.macauto.MacautoWarehoouse.ExcuteTtFailed";
        String ACTION_ENTERING_WAREHOUSE_COMPLETE = "com.macauto.MacautoWarehoouse.EnteringWarehouseComplete";

        String ACTION_ENTERING_WAREHOUSE_DIVIDED_DIALOG_TEXT_CHANGE = "com.macauto.MacautoWarehoouse.EnteringWarehouseDividedDialogTextChange";
        String ACTION_ENTERING_WAREHOUSE_DIVIDED_DIALOG_ADD = "com.macauto.MacautoWarehoouse.EnteringWarehouseDividedDialogAdd";
        //String ACTION_ENTERING_WAREHOUSE_SHOW_CONFIRM_BUTTON = "com.macauto.MacautoWarehoouse.EnteringWarehouseShowConfirmButton";
        //String ACTION_ENTERING_WAREHOUSE_HIDE_CONFIRM_BUTTON = "com.macauto.MacautoWarehoouse.EnteringWarehouseHideConfirmButton";
        String ACTION_ENTERING_WAREHOUSE_CHECKBOX_CHANGE = "com.macauto.MacautoWarehoouse.EnteringWarehouseCheckboxChange";

        //receiving goods
        String ACTION_RECEIVING_GOODS_DATA_ACTION = "com.macauto.MacautoWarehoouse.ReceivingGoodsData";
        String ACTION_RECEIVING_GOODS_DATA_FAILED = "com.macauto.MacautoWarehoouse.ReceivingGoodsDataFailed";
        String ACTION_RECEIVING_GOODS_DATA_SUCCESS = "com.macauto.MacautoWarehoouse.ReceivingGoodsDataSuccess";
        String ACTION_RECEIVING_GOODS_DATA_NO_VENDOR_DATA = "com.macauto.MacautoWarehoouse.ReceivingGoodsDataNoVendorData";

        //allocation
        String ACTION_ALLOCATION_SEND_MSG_GET_LOCATE_NO_ACTION = "com.macauto.MacautoWarehoouse.AllocationSendMsgGetLocateNoAction";
        String ACTION_ALLOCATION_SEND_MSG_GET_LOCATE_NO_SUCCESS = "com.macauto.MacautoWarehoouse.AllocationSendMsgGetLocateNoSuccess";
        String ACTION_ALLOCATION_SEND_MSG_GET_LOCATE_NO_EMPTY = "com.macauto.MacautoWarehoouse.AllocationSendMsgGetLocateNoEmpty";
        String ACTION_ALLOCATION_SEND_MSG_GET_LOCATE_NO_FAILED = "com.macauto.MacautoWarehoouse.AllocationSendMsgGetLocateNoFailed";
        String ACTION_ALLOCATION_SEND_MSG_GET_MADE_INFO_ACTION = "com.macauto.MacautoWarehoouse.AllocationSendMsgGetMadeInfoAction";
        String ACTION_ALLOCATION_SEND_MSG_GET_MADE_INFO_SUCCESS = "com.macauto.MacautoWarehoouse.AllocationSendMsgGetMadeInfoSuccess";
        String ACTION_ALLOCATION_SEND_MSG_GET_MADE_INFO_EMPTY = "com.macauto.MacautoWarehoouse.AllocationSendMsgGetMadeInfoEmpty";
        String ACTION_ALLOCATION_SEND_MSG_GET_MADE_INFO_FAILED = "com.macauto.MacautoWarehoouse.AllocationSendMsgGetMadeInfoFailed";
        String ACTION_ALLOCATION_SEND_MSG_CHECK_MADE_NO_EXIST_ACTION = "com.macauto.MacautoWarehoouse.AllocationSendMsgCheckMadeNoExistAction";
        String ACTION_ALLOCATION_SEND_MSG_CHECK_MADE_NO_EXIST_SUCCESS = "com.macauto.MacautoWarehoouse.AllocationSendMsgCheckMadeNoExistSuccess";
        String ACTION_ALLOCATION_SEND_MSG_CHECK_MADE_NO_EXIST_NOT_EXIST = "com.macauto.MacautoWarehoouse.AllocationSendMsgCheckMadeNoExistNotExist";
        String ACTION_ALLOCATION_SEND_MSG_CHECK_MADE_NO_EXIST_FAILED = "com.macauto.MacautoWarehoouse.AllocationSendMsgCheckStockNoExistFailed";
        String ACTION_ALLOCATION_SEND_MSG_CHECK_STOCK_NO_EXIST_ACTION = "com.macauto.MacautoWarehoouse.AllocationSendMsgCheckStockNoExistAction";
        String ACTION_ALLOCATION_SEND_MSG_CHECK_STOCK_NO_EXIST_SUCCESS = "com.macauto.MacautoWarehoouse.AllocationSendMsgCheckStockNoExistSuccess";
        String ACTION_ALLOCATION_SEND_MSG_CHECK_STOCK_NO_EXIST_NOT_EXIST = "com.macauto.MacautoWarehoouse.AllocationSendMsgCheckStockNoExistNotExist";
        String ACTION_ALLOCATION_SEND_MSG_CHECK_STOCK_NO_EXIST_FAILED = "com.macauto.MacautoWarehoouse.AllocationSendMsgCheckStockNoExistFailed";
        String ACTION_ALLOCATION_SEND_MSG_GET_SFA_MESS_ACTION = "com.macauto.MacautoWarehoouse.AllocationSendMsgGetSfaMessAction";
        String ACTION_ALLOCATION_SEND_MSG_GET_SFA_MESS_SUCCESS = "com.macauto.MacautoWarehoouse.AllocationSendMsgGetSfaMessSuccess";
        String ACTION_ALLOCATION_SEND_MSG_GET_SFA_MESS_EMPTY = "com.macauto.MacautoWarehoouse.AllocationSendMsgGetSfaMessEmpty";
        String ACTION_ALLOCATION_SEND_MSG_GET_SFA_MESS_FAILED = "com.macauto.MacautoWarehoouse.AllocationSendMsgGetSfaMessFailed";
        String ACTION_ALLOCATION_SEND_MSG_GET_TAG_ID_ACTION = "com.macauto.MacautoWarehoouse.AllocationSendMsgGetTagIdAction";
        String ACTION_ALLOCATION_SEND_MSG_GET_TAG_ID_SUCCESS = "com.macauto.MacautoWarehoouse.AllocationSendMsgGetTagIdSuccess";
        String ACTION_ALLOCATION_SEND_MSG_GET_TAG_ID_FAILED = "com.macauto.MacautoWarehoouse.AllocationSendMsgGetTagIdFailed";
        String ACTION_ALLOCATION_SEND_MSG_GET_SFA_MESS_MOVE_ACTION = "com.macauto.MacautoWarehoouse.AllocationSendMsgGetSfaMessMoveAction";
        String ACTION_ALLOCATION_SEND_MSG_GET_SFA_MESS_MOVE_SUCCESS = "com.macauto.MacautoWarehoouse.AllocationSendMsgGetSfaMessMoveSuccess";
        String ACTION_ALLOCATION_SEND_MSG_GET_SFA_MESS_MOVE_EMPTY = "com.macauto.MacautoWarehoouse.AllocationSendMsgGetSfaMessMoveEmpty";
        String ACTION_ALLOCATION_SEND_MSG_GET_SFA_MESS_MOVE_FAILED = "com.macauto.MacautoWarehoouse.AllocationSendMsgGetSfaMessMoveFailed";
        String ACTION_ALLOCATION_HANDLE_MSG_DELETE_ACTION = "com.macauto.MacautoWarehoouse.AllocationHandleMsgDeleteAction";
        String ACTION_ALLOCATION_HANDLE_MSG_DELETE_SUCCESS = "com.macauto.MacautoWarehoouse.AllocationHandleMsgDeleteSuccess";
        String ACTION_ALLOCATION_HANDLE_MSG_DELETE_FAILED = "com.macauto.MacautoWarehoouse.AllocationHandleMsgDeleteFailed";

        String ACTION_ALLOCATION_GET_MY_MESS_LIST_ACTION = "com.macauto.MacautoWarehoouse.AllocationGetMyMessListAction";
        String ACTION_ALLOCATION_GET_MY_MESS_LIST_SUCCESS = "com.macauto.MacautoWarehoouse.AllocationGetMyMessListSuccess";
        String ACTION_ALLOCATION_GET_MY_MESS_LIST_EMPTY = "com.macauto.MacautoWarehoouse.AllocationGetMyMessListEmpty";
        String ACTION_ALLOCATION_GET_MY_MESS_LIST_FAILED = "com.macauto.MacautoWarehoouse.AllocationGetMyMessListFailed";
        String ACTION_ALLOCATION_GET_MY_MESS_DETAIL_ACTION = "com.macauto.MacautoWarehoouse.AllocationGetMyMessDetailAction";
        String ACTION_ALLOCATION_GET_MY_MESS_DETAIL_SUCCESS = "com.macauto.MacautoWarehoouse.AllocationGetMyMessDetailSuccess";
        String ACTION_ALLOCATION_GET_MY_MESS_DETAIL_FAILED = "com.macauto.MacautoWarehoouse.AllocationGetMyMessDetailFailed";
        String ACTION_ALLOCATION_CHECK_IS_DELETE_RIGHT_ACTION = "com.macauto.MacautoWarehoouse.AllocationCheckIsDeleteRightAction";
        String ACTION_ALLOCATION_CHECK_IS_DELETE_RIGHT_SUCCESS = "com.macauto.MacautoWarehoouse.AllocationCheckIsDeleteRightSuccess";
        String ACTION_ALLOCATION_CHECK_IS_DELETE_RIGHT_FAILED = "com.macauto.MacautoWarehoouse.AllocationCheckIsDeleteRightFailed";
        //barcode
        String ACTION_ALLOCATION_GET_LOT_CODE_ACTION = "com.macauto.MacautoWarehoouse.AllocationGetLotCodeAction";
        String ACTION_ALLOCATION_GET_LOT_CODE_EMPTY = "com.macauto.MacautoWarehoouse.AllocationGetLotCodeEmpty";
        String ACTION_ALLOCATION_GET_LOT_CODE_SUCCESS = "com.macauto.MacautoWarehoouse.AllocationGetLotCodeSuccess";
        String ACTION_ALLOCATION_GET_LOT_CODE_FAILED = "com.macauto.MacautoWarehoouse.AllocationGetLotCodeFailed";


        //batch_no
        String ACTION_SEARCH_PART_BATCH_ACTION = "com.macauto.MacautoWarehoouse.SearchPartWarehouseBatchAction";
        String ACTION_SEARCH_PART_BATCH_CLEAN = "com.macauto.MacautoWarehoouse.SearchPartWarehouseBatchClean";
        String ACTION_SEARCH_PART_BATCH_FAILED = "com.macauto.MacautoWarehoouse.SearchPartWarehouseBatchFailed";
        String ACTION_SEARCH_PART_BATCH_SUCCESS = "com.macauto.MacautoWarehoouse.SearchPartWarehouseBatchSuccess";

        //Search Parts
        String ACTION_SEARCH_PART_WAREHOUSE_LIST_ACTION = "com.macauto.MacautoWarehoouse.SearchPartWarehouseListAction";
        String ACTION_SEARCH_PART_WAREHOUSE_LIST_CLEAN = "com.macauto.MacautoWarehoouse.SearchPartWarehouseListClean";
        String ACTION_SEARCH_PART_WAREHOUSE_LIST_FAILED = "com.macauto.MacautoWarehoouse.SearchPartWarehouseListFailed";
        String ACTION_SEARCH_PART_WAREHOUSE_LIST_SUCCESS = "com.macauto.MacautoWarehoouse.SearchPartWarehouseListSuccess";
        String ACTION_SEARCH_PART_WAREHOUSE_LIST_EMPTY = "com.macauto.MacautoWarehoouse.SearchPartWarehouseListEmpty";
        String ACTION_SEARCH_PART_WAREHOUSE_SORT_COMPLETE = "com.macauto.MacautoWarehoouse.SearchPartWarehouseSortComplete";
        String ACTION_SEARCH_PART_WAREHOUSE_GET_ORIGINAL_LIST = "com.macauto.MacautoWarehoouse.SearchPartWarehouseGetOriginalList";

        //Shipment
        String ACTION_SHIPMENT_GET_PRE_SHIPPING_INFO_ACTION = "com.macauto.MacautoWarehoouse.ShipmentGetPreShippingInfoAction";
        String ACTION_SHIPMENT_GET_PRE_SHIPPING_INFO_FAILED = "com.macauto.MacautoWarehoouse.ShipmentGetPreShippingInfoFailed";
        String ACTION_SHIPMENT_GET_PRE_SHIPPING_INFO_SUCCESS = "com.macauto.MacautoWarehoouse.ShipmentGetPreShippingInfoSuccess";
        String ACTION_SHIPMENT_GET_PRE_SHIPPING_INFO_EMPTY = "com.macauto.MacautoWarehoouse.ShipmentGetPreShippingInfoEmpty";

        String ACTION_SHIPMENT_GET_OGC_FILE_2_ACTION = "com.macauto.MacautoWarehoouse.ShipmentGetOgcFile2Action";
        String ACTION_SHIPMENT_GET_OGC_FILE_2_FAILED = "com.macauto.MacautoWarehoouse.ShipmentGetOgcFile2Failed";
        String ACTION_SHIPMENT_GET_OGC_FILE_2_SUCCESS = "com.macauto.MacautoWarehoouse.ShipmentGetOgcFile2Success";
        String ACTION_SHIPMENT_GET_OGC_FILE_2_EMPTY = "com.macauto.MacautoWarehoouse.ShipmentGetOgcFile2Empty";

        String ACTION_SHIPMENT_GET_WAREHOUSE_ACTION = "com.macauto.MacautoWarehoouse.ShipmentGetWarehouseAction";
        String ACTION_SHIPMENT_GET_WAREHOUSE_FAILED = "com.macauto.MacautoWarehoouse.ShipmentGetWarehouseFailed";
        String ACTION_SHIPMENT_GET_WAREHOUSE_SUCCESS = "com.macauto.MacautoWarehoouse.ShipmentGetWarehouseSuccess";
        String ACTION_SHIPMENT_GET_WAREHOUSE_EMPTY = "com.macauto.MacautoWarehoouse.ShipmentGetWarehouseEmpty";

        String ACTION_SHIPMENT_GET_PRE_SHIPPING_INFO_CONFIRM_SP_ACTION = "com.macauto.MacautoWarehoouse.ShipmentGetPreShippingInfoConfirmSpAction";
        String ACTION_SHIPMENT_GET_PRE_SHIPPING_INFO_CONFIRM_SP_FAILED = "com.macauto.MacautoWarehoouse.ShipmentGetPreShippingInfoConfirmSpFailed";
        String ACTION_SHIPMENT_GET_PRE_SHIPPING_INFO_CONFIRM_SP_SUCCESS = "com.macauto.MacautoWarehoouse.ShipmentGetPreShippingInfoConfirmSpSuccess";

        String ACTION_SHIPMENT_SHIPPING_INSERT_OGC_FILE_ACTION = "com.macauto.MacautoWarehoouse.ShipmentShippingInsertOgcFileAction";
        String ACTION_SHIPMENT_SHIPPING_INSERT_OGC_FILE_FAILED = "com.macauto.MacautoWarehoouse.ShipmentShippingInsertOgcFileFailed";
        String ACTION_SHIPMENT_SHIPPING_INSERT_OGC_FILE_SUCCESS = "com.macauto.MacautoWarehoouse.ShipmentShippingInsertOgcFileSuccess";

        String ACTION_SCAN_RESET = "com.macauto.MacautoWarehoouse.ScanReset";
        String ACTION_CHECK_STOCK_LOCATE_NO_EXIST_ACTION = "com.macauto.MacautoWarehoouse.CheckStockLocateNoExist";

        String ACTION_SCAN = "com.macauto.MacautoWarehoouse.client.android.SCAN";
        String ACTION_GET_BARCODE_MESSGAGE_COMPLETE = "com.macauto.MacautoWarehoouse.GetBarcodeMessageComplete";
        String ACTION_CHECK_RECEIVE_GOODS = "com.macauto.MacautoWarehoouse.CheckReceiveGoods.Action";

        String ACTION_SEARCH_MENU_SHOW_ACTION = "com.macauto.MacautoWarehoouse.SearchMenuShowAction";
        String ACTION_SEARCH_MENU_HIDE_ACTION = "com.macauto.MacautoWarehoouse.SearchMenuHideAction";
        String ACTION_RESET_TITLE_PART_IN_STOCK = "com.macauto.MacautoWarehoouse.ResetTitlePartInStock";
    }
}
