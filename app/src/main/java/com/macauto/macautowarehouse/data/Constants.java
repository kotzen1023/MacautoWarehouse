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
        String ACTION_SET_INSPECTED_RECEIVE_ITEM_CLEAN_ONLY = "com.macauto.MacautoWarehoouse.SetInspectedReceiveItemCleanOnly";
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
        String ACTION_GET_DOC_TYPE_IS_REG_OR_SUB_COMPLETE = "com.macauto.MacautoWarehoouse.GetDocTypeIsRegOrSubComplete";
        String ACTION_EXECUTE_TT_ACTION = "com.macauto.MacautoWarehoouse.ExcuteTtAction";
        String ACTION_EXECUTE_TT_SUCCESS = "com.macauto.MacautoWarehoouse.ExcuteTtSuccess";
        String ACTION_EXECUTE_TT_FAILED = "com.macauto.MacautoWarehoouse.ExcuteTtFailed";
        String ACTION_ENTERING_WAREHOUSE_COMPLETE = "com.macauto.MacautoWarehoouse.EnteringWarehouseComplete";
        String ACTION_DELETE_TT_RECEIVE_GOODS_IN_TEMP_ACTION = "com.macauto.MacautoWarehoouse.DeleteTTReceiveGoodsInTempAction";
        String ACTION_DELETE_TT_RECEIVE_GOODS_IN_TEMP_SUCCESS = "com.macauto.MacautoWarehoouse.DeleteTTReceiveGoodsInTempSuccess";
        String ACTION_DELETE_TT_RECEIVE_GOODS_IN_TEMP_FAILED = "com.macauto.MacautoWarehoouse.DeleteTTReceiveGoodsInTempFailed";

        String ACTION_DELETE_TT_RECEIVE_GOODS_IN_TEMP2_ACTION = "com.macauto.MacautoWarehoouse.DeleteTTReceiveGoodsInTemp2Action";
        String ACTION_DELETE_TT_RECEIVE_GOODS_IN_TEMP2_SUCCESS = "com.macauto.MacautoWarehoouse.DeleteTTReceiveGoodsInTemp2Success";
        String ACTION_DELETE_TT_RECEIVE_GOODS_IN_TEMP2_FAILED = "com.macauto.MacautoWarehoouse.DeleteTTReceiveGoodsInTemp2Failed";

        String ACTION_ENTERING_WAREHOUSE_DIVIDED_DIALOG_TEXT_CHANGE = "com.macauto.MacautoWarehoouse.EnteringWarehouseDividedDialogTextChange";
        String ACTION_ENTERING_WAREHOUSE_DIVIDED_DIALOG_ADD = "com.macauto.MacautoWarehoouse.EnteringWarehouseDividedDialogAdd";
        String ACTION_ENTERING_WAREHOUSE_DIVIDED_DIALOG_SHOW = "com.macauto.MacautoWarehoouse.EnteringWarehouseDividedDialogShow";


        String ACTION_GET_INSPECTED_RECEIVE_ITEM_AX_ACTION = "com.macauto.MacautoWarehoouse.GetInspectedReceiveItemAXAction";
        String ACTION_GET_INSPECTED_RECEIVE_ITEM_AX_SUCCESS = "com.macauto.MacautoWarehoouse.GetInspectedReceiveItemAXSuccess";
        String ACTION_GET_INSPECTED_RECEIVE_ITEM_AX_EMPTY = "com.macauto.MacautoWarehoouse.GetInspectedReceiveItemAXEmpty";
        String ACTION_GET_INSPECTED_RECEIVE_ITEM_AX_FAILED = "com.macauto.MacautoWarehoouse.GetInspectedReceiveItemAXFailed";

        //locate split
        String ACTION_GET_TT_SPLIT_RVV_ITEM_ACTION = "com.macauto.MacautoWarehoouse.GetTTSplitRvvItemAction";
        String ACTION_GET_TT_SPLIT_RVV_ITEM_SUCCESS = "com.macauto.MacautoWarehoouse.GetTTSplitRvvItemSuccess";
        String ACTION_GET_TT_SPLIT_RVV_ITEM_FAILED = "com.macauto.MacautoWarehoouse.GetTTSplitRvvItemFailed";

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
        String ACTION_ALLOCATION_GET_TAG_ID_ACTION = "com.macauto.MacautoWarehoouse.AllocationGetTagIdAction";
        String ACTION_ALLOCATION_GET_TAG_ID_SUCCESS = "com.macauto.MacautoWarehoouse.AllocationGetTagIdSuccess";
        String ACTION_ALLOCATION_GET_TAG_ID_FAILED = "com.macauto.MacautoWarehoouse.AllocationGetTagIdFailed";
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
        String ACTION_ALLOCATION_CHECK_IS_DELETE_RIGHT_YES = "com.macauto.MacautoWarehoouse.AllocationCheckIsDeleteRightYes";
        String ACTION_ALLOCATION_CHECK_IS_DELETE_RIGHT_NO = "com.macauto.MacautoWarehoouse.AllocationCheckIsDeleteRightNo";
        String ACTION_ALLOCATION_CHECK_IS_DELETE_RIGHT_FAILED = "com.macauto.MacautoWarehoouse.AllocationCheckIsDeleteRightFailed";
        String ACTION_ALLOCATION_GET_DEPT_NO_ACTION = "com.macauto.MacautoWarehoouse.AllocationGetDeptNoAction";
        String ACTION_ALLOCATION_GET_DEPT_NO_SUCCESS = "com.macauto.MacautoWarehoouse.AllocationGetDeptNoSuccess";
        String ACTION_ALLOCATION_GET_DEPT_NO_FAILED = "com.macauto.MacautoWarehoouse.AllocationGetDeptNoFailed";
        String ACTION_ALLOCATION_GET_NEW_DOC_NO_ACTION = "com.macauto.MacautoWarehoouse.AllocationGetNewDocNoAction";
        String ACTION_ALLOCATION_GET_NEW_DOC_NO_SUCCESS = "com.macauto.MacautoWarehoouse.AllocationGetNewDocNoSuccess";
        String ACTION_ALLOCATION_GET_NEW_DOC_NO_FAILED = "com.macauto.MacautoWarehoouse.AllocationGetNewDocNoFailed";
        String ACTION_ALLOCATION_INSERT_TT_IMN_FILE_NO_TLF_NO_IMG_ACTION = "com.macauto.MacautoWarehoouse.AllocationInsertTTImnFileNoTlfNoImgAction";
        String ACTION_ALLOCATION_INSERT_TT_IMN_FILE_NO_TLF_NO_IMG_YES = "com.macauto.MacautoWarehoouse.AllocationInsertTTImnFileNoTlfNoImgYes";
        String ACTION_ALLOCATION_INSERT_TT_IMN_FILE_NO_TLF_NO_IMG_NO = "com.macauto.MacautoWarehoouse.AllocationInsertTTImnFileNoTlfNoImgNo";
        String ACTION_ALLOCATION_INSERT_TT_IMN_FILE_NO_TLF_NO_IMG_FAILED = "com.macauto.MacautoWarehoouse.AllocationInsertTTImnFileNoTlfNoImgFailed";


        String ACTION_GET_PART_NO_NEED_SCAN_STATUS_ACTION = "com.macauto.MacautoWarehoouse.AllocationGetPartNoNeedScanStatusAction";
        String ACTION_GET_PART_NO_NEED_SCAN_STATUS_YES = "com.macauto.MacautoWarehoouse.AllocationGetPartNoNeedScanStatusYes";
        String ACTION_GET_PART_NO_NEED_SCAN_STATUS_NO = "com.macauto.MacautoWarehoouse.AllocationGetPartNoNeedScanStatusNo";
        String ACTION_GET_PART_NO_NEED_SCAN_STATUS_FAILED = "com.macauto.MacautoWarehoouse.AllocationGetPartNoNeedScanStatusFailed";

        //barcode
        String ACTION_ALLOCATION_GET_LOT_CODE_ACTION = "com.macauto.MacautoWarehoouse.AllocationGetLotCodeAction";
        String ACTION_ALLOCATION_GET_LOT_CODE_EMPTY = "com.macauto.MacautoWarehoouse.AllocationGetLotCodeEmpty";
        String ACTION_ALLOCATION_GET_LOT_CODE_SUCCESS = "com.macauto.MacautoWarehoouse.AllocationGetLotCodeSuccess";
        String ACTION_ALLOCATION_GET_LOT_CODE_FAILED = "com.macauto.MacautoWarehoouse.AllocationGetLotCodeFailed";
        //swipe layout
        String ACTION_ALLOCATION_SWIPE_LAYOUT_UPDATE = "com.macauto.MacautoWarehoouse.AllocationSwipeLayoutUpdate";
        String ACTION_ALLOCATION_SWIPE_LAYOUT_DELETE_ROW = "com.macauto.MacautoWarehoouse.AllocationSwipeLayoutDeleteRow";
        //delete
        String ACTION_ALLOCATION_SEND_MSG_DELETE_ITEM_CONFIRM = "com.macauto.MacautoWarehoouse.AllocationSendMsgDeleteItemConfirm";
        String ACTION_ALLOCATION_MSG_DETAIL_DELETE_ITEM_CONFIRM = "com.macauto.MacautoWarehoouse.AllocationMsgDetailDeleteItemConfirm";


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

        //Production Storage
        String ACTION_CHECK_TT_PRODUCT_ENTRY_ALREADY_CONFIRM_ACTION = "com.macauto.MacautoWarehoouse.CheckTTProductEntryAlreadyConfirmAction";
        String ACTION_CHECK_TT_PRODUCT_ENTRY_ALREADY_CONFIRM_FAILED = "com.macauto.MacautoWarehoouse.CheckTTProductEntryAlreadyConfirmFailed";
        String ACTION_CHECK_TT_PRODUCT_ENTRY_ALREADY_CONFIRM_YES = "com.macauto.MacautoWarehoouse.CheckTTProductEntryAlreadyConfirmYes";
        String ACTION_CHECK_TT_PRODUCT_ENTRY_ALREADY_CONFIRM_NO = "com.macauto.MacautoWarehoouse.CheckTTProductEntryAlreadyConfirmNo";

        String ACTION_GET_TT_PRODUCT_ENTRY_ACTION = "com.macauto.MacautoWarehoouse.GetTTProductEntryAction";
        String ACTION_GET_TT_PRODUCT_ENTRY_FAILED = "com.macauto.MacautoWarehoouse.GetTTProductEntryFailed";
        String ACTION_GET_TT_PRODUCT_ENTRY_SUCCESS = "com.macauto.MacautoWarehoouse.GetTTProductEntrySuccess";
        String ACTION_GET_TT_PRODUCT_ENTRY_EMPTY = "com.macauto.MacautoWarehoouse.GetTTProductEntryEmpty";

        String ACTION_PRODUCT_CHECK_STOCK_LOCATE_NO_EXIST_ACTION = "com.macauto.MacautoWarehoouse.ProductCheckStockLocateNoExistAction";
        String ACTION_PRODUCT_CHECK_STOCK_LOCATE_NO_EXIST_FAILED = "com.macauto.MacautoWarehoouse.ProductCheckStockLocateNoExistFailed";
        String ACTION_PRODUCT_CHECK_STOCK_LOCATE_NO_EXIST_YES = "com.macauto.MacautoWarehoouse.ProductCheckStockLocateNoExistYes";
        String ACTION_PRODUCT_CHECK_STOCK_LOCATE_NO_EXIST_NO = "com.macauto.MacautoWarehoouse.ProductCheckStockLocateNoExistNo";

        String ACTION_PRODUCT_UPDATE_TT_PRODUCT_ENTRY_LOCATE_NO_ACTION = "com.macauto.MacautoWarehoouse.ProductUpdateTTProductEntryLocateNoAction";
        String ACTION_PRODUCT_UPDATE_TT_PRODUCT_ENTRY_LOCATE_NO_FAILED = "com.macauto.MacautoWarehoouse.ProductUpdateTTProductEntryLocateNoFailed";
        String ACTION_PRODUCT_UPDATE_TT_PRODUCT_ENTRY_LOCATE_NO_SUCCESS = "com.macauto.MacautoWarehoouse.ProductUpdateTTProductEntryLocateNoSuccess";

        String ACTION_PRODUCT_IN_STOCK_WORK_COMPLETE = "com.macauto.MacautoWarehoouse.ProductInStockWorkComplete";
        String ACTION_PRODUCT_SWIPE_LAYOUT_UPDATE = "com.macauto.MacautoWarehoouse.ProductSwipeLayoutUpdate";
        String ACTION_PRODUCT_DELETE_ITEM_CONFIRM = "com.macauto.MacautoWarehoouse.ProductDeleteItemConfirm";

        String ACTION_GET_TT_ERROR_STATUS_ACTION = "com.macauto.MacautoWarehoouse.GetTTErrorStatusAction";
        String ACTION_GET_TT_ERROR_STATUS_FAILED = "com.macauto.MacautoWarehoouse.GetTTErrorStatusFailed";
        String ACTION_GET_TT_ERROR_STATUS_SUCCESS = "com.macauto.MacautoWarehoouse.GetTTErrorStatusSuccess";

        //Receiving Inspection
        String ACTION_RECEIVING_INSPECTION_GET_TT_RECEIVE_GOODS_REPORT_DATA_QC_ACTION = "com.macauto.MacautoWarehoouse.ReceivingInspectionGetTTReceiveGoodsReportDataQCAction";
        String ACTION_RECEIVING_INSPECTION_GET_TT_RECEIVE_GOODS_REPORT_DATA_QC_SUCCESS = "com.macauto.MacautoWarehoouse.ReceivingInspectionGetTTReceiveGoodsReportDataQCSuccess";
        String ACTION_RECEIVING_INSPECTION_GET_TT_RECEIVE_GOODS_REPORT_DATA_QC_EMPTY = "com.macauto.MacautoWarehoouse.ReceivingInspectionGetTTReceiveGoodsReportDataQCEmpty";
        String ACTION_RECEIVING_INSPECTION_GET_TT_RECEIVE_GOODS_REPORT_DATA_QC_FAILED = "com.macauto.MacautoWarehoouse.ReceivingInspectionGetTTReceiveGoodsReportDataQCFailed";

        String ACTION_RECEIVING_INSPECTION_GET_TT_REC_NO_IN_QC_ACTION = "com.macauto.MacautoWarehoouse.ReceivingInspectionGetTTRecNoInQCAction";
        String ACTION_RECEIVING_INSPECTION_GET_TT_REC_NO_IN_QC_YES = "com.macauto.MacautoWarehoouse.ReceivingInspectionGetTTRecNoInQCYes";
        String ACTION_RECEIVING_INSPECTION_GET_TT_REC_NO_IN_QC_NO = "com.macauto.MacautoWarehoouse.ReceivingInspectionGetTTRecNoInQCNo";
        String ACTION_RECEIVING_INSPECTION_GET_TT_REC_NO_IN_QC_COMPLETE = "com.macauto.MacautoWarehoouse.ReceivingInspectionGetTTRecNoInQCComplete";
        String ACTION_RECEIVING_INSPECTION_GET_TT_REC_NO_IN_QC_FAILED = "com.macauto.MacautoWarehoouse.ReceivingInspectionGetTTRecNoInQCFailed";

        String ACTION_RECEIVING_INSPECTION_EXECUTE_TT_OQCP_ACTION = "com.macauto.MacautoWarehoouse.ReceivingInspectionExecuteTTOQCPAction";
        String ACTION_RECEIVING_INSPECTION_EXECUTE_TT_OQCP_SUCCESS = "com.macauto.MacautoWarehoouse.ReceivingInspectionExecuteTTOQCPSuccess";
        String ACTION_RECEIVING_INSPECTION_EXECUTE_TT_OQCP_FAILED = "com.macauto.MacautoWarehoouse.ReceivingInspectionExecuteTTOQCPFailed";

        String ACTION_RECEIVING_INSPECTION_EXECUTE_TT_PRG_A_ACTION = "com.macauto.MacautoWarehoouse.ReceivingInspectionExecuteTTPrgAAction";
        String ACTION_RECEIVING_INSPECTION_EXECUTE_TT_PRG_A_SUCCESS = "com.macauto.MacautoWarehoouse.ReceivingInspectionExecuteTTPrgASuccess";
        String ACTION_RECEIVING_INSPECTION_EXECUTE_TT_PRG_A_FAILED = "com.macauto.MacautoWarehoouse.ReceivingInspectionExecuteTTPrgAFailed";

        String ACTION_RECEIVING_INSPECTION_DELETE_QC_TEMP_ACTION = "com.macauto.MacautoWarehoouse.ReceivingInspectionDeleteQCTempAction";
        String ACTION_RECEIVING_INSPECTION_DELETE_QC_TEMP_SUCCESS = "com.macauto.MacautoWarehoouse.ReceivingInspectionDeleteQCTempSuccess";
        String ACTION_RECEIVING_INSPECTION_DELETE_QC_TEMP_FAILED = "com.macauto.MacautoWarehoouse.ReceivingInspectionDeleteQCTempFailed";

        String ACTION_RECEIVING_INSPECTION_ITEM_SELECT_CHANGE = "com.macauto.MacautoWarehoouse.ReceivingInspectionItemSelectChange";


    }
}
