package com.byprogger.lab4

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

object Constants {
    const val DATABASE_NAME = "local_db.db"
    const val  DATABASE_VERSION = 1
    const val TABLE_NAME = "students"
    const val COLUMN_ID = "_id"
    const val COLUMN_FIO = "fio"
    const val COLUMN_CLASS_NAME = "className"
    const val COLUMN_CLASS_NUMBER = "classNumber"
    const val COLUMN_AVG_SCORE = "avgScore"
}

class SqlHelper(context: Context) : SQLiteOpenHelper(context, Constants.DATABASE_NAME, null,
    Constants.DATABASE_VERSION
) {

    private val createTableQuery = """
        CREATE TABLE ${Constants.TABLE_NAME} (
            ${Constants.COLUMN_ID} INTEGER PRIMARY KEY AUTOINCREMENT,
            ${Constants.COLUMN_FIO} TEXT NOT NULL,
            ${Constants.COLUMN_CLASS_NAME} TEXT NOT NULL,
            ${Constants.COLUMN_CLASS_NUMBER} INTEGER,
            ${Constants.COLUMN_AVG_SCORE} REAL
        )
    """.trimIndent()

    override fun onCreate(db: SQLiteDatabase?) {
        if (db == null) return

        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(
        db: SQLiteDatabase?,
        oldVersion: Int,
        newVersion: Int
    ) {
        if (db == null) return
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_NAME)
        onCreate(db)
    }

    fun addStudent(
        fio: String,
        className: String,
        classNumber: Int,
        avgScore: Float
    ) : Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(Constants.COLUMN_FIO, fio)
            put(Constants.COLUMN_CLASS_NAME, className)
            put(Constants.COLUMN_CLASS_NUMBER, classNumber)
            put(Constants.COLUMN_AVG_SCORE, avgScore)
        }

        val id = db.insert(Constants.TABLE_NAME, null, values)
        return id
    }

    fun getAllStudents() : List<Student> {
        val students = mutableListOf<Student>()

        val selectQuery = """
            SELECT * FROM ${Constants.TABLE_NAME}
        """.trimIndent()

        val db = readableDatabase

        val cursor: Cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()) {
            do {
                val id: Int = cursor.getInt(cursor.getColumnIndexOrThrow(Constants.COLUMN_ID))
                val fio: String = cursor.getString(cursor.getColumnIndexOrThrow(Constants.COLUMN_FIO))
                val className: String = cursor.getString(cursor.getColumnIndexOrThrow(Constants.COLUMN_CLASS_NAME))
                val classNumber: Int = cursor.getInt(cursor.getColumnIndexOrThrow(Constants.COLUMN_CLASS_NUMBER))
                val avgScore: Float = cursor.getFloat(cursor.getColumnIndexOrThrow(Constants.COLUMN_AVG_SCORE))

                val student = Student(id = id, fio = fio, className = className, classNumber = classNumber, avgScore = avgScore)

                students.add(student)
            } while (cursor.moveToNext())
        }

        cursor.close()

        return students
    }

    fun updateStudent(student: Student) : Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(Constants.COLUMN_FIO, student.fio)
            put(Constants.COLUMN_CLASS_NAME, student.className)
            put(Constants.COLUMN_CLASS_NUMBER, student.classNumber)
            put(Constants.COLUMN_AVG_SCORE, student.avgScore)
        }

        return db.update(Constants.TABLE_NAME, values, Constants.COLUMN_ID + " = ?", arrayOf<String>(student.id.toString()))
    }

    fun deleteStudent(id: Int) {
        val db = writableDatabase

        db.delete(Constants.TABLE_NAME, Constants.COLUMN_ID + " = ?", arrayOf<String>(id.toString()))
    }
}