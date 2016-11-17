package cn.gdin.diary;

import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.gdin.diary.domain.DiaryItem;
import cn.gdin.diary.util.Util;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 自定义ListView适配器
 * @author king
 *
 */
public class MyAdapter extends BaseAdapter {
	private Context context;
	List<DiaryItem> items = null;

	public MyAdapter(List<DiaryItem> items, Context context) {
		this.items = items;
		this.context = context;
	}

	public int getCount() {
		return this.items.size();
	}

	public Object getItem(int paramInt) {
		return null;
	}

	public long getItemId(int paramInt) {
		return paramInt;
	}

	public View getView(int paramInt, View paramView, ViewGroup paramViewGroup) {
		if (paramView == null) {
			LayoutInflater localLayoutInflater = (LayoutInflater)context.getSystemService("layout_inflater");
            paramView = localLayoutInflater.inflate(R.layout.list_item, paramViewGroup, false);
            localLayoutInflater.inflate(R.layout.list_item, paramViewGroup, false);
		}

		TextView paramTitle = (TextView) paramView
				.findViewById(R.id.list_diary_title);
		TextView paramDate = (TextView) paramView
				.findViewById(R.id.list_diary_date);

		DiaryItem localDiaryItems = (DiaryItem) this.items.get(paramInt);
		paramTitle.setText(localDiaryItems.getTitle());
		paramDate.setText(localDiaryItems.getDate());

		return paramView;
	}
}
