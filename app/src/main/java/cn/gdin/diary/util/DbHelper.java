package cn.gdin.diary.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * 数据库操作工具类
 * 
 * @author 敬
 * 
 */
public class DbHelper extends SQLiteOpenHelper {

	// 数据库名
	public final static String DATABASE_NAME = "MyData.db";
	// 数据库版本
	public static int DATABASE_VERSION = 1;
	// 表名
	public final static String TABLE_NAME = "diary";
	// 表中的字段
	public final static String DIARY_ID = "id";
	public final static String DIARY_DATE = "date";
	public final static String DIARY_WEEK = "week";
	public final static String DIARY_TITLE = "title";
	public final static String DIARY_CONTENT = "content";

	public DbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// 创建表
	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
				+ DIARY_ID + " INTEGER primary key autoincrement, "
				+ DIARY_DATE + " text, " + DIARY_WEEK + " text," + DIARY_TITLE
				+ " text," + DIARY_CONTENT + " text);";
		db.execSQL(sql);
	}

	/**
	 * 数据库升级时调用 删除数据库中原有的user表，并重新创建新user表
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
		db.execSQL(sql);
		onCreate(db);
	}

	
}
