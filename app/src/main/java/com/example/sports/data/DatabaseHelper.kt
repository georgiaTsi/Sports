package com.example.sports.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context:  Context) : SQLiteOpenHelper(context,  "favorites_database. db" ,  null, 1) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL( "CREATE TABLE favorites_sports_table (" + "_id INTEGER PRIMARY KEY AUTOINCREMENT," + "name TEXT)" )
        db.execSQL( "CREATE TABLE favorites_events_table (" + "_id INTEGER PRIMARY KEY AUTOINCREMENT," + "name TEXT)" )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion:Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS favorites_sports_table")
        db.execSQL("DROP TABLE IF EXISTS favorites_events_table")
        onCreate(db)
    }

    fun addFavoriteSport(sportName:  String) {
        addFavorite("favorites_sports_table", sportName)
    }

    fun removeFavoriteSport( sportName:  String) {
        removeFavorite("favorites_sports_table", sportName)
    }

    fun getAllFavoriteSports( )  : List<String> {
        return getAll("favorites_sports_table")
    }

    fun addFavoriteEvent(eventName: String) {
        addFavorite("favorites_events_table", eventName)
    }

    fun removeFavoriteEvent(eventName: String) {
        removeFavorite("favorites_events_table", eventName)
    }

    fun getAllFavoriteEvents( )  : List<String> {
        return getAll("favorites_events_table")
    }

    fun addFavorite(table:String, name: String) {
        val db = writableDatabase
        val values = ContentValues().apply { put("name", name) }
        db.insert(table,null, values)
    }

    fun removeFavorite(table: String, name: String) {
        val db = writableDatabase
        db.delete(table,  "name = ?", arrayOf(name))
    }

    fun getAll(table: String)  : List<String> {
        val db = readableDatabase
        val sportList = mutableListOf<String> ( )
        val cursor = db.query(table ,  null, null, null, null, null, null)

        while (cursor.moveToNext() )  {
            val sportName = cursor.getString(cursor. getColumnIndexOrThrow( "name" ) )
            sportList.add(sportName)
        }

        cursor.close()
        return sportList
    }
}