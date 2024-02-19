package edu.ucsd.cse110.secards.app.ui.cardlist.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import edu.ucsd.cse110.secards.app.MainViewModel;
import edu.ucsd.cse110.secards.lib.domain.Flashcard;
import edu.ucsd.cse110.secards.app.databinding.FragmentDialogCreateCardBinding;

public class CreateCardDialogFragment extends DialogFragment {

    private MainViewModel activityModel;
    private FragmentDialogCreateCardBinding view;

    CreateCardDialogFragment() {

    }

    public static CreateCardDialogFragment newInstance() {
        var fragment = new CreateCardDialogFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        var modelOwner = requireActivity();
        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
        this.activityModel = modelProvider.get(MainViewModel.class);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        this.view = FragmentDialogCreateCardBinding.inflate(getLayoutInflater());

        return new AlertDialog.Builder(getActivity())
                .setTitle("New Task")
                .setMessage("Please provide the new Task text")
                .setView(view.getRoot())
                .setPositiveButton("Create", this::onPositiveButtonClick)
                .setNegativeButton("Cancel", this::onNegativeButtonClick)
                .create();
    }

    private void onNegativeButtonClick(DialogInterface dialog, int which) {
        dialog.cancel();
    }

    private void onPositiveButtonClick(DialogInterface dialog, int which) {
        var front = view.cardFrontEditText.getText().toString();

        var card = new Flashcard(null, front, "Task", -1);

        if (view.appendRadioButton.isChecked()) {
            activityModel.append(card);
        }
        else if (view.prependRadioButton.isChecked()) {
            activityModel.prepend(card);
        } else {
            throw new IllegalStateException("No radio button is checked.");
        }

        dialog.dismiss();
    }
}
