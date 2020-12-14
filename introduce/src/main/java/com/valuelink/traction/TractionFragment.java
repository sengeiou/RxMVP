package com.valuelink.traction;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


/**
 * @author nathaniel
 */
public class TractionFragment extends Fragment {
    private int imageResource;

    private TractionFragment(int imageResource) {
        this.imageResource = imageResource;
    }

    public static TractionFragment newInstance(int imageResource) {
        return new TractionFragment(imageResource);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_traction, container, false);
        ImageView imageView = view.findViewById(R.id.traction_image_iv);
        imageView.setImageResource(imageResource);
        return view;
    }
}
