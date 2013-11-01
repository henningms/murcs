package no.shortcut.scrum;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.microsoft.windowsazure.mobileservices.*;
import com.mukesh.ui.HorizontalView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import no.shortcut.scrum.models.User;

public class MainActivity extends Activity {

    private Context context;

    private HorizontalView gallery;
    private UserImageAdapter imageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        try {
            Application.mClient = new MobileServiceClient(
                    "https://murcs.azure-mobile.net/",
                    "DKnzMvQWhglLdTMeObLCxixqLMKcaG57",
                    this);

            authenticate();
        } catch (Exception ex) {

        }

        ActionBar actionBar = getActionBar();

        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        ActionBar.Tab tab = actionBar.newTab()
                .setText("ALL TASKS")
                .setTabListener(new TabListener<AllTasksFragment>(
                        this, "all", AllTasksFragment.class));
        actionBar.addTab(tab);

        tab = actionBar.newTab()
                .setText("CONTRIBUTED BY ME")
                .setTabListener(new TabListener<ContributedByMeFragment>(
                        this, "me", ContributedByMeFragment.class));
        actionBar.addTab(tab);

        gallery = (HorizontalView) findViewById(R.id.gallery);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_add_task:
                launchAddTask();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void authenticate() {
        // Login using the Google provider.
        Application.getClient().login(MobileServiceAuthenticationProvider.Facebook,
                new UserAuthenticationCallback() {


                    @Override
                    public void onCompleted(MobileServiceUser user,
                                            Exception exception, ServiceFilterResponse response) {


                        if (exception == null) {
                            checkUsers();
                            List<Pair<String,String>> params = new ArrayList<Pair<String, String>>();

                            params.add(new Pair<String, String>("status", "online"));
                            Application.getClient().invokeApi("user_status", "PUT", params, new ApiJsonOperationCallback() {
                                @Override
                                public void onCompleted(JsonElement jsonElement, Exception e, ServiceFilterResponse serviceFilterResponse) {
                                    checkUsers();
                                }
                            });
                        } else {
                            Toast.makeText(context, "Login failure", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void checkUsers()
    {
        MobileServiceClient client = Application.getClient();

        client.getTable(User.class).execute(new TableQueryCallback<User>() {
            @Override
            public void onCompleted(List<User> users, int i, Exception e, ServiceFilterResponse serviceFilterResponse) {
                if (e != null)
                {
                    Toast.makeText(context, "EXCEPTION: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
                else
                {
                    imageAdapter = new UserImageAdapter(context, users);
                    gallery.setAdapter(imageAdapter);
                }
            }
        });

    }

    private void launchAddTask()
    {
        Intent intent = new Intent(this, AddTaskActivity.class);
        startActivity(intent);
    }

}
