package com.bookkeeperfvm.android.constants;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class Constants {

    public final static String LOGO = "https://firebasestorage.googleapis.com/v0/b/bookkeeper-ke.appspot.com/o/General%2Fbookkeeping.png?alt=media&token=edefe635-27e4-43ad-aa8c-ddc28ee2ad82";
    public final static int NOTIFICATION_ID = 0;
    public final static String NOTIFICATION = "notification";
    public final static String CHANNEL_ID = "Bookkeeper";
    public final static String CHANNEL_NAME = "Bookkeeper";
    public final static String CHANNEL_DESCRIPTION = "Bookkeeper";
    public static final String PASSCODE = "PASSCODE";
    public static final String DEFAULT_PASSCODE = "0000";

    public final static long MONTH = 2628000000L;
    public final static long YEAR = 31557600000L;

    public final static String RWANDA = "Rwanda";
    public final static String MEMBERS = "members";
    public final static String CONTACTS = "contacts";
    public final static String RECORDS = "records";
    public final static String PROGRAMS = "programs";

    public final static String PIC = "pic";
    public final static String GENDER = "gender";
    public final static String NAME = "name";
    public final static String EMAIL = "email";
    public final static String PHONE_NUMBER = "phone_number";
    public final static String TOKEN = "token";
    public final static String LOCATION = "location";
    public final static String TAGS = "tags";
    public static String CURRENCY = "KES";

    public final static String KEY_STORE_REFERENCE = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

    public final static String ADDRESS = "address";
    public final static String VILLAGE = "village";
    public final static String CELL = "cell";
    public final static String SECTOR = "sector";
    public final static String DISTRICT = "district";
    public final static String PROVINCE = "province";
    public final static String COUNTRY = "country";

    public final static String DAILY = "Daily";
    public final static String WEEKLY = "Weekly";
    public final static String MONTHLY = "Monthly";
    public final static String YEARLY = "Yearly";

    public final static String OBJECT = "object";
    public final static String RECORD_TYPE = "recordType";
    public final static String OBJECT_ID = "objectID";
    public final static String OBJECT_LIST = "objectList";

    public final static String TOKEN_NOTIFICATION = "notifications";
    public final static String GROUPS_TOKENS = "grouptokens";

    public final static String USERS = "users";
    public final static String COUNT = "count";
    public final static String CATEGORY = "category";

    public final static String ACTIVITIES = "Activities";
    public final static String BUSINESSES = "Businesses";
    public final static String PRODUCTS = "Products";
    public final static String MENU = "Menu";

    public final static int ADMIN = 0;
    public final static int EMPLOYEE = 1;

    public final static String STOCK = "Stock";
    public final static String SALES = "Sales";
    public final static String EXPENSE = "Expenses";
    public final static String DEBT = "Debt";
    public final static String LOANS = "Loans";
    public final static String MOBILE_MONEY = "Mobile Money";
    public final static String METRICS = "Metrics";

    public final static String WALLET_ADDRESS = "address";
    public final static String PRIVATE_KEY = "privateKey";
    public final static String SEED_PHRASE = "seedPhrase";
    public final static String KEY_STORE = "keyStore";


}
