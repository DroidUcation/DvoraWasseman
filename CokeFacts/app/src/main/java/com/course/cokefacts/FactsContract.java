package com.course.cokefacts;

import android.provider.BaseColumns;
import android.provider.ContactsContract;

/**
 * Facts Contract
 */
public class FactsContract {
    public  static  class FactsEntry implements BaseColumns{
        public static  final String TABLE_NAME="FactsTBL";
        public static  final String COLUMN_FACT="fact_text";

    }
}
