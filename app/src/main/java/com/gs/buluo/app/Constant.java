package com.gs.buluo.app;

import android.os.Environment;

/**
 * Created by admin on 2016/11/1.
 */
public class Constant {
    public static final String APK_URL = "http://buluo-gs-files.oss-cn-beijing.aliyuncs.com/packages/auto-updates/app.tribalc.apk";
    public static final String DIR_PATH = Environment.getExternalStorageDirectory().toString() + "/tribe/";
    public static final String DEPARTMENT_NAME = "department_name";
    public static final String DEPARTMENT_NUMBER = "department_number";
    public static final int WITHHOLD_UPDATE = 2;
    public static final String DEPARTMENT_SN = "department_sn";
    public static final String RENT_PAYED_NUM = "num";
    public static final String RENT_APARTMENT_CODE = "code";
    public static final String RENT_APARTMENT_NAME = "name";
    public static final String RENT_PROTOCOL_ID = "protocolId";
    public static final String DISCOUNT_INFO = "discount_info";
    public static final String STORE_NAME = "STORE_NAME";
    public static final String CREDIT_BILL = "credit_bill";
    public static final String CREDIT_BALANCE = "credit_balance";
    public static final String CREDIT_BILL_ID = "credit_bill_id";
    public static final String COMPANY_ID = "company_id";
    public static final String TARGET_ID = "target_id";
    public static final String REPAY_TITLE = "repay_title";
    public static final String WEB_TYPE = "web_type";
    public static final String WX_CODE = "wx_code";
    public static final String ORDER_ID = "order_id";
    public static final String DETAIL_HEADER_IMAGE = "detail_head";
    public static final String DETAIL_TITLE = "detail_title";
    public static final String GOODS_PIC = "main_pic";
    public static final String NEWORDER_TYPE = "neworder_type";
    public static final String RENT_ADD_WITHHOLD_FLAG = "rent_add_withhold_flag";
    public static final String BALANCE = "wallet_balance";
    public static final String BOARDROOM_ALERT_TIME = "boardroom_alert_time";
    public static final String ROOM_FILTER = "room_filter";
    public static final String CONFERENCE_ROOM = "conference_room";
    public static final String CONTACTS_DATA = "contacts_data";
    public static final String BOARDROOM_BEGIN_TIME = "boardroom_begin_time";
    public static final String BOARDROOM_END_TIME = "boardroom_end_time";
    public static final String BOARD_RESERVE_ID = "boardroom_serve_id";
    public static final String CONTACT_FLAG = "contact_flag";
    public static final String BOARD_RESERVE_FLAG = "boardroom_reserve_flag";
    public static final String BILL_TYPE_FLAG ="bill_type_flag";
    public static final String WARFARE = "warfare";
    public static final String CONTACT_DATA = "contact_data";

    public final static class Base {
        public static final String BASE = "https://app-services.buluo-gs.com/";
        //        public static final String BASE_URL="https://app-services.buluo-gs.com:443/tribalc/v1.0/";
        public static final String BASE_URL = BuildConfig.API_SERVER_URL;
        //        public static final String BASE_URL="http://123.56.251.131:10086/trib.alc/v1.0/";
        public static final String BASE_IMG_URL = "http://dev-app-services.buluo-gs.com/resources/";   //图片地址要加此前缀
        public static final String BASE_ALI_URL = "http://pictures.buluo-gs.com/";  //阿里云图片地址base
        public static final String WX_ID = "wx070832ea777c3ec0";
    }


    public static final String WEB_URL = "web_url";
    public static final String APP_START = "app_start";
    public static final String SIGN_IN = "sign_in";
    public static final String VERIFICATION = "verificationCode";
    public static final String DOOR = "door";
    public static final String VCODE = "vcode";
    public static final String CANCEL_UPDATE_VERSION = "CANCEL_UPDATE_VERSION";
    public static final String DOOR_LIST = "door_list";
    public static final String EQUIP_LIST = "equip_list";
    public static final String LAST_ITEM = "last_item1";
    public static final String RE_LOGIN = "re_login";
    public static final String NICKNAME = "nickname";
    public static final String SEX = "sex";
    public static final String BIRTHDAY = "birthday";
    public static final String EMOTION = "emotion";
    public static final String PROVINCE = "province";
    public static final String CITY = "city";
    public static final String DISTRICT = "district";
    public static final String AREA = "province,city,district";
    public static final String PICTURE = "picture";
    public static final String PHONE = "phone";
    public static final String MALE = "MALE";
    public static final String FEMALE = "FEMALE";
    public static final String SINGLE = "SINGLE";
    public static final String MARRIED = "MARRIED";
    public static final String LOVE = "LOVE";
    public static final String ADDRESS = "address";
    public static final String ADDRESS_ID = "address_id";
    public static final String WALLET_PWD = "wallet_pwd";
    public static final String WALLET = "wallet";
    public static final String OLD_PWD = "old_pwd";
    public static final String POUNDAGE = "poundage";

    public static final String BANK_LIMIT = "bank_limit";
    public static final String BANK_TYPE = "BANK_TYPE";
    public static final String WALLET_AMOUNT = "wallet_amount";
    public static final String CASH_FLAG = "cash_flag";
    public static final String BANK_CARD = "bank_card";
    public static final String BILL = "bill_entity";
    public static final String GOODS_ID = "goods_id";
    public static final String ORDER = "order";
    public static final String DEPARTMENT_ID = "department_id";
    public static final String ORDER_STATUS = "status";
    public static final int REQUEST_ADDRESS = 208;
    public static final String RECEIVER = "receiver";
    public static final String CONTRACT = "contract";

    public static final String PRICE = "price";
    public static final String TYPE = "type";
    public static final String COMMUNITY_ID = "community_id";
    public static final String COMMUNITY_NAME = "community_name";
    public static final String PROPERTY_MANAGEMENT = "property_management";


    public static final String REPAST = "repast";
    public static final String ENTERTAINMENT = "entertainment";
    public static final String ENTERTAINMENT_ALL = "HAIRDRESSING,FITNESS,ENTERTAINMENT,KEEPHEALTHY";
    public static final String SORT_POPULAR = "popularValue,desc";
    public static final String SORT_PERSON_EXPENSE_DESC = "personExpense,desc";
    public static final String SORT_PERSON_EXPENSE_ASC = "personExpense,asc";
    public static final String SORT_COORDINATE_DESC = "coordinate,desc";
    public static final String SERVE_ID = "serve_id";
    public static final String STORE_ID = "store_id";


    public final static class ForIntent {
        public static final int REQUEST_CODE = 0;
        public static final int REQUEST_CODE_RENT_ADD_WITHHOLD = 2;
        public static final int RESULT_CODE = 1;
        public static final String FLAG = "flag";
        public static final String FROM_ORDER = "fromOrder";
        public static final String COMPANY_FLAG = "company_info";
        public static final String PROPERTY_BEEN = "property_been";
        public static final String MODIFY = "self_modify";
        public static final String COORDINATE = "coordinate";
        public static final String FANCILITY = "facility";
        public static final String SERVE_POSITION = "serve_position";
        public static final int REQUEST_CODE_BOARDROOM_ALERT = 3;
        public static final int REQUEST_CODE_BOARDROOM_RESERVE_TIME = 4;
        public static final int REQUEST_CODE_BOARDROOM_PARTICIPANT = 5;
        public static final int REQUEST_CODE_BOARDROOM_PARTICIPANT_ADD = 6;
        public static final int REQUEST_CODE_BOARDROOM_PARTICIPANT_INPUT = 7;
    }

}
