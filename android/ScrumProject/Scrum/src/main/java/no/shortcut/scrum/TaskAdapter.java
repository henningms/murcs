package no.shortcut.scrum;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import java.util.List;

import no.shortcut.scrum.models.Task;

/**
 * Created by HenningMosand on 01.11.13.
 */
public class TaskAdapter extends BaseAdapter {

    private Context context;
    private List<Task> tasks;
    private LayoutInflater layoutInflater;

    public TaskAdapter(Context context, List<Task> tasks)
    {
        this.context = context;
        this.tasks = tasks;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return tasks.size();
    }

    @Override
    public Task getItem(int i) {
        return tasks.get(i);
    }

    @Override
    public long getItemId(int i) {
        return getItem(i).Id;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;

        if (convertView == null) {

            convertView = layoutInflater.inflate(R.layout.task_list_item, null);
            holder = new ViewHolder();
            holder.textView = (TextView) convertView.findViewById(R.id.taskTextView);

            convertView.setTag(holder);

        } else {

            holder = (ViewHolder) convertView.getTag();
        }

        holder.textView.setText(getItem(i).Text);

        return convertView;
    }

    public static class ViewHolder {
        TextView textView;
    }
}
