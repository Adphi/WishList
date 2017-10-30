package fr.wcs.wishlist;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import fr.wcs.wishlist.Controller.FirebaseHelper;

public class CreateAccountActivity extends AppCompatActivity {
    final String userName = "NameKey";
    final String userPassword = "PasswordKey";
    private boolean auth = false;
    private String mUserId = "UserKey";
    private String mEncrypt = "encrypt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        final EditText editTextUserName = findViewById(R.id.creerUserName);
        final EditText editTextCreePassword = findViewById(R.id.creerPassword);
        final EditText editTextConfirmerPassword = findViewById(R.id.confirmerUserPassword);
        // On recupere les Shared  Preferences
        final SharedPreferences sharedpreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        Button buttonCreate = (Button) findViewById(R.id.buttonCreerCompt);
        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                //On recupere le contenu des edit text
                final String userNameContent = editTextUserName.getText().toString();
                final String userPasswordContent = editTextCreePassword.getText().toString();
                final String userConfirmPasswordContent = editTextConfirmerPassword.getText().toString();// Snackbar si les champs ne sont pas remplis
                if (TextUtils.isEmpty(userNameContent) || TextUtils.isEmpty(userPasswordContent) || TextUtils.isEmpty(userConfirmPasswordContent)) {
                    Snackbar.make(view, "Veuillez remplir tous les champs", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                }else if (userPasswordContent.equals(userConfirmPasswordContent)) {
                    // Sinon on recupere tous les users
                    final DatabaseReference refUser = FirebaseHelper.getInstance().getReference("UserAuth");
                    refUser.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                                UserAuth userAuthValues = dsp.getValue(UserAuth.class);
                                //On compare le contenu des edit text avec Firebase grâce au user_name
                                if (userAuthValues.getUser_name().equals(userNameContent)) {
                                    Toast.makeText(CreateAccountActivity.this, "Pseudo déja utilisé", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                            // Utilisateur nouveau : le compte n'existe pas, on le créer !
                            UserAuth userAuth = new UserAuth(userNameContent, userPasswordContent);
                            userAuth.setUser_name(userNameContent);
                            userAuth.setUser_password(mEncrypt(userPasswordContent, "AES"));
                            int userUid = userName.hashCode();
                            refUser.child(String.valueOf(userUid)).setValue(userAuth);
                            // On enregistre dans les shared Preferences
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString(userName, userNameContent);
                            editor.putString(userPassword, userPasswordContent);
                            editor.apply();
                            Toast.makeText(CreateAccountActivity.this, "WishList te souhaite la bienvenue", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(CreateAccountActivity.this, MainActivity.class);
                            intent.putExtra("UserName", userName);
                            startActivity(intent);
                        }
                        // Encryptage du mot de passe
                        public String mEncrypt(String userPassword, String key) {
                            try {
                                Key clef = new SecretKeySpec(key.getBytes("ISO-8859-2"), "Blowfish");
                                Cipher cipher = Cipher.getInstance("Blowfish");
                                cipher.init(Cipher.ENCRYPT_MODE, clef);
                                return new String(cipher.doFinal(userPassword.getBytes()));
                            } catch (Exception e) {
                                return null;
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {}
                    });

                }else{
                    Snackbar.make(view, "veuillez verifier que les mots de passes sont identiques", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });
    }
}
