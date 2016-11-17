package cn.gdin.diary;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.gdin.diary.domain.DiaryItem;
import cn.gdin.diary.util.DbUtil;
import cn.gdin.diary.util.IAlertDialogButtonListener;
import cn.gdin.diary.util.MD5Util;
import cn.gdin.diary.util.MyApplication;
import cn.gdin.diary.util.Util;

public class ItemActivity extends Activity {

	private LinearLayout mLayoutSetting;

	private RelativeLayout mLayoutList;

	private ListView mListView;

	private Button mBtnSetPwd;
	private Button mBtnAdd;

	private EditText mSetPassword;
	private EditText mRepPassword;

	ArrayList<DiaryItem> items;

	MyAdapter myAdapter;
	DbUtil util;

	private String flag; // 标识是更新还是添加

	public final static String SER_KEY = "cn.gdin.diary.ser";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_all);

		MyApplication.getInstance().addActivity(this);

		initView();
		loadView();

		initData();

		// 添加日记
		addBtnOnClick();
	}

	// 显示listview内容
	private void initData() {
		util = new DbUtil(this);

		items = util.getAllData();

		myAdapter = new MyAdapter(items, this);

		mListView.setAdapter(myAdapter);
		// 点击查看详细内容
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long line) {
				String id = items.get(position).getId();
				String date = items.get(position).getDate();
				String week = items.get(position).getWeek();
				String title = items.get(position).getTitle();
				String content = items.get(position).getContent();
				// Log.i("my", id+":"+title+":"+content);
				flag = "1";

				DiaryItem mDiary = new DiaryItem();
				mDiary.setId(id);
				mDiary.setDate(date);
				mDiary.setWeek(week);
				mDiary.setTitle(title);
				mDiary.setContent(content);

				Intent intent = new Intent(ItemActivity.this,
						MainActivity.class);
				Bundle mBundle = new Bundle();
				mBundle.putSerializable(SER_KEY, mDiary);
				intent.putExtras(mBundle);
				intent.putExtra("flag", flag);
				startActivity(intent);
			}
		});
		// 长按弹出删除提示
		mListView
				.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {

					@Override
					public void onCreateContextMenu(ContextMenu arg0,
							View arg1, ContextMenuInfo arg2) {
						arg0.setHeaderTitle("是否删除");
						arg0.add(0, 0, 0, "删除");
						arg0.add(0, 1, 0, "取消");

					}
				});

	}

	// 提示菜单响应函数
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// item.getItemId()：点击了菜单栏里面的第几个项目");
		if (item.getItemId() == 0) {
			int selectedPosition = ((AdapterContextMenuInfo) item.getMenuInfo()).position;// 获取点击了第几行

			String id = items.get(selectedPosition).getId();
			// 删除日记
			util.delete(id);
			items.remove(items.get(selectedPosition));
			myAdapter.notifyDataSetChanged();

		}

		return super.onContextItemSelected(item);
	}

	// 初始化控件
	private void initView() {
		// 设置密码界面
		mLayoutSetting = (LinearLayout) findViewById(R.id.layout_setting);
		mSetPassword = (EditText) findViewById(R.id.et_set_pwd);// 设置密码文本框
		mBtnSetPwd = (Button) findViewById(R.id.btn_set_pwd_ok);// 确认设置密码
		mRepPassword = (EditText) findViewById(R.id.et_rep_pwd);

		// 我的日记界面
		mLayoutList = (RelativeLayout) findViewById(R.id.layout_list);
		mListView = (ListView) findViewById(R.id.listView);
		mBtnAdd = (Button) findViewById(R.id.add_diary);

	}

	// 密码不存在，显示设置密码
	private void loadView() {
		if (TextUtils.isEmpty(Util.loadData(this))) {
			mLayoutSetting.setVisibility(View.VISIBLE);
			SetBtnOnclick();
		}
	}

	// 设置密码
	private void SetBtnOnclick() {
		mBtnSetPwd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				String passwd = mSetPassword.getText().toString();
				String rePassword = mRepPassword.getText().toString();
				if (!passwd.equals(rePassword)) {
					Toast.makeText(ItemActivity.this, "两次密码不一致",
							Toast.LENGTH_SHORT).show();
				} else {
					// 加密
					String setPasswd = MD5Util.MD5(passwd);

					Util.savaData(ItemActivity.this, setPasswd);
					mLayoutList.setVisibility(View.VISIBLE);
					mLayoutSetting.setVisibility(View.GONE);
				}
			}
		});
	}

	// 跳转至添加新日记
	private void addBtnOnClick() {
		mBtnAdd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				flag = "0";
				Intent intent = new Intent();
				intent.setClass(ItemActivity.this, MainActivity.class);
				intent.putExtra("flag", flag);
				startActivity(intent);
				finish();
			}
		});
	}

	// 复写返回键功能
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			showConfirmDialog();
			return true;
		}
		return false;

	}

	// 结束进程
	private IAlertDialogButtonListener mBtnOkQuitClickListener = new IAlertDialogButtonListener() {

		@Override
		public void onClick() {
			// 关闭所有activity
			MyApplication.getInstance().exit();
			int id = android.os.Process.myPid();
			if (id != 0) {
				android.os.Process.killProcess(id);
			}
		}

	};

	// 弹出提示
	private void showConfirmDialog() {
		Util.showDialog(this, "是否退出？", mBtnOkQuitClickListener);
	}

}
