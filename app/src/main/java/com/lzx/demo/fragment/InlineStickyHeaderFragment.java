package com.lzx.demo.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.lzx.demo.ItemDecoration.StickyHeaderDecoration;
import com.lzx.demo.R;
import com.lzx.demo.adapter.InlineStickyTestAdapter;

public class InlineStickyHeaderFragment
        extends BaseDecorationFragment {

    private StickyHeaderDecoration decor;

    @Override
    protected void setAdapterAndDecor(RecyclerView list) {
        final InlineStickyTestAdapter adapter = new InlineStickyTestAdapter(this.getActivity());
        decor = new StickyHeaderDecoration(adapter, true);
        setHasOptionsMenu(true);

        mLRecyclerViewAdapter = new LRecyclerViewAdapter(adapter);
        list.setAdapter(mLRecyclerViewAdapter);
        list.addItemDecoration(decor, 1);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_clear_cache) {
            decor.clearHeaderCache();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
