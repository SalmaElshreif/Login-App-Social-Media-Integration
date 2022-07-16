package net.salma.social_media_integration;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

public class FacebookPage extends AppCompatActivity {

    CircleImageView FimageView;
    TextView Fname , Femail;
    Button logoutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook_page);

        FimageView = findViewById(R.id.fbimage);
        Fname = findViewById(R.id.fbname);
        Femail = (TextView)findViewById(R.id.fbemail);
        logoutBtn = findViewById(R.id.logout);

        AccessToken accessToken = AccessToken.getCurrentAccessToken();

        GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                        try {
                            String fullName = object.getString("name");
                            Fname.setText(fullName);

                            String url = object.getJSONObject("picture").getJSONObject("data").getString("url");
                            Picasso.get().load(url).into(FimageView);

                            String email = object.getString("email");
                            Femail.setText(email);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link,picture.type(large),email");
        request.setParameters(parameters);
        request.executeAsync();

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logOut();
                startActivity(new Intent(FacebookPage.this , LoginPage.class));
                finish();
            }
        });

    }
}

