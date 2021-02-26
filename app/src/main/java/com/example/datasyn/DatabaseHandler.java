package com.example.datasyn;


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "OfflineData";
    private static final String TABLE_CONTACTS = "contacts";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_ROLE = "role";
    private static final String KEY_LOCATION = "location";
    private static final String KEY_IMAGELINK = "imageLink";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," +
                KEY_NAME + " TEXT,"+
                KEY_EMAIL + " TEXT,"+
                KEY_ROLE + " TEXT,"+
                KEY_LOCATION + " TEXT,"+
                KEY_IMAGELINK + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);

        onCreate(db);
    }

    // code to add the new contact
    public void addContact(FormDataModal formDataModal) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, formDataModal.getName()); //  Name
        values.put(KEY_EMAIL, formDataModal.getEmail()); //  email
        values.put(KEY_ROLE, formDataModal.getRole()); //  role
        values.put(KEY_LOCATION, formDataModal.getCountry()); //  location
        values.put(KEY_IMAGELINK, formDataModal.getImageLink()); //  image

        // Inserting Row
        db.insert(TABLE_CONTACTS, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

}
