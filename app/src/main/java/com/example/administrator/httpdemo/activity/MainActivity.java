package com.example.administrator.httpdemo.activity;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.design.internal.NavigationMenuPresenter;
import android.support.design.internal.NavigationMenuView;
import android.support.design.widget.NavigationView;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.httpdemo.Data.entity.Mp3Info;
import com.example.administrator.httpdemo.Data.entity.MyUser;
import com.example.administrator.httpdemo.Data.entity.SongRecorder;
import com.example.administrator.httpdemo.Listener.DialogFragmentListener;
import com.example.administrator.httpdemo.Utils.GreenDaoUtils;
import com.example.administrator.httpdemo.Utils.OtherUtils;
import com.example.administrator.httpdemo.Utils.SharedPreferencesUtils;
import com.example.administrator.httpdemo.activity.base.BaseActivity;
import com.example.administrator.httpdemo.fragment.LocalFragment;
import com.example.administrator.httpdemo.fragment.NetWorkFragment;
import com.example.administrator.httpdemo.activity.presenter.MainPresenter;
import com.example.administrator.httpdemo.activity.view.MainView;
import com.example.administrator.httpdemo.CustomView.PagerSlidingTabStrip;
import com.example.administrator.httpdemo.R;
import com.example.administrator.httpdemo.Utils.DensityUtils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;

public class MainActivity extends BaseActivity<MainView, MainPresenter> implements
		MainView, DialogFragmentListener {

	private static final String TAG = "MainActivity";

	public static final int RESULT_OK = 1;
	public static final int REQUEST_OK = 2;

	private PagerSlidingTabStrip tabs;
	private ViewPager pager;
	private MyPagerAdapter adapter;

	private ImageView mImageView1, mImageView2;
	private TextView mTextView_username;
	private ImageView mImageView_userimage;
	private View headView;
	private DrawerLayout mDrawerLayout;
	private NavigationView mNavigationView;

	private Fragment[] fragment = {new LocalFragment(), new NetWorkFragment()};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.leftmenu);

		mPresenter = createPresenter();
		mPresenter.getMyUser();

		initView();
		initDrawerLayout();

		setPlayFragment(R.id.main_frame);
	}

	private void setNavigationMenuLineStyle(NavigationView navigationView, @ColorInt final int color, final int height) {
		try {
			Field fieldByPressenter = navigationView.getClass().getDeclaredField("mPresenter");
			fieldByPressenter.setAccessible(true);
			NavigationMenuPresenter menuPresenter = (NavigationMenuPresenter) fieldByPressenter.get(navigationView);
			Field fieldByMenuView = menuPresenter.getClass().getDeclaredField("mMenuView");
			fieldByMenuView.setAccessible(true);
			final NavigationMenuView mMenuView = (NavigationMenuView) fieldByMenuView.get(menuPresenter);
			mMenuView.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
				@Override
				public void onChildViewAttachedToWindow(View view) {
					RecyclerView.ViewHolder viewHolder = mMenuView.getChildViewHolder(view);
					if (viewHolder != null && "SeparatorViewHolder".equals(viewHolder.getClass().getSimpleName()) && viewHolder.itemView != null) {
						if (viewHolder.itemView instanceof FrameLayout) {
							FrameLayout frameLayout = (FrameLayout) viewHolder.itemView;
							View line = frameLayout.getChildAt(0);
							line.setBackgroundColor(color);
							line.getLayoutParams().height = height;
							line.setLayoutParams(line.getLayoutParams());
						}
					}
				}

				@Override
				public void onChildViewDetachedFromWindow(View view) {
				}
			});
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	private void initDrawerLayout(){
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

		mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
		mNavigationView.setItemIconTintList(null);
		setNavigationMenuLineStyle(mNavigationView, Color.LTGRAY, 20);

		headView = mNavigationView.getHeaderView(0);
		headView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivityForResult(new Intent(MainActivity.this, InfoActivity.class), REQUEST_OK);
			}
		});

		mTextView_username = (TextView) headView.findViewById(R.id.username);
		mImageView_userimage = (ImageView) headView.findViewById(R.id.userimage);

		mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
			@Override
			public boolean onNavigationItemSelected(@NonNull MenuItem item) {
				mDrawerLayout.closeDrawer(Gravity.START, false);
				switch (item.getItemId()){
					case R.id.logout:
						BmobUser.logOut();
						break;
					case R.id.change:
						BmobUser.logOut();
						startActivity(new Intent(MainActivity.this, LoginActivity.class));
						break;
					case R.id.recommend:
						break;
					case R.id.local:{
						Intent intent = new Intent(MainActivity.this, LocalActivity.class);
						Bundle bundle = new Bundle();
						bundle.putInt("position", 0);
						bundle.putString("title", "本地播放");
						intent.putExtras(bundle);
						startActivityForResult(intent, 1);
						break;
					}
					case R.id.recorder:{
						Intent intent = new Intent(MainActivity.this, LocalActivity.class);
						Bundle bundle = new Bundle();
						bundle.putInt("position", 1);
						bundle.putString("title", "播放记录");
						intent.putExtras(bundle);
						startActivityForResult(intent, 1);
						break;
					}
				}
				return true;
			}
		});
	}

	@Override
	public MainPresenter createPresenter() {
		return new MainPresenter(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		exeBindService();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		exeUnbindService();
	}

	private final Integer[] ICONS = {
			R.drawable.actionbar_music_selected,
			R.drawable.actionbar_discover_selected};

	private void initView(){
		mImageView1 = (ImageView) findViewById(R.id.swipe_image);
		mImageView2 = (ImageView) findViewById(R.id.set_image);
		mImageView1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mDrawerLayout.openDrawer(Gravity.START);
			}
		});
		mImageView2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this, SearchActivity.class));
			}
		});
		tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
		tabs.addIconTab(ICONS);

		pager = (ViewPager) findViewById(R.id.pager);
		adapter = new MyPagerAdapter(getSupportFragmentManager());
		pager.setAdapter(adapter);
		int pageMargin = DensityUtils.dp2px(this, 4);
		pager.setPageMargin(pageMargin);
		pager.setCurrentItem(1);

		tabs.setViewPager(pager);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_OK){
			if (RESULT_OK == resultCode){
				mPresenter.getMyUser();
			}
		}
	}

	@Override
	public void onBackPressed() {
		if (mDrawerLayout.isDrawerOpen(Gravity.START)) {
			mDrawerLayout.closeDrawer(Gravity.START);
		} else {
			super.onBackPressed();
		}
	}

	@Override
	public void createSongList(String name) {
		mListener.setValue(name);
	}

	@Override
	public void deleteSongList(int group, int child) {
		mListener.isDelete(group, child);
	}

	@Override
	public void getMyUser(final MyUser myUser) {

		mTextView_username.setText(myUser.getUsername());

		new Thread(new Runnable() {
			@Override
			public void run() {
				final Bitmap bitmap;
				try {
					bitmap = Picasso.with(MainActivity.this).load(myUser.getBackgroundID()).get();
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							headView.setBackground(new BitmapDrawable(bitmap));
						}
					});
				} catch (IOException e) {
					Log.e(InfoActivity.TAG, "run: ", e);;
				}
			}
		}).start();

		Picasso.with(this).load(myUser.getPhotoID()).transform(new Transformation() {
			@Override
			public Bitmap transform(Bitmap source) {
				int size = Math.min(source.getWidth(), source.getHeight());

				int width = (source.getWidth() - size) / 2;
				int height = (source.getHeight() - size) / 2;

				Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);

				Canvas canvas = new Canvas(bitmap);
				Paint paint = new Paint();
				BitmapShader shader =
						new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
				if (width != 0 || height != 0) {
					Matrix matrix = new Matrix();
					matrix.setTranslate(-width, -height);
					shader.setLocalMatrix(matrix);
				}
				paint.setShader(shader);
				paint.setAntiAlias(true);

				float r = size / 2f;
				canvas.drawCircle(r, r, r, paint);

				source.recycle();

				return bitmap;
			}

			@Override
			public String key() {
				return "circle";
			}
		}).into(mImageView_userimage);
	}

	private class MyPagerAdapter extends FragmentPagerAdapter {

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public int getCount() {
			return ICONS.length;
		}

		@Override
		public Fragment getItem(int position) {
			return fragment[position];
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
//			super.destroyItem(container, position, object);
		}
	}

	private ToLocalFragmentListener mListener;
	public void setListener(ToLocalFragmentListener listener) {
		mListener = listener;
	}
	public interface ToLocalFragmentListener{
		void setValue(String name);
		void isDelete(int group, int child);
	}


}