package com.lzx.demo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lzx.demo.interfaces.Sectionizer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Florian Audigué on 27/04/2015.
 */
public class SectionedRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context mContext;
    private static final int SECTION_TYPE = 0;

    private boolean mValid = true;
    private int mSectionResourceId;
    private int mTextResourceId;
    private RecyclerView.Adapter mBaseAdapter;
    private SparseArray<Section> mSections = new SparseArray<>();
    private List<? extends Object> mListValues ;
    private Sectionizer mSectionizer;


    public SectionedRecyclerViewAdapter(Context context, int sectionResourceId, int textResourceId,
                                        RecyclerView.Adapter baseAdapter, Sectionizer sectionizer) {

        if(context == null) {
            throw new IllegalArgumentException("context cannot be null.");
        } else if(baseAdapter == null) {
            throw new IllegalArgumentException("baseAdapter cannot be null.");
        } else if(sectionizer == null) {
            throw new IllegalArgumentException("sectionizer cannot be null.");
        } else if(!isTextView(context, sectionResourceId, textResourceId)) {
            throw new IllegalArgumentException("textResourceId should be a TextView.");
        }


        mSectionResourceId = sectionResourceId;
        mTextResourceId = textResourceId;
        mBaseAdapter = baseAdapter;
        mContext = context;
        mSectionizer = sectionizer ;


        mBaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                mValid = mBaseAdapter.getItemCount()>0;
                notifyDataSetChanged();
            }

            @Override
            public void onItemRangeChanged(int positionStart, int itemCount) {
                mValid = mBaseAdapter.getItemCount()>0;
                notifyItemRangeChanged(positionStart, itemCount);
            }

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                mValid = mBaseAdapter.getItemCount()>0;
                notifyItemRangeInserted(positionStart, itemCount);
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                mValid = mBaseAdapter.getItemCount()>0;
                notifyItemRangeRemoved(positionStart, itemCount);
            }
        });
    }




    public static class SectionViewHolder extends RecyclerView.ViewHolder {

        public TextView title;

        public SectionViewHolder(View view, int mTextResourceid) {
            super(view);
            title = (TextView) view.findViewById(mTextResourceid);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int typeView) {
        if (typeView == SECTION_TYPE) {
            final View view = LayoutInflater.from(mContext).inflate(mSectionResourceId, parent, false);
            return new SectionViewHolder(view,mTextResourceId);
        }else{
            return mBaseAdapter.onCreateViewHolder(parent, typeView -1);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder sectionViewHolder, int position) {
        if (isSectionHeaderPosition(position)) {
            ((SectionViewHolder)sectionViewHolder).title.setText(mSections.get(position).title);
        }else{
            mBaseAdapter.onBindViewHolder(sectionViewHolder,sectionedPositionToPosition(position));
        }

    }

    @Override
    public int getItemViewType(int position) {
        return isSectionHeaderPosition(position)
                ? SECTION_TYPE
                : mBaseAdapter.getItemViewType(sectionedPositionToPosition(position)) +1 ;
    }

    private boolean isTextView(Context context, int layoutId, int textViewId) {
        View inflatedView = View.inflate(context, layoutId, null);
        View foundView = inflatedView.findViewById(textViewId);

        return foundView instanceof TextView;
    }



    //--------------- Section class -------------------

    public static class Section {
        int firstPosition;
        int sectionedPosition;
        CharSequence title;

        public Section(int firstPosition, CharSequence title) {
            this.firstPosition = firstPosition;
            this.title = title;
        }

        public CharSequence getTitle() {
            return title;
        }
    }
    //-------------------------------------------------


    public void setSections(List<? extends Object> listValues) {

        mListValues = listValues ;

        List<Section> listSection = findSections();

        SectionedRecyclerViewAdapter.Section[] dummy = new SectionedRecyclerViewAdapter.Section[listSection.size()];


        Section[] sections = listSection.toArray(dummy);
        mSections.clear();

        Arrays.sort(sections, new Comparator<Section>() {
            @Override
            public int compare(Section o, Section o1) {
                return (o.firstPosition == o1.firstPosition)
                        ? 0
                        : ((o.firstPosition < o1.firstPosition) ? -1 : 1);
            }
        });

        int offset = 0; // offset positions for the headers we're adding
        for (Section section : sections) {
            section.sectionedPosition = section.firstPosition + offset;
            mSections.append(section.sectionedPosition, section);
            ++offset;
        }

        notifyDataSetChanged();
    }


    public int sectionedPositionToPosition(int sectionedPosition) {
        if (isSectionHeaderPosition(sectionedPosition)) {
            return RecyclerView.NO_POSITION;
        }

        int offset = 0;
        for (int i = 0; i < mSections.size(); i++) {
            if (mSections.valueAt(i).sectionedPosition > sectionedPosition) {
                break;
            }
            --offset;
        }
        return sectionedPosition + offset;
    }

    public boolean isSectionHeaderPosition(int position) {
        return mSections.get(position) != null;
    }


    public int getIndexForPosition(int position) {
        int nSections = 0;

        for (int i = 0 ; i < mSections.size() ; i++){
            if (mSections.valueAt(i).firstPosition < position){
                nSections++;
            }
        }
        return position - nSections;

    }

    private List<Section> findSections() {

        List<Section> listSection = new ArrayList<>();

        int n = mListValues.size();
        LinkedHashMap<String, Integer> mSections = new LinkedHashMap<>();

        for(int i=0; i<n; i++) {
            String sectionName = mSectionizer.getSectionTitle(mListValues.get(i));

            if(!mSections.containsKey(sectionName)) {
                mSections.put(sectionName, i );
                listSection.add(new SectionedRecyclerViewAdapter.Section(i ,sectionName));

            }
        }

        return listSection ;
    }

    @Override
    public long getItemId(int position) {
        return isSectionHeaderPosition(position)
                ? Integer.MAX_VALUE - mSections.indexOfKey(position)
                : mBaseAdapter.getItemId(sectionedPositionToPosition(position));
    }

    @Override
    public int getItemCount() {
        return (mValid ? mBaseAdapter.getItemCount() + mSections.size() : 0);
    }

}
