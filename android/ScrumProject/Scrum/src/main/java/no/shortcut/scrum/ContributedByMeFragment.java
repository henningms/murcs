package no.shortcut.scrum;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.microsoft.windowsazure.mobileservices.MobileServiceQuery;
import com.microsoft.windowsazure.mobileservices.QueryOrder;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;

import java.util.List;

import no.shortcut.scrum.models.Task;

/**
 * Created by HenningMosand on 31.10.13.
 */
public class ContributedByMeFragment extends Fragment {
    private ListView allTasksListView;
    private TaskAdapter taskAdapter;

    public ContributedByMeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.all_tasks, container, false);

        allTasksListView = (ListView) rootView.findViewById(R.id.allTasksListView);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        Application.getClient().getTable(Task.class).where().field("UserId").eq(Application.getClient().getCurrentUser().getUserId())
                .orderBy("CreatedAt", QueryOrder.Descending).execute(new TableQueryCallback<Task>() {
            @Override
            public void onCompleted(List<Task> tasks, int i, Exception e, ServiceFilterResponse serviceFilterResponse) {
                taskAdapter = new TaskAdapter(getActivity().getApplicationContext(), tasks);
                allTasksListView.setAdapter(taskAdapter);
            }
        });
    }
}
