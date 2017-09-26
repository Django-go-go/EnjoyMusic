/*
 * Copyright (C) 2013 Andreas Stuetz <andreas.stuetz@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.administrator.httpdemo.CustomView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Typeface;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.httpdemo.R;
import com.example.administrator.httpdemo.Utils.DensityUtils;
import com.example.administrator.httpdemo.Utils.ImageUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PagerSlidingTabStrip extends RelativeLayout {
	private Context mContext;
	private List<String> titles = new ArrayList<>();
	private List<Integer> icons = new ArrayList<>();


	private final PageListener pageListener = new PageListener();
	public OnPageChangeListener delegatePageListener;

	private ViewPager pager;

	private boolean isExpand = false;

	private int tabCount = 0;

	private int currentPosition = 0;
	private int lastPosition = 0;
	private float currentPositionOffset = 0f;

	private Paint rectPaint;
	private Paint dividerPaint;

	private int indicatorColor = 0xFF666666;
	private int underlineColor = 0x1A066060;
	private int dividerColor = 0x1A000000;

	private boolean shouldExpand = false;

	private int scrollOffset = 52;
	private int indicatorHeight = 8;
	private int underlineHeight = 6;
	private int dividerPadding = 12;

	private int tabPadding = 12;
	private int dividerWidth = 1;

	private int tabTextSize = 12;
	private int tabTextColor = 0xFF666666;

	private Typeface tabTypeface = null;
	private int tabTypefaceStyle = Typeface.BOLD;

	public PagerSlidingTabStrip(Context context) {
		this(context, null);
	}

	public PagerSlidingTabStrip(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public PagerSlidingTabStrip(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		mContext = context;
		setWillNotDraw(false);
//		DisplayMetrics dm = getResources().getDisplayMetrics();
//		scrollOffset = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, scrollOffset, dm);
//		indicatorHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, indicatorHeight, dm);
//		underlineHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, underlineHeight, dm);
//		dividerPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dividerPadding, dm);
//		tabPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, tabPadding, dm);
//		dividerWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dividerWidth, dm);
//		tabTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, tabTextSize, dm);

		rectPaint = new Paint();
		rectPaint.setAntiAlias(true);
		rectPaint.setStyle(Style.FILL);

		dividerPaint = new Paint();
		dividerPaint.setAntiAlias(true);
        dividerPaint.setColor(Color.RED);
		dividerPaint.setStrokeWidth(dividerWidth);

	}

	public void setViewPager(ViewPager pager) {
		this.pager = pager;

		if (pager.getAdapter() == null) {
			System.out.println("setViewPager");
			throw new IllegalStateException("ViewPager does not have adapter instance.");
		}

		currentPosition = pager.getCurrentItem();

		pager.addOnPageChangeListener(pageListener);

		notifyDataSetChanged();
	}

	public void setOnPageChangeListener(OnPageChangeListener listener) {
		this.delegatePageListener = listener;
	}

	public void notifyDataSetChanged() {
		removeAllViews();

		tabCount = pager.getAdapter().getCount();
		for (int i = 0; i < tabCount; i++) {
			if(titles.size() == tabCount){
				addTab(i, 0, titles.get(i));
			}

			if(icons.size() == tabCount){
				addTab(i, icons.get(i), null);
			}

		}
	}

	public boolean isExpand() {
		return isExpand;
	}

	public void setExpand(boolean expand) {
		isExpand = expand;
	}

	public void addTextTab(String[] title){
		for (int i = 0; i < title.length; i++){
			titles.add(title[i]);
		}
	}
	public void addIconTab(Integer[] icon){
		for (int i = 0; i < icon.length; i++){
			icons.add(icon[i]);
		}
	}
	private void addTab(final int position, int resId, String title) {
		TextView textView = null;
		ImageView imageButton = null;
		if(title != null){
			textView = new TextView(getContext());
			textView.setText(title);
			textView.setTextSize(16);
			textView.setTextColor(Color.DKGRAY);
			textView.setGravity(Gravity.CENTER);
		}
		if(resId != 0){
			imageButton = new ImageButton(getContext());
			Bitmap bitmap = ImageUtils.zoomBitmap(ImageUtils.drawableToBitmap(mContext.getDrawable(resId)),
					DensityUtils.dp2px(mContext, 60), DensityUtils.dp2px(mContext, 60));
			imageButton.setImageBitmap(shadowBitmap(bitmap));
			imageButton.setClickable(false);
			imageButton.setEnabled(false);
			imageButton.setBackgroundResource(R.color.red);
		}


		LinearLayout tab = new LinearLayout(mContext);
		tab.setOrientation(LinearLayout.VERTICAL);
		if(imageButton != null){
			tab.addView(imageButton, 0, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			tab.setPadding(tabPadding, 0, tabPadding, 0);
		}
		if(textView != null){
			tab.addView(textView, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			tab.setPadding(tabPadding, tabPadding, tabPadding, tabPadding);
		}

		tab.setGravity(Gravity.CENTER);
		tab.setFocusable(true);
		tab.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pager.setCurrentItem(position, true);
			}
		});
		addView(tab, position, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT));
		invalidate();
	}

	private Bitmap whiteBitmap(Bitmap bitmap){
        float[] colorArray=new float[]{
                -1,0,0,0,255,//R
                0,-1,0,0,255,//G
                0,0,-1,0,255,//B
                0,0,0,1f,0   //A
        };

        return ImageUtils.createColorBitmap(bitmap, colorArray);
    }

    private Bitmap shadowBitmap(Bitmap bitmap){
        float[] colorArray=new float[]{
                -1,0,0,0,255,//R
                0,-1,0,0,255,//G
                0,0,-1,0,255,//B
                0,0,0,0.5f,0   //A
        };

        return ImageUtils.createColorBitmap(bitmap, colorArray);
    }

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		notifyLayout();
	}
	private void notifyLayout(){
		if(getChildCount() == tabCount && tabCount != 0){
			if(isExpand){
				int mid = getMeasuredWidth()/tabCount;
				for(int i = 0; i < tabCount; i++){
					getChildAt(i).layout(i*mid, getPaddingTop(),
							(i+1) * mid, getPaddingTop() + getMeasuredHeight());
				}
			} else {
				int mid = getMeasuredWidth()/tabCount;
				getChildAt(0).layout(mid-getChildAt(0).getMeasuredWidth() ,
						getPaddingTop(), mid, getPaddingTop()+getMeasuredHeight());
				getChildAt(1).layout(mid , getPaddingTop(),
						mid+getChildAt(1).getMeasuredWidth(), getPaddingTop()+getMeasuredHeight());
			}
		}
	}

	int curPos = 0;
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		if (isInEditMode() || tabCount == 0) {
			return;
		}

		if (currentPosition <= (getChildCount() - 1)) {

			ViewGroup selectedTab = (ViewGroup) this.getChildAt(currentPosition);
			ViewGroup tv_Tab = (ViewGroup) this.getChildAt(curPos);
			if(titles.size() == tabCount){

				float x = tv_Tab.getLeft() + currentPositionOffset*(getMeasuredWidth()/tabCount);
				float y = getMeasuredHeight();
				canvas.drawRect(x, y-6, x + getMeasuredWidth()/tabCount, y, dividerPaint);


				for(int i = 0; i < tabCount; i++){
					if(i == currentPosition){
						TextView tv = (TextView)selectedTab.getChildAt(0);
						tv.setTextColor(Color.RED);
					} else {
						ViewGroup tab = (ViewGroup) this.getChildAt(i);
						TextView tv = (TextView)tab.getChildAt(0);
						tv.setTextColor(Color.DKGRAY);
					}

				}
			}

            if(icons.size() == tabCount && tabCount != 0){
				for(int i = 0; i < tabCount; i++){
					ViewGroup tab = (ViewGroup) this.getChildAt(i);
					if(i != currentPosition){
						Bitmap bitmap = ImageUtils.zoomBitmap(ImageUtils.drawableToBitmap(mContext.getDrawable(icons.get(i))),
								DensityUtils.dp2px(mContext, 60), DensityUtils.dp2px(mContext, 60));
						((ImageButton)tab.getChildAt(0)).setImageBitmap(shadowBitmap(bitmap));
					}else {
						Bitmap bitmap = ImageUtils.zoomBitmap(ImageUtils.drawableToBitmap(mContext.getDrawable(icons.get(i))),
								DensityUtils.dp2px(mContext, 60), DensityUtils.dp2px(mContext, 60));
						((ImageButton)tab.getChildAt(0)).setImageBitmap(bitmap);
					}

				}

            }
		}
	}

	private class PageListener implements OnPageChangeListener {

		@Override
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
			if (positionOffset >= 0f)
				currentPositionOffset = positionOffset;
			curPos = position;
			invalidate();
			if (delegatePageListener != null) {
				delegatePageListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
			}
		}

		@Override
		public void onPageScrollStateChanged(int state) {

			if (delegatePageListener != null) {
				delegatePageListener.onPageScrollStateChanged(state);
			}
		}

		@Override
		public void onPageSelected(int position) {

			currentPosition = position;
			invalidate();

			if (delegatePageListener != null) {
				delegatePageListener.onPageSelected(position);
			}
		}

	}

	@Override
	public void onRestoreInstanceState(Parcelable state) {
		SavedState savedState = (SavedState) state;
		super.onRestoreInstanceState(savedState.getSuperState());
		currentPosition = savedState.currentPosition;
		requestLayout();
	}

	@Override
	public Parcelable onSaveInstanceState() {
		Parcelable superState = super.onSaveInstanceState();
		SavedState savedState = new SavedState(superState);
		savedState.currentPosition = currentPosition;
		return savedState;
	}

	private static class SavedState extends BaseSavedState {
		int currentPosition;

		public SavedState(Parcelable superState) {
			super(superState);
		}

		private SavedState(Parcel in) {
			super(in);
			currentPosition = in.readInt();
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			super.writeToParcel(dest, flags);
			dest.writeInt(currentPosition);
		}

		public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
			@Override
			public SavedState createFromParcel(Parcel in) {
				return new SavedState(in);
			}

			@Override
			public SavedState[] newArray(int size) {
				return new SavedState[size];
			}
		};
	}

}
