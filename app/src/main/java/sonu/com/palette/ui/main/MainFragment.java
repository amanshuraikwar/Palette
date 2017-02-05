package sonu.com.palette.ui.main;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import sonu.com.palette.R;
import sonu.com.palette.helper.HidingScrollListener;
import sonu.com.palette.helper.PalettesAdapter;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainFragment extends Fragment implements MainMvpFragmentView{

    @BindView(R.id.palettesRecyclerView)
    RecyclerView palettesRecyclerView;

    private Context mContext;
    private MainMvpFragmentPresenter mainMvpFragmentPresenter;

    public MainFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this,v);
        mContext = getActivity();
        mainMvpFragmentPresenter = new MainFragmentPresenter(mContext);

        init();
        return v;
    }

    private void init() {
        palettesRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        palettesRecyclerView.setAdapter(
                new PalettesAdapter(
                        mContext,
                        mainMvpFragmentPresenter.getPalettesFromDb()));
        palettesRecyclerView.addOnScrollListener(new HidingScrollListener() {
            @Override
            public void onHide() {
                ((MainMvpView)mContext).hideFab();
            }

            @Override
            public void onShow() {
                ((MainMvpView)mContext).showFab();
            }
        });
    }
}
