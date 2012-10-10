package com.luzi82.snotebackup;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;

public class MainActivity extends Activity implements MenuFragment.Callbacks {

	// private boolean mTwoPane;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

//		MenuFragment menu_fragment = (MenuFragment) getFragmentManager().findFragmentById(R.id.menu_fragment);

		// if (findViewById(R.id.item_detail_container) != null) {
		// mTwoPane = true;
		// ItemListFragment lhs = (ItemListFragment) getSupportFragmentManager()
		// .findFragmentById(R.id.item_list);
		// lhs.setActivateOnItemClick(true);
		// }
	}

	@Override
	public void onItemSelected(int id) {
		T_T.v("id "+id);
		// if (mTwoPane) {
		Fragment fragment = null;
		SNoteBackup.MenuItem item = SNoteBackup.MenuItem.values()[id];
		switch (item) {
		case SDCARD:
			fragment = new SdcardSetttingFragment();
			break;
		case ABOUT:
			fragment = new AboutFragment();
			break;
		default:
			fragment = null;
			break;
		}
		if (fragment != null) {
			FragmentTransaction ft = getFragmentManager().beginTransaction();
			ft.replace(R.id.item_detail_container, fragment);
			ft.commit();
		}
		// } else {
		// // Intent detailIntent = new Intent(this, ItemDetailActivity.class);
		// // detailIntent.putExtra(ItemDetailFragment.ARG_ITEM_ID, id);
		// // startActivity(detailIntent);
		// }
	}
}
