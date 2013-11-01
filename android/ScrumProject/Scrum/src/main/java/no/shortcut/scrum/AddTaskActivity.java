package no.shortcut.scrum;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.EditText;
import android.widget.Toast;

import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableOperationCallback;

import no.shortcut.scrum.models.Task;

public class AddTaskActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_task_activity);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment(), "task")
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_task, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_done:
                saveTask();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveTask()
    {
        PlaceholderFragment fragment = (PlaceholderFragment) getFragmentManager().findFragmentByTag("task");

        Task task = new Task();
        task.Text = fragment.taskDescription.getText().toString();

        Application.getClient().getTable(Task.class).insert(task, new TableOperationCallback<Task>() {
            @Override
            public void onCompleted(Task task, Exception e, ServiceFilterResponse serviceFilterResponse) {
                Toast.makeText(getApplicationContext(), "Task added", Toast.LENGTH_LONG).show();
                navigateUpTo(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        private EditText taskDescription;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_task_activity, container, false);

            taskDescription = (EditText) rootView.findViewById(R.id.newTaskEditText);

            return rootView;
        }
    }

}
