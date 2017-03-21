package scott.spelling.view;

import android.content.*;
import android.view.*;
import android.widget.*;

import org.greenrobot.eventbus.*;

import java.util.*;

import scott.spelling.*;

import static android.content.Context.*;
import static android.graphics.Typeface.*;
import static scott.spelling.R.id.*;
import static scott.spelling.R.layout.*;

public class ChooseListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> grades;
    private Map<String, List<String>> testNames;

    public ChooseListAdapter(Context context, List<String> grades, Map<String, List<String>> testNames) {
        this.context = context;
        this.grades = grades;
        this.testNames = testNames;
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return testNames.get(grades.get(listPosition))
                        .get(expandedListPosition);
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final String expandedListText = (String) getChild(listPosition, expandedListPosition);
        if (convertView == null) { convertView = ((LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.list_item, null); }
        TextView txtListName = (TextView) convertView.findViewById(expandedListItem);
        txtListName.setText(expandedListText);
        txtListName.setOnClickListener(new ListSelected());
        return convertView;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String listTitle = (String) getGroup(listPosition);
        if (convertView == null) { convertView = ((LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(list_group, null); }
        TextView txtGrade = (TextView) convertView.findViewById(R.id.listTitle);
        txtGrade.setTypeface(null, BOLD);
        txtGrade.setText(listTitle);
        return convertView;
    }
    @Override public long getChildId(int listPosition, int expandedListPosition) { return expandedListPosition; }
    @Override public int getChildrenCount(int listPosition) { return testNames.get(grades.get(listPosition)).size(); }
    @Override public Object getGroup(int listPosition) { return grades.get(listPosition); }
    @Override public int getGroupCount() { return grades.size(); }
    @Override public long getGroupId(int listPosition) { return listPosition; }
    @Override public boolean hasStableIds() { return false; }
    @Override public boolean isChildSelectable(int listPosition, int expandedListPosition) { return true; }

    class ListSelected implements View.OnClickListener {
        @Override public void onClick(View v) {
            EventBus.getDefault().post(new ListChangedEvent((String) ((TextView) v).getText()));
        }
    }
}