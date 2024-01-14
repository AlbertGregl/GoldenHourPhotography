package hr.gregl.goldenhourphotography.dao

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import hr.gregl.goldenhourphotography.model.Item

private const val DB_NAME = "items.db"
private const val DB_VERSION = 1
private const val TABLE_NAME = "items"
private val CREATE_TABLE = "CREATE TABLE $TABLE_NAME (\n" +
        "${Item::_id.name} INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
        "${Item::date.name} TEXT NOT NULL,\n" +
        "${Item::sunrise.name} TEXT NOT NULL,\n" +
        "${Item::sunset.name} TEXT NOT NULL,\n" +
        "${Item::firstLight.name} TEXT NOT NULL,\n" +
        "${Item::lastLight.name} TEXT NOT NULL,\n" +
        "${Item::dawn.name} TEXT NOT NULL,\n" +
        "${Item::dusk.name} TEXT NOT NULL,\n" +
        "${Item::solarNoon.name} TEXT NOT NULL,\n" +
        "${Item::goldenHour.name} TEXT NOT NULL,\n" +
        "${Item::dayLength.name} TEXT NOT NULL,\n" +
        "${Item::timezone.name} TEXT NOT NULL,\n" +
        "${Item::utcOffset.name} INTEGER NOT NULL\n" +
        ")"
private const val DROP_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"

class TimeSqlHelper(context: Context?) : SQLiteOpenHelper(
    context, DB_NAME, null, DB_VERSION
), Repository {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(DROP_TABLE)
        onCreate(db)
    }

    override fun delete(selection: String?, selectionArgs: Array<String>?) =
        writableDatabase.delete(
            TABLE_NAME,
            selection,
            selectionArgs
        )

    override fun insert(values: ContentValues?) =
        writableDatabase.insert(
            TABLE_NAME,
            null,
            values
        )

    override fun query(
        projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor = readableDatabase.query(
        TABLE_NAME,
        projection,
        selection,
        selectionArgs,
        null,
        null,
        sortOrder
    )

    override fun update(
        values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ) = writableDatabase.update(
        TABLE_NAME,
        values,
        selection,
        selectionArgs
    )

}