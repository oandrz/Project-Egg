package starbright.com.projectegg.features.ingredients;

import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.reactivex.annotations.NonNull;
import starbright.com.projectegg.R;

public class CartBottomSheetDialogFragment extends BottomSheetDialogFragment {

    public CartBottomSheetDialogFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.partial_bottom_sheet, container, false);
    }
}
