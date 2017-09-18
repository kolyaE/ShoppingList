package kolyae318rub.shoplist;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.EditText;

public class SaveNameDialog extends DialogFragment {
    public static String slName;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final EditText newShopListName = new EditText(getActivity());
        return builder.setTitle(R.string.NewShopListName)
                .setMessage(R.string.EnterNewShopListName)
                .setView(newShopListName)
                .setPositiveButton(R.string.Ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!newShopListName.getText().toString().equals("")) {
                            slName = newShopListName.getText().toString();
                            ((ShopListActivity) getActivity()).savingList();
                            getActivity().finish();
                        }
                    }
                })
                .setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).create();

    }
}
