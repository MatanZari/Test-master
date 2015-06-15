package fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.zari.matan.testapk.MainActivity;
import com.zari.matan.testapk.Page;
import com.zari.matan.testapk.PageActivity;
import com.zari.matan.testapk.R;
import com.zari.matan.testapk.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import adapters.SearchResultsAdapter;
import helper.FragmentUiLifeCycleHelper;

/**
 * Created by Matan on 4/19/2015
 */
public class
        SearchFragment extends Fragment implements View.OnClickListener, Task.DoIt, AdapterView.OnItemClickListener, FragmentUiLifeCycleHelper {

    public static final String TAG = SearchFragment.class.getName();
    EditText search_bar;
    ImageButton search_button;
    ListView search_results_list;
    Task taskSearch;
    private ArrayList<Page> pages;
    SearchResultsAdapter adapter;
    MainActivity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.search_fragment_layout, container, false);
        search_bar = (EditText) v.findViewById(R.id.search_bar);
        search_button = (ImageButton) v.findViewById(R.id.search_button);
        search_results_list = (ListView) v.findViewById(R.id.search_results);



        return v;
    }


    @Override
    public void onClick(View v) {
        String query = search_bar.getText().toString();
        if (!query.isEmpty()) {
            taskSearch = new Task(this);
            query = query.replace(" ", "%20");
            String search_url = " http://wolflo.com/walls/search/" + query + "?auth=off&limit=20&skip=0";
            taskSearch.execute(search_url);

        }


    }

    @Override
    public void send(String result) {
        if (result != null) {
            try {
                JSONArray searchResults = new JSONArray(result);
                if (pages == null)
                    pages = new ArrayList<>();
                else pages.clear();
                for (int i = 0; i < searchResults.length(); i++) {
                    JSONObject page = (JSONObject) searchResults.get(i);
                    String name = page.getString("name");
                    String cover = page.getString("cover");
                    String title = page.getString("title");
                    Page item = new Page(name, cover, title);
                    pages.add(item);
                }
                if (adapter == null) {
                    adapter = new SearchResultsAdapter(getActivity(), R.layout.search_result_item, pages);
                    search_results_list.setAdapter(adapter);
                    search_results_list.setOnItemClickListener(this);

                }else{
                    adapter.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), PageActivity.class);
        intent.putExtra("page_name", pages.get(position).getTitle());
        startActivity(intent);
    }




    @Override
    public void onPauseFragment() {
        if (activity == null)
            activity = (MainActivity) getActivity();

        else
        Toast.makeText(activity, TAG + " onPauseFragment", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFragmentResumed() {
        if (activity == null)
            activity = (MainActivity) getActivity();
        else
        Toast.makeText(activity,TAG+" onFragmentResumed",Toast.LENGTH_SHORT).show();
    }
}
