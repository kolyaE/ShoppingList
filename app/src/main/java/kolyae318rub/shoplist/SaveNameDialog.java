package kolyae318rub.shoplist;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import kolyae318rub.shoplist.data.DB;

public class SaveNameDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final EditText newShopListName = new EditText(getActivity());
        return builder.setTitle(R.string.NewShopListName)
                .setView(newShopListName)
                .setPositiveButton(R.string.Ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!newShopListName.getText().toString().isEmpty()) {
                            String slName = newShopListName.getText().toString();
                            new DB(getActivity().getApplicationContext())
                                    .putListIntoDB(ShopListActivity.shopList, ShopListActivity.status, slName);
                            startActivity(new Intent(getActivity(), MainActivity.class));
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
