package sg.edu.iss.trailblazelearnft04.Fragment;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import sg.edu.iss.trailblazelearnft04.Adapter.ItemListAdapter;
import sg.edu.iss.trailblazelearnft04.Model.ContributedItem;
import sg.edu.iss.trailblazelearnft04.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class StationUpdateFragment extends Fragment {
    private RecyclerView rvItemList;
    private RecyclerView.Adapter itemListAdapter;
    private RecyclerView.LayoutManager itemListManager;
    private TextView tvEmptyItemList;
    private String uid= FirebaseAuth.getInstance().getUid();
    private ArrayList<ContributedItem> ContributedItemList;

    public StationUpdateFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View fragmentView = inflater.inflate(R.layout.fragment_station_update, container, false);

        final String userID = uid;

        Bundle bundle = this.getArguments();
        String stationID =bundle.getString("stationId");

        ContributedItemList = new ArrayList<ContributedItem>();

        // Get contributed item list from firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String temp_ref = "items/" + stationID;
        DatabaseReference ref = database.getReference(temp_ref);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ContributedItemList.clear();
                for (DataSnapshot ContributedItemSnapshot : dataSnapshot.getChildren()) {
                    ContributedItem ci = ContributedItemSnapshot.getValue(ContributedItem.class);
                    ContributedItemList.add(ci);
                }

                rvItemList = (RecyclerView) fragmentView.findViewById(R.id.item_list);
                rvItemList.setHasFixedSize(false);

                itemListManager = new LinearLayoutManager(getActivity());
                rvItemList.setLayoutManager(itemListManager);

                itemListAdapter = new ItemListAdapter(ContributedItemList, getContext());
                rvItemList.setAdapter(itemListAdapter);

                tvEmptyItemList = (TextView) fragmentView.findViewById(R.id.tv_empty_item_list);
                tvEmptyItemList.setVisibility(ContributedItemList.size() == 0 ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return fragmentView;
    }


}
