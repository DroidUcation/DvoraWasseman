package com.gfcommunity.course.gfcommunity.data;

import android.provider.BaseColumns;

/**
 * Sharing Information Contract : products, recipes and tips
 */
public class SharingInfoContract {

    //Products
        public  static  class ProductsEntry implements BaseColumns {
            public static  final String TABLE_NAME = "ProductsTBL";
            public static  final String PRODUCT_NAME = "product_name";
            public static  final String STORE_NAME = "store_name";
            public static  final String IMAGE = "image";
            public static  final String STORE_URL = "store_url";
            public static  final String CITY = "city";
            public static  final String STREET = "street";
            public static  final String HOUSE_NO = "house_no";
            public static  final String COMMENT = "comment";
            public static  final String PHONE = "phone";
            public static  final String CREATED_AT = "created_at";
            public static  final String IMAGE_URL = "img_url";
            public static final String USER_ID = "user_id";
    }
}
