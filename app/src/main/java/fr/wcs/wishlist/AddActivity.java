package fr.wcs.wishlist;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

/**
 * A login screen that offers login via email/password.
 */
public class AddActivity extends AppCompatActivity {

    @Override
    protected void onCreate (final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        ImageButton imageWish = (ImageButton) findViewById(R.id.imageWish);
        imageWish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getLayoutInflater().inflate(R.layout.dialog_add, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(AddActivity.this);
                builder.setView(view);
                AlertDialog alert = builder.create();
                alert.show();

            }
        });
    }
}



