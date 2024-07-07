package com.example.kaizen

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

    }

    fun addFavoriteSport(sportName:  String) {
        val db = writableDatabase
        val values = ContentValues().apply { put("name", sportName) }
        db.insert("favorites_sports_table" ,  null, values)
    }

    fun removeFavoriteSport( sportName:  String) {
        val db = writableDatabase
        db.delete("favorites_sports_table" ,  "name = ?", arrayOf(sportName))
    }

    fun getAllFavoriteSports( )  : List<String> {
        val db = readableDatabase
        val sportList = mutableListOf<String> ( )
        val cursor = db.query("favorites_sports_table" ,  null, null, null, null, null, null)

        while (cursor.moveToNext() )  {
            val sportName = cursor.getString(cursor. getColumnIndexOrThrow( "name" ) )
            sportList.add(sportName)
        }

        cursor.close()
        return sportList
    }

    fun addFavoriteEvent(eventName:  String) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("name", eventName)
        }

        db.insert("favorites_events_table", null, values)
    }

    fun removeFavoriteEvent( eventName:  String) {
        val db = writableDatabase
        db.delete("favorites_events_table" ,  "name = ?", arrayOf(eventName))
    }

    fun getAllFavoriteEvents( )  : List<String> {
        val db = readableDatabase
        val sportList = mutableListOf<String> ( )
        val cursor = db.query("favorites_events_table" ,  null, null, null, null, null, null)

        while (cursor.moveToNext() )  {
            val sportName = cursor.getString(cursor. getColumnIndexOrThrow( "name" ) )
            sportList.add(sportName)
        }

        cursor.close()
        return sportList
    }
}