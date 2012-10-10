package com.luzi82.snotebackup;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity implements
		MenuFragment.Callbacks {

	// private boolean mTwoPane;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// if (findViewById(R.id.item_detail_container) != null) {
		// mTwoPane = true;
		// ItemListFragment lhs = (ItemListFragment) getSupportFragmentManager()
		// .findFragmentById(R.id.item_list);
		// lhs.setActivateOnItemClick(true);
		// }
	}

	@Override
	public void onItemSelected(int id) {
		// if (mTwoPane) {
		// Fragment fragment = null;
		// SNoteBackup.MenuItem item = SNoteBackup.MenuItem.values()[id];
		// switch (item) {
		// case SDCARD:
		// fragment = new SdcardSetttingFragment();
		// default:
		// fragment = null;
		// }
		// // if (fragment != null) {
		// // getSupportFragmentManager().beginTransaction()
		// // .replace(R.id.item_detail_container, fragment).commit();
		// // }
		// } else {
		// // Intent detailIntent = new Intent(this, ItemDetailActivity.class);
		// // detailIntent.putExtra(ItemDetailFragment.ARG_ITEM_ID, id);
		// // startActivity(detailIntent);
		// }
	}
}
