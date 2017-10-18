package kolyae318rub.shoplist;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;


public class SavingDialog extends DialogFragment
{

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        return dialog.setTitle(R.string.DialogTitle)
                .setMessage(R.string.DialogText)
                .setCancelable(true)
                .setPositiveButton(R.string.PositiveButton, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new SaveNameDialog().show(getFragmentManager(), "saveNameDialog");
                    }
                })
                .setNegativeButton(R.string.NegativeButton, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(getActivity(), MainActivity.class));
                    }
                })
                .create();
    }
}
